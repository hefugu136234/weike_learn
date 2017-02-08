package com.lankr.tv_cloud.cache;

import java.util.concurrent.locks.ReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;

public class EhCache implements Cache {

	private final ReadWriteLock readWriteLock = new DummyReadWriteLock();
	
	private String id;

	public EhCache(String id) {
		this.id = id;
	}

	@Override
	public void clear() {

	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Object getObject(Object arg0) {
		
		return null;
	}

	@Override
	public ReadWriteLock getReadWriteLock() {

		return readWriteLock;
	}

	@Override
	public int getSize() {

		return 0;
	}

	@Override
	public void putObject(Object arg0, Object arg1) {
		CacheKey key = (CacheKey)arg0;
		
		
	}

	@Override
	public Object removeObject(Object arg0) {
		return null;
	}

}
