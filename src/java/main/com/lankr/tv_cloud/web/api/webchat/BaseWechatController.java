package com.lankr.tv_cloud.web.api.webchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.APIFacade;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.PageRemain;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.ResourceAccessIgnore;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.model.ViewSharing;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.Base64Data;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel.JsonConvertor;
import com.lankr.tv_cloud.vo.snapshot.ShowCommentItem;
import com.lankr.tv_cloud.web.BaseWebController;
import com.lankr.tv_cloud.web.api.webchat.util.ResourceAuthority;
import com.lankr.tv_cloud.web.api.webchat.util.ResourceAuthorityUtil;
import com.lankr.tv_cloud.web.api.webchat.util.WxBusinessCommon;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;
import com.lankr.tv_cloud.web.api.webchat.vo.WxCommentList;
import com.lankr.tv_cloud.web.api.webchat.vo.WxPdf;
import com.lankr.tv_cloud.web.api.webchat.vo.WxResourceItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxThreeScreen;
import com.lankr.tv_cloud.web.api.webchat.vo.WxUserShowInfo;
import com.lankr.tv_cloud.web.api.webchat.vo.WxVideo;
import com.lankr.tv_cloud.web.front.vo.FrontResVote;

public class BaseWechatController extends BaseWebController {

	// session wx 保存上一个失效的url
	public static final String WX_FOUND_PRIOR_URL = "WX_FOUND_PRIOR_URL";

	public static final String OPENID_KEY = "openid_key";

	public static final String APP_TOKEN = "app_token";

	public final static String INVITE_TOKEN = "invite_token";

	public final static String REG_SAVE_TOKEN = "reg_save_token";

	public static final String WX_ERROR = "WX_ERROR";

	// 微信api访问前缀，之后统一这个,为v2版本做准备
	public static final String WX_PRIOR = "/api/webchat";

	// 微信端的api url,之后应用于一处（2016-06-12）
	public static final String WX_INDEX = WX_PRIOR + "/index";

	public static final String WX_LOGIN = WX_PRIOR + "/page/login";

	public static final String WX_ABLE_LOGIN = WX_PRIOR + "/isable/page/login";

	public static final String WX_NOT_WX = WX_PRIOR + "/not/wx";

	public static final String USER_SESSION_KEY = "currentUser";

	@Override
	public User getCurrentUser(HttpServletRequest request) {
		Integer userId = (Integer) request.getSession().getAttribute(
				USER_SESSION_KEY);
		if (userId == null)
			return null;
		User user = getUserCache().get(userId, TimeUnit.MINUTES.toMillis(5),
				true);
		return user;
	}

	protected void setCurrentSessionUser(User user, HttpServletRequest request) {
		if (user != null) {
			getUserCache().put(user.getId(), user);
			request.getSession().setAttribute(USER_SESSION_KEY, user.getId());
		}
	}

	protected void removeCurrentUser(HttpServletRequest request) {
		User user = getCurrentUser(request);
		request.getSession().removeAttribute(USER_SESSION_KEY);
		if (user != null) {
			getUserCache().remove(user.getId());
		}
	}

	protected void setOpenid(String openid, HttpServletRequest request,
			String key) {
		request.getSession().setAttribute(key, openid);
	}

	protected String getOpenId(String key, HttpServletRequest request) {
		return request.getSession().getAttribute(key) == null ? null : request
				.getSession().getAttribute(key).toString();
	}

	protected void removeOpenid(HttpServletRequest request, String key) {
		request.getSession().removeAttribute(key);
	}

	protected void setToken(HttpServletRequest request, String key) {
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.getSession().setAttribute(key, token);
	}

	@Override
	protected ResourceFacade effectResourceFacade() {
		return cacheResourceFacade;
	}

	@Override
	protected APIFacade effectApiFacade() {
		return cacheAPIFacade;
	}

