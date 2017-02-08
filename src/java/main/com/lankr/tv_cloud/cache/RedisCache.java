/**
 *    Copyright 2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.lankr.tv_cloud.cache;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.ibatis.cache.Cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Cache adapter for Redis.
 *
 * @author Eduardo Macarron
 */
public final class RedisCache implements Cache {

	private final ReadWriteLock readWriteLock = new DummyReadWriteLock();

	private String id;

	private static JedisPool pool;

	private static int MAX_WAIT = 10000;

	private static int TIMEOUT = 10000;

	private static JedisPoolConfig config = new JedisPoolConfig();

	private static final String ADDR = "127.0.0.1";
	private static final int PORT = 6379;
	private static final String AUTH = "zhiliao_redis_auth";

	/**
	 * 默认端口6379
	 * 
	 * @param id
	 */
	public RedisCache(final String id) {
		if (id == null) {
			throw new IllegalArgumentException("Cache instances require an ID");
		}
		this.id = id;
		config.setMaxWaitMillis(MAX_WAIT);
		pool = new JedisPool(ADDR, PORT);
		// pool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
	}

	private Object execute(RedisCallback callback) {
		Jedis jedis = pool.getResource();
		try {
			return callback.doWithRedis(jedis);
		} finally {
			jedis.close();
		}
	}

	public String getId() {
		return this.id;
	}

	public int getSize() {
		return (Integer) execute(new RedisCallback() {
			public Object doWithRedis(Jedis jedis) {
				Map<byte[], byte[]> result = jedis.hgetAll(id.toString()
						.getBytes());
				System.out.println("getSize--" + "id:" + id.toString()
						+ "  size:" + result.size());
				return result.size();
			}
		});
	}

	public void putObject(final Object key, final Object value) {
		execute(new RedisCallback() {
			public Object doWithRedis(Jedis jedis) {
				jedis.hset(id.toString().getBytes(), key.toString().getBytes(),
						SerializeUtil.serialize(value));
				System.out.println("putObject--" + "id:" + id.toString()
						+ "  key:" + key.toString() + " value:" + value);
				return null;
			}
		});
	}

	public Object getObject(final Object key) {
		return execute(new RedisCallback() {
			public Object doWithRedis(Jedis jedis) {
				Object object = SerializeUtil.unserialize(jedis.hget(id
						.toString().getBytes(), key.toString().getBytes()));
				System.out.println("getObject--" + "id:" + id.toString()
						+ "  key:" + key.toString() + " value:" + object);
				return object;
			}
		});
	}

	public Object removeObject(final Object key) {
		return execute(new RedisCallback() {
			public Object doWithRedis(Jedis jedis) {
				System.out.println("removeObject--" + "id:" + id.toString()
						+ "  key:" + key.toString());
				return jedis.hdel(id.toString(), key.toString());
			}
		});
	}

	public void clear() {
		execute(new RedisCallback() {
			public Object doWithRedis(Jedis jedis) {
				System.out.println("clear--" + "id:" + id.toString());
				jedis.del(id.toString());
				return null;
			}
		});

	}

	public ReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}

	@Override
	public String toString() {
		return "Redis {" + id + "}";
	}

}
