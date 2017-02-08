package com.lankr.tv_cloud.cache.lru;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version 1.0
 * @author Kalean.Xiang
 * */
public class LruCache<K, V> implements Cache<K, V> {

	private static Log logger = LogFactory.getLog(LruCache.class);

	public static enum RemoveType {
		// 对象已经过期
		REPLACE,
		// 系统回收删除
		SYSTEM,
		// 用户主动删除
		USER
	}

	private static final int DEFAULT_CAPACITY = 10;

	private final Map<K, InnerBoxValue> map;

	private static final int DEFAULT_MAX = 10000;

	public static final int ALIVE = -1;

	private int memorySize;

	public LruCache() {
		this(DEFAULT_CAPACITY);
	}

	public LruCache(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("capacity must greater than 0");
		}
		this.map = new LruHashMap<>(capacity);
		// maxMemorySize = configMaxMemorySize();
	}

	// 配置缓存池的大小
	protected int configMaxMemorySize() {
		return DEFAULT_MAX;
	}

	@Override
	public final V get(K key) {
		return get(key, ALIVE, true);
	}

	/**
	 * @param renew
	 *            是否创建新的对象
	 * */
	public final V get(K key, boolean renew) {
		return get(key, ALIVE, renew);
	}

	/**
	 * @param key
	 *            获取健
	 * @param max_alive
	 *            对象存储有效时间,为毫秒
	 * @renew renew 是否创建新的对象
	 * */
	public final V get(K key, long max_alive, boolean renew) {
		Objects.requireNonNull(key, "key == null");
		synchronized (this) {
			InnerBoxValue value = map.get(key);
			if (value != null) {
				if (value.isValid(max_alive)) {
					value.hit++;
					return value.v;
				}
			}
			V replaced = null;
			if (renew) {
				replaced = create(key);
			}
			if (replaced == null) {
				return null;
			} else {
				put(key, replaced);
				return replaced;
			}
		}
	}

	public void onReplaced(V newValue, V oldValue) {
	}

	@Override
	public final V put(K key, V value) {
		Objects.requireNonNull(key, "key == null");
		Objects.requireNonNull(value, "value == null");
		InnerBoxValue previous;
		synchronized (this) {
			previous = map.put(key, new InnerBoxValue(value));
			memorySize += safeSize(key, value);
			// 只是替换原来的值，所以大小memorySize不变
			if (previous != null) {
				memorySize -= safeSize(key, previous.v);
				// 替换对象
				onReplaced(value, previous.v);
				onElementRemoved(key, previous.v, RemoveType.REPLACE);
			}
			trimToSize(configMaxMemorySize());
			logger.info("key=" + key + " add to cache");
		}
		if (previous != null)
			return previous.v;
		return null;
	}

	@Override
	public final V remove(K key) {
		return remove(key, RemoveType.USER);
	}

	private final V remove(K key, RemoveType type) {
		Objects.requireNonNull(key, "key == null");
		InnerBoxValue previous;
		synchronized (this) {
			previous = map.remove(key);
			if (previous != null) {
				memorySize -= safeSize(key, previous.v);
				onElementRemoved(key, previous.v, type);
				return previous.v;
			} else {
				return null;
			}
		}
	}

	@Override
	public synchronized final void clear() {
		trimToSize(-1);
	}

	@Override
	public synchronized final int getMemorySize() {
		return memorySize;
	}

	private void trimToSize(int maxSize) {
		while (true) {
			if (memorySize <= maxSize || map.isEmpty()) {
				break;
			}
			if (memorySize < 0 || (map.isEmpty() && memorySize != 0)) {
				throw new IllegalStateException("");
			}
			Map.Entry<K, InnerBoxValue> toRemove = map.entrySet().iterator()
					.next();
			InnerBoxValue removed = map.remove(toRemove.getKey());
			if (removed != null) {
				onElementRemoved(toRemove.getKey(), toRemove.getValue().v,
						RemoveType.SYSTEM);
				memorySize -= safeSize(toRemove.getKey(), toRemove.getValue().v);
			}
		}
	}

	@Override
	public synchronized final String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<K, InnerBoxValue> entry : map.entrySet()) {
			sb.append(entry.getKey()).append('=').append(entry.getValue().v)
					.append(",");
		}
		sb.append("maxMemory=").append(configMaxMemorySize()).append(",")
				.append("memorySize=").append(memorySize);
		return sb.toString();
	}

	@Override
	public int getMaxSize() {
		// TODO Auto-generated method stub
		return configMaxMemorySize();
	}

	protected V create(K key) {
		return null;
	}

	// 当元素被删除的时候回调通知
	protected void onElementRemoved(K key, V value, RemoveType type) {
		logger.info("key:" + key + " has been removed " + " type " + type);
	}

	// 默认每个元素占用的大小为1，不排除特例，但是需要重写该方法
	protected int itemSize(K key, V v) {
		return 1;
	}

	private int safeSize(K key, V v) {
		int size = itemSize(key, v);
		if (size < 0) {
			throw new IllegalStateException("must greater than 0");
		}
		return size;
	}

	class InnerBoxValue {
		V v;
		long timestamp;
		int hit;

		InnerBoxValue(V v) {
			this.v = v;
			timestamp = System.currentTimeMillis();
		}

		// 如果最大时间为负数，则没有过期时间
		boolean isValid(long period) {
			if (period < 0)
				return true;
			return System.currentTimeMillis() - timestamp <= period;
		}
	}	
}