package com.lankr.tv_cloud.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityConfig;
import com.lankr.tv_cloud.model.ActivityConfigMedia;
import com.lankr.tv_cloud.model.ActivityConfigMedia.Snapshot;
import com.lankr.tv_cloud.model.ActivityExpert;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.ActivitySubject;
import com.lankr.tv_cloud.model.ActivityUser;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.Notification;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.ActivitySubjectItemVo;
import com.lankr.tv_cloud.vo.ActivitySubjectSurface;
import com.lankr.tv_cloud.vo.ChosenItem;
import com.lankr.tv_cloud.vo.DynamicSearchVo;
import com.lankr.tv_cloud.vo.OptionAdditionList;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.SimpleData;
import com.lankr.tv_cloud.vo.datatable.ActivityData;
import com.lankr.tv_cloud.vo.datatable.ActivityDataItem;
import com.lankr.tv_cloud.vo.datatable.ActivityResData;
import com.lankr.tv_cloud.vo.datatable.ActivityResDataItem;
import com.lankr.tv_cloud.vo.datatable.ActivityUserData;
import com.lankr.tv_cloud.vo.datatable.ActivityUserDataItem;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;
import com.lankr.tv_cloud.vo.snapshot.ActivityExpertItem;
import com.lankr.tv_cloud.vo.snapshot.MessageItem;
import com.lankr.tv_cloud.vo.snapshot.NotificationItem;
import com.lankr.tv_cloud.vo.snapshot.UserItem;
import com.lankr.tv_cloud.web.api.webchat.util.TempleKeyWord;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@SuppressWarnings("all")
@Controller
@RequestMapping(value = "/admin")
public class ActivityController extends AdminWebController {

	private final static String REPEAT_SAVE_SUBMIT_KEY = "activity_save_submit_repeat_key";
	private final static String ACTIVITY_CONFIG_SAVE_SUBMIT_REPEAT_KEY = "activity_config_save_submit_repeat_key";
	private final static String ACTIVITY_RESMGR_SUBMIT_KEY = "activity_resmgr_submit_key";
	private final static String ACTIVITY_EXPERT_SUBMIT_KEY = "activity_expertMgr_submit_key";
	private final static String ACTIVITY_UPDATE_SUBMIT_REPEAT_KEY = "activity_update_submit_repeat_key";
	private static final int MAX_RES_RECOMMEND = 10;

	/**
	 * 新建活动页面跳转
	 * 
	 * @param model
	 * @return /wrapped/activity/add.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/new")
	public ModelAndView renderActivityaddPage(Model model) {
		model.addAttribute("token",
				makeRepeatSubmitToken(REPEAT_SAVE_SUBMIT_KEY));
		return index("/wrapped/activity/add.jsp");
	}

	/**
	 * 新建活动
	 * 
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String saveActivity(@RequestParam String token,
			@RequestParam String categoryUuid, @RequestParam String name,
			@RequestParam int joinType, @RequestParam int plimit,
			@RequestParam(required = false) String mark,
			@RequestParam(required = false) String description,
			@RequestParam int collected, @RequestParam int authentic,
			HttpServletRequest request) {
		if (Tools.isBlank(token)
				|| !token.equals(toastRepeatSubmitToken(request,
						REPEAT_SAVE_SUBMIT_KEY))) {
			return failureModel("表单重复提交").toJSON();
		}
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null) {
			return rebuildTokenModel(REPEAT_SAVE_SUBMIT_KEY, "分类不存在").toJSON();
		}
		// 人数限制
		plimit = Math.max(Activity.INFINITE, plimit);
		// 活动参与类型
		joinType = (joinType == Activity.TYPE_PUBLIC ? Activity.TYPE_PUBLIC
				: Activity.TYPE_PROTECTED);
		Activity activity = new Activity();
		activity.setName(name);
		activity.setPlimit(plimit);
		activity.setJoinType(joinType);
		activity.setDescription(description);
		activity.setMark(mark);
		activity.setStatus(BaseModel.UNAPPROVED);
		activity.setIsActive(BaseModel.ACTIVE);
		activity.setCategory(category);
		activity.setUser(getCurrentUser(request));
		activity.setAuthentic(authentic == 0 ? BaseModel.FALSE : BaseModel.TRUE);
		activity.setCollected(collected == 0 ? BaseModel.FALSE : BaseModel.TRUE);

		ActionMessage action = activityFacade.add(activity);
		if (action.isSuccess()) {
			BaseController.bulidRequest("新建活动", "activity", null,
					Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		}
		BaseController.bulidRequest("新建活动", "activity", null,
				Status.FAILURE.message(), null, "失败", request);
		return failureModel(action.getMessage()).toJSON();
	}

	/**
	 * 后台活动列表页面跳转
	 * 
	 * @param model
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/list")
	public ModelAndView renderActivityListPage(Model model) {
		return index("/wrapped/activity/list.jsp");
	}

	/**
	 * 后台查寻活动列表数据
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/list/datatable")
	public @ResponseBody String activityDataJson(Model model,
			HttpServletRequest request) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		String searchsortIdString = request.getParameter("iSortCol_0");
		String sortValString = request.getParameter("sSortDir_0");// ["desc"]iSortCol_0":["2"]
		String sortvalueString = request.getParameter("mDataProp_"
				+ searchsortIdString);
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Activity> results = activityFacade
				.searchActivitiesForDatatable(q, from, size);
		ActivityData data = new ActivityData();
		data.build(results.getResults());
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * 后台更新活动状态
	 * 
	 * @param status
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/activity/{uuid}/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityStatus(@RequestParam int status,
			HttpServletRequest request, @PathVariable String uuid) {
		Activity activity = activityFacade.getByUuid(uuid);
		if (activity == null)
			return failureModel("活动无效").toJSON();
		int willing = 2;
		if (status == 0)
			willing = 1;
		if (status == 1)
			willing = 2;
		if (status == 2)
			willing = 1;
		if (willing == 1) {
			// 如果活动想上线，必须配置有相关
			if (activity.getConfig() == null) {
				BaseController.bulidRequest("后台修改活动状态", "activity",
						activity.getId(), Status.FAILURE.message(), null,
						"失败,活动还未配置", request);
				return failureModel("上线前，请完成相关配置").toJSON();
			}
		}
		activity = activityFacade.changeStatus(activity, willing);
		if (activity == null) {
			BaseController.bulidRequest("后台修改活动状态", "activity",
					activity.getId(), Status.FAILURE.message(), null, "失败",
					request);
			return failureModel("更改状态失败").toJSON();
		}
		BaseController.bulidRequest("后台修改活动状态", "activity", activity.getId(),
				Status.SUCCESS.message(), null,
				"成功，转换为＝" + activity.getStatus(), request);
		ActivityDataItem item = ActivityDataItem.build(activity);
		return BaseAPIModel.makeWrappedSuccessDataJson(item);
	}

	/**
	 * 后台活动配置页面跳转
	 * 
	 * @param model
	 * @param uuid
	 * @return /wrapped/activity/config.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/{uuid}/config")
	public ModelAndView activitySettings(Model model, @PathVariable String uuid) {
		model.addAttribute("activity", activityFacade.getByUuid(uuid));
		model.addAttribute("token",
				makeRepeatSubmitToken(ACTIVITY_CONFIG_SAVE_SUBMIT_REPEAT_KEY));
		return index("/wrapped/activity/config.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/qiniu/uploader/sign", produces = "text/json; charset=utf-8")
	public @ResponseBody String getQiniuUploaderSign() {
		return BaseAPIModel.makeSimpleSuccessInnerDataJson(new SimpleData(
				"signature", getQiniuUploaderSignature()));
	}

	/**
	 * 保存资源配置信息
	 * 
	 * @param uuid
	 * @param auto
	 * @param token
	 * @param medias
	 * @param notification
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/{uuid}/config/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityConfigSave(@PathVariable String uuid,
			@RequestParam int auto, @RequestParam String token,
			@RequestParam(required = false) String medias,
			@RequestParam(required = false) String notification,
			HttpServletRequest request) {
		if (Tools.isBlank(token)
				|| !token.equals(toastRepeatSubmitToken(request,
						ACTIVITY_CONFIG_SAVE_SUBMIT_REPEAT_KEY))) {
			BaseController.bulidRequest("后台活动配置", "activity_config", null,
					Status.FAILURE.message(), null, "失败,操作过于频繁", request);
			return failureModel("表单重复提交").toJSON();
		}
		Activity activity = activityFacade.getByUuid(uuid);
		if (activity == null) {
			BaseController.bulidRequest("后台活动配置", "activity", null,
					Status.FAILURE.message(), null, "失败,对应活动不存在", request);
			return failureModel("活动不存在").toJSON();
		}
		ActivityConfig config = activityFacade.getActivityConfig(activity);
		if (config == null) {
			config = new ActivityConfig();
		}
		config.setNotification(notification);
		config.setAuto(auto == 0 ? 0 : 1);
		config.setActivity(activity);
		try {
			List<ActivityConfigMedia> config_medias = Snapshot
					.convertMedias(medias);
			config.setMedias(gson.toJson(config_medias));
		} catch (Exception e) {
			e.printStackTrace();
			BaseController.bulidRequest("后台活动配置", "activity_config", null,
					Status.FAILURE.message(), null, "失败,图片数据有误", request);
			return failureModel("图片数据有误，无法保存").toJSON();
		}
		ActionMessage action = activityFacade.saveActivtyConfig(config);
		if (action.isSuccess()) {
			BaseController.bulidRequest("后台活动配置", "activity_config",
					config.getId(), Status.SUCCESS.message(), null, "成功",
					request);
			return successModel().toJSON();
		}
		BaseController.bulidRequest("后台活动配置", "activity_config",
				config.getId(), Status.FAILURE.message(), null, "失败", request);
		return actionModel(action).toJSON();
	}

	/**
	 * 生成活动的二维码的测试接口
	 */
	// @RequestMapping(value = "/activity/qr/test", method = RequestMethod.GET,
	// produces = "text/json; charset=utf-8")
	// @ResponseBody
	// public String getAcviteQrTest(@RequestParam String acviteUuid) {
	// Activity activity = activityFacade.getByUuid(acviteUuid);
	// if (activity == null || !activity.isActive()) {
	// return failureModel("活动不可用").toJSON();
	// }
	// QrCode qrcode = qrCodeFacade.getQrByActiveType(activity);
	// if (qrcode == null) {
	// return failureModel("二维码生成失败").toJSON();
	// }
	// return gson.toJson(qrcode);
	// }

