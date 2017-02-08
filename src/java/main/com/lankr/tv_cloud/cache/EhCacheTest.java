package com.lankr.tv_cloud.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhCacheTest {

	private static CacheManager cacheManager;
	static String cacheName1 = "messageCache1";
	static String cacheName2 = "messageCache2";
	
	public static void main(String[] args) throws Exception {
		cacheManager = CacheManager.create();
		cacheManager.addCache(cacheName1);
		cacheManager.addCache(cacheName2);
		Cache cache1 = cacheManager.getCache(cacheName1);
		Cache cache2 = cacheManager.getCache(cacheName2);
		cache1.put(new Element("key1", "value1"));
		cache2.put(new Element("key2", "value2"));
		cache2.put(new Element("key1", "value3"));
		
//		System.out.println(cache1.get("key1"));
//		System.out.println(cacheManager.getCacheManagerPeerProviders().get("RMI"));

//		ManualRMICacheManagerPeerProvider provider = (ManualRMICacheManagerPeerProvider) cacheManager.getCacheManagerPeerProviders().get("RMI");
//		System.out.println(provider);
//		System.out.println(provider.listRemoteCachePeers(cache1));
		
	}
	
	
}
