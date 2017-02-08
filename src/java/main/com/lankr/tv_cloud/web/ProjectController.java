package com.lankr.tv_cloud.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.TvLayout;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.ChosenItem;
import com.lankr.tv_cloud.vo.DynamicSearchVo;
import com.lankr.tv_cloud.vo.ProjectListInfo;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.SimpleData;
import com.lankr.tv_cloud.vo.api.SimpleDataType;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
public class ProjectController extends AdminWebController {

	public static String project_create_token_key = "project_validate_token";

	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, requiredProject = false, logger = false)
	@RequestMapping(value = "/admin/project/new", method = RequestMethod.GET)
	public ModelAndView page_create_project(HttpServletRequest request) {
		String token = Tools.getUUID();
		request.getSession().setAttribute(project_create_token_key, token);
		request.setAttribute("token", token);
		return index(request, "/wrapped/project_create.jsp");
	}

	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, requiredProject = false, logger = false)
	@RequestMapping(value = "/admin/project/list", method = RequestMethod.GET)
	public ModelAndView page_list_project(HttpServletRequest request) {
		return index(request, "/wrapped/project_list.jsp");
	}

	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, requiredProject = false, logger = false)
	@RequestMapping(value = "/admin/project/save", method = RequestMethod.POST)
	public ModelAndView do_save_project(HttpServletRequest request,
			@RequestParam String project_name, @RequestParam String username,
			@RequestParam(required = false) String apply,
			@RequestParam(required = false) String project_desc,
			@RequestParam String token) {
		// get user by username
		Status status = Status.SUCCESS;
		if (!token.equals(toastRepeatSubmitToken(request,
				project_create_token_key))) {
			status = Status.SUBMIT_REPEAT;
		} else {
			User pro_user = userFacade.getUserByUsername(username);
			if (pro_user == null) {
				status = Status.USER_NOT_FOUND;
			} else {
				Role role = getRole(Role.PRO_ADMIN);
				if (role == null) {
					status = Status.FAILURE;
				} else {
					Project project = new Project();
					project.setApply(apply);
					project.setProjectName(project_name);
					project.setUuid(Tools.getUUID());
					project.setStatus(BaseModel.UNAPPROVED);
					project.setMark(project_desc);
					project.setIsActive(BaseModel.ACTIVE);
					project.setPinyin(Tools.getPinYin(project_name));
					status = projectFacade.addProject(project);
					if (Status.SUCCESS == status) {
						UserReference ur = new UserReference();
						ur.setProject(project);
						ur.setRole(role);
						ur.setUser(pro_user);
						ur.setIsActive(1);
						status = userFacade.addUserReference(ur);
					}
				}
			}
		}
		request.setAttribute("message", status.message());
		return page_create_project(request);
	}

	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, requiredProject = false, logger = false)
	@RequestMapping(value = "/admin/project/datatable", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String projectList(HttpServletRequest request) {
		ProjectListInfo info = new ProjectListInfo();
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Project> pagination = projectFacade.searchProjects(q, from,
				size);
		info.setiTotalDisplayRecords(pagination.getTotal());
		info.setiTotalRecords(pagination.getPage_rows());
		List<Project> projects = pagination.getResults();
		info.addItem(projects);
		return gson.toJson(info);
	}

	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, requiredProject = false, logger = false)
	@RequestMapping(value = "/admin/user/fetch", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String fetchUserInfo(@RequestParam String query,
			@RequestParam(required = false) Integer page) {
		page = (page == null ? 0 : page);
		return gson.toJson(userFacade.fetchUserBaseInfo(nullValueFilter(query),
				0));
	}

	@RequestAuthority(logger = false)
	@RequestMapping(value = "/tv/layout/{category_uuid}/list")
	public ModelAndView layoutList(HttpServletRequest request,
			@PathVariable String category_uuid, Model model) {

		Category category = assetFacade.getCategoryByUuid(category_uuid);
		model.addAttribute("category", category);
		List<TvLayout> layouts = projectFacade
				.selectTvLayoutsByCategory(category);
		model.addAttribute("layouts", layouts);
		return index(request, "/wrapped/layout/list.jsp");
	}

	@RequestAuthority(logger = true)
	@RequestMapping(value = "/tv/layout/{category_uuid}/create")
	public ModelAndView layoutNew(HttpServletRequest request,
			@PathVariable String category_uuid, Model model) {
		Category category = assetFacade.getCategoryByUuid(category_uuid);
		model.addAttribute("category", category);
		return index(request, "/wrapped/layout/new.jsp");
	}

	@RequestAuthority(logger = true)
	@RequestMapping(value = "/tv/layout/{category_uuid}/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String layoutSave(HttpServletRequest request,
			@PathVariable String category_uuid, @RequestParam String name,
			@RequestParam String widgets,
			@RequestParam(required = false) String uuid,
			@RequestParam(required = false) String mark) {
		Category category = assetFacade.getCategoryByUuid(category_uuid);
		if (category == null) {
			return BaseController.failureModel("关联的分类不可用").toJSON();
		}
		TvLayout layout = null;
		boolean edit = false;
		if (!Tools.isBlank(uuid)) {
			layout = projectFacade.getTvLayoutByUuid(uuid);
		}
		if (layout != null) {
			edit = true;
		} else {
			layout = new TvLayout();
			layout.setType(TvLayout.TYPE_INNER);
			layout.setProject(getCurrentUser(request).getStubProject());
		}
		layout.setCategory(category);
		layout.setWidgets(widgets);
		layout.setName(name);
		layout.setMark(mark);
		layout.setUser(getCurrentUser(request));
		Status s = Status.FAILURE;
		if (edit) {
			s = projectFacade.updateSubTvLayout(layout);
		} else {
			layout.setUuid(Tools.getUUID());
			layout.setStatus(TvLayout.UNDERLINE);
			layout.setIsActive(TvLayout.ACTIVE);
			s = projectFacade.addSubTvLayout(layout);
		}

		return BaseController.getStatusJson(s);
	}

	@RequestAuthority(logger = true)
	@RequestMapping(value = "/tv/layout/{uuid}/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String layoutStatus(@RequestParam int status,
			@PathVariable String uuid) {
		Status s = projectFacade.changeLayoutStatus(uuid,
				status == BaseModel.APPROVED ? BaseModel.UNDERLINE
						: BaseModel.APPROVED);
		if (s == Status.SUCCESS) {
			TvLayout layout = projectFacade.getTvLayoutByUuid(uuid);
			if (layout != null) {
				return BaseAPIModel.makeSimpleSuccessInnerDataJson(
						new SimpleData("uuid", HtmlUtils.htmlEscape(layout
								.getUuid())),
						new SimpleData("name", HtmlUtils.htmlEscape(layout
								.getName())),
						new SimpleData("date", Tools.formatYMDHMSDate(layout
								.getModifyDate())),
						new SimpleData("createUser", OptionalUtils.traceValue(
								layout, "user.username")),
						new SimpleData("mark", HtmlUtils.htmlEscape(layout
								.getMark())), new SimpleData("layout_status",
								layout.getStatus(), SimpleDataType.Number));
			}
		}
		return BaseController.getStatusJson(s);
	}

	@RequestAuthority
	@RequestMapping(value = "/tv/layout/{uuid}/del", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String layoutStatus(@PathVariable String uuid) {
		return BaseController.getStatusJson(projectFacade.delLayout(uuid));
	}

	@RequestAuthority
	@RequestMapping(value = "/tv/layout/{uuid}/edit")
	public ModelAndView layoutStatus(@PathVariable String uuid, Model model,
			HttpServletRequest request) {
		TvLayout layout = projectFacade.getTvLayoutByUuid(uuid);
		if (layout != null) {
			model.addAttribute("category", layout.getCategory());
			model.addAttribute("layout", layout);
		}
		return index(request, "/wrapped/layout/new.jsp");
	}

	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/project/tv/home/settings/v2")
	public ModelAndView projectTvSettings(Model model,
			HttpServletRequest request) {
		model.addAttribute("layouts", projectFacade
				.selectProjectTvHomeLayouts(getCurrentUser(request)
						.getStubProject()));
		return index("/wrapped/layout/home/tv_settings.jsp");
	}

	// 超级管理员获取项目首页标签
	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, requiredProject = false)
	@RequestMapping(value = "/project/tv/home/labels", produces = "text/json; charset=utf-8")
	public @ResponseBody String projectTvHomeLabels(@RequestParam String uuid) {
		Project project = projectFacade.getProjectByUuid(uuid);
		if (project == null)
			return failureModel("项目不可用").toJSON();
		List<TvLayout> homeLayouts = projectFacade
				.selectProjectTvHomeLayouts(project);
		List<AdminLayoutItemData> data = null;
		if (homeLayouts != null) {
			data = new ArrayList<>();
			for (TvLayout tvLayout : homeLayouts) {
				data.add(new AdminLayoutItemData().build(tvLayout));
			}
		}
		return BaseAPIModel.makeWrappedSuccessDataJson(data);
	}

	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, requiredProject = false)
	@RequestMapping(value = "/project/tv/home/label/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String saveHomeLable(String uuid, String name) {
		Project project = projectFacade.getProjectByUuid(uuid);
		if (project == null)
			return failureModel("项目不可用").toJSON();
		ActionMessage message = projectFacade.saveProjectHomeLabel(project,
				name, TvLayout.TYPE_HOME, null);
		return actionModel(message).toJSON();
	}

	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, requiredProject = false)
	@RequestMapping(value = "/project/tv/home/label/del_super", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String delHomeLableSuperAdmin(@RequestParam String uuid) {
		TvLayout tl = projectFacade.getTvLayoutByUuid(uuid);
		if (tl == null || !tl.isHomeLayout()) {
			return failureModel("无效的板块").toJSON();
		}
		return actionModel(projectFacade.disableHomeLabel(tl)).toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, requiredProject = true)
	@RequestMapping(value = "/project/tv/home/label/del_editor", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String delHomeLableEditor(@RequestParam String uuid) {
		TvLayout tl = projectFacade.getTvLayoutByUuid(uuid);
		if (tl == null) {
			return failureModel("版块不存在").toJSON();
		} else if (tl.isHomeLayout()) {
			return failureModel("无权执行删除操作").toJSON();
		}
		return actionModel(projectFacade.disableHomeLabel(tl)).toJSON();
	}

	private class AdminLayoutItemData {
		private String uuid;
		private String name;
		private int position;
		private int status;
		private String createDate;

		AdminLayoutItemData build(TvLayout layout) {
			uuid = layout.getUuid();
			name = layout.getName();
			status = layout.getStatus();
			createDate = Tools.formatYMDHMSDate(layout.getCreateDate());
			return this;
		}
	}

	// 获取widget关联的对象
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/project/tv/widget/setting/reference", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String widgetRefData(@RequestParam String refType,
			@RequestParam(required = false) String q) {
		if (TvLayout.TYPE_RESOURCE.equals(refType)) {
			List<Resource> res = resourceFacade.searchResourceListByQ(q);
			return DynamicSearchVo.buildResources(res, q).toJSON();
		} else if (TvLayout.TYPE_ACTIVITY.equals(refType)) {
			List<Activity> activities = activityFacade
					.queryApiUseableActivities(q);
			return DynamicSearchVo.buildActivities(activities, q).toJSON();
		} else if (TvLayout.TYPE_BROADCAST.equals(refType)) {
			List<Broadcast> casts = broadcastFacade
					.searchApiUseableBroadcast(q);
			return DynamicSearchVo.buildBroadcasts(casts, q).toJSON();
		}
		return successModel().toJSON();
	}

	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/project/tv/widget/reference/detail", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String referenceDetail(@RequestParam String uuid,
			@RequestParam String refType) {
		if (TvLayout.TYPE_RESOURCE.equals(refType)) {
			Resource res = resourceFacade.getResourceByUuid(uuid);
			ChosenItem item = DynamicSearchVo.resourceItem(res);
			return BaseAPIModel.makeWrappedSuccessDataJson(item);
		} else if (TvLayout.TYPE_ACTIVITY.equals(refType)) {
			Activity activity = activityFacade.getByUuid(uuid);
			ChosenItem item = DynamicSearchVo.activityItem(activity);
			return BaseAPIModel.makeWrappedSuccessDataJson(item);

		} else if (TvLayout.TYPE_BROADCAST.equals(refType)) {
			Broadcast cast = broadcastFacade.getCastByUuid(uuid);
			ChosenItem item = DynamicSearchVo.broadcastItem(cast);
			return BaseAPIModel.makeWrappedSuccessDataJson(item);
		}
		return failureModel("未找到关联目标").toJSON();
	}

	/**
	 * @Description: 保存tv布局
	 *
	 * @author mayuan
	 * @createDate 2016年5月11日
	 * @modifyDate 2016年5月11日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/project/tv/layout/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String saveLayHomeLayout(@RequestParam String uuid,
			@RequestParam String dataJson, HttpServletRequest request) {
		TvLayout layout = projectFacade.getTvLayoutByUuid(uuid);
		/*
		 * if (!layout.isHomeLayout()) { return
		 * failureModel("请设置首页的布局").toJSON(); }
		 */
		layout.setWidgets(dataJson);
		/*
		 * if (!layout.isHomeWidgetsValid()) { return
		 * failureModel("提交的布局数据有误").toJSON(); }
		 */
		ActionMessage action = projectFacade.updateTvHomeLayoutWidgets(layout);
		return actionModel(action).toJSON();
	}

	/**
	 * @Description: 板块置顶
	 *
	 * @author mayuan
	 * @createDate 2016年5月10日
	 * @modifyDate 2016年5月10日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_ADMIN, requiredProject = false)
	@RequestMapping(value = "/project/tv/home/label/toTop", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String homeLayoutDown(@RequestParam String uuid,
			@RequestParam(required = false) String projectUuid,
			@RequestParam(required = false) String categoryUuid) {
		TvLayout tl = projectFacade.getTvLayoutByUuid(uuid);
		if (tl == null) {
			// if (tl == null || !tl.isHomeLayout()) {
			return failureModel("无效的板块").toJSON();
		}
		if (StringUtils.isNoneEmpty(projectUuid)
				&& StringUtils.isNoneEmpty(categoryUuid)) {
			Project project = projectFacade.getProjectByUuid(projectUuid);
			Category category = assetFacade.getCategoryByUuid(categoryUuid);
			tl.setCategory(category);
			tl.setProject(project);
			return actionModel(
					projectFacade.homeLayoutToTop(tl, TvLayout.TYPE_INNER_V2))
					.toJSON();
		}
		return actionModel(
				projectFacade.homeLayoutToTop(tl, TvLayout.TYPE_HOME)).toJSON();
	}

	/**
	 * @Description: 根据分类和布局类型查询选中'Widget'下的所有'Layout'
	 * 
	 * @author mayuan
	 * @createDate 2016年5月11日
	 * @modifyDate 2016年5月11日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_ADMIN, requiredProject = false)
	@RequestMapping(value = "/project/tv/widget/labels", produces = "text/json; charset=utf-8")
	public @ResponseBody String widgetTvHomeLabels(
			@RequestParam String categoryUuid) {
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null || !category.isActive())
			return failureModel("分类不可用").toJSON();
		List<TvLayout> homeLayouts = projectFacade
				.selectWidgetTvHomeLayouts(category);
		List<AdminLayoutItemData> data = null;
		if (homeLayouts != null) {
			data = new ArrayList<>();
			for (TvLayout tvLayout : homeLayouts) {
				data.add(new AdminLayoutItemData().build(tvLayout));
			}
		}
		return BaseAPIModel.makeWrappedSuccessDataJson(data);
	}

	/**
	 * @Description: 'widget' 保存布局
	 *
	 * @author mayuan
	 * @createDate 2016年5月11日
	 * @modifyDate 2016年5月11日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_ADMIN, requiredProject = false)
	@RequestMapping(value = "/project/tv/widget/label/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String saveWidgetLable(
			@RequestParam String categoryUuid,
			@RequestParam String projectUuid, @RequestParam String name) {
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null || !category.isActive())
			return failureModel("分类不可用").toJSON();
		Project project = projectFacade.getProjectByUuid(projectUuid);
		if (project == null)
			return failureModel("项目不可用").toJSON();
		ActionMessage message = projectFacade.saveProjectHomeLabel(project,
				name, TvLayout.TYPE_INNER_V2, category);
		return actionModel(message).toJSON();
	}

	/**
	 * @Description: 设置选中'widget'的'Layout'
	 *
	 * @author mayuan
	 * @createDate 2016年5月11日
	 * @modifyDate 2016年5月11日
	 * @version V 0.1
	 */
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/project/tv/home/settings/v2Sub")
	public ModelAndView widgetSubSettings(Model model,
			HttpServletRequest request, @RequestParam String categoryUuid,
			@RequestParam String projectUuid) {
		Project project = projectFacade.getProjectByUuid(projectUuid);
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (project == null) {
			project = globalDefaultProject();
		}
		if (null != project && null != category) {
			model.addAttribute(
					"layouts",
					projectFacade.selectProjectTvWidgetLayouts(
							getCurrentUser(request).getStubProject(),
							category.getId()));
			model.addAttribute("category", category);
			model.addAttribute("projectUuid", projectUuid);
			return index("/wrapped/layout/home/tv_settings.jsp");
		}
		// model.addAttribute("error", "服务繁忙，请稍后重试");
		model.addAttribute("error", "分类不可用或已经被删除");
		return index("/wrapped/layout/home/tv_settings.jsp");
	}

}
