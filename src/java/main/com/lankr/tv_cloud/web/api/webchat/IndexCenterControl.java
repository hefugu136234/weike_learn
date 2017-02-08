package com.lankr.tv_cloud.web.api.webchat;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.WebchatFacadeImp;
import com.lankr.tv_cloud.model.ActivationCode;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.ApplicableRecords;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.RegisterTmp;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.utils.Md5;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.SimpleData;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.BaseAPIController;
import com.lankr.tv_cloud.web.api.webchat.util.WxBusinessCommon;
import com.lankr.tv_cloud.web.api.webchat.vo.VipInfo;
import com.lankr.tv_cloud.web.api.webchat.vo.WxIndexvo;
import com.lankr.tv_cloud.web.api.webchat.vo.WxMenuItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxResourceList;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;
import com.lankr.tv_cloud.web.api.webchat.vo.WxUserInfoChange;
import com.lankr.tv_cloud.web.api.webchat.vo.WxUserShowInfo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class IndexCenterControl extends BaseWechatController {
	/**
	 * 2016-04-27 登录 注册 个人中心的所有信息
	 */

	public final static String ACTIVE_CODE_TOKEN = "ACTIVE_CODE_TOKEN";

	/**
	 * 测试的登录页面，用于调试，正式删除
	 * 
	 * @param request
	 * @return,
	 */
	@RequestMapping(value = "/test/page/login/{openid}", method = RequestMethod.GET)
	public ModelAndView pageLo(HttpServletRequest request,
			@PathVariable String openid) {
		// String openid="abc";
//		if(Config.isProductEnv()){
//			return redirectErrorPage(request, "正式网站无测试内容");
//		}
		WebchatUser webchatUser = webchatFacade
				.searchWebChatUserByOpenid(openid);
		WebchatFacadeImp imp = new WebchatFacadeImp();
		System.out.println("11111111111");
		if (webchatUser == null) {
			// 新建--openid
			webchatUser = WxBusinessCommon.buildWebchatUser(openid);
			Status status = webchatFacade.addWebChatUser(webchatUser);
		}
		setOpenid(openid, request, OPENID_KEY);
		if (webchatUser.getUser() == null) {
			return new ModelAndView("wechat/login");
		} else {
			setCurrentSessionUser(webchatUser.getUser(), request);
			return new ModelAndView("redirect:/api/webchat/index");
		}
	}

	/**
	 * /api/webchat/page/login 登录的页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/page/login", method = RequestMethod.GET)
	public ModelAndView pageLogin(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null) {
			return redirectPageView("wechat/login");
		}
		return redirectUrlView(WX_PRIOR + "/logined/page");
	}

	

	/**
	 * 登录操作
	 * 
	 * @he 2016-06-12 最新修改登录
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String login(@RequestParam String username,
			@RequestParam String password, HttpServletRequest request) {
		User user = userFacade.login(username, Md5.getMd5String(password));
		if (user == null) {
			bulidRequest("微信用户登录失败", "user", null, Status.FAILURE.message(),
					null, "username=" + username + " password=" + password
							+ " 用户名或密码错误", request);
			return failureModel("用户名或密码错误").toJSON();
		} else {
			if (!loginIsAbel(user)) {
				return failureModel("用户当前无权限登录").toJSON();
			}
			setCurrentSessionUser(user, request);
			bulidRequest("微信用户登录成功", "user", user.getId(),
					Status.SUCCESS.message(), null, null, request);
			String openId = getOpenId(OPENID_KEY, request);
			if (openId == null) {
				return successModel().toJSON();
			}
			WebchatUser webchatUser = webchatFacade.buildTotalWebUser(openId);
			if (webchatUser == null) {
				return successModel().toJSON();
			}
			webchatUser.setUser(user);
			webchatFacade.updateWebChatUser(webchatUser);
			return successModel().toJSON();
		}
	}

	/**
	 * @he 2016-06-28 以后 优化登出
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/out/login", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String outLogin(HttpServletRequest request) {
		User user = getCurrentUser(request);
		String openId = getOpenId(OPENID_KEY, request);
		if (openId == null) {
			WebchatUser webchatUser = webchatFacade
					.searchWebChatUserByUserId(user.getId());
			if (webchatUser != null) {
				openId = webchatUser.getOpenId();
			}
		}
		if (openId == null) {
			return failureModel("安全登出失败").toJSON();
		}
		ActionMessage<?> actionMessage = webchatFacade.unbindWechat(openId);
		if (!actionMessage.isSuccess()) {
			return failureModel("安全登出失败").toJSON();
		}
		// 移除session
		removeCurrentUser(request);
		bulidRequest("微信用户安全登出", "user", user.getId(),
				Status.SUCCESS.message(), null, null, request);
		return successModel().toJSON();
	}

	/**
	 * 登录注册后，返回的页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/logined/page", method = RequestMethod.GET)
	public ModelAndView loginedPage(HttpServletRequest request) {
		String url = getPriorUrl(request);
		if (url == null) {
			url = WX_INDEX;
		}
		return redirectUrlView(url);
	}

	/**
	 * 没有登录权限的返回页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/isable/page/login", method = RequestMethod.GET)
	public ModelAndView isableLogin(HttpServletRequest request) {
		return redirectErrorPage(request, "您的账户信息已被禁用，请与管理员联系。");
	}

	/**
	 * 注册页面
	 * 
	 * @param username
	 * @param password
	 * @param request
	 * @return
	 * 2016-08-16 注册做新的优化
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null) {
			return redirectPageView("wechat/reg");
		}
		return redirectUrlView(WX_PRIOR + "/logined/page");
	}

	// 注册获取验证码
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/register/code/send", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String fetchRegisterCode(@RequestParam String mobile,
			HttpServletRequest request) {
		if (mobile == null || !Tools.isValidMobile(mobile)) {
			return failureModel("手机号码不正确").toJSON();
		}
		Status s = userFacade.sendRegisterCode(mobile, "",
				BaseController.getClientIpAddr(request));
		if (s == Status.IN_USE) {
			return failureModel("该手机号已注册").toJSON();
		} else if (s == Status.OPERATION_FAST) {
			return failureModel("请在60s之后重试").toJSON();
		} else if (s == Status.SUCCESS) {
			return successModel().toJSON();
		}
		return failureModel("验证码发送失败，请稍后再试").toJSON();
	}

	// 验证注册码
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/register/code/valid", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String validRegisterCode(@RequestParam String code,
			String mobile) {
		RegisterTmp tmp = userFacade.validRegisterCode(code, mobile);
		if (tmp == null || !tmp.isActive()) {
			return failureModel("验证码错误").toJSON();
		}
		if (Math.abs(System.currentTimeMillis() - tmp.getCreateDate().getTime()) > 10 * 60 * 1000) {
			return failureModel("验证码已过期").toJSON();
		}
		return BaseAPIModel.makeSimpleSuccessInnerDataJson(new SimpleData(
				"valid_code", tmp.getUuid()));
	}

	

	/**
	 * 
	 * @param valid_code
	 * @param password
	 * @param nickname
	 * @param token
	 * @param type
	 * @param hosipital
	 * @param professor
	 * @param city
	 * @param department
	 * @param activeCode
	 * @param company
	 * @param request
	 * @param response
	 * @return
	 * @he 2016-06-12 最新注册修改
	 * type=0 医生用户 type=1 企业用户
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/user/register/save", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String saveRegisterUser(String valid_code,
			String password, @RequestParam String nickname,
			@RequestParam int type,
			@RequestParam(required = false) String hosipital,
			@RequestParam(required = false) String professor,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) String department,
			@RequestParam(required = false) String activeCode,
			@RequestParam(required = false) String company,
			HttpServletRequest request) {
		RegisterTmp tmp = userFacade.getRegisterTmpByUuid(valid_code);
		if (tmp == null || !tmp.isActive()) {
			bulidRequest("微信个人注册", "user", null, Status.FAILURE.message(),
					null, "nickname=" + nickname + " 手机号暂不可用,注册失败", request);
			return failureModel("注册的手机号暂不可用，请重新验证！").toJSON();
		}
		User user = userFacade.getUserByUsername(tmp.getMobile());
		if (user != null) {
			bulidRequest("微信个人注册", "user", null, Status.FAILURE.message(),
					null, "手机号=" + tmp.getMobile() + " 已被注册", request);
			return failureModel("该手机号已被注册！").toJSON();
		}

		if (default_project == null) {
			default_project = projectFacade.getProjectByUuid(PROJECT_UUID);
		}
		String uuid = Tools.getUUID();
		user = new User();
		user.setNickname(nickname);
		if (nickname != null && !nickname.isEmpty()) {
			user.setPinyin(Tools.getPinYin(nickname));
		} else {
			user.setPinyin("");
		}

		user.setUsername(tmp.getMobile());
		user.setPhone(tmp.getMobile());
		user.setUuid(uuid);
		user.setIsActive(BaseModel.ACTIVE);
		user.setPassword(Md5.getMd5String(password));
		user.setMainRole(getRole(Role.PRO_USER_LEVEL));

		String re_uuid = Tools.getUUID();
		UserReference ur = new UserReference();
		ur.setProject(default_project);
		ur.setRole(getRole(Role.PRO_CUSTOMER));
		ur.setUuid(re_uuid);
		if (type == 1) {
			// 企业要激活用户本身状态
			if (activeCode != null && !activeCode.isEmpty()) {
				ur.setIsActive(BaseModel.ACTIVE);
			} else {
				ur.setIsActive(BaseModel.DISABLE);
			}
		} else {
			ur.setIsActive(BaseModel.ACTIVE);
		}

		UserExpand userExpand = new UserExpand();
		userExpand.setUuid(Tools.getUUID());
		userExpand.setUser(user);
		userExpand.setStatus(BaseModel.APPROVED);
		userExpand.setIsActive(BaseModel.ACTIVE);
		userExpand.setMark("");
		userExpand.setBirthday("");
		userExpand.setType(type);
		if (city != null && !city.isEmpty()) {
			City city_model = provinceMapper.selectCtiByUUid(city);
			if (city_model != null) {
				userExpand.setCity(city_model);
			}
		}
		if (hosipital != null && !hosipital.isEmpty()) {
			Hospital hospital_model = hospitalMapper
					.selectHospitalByUUid(hosipital);
			if (hospital_model != null) {
				userExpand.setHospital(hospital_model);
			}
		}
		if (department != null && !department.isEmpty()) {
			Departments departments_model = hospitalMapper
					.selectDePatByUuid(department);
			if (departments_model != null) {
				userExpand.setDepartments(departments_model);
			}
		}
		userExpand.setProfessor(professor);
		userExpand.setSex("");
		userExpand.setResume("");
		userExpand.setClassId("");
		userExpand.setQualificationId("");
		if (company != null && !company.isEmpty()) {
			Manufacturer manufacturer = groupFacade
					.selectManufacturerByUuid(company);
			userExpand.setManufacturer(manufacturer);
		}

		// 添加用户
		user.setUserExpand(userExpand);

		Status status=userFacade.addUserByWX(user, ur);
		if (status == Status.FAILURE) {
			bulidRequest("微信个人注册", "user", null,
					Status.FAILURE.message(), null, "手机号=" + tmp.getMobile()
							+ " 注册失败", request);
			return failureModel("用户注册失败").toJSON();
		}

		// 绑定微信
		String openId = getOpenId(OPENID_KEY, request);
		if (openId != null) {
			WebchatUser webchatUser = webchatFacade.buildTotalWebUser(openId);
			if (webchatUser != null) {
				webchatUser.setUser(user);
				webchatFacade.updateWebChatUser(webchatUser);
			}
		}


		User addUser = userFacade.getUserByUuid(uuid);

		// 放置session
		setCurrentSessionUser(addUser, request);

		/**
		 * 2016-2-24,注册新增积分
		 */
		integralFacade.actionRegister(addUser);

		/**
		 * 2016-3-16,添加发送模板消息
		 */
		getModelMessageFacade().registerSuccess(addUser);

		// 最后处理充值卡问题
		if (activeCode != null && !activeCode.isEmpty()) {
			ActivationCode code = activationFacade
					.getActivationByCode(activeCode);
			ActionMessage<?> actionMessage = userFacade
					.increaseUserValidDateForBox(addUser, code);
			if (actionMessage.getStatus() == Status.FAILURE) {
				bulidRequest("微信个人注册", "user", null, Status.FAILURE.message(),
						null, "手机号=" + tmp.getMobile() + " code=" + activeCode
								+ " 流量卡激活失效，注册失败", request);
				BaseAPIModel apiModel = BaseController.failureModel();
				apiModel.setStatus(Status.PARAM_ERROR);
				apiModel.setMessage("注册已成功，但vip激活无效");
				return apiModel.toJSON();
			}
		}

		return successModel().toJSON();
	}

	/**
	 * 微信注册成功以后跳转页面
	 * 
	 * @param request
	 * @param num
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/reg/success/{num}", method = RequestMethod.GET)
	public ModelAndView regSuccess(HttpServletRequest request,
			@PathVariable int num) {
		if (num == 1) {
			// 医生
			return redirectPageView("wechat/reg_success_n");
		} else if (num == 2) {
			// 企业
			return redirectPageView("wechat/reg_success_c");
		} else if (num == 4) {
			return redirectPageView("wechat/isabel_login");
		} else {
			return redirectPageView("wechat/reg_success");
		}
	}

	/**
	 * 微信首页
	 * 
	 * @he 2016-06-23 修改
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request,
			@RequestParam(required = false) String originWxUrl) {
		WxIndexvo vo = new WxIndexvo();
		List<Banner> banners = bannerFacade.getWxBanner(Banner.TYPE_WECHAT,Banner.POSITION_INDEX);
		List<Activity> activitys = activityFacade.recommendActivities(5);
		List<WxSubject> list = wxSubjectFacade.searchWxSubjectByWx(
				WxSubject.ROOT, WxSubject.TYPE_CATEGORY);
		List<Resource> resources = effectResourceFacade()
				.searchAPIResourcesAllLatest(new Date(), 5);

		vo.buildBanner(banners);
		vo.buildActivity(activitys);
		vo.buildMenu(list, assetFacade, wxSubjectFacade, effectResourceFacade());
		vo.buildResource(resources);

		bulidRequest("查看首页", null, null, Status.SUCCESS.message(), null, null,
				request);
		// 记录微信来源
		bulidRequest(originWxUrl, request);

		request.setAttribute("vo_data", vo);
		return redirectPageView("wechat/index");
	}

	/**
	 * 2016-06-27 修改 学科分类页面
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/first/level/subject", method = RequestMethod.GET)
	public ModelAndView subjectCategory(HttpServletRequest request) {
		bulidRequest("微信查看学科分类", null, null, Status.SUCCESS.message(), null,
				null, request);
		WxIndexvo vo = new WxIndexvo();
		List<Banner> banners = bannerFacade.getWxBanner(Banner.TYPE_WECHAT,Banner.POSITION_INDEX);
		List<WxSubject> list = wxSubjectFacade.searchWxSubjectByWx(
				WxSubject.ROOT, WxSubject.TYPE_CATEGORY);

		vo.buildBanner(banners);
		vo.buildMenu(list, assetFacade, wxSubjectFacade, effectResourceFacade());

		request.setAttribute("vo_data", vo);
		return redirectPageView("wechat/subject/subject");
	}

	/**
	 * 首页进入分类的二级目录
	 * 
	 * @param request
	 * @return
	 * @he 2016-06-27修改
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/index/second/category/{uuid}", method = RequestMethod.GET)
	public ModelAndView secondCategory(HttpServletRequest request,
			@PathVariable String uuid) {
		WxSubject wxSubject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		if (wxSubject == null || !wxSubject.apiUseable()) {
			return redirectErrorPage(request, "此学科分类不存在或下线");
		}
		Category category = assetFacade.getCategoryById(wxSubject
				.getReflectId());
		if (category == null) {
			return redirectErrorPage(request, "此学科分类不存在或下线");
		}
		WxMenuItem item = new WxMenuItem();
		item.buildbSimpleData(wxSubject);

		List<WxSubject> activityChildrenList = wxSubjectFacade
				.searchWxSubjectChildrenByWx(wxSubject.getId(),
						WxSubject.TYPE_ACTIVITY, WxSubject.ALL_LEVEL);
		item.buildSecondActivity(activityChildrenList, activityFacade);

		// 无层级子分类
		List<WxSubject> noLevelChildrenList = wxSubjectFacade
				.searchWxSubjectChildrenByWx(wxSubject.getId(),
						WxSubject.TYPE_CATEGORY, WxSubject.NO_LEVEL);
		item.buildNoLevelSubject(noLevelChildrenList, effectResourceFacade());

		// 有层级子分类
		List<WxSubject> hasLevelChildrenList = wxSubjectFacade
				.searchWxSubjectChildrenByWx(wxSubject.getId(),
						WxSubject.TYPE_CATEGORY, WxSubject.HAS_LEVEL);
		item.buildHasLevel(hasLevelChildrenList, effectResourceFacade(),
				wxSubjectFacade);

		request.setAttribute("vo_data", item);
		bulidRequest("微信查看2级分类学科", "category", category.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("wechat/subject/second_subject");
	}

	/**
	 * 个人中心
	 * 
	 * @param request
	 * @param originWxUrl
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/my/center", method = RequestMethod.GET)
	public ModelAndView myCenter(HttpServletRequest request,
			@RequestParam(required = false) String originWxUrl) {
		User user = getCurrentUser(request);
		// 头部公共信息
		wxUserShowBaseInfo(request);
		// 取2条最新
		WxResourceList collect = new WxResourceList();
		List<Resource> resources = myCollectionFacade.getUserFavoriteResources(
				user, new Date(), 2);
		collect.build(resources);
		request.setAttribute("collect_data", collect);

		WxResourceList oups = new WxResourceList();
		List<ActivityApplication> list = activityOpusFacade
				.searchApplicateByUserId(1, user.getId(),
						Tools.formatYMDHMSDate(new Date()), 2);
		oups.buildDownOups(list);
		request.setAttribute("oups_data", oups);

		// 记录微信来源
		bulidRequest(originWxUrl, request);
		bulidRequest("微信查看-个人中心", null, null, Status.SUCCESS.message(), null,
				null, request);
		return redirectPageView("wechat/my_center");
	}



	/**
	 * 提交完，跳转页面
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/apply/finsh/page")
	public ModelAndView applyFinsh(HttpServletRequest request) {
		return redirectPageView("wechat/feedback");
	}
	
	/**
	 * 2016-06-29 之后需要优化 我的vip页面
	 * 
	 * @param request
	 * @return
	 * 
	 */
	@RequestAuthority(requiredProject = true, logger = false, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/active/page/code")
	public ModelAndView activePage(HttpServletRequest request) {
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.getSession().setAttribute(ACTIVE_CODE_TOKEN, token);

		// 头部公共信息
		wxUserShowBaseInfo(request);

		/**
		 * vip 2016-5-9 改版逻辑 1.没有实名，先实名逻辑 2.有无申请记录，判断状态 3.
		 */
		WxUserShowInfo showInfo = (WxUserShowInfo) request
				.getAttribute("vo_user_data");
		User user = getCurrentUser(request);
		ApplicableRecords applicableRecords = applicableFacade
				.selectApplicableByUserId(user.getId());
		VipInfo vip = new VipInfo();
		if(!showInfo.isRealFlag()){
			//未实名，重新查找实名
			Certification certification = certificationFacade
					.getCertifiActiveByUserId(user);
			if (certification != null) {
				showInfo.setRealFlag(true);
				showInfo.setShowName(OptionalUtils.traceValue(certification, "name"));
				showInfo.setRealNameInfo(OptionalUtils.traceValue(certification,
						"credentials"));
			}
		}
		vip.buildPage(showInfo.isRealFlag(), applicableRecords);

		vip.buildVipStatus(applicableRecords, showInfo.getVipStatus());

		request.setAttribute("vip_page_show", vip.toJSON());
		return redirectPageView("wechat/active_code");
	}

	/**
	 * 2016-5-12 新增 vip的申请操作
	 * 2016-08-05 修改
	 * @he 增加我已有卡服务
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/application/vip", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String applicationVip(HttpServletRequest request,@RequestParam int type) {
		User user = getCurrentUser(request);
		Certification certification = certificationFacade
				.getCertifiActiveByUserId(user);
		// 首先查看实名信息
		VipInfo vipInfo = new VipInfo();
		if (certification == null) {
			vipInfo.setStatus(Status.FAILURE);
			vipInfo.setShowPage(0);
			return vipInfo.toJSON();
		}
		// 实名过
		ApplicableRecords applicableRecords = applicableFacade
				.selectApplicableByUserId(user.getId());
		Status status = Status.SUCCESS;
		if (default_project == null) {
			default_project = projectFacade.getProjectByUuid(PROJECT_UUID);
		}
		UserReference reference = userFacade.searchUserReference(user,
				default_project);
		String isDateValid = reference.isDateStr();
		if (applicableRecords == null) {
			// 未申请过，填充新的信息
			applicableRecords = new ApplicableRecords();
			applicableRecords.setUuid(Tools.getUUID());
			applicableRecords.setApplyName(certification.getName());
			applicableRecords.setPingYin(Tools.getPinYin(certification
					.getName()));
			applicableRecords.setMobile(user.getPhone());
			applicableRecords.setHospital(user.getUserExpand().getHospital());
			applicableRecords.setDepartments(user.getUserExpand()
					.getDepartments());
			if (isDateValid.equals("no_use")) {
				// 从未激活
				//type==0 申领vip type==2 我已有卡
				applicableRecords.setStatus(type);
			} else {
				applicableRecords.setStatus(2);
			}
			applicableRecords.setIsActive(1);
			applicableRecords.setMark(user.getUserExpand().getProfessor());
			applicableRecords.setUser(user);
			status = applicableFacade.addApplicable(applicableRecords);
		} else {
			// 有申请，暂时不变，以后看要求是否更新
		}

		if (status == Status.SUCCESS) {
			vipInfo.setShowPage(2);
			vipInfo.setStatus(status);
		} else {
			vipInfo.setShowPage(1);
			vipInfo.setStatus(status);
			return vipInfo.toJSON();
		}

		vipInfo.buildVipStatus(applicableRecords, isDateValid);
		if (vipInfo.getShowProcess() == 3) {
			vipInfo.setDeadTime(Tools.df.format(reference.getValidDate()));
		}
		return vipInfo.toJSON();
	}

	

	/**
	 * 2016-06-29 之后需要优化 vip 云卡激活操作
	 * 
	 * @param activeCode
	 * @param token
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/active/code", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String acticeCode(@RequestParam String activeCode,
			@RequestParam String token, HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (activeCode != null && !activeCode.isEmpty()) {
			ActivationCode code = activationFacade
					.getActivationByCode(activeCode);
			if (code == null) {
				BaseAPIController.bulidRequest("微信云卡激活", "activation_code",
						null, Status.FAILURE.message(), null, "输入code="
								+ activeCode + " 对应的卡不存在或已不可用", request);
				return BaseController.failureModel("该卡不存在或已不可用").toJSON();
			}
			if (!code.isUsed()) {
				BaseAPIController.bulidRequest("微信云卡激活", "activation_code",
						code.getId(), Status.FAILURE.message(), null, "输入code="
								+ activeCode + " 对应的卡已被使用", request);
				return BaseController.failureModel("该卡已被使用").toJSON();
			}
			String repeat_token = toastRepeatSubmitToken(request,
					ACTIVE_CODE_TOKEN);
			if (!token.equals(repeat_token)) {
				return BaseController.failureModel("请勿重复提交，刷新页面").toJSON();
			}
			ActionMessage actionMessage = userFacade
					.increaseUserValidDateForBox(user, code);
			if (actionMessage.getStatus() == Status.FAILURE) {
				BaseAPIController.bulidRequest("微信云卡激活", "activation_code",
						code.getId(), Status.FAILURE.message(), null, "输入code="
								+ activeCode + " 激活失败", request);
				if (actionMessage.getMessage().equals("参数有错误")) {
					return BaseController.failureModel("该卡无法充值").toJSON();
				}
				return BaseController.failureModel(actionMessage.getMessage())
						.toJSON();
			}
			// 重新更新user；
			User newUser = userFacade.getUserByUuid(user.getUuid());
			user = newUser;
			UserReference reference = user.getUserReference();
			String deadTime = Tools.df.format(reference.getValidDate());
			BaseAPIModel apiModel = BaseController.successModel();
			apiModel.setMessage(deadTime);
			BaseAPIController.bulidRequest("微信云卡激活", "activation_code", null,
					Status.SUCCESS.message(), null, "输入code=" + activeCode
							+ " 激活成功", request);
			return apiModel.toJSON();
		} else {
			return failureModel("卡号不能为空！").toJSON();
		}
	}

	/**
	 * 2016-06-29 之后需要优化 修改密码页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = false, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/change/page/psw")
	public ModelAndView changePswPage(HttpServletRequest request) {
		return redirectPageView("wechat/change_pw");
	}

	/**
	 * 2016-06-29 之后需要优化 修改密码的操作
	 * 
	 * @param oldPsw
	 * @param psw
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/change/psw", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String changePsw(@RequestParam String oldPsw,
			@RequestParam String psw, HttpServletRequest request) {
		User user = getCurrentUser(request);
		User changeUser = userFacade.getUserByUuid(user.getUuid());
		oldPsw = Md5.getMd5String(oldPsw);
		if (!changeUser.getPassword().equals(oldPsw)) {
			bulidRequest("微信用户修改密码", "user", user.getId(),
					Status.FAILURE.message(), null, "修改失败", request);
			return failureModel("原始密码错误").toJSON();
		}
		changeUser.setPassword(Md5.getMd5String(psw));
		Status status = userFacade.updateUser(changeUser);
		if (status == Status.SUCCESS) {
			bulidRequest("微信用户修改密码", "user", user.getId(),
					Status.SUCCESS.message(), null, "修改成功", request);
			return successModel().toJSON();
		}
		bulidRequest("微信用户修改密码", "user", user.getId(),
				Status.FAILURE.message(), null, "修改失败", request);
		return failureModel("修改密码失败").toString();
	}

	/**
	 * 2016-06-29 之后需要优化 修改个人信息页面 12-30 微信修改个人用户信息， 要区分企业用户，还是医生用户，不是企业（其他为医生）
	 * 
	 * @param request
	 * @return
	 * @he 2016-08-16 优化用户信息修改
	 * 将医生和企业合为一个页面
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/change/page/info", method = RequestMethod.GET)
	public ModelAndView infoChange(HttpServletRequest request) {
		// 拿到老用户
		User olduser = getCurrentUser(request);
		User user = userFacade.getUserByUuid(olduser.getUuid());
		WxUserInfoChange infoShow=new WxUserInfoChange();
		infoShow.changeUserInfo(user);
		request.setAttribute("vo_data", infoShow);
		bulidRequest("微信用户查看个人信息", "user", user.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return new ModelAndView("wechat/change_info");
	}

	/**
	 * 2016-06-29 之后需要优化 2016-3-7 最新修改 type=0 医生用户 type=1 企业用户
	 * 
	 * @param name
	 * @param city
	 * @param hosipital
	 * @param office
	 * @param professor
	 * @param request
	 * @return
	 * @he 2016-08-16 优化数据
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/change/info", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String userInfoChange(@RequestParam String name,
			@RequestParam int type,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) String hosipital,
			@RequestParam(required = false) String office,
			@RequestParam(required = false) String professor,
			@RequestParam(required = false) String company,
			HttpServletRequest request) {
		User user = getCurrentUser(request);
		user.setNickname(name);
		if (Tools.isBlank(name)) {
			user.setPinyin(Tools.getPinYin(name));
		} else {
			user.setPinyin("");
		}
		Status status = Status.SUCCESS;
		UserExpand userExpand = user.getUserExpand();
		if (userExpand == null) {
			userExpand = new UserExpand();
			userExpand.setUuid(Tools.getUUID());
			userExpand.setUser(user);
			userExpand.setStatus(BaseModel.APPROVED);
			userExpand.setIsActive(BaseModel.ACTIVE);
			userExpand.setMark("");
			userExpand.setBirthday("");
			userExpand.setReceipt(0);
			if (type == UserExpand.USER_DOCTOR) {
				// 医生
				City city_model = provinceMapper.selectCtiByUUid(city);
				if (city_model != null) {
					userExpand.setCity(city_model);
				}
				Hospital hospital_model = hospitalMapper
						.selectHospitalByUUid(hosipital);
				if (hospital_model != null) {
					userExpand.setHospital(hospital_model);
				}
				Departments departments_model = hospitalMapper
						.selectDePatByUuid(office);
				if (departments_model != null) {
					userExpand.setDepartments(departments_model);
				}
			} else if (type == UserExpand.USER_COMPANY) {
				// 企业
				Manufacturer manufacturer = groupFacade
						.selectManufacturerByUuid(company);
				if (manufacturer != null) {
					userExpand.setManufacturer(manufacturer);
				}
			}
			userExpand.setType(type);
			userExpand.setProfessor(professor);
			userExpand.setSex("");
			userExpand.setResume("");
			userExpand.setClassId("");
			userExpand.setQualificationId("");
			status = userFacade.updateUserInfo(user, userExpand, true);
		} else {
			if (type == UserExpand.USER_DOCTOR) {
				City city_model = provinceMapper.selectCtiByUUid(city);
				if (city_model != null) {
					userExpand.setCity(city_model);
				}
				Hospital hospital_model = hospitalMapper
						.selectHospitalByUUid(hosipital);
				if (hospital_model != null) {
					userExpand.setHospital(hospital_model);
				}
				Departments departments_model = hospitalMapper
						.selectDePatByUuid(office);
				if (departments_model != null) {
					userExpand.setDepartments(departments_model);
				}
			} else if (type == UserExpand.USER_COMPANY) {
				Manufacturer manufacturer = groupFacade
						.selectManufacturerByUuid(company);
				if (manufacturer != null) {
					userExpand.setManufacturer(manufacturer);
				}
			}
			userExpand.setType(type);
			userExpand.setProfessor(professor);
			status = userFacade.updateUserInfo(user, userExpand, false);
		}
		if (Status.SUCCESS == status) {
			User newUser = userFacade.getUserByUuid(user.getUuid());
			BaseAPIController.bulidRequest("微信用户修改个人信息", "user", user.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			user = newUser;
			return BaseController.successModel().toJSON();
		}
		bulidRequest("微信用户修改个人信息", "user", user.getId(),
				Status.FAILURE.message(), null, "失败", request);
		return failureModel("修改个人信息失败").toJSON();
	}

	/**
	 * 2016-06-29 之后需要优化 11-22 忘记密码页面
	 */
	@RequestAuthority(requiredProject = false, logger = false, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/forget/password", method = RequestMethod.GET)
	public ModelAndView forgetPsw(HttpServletRequest request) {
		return redirectPageView("wechat/forget");
	}

	/**
	 * 11-22 忘记密码-发送验证码
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/forget/code/send", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String fetchForgetCode(@RequestParam String mobile,
			HttpServletRequest request) {
		if (mobile == null || !Tools.isValidMobile(mobile)) {
			return BaseAPIController.failureModel("手机号码不正确").toJSON();
		}
		// 判断用户是否存在
		if (!userFacade.hasUser(mobile)) {
			return BaseAPIController.failureModel("用户不存在").toJSON();
		}
		Status s = userFacade.sendForgetPassCode(mobile,
				BaseController.getClientIpAddr(request));
		if (s == Status.OPERATION_FAST) {
			return BaseAPIController.failureModel("请在60s之后重试").toJSON();
		} else if (s == Status.SUCCESS) {
			return BaseController.successModel().toJSON();
		}
		return BaseAPIController.failureModel("验证码发送失败，请稍后再试").toJSON();
	}

	/**
	 * 11-22 忘记密码-验证验证码
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/froget/code/valid", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String validForgetCode(@RequestParam String code,
			String mobile) {
		RegisterTmp tmp = userFacade.validForgetPassCode(code, mobile);
		if (tmp == null || !tmp.isActive()) {
			return BaseController.failureModel("验证码错误").toJSON();
		}
		if (Math.abs(System.currentTimeMillis() - tmp.getCreateDate().getTime()) > 10 * 60 * 1000) {
			return BaseController.failureModel("验证码已过期").toJSON();
		}
		return BaseAPIModel.makeSimpleSuccessInnerDataJson(new SimpleData(
				"valid_code", tmp.getUuid()));
	}

	/**
	 * 11-22 忘记密码-重置密码
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/forget/init/psw", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String initPsw(@RequestParam String psw, String valid_code,
			HttpServletRequest request) {
		RegisterTmp tmp = userFacade.getRegisterTmpByUuid(valid_code);
		if (tmp == null || !tmp.isActive()) {
			BaseAPIController.bulidRequest("微信-重置-忘记密码", "user", null,
					Status.FAILURE.message(), null, "验证暂不可用", request);
			return BaseController.failureModel("手机验证暂不可用").toJSON();
		}
		User user = userFacade.getUserByUsername(tmp.getMobile());
		if (user == null) {
			BaseAPIController.bulidRequest("微信-重置-忘记密码", "user", null,
					Status.FAILURE.message(), null, "手机：" + tmp.getMobile()
							+ " 对应user不存在", request);
			return BaseController.failureModel("用户不存在").toJSON();
		}
		user.setPassword(Md5.getMd5String(psw));
		Status status = userFacade.updateUser(user);
		if (status == Status.SUCCESS) {
			BaseAPIController.bulidRequest("微信-重置-忘记密码", "user", null,
					Status.SUCCESS.message(), null, "手机：" + tmp.getMobile()
							+ " 成功", request);
			return BaseController.successModel().toJSON();
		}
		BaseAPIController.bulidRequest("微信-重置-忘记密码", "user", null,
				Status.SUCCESS.message(), null,
				"手机：" + tmp.getMobile() + " 失败", request);
		return BaseController.failureModel("重置密码失败").toString();
	}

	// 关于我们 2016-06-29 之后需要优化
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/about/our", method = RequestMethod.GET)
	public ModelAndView aboutOur(HttpServletRequest request) {
		bulidRequest("微信查看-关于我们", null, null, Status.SUCCESS.message(), null,
				"成功", request);
		return redirectPageView("wechat/about");

	}

	/**
	 * 签收页面，盒子的签名，（暂不使用了）
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/sign/page", method = RequestMethod.GET)
	public ModelAndView signPage(HttpServletRequest request) {
		User user = getCurrentUser(request);
		user = userFacade.getUserByUuid(user.getUuid());
		UserExpand expand = user.getUserExpand();
		String logMark = "";
		if (expand == null) {
			// 没签收
			logMark = "未签收";
			BaseController.bulidRequest("微信查看签收", "user", null,
					Status.SUCCESS.message(), null, logMark, request);
			return redirectPageView("wechat/signIn");
		} else {
			if (expand.getReceipt() == BaseModel.ACTIVE) {
				// 签收
				logMark = "已签收";
				BaseController.bulidRequest("微信查看签收", "user", null,
						Status.SUCCESS.message(), null, logMark, request);
				return redirectPageView("wechat/signIn_success");
			} else {
				// 未签收
				logMark = "未签收";
				BaseController.bulidRequest("微信查看签收", "user", null,
						Status.SUCCESS.message(), null, logMark, request);
				return redirectPageView("wechat/signIn");
			}
		}
	}

	/**
	 * 15-12-30 确认签收（暂不使用了）
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/affirm/sign", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String affirmSign(HttpServletRequest request) {
		User user = getCurrentUser(request);
		String fileFold = request.getRealPath("/") + File.separator + "signimg"
				+ File.separator + user.getUuid();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获得文件：
		MultipartFile file = multipartRequest.getFile("imgData");
		Status status = Status.FAILURE;
		try {
			File fold = new File(fileFold);
			if (!fold.exists()) {
				fold.mkdirs();
			}
			int count = 0;
			File oldFile;
			do {
				count++;
				String filepath = fileFold + File.separator + count + ".jpg";
				oldFile = new File(filepath);
			} while (oldFile.exists() && oldFile.length() > 0);
			file.transferTo(oldFile);
			status = buildReceipt(user);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("微信签收", "user", null,
					Status.SUCCESS.message(), null, "签收保存成功", request);
			return BaseController.successModel().toJSON();
		} else {
			BaseController.bulidRequest("微信签收", "user", null,
					Status.FAILURE.message(), null, "签收保存失败", request);
			return BaseController.failureModel("签名保存失败，请重新签收提交").toJSON();
		}

	}

	/**
	 * （暂不使用了）
	 * 
	 * @param user
	 * @return
	 */
	public Status buildReceipt(User user) {
		Status status = Status.FAILURE;
		UserExpand userExpand = user.getUserExpand();
		if (userExpand == null) {
			userExpand = new UserExpand();
			userExpand.setUuid(Tools.getUUID());
			userExpand.setUser(user);
			userExpand.setStatus(1);
			userExpand.setIsActive(1);
			userExpand.setMark("");
			userExpand.setBirthday("");
			userExpand.setReceipt(1);
			userExpand.setProfessor("");
			userExpand.setSex("");
			userExpand.setResume("");
			userExpand.setClassId("");
			userExpand.setQualificationId("");
			status = userFacade.addUserExpand(userExpand);
		} else {
			userExpand.setReceipt(1);
			status = userFacade.updateReceiptExpand(userExpand);
		}
		return status;
	}

	/**
	 * 2016-06-29 之后需要优化 (废弃使用) 2016-3-3,修改没有微信昵称的，拿数据 微信获取头像
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/wx/get/photo", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String wxGetPhoto(HttpServletRequest request) {
		User user = getCurrentUser(request);
		String openId = getOpenId(OPENID_KEY, request);
		if (openId == null) {
			return failureModel("openId未找到").toJSON();
		}
		WebchatUser webchatUser = webchatFacade
				.searchWebChatUserByOpenid(openId);
		if (webchatUser == null) {
			return failureModel("未关注").toJSON();
		}
		// 请求数据
		boolean flag = WxBusinessCommon.getWxUserInfo(webchatUser);
		if (flag) {
			user.setAvatar(webchatUser.getPhoto());
			// 更新user
			userFacade.updateUserAvatar(user);
			UserExpand expand = user.getUserExpand();
			if (expand != null) {
				if (expand.getSex() == null || expand.getSex().isEmpty()) {
					expand.setSex(webchatUser.getSex());
					userFacade.updateSexExpand(expand);
				}
			}
			webchatFacade.updateWebChatUser(webchatUser);
		}
		return successModel(OptionalUtils.traceValue(webchatUser, "photo"))
				.toJSON();

	}

}
