package com.lankr.tv_cloud.web.api.webchat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.cache.EphemeralData;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.ApplicableRecords;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.SharingRes;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.model.ViewSharing;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;
import com.lankr.tv_cloud.web.api.webchat.vo.ApplyVo;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class OldInterFaceControl extends BaseWechatController{
	/**
	 * 此class中，是保留2016-04-29 之前的老接口，为之前做兼容使用
	 * 以后将不再使用
	 */
	
	/**
	 * 微信2次跳转的获取openid，不需要验证， 从微信回调到后台的验证，通过这一步，进入知了系统，通过参数，判断跳转那个页面
	 */
	
	@RequestMapping(value = "/verification", method = RequestMethod.GET)
	public ModelAndView verification(HttpServletRequest request) {
		String state = request.getParameter("state");
		System.out.println("state:" + state);
		String code = request.getParameter("code");
		System.out.println("code:" + code);
		String redirect_uri = request.getParameter("redirect_uri");
		System.out.println("redirect_uri:" + redirect_uri);
		if (state != null && state.equals(WebChatMenu.ZHILIAO_STATE)
				&& code != null && !code.isEmpty()) {
			// 此处判断是来自微信的链接
			String openid = WxUtil.getOpenIdByCode(code, 3);
			if (openid == null || openid.isEmpty()) {
				return new ModelAndView("wechat/not_webchat");
			}
			setOpenid(openid, request, OPENID_KEY);
			/**
			 * 11-27 网站扫码登录，注册的思路 首先webuser 中加unionid 字段，唯一关联键 以及新订阅是，更新unionid，
			 * 此处检测，无unionid ，更新unionid 以后用unionid做唯一键
			 */
			WebchatUser webchatUser = webchatFacade
					.searchWebChatUserByOpenid(openid);
			// redire_url==null,表示正常登陆，否则跳转该链接
			String redire_url = WxUtil.isLogin(webchatUser);
			if (redire_url != null) {
				// 跳转登录或注册
				return new ModelAndView("redirect:" + redire_url);
			}
			// 记录session，表示登陆
			User user = webchatUser.getUser();
			setCurrentSessionUser(user, request);
			return pageTotal(request, redirect_uri);
		} else {
			return new ModelAndView("wechat/not_webchat");
		}
	}
	
	/**
	 * 16-01-04,变为中间跳转路由 2016-04-27 废弃
	 * 
	 * @param request
	 * @param redirect_uri
	 * @param isPhoto
	 * @return
	 */
	public ModelAndView pageTotal(HttpServletRequest request,
			String redirect_uri) {
		if (redirect_uri.equals("index")) {
			return new ModelAndView("redirect:/api/webchat/index");
		} else if (redirect_uri.equals("center")) {
			return new ModelAndView("redirect:/api/webchat/my/center");
		} else if (redirect_uri.equals("recommend")) {
			return new ModelAndView("redirect:/api/webchat/recommend/page");
		} else {
			return new ModelAndView("wechat/not_webchat");
		}

	}
	
	/**
	 * 之前的二维码老接口 ，2014-4-27 之后过期，一般不适用 改统一
	 * 
	 * @param uuid
	 * @param request
	 * @throws IOException
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/res/{uuid}/play", method = RequestMethod.GET)
	public void apiPage(@PathVariable String uuid, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 普通的扫一扫的页面，不是微信和app
		String link="/api/webchat/resource/first/page/"+uuid;
		link=WebChatMenu.authCommonUrl(link);
		response.sendRedirect(link);
	}
	

	/**
	 * 用户点击直播分享之后，所跳转的链接
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/cast/view/share", method = RequestMethod.GET)
	public ModelAndView clickCastShare(HttpServletRequest request,
			@RequestParam String state, @RequestParam String code,
			@RequestParam String castUuid) {
		String authFlag = authJudyUser(request, code);
		String redirect_uri="/api/webchat/broadcast/first/page/"+castUuid;
		String authUrl = ultimateAuthUrl(authFlag, redirect_uri);
		return new ModelAndView("redirect:" + authUrl);
	}
	
	/**
	 * 点击者通过授权进入到此页面
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/click/view/share", method = RequestMethod.GET)
	public ModelAndView clickShare(HttpServletRequest request,
			@RequestParam String state, @RequestParam String code,
			@RequestParam(required = false) String userUUid,
			@RequestParam String resUUid) {
		System.out.println("state:" + state);
		System.out.println("code:" + code);
		if (state != null && state.equals(WebChatMenu.ZHILIAO_STATE)
				&& code != null && !code.isEmpty()) {
			// 此处判断是来自微信的链接
			String openid = getOpenId(OPENID_KEY, request);
			User user = getCurrentUser(request);
			if (user != null) {

			} else {
				if (openid == null || openid.isEmpty()) {
					openid = WxUtil.getOpenIdByCode(code, 3);
					setOpenid(openid, request, OPENID_KEY);
				}
				/**
				 * 12-08，若果以后改为只要openid，点击计算分享， 在此处添加点击分享的记录
				 */
				WebchatUser webchatUser = webchatFacade
						.searchWebChatUserByOpenid(openid);
				// redire_url==null,表示正常登陆，否则跳转该链接
				String redire_url = WxUtil.isLogin(webchatUser);
				if (redire_url == null) {
					// 记录session，表示登陆
					if (webchatUser != null) {
						user = webchatUser.getUser();
						setCurrentSessionUser(user, request);
					}
				}
			}

			/**
			 * 现在只记录,只有用户点击才算分享的记录,记录分享查看者 userUUid=原分享者
			 */
			addViewShare(request, user, userUUid, resUUid, openid);
			// 跳转分享有礼详情,oriUserUUid是给之后的点击分享做记录用的
			String giftUrl = "/api/webchat/share/git/detail/" + resUUid;
			if (userUUid != null && !userUUid.isEmpty()) {
				giftUrl = giftUrl + "?oriUserUUid=" + userUUid;
			}
			return new ModelAndView("redirect:" + giftUrl);
		} else {
			return new ModelAndView("wechat/not_webchat");
		}
	}

	public void addViewShare(HttpServletRequest request, User user,
			String shareUserUuid, String resUuid, String openid) {
		if (user == null) {
			return;
		}
		if (shareUserUuid == null || shareUserUuid.isEmpty()) {
			return;
		}
		Resource res = resourceFacade.getResourceByUuid(resUuid);
		if (res != null && res.isValid()) {
			// 首先判断是否是自己点击自己
			if (user.getUuid().equals(shareUserUuid)) {
				// 自己点击自己，不记录
				return;
			}
			User shareUser = userFacade.getUserByUuid(shareUserUuid);
			if (shareUser == null)
				return;
			// 判断是否该用户已经点击过

			/**
			 * 2016-02-24 分享有礼的点击加积分
			 */
			int effect = integralFacade.actionUserSharingViewed(shareUser, res,
					user);

			SubParams params = new SubParams();
			params.setProjectId(res.getId());
			params.setId(shareUser.getId());
			params.setUserId(user.getId());
			ViewSharing viewSharing = shareGiftMpper
					.selectViewSharingExist(params);
			if (viewSharing == null) {
				viewSharing = new ViewSharing();
				viewSharing.setUuid(Tools.getUUID());
				viewSharing.setResource(res);
				viewSharing.setUser(user);
				viewSharing.setOpenid(openid);
				viewSharing.setShareUser(shareUser);
				viewSharing.setStatus(1);
				viewSharing.setIsActive(1);
				viewSharing.setViewCount(1);
				shareGiftMpper.addViewSharing(viewSharing);
			} else {
				shareGiftMpper.addViewShareCount(viewSharing);
			}
			BaseController.bulidRequest("微信-点击查看-分享过分享有礼链接", "view_sharing",
					viewSharing.getId(), Status.SUCCESS.message(), null,
					"点击资源uuid=" + resUuid + " 成功", request);
		}
		BaseController.bulidRequest("微信-点击查看-分享过分享有礼链接", null, null,
				Status.FAILURE.message(), null, "点击资源uuid=" + resUuid + " 失败",
				request);
	}
	
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/share/common", method = RequestMethod.GET)
	public ModelAndView shareCommon(HttpServletRequest request,
			@RequestParam String redirect_uri) {
		String state = request.getParameter("state");
		// System.out.println("state:" + state);
		String code = request.getParameter("code");
		// System.out.println("code:" + code);
		System.out.println("redirect_uri:" + redirect_uri);
		if (state != null && state.equals(WebChatMenu.ZHILIAO_STATE)
				&& code != null && !code.isEmpty()) {
			String openid = getOpenId(OPENID_KEY, request);
			User user = getCurrentUser(request);
			if (openid != null || user != null) {
				BaseController.bulidRequest("微信点击一般分享页面", null, null,
						Status.SUCCESS.message(), null, "页面链接=" + redirect_uri,
						request);
				return new ModelAndView("redirect:" + redirect_uri);
			}
			// System.out.println("来自微信扫，看视频");
			openid = WxUtil.getOpenIdByCode(code, 3);
			if (openid == null || openid.isEmpty()) {
				return new ModelAndView("wechat/not_webchat");
			}
			setOpenid(openid, request, OPENID_KEY);
			WebchatUser webchatUser = webchatFacade
					.searchWebChatUserByOpenid(openid);
			// redire_url==null,表示正常登陆，否则跳转该链接
			String m_redire_url = WxUtil.isLogin(webchatUser);
			if (m_redire_url == null) {
				// 记录session，表示登陆
				user = webchatUser.getUser();
				setCurrentSessionUser(user, request);
				BaseController.bulidRequest("微信点击一般分享页面", null, null,
						Status.SUCCESS.message(), null, "页面链接=" + redirect_uri,
						request);
				return new ModelAndView("redirect:" + redirect_uri);
			} else {
				// 否则返回登陆页面
				return new ModelAndView("redirect:" + m_redire_url);
			}
		} else {
			return new ModelAndView("wechat/not_webchat");
		}
	}
	
	/**
	 * 对资源的分享
	 * 
	 * @param request
	 * @param userUuid
	 *            分享着的uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/share/res/common", method = RequestMethod.GET)
	public ModelAndView shareResCommon(HttpServletRequest request,
			@RequestParam String resUuid, @RequestParam String redirect_uri,
			@RequestParam(required = false) String userUuid) {
		String state = request.getParameter("state");
		String code = request.getParameter("code");
		System.out.println("redirect_uri:" + redirect_uri);
		if (state != null && state.equals(WebChatMenu.ZHILIAO_STATE)
				&& code != null && !code.isEmpty()) {
			User user = getCurrentUser(request);
			if (user != null) {
				BaseController.bulidRequest("微信点击资源分享页面", null, null,
						Status.SUCCESS.message(), null, "页面链接=" + redirect_uri,
						request);
				if (userUuid != null) {
					User shareUser = userFacade.getUserByUuid(userUuid);
					if (shareUser != null) {
						Resource resource = resourceFacade
								.getResourceByUuid(resUuid);
						if (resource != null) {
							int effect = integralFacade
									.actionUserSharingViewed(shareUser,
											resource, user);
						}
					}
				}
				return new ModelAndView("redirect:" + redirect_uri);
			}
			String openid = getOpenId(OPENID_KEY, request);
			if (openid == null) {
				openid = WxUtil.getOpenIdByCode(code, 3);
				if (openid == null || openid.isEmpty()) {
					return new ModelAndView("wechat/not_webchat");
				}
				setOpenid(openid, request, OPENID_KEY);
			}
			// System.out.println("来自微信扫，看视频");
			WebchatUser webchatUser = webchatFacade
					.searchWebChatUserByOpenid(openid);
			// redire_url==null,表示正常登陆，否则跳转该链接
			String m_redire_url = WxUtil.isLogin(webchatUser);
			if (m_redire_url == null) {
				// 记录session，表示登陆
				user = webchatUser.getUser();
				setCurrentSessionUser(user, request);

				// 有用户点击分享者分享的资源页面
				if (userUuid != null) {
					User shareUser = userFacade.getUserByUuid(userUuid);
					if (shareUser != null) {
						Resource resource = resourceFacade
								.getResourceByUuid(resUuid);
						if (resource != null) {
							int effect = integralFacade
									.actionUserSharingViewed(shareUser,
											resource, user);
						}
					}
				}

				BaseController.bulidRequest("微信点击资源分享页面", null, null,
						Status.SUCCESS.message(), null, "页面链接=" + redirect_uri,
						request);
				return new ModelAndView("redirect:" + redirect_uri);
			} else {
				// 否则资源的公共页面
				String res_url="/api/webchat/resource/total/share/page/"+resUuid;
				return new ModelAndView("redirect:" + res_url);
			}
		} else {
			return new ModelAndView("wechat/not_webchat");
		}
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
	
	public void buildsharePage(HttpServletRequest request){
		 long timestamp = System.currentTimeMillis() / 1000;
		 String nonceStr = (new Random().nextInt(9000000 - 1) + 1000000)
		 + "";
		 String appid = Config.wx_appid;
		 String localHref = getBaseUrl(request);
		 String signature = WxUtil.getShareSignature(timestamp, nonceStr,
		 localHref);
		 request.setAttribute("wx_timestamp", timestamp);
		 request.setAttribute("wx_nonceStr", nonceStr);
		 request.setAttribute("wx_signature", signature);
		 request.setAttribute("wx_appid", appid);
		
	}
	
	/**2016-06-15 之后要废弃
	 * 查看视频操作，状态
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/video/local/{uuid}", method = RequestMethod.GET)
	public ModelAndView videoLocal(HttpServletRequest request,
			@PathVariable String uuid) {
//		buildsharePage(request);
//		Resource res = effectResourceFacade().getResourceByUuid(uuid);
//		if (res == null || !res.isValid()) {
//			return redirectErrorPage(request, "资源不存在或已下线");
//		}
//		User user = getCurrentUser(request);
//		// 编辑返回前台的数据
//		buildDetailPage(request, user, res, true);
//		// app扫码
//		bulidRequest("微信订阅号播放视频", res, uuid, request);
//
//		/**
//		 * 2016-06-14 记录页面时长
//		 */
//		addPageRecordTime(request, PageRemain.RESOURCE_DETAIL_TYPE, res.getId());
//
//		return redirectPageView("wechat/oldresource/resource_preview");
		return commonResourceFirstView(request, uuid);
	}

	/**2016-06-15 之后要废弃
	 * 微信查看pdf一级页面 要改
	 * 
	 * @param request
	 * @return 查看pdf详情一级页面
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/pdf/local/view/{uuid}", method = RequestMethod.GET)
	public ModelAndView pdfLocal(HttpServletRequest request,
			@PathVariable String uuid) {
//		buildsharePage(request);
//		
//		Resource res = effectResourceFacade().getResourceByUuid(uuid);
//		String data_error = "";
//		if (res == null || !res.isValid()) {
//			return redirectErrorPage(request, "资源不存在或已下线");
//		}
//		if (res.getType() != Type.PDF) {
//			return redirectErrorPage(request, "您查看的不是pdf资源");
//		}
//		User user = getCurrentUser(request);
//		// 编辑返回前台的数据
//		buildDetailPage(request, user, res, false);
//		BaseAPIController.bulidRequest("微信查看pdf详情", res, uuid, request);
//		return new ModelAndView("wechat/oldresource/pdf_preview");
		return commonResourceFirstView(request, uuid);

	}

	/**2016-06-15 之后要废弃
	 * 微信查看pdf二级页面 要改
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/pdf/click/open/{uuid}", method = RequestMethod.GET)
	public ModelAndView pdfOPen(HttpServletRequest request,
			@PathVariable String uuid) {
//		buildsharePage(request);
//		
//		Resource res = effectResourceFacade().getResourceByUuid(uuid);
//		String data_error = "";
//		if (res == null || !res.isValid()) {
//			return redirectErrorPage(request, "资源不存在或已下线");
//		}
//		if (res.getType() != Type.PDF) {
//			return redirectErrorPage(request, "您查看的不是pdf资源");
//		}
//		PdfInfo info = res.getPdf();
//		int pageNum = Integer.parseInt(info.getPdfnum());
//		String taskId = info.getTaskId();
//		request.setAttribute("pageNum", pageNum);
//		request.setAttribute("taskId", taskId);
//		// 2016-4-29 改
//		RecourceDetailVo vo = new RecourceDetailVo();
//		vo.buildPdfDetail(res);
//		request.setAttribute("data_json", vo.toJSON());
//		// 增加观看次数
//		effectResourceFacade().countResourceView(res);
//
//		User user = getCurrentUser(request);
//		/**
//		 * 2016-2-24,打开pdf，增加积分
//		 */
//		if (user != null) {
//			integralFacade.actionUserPlayResource(user, res);
//		}
//
//		bulidRequest("微信播放PDF", res, uuid, request);
//
//		/**
//		 * 2016-06-14 记录页面时长
//		 */
//		addPageRecordTime(request, PageRemain.RESOURCE_DETAIL_TYPE, res.getId());
//		return redirectPageView("wechat/oldresource/pdf_view");
		return commonResourceViewDetail(request, uuid);

	}

	/**2016-06-15 之后要废弃
	 * 要改 查看三分屏 一级页面
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/threescree/local/view/{uuid}", method = RequestMethod.GET)
	public ModelAndView threescreeLocal(HttpServletRequest request,
			@PathVariable String uuid) {
//		buildsharePage(request);
//		Resource res = effectResourceFacade().getResourceByUuid(uuid);
//		String data_error = "";
//		if (res == null || !res.isValid()) {
//			return redirectErrorPage(request, "资源不存在或已下线");
//		}
//		if (res.getType() != Type.THREESCREEN) {
//			return redirectErrorPage(request, "您查看的不是三分屏资源");
//		}
//		User user = getCurrentUser(request);
//		// 编辑返回前台的数据
//		buildDetailPage(request, user, res, false);
//		BaseAPIController.bulidRequest("微信查看三分屏详情", res, uuid, request);
//		return new ModelAndView("wechat/oldresource/three_screen_preview");
		return commonResourceFirstView(request, uuid);

	}

	/**2016-06-15 之后要废弃
	 * 查看三分屏二级页面（要改）
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
//	@RequestAuthority(requiredProject = true, logger = true)
//	@RequestMapping(value = "/threescree/click/open/{uuid}", method = RequestMethod.GET)
//	public ModelAndView threescreeClick(HttpServletRequest request,
//			@PathVariable String uuid) {
//		Resource res = effectResourceFacade().getResourceByUuid(uuid);
//		String data_error = "";
//		if (res == null || !res.isValid()) {
//			data_error = "资源不存在或已下线";
//			request.setAttribute("error_data", data_error);
//			BaseAPIController.bulidRequest("微信播放三分屏", res, uuid, request);
//			return new ModelAndView("wechat/three_screen");
//		}
//		if (res.getType() != Type.THREESCREEN) {
//			data_error = "您查看的不是三分屏资源";
//			request.setAttribute("error_data", data_error);
//			BaseAPIController.bulidRequest("微信播放三分屏", res, uuid, request);
//			return new ModelAndView("wechat/three_screen");
//		}
//		User user = getCurrentUser(request);
//		// 编辑返回前台的数据
//		buildDetailPage(request, user, res, false);
//		// 增加观看次数
//		effectResourceFacade().countResourceView(res);
//		bulidRequest("微信播放三分屏", res, uuid, request);
//
//		/**
//		 * 2016-06-14 记录页面时长
//		 */
//		addPageRecordTime(request, PageRemain.RESOURCE_DETAIL_TYPE, res.getId());
//		return redirectPageView("wechat/three_screen");
//
//	}
	
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/threescree/click/open/{uuid}", method = RequestMethod.GET)
	public ModelAndView threescreeClick(HttpServletRequest request,
			@PathVariable String uuid) {
//		buildsharePage(request);
//		Resource resource = effectResourceFacade().getResourceByUuid(uuid);
//		if (resource == null || !resource.apiUseable()) {
//			return redirectErrorPage(request, "资源不存在或已下线");
//		}
//		if (resource.getType() == Type.THREESCREEN) {
//			return threeScreenDetailPage(request, resource);
//		}
//		return redirectErrorPage(request, "资源数据类型未识别");
		return commonResourceViewDetail(request, uuid);

	}
	
