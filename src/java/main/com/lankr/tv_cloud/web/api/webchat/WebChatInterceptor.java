package com.lankr.tv_cloud.web.api.webchat;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.WebchatFacade;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.AbsHandlerIntercepor;
import com.lankr.tv_cloud.web.BaseController.Platform;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;
import com.lankr.tv_cloud.web.api.webchat.util.WxBusinessCommon;
import com.lankr.tv_cloud.web.api.webchat.util.WxShare;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

public class WebChatInterceptor extends AbsHandlerIntercepor {
	@Autowired
	protected WebchatFacade webchatFacade;

	public final static String HANDLE_VISITOR = "handle_visitor";
	
	public final static String NOT_HANDLE_VISITOR = "not_handle_visitor";

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		logCurrentTime(request);
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			RequestAuthority requestAuthority = handlerMethod
					.getMethodAnnotation(RequestAuthority.class);
			/**
			 * 2016-12-16 添加微信浏览用户的记录
			 */
			if (requestAuthority != null && controllerMethodView(handlerMethod)
					&& !isUser(request)) {
				// 除去公共跳转，不是页面，当前未登录
				/**
				 * 1.没有opendid，需要主动授权（获取信息，最后跳转当前页面）
				 * 获取到opendid，如有user，放入session，若无，检查微信信息是否存放（有存放标志），入伍获取信息
				 */
				String red_url=preHandleVisitor(request);
				if(!Tools.isBlank(red_url)){
					response.sendRedirect(red_url);
					return false;
				}
			}

