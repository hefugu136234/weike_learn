package com.lankr.tv_cloud.web.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.cache.EphemeralData;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ActivationCode;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.RegisterTmp;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.utils.Md5;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;
import com.lankr.tv_cloud.web.api.webchat.vo.CommonItemVo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
import com.lankr.tv_cloud.web.front.util.FrontWxOpenUtil;
import com.lankr.tv_cloud.web.front.vo.FrontActivityList;
import com.lankr.tv_cloud.web.front.vo.FrontBannerList;
import com.lankr.tv_cloud.web.front.vo.TopMenuListVo;
import com.lankr.tv_cloud.web.front.vo.UserFrontBaseView;

@Controller
@RequestMapping(value = BaseFrontController.PC_PRIOR)
public class WebIndexController extends BaseFrontController {

	// 测试
	@RequestMapping(value = "/test/login/{unionid}", method = RequestMethod.GET)
	public ModelAndView pageLo(HttpServletRequest request,
			@PathVariable String unionid) {
		if(Config.isProductEnv()){
			return redirectUrlView("/");
		}
		setKeySession(unionid, request, UNIONID_KEY);
		String url = authJudyUser(request, "12345", WebChatMenu.ZHILIAO_STATE);
		return redirectUrlView(url);
	}

	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/login")
	public ModelAndView login(HttpServletRequest request) {
		String authUrl = FrontWxOpenUtil.getOpenWxQrLink();
		return redirectUrlView(authUrl);
	}