	/**
	 * 2016-06-15 微信资源被点开的数据整合
	 */
	public WxResourceItem buildResFirstPage(HttpServletRequest request,
			Resource resource) {
		WxResourceItem item = new WxResourceItem();
		// 基本数据
		item.buildBaseListItem(resource);
		// 点赞收藏
		int collectionCount = myCollectionFacade.selectCountByReId(resource
				.getId());
		item.setCollectCount(collectionCount);
		int praiseCount = praiseFacade.selectCountByReId(resource.getId());
		item.setPraiseCount(praiseCount);

		User user = getCurrentUser(request);
		boolean isCollent = false;// 0=否 1=是
		boolean isPraise = false;
		if (user != null) {
			isCollent = myCollectionFacade.isFavoritedResourceByUser(resource,
					user);
			isPraise = praiseFacade.selectPraiseByReIdAndUserId(
					resource.getId(), user.getId());
		}
		item.setCollectStatus(isCollent);
		item.setPraiseStatus(isPraise);
		return item;
	}

	/**
	 * 微信登录返回状态
	 */
	public static final String WX_NOT_WECHAT = "wx_not_wechat";// 不是来自微信

	public static final String WX_NOT_USER = "wx_not_user";// 没有用户

	public static final String WX_NOT_LOGIN = "wx_not_login";// 没有权限登录

	public static final String WX_LOGIN_ING = "wx_login_ing";// 登录中

	/**
	 * 微信处理授权过来的user相关
	 */
	public String authJudyUser(HttpServletRequest request, String code) {
		User user = getCurrentUser(request);
		if (user != null) {
			return WX_LOGIN_ING;
		}
		String openid = getOpenId(OPENID_KEY, request);
		if (openid == null) {
			openid = WxUtil.getOpenIdByCode(code, 1);
			if (openid == null || openid.isEmpty()) {
				//2016-12-20 交给前面的过滤器的主动授权，静默授权，有时code会获取不到，出现错误。
				return WX_NOT_USER;
			}
			setOpenid(openid, request, OPENID_KEY);
		}
		System.out.println("授权获取openid:" + openid);
		WebchatUser webchatUser = webchatFacade
				.searchWebChatUserByOpenid(openid);
		if (webchatUser == null) {
			return WX_NOT_USER;
		}
		user = webchatUser.getUser();
		if (user == null) {
			return WX_NOT_USER;
		}
		if (!loginIsAbel(user)) {
			return WX_NOT_LOGIN;
		}
		setCurrentSessionUser(user, request);
		return WX_LOGIN_ING;
	}

	/**
	 * 最终授权返回的页面
	 */
	public String ultimateAuthUrl(String flag, String redirect_uri) {
		if (flag.equals(WX_NOT_WECHAT)) {
			return WX_NOT_WX;
		} else if (flag.equals(WX_NOT_LOGIN)) {
			return WX_ABLE_LOGIN;
		} else if (flag.equals(WX_NOT_USER)) {
			if (redirect_uri != null) {
				return redirect_uri;
			}
			return WX_INDEX;
		} else {
			if (redirect_uri != null) {
				return redirect_uri;
			}
			return WX_INDEX;
		}
	}

	/**
	 * 个人的基本展示信息
	 * 
	 * @he 2016-06-27 修改
	 */
	public void wxUserShowBaseInfo(HttpServletRequest request) {
		User user = getCurrentUser(request);
		wxUserShowBaseInfo(request, user);
	}

	/**
	 * 2016-07-12修改
	 * 
	 * @param request
	 * @return
	 */
	public void wxUserShowBaseInfo(HttpServletRequest request, User user) {
		WxUserShowInfo info = new WxUserShowInfo();
		info.setUserUuid(user.getUuid());
		UserReference reference = user.getUserReference();
		String vipStatus = reference.isDateStr();
		info.setVipStatus(vipStatus);

		String vipDeadTime = "";
		if ("in_use".equals(vipStatus)) {
			vipDeadTime = Tools.df.format(reference.getValidDate());
		}
		info.setVipDeadTime(vipDeadTime);

		/**
		 * 2016-2-25 个人的总积分
		 */
		int score = integralFacade.fetchUserIntegralTotal(user);
		info.setScore(score);

		/**
		 * 2016-3-14 实名认证之后的信息
		 */
		Certification certification = certificationFacade
				.getCertifiActiveByUserId(user);
		if (certification == null) {
			info.setRealFlag(false);
			info.setShowName(OptionalUtils.traceValue(user, "nickname"));
		} else {
			info.setRealFlag(true);
			info.setShowName(OptionalUtils.traceValue(certification, "name"));
			info.setRealNameInfo(OptionalUtils.traceValue(certification,
					"credentials"));
		}
		String photo = dealWxPhoto(user);
		photo = WxUtil.getDefaultAvatar(photo);
		info.setPhoto(photo);
		request.setAttribute("vo_user_data", info);
	}

