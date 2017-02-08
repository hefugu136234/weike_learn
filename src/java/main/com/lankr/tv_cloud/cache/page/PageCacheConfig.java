/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年7月18日
 * 	@modifyDate 2016年7月18日
 *  
 */
package com.lankr.tv_cloud.cache.page;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.tv_cloud.facade.AccessLogFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.UserFacade;
import com.lankr.tv_cloud.model.AccessLog;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.BaseWebController;
import com.lankr.tv_cloud.web.GlobalUserCache;
import com.lankr.tv_cloud.web.BaseController.Platform;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;
import com.lankr.tv_cloud.web.front.BaseFrontController;

/**
 * @author Kalean.Xiang
 *
 */

public class PageCacheConfig {

	@Autowired
	private AccessLogFacade accessLogFacade;

	@Autowired
	protected UserFacade userFacade;

	private final PageCacheUnit PC_WEB_HOME = new SimplePageCache(
			"/f/web/index") {
		public boolean permission(HttpServletRequest request,
				javax.servlet.http.HttpServletResponse response) {
			return true;
		}

		@Override
		public void penetrate(HttpServletRequest request,
				HttpServletResponse response) {

			BaseController.bulidRequest("web首页", null, null,
					Status.SUCCESS.message(), null, "成功", request);
			recordLog(request, response, Platform.PCWEB);
		}
	};

	private final PageCacheUnit PC_WEB_HOME_MENU = new SimplePageCache(
			"/f/web/top/menu") {
		public boolean permission(HttpServletRequest request,
				javax.servlet.http.HttpServletResponse response) {
			return true;
		}

		@Override
		public void penetrate(HttpServletRequest request,
				HttpServletResponse response) {
		}

		// public int pageLiveSeconds() {
		// return 5;
		// }
	};

	// 微信首页
	private final PageCacheUnit WX_INDEX = new SimplePageCache(
			BaseWechatController.WX_PRIOR + "/index") {
		@Override
		public boolean permission(HttpServletRequest request,
				HttpServletResponse response) {
			return true;
		};

		@Override
		public void penetrate(HttpServletRequest request,
				HttpServletResponse response) {
			BaseController.bulidRequest("查看首页", null, null,
					Status.SUCCESS.message(), null, null, request);
			recordLog(request, response, Platform.WECHAT);
		};
	};

	// 微信学科页面
	private final PageCacheUnit WX_FIRST_SUBJECT = new SimplePageCache(
			BaseWechatController.WX_PRIOR + "/first/level/subject") {
		@Override
		public boolean permission(HttpServletRequest request,
				HttpServletResponse response) {
			return true;
		};

		@Override
		public void penetrate(HttpServletRequest request,
				HttpServletResponse response) {
			BaseController.bulidRequest("微信查看学科分类", null, null,
					Status.SUCCESS.message(), null, null, request);
			recordLog(request, response, Platform.WECHAT);
		};
	};

	private final static Map<String, PageCacheUnit> _cache = new HashMap<String, PageCacheUnit>();

	public void config() {
		addCache(PC_WEB_HOME);
		addCache(PC_WEB_HOME_MENU);
		addCache(WX_INDEX);
		addCache(WX_FIRST_SUBJECT);
	}

	private void addCache(PageCacheUnit page) {
		_cache.put(page.cachePageURI(), page);
	}

	public PageCacheUnit getPageCacheUnit(HttpServletRequest request) {
		String uri = request.getRequestURI();
		PageCacheUnit unit = _cache.get(uri);
		return unit;
	}

	protected GlobalUserCache userCache() {
		return GlobalUserCache.getInstance(userFacade);
	}

	public User getCacheUser(int userId) {
		return userCache().get(userId, TimeUnit.MINUTES.toMillis(5), true);
	}

	public User getCacheUser(int userId, boolean renew) {
		return userCache().get(userId, TimeUnit.MINUTES.toMillis(5), renew);
	}

	public User getCurrentUser(HttpServletRequest request, String key) {
		Integer userId = (Integer) request.getSession().getAttribute(
				BaseFrontController.FRONT_USER_SESSION_KEY);
		if (userId == null)
			return null;
		return getCacheUser(userId);
	}

	public User getCurrentUser(HttpServletRequest request) {
		Integer userId = (Integer) request.getSession().getAttribute(
				BaseWechatController.USER_SESSION_KEY);
		if (userId == null)
			return null;
		return getCacheUser(userId);
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

	public User getPlatformUser(HttpServletRequest request, Platform platform) {
		if (platform == Platform.PCWEB) {
			return getCurrentUser(request,
					BaseFrontController.FRONT_USER_SESSION_KEY);
		} else if (platform == Platform.WECHAT) {
			return getCurrentUser(request, BaseWechatController.USER_SESSION_KEY);
		}
		return null;
	}

	public void recordLog(HttpServletRequest request,
			HttpServletResponse response, Platform platform) {
		String mark = BaseController.readStirngData(request,
				BaseController.API_LOG_MARK);
		if (mark.isEmpty()) {
			// 空日志不在记录
			return;
		}
		if (request.getAttribute(BaseController.LOG_USERID) == null) {
			User user = getPlatformUser(request, platform);

//			if(user==null){
//				//若果user==null，不记录日志
//				return ;
//			}
			request.setAttribute(BaseController.LOG_USERID,
					user == null ? user : user.getId());
		}
		request.setAttribute(BaseController.LOG_PLATEFORM, platform.name());
		request.setAttribute(BaseController.LOG_USER_AGEN,
				wrapUseagent(request));
		request.setAttribute(BaseController.LOG_VERSION,
				wrapPlatFormVersion(request));
		request.setAttribute(BaseController.LOG_RESPONSETIME, 0);
		AccessLog log = new AccessLog();
		log.buildData(request, response);
		accessLogFacade.addAccessLog(log);
	}

}
