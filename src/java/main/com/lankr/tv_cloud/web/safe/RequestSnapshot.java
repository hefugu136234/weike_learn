package com.lankr.tv_cloud.web.safe;

import java.lang.ref.SoftReference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lankr.tv_cloud.web.BaseController;

public class RequestSnapshot {

	private String sessionId;

	private String remoteIp;

	private String method;

	private String useagent;

	private String uri;

	public SoftReference<RequestSnapshot> makeSnapshot(
			HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			sessionId = session.getId();
		}
		remoteIp = BaseController.getClientIpAddr(request);
		method = request.getMethod();
		useagent = request.getHeader("User-Agent");
		uri = request.getRequestURI();
		return new SoftReference<RequestSnapshot>(this);
	}
}
