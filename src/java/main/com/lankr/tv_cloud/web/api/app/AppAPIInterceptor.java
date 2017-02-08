/*package com.lankr.tv_cloud.web.api.app;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.APIFacade;
import com.lankr.tv_cloud.facade.AccessLogFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.UserFacade;
import com.lankr.tv_cloud.model.AccessLog;
import com.lankr.tv_cloud.model.AppAuthentication;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.ApiAuth;
import com.lankr.tv_cloud.web.api.BaseAPIController;

public class AppAPIInterceptor extends HandlerInterceptorAdapter {
	

	@Autowired
	protected UserFacade userFacade;

	@Autowired
	protected APIFacade apiFacade;
	
	@Autowired
	private AccessLogFacade accessLogFacade;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			ApiAuth auth = handlerMethod.getMethodAnnotation(ApiAuth.class);
			if (auth != null) {
				if (auth.requiredToken()) {
					String token = request.getParameter("token");
					if (token == null) {
						sendErrorAuthToClient(request, response,
								"parameter(s) must contain token");
						return false;
					}
					AppAuthentication appAuth = userFacade
							.getAppAuthByToken(token);
					if (appAuth == null) {
						sendErrorAuthToClient(request, response,
								"token was not found");
						return false;
					}
					if (!appAuth.isActive()) {
						sendErrorAuthToClient(request, response,
								"token was invalid");
						return false;
					}
					User user = appAuth.getUser();
					if (user == null || !user.isActive()) {
						sendErrorAuthToClient(request, response,
								"user was inactive");
						return false;
					}
					request.setAttribute(BaseAPIController.AUTH_TOKEN_KEY,
							appAuth);
				}
			}
		}
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
		if (!Config.mysql_logger_enable) {
			return;
		}
		AppAuthentication tvAuth = (AppAuthentication) request
				.getAttribute(BaseAPIController.AUTH_TOKEN_KEY);
		if (tvAuth == null) {
			return;
		}
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			ApiAuth auth = handlerMethod.getMethodAnnotation(ApiAuth.class);
			if (auth != null && auth.logger()) {
				User user = tvAuth.getUser();
				request.setAttribute(BaseController.LOG_USERID,
						user == null ? user : user.getId());
				request.setAttribute(BaseController.LOG_PLATEFORM,
						BaseController.Plateform.ANDROID.name());
				request.setAttribute(BaseController.LOG_USER_AGEN,
						request.getHeader("user_agent"));
				request.setAttribute(BaseController.LOG_VERSION,
						request.getParameter("version"));
				AccessLog log = new AccessLog();
				log.BuildData(request, response);
				accessLogFacade.addAccessLog(log);
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	private void sendErrorAuthToClient(HttpServletRequest request,
			HttpServletResponse response, String detail) {
		try {
			response.setContentType("text/json; charset=utf-8");
			BaseAPIModel model = new BaseAPIModel();
			model.setStatus(Status.FAILURE);
			model.setMessage(Status.NO_PERMISSION.message());
			model.setSuggestion(detail);
			response.getWriter().print(model.toJSON());
		} catch (IOException ignore) {
			ignore.printStackTrace();
		}
	}
	


}
*/