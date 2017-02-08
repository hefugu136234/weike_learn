package com.lankr.tv_cloud.web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.UserFacade;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.AdminWebInterceptor.AdminActiveListener;
import com.lankr.tv_cloud.web.BaseController.Platform;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

public class AdminWebInterceptor extends AbsHandlerIntercepor {

	@Autowired
	protected UserFacade userFacade;
	protected static Log logger = LogFactory.getLog(AdminWebInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logCurrentTime(request);
		// 全局设置七牛cdn域名
		request.setAttribute("qiniu_cdn_host", Config.qn_cdn_host);
		BaseController.isHostileAttack(request);
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			RequestAuthority auth = handlerMethod
					.getMethodAnnotation(RequestAuthority.class);
			if (auth != null && auth.value() > 0) {
				User user = getCurrentUser(request);
				if (user == null || !user.isActive()) {
					removeCurrentUser(request);
					ResponseBody retBody = handlerMethod
							.getMethodAnnotation(ResponseBody.class);
					if (retBody != null) {
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						response.addHeader("Content-Type",
								"text/json;charset=utf-8");
						response.getWriter().print(
								BaseController.failureModel(
										Status.NO_PERMISSION.message())
										.toJSON());
					} else {
						// 调用登录前置函数
						onAuthWaiting(request, handlerMethod);
						response.sendRedirect("/admin");
					}
					return false;
				}
				if (auth.requiredProject()) {
					if (user.getStubProject() == null) {
						response.sendRedirect("/user/project/multipart");
						return false;
					} else {

						// 非get的请求才通知有数据改变
						String method = request.getMethod();
						if (!"get".equalsIgnoreCase(method)) {
							mLoop.markTime();
						}
					}
				}
				if (user.getMainRole().isProUser()) {
					UserReference ur = user.getHandlerReference();
					if (ur != null && ur.getRole().getLevel() > auth.value()) {
						response.setContentType("text/json; charset=utf-8");
						response.getWriter().print(
								BaseController.failureModel("没有权限操作").toJSON());
						return false;
					}
				}
			}
		}

		return super.preHandle(request, response, handler);
	}

	//public static final String USER_SESSION_KEY = BaseWebController.USER_SESSION_KEY;

	@Override
	public User getCurrentUser(HttpServletRequest request) {
		Integer userId = (Integer) request.getSession().getAttribute(
				AdminWebController.ADMIN_USER_SESSION_KEY);
		if (userId == null)
			return null;
		return getCacheUser(userId);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * use BaseController
	 * */
	@Deprecated
	public String getStatusJson(Status status) {
		if (status == null)
			return BaseController.getEmptyJson();
		return BaseController.getStatusJson(status.message());
	}

	@Deprecated
	public String getStatusJson(String message) {
		return BaseController.getStatusJson(message);
	}

	protected static String nullValueFilter(String value) {
		return value == null ? "" : value;
	}

	protected void removeCurrentUser(HttpServletRequest request) {
		User user = getCurrentUser(request);
		request.getSession().removeAttribute(AdminWebController.ADMIN_USER_SESSION_KEY);
		if (user != null) {
			userCache().remove(user.getId());
		}
	}

	@Override
	public Platform platform() {
		return Platform.WEBAPP;
	}

	/**
	 * @author Kalean.Xiang 判断后台是否处于繁忙状态
	 *
	 */
	public static interface AdminActiveListener {

		public void onActive(boolean busy);
	}

	public static void registerAdminActiveListener(AdminActiveListener listener) {
		mLoop.registerAdminActiveListener(listener);
	}

	// message loop
	private final static MessageLoop mLoop = MessageLoop.getInstance();
	static {
		if (!mLoop.isAlive()) {
			mLoop.start();
		}
	}

}

class MessageLoop extends Thread {
	private volatile long access_time = 0;
	// 5分钟后台无操作，则判断为后台未空闲时间
	private final static long DEADLINE = TimeUnit.MINUTES.toMillis(5);

	private final static List<AdminActiveListener> mActiveListeners = new ArrayList<AdminWebInterceptor.AdminActiveListener>();

	void registerAdminActiveListener(AdminActiveListener listener) {
		if (listener != null) {
			mActiveListeners.add(listener);
		}
	}

	private MessageLoop() {
	}

	private static MessageLoop loop = new MessageLoop();

	public static MessageLoop getInstance() {
		return loop;
	}

	public void markTime() {
		access_time = System.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		boolean busy = false;
		while (true) {
			try {
				long current = System.currentTimeMillis();
				boolean t = current - access_time < DEADLINE;
				if (t != busy) {
					for (int i = 0; i < mActiveListeners.size(); i++) {
						mActiveListeners.get(i).onActive(t);
					}
					busy = t;
				}
				sleep(1000);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}
}
