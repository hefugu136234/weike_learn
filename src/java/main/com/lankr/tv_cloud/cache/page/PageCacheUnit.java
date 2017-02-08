/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年7月14日
 * 	@modifyDate 2016年7月14日
 *  
 */
package com.lankr.tv_cloud.cache.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kalean.Xiang
 *
 */
public interface PageCacheUnit {

	public boolean cacheEnable(HttpServletRequest request);

	// 缓存的uri，忽略host
	public String cachePageURI();

	// 缓存存储的key以值
	public String computKey(HttpServletRequest request);

	// 进入缓存的验证
	public boolean permission(HttpServletRequest request,
			HttpServletResponse response);

	// 可用于日志记录或其他
	public void penetrate(HttpServletRequest request,
			HttpServletResponse response);

	// 页面缓存的时间  单位(秒)
	public int pageLiveSeconds();
	
}
