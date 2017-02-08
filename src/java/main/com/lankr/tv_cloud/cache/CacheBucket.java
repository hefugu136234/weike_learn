package com.lankr.tv_cloud.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;

public class CacheBucket {

	public static final String RESOURCEITEM = "resourceItem";

	public static final String RESOURCEITEMRELATED = "resourceRelated";

	public static final String TVAUTHCACHE = "tvAuthCache";

	public static final String ACTIVITYAPICOMPLETED = "activityAPICompleted";

	public static final String SIMPLEPAGECACHINGFILTER = "SimplePageCachingFilter";
	
	public static final String ALLCOMMENTCOUNT = "allCommentCount";
	

	private static final int NO_CONFIG = -1;

	private static final Map<String, Config> configs = new HashMap<String, Config>();
	static {
		configs.put(RESOURCEITEM, new Config(5, 60));
		configs.put(RESOURCEITEMRELATED, new Config(10, 60 * 10));
		configs.put(TVAUTHCACHE, new Config(10, 60));
		configs.put(ACTIVITYAPICOMPLETED, new Config(10, 60 * 30));
		configs.put(SIMPLEPAGECACHINGFILTER, new Config(10, 60 * 60));
		configs.put(ALLCOMMENTCOUNT, new Config(10, 60 * 60));
	}

	public static int getBusyTime(String bucket) {
		Config config = configs.get(bucket);
		if (config != null) {
			return config.busyTime;
		}
		return NO_CONFIG;
	}

	public static int getIdleTime(String bucket) {
		Config config = configs.get(bucket);
		if (config != null) {
			return config.idleTime;
		}
		return NO_CONFIG;
	}

	public static void resetConfig(CacheManager cacheMgr, boolean busy) {
		try {
			if (cacheMgr == null)
				return;
			Iterator<String> keys = configs.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				//Config conf = configs.get(key);
				Cache cache = cacheMgr.getCache(key);
				CacheConfiguration cc = null;
				if (cache == null) {
					Ehcache ehcache = cacheMgr.getEhcache(key);
					if (ehcache == null) {
						continue;
					} else {
						ehcache.remove(key);
						cc = ehcache.getCacheConfiguration();
					}
				} else {
					cache.remove(key);
					cc = cache.getCacheConfiguration();

				}
				int live = NO_CONFIG;
				if (busy) {
					live = getBusyTime(key);
				} else {
					live = getIdleTime(key);
				}
				if (live != NO_CONFIG) {
					cc.setTimeToLiveSeconds(live);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class Config {

		Config(int busyTime, int idelTime) {
			this.busyTime = busyTime;
			this.idleTime = idelTime;
		}

		// seconds
		int busyTime;

		// seconds
		int idleTime;

	}
}
