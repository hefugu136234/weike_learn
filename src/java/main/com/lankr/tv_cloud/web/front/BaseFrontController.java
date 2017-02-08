package com.lankr.tv_cloud.web.front;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.APIFacade;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.PageRemain;
import com.lankr.tv_cloud.model.QrCode;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.ActivityTotalApiData;
import com.lankr.tv_cloud.vo.api.BaseAPIModel.JsonConvertor;
import com.lankr.tv_cloud.web.BaseWebController;
import com.lankr.tv_cloud.web.api.webchat.util.ResourceAuthorityUtil;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;
import com.lankr.tv_cloud.web.front.util.FrontWxOpenUtil;
import com.lankr.tv_cloud.web.front.vo.FrontBannerList;
import com.lankr.tv_cloud.web.front.vo.FrontCommentList;
import com.lankr.tv_cloud.web.front.vo.FrontNewsVo;
import com.lankr.tv_cloud.web.front.vo.FrontPdfVo;
import com.lankr.tv_cloud.web.front.vo.FrontResVote;
import com.lankr.tv_cloud.web.front.vo.FrontResourceItem;
import com.lankr.tv_cloud.web.front.vo.FrontThreeScreenVo;
import com.lankr.tv_cloud.web.front.vo.FrontVideoVo;
import com.lankr.tv_cloud.web.front.vo.TopMenuItem;
import com.lankr.tv_cloud.web.front.vo.UserFrontBaseView;

public class BaseFrontController extends BaseWebController {
	public static final String FRONT_USER_SESSION_KEY = "front_user_session_key";
	// 前台pc controller需要处理的事情
	public static final String FRONT_FOUND_PRIOR_URL = "front_found_prior_url";

	public static final String UNIONID_KEY = "unionid_key";

	public static final String USER_VIEW_KEY = "user_view_key";

	public static final String PC_PRIOR = "/f/web";

	@Override
	public User getCurrentUser(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Integer userId = (Integer) request.getSession().getAttribute(
				FRONT_USER_SESSION_KEY);
		if (userId == null)
			return null;
		User user = getUserCache().get(userId, TimeUnit.MINUTES.toMillis(5),
				true);
		return user;
	}

	protected void setCurrentSessionUser(User user, HttpServletRequest request) {
		if (user != null) {
			getUserCache().put(user.getId(), user);
			request.getSession().setAttribute(FRONT_USER_SESSION_KEY,
					user.getId());
			// 将user必要信息保存(之后重新规划)
			// buildShowBaseView(request,user);
		}
	}

	protected void removeCurrentUser(HttpServletRequest request) {
		User user = getCurrentUser(request);
		request.getSession().removeAttribute(FRONT_USER_SESSION_KEY);
		if (user != null) {
			getUserCache().remove(user.getId());
		}
	}

	public UserFrontBaseView getUserView(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Object object = request.getSession().getAttribute(USER_VIEW_KEY);
		if (object == null) {
			return null;
		}
		return (UserFrontBaseView) object;
	}

	protected void setUserView(UserFrontBaseView userview,
			HttpServletRequest request) {
		request.getSession().setAttribute(USER_VIEW_KEY, userview);
	}

	protected void removeUserVie(HttpServletRequest request) {
		request.getSession().removeAttribute(USER_VIEW_KEY);
	}

	public static final String LOGIN_URL = PC_PRIOR + "/login";

	public static final String AUTH_OPEN_LOGIN_URL = PC_PRIOR
			+ "/auth/logined/before";

	public static final String LOGINED_REDIRECT = PC_PRIOR
			+ "/logined/redirect/url";

	public static final String INDEX_URL = PC_PRIOR + "/index";

	public static final String REG_URL = PC_PRIOR + "/reg";

	public static final String ERROR_URL = PC_PRIOR + "/error/page";

	public static final String NOT_SAO_WX = "不是来自微信扫码登录";

	public static final String NOT_AUTH_LOGIN = "此用户无登录权限";

	// public static final String NOT_SUBSCRIBE="请先关注知了云盒微信公账号（扫描右上角二维码）";

	public static final String LOGIN_ING_ERROR = "login_ing_error";

	public static final String REG_ONE_PART = "1";

	public static final String REG_SECOND_PART = "2";

	public static final String AGAIN_SAO_LOGIN = "请重新扫码登录";

