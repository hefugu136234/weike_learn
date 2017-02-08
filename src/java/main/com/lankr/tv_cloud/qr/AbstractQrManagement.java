/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年7月22日
 * 	@modifyDate 2016年7月22日
 *  
 */
package com.lankr.tv_cloud.qr;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.tv_cloud.cache.lru.LruCache;
import com.lankr.tv_cloud.codes.CodeProvider;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.QrAuthFacade;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.utils.Tools;

/**
 * @author Kalean.Xiang
 *
 */
public abstract class AbstractQrManagement<T extends BaseModel> {

	protected LruCache<Serializable, T> qrCache = null;

	protected static final LruCache<String, String> channel = new LruCache<String, String>() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see com.lankr.tv_cloud.cache.lru.LruCache#configMaxMemorySize()
		 */
		@Override
		protected int configMaxMemorySize() {
			return 20000;
		}
	};

	@Autowired
	protected QrAuthFacade qrAuthFacade;

	@Autowired
	protected CodeProvider codeProvider;

	// 缓存中的是否可用
	public abstract boolean cacheValid(T t);

	public abstract ActionMessage<T> onPersist(String ip, String device);

	public long getExpiredTime() {
		return TimeUnit.MINUTES.toMillis(10);
	}

	// 默认缓存1分钟
	protected long itemCacheMillis() {
		return TimeUnit.MINUTES.toMillis(1);
	}

	// 生产二维码
	public ActionMessage<T> product(String ip, String device) {
		device = Tools.subString(device, 50);
		String client = client(ip, device);
		String uuid = channel.get(client);
		T t = null;
		if (!Tools.isBlank(uuid)) {
			t = qrCache.get(uuid, itemCacheMillis(), false);
			if (t == null) {
				channel.remove(client);
				qrCache.remove(uuid);
			} else {
				if (cacheValid(t)) {
					return ActionMessage.successStatus(t);
				} else {
					channel.remove(client);
					qrCache.remove(uuid);
				}
			}
		}
		ActionMessage<T> action = onPersist(ip, device);
		if (action.isSuccess()) {
			t = action.getData();
			if (t != null) {
				channel.put(client, t.getUuid());
				qrCache.put(t.getUuid(), t);
			}
		}
		return action;
	}

	protected String client(String ip, String device) {
		String join = ip + "&" + device + hashCode();
		return Tools.getSHA1String(join);
	}

	// protected String clientSalt() {
	// return "";
	// }

	// protected String client(String ip, String device, int referType) {
	// // TODO Auto-generated method stub
	// String join = ip + "&" + device + "&" + referType;
	// return Tools.getSHA1String(join);
	// }

	public ActionMessage tvUserCheck(User user) {
		if (user == null) {
			return codeProvider.code(-2001).getActionMessage();
		}
		UserReference ref = user.getUserReference();
		if (!user.apiUseable() || ref == null) {
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
