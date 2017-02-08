package com.lankr.tv_cloud.web.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.AbsHandlerIntercepor;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.BaseController.Platform;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

public class FrontIntercepor extends AbsHandlerIntercepor {

	public static final String PHONE_VIEW = "phone_view";

	public final static String[] AGENT = { "Android", "iPhone", "iPod", "iPad",
			"Windows Phone" };

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logCurrentTime(request);
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			RequestAuthority requestAuthority = handlerMethod
					.getMethodAnnotation(RequestAuthority.class);

			if (requestAuthority != null && !requestAuthority.phoneView()) {
				// 过滤客户端phone
				boolean phone_viewed = viewPhoneOfSession(request);
				if (!phone_viewed) {
					boolean isMobile = checkAgentIsMobile(request);
					if (isMobile) {
						response.sendRedirect(BaseFrontController.PC_PRIOR
								+ "/mobile/index");
						return false;
					}
				}
			}

			if (requestAuthority != null && requestAuthority.requiredProject()) {
				if (!isUser(request)) {
					// 无user
					ResponseBody retBody = handlerMethod
							.getMethodAnnotation(ResponseBody.class);
					if (retBody != null) {
						// 操作
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						response.addHeader("Content-Type",
								"text/json;charset=utf-8");
						response.getWriter().print(
								BaseController.failureModel(
										Status.NO_PERMISSION.message())
										.toJSON());
					} else {
						// 页面
						onAuthWaiting(request);
						// 跳转登录
						response.sendRedirect(BaseFrontController.LOGIN_URL);
					}
					return false;
				}
			}
		}
		return super.preHandle(request, response, handler);
	}

	// @Override
	// public void postHandle(HttpServletRequest request,
	// HttpServletResponse response, Object handler,
	// ModelAndView modelAndView) throws Exception {
	// // TODO Auto-generated method stub
	// super.postHandle(request, response, handler, modelAndView);
	// if(modelAndView!=null){
	// modelAndView.addObject("333333", "3333333");
	// }
	// }

	public boolean viewPhoneOfSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session != null) {
			Object object = session.getAttribute(PHONE_VIEW);
			if (object != null) {
				String viewed = (String) object;
				if (viewed != null && viewed.equals(PHONE_VIEW)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkAgentIsMobile(HttpServletRequest request) {
		boolean flag = false;
		String user_agent = request.getHeader("User-Agent");
		if (Tools.isBlank(user_agent)) {
			return flag;
		}
		for (String item : AGENT) {
			if (user_agent.contains(item)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	protected void onAuthWaiting(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			if (session != null) {
				session.setAttribute(BaseFrontController.FRONT_FOUND_PRIOR_URL,
						getBaseUrl(request));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 判断用户详情的入口
	public boolean isUser(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null) {
			return false;
		}
		return true;
	}

	@Override
	public Platform platform() {
		// TODO Auto-generated method stub
		return Platform.PCWEB;
	}

	@Override
	public User getCurrentUser(HttpServletRequest request) {
		Integer userId = (Integer) request.getSession().getAttribute(
				BaseFrontController.FRONT_USER_SESSION_KEY);
		if (userId == null)
			return null;
		return getCacheUser(userId);
	}

}
