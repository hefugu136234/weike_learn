/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年7月15日
 * 	@modifyDate 2016年7月15日
 *  
 */
package com.lankr.tv_cloud.cache.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kalean.Xiang
 *
 */
public class SimplePageCache implements PageCacheUnit {

	private String mUri;

	public SimplePageCache(String uri) {
		mUri = uri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.cache.page.PageCacheUnit#cacheEnable(javax.servlet
	 * .http.HttpServletRequest)
	 */
	// 目前 只支持get请求的缓存
	@Override
	public boolean cacheEnable(HttpServletRequest request) {
		//
		return "GET".equalsIgnoreCase(request.getMethod());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.cache.page.PageCacheUnit#cachePageURI()
	 */
	@Override
	public String cachePageURI() {
		return mUri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.cache.page.PageCacheUnit#computKey(javax.servlet.http
	 * .HttpServletRequest)
	 */
	@Override
	public String computKey(HttpServletRequest request) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(request.getMethod())
				.append(request.getRequestURI());
		return stringBuffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.cache.page.PageCacheUnit#authNeeded(javax.servlet.
	 * http.HttpServletRequest)
	 */

	@Override
	public boolean permission(HttpServletRequest request,
			HttpServletResponse response) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.cache.page.PageCacheUnit#penetrate(javax.servlet.http
	 * .HttpServletRequest)
	 */
	public void penetrate(HttpServletRequest request,
			HttpServletResponse response) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.cache.page.PageCacheUnit#pageLiveSeconds()
	 */
	// 默认缓存3分钟  可用被重写
	@Override
	public int pageLiveSeconds() {
		return 60 * 3;
	}
}