	/**
	 * 2016-06-27 微信处理头像
	 */
	public String dealWxPhoto(User user) {
		String photo = OptionalUtils.traceValue(user, "avatar");
		if (!WxBusinessCommon.judyString(photo)) {
			return photo;
		}
		// 查一次最新的
		User userLast = userFacade.getUserById(user.getId());
		photo = OptionalUtils.traceValue(userLast, "avatar");
		if (!WxBusinessCommon.judyString(photo)) {
			user.setAvatar(photo);
			return photo;
		}
		WebchatUser webchatUser = webchatFacade.searchWebChatUserByUserId(user
				.getId());
		if (webchatUser == null) {
			return null;
		}
		// 2016-08-19,当WebchatUser中有头像信息
		photo = webchatUser.getPhoto();
		// 剩下的自己更新
		webchatFacade.subscribeUpdateWxInfo(webchatUser);
		return photo;
	}

	public void setPriorUrl(HttpServletRequest request, String url) {
		setOpenid(url, request, WX_FOUND_PRIOR_URL);
	}

	public String getPriorUrl(HttpServletRequest request) {
		String url = getOpenId(WX_FOUND_PRIOR_URL, request);
		if (url != null) {
			removeOpenid(request, WX_FOUND_PRIOR_URL);
		}
		return url;
	}

	/**
	 * 2016-06-07 以后暂用
	 * 
	 * @param request
	 * @return
	 */
	public String errorMessage(HttpServletRequest request) {
		String error = getKeySession(WX_ERROR, request);
		if (error != null) {
			removeKeySession(request, WX_ERROR);
		}
		return error;
	}

	public ModelAndView redirectErrorPage(HttpServletRequest request,
			String errorMessage) {
		return redirectErrorPage(request, errorMessage, "wechat/error/error");
	}

	/**
	 * 给需要记录时间页面增加计时接口
	 */
	public void addPageRecordTime(HttpServletRequest request, int referType,
			int referId) {
		PageRemain pageRemain = buildInitRemain(request, referType, referId);
		pageRemain.setPlateform(Platform.WECHAT.name());
		User user = getCurrentUser(request);
		if (user != null) {
			pageRemain.setUserId(user.getId());
		}
		String openid = getOpenId(OPENID_KEY, request);
		if (openid != null) {
			pageRemain.setOpenId(openid);
		}
		ActionMessage<?> actionMessage = pageRemainFacade
				.addPageRemain(pageRemain);
		if (actionMessage.isSuccess()) {
			request.setAttribute(PAGE_REAMIN_UUID, pageRemain.getUuid());
		}
	}

	/**
	 * 资源观看增加观看次数，增加积分
	 */
	public void buildResCommonFirstView(HttpServletRequest request,
			Resource resource) {
		User user = getCurrentUser(request);
		if (user != null) {
			// 增加积分
			integralFacade.actionUserPlayResource(user, resource);
		}
		// 增加观看次数
		effectResourceFacade().countResourceView(resource);
	}

	/**
	 * 获取资源投票的公共方法
	 */
	public String buildCommonResVote(HttpServletRequest request,
			Resource resource) {
		User user = getCurrentUser(request);
		FrontResVote vote = buildCommonResVote(user, resource);
		return vote.toJSON();
	}

