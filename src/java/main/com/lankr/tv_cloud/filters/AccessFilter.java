package com.lankr.tv_cloud.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.google.gson.Gson;

public class AccessFilter extends CharacterEncodingFilter {

	private static Log logger = LogFactory.getLog(AccessFilter.class);
	private static Gson gson = new Gson();

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		long begin = System.currentTimeMillis();
		try {
			super.doFilterInternal(request, response, filterChain);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (!isJsRequest(request) && !isCssRequest(request)
						&& !isImageRequest(request))
					logger.info("IP:" + request.getRemoteAddr() + "\t["
							+ request.getMethod() + "]" + "\tURI:"
							+ request.getRequestURI() + "\tParams:"
							+ gson.toJson(request.getParameterMap())
							+ "\tUser-Agent:" + request.getHeader("User-Agent")
							+ "\tContent-Type:" + request.getContentType()
							+ "\tStatus:" + response.getStatus() + "\tUse:"
							+ (System.currentTimeMillis() - begin) + "ms");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isJsRequest(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return uri.endsWith(".js");
	}

	private boolean isCssRequest(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return uri.endsWith(".css");
	}

	private boolean isImageRequest(HttpServletRequest request) {
		String uri = request.getRequestURI().toLowerCase();
		return uri.endsWith(".jpg") || uri.endsWith(".png")
				|| uri.endsWith(".gif") || uri.endsWith(".jpeg");
	}

	// @Override
	// protected void doFilterInternal(HttpServletRequest request,
	// HttpServletResponse response, FilterChain filterChain)
	// throws ServletException, IOException {
	// long begin = System.currentTimeMillis();
	// response.setHeader("Access-Control-Allow-Origin", "*");
	// response.setHeader("Access-Control-Allow-Methods",
	// "POST, GET, OPTIONS, DELETE");
	// response.setHeader("Access-Control-Max-Age", "3600");
	// response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
	// filterChain.doFilter(request, response);
	// try {
	// logger.info("IP:" + request.getRemoteAddr() + "\t["
	// + request.getMethod() + "]" + "\tURI:"
	// + request.getRequestURI() + "\tParams:"
	// + gson.toJson(request.getParameterMap()) + "\tUser-Agent:"
	// + request.getHeader("User-Agent") + "\tContent-Type:"
	// + request.getContentType() + "\tStatus:"
	// + response.getStatus() + "\tUse:"
	// + (System.currentTimeMillis() - begin) + "ms");
	// } catch (Exception e) {
	// logger.error(e);
	// }
	// }
}
