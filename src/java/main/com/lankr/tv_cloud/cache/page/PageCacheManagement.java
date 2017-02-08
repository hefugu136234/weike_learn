/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年7月15日
 * 	@modifyDate 2016年7月15日
 *  
 */
package com.lankr.tv_cloud.cache.page;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kalean.Xiang
 *
 */
public class PageCacheManagement {

	private Map<String, PageCacheUnit> cache_units = new HashMap<String, PageCacheUnit>();
	
	
	public void addPageCache(PageCacheUnit page){
		if(page == null)
			return;
		cache_units.put(page.cachePageURI(), page);
	}
	
	
}
