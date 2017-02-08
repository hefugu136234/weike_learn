package com.lankr.tv_cloud.cache.lru;

import java.util.LinkedHashMap;

final class LruHashMap<K, V> extends LinkedHashMap<K, V> {

	private final int capacity;

	public LruHashMap(int capacity) {
		super(capacity, 0.75f, true);
		this.capacity = capacity;
	}
}
