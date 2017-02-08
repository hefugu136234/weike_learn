package com.lankr.tv_cloud.cache.lru;

public interface Cache<K, V> {

	V get(K key);

	V put(K key, V value);

	V remove(K key);
	
	void clear();

	int getMaxSize();

	int getMemorySize();

	
}
