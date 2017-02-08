package com.lankr.tv_cloud.web;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.model.User;

public class AdminWebController extends BaseWebController {

	public static final String ADMIN_USER_SESSION_KEY = "admin_user_session_key";

	@Override
	public User getCurrentUser(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Integer userId = (Integer) request.getSession().getAttribute(
				ADMIN_USER_SESSION_KEY);
		if (userId == null)
			return null;
		User user = getUserCache().get(userId, TimeUnit.MINUTES.toMillis(5),
				true);
		return user;
	}

	protected void setCurrentSessionUser(User user, HttpServletRequest request) {
		if (user != null) {
			getUserCache().put(user.getId(), user);
			request.getSession().setAttribute(ADMIN_USER_SESSION_KEY,
					user.getId());
		}
	}

	protected void removeCurrentUser(HttpServletRequest request) {
		User user = getCurrentUser(request);
		request.getSession().removeAttribute(ADMIN_USER_SESSION_KEY);
		if (user != null) {
			getUserCache().remove(user.getId());
		}
	}

	protected void refreshUserItemCache(User oldUser) {
		if (oldUser != null) {
			User pre = getUserCache().get(oldUser.getId(), false);
			if (pre == null)
				return;
			User newUser = getUserCache().create(oldUser.getId());
			if (newUser != null)
				getUserCache().put(newUser.getId(), newUser);
		}
	}

	// protected void refreshUserItemCache(HttpServletRequest request) {
	// refreshUserItemCache(getCurrentUser(request));
	// }

	public ModelAndView index(HttpServletRequest request) {
		request.setAttribute("currentUser", getCurrentUser(request));
		return new ModelAndView("common/index");
	}

	public ModelAndView index(String content_jsp) {
		return index(getHandleRequest(), content_jsp);
	}

	public ModelAndView index(HttpServletRequest request, String content_jsp) {
		content_jsp = "/WEB-INF" + content_jsp;
		request.setAttribute("page_include", content_jsp);
		return index(request);
	}
	


}