	/**
	 * 跳转到资源配置页面
	 * 
	 * @param model
	 * @param uuid
	 * @return /wrapped/activity/resource_mgr.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/{uuid}/resmgr")
	public ModelAndView activityResourceMgr(Model model,
			@PathVariable String uuid) {
		model.addAttribute("activity", activityFacade.getByUuid(uuid));
		model.addAttribute("token",
				makeRepeatSubmitToken(ACTIVITY_RESMGR_SUBMIT_KEY));
		return index("/wrapped/activity/resource_mgr.jsp");
	}

	/**
	 * 资源配置页面搜索资源
	 * 
	 * @param request
	 * @param queryKey
	 * @return json
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/resmgr/search", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityResmgrSearch(
			HttpServletRequest request,
			@RequestParam(required = false, value = "q") String queryKey) {
		List<Resource> list = resourceFacade.searchResourceListByQ(queryKey);
		OptionAdditionList model = new OptionAdditionList();
		model.buildResource(list);
		model.setQ(queryKey);
		return gson.toJson(model);
	}

	/**
	 * 添加活动资源
	 * 
	 * @param request
	 * @param mark
	 * @param activityUuid
	 * @param token
	 * @param resourceUuid
	 * @return ActionMessage
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/resmgr/save/", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityResmgrSave(HttpServletRequest request,
			@RequestParam String mark, @RequestParam String activityUuid,
			@RequestParam String token, @RequestParam String resourceUuid) {
		if (Tools.isBlank(token)
				|| !token.equals(toastRepeatSubmitToken(request,
						ACTIVITY_RESMGR_SUBMIT_KEY))) {
			BaseController.bulidRequest("后台添加活动资源", "activity_resource", null,
					Status.FAILURE.message(), null, "失败，提交过于频繁", request);
			// return failureModel("你已经提交过表单，请刷新页面").toJSON();
			return failureModel("貌似你点鼠标的速度快了点，请刷新页面重新添加").toJSON();
		}
		Activity activity = activityFacade.getByUuid(activityUuid);
		if (activity == null) {
			BaseController.bulidRequest("后台添加活动资源", "activity", null,
					Status.FAILURE.message(), null, "失败，相应活动不存在", request);
			return failureModel("活动不存在").toJSON();
		}
		Resource resource = resourceFacade.getResourceByUuid(resourceUuid);
		if (null == resource) {
			BaseController.bulidRequest("后台添加活动资源", "resource", null,
					Status.FAILURE.message(), null, "失败，相应资源不存在", request);
			return failureModel("资源有误").toJSON();
		}
		ActivityResource activityResource = new ActivityResource();
		activityResource.setResource(resource);
		activityResource.setActivity(activity);
		activityResource.setMark(mark);
		activityResource.setUuid(Tools.getUUID());
		// 20160223 activityResource.setStatus(resource.getStatus());
		activityResource.setStatus(BaseModel.UNDERLINE);
		activityResource.setName(resource.getName());
		activityResource.setIsActive(BaseModel.ACTIVE);
		ActionMessage action = activityFacade
				.saveActivityResource(activityResource);
		if (action.isSuccess()) {
			BaseController.bulidRequest("后台添加活动资源", "activity_resource", null,
					Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		}
		BaseController.bulidRequest("后台添加活动资源", "activity_resource", null,
				Status.FAILURE.message(), null, "失败,保存过程中出错", request);
		return actionModel(action).toJSON();
	}

	/**
	 * 跳转到活动资源列表
	 * 
	 * @param model
	 * @param uuid
	 * @return /wrapped/activity/resource_list.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/{uuid}/reslist")
	public ModelAndView activityResourceList(Model model,
			@PathVariable String uuid) {
		model.addAttribute("activity", activityFacade.getByUuid(uuid));
		return index("/wrapped/activity/resource_list.jsp");
	}

	/**
	 * 查询对应活动下的资源
	 * 
	 * @param model
	 * @param request
	 * @param activityUuid
	 * @return json
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/reslist/data/{activityUuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityResDataJson(Model model,
			HttpServletRequest request, @PathVariable String activityUuid) {
		String queryKey = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageSize = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int startPage = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<ActivityResource> results = activityFacade
				.searchActivitieResForDatatable(activityUuid, queryKey,
						startPage, pageSize);
		ActivityResData data = new ActivityResData();
		data.build(results.getResults());
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * 删除活动资源
	 * 
	 * @param request
	 * @param uuid
	 * @return ActionMessage
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/resmgr/del/", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String deleteActivityResource(
			HttpServletRequest request, @RequestParam String uuid) {
		ActivityResource activityResource = activityFacade
				.getActivityResourceByUuid(uuid);
		if (null == activityResource) {
			BaseController.bulidRequest("后台删除活动资源", "activity_resource", null,
					Status.FAILURE.message(), null, "失败,活动资源不存在", request);
			return failureModel("活动资源不存在").toJSON();
		}
		ActionMessage action = activityFacade
				.deleteActivityResource(activityResource);
		if (action.isSuccess()) {
			BaseController.bulidRequest("后台删除活动资源", "activity_resource",
					activityResource.getId(), Status.SUCCESS.message(), null,
					"成功", request);
			return successModel().toJSON();
		}
		BaseController.bulidRequest("后台删除活动资源", "activity_resource", null,
				Status.FAILURE.message(), null, "失败,删除过程中出错", request);
		return failureModel(action.getMessage()).toJSON();
	}

	/**
	 * 查询活动成员页面跳转
	 * 
	 * @param model
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/{uuid}/userlist")
	public ModelAndView activityUserList(Model model, @PathVariable String uuid) {
		model.addAttribute("activity", activityFacade.getByUuid(uuid));
		return index("/wrapped/activity/user_list.jsp");
	}

	/**
	 * 查询选中活动的成员
	 * 
	 * @param model
	 * @param request
	 * @param activityUuid
	 * @return json
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/userlist/data/{activityUuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityUserDataJson(Model model,
			HttpServletRequest request, @PathVariable String activityUuid) {
		String queryKey = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageSize = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int startPage = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<ActivityUser> results = activityFacade
				.searchActivitieUserForDatatable(activityUuid, queryKey,
						startPage, pageSize);
		ActivityUserData data = new ActivityUserData();
		data.build(results.getResults());
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * 后台移除活动成员
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/resuser/del/", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String deleteActivityUser(HttpServletRequest request,
			@RequestParam String uuid) {
		ActivityUser activityUser = activityFacade.getActivityUserByUuid(uuid);
		if (null == activityUser) {
			BaseController.bulidRequest("后台删除活动成员", "activity_user", null,
					Status.FAILURE.message(), null, "失败,活动成员有误", request);
			return failureModel("活动成员有误").toJSON();
		}
		ActionMessage action = activityFacade.deleteActivityUser(activityUser);
		if (action.isSuccess()) {
			BaseController.bulidRequest("后台删除活动成员", "activity_user",
					activityUser.getId(), Status.SUCCESS.message(), null, "成功",
					request);
			return successModel().toJSON();
		}
		BaseController.bulidRequest("后台删除活动成员", "activity_user",
				activityUser.getId(), Status.FAILURE.message(), null,
				"失败,删除活动成员过程中出错", request);
		return failureModel(action.getMessage()).toJSON();
	}

	/**
	 * 更新活动资源状态，以 status 为阀值
	 * 
	 * @param uuid
	 * @param status
	 * @return json
	 */
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/activity/res/updateState", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityResStatus(@RequestParam String uuid,
			HttpServletRequest request, @RequestParam int status) {
		ActivityResource activityResource = activityFacade
				.getActivityResourceByUuid(uuid);
		if (activityResource == null) {
			BaseController.bulidRequest("后台更新活动资源状态", "activity_resource",
					null, Status.FAILURE.message(), null, "失败,活动资源有误", request);
			return failureModel("活动资源无效").toJSON();
		}
		activityResource.setStatus(BaseModel.UNDERLINE == activityResource
				.getStatus() ? BaseModel.APPROVED : BaseModel.UNDERLINE);
		ActivityResource aResource = activityFacade
				.updateActivityResState(activityResource);
		if (aResource == null) {
			BaseController.bulidRequest("后台更新活动资源状态", "activity_resource",
					null, Status.FAILURE.message(), null, "失败,更新活动资源状态过程中出错",
					request);
			return failureModel("更改状态失败").toJSON();
		}
		int willing = 2;
		if (status == 0)
			willing = 1;
		if (status == 1)
			willing = 2;
		if (status == 2)
			willing = 1;
		activityResource = activityFacade.changeActivityResStatus(
				activityResource, willing);
		if (activityResource == null) {
			BaseController.bulidRequest("后台更新活动资源状态", "activity_resource",
					null, Status.FAILURE.message(), null, "失败,更新活动资源状态过程中出错",
					request);
			return failureModel("更改状态失败").toJSON();
		}
		ActivityResDataItem item = ActivityResDataItem.build(activityResource);
		BaseController.bulidRequest("后台更新活动资源状态", "activity_resource",
				activityResource.getId(), Status.SUCCESS.message(), null, "成功",
				request);
		return BaseAPIModel.makeWrappedSuccessDataJson(item);
	}

	/**
	 * 活动成员状态操作，以 isActive 为阀值
	 * 
	 * @param uuid
	 * @param isActive
	 * @return json
	 */
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/activity/resuser/updateIsActive", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityUserIsActive(@RequestParam String uuid,
			HttpServletRequest request, @RequestParam int isActive) {
		ActivityUser activityUser = activityFacade.getActivityUserByUuid(uuid);
		if (activityUser == null) {
			BaseController.bulidRequest("后台更新活动成员状态", "activity_user", null,
					Status.FAILURE.message(), null, "失败，活动成员无效", request);
			return failureModel("活动成员无效").toJSON();
		}
		activityUser.setIsActive(isActive == 1 ? 0 : 1);
		ActivityUser aUser = activityFacade
				.updateActivityUserIsAvtive(activityUser);
		if (aUser == null) {
			BaseController.bulidRequest("后台更新活动成员状态", "activity_user",
					aUser.getId(), Status.FAILURE.message(), null,
					"失败，更新活动成员过程中出错", request);
			return failureModel("更改状态失败").toJSON();
		}
		BaseController.bulidRequest("后台更新活动成员状态", "activity_user",
				aUser.getId(), Status.SUCCESS.message(), null, "成功", request);
		ActivityUserDataItem item = ActivityUserDataItem.build(aUser);
		return BaseAPIModel.makeWrappedSuccessDataJson(item);
	}

	/**
	 * 活动编辑页面跳转
	 * 
	 * @param model
	 * @param uuid
	 *            活动唯一标识
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/{uuid}/edit")
	public ModelAndView activityUpdate(Model model, @PathVariable String uuid) {
		Activity activityByUuid = activityFacade.getByUuid(uuid);
		model.addAttribute("activity", activityByUuid);
		model.addAttribute("token",
				makeRepeatSubmitToken(ACTIVITY_UPDATE_SUBMIT_REPEAT_KEY));
		return index("/wrapped/activity/update.jsp");
	}

	/**
	 * 更新活动
	 * 
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/update/", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityUpdateSubmit(
			HttpServletRequest request, @RequestParam String token,
			@RequestParam String activityUuid, @RequestParam String medias,
			@RequestParam String name, @RequestParam String categoryUuid,
			@RequestParam int joinType, @RequestParam int plimit,
			@RequestParam int auto, @RequestParam String mark,
			@RequestParam String description,
			@RequestParam String notification, @RequestParam int collected,
			@RequestParam int authentic) {
		if (Tools.isBlank(token)
				|| !token.equals(toastRepeatSubmitToken(request,
						ACTIVITY_UPDATE_SUBMIT_REPEAT_KEY))) {
			BaseController.bulidRequest("后台更新活动", "activity,activity_config",
					null, Status.FAILURE.message(), null, "失败，提交操作过于频繁",
					request);
			return failureModel("您的提交过于频繁，请刷新页面重新尝试").toJSON();
		}
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (null == category) {
			BaseController.bulidRequest("后台更新活动", "category", null,
					Status.FAILURE.message(), null, "失败，查询分类过程中出错", request);
			return rebuildTokenModel(ACTIVITY_UPDATE_SUBMIT_REPEAT_KEY, "分类不存在")
					.toJSON();
		}
		Activity activity = activityFacade.getByUuid(activityUuid);
		if (null == activity) {
			BaseController.bulidRequest("后台更新活动", "activity", null,
					Status.FAILURE.message(), null, "失败，查询活动信息过程中出错", request);
			return failureModel("活动不存在").toJSON();
		}
		ActivityConfig config = activityFacade.getActivityConfig(activity);
		if (config == null) {
			config = new ActivityConfig();
		}

		// 人数限制
		plimit = Math.max(Activity.INFINITE, plimit);
		// 活动参与类型
		joinType = (joinType == Activity.TYPE_PUBLIC ? Activity.TYPE_PUBLIC
				: Activity.TYPE_PROTECTED);
		activity.setName(name);
		activity.setPlimit(plimit);
		activity.setJoinType(joinType);
		activity.setDescription(description);
		activity.setMark(mark);
		activity.setCategory(category);
		activity.setUser(getCurrentUser(request));
		activity.setAuthentic(authentic == 0 ? BaseModel.FALSE : BaseModel.TRUE);
		activity.setCollected(collected == 0 ? BaseModel.FALSE : BaseModel.TRUE);

		config.setNotification(notification);
		config.setAuto(auto == 0 ? 0 : 1);
		config.setActivity(activity);
		try {
			List<ActivityConfigMedia> config_medias = Snapshot
					.convertMedias(medias);
			config.setMedias(gson.toJson(config_medias));
		} catch (Exception e) {
			e.printStackTrace();
			BaseController.bulidRequest("后台更新活动", "activity,activity_config",
					null, Status.FAILURE.message(), null, "失败，图片数据有误", request);
			return failureModel("图片数据有误，无法保存").toJSON();
		}
		ActionMessage action = activityFacade.updateActivityWithConfig(config);
		if (action.isSuccess()) {
			BaseController.bulidRequest("后台更新活动", "activity,activity_config",
					null, Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		}
		BaseController.bulidRequest("后台更新活动", "activity,activity_config", null,
				Status.FAILURE.message(), null, "失败，更新活动数据过程中出错", request);
		return failureModel(action.getMessage()).toJSON();
	}

	/**
	 * 获取活动的二维码
	 */
	// @RequestAuthority(value = Role.PRO_EDITOR)
	// @RequestMapping(value = "/activity/get/qrcode/{uuid}", method =
	// RequestMethod.GET, produces = "text/json; charset=utf-8")
	// public @ResponseBody String getQrcode(HttpServletRequest request,
	// @PathVariable String uuid) {
	// Activity activity = activityFacade.getByUuid(uuid);
	// if (null == activity || !activity.isActive()) {
	// BaseController.bulidRequest("获取活动二维码", "activity", null,
	// Status.FAILURE.message(), null, "失败，查询活动数据过程中出错", request);
	// return failureModel("活动不存在").toJSON();
	// }
	// if(activity.getStatus()==BaseModel.UNAPPROVED){
	// return failureModel("活动请先审核").toJSON();
	// }
	// QrCode qrCode = qrCodeFacade.getQrByActiveType(activity);
	// if (qrCode == null) {
	// BaseController.bulidRequest("获取活动二维码", "qrscene", null,
	// Status.FAILURE.message(), null, "失败，获取活动二维码出错", request);
	// return failureModel("活动的二维码生成失败，请重新生成").toJSON();
	// }
	// BaseController.bulidRequest("获取活动二维码", "qrscene", null,
	// Status.SUCCESS.message(), null, "成功", request);
	// return successModel(qrCode.getQrurl()).toJSON();
	// }

	/**
	 * 资源推荐
	 * 
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/activity/resource/recommend", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityResRecommend(@RequestParam String uuid,
			HttpServletRequest request) {
		ActivityResource activityRes = activityFacade
				.getActivityResourceByUuid(uuid);
		if (null == activityRes) {
			BaseController.bulidRequest("后台手动资源置顶", "activity_resource", null,
					Status.FAILURE.message(), null, "失败，获取活动信息出错", request);
			return failureModel("活动无效").toJSON();
		}

		int count = activityFacade.getRecommendResourceCount(activityRes);
		if (count >= MAX_RES_RECOMMEND
				&& activityRes.getRecommendDate() == null) {
			return failureModel("请保持推荐数不多余10个，如需继续，请取消您之前推荐的部分资源").toJSON();
		}
		activityRes = activityFacade.recommendResource(activityRes);
		if (activityRes == null) {
			BaseController.bulidRequest("后台手动资源置顶", "activity_resource", null,
					Status.FAILURE.message(), null, "失败，更新推荐时间过程中出错", request);
			return failureModel("出错啦，请刷新页面稍后重试").toJSON();
		}
		ActivityResDataItem item = ActivityResDataItem.build(activityRes);
		BaseController.bulidRequest("后台手动资源置顶", "activity_resource", null,
				Status.SUCCESS.message(), null, "成功", request);
		return BaseAPIModel.makeWrappedSuccessDataJson(item);
	}

	/**
	 * 2016-04-07 活动的学科配置页面
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/activity/subject/page/{uuid}")
	public ModelAndView activitySubjectPage(HttpServletRequest request,
			@PathVariable String uuid) {
		Activity activity = activityFacade.getByUuid(uuid);
		if (null == activity || !activity.isActive()) {
			request.setAttribute("error", "活动不存在");
			return index(request, "/wrapped/activity/activity_subject.jsp");
		}
		request.setAttribute("name", activity.getName());
		request.setAttribute("uuid", uuid);
		return index(request, "/wrapped/activity/activity_subject.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/activity/subject/list/{uuid}")
	public @ResponseBody String subjectList(HttpServletRequest request,
			@PathVariable String uuid) {
		Activity activity = activityFacade.getByUuid(uuid);
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<ActivitySubject> results = activityFacade
				.searchActivitySubjectForTable(q, activity.getId(), from, size);
		ActivitySubjectSurface data = new ActivitySubjectSurface();
		data.buildList(results.getResults());
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * 编辑学科的数据
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/activity/subject/update/page", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String subjectUpdatePage(@RequestParam String uuid,
			HttpServletRequest request) {
		ActivitySubject subject = activityFacade.getActivitySubjectByUuid(uuid);
		if (subject == null || !subject.isActive()) {
			return failureModel("此活动学科不存在").toJSON();
		}
		ActivitySubjectItemVo vo = new ActivitySubjectItemVo();
		vo.updatePageData(subject);
		return vo.toJSON();
	}

	/**
	 * 增加活动的学科
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/activity/subject/add/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String subjectAddData(
			@RequestParam String activityUuid, @RequestParam String cateUuid,
			@RequestParam String cateNickName, HttpServletRequest request) {
		Activity activity = activityFacade.getByUuid(activityUuid);
		if (activity == null) {
			return failureModel("此活动不存在").toJSON();
		}
		Category category = assetFacade.getCategoryByUuid(cateUuid);
		if (category == null) {
			return failureModel("分类不存在，请重新选择").toJSON();
		}
		ActivitySubject subject = new ActivitySubject();
		subject.setUuid(Tools.getUUID());
		subject.setCategory(category);
		subject.setName(cateNickName);
		subject.setPinyin(Tools.getPinYin(cateNickName));
		subject.setActivity(activity);
		subject.setStatus(BaseModel.APPROVED);
		subject.setIsActive(BaseModel.ACTIVE);
		Status status = activityFacade.addActivitySubject(subject);
		if (status == Status.SUCCESS) {
			bulidRequest("后台活动添加学科", "activity_subject", subject.getId(),
					status.message(), null, "成功", request);
			ActivitySubjectItemVo vo = new ActivitySubjectItemVo();
			vo.buildTableItem(subject);
			return vo.toJSON();

		}
		return failureModel("保存失败").toJSON();
	}

	/**
	 * 更新活动的学科
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/activity/subject/update", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String subjectUpdateData(@RequestParam String uuid,
			@RequestParam String cateUuid, @RequestParam String cateNickName,
			HttpServletRequest request) {
		ActivitySubject subject = activityFacade.getActivitySubjectByUuid(uuid);
		if (subject == null || !subject.isActive()) {
			return failureModel("此活动学科不存在").toJSON();
		}
		Category category = assetFacade.getCategoryByUuid(cateUuid);
		if (category == null) {
			return failureModel("分类不存在，请重新选择").toJSON();
		}
		subject.setCategory(category);
		subject.setName(cateNickName);
		subject.setPinyin(Tools.getPinYin(cateNickName));
		Status status = activityFacade.updateActivitySubject(subject);
		if (status == Status.SUCCESS) {
			bulidRequest("后台活动更新学科", "activity_subject", subject.getId(),
					status.message(), null, "成功", request);
			ActivitySubjectItemVo vo = new ActivitySubjectItemVo();
			vo.buildTableItem(subject);
			return vo.toJSON();

		}
		return failureModel("更新失败").toJSON();
	}

	/**
	 * 删除活动的学科
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/activity/subject/detele", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String subjectDetele(@RequestParam String uuid,
			HttpServletRequest request) {
		ActivitySubject subject = activityFacade.getActivitySubjectByUuid(uuid);
		if (subject == null || !subject.isActive()) {
			return failureModel("此活动学科不存在").toJSON();
		}
		Status status = activityFacade.deteleActivitySubject(subject);
		if (status == Status.SUCCESS) {
			bulidRequest("后台活动删除学科", "activity_subject", subject.getId(),
					status.message(), null, "成功", request);
			return successModel().toJSON();

		}
		return failureModel("更新失败").toJSON();
	}

	/**
	 * @Description: 活动专家列表页跳转
	 *
	 * @author mayuan
	 * @createDate 2016年6月3日
	 * @modifyDate 2016年6月3日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/activity/expert/page/{uuid}")
	public ModelAndView activityExpertPage(HttpServletRequest request,
			@PathVariable String uuid) {
		Activity activity = activityFacade.getByUuid(uuid);
		if (null == activity || !activity.isActive()) {
			request.setAttribute("error", "该活动有误，请刷新页面重新尝试");
			return index(request, "/wrapped/activity/list.jsp");
		}
		request.setAttribute("activity", activity);
		return index(request, "/wrapped/activity/expert_list.jsp");
	}

	/**
	 * @Description: 活动专家配置页面跳转
	 *
	 * @author mayuan
	 * @createDate 2016年6月3日
	 * @modifyDate 2016年6月3日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/expertMgr/{uuid}")
	public ModelAndView activityExpertMgr(Model model, @PathVariable String uuid) {
		model.addAttribute("activity", activityFacade.getByUuid(uuid));
		model.addAttribute("token",
				makeRepeatSubmitToken(ACTIVITY_EXPERT_SUBMIT_KEY));
		return index("/wrapped/activity/expert_mgr.jsp");
	}

	/**
	 * @Description: 活动专家列表
	 *
	 * @author mayuan
	 * @createDate 2016年6月3日
	 * @modifyDate 2016年6月3日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/expertList/data/{activityUuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityExxpertDataJson(Model model,
			HttpServletRequest request,
			@PathVariable(value = "activityUuid") String activityUuid) {
		String queryKey = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageSize = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int startPage = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<ActivityExpert> pagination = activityFacade
				.searchActivitieExpertForDatatable(activityUuid, queryKey,
						startPage, pageSize);

		DataTableModel data = DataTableModel.makeInstance(
				pagination.getResults(), ActivityExpertItem.class);
		data.setiTotalDisplayRecords(pagination.getTotal());
		data.setiTotalRecords(pagination.getPage_rows());

		/*
		 * ActivityExpertData data = new ActivityExpertData();
		 * data.build(results.getResults());
		 * data.setiTotalDisplayRecords(results.getTotal());
		 * data.setiTotalRecords(results.getPage_rows());
		 */
		return data.toJSON();
	}

	/**
	 * @Description: 展示／取消展示专家
	 *
	 * @author mayuan
	 * @createDate 2016年6月3日
	 * @modifyDate 2016年6月3日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/activity/expert/updateState", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityExpertStatus(
			HttpServletRequest request, @RequestParam String uuid,
			@RequestParam int status) {
		ActivityExpert activityExpert = activityFacade
				.getActivityExpertByUuid(uuid);
		if (activityExpert == null) {
			BaseController.bulidRequest("后台操作活动专家状态", "activity_expert", null,
					Status.FAILURE.message(), null, "失败", request);
			return failureModel("该记录无效，请重新尝试").toJSON();
		}
		if (activityExpert.getStatus() == BaseModel.UNAPPROVED) {
			activityExpert.setStatus(BaseModel.APPROVED);
		} else {
			activityExpert
					.setStatus(activityExpert.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
							: BaseModel.APPROVED);
		}
		ActionMessage action = activityFacade
				.updateActivityExpertStatus(activityExpert);
		if (action.isSuccess()) {
			BaseController.bulidRequest("后台操作活动专家状态", "activity_expert",
					activityExpert.getId(), Status.SUCCESS.message(), null,
					"成功", request);

			/*
			 * ActivityExpertDataItem item =
			 * ActivityExpertDataItem.build(activityExpert); return
			 * BaseAPIModel.makeWrappedSuccessDataJson(item);
			 */

			List list = new ArrayList<ActivityExpert>();
			list.add(activityExpert);
			DataTableModel data = DataTableModel.makeInstance(list,
					ActivityExpertItem.class);
			return data.toJSON();

		} else {
			BaseController.bulidRequest("后台操作活动专家状态", "activity_expert",
					activityExpert.getId(), Status.FAILURE.message(), null,
					"失败", request);
			return BaseController.failureModel(action.getMessage()).toJSON();
		}
	}

	/**
	 * @Description: 移除专家
	 *
	 * @author mayuan
	 * @createDate 2016年6月3日
	 * @modifyDate 2016年6月3日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/activity/expert/del", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String removeExpert(HttpServletRequest request,
			@RequestParam String uuid) {
		ActivityExpert activityExpert = activityFacade
				.getActivityExpertByUuid(uuid);
		if (activityExpert == null) {
			BaseController.bulidRequest("后台删除活动专家", "activity_expert", null,
					Status.FAILURE.message(), null, "失败", request);
			return failureModel("该记录无效，请重新尝试").toJSON();
		}
		if (activityExpert.getStatus() == BaseModel.APPROVED) {
			return failureModel("请先下线该游戏").toJSON();
		}
		activityExpert.setIsActive(BaseModel.DISABLE);
		ActionMessage action = activityFacade
				.updateActivityExpertStatus(activityExpert);
		if (action.isSuccess()) {
			BaseController.bulidRequest("后台删除活动专家", "activity_expert",
					activityExpert.getId(), Status.SUCCESS.message(), null,
					"成功", request);
			return successModel().toJSON();
		} else {
			BaseController.bulidRequest("后台删除活动专家", "activity_expert",
					activityExpert.getId(), Status.FAILURE.message(), null,
					"失败", request);
			return BaseController.failureModel(action.getMessage()).toJSON();
		}
	}

	/**
	 * @Description: 保存专家
	 *
	 * @author mayuan
	 * @createDate 2016年6月3日
	 * @modifyDate 2016年6月3日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/expertMgr/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String saveActiveExpert(HttpServletRequest request,
			@RequestParam String speakerUuid, @RequestParam String token,
			@RequestParam String activityUuid,
			@RequestParam String webchatBgImage,
			@RequestParam(required = false) String activityExpertUuid,
			@RequestParam(required = false) String mark) {
		if (!token.equals(toastRepeatSubmitToken(ACTIVITY_EXPERT_SUBMIT_KEY))) {
			return failureModel("重复提交，或者页面已过期，请刷新重试").toJSON();
		}
		if (StringUtils.isEmpty(webchatBgImage)) {
			return failureModel("请上传专家微信背景图").toJSON();
		}
		Activity activity = activityFacade.getByUuid(activityUuid);
		if (activity == null) {
			return failureModel("活动不存在").toJSON();
		}
		Speaker speaker = assetFacade.getSpeakerByUuid(speakerUuid);
		if (speaker == null) {
			return failureModel("讲者不存在").toJSON();
		}

		ActivityExpert activityExpert = activityFacade
				.getActivityExpertByUuid(activityExpertUuid);
		if (null == activityExpert) {
			activityExpert = new ActivityExpert();
			activityExpert.setUuid(Tools.getUUID());
			activityExpert.setSpeaker(speaker);
			activityExpert.setActivity(activity);
			activityExpert.setStatus(BaseModel.UNDERLINE);
			activityExpert.setIsActive(BaseModel.ACTIVE);
			activityExpert.setMark(mark);
		} else {
			// activityExpert.setSpeaker(speaker);
			activityExpert.setMark(mark);
		}

		HashMap<Integer, String> bgImgs = new HashMap<Integer, String>();
		bgImgs.put(MediaCentral.SIGN_WX_BACKGROUND, webchatBgImage);

		ActionMessage action = activityFacade.saveActivityExpert(
				activityExpert, bgImgs);
		if (action.isSuccess()) {
			BaseController.bulidRequest("后台添加活动专家", "activity_expert", null,
					Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		}
		BaseController.bulidRequest("后台添加活动专家", "activity_expert", null,
				Status.FAILURE.message(), null, "失败,保存过程中出错", request);
		return actionModel(action).toJSON();
	}

	/**
	 * @Description: 专家更新
	 *
	 * @author mayuan
	 * @createDate 2016年6月7日
	 * @modifyDate 2016年6月7日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/expert/update/page/{uuid}")
	public ModelAndView activityExpertUpdate(Model model,
			@PathVariable(value = "uuid") String activityExpertUuid) {
		ActivityExpert activityExpert = activityFacade
				.getActivityExpertByUuid(activityExpertUuid);
		if (activityExpert == null) {
			model.addAttribute("error", "该记录有误，请刷新重试");
			return index("/wrapped/activity/expert_mgr.jsp");
		}
		model.addAttribute("activityExpert", activityExpert);
		MediaCentral media = mediaCentralFacade.getActivityExpertMedia(
				activityExpert, MediaCentral.SIGN_WX_BACKGROUND);
		if (null != media) {
			model.addAttribute("webChatMedia", media);
		}
		Speaker speaker = activityExpert.getSpeaker();
		if (null != speaker) {
			ChosenItem item = DynamicSearchVo.speakerItem(speaker);
			model.addAttribute("chosenItem", item);
		}
		Activity activity = activityExpert.getActivity();
		if (null != activity) {
			model.addAttribute("activity", activity);
		}
		model.addAttribute("token",
				makeRepeatSubmitToken(ACTIVITY_EXPERT_SUBMIT_KEY));
		return index("/wrapped/activity/expert_mgr.jsp");
	}

	/**
	 * @Description: 活动专家置顶
	 *
	 * @author mayuan
	 * @createDate 2016年6月12日
	 * @modifyDate 2016年6月12日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/activity/expert/recommend", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityExpertRecommend(
			@RequestParam String uuid, HttpServletRequest request) {
		ActivityExpert activityExpert = activityFacade
				.getActivityExpertByUuid(uuid);
		if (null == activityExpert) {
			return failureModel("该记录有误，请刷新页面重新尝试").toJSON();
		}
		ActionMessage action = activityFacade.recommendExpert(activityExpert);
		if (action.isSuccess()) {
			BaseController.bulidRequest("后台置顶活动专家", "activity_expert", null,
					Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		}
		return actionModel(action).toJSON();
	}

	/**
	 * @Description: 查询活动成员（发送模版消息）
	 *
	 * @author mayuan
	 * @createDate 2016年6月14日
	 * @modifyDate 2016年6月14日
	 */
	/*@RequestAuthority(value = Role.PRO_EDITOR, requiredProject = false, logger = false)
	@RequestMapping(value = "/activity/userlist/forMessage/{uuid}", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String resourceData(@PathVariable String uuid) {
		Activity activity = activityFacade.getByUuid(uuid);
		if (null == activity || !activity.isActive()) {
			return failureModel("该记录有误，请刷新页面重新尝试").toJSON();
		}
		List<ActivityUser> users = activityFacade
				.searchActivitieUserForMessage(activity);
		List<UserItem> result = new ArrayList<UserItem>();
		for (ActivityUser user : users) {
			UserItem item = new UserItem(user.getUser());
			item.build();
			result.add(item);
		}
		return BaseAPIModel.makeWrappedSuccessDataJson(result);
	}*/

	/**
	 * @Description: 给指定用户发送活动相关信息变更模版消息
	 *
	 * @author mayuan
	 * @createDate 2016年6月14日
	 * @modifyDate 2016年6月14日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/messageForChange", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String resAddLabel(HttpServletRequest request,
			@RequestParam(required = false) String checkedUuids,
			@RequestParam String activityUuid, @RequestParam String subject,
			@RequestParam String content) {
		/*
		 * if(StringUtils.isEmpty(checkedUuids)) return
		 * failureModel("请选择需要发送消息的用户").toJSON();
		 */
		if (StringUtils.isEmpty(subject) || StringUtils.isEmpty(content))
			return failureModel("请输入完整的消息详情").toJSON();
		Activity activity = activityFacade.getByUuid(activityUuid);
		if (null == activity || !activity.isActive()) {
			return failureModel("该记录有误，请刷新页面重新尝试").toJSON();
		}

		// 记录管理员发送
		User user = getCurrentUser(request);
		StringBuilder messageBody = new StringBuilder();
		messageBody.append("标题: ").append(subject).append("－－").append("内容: ")
				.append(content);
		Notification notification = notificationFacade
				.addActivityResourceChangedNotification(activity, user,
						messageBody.toString());
		if (null == notification) {
			return failureModel("操作失败").toJSON();
		}

		TempleKeyWord templeKeyWord= new TempleKeyWord();
		templeKeyWord.setFirst(subject);
		templeKeyWord.setKeyword2(content);

		try {
			//分批读取数据
			List<ActivityUser> users_ = activityFacade
					.searchActivitieUserForMessage(0, activity);
			int lastUserId;	
			while(users_.size() > 0){
				// 分批发送
				Status status = sendMessageUserBuffer(activity, notification,
						templeKeyWord, users_);
				// 查询新数据
				lastUserId = users_.get(users_.size()-1).getId();	
				users_ = activityFacade
						.searchActivitieUserForMessage(lastUserId, activity);
				logger.info("------");
				logger.info("新增用户数：" + users_.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return failureModel("操作失败").toJSON();
		}
		return successModel().toJSON();

		/*
		 * String[] userUuids = checkedUuids.split(","); for(String userUuid :
		 * userUuids){ //发送消息 User toUser = userFacade.getUserByUuid(userUuid);
		 * Status status = Status.FAILURE; if(null != toUser){ status =
		 * wxModelMessageFacade.onActivityResourceChanged(activity, toUser,
		 * message); } //模版消息发送成功，记录发送
		 * if(Status.SUCCESS.name().equals(status.name())){ Notification
		 * notification_ = notificationFacade.getByUuid(notification.getUuid());
		 * if(null == notification_){ continue ; }else{
		 * messageFacade.addNotificationMessage(notification_, toUser); } } }
		 */
	}

	/**
	 * @author mayuan
	 * @createDate 2016年6月17日
	 * @modifyDate 2016年6月17日
	 */
	private Status sendMessageUserBuffer(Activity activity,
			Notification notification, TempleKeyWord templeKeyWord,
			List<ActivityUser> users) {
		try {
			int userSize = users.size();
			int buffer = 10; // 每次最多发送给buffer个用户
			int sendBufferCount = 0;
			logger.info("本次需要发送的用户个数：" + userSize);
			logger.info("本次需要发送的buffer个数："
					+ Math.ceil((double) userSize / (double) buffer));
			for (int i = 0; i < Math.ceil((double) userSize / (double) buffer); i++) {
				int subListStart = buffer * i;
				int subListEnd = buffer * i + buffer;
				try {
					List<ActivityUser> users_a = users.subList(subListStart,
							subListEnd);
					sendMessageForBuffer(activity, notification, templeKeyWord,
							users_a);
				} catch (IndexOutOfBoundsException e) {
					// e.printStackTrace();
					// 如果抛异常说明指针越界,这时候终止角标要取真实的终止角标
					List<ActivityUser> users_b = users.subList(subListStart,
							userSize);
					sendMessageForBuffer(activity, notification, templeKeyWord,
							users_b);
				}
				sendBufferCount++;
			}
			logger.info("已发送的buffer个数:" + sendBufferCount);

			/* Test 模拟新增了用户 start */
			/*Thread currentTherad = Thread.currentThread();
			logger.info("当前线程名:" + currentTherad.getName());
			currentTherad.sleep(10*1000);*/
			/* Test 模拟新增了用户 end */

		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException();
		}
		return Status.SUCCESS;
	}

	private void sendMessageForBuffer(Activity activity,
			Notification notification, TempleKeyWord templeKeyWord,
			List<ActivityUser> users) {
		for (ActivityUser activityUser : users) {
			// 发送消息
			Status status = Status.FAILURE;
			if (null != activityUser.getUser()) {
				status = getModelMessageFacade().onActivityResourceChanged(activity,activityUser.getUser(), templeKeyWord);
			}
			// 模版消息发送成功，记录发送
			if (Status.SUCCESS.name().equals(status.name())) {
				Notification notification_ = notificationFacade
						.getByUuid(notification.getUuid());
				if (null == notification_) {
					continue;
				} else {
					messageFacade.addNotificationMessage(notification_,
							activityUser.getUser());
				}
			}
		}
		logger.info("单次发送的用户个数:" + users.size());
	}

	/**
	 * @Description: 指定活动管理员消息发送记录页面跳转
	 *
	 * @author mayuan
	 * @createDate 2016年6月15日
	 * @modifyDate 2016年6月15日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/activity/message/page/{uuid}")
	public ModelAndView activityMessagePage(HttpServletRequest request,
			@PathVariable String uuid) {
		Activity activity = activityFacade.getByUuid(uuid);
		if (null == activity || !activity.isActive()) {
			request.setAttribute("error", "该活动有误，请刷新页面重新尝试");
			return index(request, "/wrapped/activity/list.jsp");
		}
		request.setAttribute("activity", activity);
		return index(request, "/wrapped/activity/message_log.jsp");
	}

	/**
	 * @Description: 获取管理员消息发送记录
	 *
	 * @author mayuan
	 * @createDate 2016年6月15日
	 * @modifyDate 2016年6月15日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/messageList/data/{activityUuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityMessageDataJson(Model model,
			HttpServletRequest request,
			@PathVariable(value = "activityUuid") String activityUuid) {
		String queryKey = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageSize = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int startPage = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Notification> pagination = notificationFacade
				.searchActivitieMessageForDatatable(activityUuid, queryKey,
						startPage, pageSize);
		DataTableModel data = DataTableModel.makeInstance(
				pagination.getResults(), NotificationItem.class);
		data.setiTotalDisplayRecords(pagination.getTotal());
		data.setiTotalRecords(pagination.getPage_rows());
		return data.toJSON();
	}

	/**
	 * @Description: 发送记录详情页面跳转
	 *
	 * @author mayuan
	 * @createDate 2016年6月15日
	 * @modifyDate 2016年6月15日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping("/activity/messageDetail/page/{uuid}/{activityUuid}")
	public ModelAndView activityMessageDetailPage(HttpServletRequest request,
			@PathVariable String uuid, @PathVariable String activityUuid) {
		Notification notification = notificationFacade.getByUuid(uuid);
		if (null == notification || !notification.isActive()) {
			request.setAttribute("error", "该记录有误，请刷新页面重新尝试");
			return index(request, "/wrapped/activity/list.jsp");
		}
		Activity activity = activityFacade.getByUuid(activityUuid);
		if (null == activity || !activity.isActive()) {
			request.setAttribute("error", "该活动有误，请刷新页面重新尝试");
			return index(request, "/wrapped/activity/list.jsp");
		}
		request.setAttribute("activity", activity);
		request.setAttribute("notification", notification);
		return index(request, "/wrapped/activity/message_log_detail.jsp");
	}

	/**
	 * @Description: 获取指定消息记录的详情（发送到的目标用户列表）
	 *
	 * @author mayuan
	 * @createDate 2016年6月15日
	 * @modifyDate 2016年6月15日
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/activity/messageListDetail/data/{notificationUuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityMessageDataDetailJson(Model model,
			HttpServletRequest request,
			@PathVariable(value = "notificationUuid") String notificationUuid) {
		String queryKey = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageSize = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int startPage = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Message> pagination = messageFacade
				.searchActivitieMessageDetailForDatatable(notificationUuid,
						queryKey, startPage, pageSize);
		DataTableModel data = DataTableModel.makeInstance(
				pagination.getResults(), MessageItem.class);
		data.setiTotalDisplayRecords(pagination.getTotal());
		data.setiTotalRecords(pagination.getPage_rows());
		return data.toJSON();
	}

}