	/**
	 * 2016-06-15 之后要废弃 2016-4-29 修改 在分享有礼的详情页中记录，来自分享的点击事件 查看分享 @ 2016-06-21
	 * 做最新修改处理
	 */
	public void addViewShare(HttpServletRequest request, Resource resource,
			String oriUserUuid) {
		User user = getCurrentUser(request);
		if (user == null) {
			return;
		}
		if (oriUserUuid == null || oriUserUuid.isEmpty()) {
			return;
		}
		if (ResourceControl.NONE_USER.equals(oriUserUuid)) {
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
		@SuppressWarnings("unused")
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

	/**
	 * 2016-04-39 来自分享的资源点击，增加积分
	 */
	public void shareResAddIntegral(HttpServletRequest request,
			Resource resource, String oriUserUuid) {
		if (resource == null || !resource.isValid()) {
			return;
		}
		User user = getCurrentUser(request);
		if (user == null) {
			return;
		}
		if (oriUserUuid == null || oriUserUuid.isEmpty()) {
			return;
		}
		if (ResourceControl.NONE_USER.equals(oriUserUuid)) {
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
		@SuppressWarnings("unused")
		int effect = integralFacade.actionUserSharingViewed(shareUser,
				resource, user);
	}

	/**
	 * 视频详情页面
	 * 
	 * @param resource
	 * @param request
	 * @return
	 */
	public ModelAndView videDetailPage(HttpServletRequest request,
			Resource resource) {
		WxResourceItem item = buildResFirstPage(request, resource);
		WxVideo vo = new WxVideo();
		vo.buildData(resource.getVideo());
		item.setVideo(vo);
		request.setAttribute("res_vo", item);
		//2016-12-6 判断是否是vr
		if(item.isVrFlag()){
			bulidRequest("微信查看VR首页", resource, resource.getUuid(), request);
		}else{
			//视频
			/**
			 * 2016-06-14 记录页面时长
			 */
			addPageRecordTime(request, PageRemain.RESOURCE_DETAIL_TYPE,
					resource.getId());

			bulidRequest("微信查看视频详情", resource, resource.getUuid(), request);
		}
		return redirectPageView("wechat/resource/video_detail");
	}
	
	/**
	 * vr 视频详情播放
	 */
//	public ModelAndView vrDetailPage(HttpServletRequest request,
//			Resource resource) {
//		WxResourceItem item = new WxResourceItem();
//		item.buildDetailLittle(resource);
//		request.setAttribute("res_vo", item);
//		
//		/**
//		 * 2016-06-14 记录页面时长
//		 */
//		addPageRecordTime(request, PageRemain.RESOURCE_DETAIL_TYPE,
//				resource.getId());
//		
//		bulidRequest("微信查看VR播放详情", resource, resource.getUuid(), request);
//		return redirectPageView("wechat/resource/vr_video_detail");
//	}
	
	/**
	 * 2017-01-12
	 * vr视频详情跳转第3方页面
	 * @param request
	 * @param resource
	 * @return
	 */
	public ModelAndView vrDetailPage(HttpServletRequest request,
			Resource resource) {
		WxVideo wxVideo=new WxVideo();
		wxVideo.buildData(resource.getVideo());
		
		String url=Config.qn_cdn_host+"/227_u2vr.html?playUrl="+Base64Data.encodeBy64(wxVideo.getVrUrl());
		int random=new Random().nextInt();
		url=url+"&timestmp="+System.currentTimeMillis()+"&randdom="+random;
		
		bulidRequest("微信查看VR播放详情", resource, resource.getUuid(), request);
		return redirectUrlView(url);
	}

	/**
	 * pdf第一级页面
	 * 
	 * @param resource
	 * @param request
	 * @return
	 */
	public ModelAndView pdfFirstPage(HttpServletRequest request,
			Resource resource) {
		WxResourceItem item = buildResFirstPage(request, resource);
		WxPdf vo = new WxPdf();
		vo.build(resource.getPdf());
		item.setPdf(vo);
		request.setAttribute("res_vo", item);
		bulidRequest("微信查看pdf详情", resource, resource.getUuid(), request);
		return redirectPageView("wechat/resource/pdf_first");
	}

	/**
	 * pdf详情页面播放
	 * 
	 * @param request
	 * @param resource
	 * @return
	 */
	public ModelAndView pdfDetailPage(HttpServletRequest request,
			Resource resource) {
		WxResourceItem item = new WxResourceItem();
		item.buildDetailLittle(resource);
		request.setAttribute("res_vo", item);

		// 公共增加播放次数和积分
		buildResCommonFirstView(request, resource);
		/**
		 * 2016-06-14 记录页面时长
		 */
		addPageRecordTime(request, PageRemain.RESOURCE_DETAIL_TYPE,
				resource.getId());

		bulidRequest("微信播放PDF", resource, resource.getUuid(), request);
		return redirectPageView("wechat/resource/pdf_detail");
	}

	/**
	 * 三分屏第一级页面
	 * 
	 * @param resource
	 * @param request
	 * @return
	 */
	public ModelAndView threeScreenFirstPage(HttpServletRequest request,
			Resource resource) {
		WxResourceItem item = buildResFirstPage(request, resource);
		WxThreeScreen vo = new WxThreeScreen();
		vo.buildFirstPage(resource.getThreeScreen());

		item.setThreeScreen(vo);

		request.setAttribute("res_vo", item);
		bulidRequest("微信查看三分屏详情", resource, resource.getUuid(), request);
		return redirectPageView("wechat/resource/threescreen_first");
	}

	/**
	 * 三分屏详情页
	 * 
	 * @param request
	 * @param resource
	 * @return
	 */
	public ModelAndView threeScreenDetailPage(HttpServletRequest request,
			Resource resource) {
		WxResourceItem item = new WxResourceItem();
		item.buildDetailLittle(resource);
		request.setAttribute("res_vo", item);

		/**
		 * 2016-06-14 记录页面时长
		 */
		addPageRecordTime(request, PageRemain.RESOURCE_DETAIL_TYPE,
				resource.getId());

		bulidRequest("微信播放三分屏", resource, resource.getUuid(), request);
		return redirectPageView("wechat/resource/threescreen_detail");
	}

	/**
	 * 新闻数据详情
	 * 
	 * @param request
	 * @param resource
	 * @return
	 */
	public ModelAndView newsDetailPage(HttpServletRequest request,
			Resource resource) {
		WxResourceItem item = new WxResourceItem();
		item.buildDetailLittle(resource);
		request.setAttribute("res_vo", item);

		String categoryName = OptionalUtils.traceValue(resource,
				"category.name");
		if (categoryName.equals("分享有礼")) {
			// 分享有礼
			bulidRequest("微信查看分享有礼详情", resource, resource.getUuid(), request);
			return redirectPageView("wechat/resource/share_gift_detail");
		}
		// 增加观看次数
		effectResourceFacade().countResourceView(resource);
		// 一般新闻详情
		bulidRequest("微信查看新闻详情", resource, resource.getUuid(), request);
		return redirectPageView("wechat/resource/news_detail");
	}

	public ModelAndView commonResourceFirstView(HttpServletRequest request,
			String uuid) {
		Resource resource = effectResourceFacade().getResourceByUuid(uuid);
		return commonResourceFirstView(request, resource);
	}

	public ModelAndView commonResourceFirstView(HttpServletRequest request,
			Resource resource) {
		if (resource == null || !resource.isValid())
			return redirectErrorPage(request, "资源不存在或已下线");
		User user = getCurrentUser(request);
		  /**
	     * 2016-12-15 添加权限
	     */
		ResourceAuthorityUtil resourceAuthorityUtil=resourceAuthority(user,resource);
		if(resourceAuthorityUtil!=null){
			//判断不能浏览的权限
			if(!resourceAuthorityUtil.isReScan()){
				return redirectErrorPage(request, resourceAuthorityUtil.getReScanResult());
			}
			
			//可以浏览
			if(!resourceAuthorityUtil.isResView()){
				//不能观看
				if(resource.getType()==Type.NEWS){
					return redirectErrorPage(request, resourceAuthorityUtil.getReScanResult());
				}else if(resource.getType()==Type.VIDEO){
					request.setAttribute("res_auth_view", "0");
					request.setAttribute("res_auth_view_val", resourceAuthorityUtil.getResViewResult());
					if(resourceAuthorityUtil.isNeedLogin()){
						request.setAttribute("res_auth_view_login", "1");
					}
				}
			}
		}
		return resourceLogingPage(request, resource);

		/**
		 * 2016-07-22 @he 修改 无登录用户，返回资源首页
		 */
//		if (user == null) {
//			return resourceNotLoginPage(request, resource);
//		}
//		return resourceLogingPage(request, resource);
	}
	
	
	

	

	public ModelAndView commonResourceViewDetail(HttpServletRequest request,
			String uuid) {
		Resource resource = effectResourceFacade().getResourceByUuid(uuid);
		if (resource == null || !resource.apiUseable()) {
			return redirectErrorPage(request, "资源不存在或已下线");
		}
		User user = getCurrentUser(request);
		  /**
	     * 2016-12-15 添加权限
	     */
		ResourceAuthorityUtil resourceAuthorityUtil=resourceAuthority(user,resource);
		if(resourceAuthorityUtil!=null){
			if(!resourceAuthorityUtil.isResView()){
				//不能观看
				return redirectErrorPage(request, resourceAuthorityUtil.getResViewResult());
			}
		}
		
		if (resource.getType() == Type.PDF) {
			return pdfDetailPage(request, resource);
		} else if (resource.getType() == Type.THREESCREEN) {
			return threeScreenDetailPage(request, resource);
		}else if(resource.getType()==Type.VIDEO){
			//vr视频
			return vrDetailPage(request, resource);
		}
		return redirectErrorPage(request, "资源数据类型未识别");
	}

	/**
	 * 当资源首页没登录时，返回的页面
	 * 
	 * @param request
	 * @param resource
	 * @return
	 */
	public ModelAndView resourceNotLoginPage(HttpServletRequest request,
			Resource resource) {
		if (resource.getType() == Type.NEWS) {
			return newsDetailPage(request, resource);
		} else if (resource.getType() == Type.VIDEO) {
			WxResourceItem item = buildResFirstPage(request, resource);
			WxVideo vo = new WxVideo();
			vo.buildData(resource.getVideo());
			item.setVideo(vo);
			request.setAttribute("res_vo", item);
			return redirectPageView("wechat/resource/resource_preview");
		} else if (resource.getType() == Type.PDF) {
			WxResourceItem item = buildResFirstPage(request, resource);
			WxPdf vo = new WxPdf();
			vo.build(resource.getPdf());
			item.setPdf(vo);
			request.setAttribute("res_vo", item);
			return redirectPageView("wechat/resource/resource_preview");
		} else if (resource.getType() == Type.THREESCREEN) {
			WxResourceItem item = buildResFirstPage(request, resource);
			WxThreeScreen vo = new WxThreeScreen();
			vo.buildFirstPage(resource.getThreeScreen());
			item.setThreeScreen(vo);
			request.setAttribute("res_vo", item);
			return redirectPageView("wechat/resource/resource_preview");
		}
		return redirectErrorPage(request, "资源数据类型未识别");
	}




	/**
	 * @he 2016-07-22 资源登录后返回的首页
	 * @param request
	 * @param resource
	 * @return
	 */
	public ModelAndView resourceLogingPage(HttpServletRequest request,
			Resource resource) {
		if (resource.getType() == Type.VIDEO) {
			return videDetailPage(request, resource);
		} else if (resource.getType() == Type.PDF) {
			return pdfFirstPage(request, resource);
		} else if (resource.getType() == Type.THREESCREEN) {
			return threeScreenFirstPage(request, resource);
		} else if (resource.getType() == Type.NEWS) {
			return newsDetailPage(request, resource);
		}
		return redirectErrorPage(request, "资源数据类型未识别");
	}

	/**
	 * 获取用户的姓名和头像
	 */
	public WxUserShowInfo wxGetUserinfo(HttpServletRequest request) {
		User user = getCurrentUser(request);
		return wxGetUserinfo(user);
	}

	public WxUserShowInfo wxGetUserinfo(User user) {
		WxUserShowInfo info = new WxUserShowInfo();
		String photo = "", name = "";
		if (user != null) {
			photo = OptionalUtils.traceValue(user, "avatar");
			Certification certification = certificationFacade
					.getCertifiActiveByUserId(user);
			if (certification == null) {
				name = OptionalUtils.traceValue(user, "nickname");
			} else {
				name = OptionalUtils.traceValue(certification, "name");
			}
		}
		photo = WxUtil.getDefaultAvatar(photo);
		info.setShowName(name);
		info.setPhoto(photo);
		return info;
	}

	public String wxShowMessageList(HttpServletRequest request, int modelId,
			int referType, String startTime, int size) {
		if (size > 20) {
			size = 20;
		}
		User user = getCurrentUser(request);
		List<Message> messages = messageFacade.searchCommentsForWxResource(
				modelId, referType, Message.COMMENT_TYPE, startTime, size);
		WxCommentList commentList = new WxCommentList();
		commentList.buildCommentVos(user, messages, messageFacade,
				webchatFacade, commonPraiseFacade);
		return commentList.toJSON(JsonConvertor.JACKSON);
	}

}
