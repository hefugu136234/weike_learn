/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月29日
 * 	@modifyDate 2016年6月29日
 *  
 */
package com.lankr.tv_cloud.qr;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lankr.tv_cloud.cache.lru.LruCache;
import com.lankr.tv_cloud.codes.CodeProvider;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.QrAuthFacade;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.TvQrAuth;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.utils.Tools;

@Service(value = "qrLoginManagement")
public class QrLoginManagement {

	private final QrAuth authCaches = new QrAuth();

	// 二维码10分钟到期
	private final long QR_TIMEOUT_MAX = TimeUnit.MINUTES.toMillis(10);

	@Autowired
	private QrAuthFacade qrAuthFacade;

	@Autowired
	protected CodeProvider codeProvider;

	private final Map<String, String> channel = new ConcurrentHashMap<String, String>();

	public long getExpiredTime() {
		return QR_TIMEOUT_MAX;
	}

	private class QrAuth extends LruCache<String, TvQrAuth> {

		@Override
		protected int configMaxMemorySize() {
			return 5000;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.lankr.tv_cloud.cache.lru.LruCache#create(java.lang.Object)
		 */
		@Override
		protected TvQrAuth create(String key) {
			return null;
		}

	}

	// for tv
	// 相同的ip和device 会缓存1分钟时间
	public ActionMessage<TvQrAuth> product(String ip, String device) {
		device = Tools.subString(device, 50);
		String client = client(ip, device);
		String uuid = channel.get(client);
		TvQrAuth auth = null;
		if (!Tools.isBlank(uuid)) {
			auth = authCaches.get(uuid, TimeUnit.MINUTES.toMillis(1), false);
			if (auth == null) {
				channel.remove(client);
				authCaches.remove(uuid);
			} else {
				if (auth.getStatus() == auth.STATUS_CREATED
						|| auth.getStatus() == auth.STATUS_SCANNED) {
					return ActionMessage.successStatus(auth);
				} else {
					channel.remove(client);
					authCaches.remove(uuid);
				}
			}
		}
		auth = new TvQrAuth();
		auth.setIp(ip);
		auth.setDevice(device);
		auth.setUuid(Tools.getUUID());
		auth.setKey("key_" + Tools.getUUID());
		auth.setCreateDate(Tools.getCurrentDate());
		auth.setModifyDate(Tools.getCurrentDate());
		auth.setStatus(TvQrAuth.STATUS_CREATED);
		auth.setIsActive(TvQrAuth.ACTIVE);
		ActionMessage action = qrAuthFacade.addQrAuth(auth);
		if (action.isSuccess()) {
			channel.put(client(ip, device), auth.getUuid());
			authCaches.put(auth.getUuid(), auth);
			action.setData(auth);
		}
		return action;
	}

	private String client(String ip, String device) {
		String join = ip + "&" + device;
		return Tools.getSHA1String(join);
	}

	// for tv or auth client
	public ActionMessage<TvQrAuth> peekTvAuth(String ip, String device,
			String uuid) {
		device = Tools.subString(device, 50);
		ActionMessage message = codeProvider.code(200).getActionMessage();
		TvQrAuth auth = authCaches.get(uuid, false);
		if (auth == null) {
			auth = qrAuthFacade.getQrAuthByUuid(uuid);
		}
		if (auth == null) {
			return codeProvider.code(404).getActionMessage();
		}
		synchronized (auth.getDefaultLock()) {
			if (!ip.equals(auth.getIp()) || !device.equals(auth.getDevice())) {
				return codeProvider.code(400).getActionMessage();
			}
			if (System.currentTimeMillis() - auth.getCreateDate().getTime() > QR_TIMEOUT_MAX) {
				authCaches.remove(uuid);
				return codeProvider.code(401).getActionMessage();
			}
			// 客户端已经扫描
			if (auth.getStatus() == TvQrAuth.STATUS_SCANNED) {
				return codeProvider.code(201).getActionMessage();
			}
			if (auth.getStatus() == TvQrAuth.STATUS_AUTHED) {
				message = codeProvider.code(208).getActionMessage();
				// 拿到数据之后更新为已经完成
				message.setData(auth);
				auth.setStatus(TvQrAuth.STATUS_FINISHED);
				qrAuthFacade.update(auth);
			}
		}
		return message;
	}

	// for auth client
	public ActionMessage notifyTvAuth(String uuid, User user) {
		TvQrAuth auth = authCaches.get(uuid, false);
		if (auth == null) {
			auth = qrAuthFacade.getQrAuthByUuid(uuid);
		}
		if (auth == null) {
			return codeProvider.code(404).getActionMessage();
		}
		if (System.currentTimeMillis() - auth.getCreateDate().getTime() > QR_TIMEOUT_MAX) {
			authCaches.remove(uuid);
			return codeProvider.code(401).getActionMessage();
		}
		if (auth.getStatus() == TvQrAuth.STATUS_AUTHED
				|| auth.getStatus() == TvQrAuth.STATUS_FINISHED
				|| auth.getStatus() == TvQrAuth.STATUS_GRANTED) {
			return codeProvider.code(403).getActionMessage();
		}
		if (!BaseModel.hasPersisted(user)) {
			auth.setStatus(TvQrAuth.STATUS_SCANNED);
			auth.setLastScanDate(Tools.getCurrentDate());
			qrAuthFacade.update(auth);
		} else {
			ActionMessage action = tvUserCheck(user);
			if (!action.isSuccess()) {
				return action;
			}
			auth.setUser(user);
			auth.setAuthDate(Tools.getCurrentDate());
			auth.setStatus(TvQrAuth.STATUS_AUTHED);
			qrAuthFacade.update(auth);
		}
		return ActionMessage.successStatus();
	}

	// for tv
	public ActionMessage<User> getTvUserByTicket(String ticket, String device,
			String ip) {
		TvQrAuth auth = qrAuthFacade.getAuthByTicket(ticket);
		if (auth == null) {
			return codeProvider.code(510).getActionMessage();
		}
		if (auth.getStatus() == auth.STATUS_GRANTED) {
			return codeProvider.code(513).getActionMessage();
		}
		Date authDate = auth.getAuthDate();
		if (authDate != null) {
			// 超过30未被获取ticket 则判为失效
			if (System.currentTimeMillis() - authDate.getTime() > TimeUnit.SECONDS
					.toMillis(30)) {
				return codeProvider.code(512).getActionMessage();
			}
		}
		ActionMessage action = tvUserCheck(auth.getUser());
		if (!action.isSuccess()) {
			return action;
		}
		auth.setStatus(TvQrAuth.STATUS_GRANTED);
		qrAuthFacade.update(auth);
		return ActionMessage.successStatus(auth.getUser());
	}

	public ActionMessage tvUserCheck(User user) {
		if (user == null) {
			return codeProvider.code(-2001).getActionMessage();
		}
		UserReference ref = user.getUserReference();
		if (!user.apiUseable() || ref == null || !ref.apiUseable()) {
			return codeProvider.code(-2003).getActionMessage();
		}
		Role r = ref.getRole();
		if (!r.isBoxUser()) {
			return codeProvider.code(-2003).getActionMessage();
		}

		if (!ref.isDateValid()) {
			return codeProvider.code(-2010).getActionMessage();
		}
		return ActionMessage.successStatus(null);
	}
}
