package com.lankr.tv_cloud.web.api.tv;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import com.lankr.tv_cloud.facade.APIFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.TvAuthentication;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.AbsHandlerIntercepor;
import com.lankr.tv_cloud.web.BaseController.Platform;
import com.lankr.tv_cloud.web.api.BaseAPIController;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

public class TvAPIInterceptor extends AbsHandlerIntercepor {

	@Autowired
	protected APIFacade apiFacade;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logCurrentTime(request);
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			boolean json = handlerMethod
					.getMethodAnnotation(ResponseBody.class) != null;
			RequestAuthority auth = handlerMethod
					.getMethodAnnotation(RequestAuthority.class);
			String token = request.getParameter("token");
			TvAuthentication tvAuth = null;
			if(!Tools.isBlank(token)){
				tvAuth = apiFacade.getTvAuthByToken(token);
				request.setAttribute(BaseAPIController.AUTH_KEY, tvAuth);
			}
			if (auth != null) {
				if (auth.requiredToken()) {
					if (token == null) {
						sendErrorAuthToClient(request, response,
								"parameter(s) must contain token", false, json);
						return false;
					}
					
					if (tvAuth == null) {
						sendErrorAuthToClient(request, response,
								"token was not found", true, json);
						return false;
					}
					if (!tvAuth.isActive()) {
						sendErrorAuthToClient(request, response,
								"token was invalid", true, json);
						return false;
					}
					User user = tvAuth.getUser();
					if (user == null || !user.isActive()) {
						sendErrorAuthToClient(request, response, "用户无效", true,
								json);
						return false;
					}
					// 替换成缓存中的user
					user = getCacheUser(user.getId());
					tvAuth.setUser(user);
					UserReference ur = user.getUserReference();
					if (ur == null || !ur.isActive()) {
						sendErrorAuthToClient(request, response, "用户无权访问",
								true, json);
						return false;
					}
					Role role = ur.getRole();
					if (role == null || !role.isBoxUser()) {
						sendErrorAuthToClient(request, response, "用户无权访问",
								true, json);
						return false;
					}
					if (!ur.isDateValid()) {
						sendRechargeAuthToClient(request, response,
								"用户流量不足，请充值流量卡", true);
						return false;
					}					
				}
			}
		}
		return super.preHandle(request, response, handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.web.AbsHandlerIntercepor#wrapUseagent(javax.servlet
	 * .http.HttpServletRequest)
	 */
	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月27日
	 * @modifyDate 2016年5月27日
	 * 
	 */
	@Override
	protected String wrapUseagent(HttpServletRequest request) {
		String origin = super.wrapUseagent(request);
		String own = request.getParameter("user_agent");
		if (!Tools.isBlank(own)) {
			return origin + "@" + own;
		}
		return origin;
	}

	private void sendErrorAuthToClient(HttpServletRequest request,
			HttpServletResponse response, String detail, boolean relogin,
			boolean json) {
		try {
			if (json) {
				response.setContentType("text/json; charset=utf-8");
				BaseAPIModel model = new BaseAPIModel();
				model.setStatus(Status.FAILURE);
				model.setMessage(detail);
				model.setSuggestion(detail);
				if (relogin) {
					model.setCode(BaseAPIModel.CodeConstant.TV_RELOGIN);
				}
				response.getWriter().print(model.toJSON());
			} else {
				response.sendRedirect("/tv/auth_error.jsp");
			}
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.web.AbsHandlerIntercepor#afterCompletion(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, java.lang.Exception)
	 */

	private void sendRechargeAuthToClient(HttpServletRequest request,
			HttpServletResponse response, String detail, boolean relogin) {
		try {
			response.setContentType("text/json; charset=utf-8");
			BaseAPIModel model = new BaseAPIModel();
			model.setStatus(Status.FAILURE);
			model.setMessage(detail);
			model.setSuggestion(detail);
			// if(relogin){
			// model.setCode(BaseAPIModel.CodeConstant.TV_RELOGIN);
			// }
			model.setCode(BaseAPIModel.CodeConstant.TV_RECHARGE);
			response.getWriter().print(model.toJSON());
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}
	}

	@Override
	public Platform platform() {
		return Platform.TV_BOX;
	}

	@Override
	public User getCurrentUser(HttpServletRequest request) {
		TvAuthentication auth = BaseAPIController
				.getHttpRequestWrappedData(TvAuthentication.class);
		if (auth != null) {
			return auth.getUser();
		}
		return null;
	}

}
