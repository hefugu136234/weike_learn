package com.lankr.tv_cloud.web.api.webchat;

import java.text.ParseException;
import java.util.Date;
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
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.ActivityExpert;
import com.lankr.tv_cloud.model.ActivitySubject;
import com.lankr.tv_cloud.model.ActivityUser;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.support.qiniu.QiniuUtils;
import com.lankr.tv_cloud.support.qiniu.storage.BucketManager;
import com.lankr.tv_cloud.support.qiniu.storage.model.DefaultPutRet;
import com.lankr.tv_cloud.support.qiniu.util.Auth;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;
import com.lankr.tv_cloud.web.api.webchat.vo.ApplyVo;
import com.lankr.tv_cloud.web.api.webchat.vo.CommonItemVo;
import com.lankr.tv_cloud.web.api.webchat.vo.ItemVo;
import com.lankr.tv_cloud.web.api.webchat.vo.OpusVo;
import com.lankr.tv_cloud.web.api.webchat.vo.OupsCommonList;
import com.lankr.tv_cloud.web.api.webchat.vo.OupsDetail;
import com.lankr.tv_cloud.web.api.webchat.vo.WxActivityItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxActivityList;
import com.lankr.tv_cloud.web.api.webchat.vo.WxExpertVo;
import com.lankr.tv_cloud.web.api.webchat.vo.WxMenuItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxResourceList;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;
import com.lankr.tv_cloud.web.api.webchat.vo.WxUserInfoChange;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class WeChatActivityControl extends BaseWechatController {

	public final static String COMMON_OUPS = "common_oups";

	/**
	 * 精彩活动列表
	 * 
	 * @he 修改 2016-06-28
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/activity/wonder/page", method = RequestMethod.GET)
	public ModelAndView activityWonderList(HttpServletRequest request) {
		bulidRequest("微信查看精彩活动列表", null, null, Status.SUCCESS.message(), null,
				"成功", request);
		WxMenuItem item = new WxMenuItem();
		int resCount = activityFacade.allActivityCount();
		item.setResCount(resCount);
		request.setAttribute("vo_data", item);
		return redirectPageView("wechat/activity/activity_wonder");
	}

	/**
	 * 2014-4-22 精彩活动的列表数据
	 * 
	 * @he 修改 2016-06-28
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/wonder/activity/data", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String activityWonderData(HttpServletRequest request,
			@RequestParam String startTime, @RequestParam int size) {
		if (size > 20) {
			size = 20;
		}
		List<Activity> list = activityFacade.wonderActivites(startTime, size);
		WxActivityList wx = new WxActivityList();
		wx.buildWonderList(list);
		return wx.toJSON();
	}

	/**
	 * 2016-06-07 修改
	 * 
	 * @he 点击进入活动详情页面
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_ACTIVITY_SHARE)
	@RequestMapping(value = "/activity/total/page/{uuid}", method = RequestMethod.GET)
	public ModelAndView activityDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		Activity activity = activityFacade.getByUuid(uuid);
		if (activity == null || !activity.apiUseable()) {
			return redirectErrorPage(request, "该活动不存在或已下线");
		}
		//User user = getCurrentUser(request);
		WxActivityItem vo = new WxActivityItem();
		vo.buildDetail(activity);

		List<ActivityExpert> experts = activityFacade.searchExpertByWx(
				activity.getId(), Tools.formatYMDHMSDate(new Date()), 20);
		vo.buildExpert(experts);

		List<Resource> resources = effectApiFacade()
				.fetchActivityRecommendResources(activity);
		vo.buildRecommend(resources);

		request.setAttribute("vo_data", vo);

		bulidRequest("微信查看活动详情", "activity", activity.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("wechat/activity/activity_detail");
	}

	/**
	 * @he 2016-06-29修改 获取活动详情的作品列表,分页处理
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/activity/oups/list/{uuid}", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String oupsList(HttpServletRequest request,
			@PathVariable String uuid, @RequestParam String startTime,
			@RequestParam int size) {
		Activity activity = activityFacade.getByUuid(uuid);
		if (activity == null || activity.getStatus() != BaseModel.ACTIVE) {
			return failureModel("活动不存在或已下线").toJSON();
		}
		if (size > 20) {
			size = 20;
		}
		Date date = null;
		try {
			date = Tools.df1.parse(startTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Resource> list = effectApiFacade().fetchActivityResources(
				activity, date, size);
		WxResourceList vo = new WxResourceList();
		vo.build(list);
		return vo.toJSON();
	}

	/**
	 * @he 修改 进入活动详情页 2016-06-29 查看用户是否参加活动
	 * @param request
	 * @param uuid
	 * @return
	 * @2016-07-22 页面数据修改
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/activity/is/sign/up", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String activityIsSign(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = getCurrentUser(request);
		if(user==null){
			return failureModel("not login").toJSON();
		}
		Activity activity = activityFacade.getByUuid(uuid);
		if (activity == null || !activity.apiUseable()) {
			return failureModel("活动不存在或已下线").toJSON();
		}
		ActivityUser activityUser = activityFacade.getActivityUserByUserId(
				user.getId(), activity.getId());
		if (activityUser == null || !activityUser.apiUseable()) {
			return successModel("not_sign").toJSON();
		}
		return successModel("signed").toJSON();

	}

	/**
	 * @he 2016-06-29修改 查看该活动的作品
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/activity/isneed/real/name", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String isneedName(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = getCurrentUser(request);
		if(user==null){
			return failureModel("not login").toJSON();
		}
		Activity activity = activityFacade.getByUuid(uuid);
		if (activity == null || activity.getStatus() != BaseModel.ACTIVE) {
			return failureModel("该活动不存在，或已下线").toJSON();
		}
		if (activity.isNeedRealName()) {
			// 需要实名
			boolean flag = certificationFacade.isUserCertificated(user);
			if (!flag) {
				return failureModel("need real name").toJSON();
			}
		}
		return successModel().toJSON();
	}

	/**
	 * @he 2016-06-29修改 人员报名参加活动，活动增加人员
	 * @he 2016-07-22 修改
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/activity/sign/up/add", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String activitySignUp(@RequestParam String uuid,
			HttpServletRequest request) {
		User user = getCurrentUser(request);
		if(user==null){
			return failureModel("报名失败").toJSON();
		}
		Activity activity = activityFacade.getByUuid(uuid);
		if (activity == null || !activity.apiUseable()) {
			return failureModel("该活动不存在，或已下线").toJSON();
		}
		//报名活动不需要实名
//		if (activity.isNeedRealName()) {
//			// 需要实名
//			boolean flag = certificationFacade.isUserCertificated(user);
//			if (!flag) {
//				return failureModel("提交作品，需要实名认证，请先完成实名认证").toJSON();
//			}
//		}
		Status status = Status.SUCCESS;
		ActivityUser activityUser = activityFacade.getActivityUserByUserId(
				user.getId(), activity.getId());
		if (activityUser == null) {
			activityUser = new ActivityUser();
			activityUser.setUuid(Tools.getUUID());
			activityUser.setUser(user);
			activityUser.setActivity(activity);
			activityUser.setStatus(BaseModel.APPROVED);
			activityUser.setMark("");
			activityUser.setIsActive(BaseModel.ACTIVE);
			status = activityFacade.addActivityUser(activityUser);
		} else {
			activityUser.setIsActive(BaseModel.ACTIVE);
			activityUser.setStatus(BaseModel.APPROVED);
			status = activityFacade.updateActivityUserStatus(activityUser);
		}

		if (status == Status.SUCCESS) {
			bulidRequest("微信活动人员报名", "activity_user", activityUser.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		}
		return failureModel("报名失败").toJSON();
	}

	/**
	 * 专家详情页 2016-06-08
	 * 
	 * @param request
	 * @param uuid
	 * @return 2016-06-29 以后页面资源做分页优化
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_ACTIVITY_EXPERT_SHARE)
	@RequestMapping(value = "/activity/expert/detail/{uuid}", method = RequestMethod.GET)
	public ModelAndView activityExpert(HttpServletRequest request,
			@PathVariable String uuid) {
		ActivityExpert expert = activityFacade.getActivityExpertByUuid(uuid);
		if (expert == null || !expert.apiUseable()) {
			return redirectErrorPage(request, "该专家不存在或已下线");
		}
		Speaker speaker = expert.getSpeaker();
		if (speaker == null || !speaker.apiUseable()) {
			return redirectErrorPage(request, "该专家关联讲者不存在或已下线");
		}
		MediaCentral media = mediaCentralFacade.getActivityExpertMedia(expert,
				MediaCentral.SIGN_WX_BACKGROUND);
		List<Resource> resources = effectResourceFacade()
				.resourceBySpeakerWxPage(speaker.getId(),
						Tools.formatYMDHMSDate(new Date()), 50);
		WxExpertVo vo = new WxExpertVo();
		vo.buildDetail(expert, resources, media, myCollectionFacade,
				praiseFacade);
		request.setAttribute("vo_data", vo);

		bulidRequest("微信查看专家详情", "speaker", speaker.getId(),
				Status.SUCCESS.message(), null, "成功", request);

		return redirectPageView("wechat/activity/specialist");
	}

	/**
	 * @he 修改 2016-06-28
	 * @param request
	 * @param uuid
	 * @return 活动的排行榜
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/activity/res/ranking/{uuid}", method = RequestMethod.GET)
	public ModelAndView activityRankinf(HttpServletRequest request,
			@PathVariable String uuid) {
		Activity activity = activityFacade.getByUuid(uuid);
		if (activity == null || !activity.apiUseable()) {
			return redirectErrorPage(request, "该活动不存在或已下线");
		}
		List<Resource> resources = apiFacade.fetchActivityRanking(activity);
		WxActivityItem vo = new WxActivityItem();
		vo.buildRank(activity);
		vo.buildRankList(resources);
		request.setAttribute("vo_data", vo);
		bulidRequest("微信查看活动的排行榜", "activity", activity.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("wechat/activity/rank");
	}

	/**
	 * @he 2016-06-28 以后优化数据 点击进入提交作品页面
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/activity/opus/page/{uuid}", method = RequestMethod.GET)
	public ModelAndView activityOpus(HttpServletRequest request,
			@PathVariable String uuid) {
		Activity activity = activityFacade.getByUuid(uuid);
		if (activity == null || !activity.apiUseable()) {
			return redirectErrorPage(request, "该活动不存在或已下线");
		}
		request.setAttribute("activityUuid", uuid);
		request.setAttribute("activityName",
				OptionalUtils.traceValue(activity, "name"));
		/**
		 * 2016-4-11 活动获取自身的学科分类
		 */
		List<ActivitySubject> list = activityOpusFacade
				.searchActivitySubjectForWx(activity);
		CommonItemVo vo = new CommonItemVo();
		List<ItemVo> itemList = vo.buildActivitySubject(list);
		request.setAttribute("itemList", itemList);
		bulidRequest("微信进入特定活动作品申请页面", "activity", activity.getId(),
				Status.SUCCESS.message(), null, activity.getName() + " 成功",
				request);
		return redirectPageView("wechat/works/activity_oups");
	}

	/**
	 * @he 2016-06-28 以后优化数据 普通活动点击进入提交作品页面
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = false, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/activity/common/opus/page", method = RequestMethod.GET)
	public ModelAndView activityCommonOpus(HttpServletRequest request) {
		request.setAttribute("activityUuid", COMMON_OUPS);
		bulidRequest("微信进入特公共活动作品申请页面", "activity", null,
				Status.SUCCESS.message(), null, "成功", request);
		return redirectPageView("wechat/works/common_oups");
	}

	/**
	 * @he 2016-06-28 以后优化数据 参加活动人员提交作品申请
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/activity/opus/appilcation/save", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String opusSave(@RequestParam String activityUuid,
			@RequestParam String name, @RequestParam String categoryUuid,
			@RequestParam(required = false) String desc,
			@RequestParam int sendType, HttpServletRequest request) {
		User user = getCurrentUser(request);
		Activity activity = null;
		if (!activityUuid.equals(COMMON_OUPS)) {
			activity = activityFacade.getByUuid(activityUuid);
			if (activity == null || activity.getStatus() != BaseModel.ACTIVE) {
				bulidRequest("微信提交作品申请", "activity_resource_application", null,
						Status.FAILURE.message(), null, "活动的uuid="
								+ activityUuid + " 不存在或已下线", request);
				return failureModel("当前活动不存在，或已下线").toJSON();
			}

			/**
			 * 2016-3-17 判断是否需要实名才能提交
			 */
			if (activity.isNeedRealName()) {
				// 需要实名
				boolean flag = certificationFacade.isUserCertificated(user);
				if (!flag) {
					return failureModel("提交作品，需要实名认证，请先完成实名认证").toJSON();
				}
			}
		}
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null || !category.isActive()) {
			BaseController.bulidRequest("微信提交作品申请",
					"activity_resource_application", null,
					Status.FAILURE.message(), null, "种类的uuid=" + categoryUuid
							+ " 不存在或已下线", request);
			return failureModel("分类不可用，请重新选择").toJSON();
		}
		ActivityApplication activityApplication = new ActivityApplication();
		activityApplication.setUser(user);
		activityApplication.setUuid(Tools.getUUID());
		activityApplication.setName(name);
		activityApplication.setActivity(activity);
		activityApplication.setCategory(category);
		activityApplication.setSendType(sendType);
		activityApplication.setIsActive(BaseModel.ACTIVE);
		activityApplication.setStatus(BaseModel.UNAPPROVED);
		activityApplication.setMark(desc);
		OpusVo vo = activityOpusFacade
				.addActivityapplication(activityApplication);
		if (vo.getStatus() == Status.SUCCESS.message()) {
			BaseController.bulidRequest("微信提交作品申请",
					"activity_resource_application",
					activityApplication.getId(), Status.SUCCESS.message(),
					null, "成功", request);

			/**
			 * 2016-3-17 作品提交发送模板消息
			 */
			if (activityApplication.getUser() != null) {
				String nameValue = certificationFacade
						.realNameByUser(activityApplication.getUser());
				getModelMessageFacade().oupsSubmit(activityApplication,
						activityApplication.getUser(), nameValue);
			}

		} else {
			bulidRequest("微信提交作品申请",
					"activity_resource_application", null,
					Status.FAILURE.message(), null, "活动的uuid=" + activityUuid
							+ " 作品编号生成失败", request);
			vo.setMessage("作品的编号生成失败");
		}
		return vo.toJSON();
	}

	/**
	 * @he 2016-06-28 以后优化数据 在我的作品列表
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/activity/my/oups/page", method = RequestMethod.GET)
	public ModelAndView myOupsPage(HttpServletRequest request,
			@RequestParam(required = false) String originWxUrl) {
		bulidRequest("微信查看我的作品", "activity_resource_application", null,
				Status.SUCCESS.message(), null, "成功", request);
		// 记录微信来源
		bulidRequest(originWxUrl, request);
		return redirectPageView("wechat/works/my_works_list");
	}

	/**
	 * @he 2016-06-28 以后优化数据 我的作品
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/activity/my/center/oups/list", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String myOupsList(HttpServletRequest request,
			@RequestParam int type, @RequestParam String startTime,
			@RequestParam int size) {
		User user = getCurrentUser(request);
		if (size > 20) {
			size = 20;
		}
		List<ActivityApplication> list = activityOpusFacade
				.searchApplicateByUserId(type, user.getId(), startTime, size);
		WxResourceList vo = new WxResourceList();
		if (type == 0) {
			vo.buildNotDownOups(list);
		} else if (type == 1) {
			vo.buildDownOups(list);
		}
		return vo.toJSON();
	}

	/**
	 * @he 2016-06-28 以后优化数据 实名认证,0=上传未审核，1=审核通过 2=审核未通过 3=未上传
	 * @he 2016-08-16 数据优化
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/activity/real/name/page", method = RequestMethod.GET)
	public ModelAndView realNamePage(HttpServletRequest request,
			@RequestParam(required = false) String originWxUrl) {
		User user = getCurrentUser(request);
		Certification certification = activityOpusFacade
				.getCertificationByUserId(user);
		WxUserInfoChange infoChange=new WxUserInfoChange();
		infoChange.certificName(certification, user);
		request.setAttribute("vo_data", infoChange);

		bulidRequest("微信进入实名认证", "certification", null,
				Status.SUCCESS.message(), null, "成功", request);
		// 记录微信来源
		bulidRequest(originWxUrl, request);
		return redirectPageView("wechat/certified");
	}

	/**
	 * @he 2016-06-28 以后优化数据
	 * 实名认证提交
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/activity/real/name/submit", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String realNameSubmit(HttpServletRequest request,
			@RequestParam String real_name, @RequestParam String user_license,
			@RequestParam String city, @RequestParam String hosipital,
			@RequestParam String office, @RequestParam String professor) {
		User user = getCurrentUser(request);
		Certification certification = new Certification();
		certification.setUuid(Tools.getUUID());
		certification.setName(real_name);
		certification.setPinyin(Tools.getPinYin(real_name));
		certification.setUser(user);
		certification.setImageUrl(user_license);
		certification.setStatus(Certification.NO_VERIFY);
		certification.setIsActive(BaseModel.ACTIVE);

		// 附带更新
		UserExpand userExpand = user.getUserExpand();
		if (userExpand != null) {
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
			userExpand.setProfessor(professor);
			userFacade.updateUserInfo(user, userExpand, false);
		}

		Status status = activityOpusFacade.addCertification(certification);
		if (status == Status.SUCCESS) {
			bulidRequest("微信实名认证上传", "certification", certification.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			/**
			 * 2016-3-17 实名提交发送模板消息
			 */
			getModelMessageFacade().realNameSubmit(certification,
					certification.getUser());

			return successModel().toJSON();
		} else {
			bulidRequest("微信实名认证上传", "certification", null,
					Status.FAILURE.message(), null, "保存失败", request);
			return failureModel("保存失败，请重新上传").toJSON();
		}

	}

	/**
	 * 实名认证上传七牛
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/wx/get/upload/file", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String getUploadFile(HttpServletRequest request,
			@RequestParam String media_id) {
		// User user = getCurrentUser(request);
		String forntImageUrl = null;
		try {
			Auth auth = Auth.create(Config.qn_access_key, Config.qn_secret_key);
			BucketManager bucketManager = new BucketManager(auth);
			String url = WebChatMenu.getWxUserUploadFile(media_id);
			System.out.println("url:" + url);
			DefaultPutRet defaultPutRet = bucketManager.fetch(url,
					QiniuUtils.DEF_BUCKET);
			if (defaultPutRet != null && defaultPutRet.key != null
					&& !defaultPutRet.key.isEmpty()) {
				forntImageUrl = Config.qn_cdn_host + "/" + defaultPutRet.key;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.out.println("forntImageUrl:" + forntImageUrl);
		if (forntImageUrl != null) {
			return successModel(forntImageUrl).toJSON();
		}
		return failureModel("图片上传失败，请重新上传").toJSON();

	}

	/**
	 * 征稿上传页面（2016-4-27 以后整理分页）
	 * 
	 * @he 修改 2016-06-28 分页已处理
	 */
	@RequestAuthority(requiredProject = false, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/wx/upload/work", method = RequestMethod.GET)
	public ModelAndView uploadWxWork(HttpServletRequest request) {
		bulidRequest("微信征稿上传", "activity", null, Status.SUCCESS.message(),
				null, "成功", request);
		return redirectPageView("wechat/works/works_upload_list");
	}

	/**
	 * 2014-4-22 征稿上传的作品列表分页
	 * 
	 * @he 修改 2016-06-28
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/works/activity/list", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String worksActivityList(HttpServletRequest request,
			@RequestParam String startTime, @RequestParam int size) {
		if (size > 20) {
			size = 20;
		}
		List<Activity> list = activityFacade.collecteActivites(startTime, size);
		WxActivityList vo = new WxActivityList();
		vo.buildWonderList(list);
		return vo.toJSON();
	}

	/**
	 * @he 2016-06-28 以后优化数据
	 * 作品详情的页面
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/wx/works/detail/{uuid}", method = RequestMethod.GET)
	public ModelAndView worksDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		ActivityApplication application = activityOpusFacade
				.getApplicateByUuid(uuid);
		if (application == null) {
			return redirectErrorPage(request, "该作品不存在");
		}
		OupsDetail wx = new OupsDetail();
		wx.buildData(application);
		request.setAttribute("oups_detail", wx.toJSON());
		bulidRequest("微信查看作品详情", "activity_resource_application",
				application.getId(), Status.SUCCESS.message(), null, "成功",
				request);
		return redirectPageView("wechat/works/my_works_detail");
	}

	/**
	 * 作品的评论的列表
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/wx/oups/comments/list/{uuid}", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String oupsComments(HttpServletRequest request,
			@PathVariable String uuid, @RequestParam String startTime,
			@RequestParam int size) {
		User user = getCurrentUser(request);
		ActivityApplication application = activityOpusFacade
				.getApplicateByUuid(uuid);
		if (size > 20) {
			size = 20;
		}
		int oupsId = 0;
		if (application != null) {
			oupsId = application.getId();
		}
		Date date = null;
		try {
			date = Tools.df1.parse(startTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Message> list = messageFacade.searchWxOupsPageMess(oupsId, date,
				size);
		OupsCommonList vo = new OupsCommonList();
		vo.buildData(user, list);
		return vo.toJSON();
	}

	/**
	 * 作品提交评论
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/wx/oups/add/comments", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String oupsAddComments(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String common) {
		User user = getCurrentUser(request);
		ActivityApplication application = activityOpusFacade
				.getApplicateByUuid(uuid);
		if (application == null || !application.isActive()) {
			return failureModel("作品不存在").toJSON();
		}
		ActionMessage<?> am = messageFacade.addOpusMessage(common, application,
				user);
		if (am.isSuccess()) {
			bulidRequest("微信作品提交反馈",
					"activity_resource_application", application.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		} 
		return failureModel("反馈保存失败").toJSON();
	}

	/**
	 * 作品中删除评论
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/wx/oups/del/comments", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String oupsDelComments(HttpServletRequest request,
			@RequestParam String uuid) {
		Message msg = messageFacade.getByUuid(uuid);
		if (msg == null) {
			return failureModel("消息不存在或已经被删除").toJSON();
		} else {
			messageFacade.del(msg);
			return successModel().toJSON();
		}
	}

}
