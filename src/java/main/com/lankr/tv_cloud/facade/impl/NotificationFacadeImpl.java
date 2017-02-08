/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月14日
 * 	@modifyDate 2016年6月14日
 *  
 */
package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import org.eclipse.core.internal.events.NotificationManager;

import com.lankr.orm.mybatis.mapper.NotificaitonMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.NotificationFacade;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Notification;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;

/**
 * @author Kalean.Xiang
 *
 */

public class NotificationFacadeImpl extends FacadeBaseImpl implements
		NotificationFacade {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#add(java.lang.Object)
	 */
	public Notification save(Notification t) {
		try {
			notificaitonMapper.saveNotification(t);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#del(java.lang.Object)
	 */
	@Override
	public ActionMessage del(Notification t) {
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#getByUuid(java.lang.String)
	 */
	@Override
	public Notification getByUuid(String uuid) {
		try {
			Notification notification = notificaitonMapper.getByUuid(uuid);
			return notification;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#update(java.lang.Object)
	 */
	@Override
	public ActionMessage update(Notification t) {
		try {
			int effect = notificaitonMapper.updateNotification(t);;
			if (effect == 1) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("插入记录失败");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.NotificationFacade#
	 * addActivityResourceChangedNotification(com.lankr.tv_cloud.model.Activity,
	 * java.lang.String)
	 */
	@Override
	public Notification addActivityResourceChangedNotification(
			Activity activity, User user, String body) {
		if (!BaseModel.hasPersisted(activity)) {
			//return ActionMessage.failStatus("empty value");
			return null;
		}
		Notification notify = new Notification();
		notify.setReferId(activity.getId());
		notify.setUser(user);
		notify.setReferType(Notification.TYPE_ACTIVITY);
		notify.setSign(Notification.SIGN_CONTENT_UPDATED);
		notify.setUuid(Tools.getUUID());
		notify.setSendDate(Tools.getCurrentDate());
		notify.setBody(body);
		notify.setStatus(BaseModel.APPROVED);
		notify.setIsActive(BaseModel.ACTIVE);
		return save(notify);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.NotificationFacade#addBroadcastNotification
	 * (com.lankr.tv_cloud.model.Broadcast, java.lang.String)
	 */
	@Override
	public ActionMessage addBroadcastNotification(Broadcast broadcast,
			String body) {
		if (!BaseModel.hasPersisted(broadcast)) {
			return ActionMessage.failStatus("empty value");
		}
		Notification notify = new Notification();
		notify.setReferId(broadcast.getId());
		notify.setReferType(Notification.TYPE_BROADCAST);
		notify.setSign(Notification.SIGN_IMMEDIATELY);
		notify.setUuid(Tools.getUUID());
		notify.setSendDate(Tools.getCurrentDate());
		notify.setBody(body);
		notify.setStatus(Notification.STATUS_PROCESSING);
		notify.setIsActive(BaseModel.ACTIVE);
		return add(notify);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.impl.FacadeBaseImpl#namespace()
	 */
	@Override
	protected String namespace() {
		return NotificationManager.class.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.NotificationFacade#broadcastRecentlyNotification
	 * (com.lankr.tv_cloud.model.Broadcast)
	 */
	@Override
	public Notification broadcastRecentlyNotification(Broadcast cast) {
		Notification notification;
		try {
			notification = notificaitonMapper.broadcastRecentlyNotification(cast);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return notification;
	}

	@Override
	public ActionMessage add(Notification t) {
		try {
			int effect = notificaitonMapper.saveNotification(t);;
			if (effect == 1) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("插入记录失败");
	}
	
	@Override
	public Pagination<Notification> searchActivitieMessageForDatatable(
							String activityUuid, String queryKey, int startPage, int pageSize) {
		queryKey = filterSQLSpecialChars(queryKey);
		Activity activity = activityMapper.getActivityByUuid(activityUuid);
		int activityId = 0;
		if (null != activity) {
			activityId = activity.getId();
		}
		String total_sql =  " select count(*) from notification n left join user u on n.userId = u.id " + 
							" where n.isActive = 1 and n.referType = 1 and  n.sign = 1 " + 
							" and n.referId = " + activityId +
							" and (n.body like '%" + queryKey + "%' or u.nickname like '%" + queryKey + 
							"%' or u.pinyin like '%" + queryKey + "%' or u.phone like '%" + queryKey + "%')";
		Pagination<Notification> pagination = initPage(total_sql, startPage, pageSize);
		List<Notification> activityMessages= notificaitonMapper
				.searchActivitieMessageForDatatable(activityId, queryKey,
						startPage, pageSize);
		pagination.setResults(activityMessages);
		return pagination;
	}

}
