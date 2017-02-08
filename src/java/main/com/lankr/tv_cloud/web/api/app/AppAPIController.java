/*package com.lankr.tv_cloud.web.api.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lankr.orm.mybatis.mapper.HospitalMapper;
import com.lankr.orm.mybatis.mapper.ProvinceMapper;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.annotations.RequestAuthority;
import com.lankr.tv_cloud.cache.EphemeralData;
import com.lankr.tv_cloud.facade.ApplicableFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.AppAuthentication;
import com.lankr.tv_cloud.model.ApplicableRecords;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.RegisterTmp;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceVoteSubject;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.Md5;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.ResourceItemData;
import com.lankr.tv_cloud.vo.UserResourceVotesData;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.SimpleData;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.BaseAPIController;
import com.lankr.tv_cloud.web.api.OPenAuth;
import com.lankr.tv_cloud.web.api.app.vo.AppUserAuthVo;
import com.lankr.tv_cloud.web.api.app.vo.CommonAppItemVo;
import com.lankr.tv_cloud.web.api.app.vo.NewsDetailVo;
import com.lankr.tv_cloud.web.api.app.vo.NewsListVo;
import com.lankr.tv_cloud.web.api.tv.ResourcesData;

@Controller
@RequestMapping("/api/app")
public class AppAPIController extends BaseAPIController {

	public static final String PROJECT_UUID = "be528a45-4d61-11e5-b667-8c6a6fec53d9";

	private Project default_project;

	@Autowired
	private ProvinceMapper provinceMapper;

	@Autowired
	private HospitalMapper hospitalMapper;

	@Autowired
	protected ApplicableFacade applicableFacade;

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm",
			Locale.CHINESE);

	*//**
	 * 
	 * @param username
	 * @param password
	 * @param request
	 * @return 登录接口
	 *//*
	@RequestAuthority(requiredAuth = false)
	@RequestMapping(value = "/authorize", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String userLogin(@RequestParam String username,
			@RequestParam String password, HttpServletRequest request) {
		User user = userFacade.login(username, Md5.getMd5String(password));
		if (user == null) {
			return failureModel("username or password was invalid").toJSON();
		}
		try {
			if (user.getUserReference() != null
					&& user.getUserReference().getRole().getLevel() >= Role.PRO_CLIENT) {
				AppAuthentication auth = AppAuthentication.makePrototype(
						AppAuthentication.class, getClientIpAddr(request));
				auth.setUser(user);
				userFacade.addAppAuth(auth);
				AppUserAuthVo vo = new AppUserAuthVo();
				vo.setToken(auth.getToken());
				vo.build(user);
				BaseController.bulidRequest("手机用户登录", "user", user.getId(),
						request);
				return vo.toJSON();
			} else {
				return failureModel(Status.NO_PERMISSION.message()).toJSON();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return failureModel().toJSON();
	}

	*//**
	 * app加载首页请求的种类数据
	 * 
	 * @param categoryName
	 * @param spreadable
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value = "/list/category", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String getCategory(HttpServletRequest request) {
		if (default_project == null) {
			default_project = projectFacade
					.getProjectByUuid(AppAPIController.PROJECT_UUID);
		}
		List<Category> list = assetFacade
				.fetchProjectRootCategory(default_project);
		CommonAppItemVo app = new CommonAppItemVo();
		if (list == null || list.isEmpty()) {
			return failureModel("种类数据加载出错").toJSON();
		}
		app.setStatus(Status.SUCCESS);
		app.buildCate(list);
		return app.toJSON();
	}

	*//**
	 * 加载省份数据
	 * 
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value = "/list/province", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String getProvince(HttpServletRequest request) {
		List<Province> list = EphemeralData.getIntansce(provinceMapper)
				.getProvinces();
		if (list == null || list.isEmpty()) {
			return failureModel("省份数据加载出错").toJSON();
		}
		CommonAppItemVo app = new CommonAppItemVo();
		app.setStatus(Status.SUCCESS);
		app.buildProvince(list);
		return app.toJSON();
	}

	@RequestMapping(value = "/city/{uuid}", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getCityList(HttpServletRequest request,
			@PathVariable String uuid) {
		Province province = provinceMapper.selectProByUUid(uuid);
		if (province == null) {
			return failureModel("uuid参数出错").toJSON();
		}
		CommonAppItemVo app = new CommonAppItemVo();
		List<City> list = EphemeralData.getIntansce(provinceMapper).getCitys(
				province);
		if (list == null || list.isEmpty()) {
			return failureModel("uuid参数出错").toJSON();
		}
		app.setStatus(Status.SUCCESS);
		app.buildCity(list);
		return app.toJSON();
	}

	@RequestMapping(value = "/hospital/{uuid}", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getHospitalList(HttpServletRequest request,
			@PathVariable String uuid) {
		City city = provinceMapper.selectCtiByUUid(uuid);
		if (city == null) {
			return failureModel("uuid参数出错").toJSON();
		}
		CommonAppItemVo app = new CommonAppItemVo();
		List<Hospital> list = hospitalMapper
				.selectHosListByCityId(city.getId());
		if (list == null || list.isEmpty()) {
			return failureModel("uuid参数出错").toJSON();
		}
		app.setStatus(Status.SUCCESS);
		app.buildHospital(list);
		return app.toJSON();
	}

	*//**
	 * 加载科室
	 * 
	 * @param categoryName
	 * @param spreadable
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value = "/list/department", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getDepartment(HttpServletRequest request) {
		List<Departments> departments = hospitalMapper.selectDePatList();
		if (departments == null || departments.isEmpty()) {
			return failureModel("科室数据加载出错").toJSON();
		}
		CommonAppItemVo app = new CommonAppItemVo();
		app.setStatus(Status.SUCCESS);
		app.buildDepartments(departments);
		return app.toJSON();
	}

	*//**
	 * 保存邀请码页面
	 *//*
	@RequestAuthority(requiredAuth = false)
	@RequestMapping(value = "/invite/save", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String addApply(HttpServletRequest request,
			@RequestParam(required = false) String token,
			@RequestParam String applyName, @RequestParam String mobile,
			@RequestParam String hosipital, @RequestParam String office) {
		Hospital hospital = hospitalMapper.selectHospitalByUUid(hosipital);
		if (hospital == null || !hospital.isActive())
			return failureModel("医院信息不可用").toJSON();
		Departments departments = hospitalMapper.selectDePatByUuid(office);
		if (departments == null || !departments.isActive()) {
			return failureModel("科室信息不可用").toJSON();
		}
		Status status = Status.SUCCESS;
		ApplicableRecords applic = new ApplicableRecords();
		applic.setUuid(Tools.getUUID());
		applic.setApplyName(applyName);
		applic.setPingYin(Tools.getPinYin(applyName));
		applic.setMobile(mobile);
		applic.setHospital(hospital);
		applic.setDepartments(departments);
		applic.setStatus(0);
		applic.setIsActive(1);
		applic.setMark("");
		AppAuthentication appAuth = getHttpWrappedTokenData(
				AppAuthentication.class, request);
		if (appAuth != null) {
			User user = appAuth.getUser();
			if (user != null) {
				applic.setUser(user);
			}
		}
		status = applicableFacade.addApplicable(applic);
		BaseAPIModel apiModel = new BaseAPIModel();
		if (status == Status.SUCCESS) {
			apiModel.setStatus(status);
			BaseController.bulidRequest("完成邀请码提交:" + applyName,
					"applicable_records", null, request);
		}
		return apiModel.toJSON();
	}

	// 根据分类获取该分类的pdf和视频信息
	@@RequestAuthority(requiredToken = false)
	@RequestMapping(value = "/get/resource/category", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String getInfoByCate(@RequestParam String uuid,
			HttpServletRequest request) {

		Category category = assetFacade.getCategoryByUuid(uuid);

		if (category == null || !category.isActive()) {
			return failureModel("分类不可用").toJSON();
		}
		ResourcesData data = new ResourcesData();
		data.setStatus(Status.SUCCESS);
		List<Resource> resources = resourceFacade.searchAPIResources(new Date(),
				category, 20);
		data.buildResource(resources);
		BaseController.bulidRequest("app根据分类ID:" + category.getId() + "获取资源列表",
				"resource", null, request);
		return data.toJSON();
	}

	// 根据具体资源看视频信息或pdf信息
	@ApiAuth(requiredToken = false)
	@RequestMapping(value = "/resource/detail/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String getResourceDetail(@PathVariable String uuid,
			HttpServletRequest request) {
		Resource res = resourceFacade.getResourceByUuid(uuid);
		if (res == null || !res.isValid()) {
			return BaseController.failureModel("资源不存在或已被删除").toJSON();
		}
		ResourceItemData data = new ResourceItemData();
		data.build(res);
		BaseController.bulidRequest("app获取资源信息", "resource", res.getId(),
				request);
		return data.toJSON();
	}
	
	@ApiAuth(requiredToken = true)
	@RequestMapping(value = "/my/collection", method = RequestMethod.GET,produces = "text/json; charset=utf-8")
	@ResponseBody
	public String collection(HttpServletRequest request) {
		AppAuthentication appAuth = getHttpWrappedTokenData(
				AppAuthentication.class, request);
		User user = appAuth.getUser();
		List<Resource> resources = myCollectionFacade.getUserFavoriteResources(
				user, new Date(), 20);
		ResourcesData data = new ResourcesData();
		data.setStatus(Status.SUCCESS);
		data.buildResource(resources);
		BaseController.bulidRequest("app我的收藏", "my_collection", null, request);
		
		return data.toJSON();
	}

	// app跳转视频观看页面
	@ApiAuth(requiredToken = false)
	@RequestMapping(value = "/video/view/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String viewVideo(@PathVariable String uuid,
			HttpServletRequest request) {
		String url = Config.host + "/api/webchat/video/app?uuid=" + uuid;
		return url;
	}

	*//**
	 * 按分类查找新闻
	 *//*
	@ApiAuth(requiredToken = false)
	@RequestMapping(value = "/news/list/category", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String newsList(@RequestParam String name,
			HttpServletRequest request) {
		if (name == null || name.isEmpty()) {
			return failureModel("name分类参数错误").toJSON();
		}
		Category category = assetFacade.getCategoryByName(name);
		if (category == null || !category.isActive()) {
			return failureModel("分类不可用").toJSON();
		}
		List<NewsInfo> list = newsFacade.searchAllNews(category.getId());
		NewsListVo vo = new NewsListVo();
		if (list != null && !list.isEmpty()) {
			vo.buildData(list);
		}
		vo.setStatus(Status.SUCCESS);
		BaseController.bulidRequest("手机分类下的新闻列表", "category", category.getId(),
				request);
		return vo.toJSON();
	}

	*//**
	 * 新闻列表
	 * 
	 * @return
	 *//*
	@ApiAuth(requiredToken = false, logger = false)
	@RequestMapping(value = "/news/list", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String newsList() {
		List<NewsInfo> list = newsFacade.searchAllNews();
		NewsListVo vo = new NewsListVo();
		if (list != null && !list.isEmpty()) {
			vo.buildData(list);
		}
		vo.setStatus(Status.SUCCESS);
		return vo.toJSON();
	}

	*//**
	 * 获取单条新闻的详细记录
	 * 
	 * @return
	 *//*
	@ApiAuth(requiredToken = false)
	@RequestMapping(value = "/news/detail/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String newsDetail(@PathVariable String uuid,
			HttpServletRequest request) {
		NewsInfo info = newsFacade.selectInfoByUuidOfApp(uuid);
		NewsDetailVo vo = new NewsDetailVo();
		if (info != null) {
			vo.buildData(info);
		}
		vo.setStatus(Status.SUCCESS);
		BaseController.bulidRequest("手机新闻详情", "news_info", info.getId(),
				request);
		return vo.toJSON();
	}

	// 注册获取验证码
	@ApiAuth(requiredToken = false, logger = false)
	@RequestMapping(value = "/user/register/code/send", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String fetchRegisterCode(@RequestParam String mobile,
			@RequestParam(required = false) String deivce,
			HttpServletRequest request) {
		if (mobile == null || !Tools.isValidMobile(mobile)) {
			return failureModel("please input valid mobile number").toJSON();
		}
		Status s = userFacade.sendRegisterCode(mobile, "",
				getClientIpAddr(request));
		if (Status.SUCCESS == s) {
			return getStatusJson(Status.SUCCESS);
		} else {
			return failureModel(s.message()).toJSON();
		}
	}

	// 验证注册码
	@ApiAuth(requiredToken = false, logger = false)
	@RequestMapping(value = "/user/register/code/valid", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String validRegisterCode(@RequestParam String code,
			String mobile) {
		RegisterTmp tmp = userFacade.validRegisterCode(code, mobile);
		if (tmp == null || !tmp.isActive()) {
			return failureModel("validation failure").toJSON();
		}
		if (Math.abs(System.currentTimeMillis() - tmp.getCreateDate().getTime()) > 10 * 60 * 1000) {
			return failureModel("验证码已过期").toJSON();
		}
		return BaseAPIModel.makeSimpleSuccessInnerDataJson(new SimpleData(
				"valid_code", tmp.getUuid()));
	}

	// 保存注册用户
	@ApiAuth(requiredToken = false, logger = true)
	@RequestMapping(value = "/user/register/save", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String saveRegisterUser(String valid_code,
			String password, @RequestParam(required = false) String nickname,
			HttpServletRequest request) {
		RegisterTmp tmp = userFacade.getRegisterTmpByUuid(valid_code);
		if (tmp == null|| !tmp.isActive()) {
			return failureModel("未匹配到注册内容").toJSON();
		}
		User user = userFacade.getUserByUsername(tmp.getMobile());
		if (user != null) {
			return failureModel("用户已被注册").toJSON();
		}
		user = new User();
		user.setNickname(nickname);
		if (nickname != null) {
			user.setPinyin(Tools.getPinYin(nickname));
		}
		user.setUsername(tmp.getMobile());
		user.setPhone(tmp.getMobile());
		user.setUuid(Tools.getUUID());
		user.setIsActive(BaseModel.ACTIVE);
		user.setPassword(Md5.getMd5String(password));
		if (default_project == null) {
			default_project = projectFacade.getProjectByUuid(PROJECT_UUID);
		}
		try {
			userFacade.addCustomUserWithAppAuth(user, default_project);
			userFacade.disableRegisterTmp(tmp);
			BaseController.bulidRequest("手机个人注册", "user", null, request);
			return userLogin(user.getUsername(), password, request);
		} catch (Exception e) {
			return failureModel("save error").toJSON();
		}
	}

	*//**
	 * 修改密码
	 * 
	 * @param oriPassword
	 * @param newPassword
	 * @return
	 *//*
	@ApiAuth(requiredToken = true, logger = true)
	@RequestMapping(value = "/user/password/update", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String userPasswordUpdate(String oriPassword,
			String newPassword, HttpServletRequest request) {
		AppAuthentication appAuth = getHttpWrappedTokenData(
				AppAuthentication.class, request);
		User userold = appAuth.getUser();
		User user = userFacade.getUserByUuid(userold.getUuid());
		oriPassword = Md5.getMd5String(oriPassword);
		newPassword = Md5.getMd5String(newPassword);
		if (oriPassword != null && oriPassword.equals(user.getPassword())) {
			if (newPassword != null) {
				user.setPassword(newPassword);
				Status status = userFacade.updateUser(user);
				if (status == Status.SUCCESS) {
					BaseController.bulidRequest("手机个人修改密码", "user",
							user.getId(), request);
				}
				return getStatusJson(status);
			} else {
				return failureModel("new password is not error").toJSON();
			}
		} else {
			return failureModel("old password is not match").toJSON();
		}
	}

	// 获取投票内容
	@ApiAuth
	@RequestMapping(value = "/user/vote/{res_uuid}/data", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	public @ResponseBody String userVoteData(@PathVariable String res_uuid,
			HttpServletRequest request) {
		AppAuthentication appAuth = getHttpWrappedTokenData(
				AppAuthentication.class, request);
		User user = appAuth.getUser();
		Resource res = resourceFacade.getResourceByUuid(res_uuid);
		if (res == null || !res.isValid()) {
			return BaseAPIController.failureModel("资源不可用或已经被删除").toJSON();
		}
		List<ResourceVoteSubject> subjects = resourceFacade
				.getVotesByResourceId(res);
		UserResourceVotesData data = new UserResourceVotesData();
		data.setStatus(Status.SUCCESS);
		if (subjects != null) {
			for (ResourceVoteSubject resourceVoteSubject : subjects) {
				data.buildSubjectItem(resourceVoteSubject, resourceFacade
						.seachVotedSubjectOptions(user.getId(),
								resourceVoteSubject.getId()));
			}
		}
		return data.toJSON();
	}

	// 投票
	@ApiAuth
	@RequestMapping(value = "/user/votes/post", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String postVotes(@RequestParam String votes_data,
			HttpServletRequest request) {
		try {
			AppAuthentication appAuth = getHttpWrappedTokenData(
					AppAuthentication.class, request);
			User user = appAuth.getUser();
			VotesPost vp = gson.fromJson(votes_data, VotesPost.class);
			if (vp == null) {
				return failureModel("提交的参数有误").toJSON();
			}
			List<VotePostItem> posts = vp.posts;
			if (posts == null || posts.isEmpty())
				return failureModel("未提交任何选项").toJSON();
			List<String> suuids = new ArrayList<String>();
			for (VotePostItem votePostItem : posts) {
				resourceFacade.votedByUser(user, votePostItem.s_uuid,
						votePostItem.selected_options);
				suuids.add(votePostItem.s_uuid);
			}
			return userVotesResult(user, suuids);
		} catch (Exception e) {
			e.printStackTrace();
			return failureModel("参数不正确").toJSON();
		}
	}

	class VotesPost {
		List<VotePostItem> posts;
	}

	class VotePostItem {
		String s_uuid;
		List<String> selected_options;
	}
}
*/