//	public ModelAndView threeScreenDetailPage(HttpServletRequest request,
//			Resource resource) {
//		WxResourceItem item = new WxResourceItem();
//		item.buildDetailLittle(resource);
//		request.setAttribute("res_vo", item);
//		
//		User user = getCurrentUser(request);
//		// 编辑返回前台的数据
//		buildDetailPage(request, user, resource, false);
//
//		/**
//		 * 2016-06-14 记录页面时长
//		 */
//		addPageRecordTime(request, PageRemain.RESOURCE_DETAIL_TYPE,
//				resource.getId());
//
//		bulidRequest("微信播放三分屏", resource, resource.getUuid(), request);
//		return redirectPageView("wechat/oldresource/three_screen");
//	}
//	
	/**2016-06-15 之后要废弃
	 * 精彩推荐详情 新闻页面---2016-4-27要做调整
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
//	@RequestAuthority(requiredProject = false, logger = true)
//	@RequestMapping(value = "/recommend/detail/{uuid}", method = RequestMethod.GET)
//	public ModelAndView recommendDetail(HttpServletRequest request,
//			@PathVariable String uuid) {
//		Resource res = effectResourceFacade().getResourceByUuid(uuid);
//		if (res == null || !res.isValid()) {
//			request.setAttribute("error", "资源不可用或已下线");
//			BaseAPIController.bulidRequest("微信查看精彩推荐详情", res, uuid, request);
//			return new ModelAndView("wechat/user_defined");
//		}
//		// 根据分类查找，视频信息的资源
//		WeChatBulidData data = new WeChatBulidData();
//		NewsInfoVo vo = data.buildrecommend(res);
//		if (vo == null) {
//			request.setAttribute("error", "资源不属于精彩推荐分类");
//			BaseAPIController.bulidRequest("微信查看精彩推荐详情", res, uuid, request);
//			return new ModelAndView("wechat/user_defined");
//		}
//		request.setAttribute("newsinfovo", vo);
//		BaseAPIController.bulidRequest("微信查看精彩推荐详情", res, uuid, request);
//		return new ModelAndView("wechat/user_defined");
//
//	}
	
	/**2016-06-15 之后要废弃
	 * 2016-4-22 最新的新闻 新闻详情-咨询详情
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
//	@RequestAuthority(requiredProject = false, logger = false)
//	@RequestMapping(value = "/news/detail/{uuid}", method = RequestMethod.GET)
//	public ModelAndView newsDetail(HttpServletRequest request,
//			@PathVariable String uuid) {
//		Resource res = effectResourceFacade().getResourceByUuid(uuid);
//		if (res == null || !res.isValid()) {
//			request.setAttribute("error", "资源不可用或已下线");
//			return new ModelAndView("wechat/news/news_detail");
//		}
//		// 根据分类查找，视频信息的资源
//		WeChatBulidData data = new WeChatBulidData();
//		NewsInfoVo vo = data.buildrecommend(res);
//		if (vo == null) {
//			request.setAttribute("error", "资源不属于新闻咨询");
//			return new ModelAndView("wechat/news_detail");
//		}
//		request.setAttribute("newsinfovo", vo);
//		return new ModelAndView("wechat/news/news_detail");
//
//	}
	
	/**2016-06-15 之后要废弃
	 * 分享有礼详情---要做调整 2016-3-1 oriUserUUid为最原始的分享着，给此次分享保存数据使用
	 * 
	 * 2016-4-28修改
	 * 
	 * @param request
	 * @param uuid
	 * @param oriUserUUid
	 * @return
	 */
