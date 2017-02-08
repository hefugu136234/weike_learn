/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月13日
 * 	@modifyDate 2016年6月13日
 *  
 */
package com.lankr.tv_cloud.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.BroadcastFacade;
import com.lankr.tv_cloud.facade.MessageFacade;
import com.lankr.tv_cloud.facade.NotificationFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.WxModelMessageFacade;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.BroadcastUser;
import com.lankr.tv_cloud.model.Notification;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.AdminWebInterceptor;
import com.lankr.tv_cloud.web.AdminWebInterceptor.AdminActiveListener;

/**
 * @author Kalean.Xiang
 *
 */
public class BroadcastNotificationTask extends AbstractTask {
	
	protected static Log logger = LogFactory.getLog(BroadcastNotificationTask.class);

	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年6月14日
	 * @modifyDate 2016年6月14日
	 * 
	 */
	/**
	 * 临近1小时执行 通知事件
	 * */
	private static final long NOTIFICATION_TRGGER_TIME_LINE = TimeUnit.HOURS
			.toMillis(1);

	private transient boolean mRefreshable = false;

	private final Map<Integer, Broadcast> processing_cache = new HashMap<Integer, Broadcast>();

	private final Map<Integer, Broadcast> finished_cache = new HashMap<Integer, Broadcast>();

	private long mRecentlyTime = Long.MAX_VALUE;

	@Autowired
	protected BroadcastFacade broadcastFacade;

	@Autowired
	protected NotificationFacade notificationFacade;
	
	@Autowired
	private WxModelMessageFacade wxModelMessageFacade;
	
	@Autowired
	private WxModelMessageFacade wxUndueModelMessageFacade;
	
	@Autowired
	protected MessageFacade messageFacade;
	
	public WxModelMessageFacade getModelMessageFacade(){
//		if(Config.env.equals(Config.Environment.PRODUCT.getValue())){
//			return wxUndueModelMessageFacade;
//		}
		return wxModelMessageFacade;
	}

	public void schedule() {
		if (mRefreshable || loadable()) {
			refresh();
		}
	}

	private synchronized void refresh() {
		refreshTimeTrigger(Tools.getCurrentDate());
		if (loadable()) {
			Date target = new Date(Tools.getCurrentDate().getTime()
					+ NOTIFICATION_TRGGER_TIME_LINE);
			List<Broadcast> casts = broadcastFacade
					.searchBeginingBroadcastsInterval(Tools.getCurrentDate(),
							target);
			if (casts != null) {
				preProcessing(casts);
			}
			refreshTimeTrigger(target);
		}
	}

	private void refreshTimeTrigger(Date line) {
		Broadcast cast = broadcastFacade.searchRecentBeginBroadcast(line);
		if (cast != null) {
			try {
				mRecentlyTime = cast.getStartDate().getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			mRecentlyTime = Long.MAX_VALUE;
		}
	}

	// 清除缓存
	// private void evict(){
	// }

	private void preProcessing(List<Broadcast> casts) {
		if (Tools.isEmpty(casts))
			return;
		for (Broadcast broadcast : casts) {
			if (addToProcessing(broadcast)) {
				processing(broadcast);
			}
		}
	}

	private boolean addToProcessing(Broadcast cast) {
		if (!BaseModel.hasPersisted(cast) || !cast.apiUseable())
			return false;
		boolean addable = !processing_cache.containsKey(cast.getId());
		if (addable) {
			addable = !finished_cache.containsKey(cast.getId());
		}
		if (addable) {
			processing_cache.put(cast.getId(), cast);
		}
		return addable;
	}

	/**
	 * @he 2016-06-22
	 * 修改为正式版本
	 */
	// 执行发送
	private void processing(final Broadcast cast) {
		// 判断是否已经发送
		Notification n = notificationFacade.broadcastRecentlyNotification(cast);
		if (n != null && n.hasSend()) {
			return;
		}
		// 执行发送
		execute(new Task() {

			// notification 中添加发送中的消息
			@Override
			public boolean onBefore() {
				//TODO set body value
				String body = "直播发送模版消息测试";
				ActionMessage message = notificationFacade.addBroadcastNotification(cast, body);
				return message.isSuccess();
			}

			// 执行发送
			@Override
			public void process() {
				Notification notification = notificationFacade.broadcastRecentlyNotification(cast);
				if(null == notification){
					logger.info("notification is null");
					return ;
				}
				//分批读取数据
				List<BroadcastUser> users = broadcastFacade.searchBroadcastUserForMessage(0, cast);
				int lastUserId;	
				while(users.size() > 0){
					for(BroadcastUser broadcastUser : users){
						//发送消息
						Status status = Status.FAILURE;
						if(null != broadcastUser.getUser()){
							// TODO 这是测试直播是否接通的接口，以后会改
							status = getModelMessageFacade().beforeHourBroadcast(cast, broadcastUser.getUser());
						}
						//模版消息发送成功，记录发送
						if(Status.SUCCESS == status){
							messageFacade.addNotificationMessage(notification, broadcastUser.getUser());
						}
					}
					// 查询新数据
					lastUserId = users.get(users.size()-1).getId();	
					users = broadcastFacade.searchBroadcastUserForMessage(lastUserId, cast);
				}
			}

			// notification 中添加发送完成的消息
			// 执行完毕
			@Override
			public void onFinished() {
				syncFinished(cast);

				// 更新notification的状态为已发送
				Notification notification = notificationFacade.broadcastRecentlyNotification(cast); 
				if(null == notification) 
					return;
				notification.setStatus(Notification.STATUS_FINISHED);
				notificationFacade.update(notification);
			}
		});
	}

	private synchronized void syncFinished(Broadcast cast) {
		// 进行的扔到完成的
		processing_cache.remove(cast.getId());
		finished_cache.put(cast.getId(), cast);
	}

	private boolean loadable() {
		return mRecentlyTime - System.currentTimeMillis() <= NOTIFICATION_TRGGER_TIME_LINE;
	}

	public void registerActive() {
		AdminWebInterceptor
				.registerAdminActiveListener(new AdminActiveListener() {
					@Override
					public void onActive(boolean busy) {
						if (busy) {
							onAdminBusy();
						} else {
							onAdminIdle();
						}
					}
				});
		// 第一次加载
		refresh();
	}

	private void onAdminBusy() {
		mRefreshable = true;
	}

	private void onAdminIdle() {
		mRefreshable = false;
	}

}