	/**
	 * 所有出错页面的汇总页面 一般由/auth/logined/before接口跳入
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/error/page", method = RequestMethod.GET)
	public ModelAndView errorPage(HttpServletRequest request) {
		String error = errorMessage(request);
		if (error != null && !error.isEmpty()) {
			error = "数据出错，请刷新页面";
		}
		return redirectErrorPage(request, error);
	}

	/**
	 * 登陆授权后跳转的页面
	 * 
	 * @param request
	 * @return 此处只处理
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/auth/logined/before", method = RequestMethod.GET)
	public ModelAndView authLoginedBefore(HttpServletRequest request,
			@RequestParam String state, @RequestParam String code,
			@RequestParam(required = false) String redirect_uri) {
		String url = authJudyUser(request, code, state);
		return redirectUrlView(url);
	}

	/**
	 * 登录以后执行跳转的接口页面 1，登录 2，注册之后都将执行此接口
	 * 
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/logined/redirect/url", method = RequestMethod.GET)
	public ModelAndView loginRedirect(HttpServletRequest request) {
		User user = getCurrentUser(request);
		String url = getPriorUrl(request);
		if (url == null) {
			url = INDEX_URL;
		}
		bulidRequest("web登录", "user", user.getId(), Status.SUCCESS.message(),
				null, "成功", request);
		return redirectUrlView(url);
	}
	
	//记录登录之前的url
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/record/prior/url", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String recordPriorUrl(HttpServletRequest request,
			@RequestParam String url) {
		setPriorUrl(request, url);
		return successModel().toJSON();
	}

	/**
	 * 获取登录状态的数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/is/login/info", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String isLoginInfo(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null) {
			return UserFrontBaseView.notLogin();
		}
		UserFrontBaseView view = getUserView(request);
		if (view == null) {
			view = buildShowBaseView(request, user);
		}
		return view.toJSON();
	}

	/**
	 * 获取顶部菜单
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/top/menu", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String topMenu(HttpServletRequest request) {
		List<WxSubject> list = wxSubjectFacade.searchWxSubjectByWx(
				WxSubject.ROOT, WxSubject.TYPE_CATEGORY);
		TopMenuListVo vo = new TopMenuListVo();
		vo.buildCategoryData(list, assetFacade, wxSubjectFacade,
				effectResourceFacade());
		return vo.toJSON();
	}

	/**
	 * 首页的banner数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/banner/index/top", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String bannerIndex(HttpServletRequest request) {
		List<Banner> banerList = bannerFacade.getWxBanner(Banner.TYPE_WEB,Banner.POSITION_INDEX);
		FrontBannerList vo=new FrontBannerList();
		vo.buildBanner(banerList);
		return vo.toJSON();
	}
	
	
	@RequestAuthority(requiredProject = false, logger = false,phoneView=true)
	@RequestMapping(value = "/mobile/index")
	public ModelAndView mobileIndex(HttpServletRequest request) {
		setKeySession(FrontIntercepor.PHONE_VIEW, request, FrontIntercepor.PHONE_VIEW);
		return redirectPageView("web/mobile_index");
	}

	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/index")
	public ModelAndView frontIndex(HttpServletRequest request) {
		bulidRequest("web首页", null, null, Status.SUCCESS.message(), null, "成功",
				request);
		// banner;
		getCommonBanner(request);

		// 推荐活动的数据
		List<Activity> list = activityFacade.recommendActivities(3);
		FrontActivityList wx = new FrontActivityList();
		wx.buildIndexActivity(list);
		request.setAttribute("activity_list", wx.getItemList());
		// 学科分类

		List<WxSubject> wxSubjects = wxSubjectFacade.searchWxSubjectByWx(
				WxSubject.ROOT, WxSubject.TYPE_CATEGORY);
		TopMenuListVo vo = new TopMenuListVo();
		vo.buildIndexContent(wxSubjects, assetFacade, wxSubjectFacade,
				effectResourceFacade());
		request.setAttribute("subject_list", vo.getItemList());
		return redirectPageView("web/index");
	}

	/**
	 * 进入注册页面一定要保证，unionid存在
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/reg")
	public ModelAndView regPage(HttpServletRequest request) {
		// 进入注册前的校验
		User user = getCurrentUser(request);
		if (user != null) {
			return redirectUrlView(LOGINED_REDIRECT);
		}
		String unionid = getKeySession(UNIONID_KEY, request);
		if (unionid == null) {
			return redirectErrorPage(request, AGAIN_SAO_LOGIN);
		}
		CommonItemVo pro = new CommonItemVo();
		List<Province> list = EphemeralData.getIntansce(provinceMapper)
				.getProvinces();
		pro.buildProvince(list);
		request.setAttribute("province_list", pro.getItemList());

		CommonItemVo dep = new CommonItemVo();
		List<Departments> depList = hospitalMapper.selectDePatList();
		dep.buildDepartments(depList);
		request.setAttribute("depart_list", dep.getItemList());
		// String part_view=regRedirectPart(request);
		// request.setAttribute("part_view", part_view);
		return redirectPageView("web/reg");
	}

	/**
	 * 获取发送短信验证码 在获取之前检查是否关注，没有关注先关注
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/reg/code/send", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String fetchRegisterCode(@RequestParam String mobile,
			HttpServletRequest request) {
		String part_view = regRedirectPart(request);
		if (part_view.equals(REG_ONE_PART)) {
			return failureModel("no subscribe").toJSON();
		}
		if (mobile == null || !Tools.isValidMobile(mobile)) {
			return failureModel("手机号码不正确").toJSON();
		}
		Status s = userFacade.sendRegisterCode(mobile, "",
				getClientIpAddr(request));
		if (s == Status.IN_USE) {
			return failureModel("该手机号已注册").toJSON();
		} else if (s == Status.OPERATION_FAST) {
			return failureModel("请在60s之后重试").toJSON();
		} else if (s == Status.SUCCESS) {
			return successModel().toJSON();
		}
		return failureModel("验证码发送失败，请稍后再试").toJSON();
	}

	// 保存注册用户
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/user/reg/save", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String saveRegisterUser(String mobile, String code, String password,
			@RequestParam String nickname,
			@RequestParam(required = false) String hosipital,
			@RequestParam(required = false) String professor,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) String department,
			@RequestParam(required = false) String activeCode,
			HttpServletRequest request) {
		String unionid = getKeySession(UNIONID_KEY, request);
		if (unionid == null) {
			return failureModel("请重新扫码登录").toJSON();
		}
		WebchatUser webchatUser = webchatFacade
				.searchWebChatUserByUnionid(unionid);
		if (webchatUser == null) {
			// 跳转注册
			return failureModel("请先用微信扫一扫右侧二维码关注知了云盒(若已关注，请先取消重新关注)").toJSON();
		}

		RegisterTmp tmp = userFacade.validRegisterCode(code, mobile);
		if (tmp == null || !tmp.isActive()) {
			return failureModel("验证码错误").toJSON();
		}
		if (Math.abs(System.currentTimeMillis() - tmp.getCreateDate().getTime()) > 10 * 60 * 1000) {
			return failureModel("验证码已过期").toJSON();
		}

		User user = userFacade.getUserByUsername(mobile);
		if (user != null) {
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
		ur.setIsActive(BaseModel.ACTIVE);

		UserExpand userExpand = new UserExpand();
		userExpand.setUuid(Tools.getUUID());
		userExpand.setUser(user);
		userExpand.setStatus(BaseModel.APPROVED);
		userExpand.setIsActive(BaseModel.ACTIVE);
		userExpand.setMark("");
		userExpand.setBirthday("");
		userExpand.setType(UserExpand.USER_DOCTOR);
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

		// 添加用户
		user.setUserExpand(userExpand);

		webchatUser.setUser(user);

		Status status = userFacade.addUserByPcWeb(webchatUser, ur);
		if (status == Status.FAILURE) {
			return failureModel("用户注册失败").toJSON();
		}

		User addUser = userFacade.getUserByUuid(uuid);
		
		// 放置session
		setCurrentSessionUser(addUser, request);

		bulidRequest("web绑定注册", "user", addUser.getId(),
				Status.SUCCESS.message(), null, "成功", request);

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
			ActivationCode activationCode = activationFacade
					.getActivationByCode(activeCode);
			ActionMessage actionMessage = userFacade
					.increaseUserValidDateForBox(addUser, activationCode);
			if (actionMessage.getStatus() == Status.FAILURE) {
				BaseAPIModel apiModel = BaseController.failureModel();
				apiModel.setStatus(Status.PARAM_ERROR);
				apiModel.setMessage("注册已成功，但vip激活无效");
				return apiModel.toJSON();
			}
		}

		return successModel().toJSON();
	}
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/reg/protocol",method = RequestMethod.GET)
	public ModelAndView regProtocol(HttpServletRequest request) {
		return redirectPageView("web/protocol");
		
	}

}