//	@RequestAuthority(requiredProject = false, logger = true)
//	@RequestMapping(value = "/share/git/detail/{uuid}", method = RequestMethod.GET)
//	public ModelAndView viewShareGiftDetail(HttpServletRequest request,
//			@PathVariable String uuid,
//			@RequestParam(required = false) String oriUserUuid) {
//		Resource res = effectResourceFacade().getResourceByUuid(uuid);
//		if (res == null || !res.isValid()) {
//			request.setAttribute("error", "资源不可用或已下线");
//			BaseAPIController.bulidRequest("微信查看分享有礼详情", res, uuid, request);
//			return new ModelAndView("wechat/share_detail");
//		}
//		// 根据分类查找，视频信息的资源
//		WeChatBulidData data = new WeChatBulidData();
//		ShareGiftVo vo = data.buildShare(res);
//		if (vo == null) {
//			request.setAttribute("error", "资源不属于分享有礼分类");
//			return new ModelAndView("wechat/share_detail");
//		}
//
//		oriUserUuid = (oriUserUuid == null ? ResourceControl.NONE_USER : oriUserUuid);
//
//		/**
//		 * 2016-4-29 修改 在分享有礼的详情页中记录，来自分享的点击事件
//		 */
//		addViewShare(request, res, oriUserUuid);
//
//		request.setAttribute("share_detail", vo);
//		request.setAttribute("oriUserUuid", oriUserUuid);
//		BaseAPIController.bulidRequest("微信查看分享有礼详情", res, uuid, request);
//		return new ModelAndView("wechat/share_detail");
//	}
	
	/**2016-06-15 之后要废弃
	 * 2016-4-29 修改 在分享有礼的详情页中记录，来自分享的点击事件 查看分享
	 */
	public void addViewShare(HttpServletRequest request, Resource resource,
			String oriUserUuid) {
		User user = getCurrentUser(request);
		if (user == null) {
			return;
		}
		if (oriUserUuid.equals(ResourceControl.NONE_USER)) {
			return;
		}
		if (user.getUuid().equals(oriUserUuid)) {
			// 自己点击自己，不记录
			return;
		}
		User shareUser = userFacade.getUserByUuid(oriUserUuid);
		if (shareUser == null) {
			return;
		}
		/**
		 * 2016-02-24 分享有礼的点击加积分
		 */
		int effect = integralFacade.actionUserSharingViewed(shareUser,
				resource, user);
		SubParams params = new SubParams();
		params.setProjectId(resource.getId());
		params.setId(shareUser.getId());
		params.setUserId(user.getId());
		ViewSharing viewSharing = shareGiftMpper.selectViewSharingExist(params);
		if (viewSharing == null) {
			viewSharing = new ViewSharing();
			viewSharing.setUuid(Tools.getUUID());
			viewSharing.setResource(resource);
			viewSharing.setUser(user);
			viewSharing.setOpenid(getOpenId(OPENID_KEY, request));
			viewSharing.setShareUser(shareUser);
			viewSharing.setStatus(BaseModel.APPROVED);
			viewSharing.setIsActive(BaseModel.ACTIVE);
			viewSharing.setViewCount(1);
			shareGiftMpper.addViewSharing(viewSharing);
		} else {
			shareGiftMpper.addViewShareCount(viewSharing);
		}
	}
	
	/**2016-06-15 之后要废弃
	 * 活动报道的详情页（要修改）
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
//	@RequestAuthority(requiredProject = false, logger = true)
//	@RequestMapping(value = "/activity/report/detail/{uuid}", method = RequestMethod.GET)
//	public ModelAndView reportDetail(HttpServletRequest request,
//			@PathVariable String uuid) {
//		Resource res = effectResourceFacade().getResourceByUuid(uuid);
//		if (res == null || !res.isValid()) {
//			request.setAttribute("error", "资源不可用或已下线");
//			BaseAPIController.bulidRequest("微信查看活动报道详情", res, uuid, request);
//			return new ModelAndView("wechat/activity_report_detail");
//		}
//		// 根据分类查找，视频信息的资源
//		WeChatBulidData data = new WeChatBulidData();
//		NewsInfoVo vo = data.buildrecommend(res);
//		if (vo == null) {
//			request.setAttribute("error", "资源不属于活动报道");
//			BaseAPIController.bulidRequest("微信查看活动报道详情", res, uuid, request);
//			return new ModelAndView("wechat/activity_report_detail");
//		}
//		request.setAttribute("newsinfovo", vo);
//		BaseAPIController.bulidRequest("资源不属于活动报道", res, uuid, request);
//		return new ModelAndView("wechat/activity_report_detail");
//
//	}
	
	
	
	/**
	 * 
	 * @param url
	 *            分享的链接
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/wx/share/config", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String shareConfig(HttpServletRequest request,
			@RequestParam String url) {
		return WxUtil.getShareSignature(url);
	}
	
	/**
	 * 2016-04-29 资源分享的特定链接(video ,threescreen,pdf)
	 * 
	 * @param request
	 * @param url
	 * @param isLogin
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/wx/share/res/config", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String shareConfig(HttpServletRequest request,
			@RequestParam String url, @RequestParam String uuid) {
		User user = getCurrentUser(request);
		String oriUserUuid = ResourceControl.NONE_USER;
		if (user != null) {
			oriUserUuid = user.getUuid();
		}
		String redirectUrl = "/api/webchat/resource/first/page/" + uuid
				+ "?oriUserUuid=" + oriUserUuid;
		return WxUtil.getRedireSignature(url, redirectUrl);
	}
	
	/**
	 * 获取分享有礼的分享配置
	 * 
	 * @param request
	 * @param url
	 * @param userUuid
	 * @param resUUid
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/wx/share/gift/config", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String shareGiftConfig(HttpServletRequest request,
			@RequestParam String url, @RequestParam String uuid) {
		User user = getCurrentUser(request);
		String oriUserUuid = ResourceControl.NONE_USER;
		if (user != null) {
			oriUserUuid = user.getUuid();
		}
		String redirectUrl = "/api/webchat/share/git/detail/" + uuid
				+ "?oriUserUuid=" + oriUserUuid;
		return WxUtil.getRedireSignature(url, redirectUrl);
	}
	
	/**
	 * 直播的专属分享配置
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/wx/share/cast/config", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String shareCastConfig(HttpServletRequest request,
			@RequestParam String url, @RequestParam String uuid) {
		Broadcast broadcast = broadcastFacade.getCastByUuid(uuid);
		if (broadcast == null) {
			return failureModel().toJSON();
		}
		return WxUtil.getShareCastSignature(url, broadcast);
	}
	
	/**
	 * 当分享有礼-分享以后的记录 要做调整 记录分享
	 * 
	 * @param request
	 * @param uuid
	 * @param type
	 * @param oriUserUuid
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/gift/share", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String giftShare(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String type,
			@RequestParam String oriUserUuid) {
		User user = getCurrentUser(request);
		if (user == null) {
			return Status.FAILURE.message();
		}
		Resource res = resourceFacade.getResourceByUuid(uuid);
		if (res == null) {
			return Status.FAILURE.message();
		}
		User oriUser = null;
		if (!oriUserUuid.equals(ResourceControl.NONE_USER)) {
			oriUser = userFacade.getUserByUuid(oriUserUuid);
		}
		SharingRes sharingRes = new SharingRes();
		sharingRes.setUuid(Tools.getUUID());
		sharingRes.setResource(res);
		sharingRes.setUser(user);
		sharingRes.setApproach(type);
		sharingRes.setIsActive(BaseModel.ACTIVE);
		sharingRes.setStatus(BaseModel.APPROVED);
		sharingRes.setOriUser(oriUser);
		shareGiftMpper.addShareRes(sharingRes);
		BaseController.bulidRequest("微信分享-分享有礼-" + type, "sharing_res",
				sharingRes.getId(), Status.SUCCESS.message(), res.getName(),
				"成功", request);
		return Status.SUCCESS.message();
	}
	
	/**
	 * @he 2016-06-28 以后 优化登录 登录操作
	 */
	// @RequestAuthority(requiredProject = false, logger = true)
	// @RequestMapping(value = "/login", method = RequestMethod.POST, produces =
	// "text/json;charset=UTF-8")
	// @ResponseBody
	// public String login(@RequestParam String username,
	// @RequestParam String password, HttpServletRequest request,
	// HttpServletResponse response) {
	// String openId = getOpenId(OPENID_KEY, request);
	// if (openId == null) {
	// failureModel("请退出页面，重新点击进入").toJSON();
	// }
	// User user = userFacade.login(username, Md5.getMd5String(password));
	// if (user == null) {
	// bulidRequest("微信用户登录失败", "user", null, Status.FAILURE.message(),
	// null, "username=" + username + " password=" + password
	// + " 用户名或密码错误", request);
	// return failureModel("用户名或密码错误").toJSON();
	// } else {
	// UserReference reference = user.getUserReference();
	// if (reference == null) {
	// bulidRequest("微信用户登录失败", "user", user.getId(),
	// Status.FAILURE.message(), null, "username=" + username
	// + " password=" + password + " 没有权限登录", request);
	// return failureModel("您当前没有权限登录").toJSON();
	// }
	// Role role = reference.getRole();
	// if (role == null || role.getLevel() < 5) {
	// BaseController.bulidRequest("微信用户登录失败", "user", user.getId(),
	// Status.FAILURE.message(), null, "username=" + username
	// + " password=" + password + " 没有权限登录", request);
	// return failureModel("您当前没有权限登录").toJSON();
	// }
	// setCurrentSessionUser(user, request);
	// WebchatUser webchatUser = null;
	// if (openId != null) {
	// webchatUser = webchatFacade.searchWebChatUserByOpenid(openId);
	// // 更新绑定user
	// if (webchatUser != null && webchatUser.getUser() == null) {
	// webchatUser.setUser(user);
	// webchatFacade.updateWebChatUser(webchatUser);
	// } else if (webchatUser == null) {
	// // 新增绑定
	// webchatUser = new WebchatUser();
	// webchatUser.setUuid(Tools.getUUID());
	// webchatUser.setOpenId(openId);
	// webchatUser.setStatus(BaseModel.APPROVED);
	// webchatUser.setIsActive(BaseModel.ACTIVE);
	// webchatUser.setUser(user);
	// webchatFacade.addWebChatUser(webchatUser);
	// }
	// }
	// bulidRequest("微信用户登录成功", "user", user.getId(),
	// Status.SUCCESS.message(), null, null, request);
	// return successModel().toJSON();
	// }
	// }
	
	// /**
		// * @he 2016-06-28 以后 优化注册
		// * @param valid_code
		// * @param password
		// * @param nickname
		// * @param token
		// * @param type
		// * @param hosipital
		// * @param professor
		// * @param city
		// * @param department
		// * @param activeCode
		// * @param company
		// * @param request
		// * @param response
		// * @return
		// */
		// // 保存注册用户
		// @RequestAuthority(requiredProject = false, logger = true)
		// @RequestMapping(value = "/user/register/save", produces =
		// "text/json; charset=utf-8", method = RequestMethod.POST)
		// public @ResponseBody String saveRegisterUser(String valid_code,
		// String password, @RequestParam String nickname,
		// @RequestParam String token, @RequestParam int type,
		// @RequestParam(required = false) String hosipital,
		// @RequestParam(required = false) String professor,
		// @RequestParam(required = false) String city,
		// @RequestParam(required = false) String department,
		// @RequestParam(required = false) String activeCode,
		// @RequestParam(required = false) String company,
		// HttpServletRequest request, HttpServletResponse response) {
		// // 绑定微信
		// String openId = getOpenId(OPENID_KEY, request);
		// if (openId == null) {
		// failureModel("请退出页面，重新点击进入").toJSON();
		// }
		// RegisterTmp tmp = userFacade.getRegisterTmpByUuid(valid_code);
		// if (tmp == null || !tmp.isActive()) {
		// BaseController.bulidRequest("微信个人注册", "user", null,
		// Status.FAILURE.message(), null, "nickname=" + nickname
		// + " 手机号暂不可用,注册失败", request);
		// return BaseController.failureModel("注册的手机号暂不可用，请重新验证！").toJSON();
		// }
		// User user = userFacade.getUserByUsername(tmp.getMobile());
		// if (user != null) {
		// BaseController.bulidRequest("微信个人注册", "user", null,
		// Status.FAILURE.message(), null, "手机号=" + tmp.getMobile()
		// + " 已被注册", request);
		// return BaseController.failureModel("该手机号已被注册！").toJSON();
		// }
		//
		// String server_token = toastRepeatSubmitToken(request, REG_SAVE_TOKEN);
		// if (!token.equals(server_token)) {
		// return BaseController.failureModel("请勿重复提交数据").toJSON();
		// }
		// if (default_project == null) {
		// default_project = projectFacade.getProjectByUuid(PROJECT_UUID);
		// }
		// String uuid = Tools.getUUID();
		// user = new User();
		// user.setNickname(nickname);
		// if (nickname != null && !nickname.isEmpty()) {
		// user.setPinyin(Tools.getPinYin(nickname));
		// } else {
		// user.setPinyin("");
		// }
		//
		// user.setUsername(tmp.getMobile());
		// user.setPhone(tmp.getMobile());
		// user.setUuid(uuid);
		// user.setIsActive(BaseModel.ACTIVE);
		// user.setPassword(Md5.getMd5String(password));
		// user.setMainRole(getRole(Role.PRO_USER_LEVEL));
		//
		// String re_uuid = Tools.getUUID();
		// UserReference ur = new UserReference();
		// ur.setProject(default_project);
		// ur.setRole(getRole(Role.PRO_CUSTOMER));
		// ur.setUuid(re_uuid);
		// if (type == 1) {
		// // 企业要激活用户本身状态
		// if (activeCode != null && !activeCode.isEmpty()) {
		// ur.setIsActive(BaseModel.ACTIVE);
		// } else {
		// ur.setIsActive(BaseModel.DISABLE);
		// }
		// } else {
		// ur.setIsActive(BaseModel.ACTIVE);
		// }
		//
		// UserExpand userExpand = new UserExpand();
		// userExpand.setUuid(Tools.getUUID());
		// userExpand.setUser(user);
		// userExpand.setStatus(BaseModel.APPROVED);
		// userExpand.setIsActive(BaseModel.ACTIVE);
		// userExpand.setMark("");
		// userExpand.setBirthday("");
		// userExpand.setType(type);
		// if (city != null && !city.isEmpty()) {
		// City city_model = provinceMapper.selectCtiByUUid(city);
		// if (city_model != null) {
		// userExpand.setCity(city_model);
		// }
		// }
		// if (hosipital != null && !hosipital.isEmpty()) {
		// Hospital hospital_model = hospitalMapper
		// .selectHospitalByUUid(hosipital);
		// if (hospital_model != null) {
		// userExpand.setHospital(hospital_model);
		// }
		// }
		// if (department != null && !department.isEmpty()) {
		// Departments departments_model = hospitalMapper
		// .selectDePatByUuid(department);
		// if (departments_model != null) {
		// userExpand.setDepartments(departments_model);
		// }
		// }
		// userExpand.setProfessor(professor);
		// userExpand.setSex("");
		// userExpand.setResume("");
		// userExpand.setClassId("");
		// userExpand.setQualificationId("");
		// if (company != null && !company.isEmpty()) {
		// Manufacturer manufacturer = groupFacade
		// .selectManufacturerByUuid(company);
		// userExpand.setManufacturer(manufacturer);
		// }
		//
		// // 添加用户
		// user.setUserExpand(userExpand);
		//
		// Status status = userFacade.addUserByWX(user, ur);
		// if (status == Status.FAILURE) {
		// BaseController.bulidRequest("微信个人注册", "user", null,
		// Status.FAILURE.message(), null, "手机号=" + tmp.getMobile()
		// + " 注册失败", request);
		// return BaseController.failureModel("用户注册失败").toJSON();
		// }
		//
		// User addUser = userFacade.getUserByUuid(uuid);
		//
		// /**
		// * 2016-2-24,注册新增积分
		// */
		// integralFacade.actionRegister(addUser);
		//
		// /**
		// * 2016-3-16,添加发送模板消息
		// */
		// getModelMessageFacade().registerSuccess(addUser);
		//
		// WebchatUser webchatUser = null;
		// if (openId != null) {
		// // 未关注用户
		// webchatUser = webchatFacade.searchWebChatUserByOpenid(openId);
		// if (webchatUser != null) {
		// webchatUser.setUser(addUser);
		// status = webchatFacade.updateWebChatUser(webchatUser);
		// } else {
		// // 新增绑定
		// webchatUser = new WebchatUser();
		// webchatUser.setUuid(Tools.getUUID());
		// webchatUser.setOpenId(openId);
		// webchatUser.setStatus(1);
		// webchatUser.setIsActive(1);
		// webchatUser.setUser(addUser);
		// status = webchatFacade.addWebChatUser(webchatUser);
		// }
		//
		// if (status == Status.SUCCESS) {
		// setCurrentSessionUser(addUser, request);
		// userFacade.disableRegisterTmp(tmp);
		// BaseController.bulidRequest("微信个人注册", "user", addUser.getId(),
		// Status.SUCCESS.message(), null,
		// "手机号=" + tmp.getMobile() + " 注册成功", request);
		// } else {
		// BaseAPIModel apiModel = BaseController
		// .failureModel("用户绑定失败，请登录");
		// apiModel.setStatus(Status.ERROR);
		// return apiModel.toJSON();
		// }
		// }
		//
		// // 最后处理充值卡问题
		// if (activeCode != null && !activeCode.isEmpty()) {
		// ActivationCode code = activationFacade
		// .getActivationByCode(activeCode);
		// ActionMessage actionMessage = userFacade
		// .increaseUserValidDateForBox(addUser, code);
		// if (actionMessage.getStatus() == Status.FAILURE) {
		// BaseController.bulidRequest("微信个人注册", "user", null,
		// Status.FAILURE.message(), null,
		// "手机号=" + tmp.getMobile() + " code=" + activeCode
		// + " 流量卡激活失效，注册失败", request);
		// BaseAPIModel apiModel = BaseController.failureModel();
		// apiModel.setStatus(Status.PARAM_ERROR);
		// if (actionMessage.getMessage().equals("参数有错误")) {
		// apiModel.setMessage("该卡无法充值");
		// } else {
		// apiModel.setMessage(actionMessage.getMessage());
		// }
		// return apiModel.toJSON();
		// }
		// }
		//
		// return BaseController.successModel().toJSON();
		// }
	
	/**
	 * @he 2016-06-28 以后 优化（基本废弃使用） 申请vip 要修改
	 * @he 接口基本废弃 （2016-08-16）
	 */
	@RequestAuthority(requiredProject = true, logger = false, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/apply/invite", method = RequestMethod.GET)
	public ModelAndView applyPage(HttpServletRequest request) {
		setToken(request, INVITE_TOKEN);
		User user = getCurrentUser(request);
		ApplyVo vo = new ApplyVo();
		vo.setName(Tools.nullValueFilter(user.getNickname()));
		vo.setPhone(Tools.nullValueFilter(user.getPhone()));
		List<Province> list = EphemeralData.getIntansce(provinceMapper)
				.getProvinces();
		vo.setProList(list);
		List<Departments> depList = hospitalMapper.selectDePatList();
		vo.setDepList(depList);
		UserExpand expand = user.getUserExpand();
		if (expand != null) {
			City city = expand.getCity();
			if (city != null) {
				Province province = city.getProvince();
				if (province != null) {
					vo.setProUUid(province.getUuid());
					List<City> cityList = provinceMapper
							.selectCtiListById(province.getId());
					vo.setCityList(cityList);
					vo.setCityUUid(city.getUuid());
				}
				List<Hospital> hosList = hospitalMapper
						.selectHosListByCityId(city.getId());
				vo.setHosList(hosList);
				Hospital hospital = expand.getHospital();
				if (hospital != null) {
					vo.setHosUUid(hospital.getUuid());
				}

				Departments departments = expand.getDepartments();
				if (departments != null) {
					vo.setDepUUid(departments.getUuid());
				}
			}
			vo.setProfessor(Tools.nullValueFilter(expand.getProfessor()));
		}
		request.setAttribute("info", vo);
		return redirectPageView("wechat/apply");
	}

	/**
	 * 保存申请vip 要修改
	 * @he 接口基本废弃 （2016-08-16）
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/apply/invite", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String addApply(HttpServletRequest request,
			@RequestParam String token, @RequestParam String applyName,
			@RequestParam String mobile, @RequestParam String hosipital,
			@RequestParam String office,
			@RequestParam(required = false) String professor) {
		Hospital hospital = hospitalMapper.selectHospitalByUUid(hosipital);
		if (hospital == null || !hospital.isActive())
			return getStatusJson("医院信息不可用");
		Departments departments = hospitalMapper.selectDePatByUuid(office);
		if (departments == null || !departments.isActive()) {
			return getStatusJson("科室信息不可用");
		}
		User user = getCurrentUser(request);
		String server_token = toastRepeatSubmitToken(request, INVITE_TOKEN);
		Status status = Status.SUCCESS;
		if (!token.equals(server_token)) {
			status = Status.SUBMIT_REPEAT;
		} else {
			ApplicableRecords applic = new ApplicableRecords();
			applic.setUuid(Tools.getUUID());
			applic.setApplyName(applyName);
			applic.setPingYin(Tools.getPinYin(applyName));
			applic.setMobile(mobile);
			applic.setHospital(hospital);
			applic.setDepartments(departments);
			applic.setStatus(0);
			applic.setIsActive(1);
			applic.setMark(professor);
			applic.setUser(user);
			status = applicableFacade.addApplicable(applic);
			if (status == Status.SUCCESS) {
				BaseController.bulidRequest("完成邀请码提交:" + applyName,
						"applicable_records", applic.getId(), status.message(),
						null, null, request);
			} else {
				BaseController.bulidRequest("完成邀请码提交:" + applyName,
						"applicable_records", applic.getId(), status.message(),
						null, "提交失败", request);
			}
		}
		return getStatusJson(status);
	}
	
    public static void main(String[] args) {
		List<String> list=new ArrayList<String>();
		Collections.sort(list);
		Collections.sort(list, (a,b)->b.compareTo(a));
	}
	
	



}