	// 判断user，执行跳转
	public String authJudyUser(HttpServletRequest request, String code,
			String state) {
		User user = getCurrentUser(request);
		if (user != null) {
			return LOGINED_REDIRECT;
		}
		if (state == null || !state.equals(WebChatMenu.ZHILIAO_STATE)) {
			setKeySession(NOT_SAO_WX, request, LOGIN_ING_ERROR);
			return ERROR_URL;
		}
		String unionid = getKeySession(UNIONID_KEY, request);
		if (unionid == null) {
			unionid = FrontWxOpenUtil.getUnionidByCode(code);
			if (unionid == null || unionid.isEmpty()) {
				setKeySession(NOT_SAO_WX, request, LOGIN_ING_ERROR);
				return ERROR_URL;
			}
			setKeySession(unionid, request, UNIONID_KEY);
		}
		System.out.println("授权获取unionid:" + unionid);
		// 获取webuse
		WebchatUser webchatUser = webchatFacade
				.searchWebChatUserByUnionid(unionid);
		if (webchatUser == null) {
			// 跳转注册
			return REG_URL;
		}
		user = webchatUser.getUser();
		if (user == null) {
			return REG_URL;
		}
		if (!loginIsAbel(user)) {
			setKeySession(NOT_AUTH_LOGIN, request, LOGIN_ING_ERROR);
			return ERROR_URL;
		}
		setCurrentSessionUser(user, request);
		return LOGINED_REDIRECT;
	}

	public String getPriorUrl(HttpServletRequest request) {
		String url = getKeySession(FRONT_FOUND_PRIOR_URL, request);
		if (url != null) {
			removeKeySession(request, FRONT_FOUND_PRIOR_URL);
		}
		return url;
	}
	
	public void setPriorUrl(HttpServletRequest request, String url) {
		setKeySession(url, request, FRONT_FOUND_PRIOR_URL);
	}

	public String errorMessage(HttpServletRequest request) {
		String error = getKeySession(LOGIN_ING_ERROR, request);
		if (error != null) {
			removeKeySession(request, LOGIN_ING_ERROR);
		}
		return error;
	}

