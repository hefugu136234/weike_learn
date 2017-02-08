package com.lankr.tv_cloud.web;

import java.util.Objects;

import com.lankr.tv_cloud.cache.lru.LruCache;
import com.lankr.tv_cloud.facade.UserFacade;
import com.lankr.tv_cloud.model.User;

public class GlobalUserCache extends LruCache<Integer, User> {

	private static GlobalUserCache cache;

	private GlobalUserCache(UserFacade userFacade) {
		Objects.requireNonNull(userFacade, "userFacade == null");
		this.userFacade = userFacade;
	}

	private UserFacade userFacade;

	public synchronized static GlobalUserCache getInstance(
			UserFacade userFacade) {
		if (cache == null) {
			cache = new GlobalUserCache(userFacade);
		}
		return cache;
	}

	@Override
	protected User create(Integer key) {
		if (key == null)
			return null;
		return userFacade.getUserById(key);
		
	}

	//新老用户交替权限接力
	@Override
	public void onReplaced(User newValue, User oldValue) {
		super.onReplaced(newValue, oldValue);
		if (newValue == null || oldValue == null)
			return;
		newValue.setHandlerReference(oldValue.getHandlerReference());
	}

	@Override
	protected int configMaxMemorySize() {
		return 5000;
	}

}