			if (requestAuthority != null && requestAuthority.requiredProject()) {
				if (!isUser(request)) {
					// 无user
					boolean viewPage = controllerMethodPage(handlerMethod);
					if (viewPage) {
						// 页面
						onAuthWaiting(request);
						response.sendRedirect(dealUrl(request, false));
					} else {
						// 操作
						response.sendRedirect(dealUrl(request, true));
					}
					return false;
				}
			}
		}
		return super.preHandle(request, response, handler);
	}

	// 检测游客数据
	/**
	 * @he 2016-12-19 1.没有opendid（session），需要主动授权（获取信息，最后跳转当前页面），
	 *     获取opendid，当有user信息时，直接处于登录状态；
	 *     若无user，查看到wechatuser，有信息时，存放opneid，标志获取过信息； 若无信息，获取信息后，标志
	 * 
	 */
	public String preHandleVisitor(HttpServletRequest request) {
		//由于前次微信code跳转2次，拿不到
		String not_handle_visitor=getValueBySessionKey(NOT_HANDLE_VISITOR, request);
		if(!Tools.isBlank(not_handle_visitor)&&not_handle_visitor.equals(NOT_HANDLE_VISITOR)){
			//此次没有拿到openid，暂不做处理，等待下次
			return null;
		}
		String currentUrl = getBaseUrl(request);
		String openId = getValueBySessionKey(BaseWechatController.OPENID_KEY,
				request);
		if (Tools.isBlank(openId)) {
			// 返回授权页面
			return WebChatMenu.authCommonVisitor(currentUrl);
		}
		// 有opneid
		String visitorMark = getValueBySessionKey(HANDLE_VISITOR, request);
		if (Tools.isBlank(visitorMark) || !visitorMark.equals(HANDLE_VISITOR)) {
			// 没有获取信息标记
			/**
			 * 1.获取webuser信息
			 */
			WebchatUser webchatUser = webchatFacade
					.searchWebChatUserByOpenid(openId);
			if (webchatUser == null) {
				return WebChatMenu.authCommonVisitor(currentUrl);
			}
			User user = webchatUser.getUser();
			if (user == null) {
				// 需要获取用户微信信息,user不存在
				if (WxBusinessCommon.judyVisitor(webchatUser)) {
					return WebChatMenu.authCommonVisitor(currentUrl);
				}
				// 不获取用户信息，标志信息获取
				setValueBySessionKey(HANDLE_VISITOR, HANDLE_VISITOR, request);
			}
		}
		return null;
	}

	public boolean controllerMethodPage(HandlerMethod handlerMethod) {
		ResponseBody responseBody = handlerMethod
				.getMethodAnnotation(ResponseBody.class);
		if (responseBody == null) {
			return true;
		}
		return false;
	}

	public boolean controllerMethodView(HandlerMethod handlerMethod) {
		Method method = handlerMethod.getMethod();
		if (method == null) {
			return false;
		}
		Class<?> reTypeClass = method.getReturnType();
		if (reTypeClass == null) {
			return false;
		}
		String pageView = reTypeClass.getSimpleName();
		if (Tools.isBlank(pageView)) {
			return false;
		}
		if ("ModelAndView".equals(pageView)) {
			return true;
		}
		return false;
	}

	// 此处添加微信jssdk签名
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
		if (modelAndView == null) {
			return;
		}
		String viewName = modelAndView.getViewName();
		// System.out.println("modelAndView:"+viewName);
		// 如果为跳转页面，则不加分享签名（只在跳转页面里面加）
		if (viewName.startsWith("redirect:")) {
			return;
		}
		if (!(handler instanceof HandlerMethod)) {
			return;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		RequestAuthority requestAuthority = handlerMethod
				.getMethodAnnotation(RequestAuthority.class);
		if (requestAuthority == null) {
			return;
		}
		int shareType = requestAuthority.wxShareType();
		if (shareType == WxSignature.WX_NO_SHARE) {
			// 不分享
			return;
		}

		// System.out.println("modelAndView:"+modelAndView.getViewName());
		String localHref = getBaseUrl(request);
		WxSignature wxSignature = WxUtil.getWxSignature(localHref);
		User user = getCurrentUser(request);
		WxShare.buildWxSignatureDetail(request, viewName, shareType, localHref,
				user, wxSignature);
		request.setAttribute("wx_signature", wxSignature);

	}

	@Override
	public Platform platform() {
		return Platform.WECHAT;
	}

	@Override
	public User getCurrentUser(HttpServletRequest request) {
		Integer userId = (Integer) request.getSession().getAttribute(
				BaseWechatController.USER_SESSION_KEY);
		if (userId == null)
			return null;
		return getCacheUser(userId);
	}

	// 判断用户详情的入口
	public boolean isUser(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null) {
			return false;
		}
		return true;
	}

	protected void setCurrentSessionUser(User user, HttpServletRequest request) {
		if (user != null) {
			userCache().put(user.getId(), user);
			request.getSession().setAttribute(
					BaseWechatController.USER_SESSION_KEY, user.getId());
		}
	}

	protected void onAuthWaiting(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			if (session != null) {
				// session.setAttribute(BaseWechatController.WX_FOUND_PRIOR_URL,
				// request.getRequestURL().toString());
				session.setAttribute(BaseWechatController.WX_FOUND_PRIOR_URL,
						getBaseUrl(request));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String getValueBySessionKey(String key, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session == null)
			return null;
		return session.getAttribute(key) == null ? null : request.getSession()
				.getAttribute(key).toString();
	}

	protected void setValueBySessionKey(String key, String value,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session == null)
			return;
		session.setAttribute(key, value);
	}

	// 当session失效时，Interceptor处理的跳转链接
	public String dealUrl(HttpServletRequest request, boolean body) {
		String url = "";
		if (body) {
			url = BaseWechatController.WX_INDEX;
		} else {
			url = getBaseUrl(request);
		}
		// 此处无user
		String openId = getValueBySessionKey(BaseWechatController.OPENID_KEY,
				request);
		System.out.println("过滤器的openid: " + openId);
		if (openId != null && !openId.isEmpty()) {
			// 当openid存在时，不必走授权
			WebchatUser webchatUser = webchatFacade
					.searchWebChatUserByOpenid(openId);
			if (webchatUser == null) {
				// 登录
				url = BaseWechatController.WX_LOGIN;
			} else {
				User user = webchatUser.getUser();
				if (user == null) {
					url = BaseWechatController.WX_LOGIN;
				} else {
					if (!BaseWechatController.loginIsAbel(user)) {
						// 无登录权限
						url = BaseWechatController.WX_ABLE_LOGIN;
					} else {
						// 可以登录url 不变
						setCurrentSessionUser(user, request);
					}
				}
			}
		} else {
			// 授权链接
			url = WebChatMenu.authCommonUrl(url);
		}
		System.out.println("过滤器的url：" + url);
		return url;

	}
}