	public String regBeforePage(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user != null) {
			return LOGINED_REDIRECT;
		}
		String unionid = getKeySession(UNIONID_KEY, request);
		if (unionid == null) {
			setKeySession(AGAIN_SAO_LOGIN, request, LOGIN_ING_ERROR);
			return INDEX_URL;
		}
		return null;
	}

	public ModelAndView redirectErrorPage(HttpServletRequest request,
			String errorMessage) {
		return redirectErrorPage(request, errorMessage, "web/error");
	}

	public String regRedirectPart(HttpServletRequest request) {
		String unionid = getKeySession(UNIONID_KEY, request);
		WebchatUser webchatUser = webchatFacade
				.searchWebChatUserByUnionid(unionid);
		if (webchatUser == null) {
			// 跳转注册
			return REG_ONE_PART;
		}
		return REG_SECOND_PART;
	}

	public UserFrontBaseView buildShowBaseView(HttpServletRequest request,
			User user) {
		UserFrontBaseView view = new UserFrontBaseView();
		view.setStatus(Status.SUCCESS);
		view.setId(user.getId());
		view.setUuid(user.getUuid());
		view.setLogin(true);
		if (default_project == null) {
			default_project = projectFacade.getProjectByUuid(PROJECT_UUID);
		}
		UserReference reference = userFacade.searchUserReference(user,
				default_project);
		String isDateValid = reference.isDateStr();// no_use in_use late_use
		view.setVip(isDateValid);

		String deadTime = "";
		if ("in_use".equals(isDateValid)) {
			deadTime = Tools.df.format(reference.getValidDate());
		}
		view.setVipDeadTime(deadTime);

		// int score = integralFacade.fetchUserIntegralTotal(user);

		Certification certification = certificationFacade
				.getCertifiActiveByUserId(user);

		if (certification == null) {
			view.setRealName(false);
			view.setShowName(OptionalUtils.traceValue(user, "nickname"));
			view.setRealNameInfo("");
		} else {
			view.setRealName(true);
			view.setShowName(OptionalUtils.traceValue(certification, "name"));
			view.setRealNameInfo(OptionalUtils.traceValue(certification,
					"credentials"));
		}

		String photo = OptionalUtils.traceValue(user, "avatar");
		if (photo.isEmpty()) {
			WebchatUser webchatUser = webchatFacade
					.searchWebChatUserByUserId(user.getId());
			photo = OptionalUtils.traceValue(webchatUser, "photo");
			//假如photo有值，更新photo
			if(!Tools.isBlank(photo)){
				user.setAvatar(photo);
				userFacade.updateUserAvatar(user);
			}
		}
		view.setPhoto(photo);

		setUserView(view, request);
		return view;
	}

	@Override
	protected ResourceFacade effectResourceFacade() {
		return cacheResourceFacade;
	}

	// 公共模块获取banner
	public void getCommonBanner(HttpServletRequest request) {
		getCommonBanner(request, Banner.POSITION_INDEX);
	}

	public void getCommonBanner(HttpServletRequest request, int position) {
		List<Banner> banerList = bannerFacade.getWxBanner(Banner.TYPE_WEB,
				position);
		FrontBannerList vo = new FrontBannerList();
		vo.buildBanner(banerList);
		request.setAttribute("banner_list", vo);
	}

	@Override
	protected APIFacade effectApiFacade() {
		return cacheAPIFacade;
	}

	protected ActivityTotalApiData activityCompletedJson(Activity activity) {
		ActivityTotalApiData data = effectApiFacade().activityCompletedJson(
				activity);
		return data;
	}

	/**
	 * 获取公共resource的基本属性
	 * 
	 * @param resource
	 * @param request
	 * @return
	 */
	public FrontResourceItem buildResourceDetail(Resource resource,
			HttpServletRequest request) {
		FrontResourceItem item = new FrontResourceItem();
		// 基本数据
		item.buildItemList(resource);
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
		// 所在科目目录
		List<TopMenuItem> menuList = new ArrayList<TopMenuItem>();
		WxSubject wxSubject = wxSubjectFacade
				.searchWxSubjectByCategoryId(OptionalUtils.traceInt(resource,
						"category.id"));
		do {
			if (wxSubject == null) {
				break;
			}
			TopMenuItem menuItem = new TopMenuItem();
			menuItem.buildData(wxSubject);
			menuList.add(menuItem);
			wxSubject = wxSubject.getParent();
		} while (wxSubject != null);
		if (!menuList.isEmpty()) {
			Collections.reverse(menuList);
		}
		item.setMenuList(menuList);

		QrCode qrCode = qrCodeFacade.getQrByResource(resource);
		item.setQr(OptionalUtils.traceValue(qrCode, "qrurl"));

		/**
		 * 2016-06-14 记录页面时长
		 */
		addPageRecordTime(request, PageRemain.RESOURCE_DETAIL_TYPE,
				resource.getId());

		return item;
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
	 * 给需要记录时间页面增加计时接口
	 */
	public void addPageRecordTime(HttpServletRequest request, int referType,
			int referId) {
		PageRemain pageRemain = buildInitRemain(request, referType, referId);
		pageRemain.setPlateform(Platform.PCWEB.name());
		User user = getCurrentUser(request);
		if (user != null) {
			pageRemain.setUserId(user.getId());
		}
		String unionid = getKeySession(UNIONID_KEY, request);
		if (unionid != null) {
			pageRemain.setUnionid(unionid);
		}
		ActionMessage<?> actionMessage = pageRemainFacade
				.addPageRemain(pageRemain);
		if (actionMessage.isSuccess()) {
			request.setAttribute(PAGE_REAMIN_UUID, pageRemain.getUuid());
		}
	}

	public ModelAndView videoVo(Resource resource, HttpServletRequest request) {
		FrontResourceItem item = buildResourceDetail(resource, request);
		FrontVideoVo vo = new FrontVideoVo();
		vo.buildData(resource.getVideo());
		item.setVideoVo(vo);
		request.setAttribute("res_data", item);
		bulidRequest("web查看视频详情", resource, resource.getUuid(), request);
		return redirectPageView("web/video");
	}

	public ModelAndView pdfVo(Resource resource, HttpServletRequest request) {
		FrontResourceItem item = buildResourceDetail(resource, request);
		FrontPdfVo vo = new FrontPdfVo();
		vo.build(resource.getPdf());
		item.setPdfVo(vo);
		request.setAttribute("res_data", item);

		// 增加积分和观看次数
		buildResCommonFirstView(request, resource);

		bulidRequest("web查看pdf详情", resource, resource.getUuid(), request);
		return redirectPageView("web/pdf");
	}

	public ModelAndView threeScreenVo(Resource resource,
			HttpServletRequest request) {
		FrontResourceItem item = buildResourceDetail(resource, request);
		FrontThreeScreenVo threeScreenVo = new FrontThreeScreenVo();
		threeScreenVo.buildData(resource.getThreeScreen());
		item.setThreeScreenVo(threeScreenVo);
		request.setAttribute("res_data", item);

		bulidRequest("web查看三分屏详情", resource, resource.getUuid(), request);
		return redirectPageView("web/video_split");
	}

	// 新闻不需要这些数据
	public ModelAndView newsVo(Resource resource, HttpServletRequest request) {
		FrontResourceItem item = new FrontResourceItem();
		item.buildItemList(resource);
		FrontNewsVo vo = new FrontNewsVo();
		vo.build(resource.getNews());
		item.setNewsVo(vo);
		request.setAttribute("res_data", item);

		// 增加观看次数
		effectResourceFacade().countResourceView(resource);

		bulidRequest("web查看新闻详情", resource, resource.getUuid(), request);
		return redirectPageView("web/news_detail");
	}

	public ModelAndView resorceDetailCommon(HttpServletRequest request,
			String uuid) {
		Resource resource = getResourceByUuid(uuid);
		return resorceDetailCommon(request, resource);
	}

	public ModelAndView resorceDetailCommon(HttpServletRequest request,
			Resource resource) {
		if (resource == null || !resource.apiUseable()) {
			return redirectErrorPage(request, "资源不存在或已下线");
		}
		
		/**
		 * 2017-01-11 新增权限控制 暂时先不添加
		 */
//		User user = getCurrentUser(request);
//		ResourceAuthorityUtil resourceAuthorityUtil=resourceAuthority(user,resource);
//		if(resourceAuthorityUtil!=null){
//			//判断不能浏览的权限
//			if(!resourceAuthorityUtil.isReScan()){
//				return redirectErrorPage(request, resourceAuthorityUtil.getReScanResult());
//			}
//			
//			//可以浏览
//			if(!resourceAuthorityUtil.isResView()){
//				//不能观看
//				if(resource.getType()==Type.NEWS){
//					return redirectErrorPage(request, resourceAuthorityUtil.getReScanResult());
//				}else if(resource.getType()==Type.VIDEO){
//					request.setAttribute("res_auth_view", "0");
//					request.setAttribute("res_auth_view_val", resourceAuthorityUtil.getResViewResult());
//					if(resourceAuthorityUtil.isNeedLogin()){
//						request.setAttribute("res_auth_view_login", "1");
//					}
//				}
//			}
//		}
		
		return resorceDetailPage(request,resource);
	}
	
	public ModelAndView resorceDetailPage(HttpServletRequest request,
			Resource resource){
		if (resource.getType() == Type.VIDEO) {
			return videoVo(resource, request);
		} else if (resource.getType() == Type.PDF) {
			return pdfVo(resource, request);
		} else if (resource.getType() == Type.THREESCREEN) {
			return threeScreenVo(resource, request);
		} else if (resource.getType() == Type.NEWS) {
			return newsVo(resource, request);
		}
		return redirectErrorPage(request, "资源数据出错");
	}
	
	
	public String webShowMessageList(HttpServletRequest request, int modelId,
			int referType, int currentPage, int size) {
		String sign = request.getParameter("first") ;
		int from = Math.max(currentPage - 1, 0);
		if (size > 20) {
			size = 20;
		}
		from = from * size;
		User user = getCurrentUser(request) ;
		List<Message> messages = messageFacade.searchCommentsForResource(modelId, referType, Message.COMMENT_TYPE, from, size) ;
		FrontCommentList commentList = new FrontCommentList() ;
		try {
			if (!Tools.isBlank(sign)&&"sign".equals(sign))
				commentList.buildCommentVosPage(user, messages, messageFacade, webchatFacade, commonPraiseFacade,referType);
			else 
				commentList.buildCommentVos(user, messages, messageFacade, webchatFacade, commonPraiseFacade);
		} catch (Exception e) {
			e.printStackTrace();
			return failureModel("加载出错").toJSON();
		}
		
		return commentList.toJSON(JsonConvertor.JACKSON);
	}

}
