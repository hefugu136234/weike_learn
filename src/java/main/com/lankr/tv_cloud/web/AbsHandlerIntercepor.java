package com.lankr.tv_cloud.web;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.AccessLogFacade;
import com.lankr.tv_cloud.facade.UserFacade;
import com.lankr.tv_cloud.model.AccessLog;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.BaseController.Platform;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

public abstract class AbsHandlerIntercepor extends HandlerInterceptorAdapter {

	protected static Log logger = LogFactory.getLog(AbsHandlerIntercepor.class);

	private static final String PLATEFORM_PARAM = "api_plateform";

	public static final String PLATFORM_REQUEST_KEY = "platform_request_key";

	public static final String LOGIN_FORWARD_URL_KEY = "LOGIN_FORWARD_URL";

	public abstract Platform platform();

	public abstract User getCurrentUser(HttpServletRequest request);

	public static final String START_TIME = "start_time";

	@Autowired
	private AccessLogFacade accessLogFacade;

	@Autowired
	protected UserFacade userFacade;

	protected GlobalUserCache userCache() {
		return GlobalUserCache.getInstance(userFacade);
	}

	public User getCacheUser(int userId) {
		return userCache().get(userId, TimeUnit.MINUTES.toMillis(5), true);
	}

	public User getCacheUser(int userId, boolean renew) {
		return userCache().get(userId, TimeUnit.MINUTES.toMillis(5), renew);
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		request.setAttribute(PLATFORM_REQUEST_KEY, platform());
		return super.preHandle(request, response, handler);
	}

	public void logCurrentTime(HttpServletRequest request) {
		request.setAttribute(START_TIME, System.currentTimeMillis());
	}

	/**
	 * @author Kalean.Xiang
	 * @createDate: 2016年3月25日
	 * @modifyDate: 2016年3月25日 需要登錄
	 */
	protected void onAuthWaiting(HttpServletRequest request,
			HandlerMethod handler) {
		try {
			if (handler == null)
				return;
			RequestAuthority auth = handler
					.getMethodAnnotation(RequestAuthority.class);
			if (auth == null)
				return;
			ResponseBody retBody = handler
					.getMethodAnnotation(ResponseBody.class);
			if (retBody != null)
				return;
			HttpSession session = request.getSession();
			if (session != null) {
				session.setAttribute(LOGIN_FORWARD_URL_KEY, new RedirectTile(
						auth, request.getRequestURL().toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class RedirectTile {
		public RedirectTile(RequestAuthority auth, String url) {
			this.auth = auth;
			this.url = url;
		}

		final RequestAuthority auth;
		final String url;
	}
	
	public String getBaseUrl(HttpServletRequest request) {
		String url = request.getScheme() + "://";
		url += request.getHeader("host");
		url += request.getRequestURI();
		if (request.getQueryString() != null) {
			url += "?" + request.getQueryString();
		}
		//System.out.println(url);
		return url;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
		if (ex != null) {
			logger.error(request.getRequestURI() + " has an error", ex);
		}
		try {
			if (!Config.mysql_logger_enable) {
				return;
			}
			if (handler instanceof HandlerMethod) {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				RequestAuthority auth = handlerMethod
						.getMethodAnnotation(RequestAuthority.class);
				if (auth != null && auth.logger()) {
					/**
					 * 2016-06-06 mark 为空时，不再记录日志
					 */
					String mark=BaseController.readStirngData(request,BaseController.API_LOG_MARK);
					if(mark.isEmpty()){
						//空日志不在记录
						return ;
					}
					if (request.getAttribute(BaseController.LOG_USERID) == null) {
						User user = getCurrentUser(request);
//						if(user==null){
//							//若果user==null，不记录日志
//							return ;
//						}
						request.setAttribute(BaseController.LOG_USERID,
								user == null ? user : user.getId());
					}
					/**
					 * 增加记录openid 对应resource字段
					 */
					
					request.setAttribute(BaseController.LOG_PLATEFORM,
							platform().name());
					request.setAttribute(BaseController.LOG_USER_AGEN,
							wrapUseagent(request));
					request.setAttribute(BaseController.LOG_VERSION,
							wrapPlatFormVersion(request));
					long startTime = (Long) request.getAttribute(START_TIME);
					long currentTime = System.currentTimeMillis();
					currentTime = currentTime - startTime;
					Integer time = Integer
							.parseInt(String.valueOf(currentTime));
					request.setAttribute(BaseController.LOG_RESPONSETIME, time);
					AccessLog log = new AccessLog();
					log.buildData(request, response);
					accessLogFacade.addAccessLog(log);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected String wrapUseagent(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}

	protected String wrapPlatFormVersion(HttpServletRequest request) {
		String ver = "0.0";
		try {
			ver = request.getParameter("version");
			if (!Tools.isBlank(ver)) {
				return ver.substring(0, Math.min(ver.length(), 15));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ver;

	}
}
