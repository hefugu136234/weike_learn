/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年7月13日
 * 	@modifyDate 2016年7月13日
 *  
 */
package com.lankr.tv_cloud.filters;

import io.netty.handler.codec.http.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.DataFormatException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.cache.page.PageCacheConfig;
import com.lankr.tv_cloud.cache.page.PageCacheUnit;
import com.lankr.tv_cloud.utils.Tools;

import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.web.AlreadyCommittedException;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.GenericResponseWrapper;
import net.sf.ehcache.constructs.web.PageInfo;
import net.sf.ehcache.constructs.web.ResponseHeadersNotModifiableException;
import net.sf.ehcache.constructs.web.filter.FilterNonReentrantException;
import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

/**
 * @author Kalean.Xiang
 *
 */
public class PageCachingFilter extends SimplePageCachingFilter {

	// @Autowired
	protected PageCacheConfig pageCacheConfig;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.ehcache.constructs.web.filter.CachingFilter#doFilter(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * javax.servlet.FilterChain)
	 */
	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年7月13日
	 * @modifyDate 2016年7月13日
	 * 
	 */
	@Override
	protected void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws AlreadyGzippedException, AlreadyCommittedException,
			FilterNonReentrantException, LockTimeoutException, Exception {
		// 是否支持缓存
		if (Config.page_cache_enable) {
			if (pageCacheConfig == null) {
				pageCacheConfig = WebApplicationContextUtils
						.getRequiredWebApplicationContext(
								request.getServletContext()).getBean(
								"pageCacheConfig", PageCacheConfig.class);
			}
			if (pageCacheConfig != null) {
				PageCacheUnit unit = pageCacheConfig.getPageCacheUnit(request);
				if (unit != null && unit.cacheEnable(request)) {
					if (unit.permission(request, response)) {
						// 清除过期的连接
						cacheCheckValid(request);
						super.doFilter(request, response, chain);
						return;
					} else {
						return;
					}
				}
			}
		}
		request.getRequestDispatcher(request.getServletPath()).forward(request,
				response);
	}

	private void cacheCheckValid(HttpServletRequest request) {
		final String key = calculateKey(request);
		Element e = blockingCache.getQuiet(key);
		if (e == null)
			return;
		PageInfo info = (PageInfo) e.getObjectValue();
		long created = info.getCreated().getTime();
		long expired = TimeUnit.MILLISECONDS.toSeconds(System
				.currentTimeMillis() - created);
		if (expired > info.getTimeToLiveSeconds()) {
			blockingCache.remove(key);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.ehcache.constructs.web.filter.CachingFilter#buildPage(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * javax.servlet.FilterChain)
	 */
	/**
	 * 注* 此实现是CachingFilter。buildPage 的实现，如果ehcache-web.jar lib更换，则应该注意磁此处的更改
	 * 重写的目的是设置每个页面不同的缓存时间需要
	 * */
	@Override
	protected PageInfo buildPage(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws AlreadyGzippedException, Exception {
		// Invoke the next entity in the chain
		final ByteArrayOutputStream outstr = new ByteArrayOutputStream();
		final GenericResponseWrapper wrapper = new GenericResponseWrapper(
				response, outstr);
		chain.doFilter(request, wrapper);
		wrapper.flush();
		long timeToLiveSeconds = blockingCache.getCacheConfiguration()
				.getTimeToLiveSeconds();
		PageCacheUnit unit = pageCacheConfig.getPageCacheUnit(request);
		if (unit != null) {
			timeToLiveSeconds = unit.pageLiveSeconds();
		}
		// Return the page info
		return new PageInfo(wrapper.getStatus(), wrapper.getContentType(),
				wrapper.getCookies(), outstr.toByteArray(), true,
				timeToLiveSeconds, wrapper.getAllHeaders());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.ehcache.constructs.web.filter.CachingFilter#buildPageInfo(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * javax.servlet.FilterChain)
	 */
	@Override
	protected PageInfo buildPageInfo(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain) throws Exception {
		/**
		 * @author Kalean.Xiang
		 * @createDate 2016年7月20日
		 * @modifyDate 2016年7月20日
		 * 
		 */
		return super.buildPageInfo(request, response, chain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.ehcache.constructs.web.filter.Filter#processInitParams(javax.servlet
	 * .FilterConfig)
	 */
	@Override
	protected void processInitParams(FilterConfig config)
			throws ServletException {
		super.processInitParams(config);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.ehcache.constructs.web.filter.CachingFilter#writeResponse(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * net.sf.ehcache.constructs.web.PageInfo)
	 */
	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年7月13日
	 * @modifyDate 2016年7月13日
	 */
	@Override
	protected void writeResponse(HttpServletRequest request,
			HttpServletResponse response, PageInfo pageInfo)
			throws IOException, DataFormatException,
			ResponseHeadersNotModifiableException {
		PageCacheUnit unit = pageCacheConfig.getPageCacheUnit(request);
		if (unit != null) {
			unit.penetrate(request, response);
			System.out.println(unit.cachePageURI()
					+ "==========================" + pageInfo);
		}

		super.writeResponse(request, response, pageInfo);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter#calculateKey
	 * (javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected String calculateKey(HttpServletRequest httpRequest) {
		// StringBuffer stringBuffer = new StringBuffer();
		// stringBuffer.append(httpRequest.getMethod())
		// .append(httpRequest.getRequestURI())
		// .append(httpRequest.getQueryString());
		// String key = stringBuffer.toString();
		PageCacheUnit unit = pageCacheConfig.getPageCacheUnit(httpRequest);
		if (unit != null) {
			String key = unit.computKey(httpRequest);
			if (!Tools.isBlank(key)) {
				return key;
			}
		}
		return super.calculateKey(httpRequest);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.ehcache.constructs.web.filter.CachingFilter#buildPageInfo(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * javax.servlet.FilterChain)
	 */
}
