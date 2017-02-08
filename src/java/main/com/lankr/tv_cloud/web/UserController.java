package com.lankr.tv_cloud.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.google.gson.Gson;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.cache.EphemeralData;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.facade.po.AwardResult;
import com.lankr.tv_cloud.model.ActivationCode;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.Md5;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.BooleanResultVo;
import com.lankr.tv_cloud.vo.CertificationUserDataItem;
import com.lankr.tv_cloud.vo.OptionAdditionList;
import com.lankr.tv_cloud.vo.SpeakerSufaceVo;
import com.lankr.tv_cloud.vo.SpeakerVo;
import com.lankr.tv_cloud.vo.UserCardSurface;
import com.lankr.tv_cloud.vo.UserClientData;
import com.lankr.tv_cloud.vo.UserClientItem;
import com.lankr.tv_cloud.vo.UserDetailResultItem;
import com.lankr.tv_cloud.vo.UserProjectClientData;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.BaseAPIModel.CodeConstant;
import com.lankr.tv_cloud.vo.datatable.ActivityData;
import com.lankr.tv_cloud.vo.datatable.ActivityDataItem;
import com.lankr.tv_cloud.vo.datatable.CertificationData;
import com.lankr.tv_cloud.vo.datatable.CertificationDataItem;
import com.lankr.tv_cloud.web.api.BaseAPIController;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@SuppressWarnings("all")
@Controller
public class UserController extends AdminWebController {

	public static final String USER_SAVE_TOKEN_KEY = "user_save_token_key";

	private static final String USER_LOGIN_FAILURE_TIMES_KEY = "USER_LOGIN_FAILURE_TIMES_KEY";

	private static final int USER_LOGIN_FAILURE_TIMES = 2;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView frontLogin(HttpServletRequest request) {
		// 模拟用户
		//return new ModelAndView("front/subscribe");
		return new ModelAndView("redirect:/f/web/index");
	}

	// 后台登录
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) {
		// 模拟用户
		request.setAttribute("need_validcode", needValidCode(request));
		return new ModelAndView("login");
	}

	@RequestAuthority(value = Role.NULL)
	@RequestMapping(value = "/admin/login", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String doLogin(@RequestParam String username,
			@RequestParam String password,
			@RequestParam(required = false) String validateCode,
			HttpServletRequest request, HttpServletResponse response) {
		if (needValidCode(request)) {
			if (!validCode(validateCode, ValidationCodeMaker.USER_LOGIN)) {
				BaseAPIModel ret = failureModel("请输入正确的验证码");
				ret.setCode(CodeConstant.VALIDATE_CODE_NEEDED);
				return ret.toJSON();
			}
		}
		User user = userFacade.login(username, Md5.getMd5String(password));
		HttpSession session = request.getSession();

		if (user == null) {
			BaseController.logForMark(request, username + " 登录失败");
			BaseController.bulidRequest("后台用户登录", "user", null,
					Status.FAILURE.message(), null, "失败，username=" + username
							+ ",password=" + password + " 用户名或密码错误", request);
			int failure = markForLoginFailure(request);
			BaseAPIModel ret = failureModel("用户名或密码错误");
			if (needValidCode(request)) {
				ret.setCode(CodeConstant.VALIDATE_CODE_NEEDED);
			}
			return ret.toJSON();

		} else {
			setCurrentSessionUser(user, request);
			BaseController.bulidRequest("后台用户登录", "user", user.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		}
	}

	private int markForLoginFailure(HttpServletRequest request) {
		int count = 0;
		try {
			HttpSession session = request.getSession();
			Object value = session.getAttribute(USER_LOGIN_FAILURE_TIMES_KEY);
			if (value != null) {
				count = (Integer) value;
			}
			session.setAttribute(USER_LOGIN_FAILURE_TIMES_KEY, ++count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	private boolean needValidCode(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			int count = 0;
			Object value = session.getAttribute(USER_LOGIN_FAILURE_TIMES_KEY);
			if (value != null) {
				count = (Integer) value;
			}
			return count >= USER_LOGIN_FAILURE_TIMES;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@RequestAuthority(value = Role.PRO_USER_LEVEL, requiredProject = false, logger = false)
	@RequestMapping(value = "/user/project/multipart")
	public ModelAndView projectHandler(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		User user = getCurrentUser(request);
		// 判断用户是否是多个项目的后台用户
		Role mainRole = user.getMainRole();
		// 判断用户的权限
		if (mainRole.isSuperAdmin()) {
			// 直接进入超级管理员页面
			return index(request, "/wrapped/project_list.jsp");
		}
		List<UserReference> refs = user.getUser_reference();
		if (refs == null) {
			model.addAttribute("status", getStatusJson(Status.NO_PERMISSION));
			removeCurrentUser(request);
			return login(request);
		}
		for (int i = 0; i < refs.size(); i++) {
			Role role = refs.get(i).getRole();
			if (!role.isProUser()) {
				refs.remove(i);
			}
		}
		List<UserReference> refs_useable = new ArrayList<UserReference>();
		for (UserReference userReference : refs) {
			Role role = userReference.getRole();
			if (role.isProUser()) {
				refs_useable.add(userReference);
			}
		}
		if (refs_useable.isEmpty()) {
			model.addAttribute("status", getStatusJson(Status.NO_PERMISSION));
			removeCurrentUser(request);
			return login(request);
		}
		setCurrentSessionUser(user, request);
		// 如果只绑定一个项目，则直接跳转到项目管理后台
		if (refs_useable.size() == 1) {
			user.setHandlerReference(refs_useable.get(0));
			// return index(request);
			try {
				if (authGrantedRedirected(request, response, user)) {
					return null;
				}
				if (user.getHandlerReference().getRole().isProAdmin()) {
					response.sendRedirect("/tv/home/settings");
				} else {
					response.sendRedirect("/asset/video/mgr");
				}
				return null;
			} catch (IOException e) {

				e.printStackTrace();
				return index(request);
			}
		} else {
			// 如果绑定多个项目，则调转到项目选择页面
			model.addAttribute("references", refs_useable);
		}
		return new ModelAndView("common/project_handler");
	}

	// @RequestAuthority(value = Role.PRO_USER_LEVEL)
	// @RequestAuthority(value = Role.NULL)
	@RequestMapping(value = "/admin/user/logout", method = RequestMethod.GET)
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			User user = getCurrentUser(request);
			removeCurrentUser(request);
			BaseController.bulidRequest("后台用户退出登录", "user", user == null ? 0
					: user.getId(), Status.SUCCESS.message(), null, "成功",
					request);
			response.sendRedirect("/admin");
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestAuthority(value = Role.PRO_USER_LEVEL, requiredProject = false, logger = false)
	@RequestMapping(value = "/user/hold/project", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String holdProject(@RequestParam String uuid,
			HttpServletRequest request, HttpServletResponse response) {
		User user = getCurrentUser(request);
		Project project = projectFacade.getProjectByUuid(uuid);
		if (project == null) {
			return getStatusJson(Status.NOT_FOUND);
		}
		if (user.getMainRole().isSuperAdmin()) {
			UserReference ur = new UserReference();
			ur.setProject(project);
			ur.setRole(getRole(Role.PRO_ADMIN));
			user.setHandlerReference(ur);
			return getStatusJson(Status.SUCCESS);
		}

		List<UserReference> refs = user.getUser_reference();
		if (refs == null || refs.isEmpty()) {
			return getStatusJson(Status.NO_PERMISSION);
		}
		for (UserReference userReference : refs) {
			if (project.isSame(userReference.getProject())) {
				Role role = userReference.getRole();
				if (role.isProUser()) {
					user.setHandlerReference(userReference);
					return getStatusJson(Status.SUCCESS);
				}
				break;
			}
		}
		return getStatusJson(Status.NO_PERMISSION);
	}

	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, requiredProject = false, logger = false)
	@RequestMapping("/admin/user/new")
	public ModelAndView newUser(HttpServletRequest request) {
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.getSession().setAttribute(USER_SAVE_TOKEN_KEY, token);
		return index(request, "/wrapped/user_create.jsp");
	}

	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, requiredProject = false, logger = false)
	@RequestMapping(value = "/admin/user/save", method = RequestMethod.POST)
	public ModelAndView saveGlobleUser(HttpServletRequest request,
			@RequestParam String token, @RequestParam String username,
			@RequestParam String password,
			@RequestParam(required = false) String nickname,
			@RequestParam(required = false) String mark, Model model) {
		String server_token = toastRepeatSubmitToken(request,
				USER_SAVE_TOKEN_KEY);
		Status status = Status.SUCCESS;
		if (!token.equals(server_token)) {
			status = Status.SUBMIT_REPEAT;
		} else {
			String uuid = Tools.getUUID();
			User user = new User();
			user.setUsername(username);
			user.setPassword(Md5.getMd5String(password));
			user.setNickname(nickname);
			user.setMark(mark);
			user.setUuid(Tools.getUUID());
			user.setMainRole(getRole(Role.PRO_USER_LEVEL));
			user.setPinyin(Tools.getPinYin(nickname));
			user.setIsActive(BaseModel.ACTIVE);
			status = userFacade.addUser(user);
		}
		model.addAttribute("status", status.message());
		return newUser(request);
	}

	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, requiredProject = false, logger = false)
	@RequestMapping(value = "/admin/user/list", method = RequestMethod.GET)
	public ModelAndView gloabUserListPage(HttpServletRequest request) {
		return index(request, "/wrapped/user_list.jsp");
	}

	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, requiredProject = false, redirect = false, logger = false)
	@RequestMapping(value = "/admin/user/datatable", method = RequestMethod.GET)
	public @ResponseBody String adminUserDatatable(HttpServletRequest request) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<User> users = userFacade.searchUsers(q, from, size);
		UserClientData uc = new UserClientData();
		uc.setiTotalDisplayRecords(users.getTotal());
		uc.setiTotalRecords(users.getPage_rows());
		uc.buildData(users.getResults());
		return gson.toJson(uc);
	}

	/**
	 * 加载项目下用户页面
	 */

	@RequestAuthority(value = Role.PRO_ADMIN, requiredProject = true, logger = false)
	@RequestMapping(value = "/project/userReference/list", method = RequestMethod.GET)
	public ModelAndView loadUserReferenceListPage(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (Role.SUPER_ADMIN_LEVEL != user.getMainRole().getId()) {
			request.setAttribute("isSuperAdmin", "N");
		} else {
			request.setAttribute("isSuperAdmin", "Y");
		}
		return index(request, "/wrapped/project_user_list.jsp");
	}

	/**
	 * 查找项目下的用户列表的table数据
	 */
	@RequestAuthority(value = Role.PRO_ADMIN, requiredProject = true, redirect = false)
	@RequestMapping(value = "/project/userReference/user/datatable", method = RequestMethod.GET)
	public @ResponseBody String ProUserReferenceDatatable(
			HttpServletRequest request) {
		User user = getCurrentUser(request);
		Project project = user.getStubProject();
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<User> users = userFacade.searchUsersByProject(q, from, size,
				project.getId());
		UserProjectClientData uc = new UserProjectClientData();
		uc.setiTotalDisplayRecords(users.getTotal());
		uc.setiTotalRecords(users.getPage_rows());
		uc.buildData(users.getResults(), project, webchatFacade,
				certificationFacade);
		// 记录日志
		BaseController.bulidRequest("后台用户查看用户列表", "user", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(uc);
	}

	/**
	 * 项目下创建用户页面跳转
	 */
	@RequestAuthority(value = Role.PRO_ADMIN, requiredProject = true, logger = false)
	@RequestMapping("/project/user/new")
	public ModelAndView projectNewUser(HttpServletRequest request) {
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.getSession().setAttribute(USER_SAVE_TOKEN_KEY, token);
		return index(request, "/wrapped/project_user_create.jsp");
	}

	/**
	 * 项目下创建用户保存
	 */
	@RequestAuthority(value = Role.PRO_ADMIN, requiredProject = true)
	@RequestMapping(value = "/project/new/user/save", method = RequestMethod.POST)
	public ModelAndView saveProjectUser(HttpServletRequest request,
			@RequestParam String token, @RequestParam String username,
			@RequestParam String password,
			@RequestParam(required = false) String nickname,
			@RequestParam(required = false) String roleName,
			@RequestParam(required = false) String mark, Model model) {
		String server_token = toastRepeatSubmitToken(request,
				USER_SAVE_TOKEN_KEY);
		Status status = Status.SUCCESS;
		String message = "";
		if (!token.equals(server_token)) {
			message = Status.SUBMIT_REPEAT.message();
		} else {
			Role role = getRoleByName(roleName);
			if (status == Status.SUCCESS) {
				String uuid = Tools.getUUID();
				User user = new User();
				user.setUsername(username);
				user.setPassword(Md5.getMd5String(password));
				user.setNickname(nickname);
				user.setMark(mark);
				user.setUuid(uuid);
				user.setMainRole(getRole(Role.PRO_USER_LEVEL));
				user.setPinyin(Tools.getPinYin(nickname));
				user.setIsActive(BaseModel.ACTIVE);
				status = userFacade.addUser(user);
				if (Status.SUCCESS == status) {
					Project project = getCurrentUser(request).getStubProject();
					UserReference ur = new UserReference();
					ur.setProject(project);
					ur.setRole(role);
					ur.setUser(user);
					ur.setUuid(Tools.getUUID());
					ur.setIsActive(1);
					status = userFacade.addUserReference(ur);
				}
				if (status == Status.SUCCESS) {
					// 记录日志
					BaseController.bulidRequest("后台新建用户", "user", user.getId(),
							Status.SUCCESS.message(), null, "成功", request);
					message = status.message();
				} else {
					message = "保存失败";
				}
			}
		}
		model.addAttribute("status", message);
		return projectNewUser(request);
	}

	/**
	 * 改变项目下用户状态
	 */
	@RequestAuthority(value = Role.PRO_ADMIN, requiredProject = true, logger = false)
	@RequestMapping(value = "/project/manage/isAction/user", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String changeUserStatus(HttpServletRequest request,
			@RequestParam String uuid) {
		User currentUser = getCurrentUser(request);
		Project project = currentUser.getStubProject();
		User user = userFacade.getUserByUuid(uuid);
		if (user == null) {
			return getStatusJson(Status.NOT_FOUND);
		}
		List<UserReference> urs = user.getUser_reference();
		UserReference ur = null;
		if (urs != null && !urs.isEmpty()) {
			for (UserReference userReference : urs) {
				if (userReference.getProject().isSame(project)) {
					ur = userReference;
					break;
				}
			}
		}
		if (ur == null)
			return getStatusJson(Status.NOT_FOUND);
		if (currentUser.getHandlerReference().getRole().getLevel() < ur
				.getRole().getLevel()) {
			Status status = userFacade.changeUserProjectRef(user, project);
			if (status == Status.SUCCESS) {
				UserReference referrnce = userFacade.searchUserReference(user,
						project);
				// 刷新缓存的user
				refreshUserItemCache(user);
				return getStatusJson((referrnce.getIsActive() ^ 1) + "");
			}
		}

		return getStatusJson(Status.FAILURE);
	}

	@RequestAuthority(value = Role.PRO_ADMIN, requiredProject = true, logger = false)
	@RequestMapping(value = "/project/user/getRole", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String changeUserStatus(HttpServletRequest request) {
		List<Role> sys_roles = userFacade.getRolesByUser();
		return gson.toJson(sys_roles);
	}

	@RequestMapping(value = "/user/load/password/page", method = RequestMethod.GET)
	public ModelAndView loadtPasswordPage(HttpServletRequest request) {
		return index(request, "/wrapped/update_password.jsp");
	}

	@RequestAuthority(value = Role.NULL)
	@RequestMapping(value = "/user/update/password", method = RequestMethod.POST)
	public @ResponseBody String updateUserPassWord(
			@RequestParam String oldPassword, @RequestParam String password,
			HttpServletRequest request) {
		User currentUser = getCurrentUser(request);
		if (oldPassword == null || oldPassword.isEmpty()) {
			return getStatusJson(Status.FAILURE);
		}
		if (password == null || password.isEmpty()) {
			return getStatusJson(Status.FAILURE);
		}
		User user = userFacade.getUserByUuid(currentUser.getUuid());
		if (!Md5.getMd5String(oldPassword).equals(user.getPassword())) {
			// return getStatusJson(Status.FAILURE);
			return getStatusJson("The original password error");
		}
		currentUser.setPassword(Md5.getMd5String(password));
		Status staus = userFacade.updateUser(currentUser);
		if (staus == Status.SUCCESS) {
			// 记录日志
			BaseController.bulidRequest("后台用户修改密码", "user", user.getId(),
					Status.SUCCESS.message(), null, "成功", request);
		}
		return getStatusJson(staus);
	}

	/**
	 * 查看旧密码是否正确
	 */
	@RequestMapping(value = "/user/validat/oldpassword", method = RequestMethod.GET)
	public @ResponseBody String comparePassword(
			@RequestParam String oldPassword, HttpServletRequest request) {
		User currentUser = getCurrentUser(request);
		if (oldPassword == null || oldPassword.isEmpty()) {
			return getStatusJson(Status.FAILURE);
		}
		if (Md5.getMd5String(oldPassword).equals(currentUser.getPassword())) {
			return getStatusJson(Status.SUCCESS);
		}
		return getStatusJson(Status.FAILURE);
	}

	/**
	 * 改变用户的状态
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.SUPER_ADMIN_LEVEL, redirect = false, requiredProject = false)
	@RequestMapping(value = "/admin/change/user/isaction", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String changeAdverStatus(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = userFacade.getUserByUuid(uuid);
		if (user == null)
			return getStatusJson(Status.NOT_FOUND);
		// 上下线转换
		if (user.getMainRole().getLevel() == Role.SUPER_ADMIN_LEVEL) {
			return getStatusJson("Super administrator can disable");
		}
		user.setIsActive(user.getIsActive() == BaseModel.UNAPPROVED ? BaseModel.APPROVED
				: BaseModel.UNAPPROVED);
		Status staus = userFacade.updateUser(user);
		if (staus == Status.SUCCESS) {
			UserClientData item = new UserClientData();
			item.buildData(user);
			BaseController.bulidRequest("后台用户状态改变", "user", user.getId(),
					Status.SUCCESS.message(), null,
					"成功，装变为" + user.getIsActive(), request);
			refreshUserItemCache(user);
			return gson.toJson(item.getAaData().get(0));
		}
		return getStatusJson(Status.FAILURE);
	}

	// 用户充值记录
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/userCard/list/page/data", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String userCardListData(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = userFacade.getUserByUuid(uuid);
		if (null == user || !user.isActive())
			return getStatusJson(Status.USER_NOT_FOUND);
		int userId = user.getId();
		List<ActivationCode> activCodes = activationFacade
				.getActivationByUserId(userId);
		UserCardSurface suface = new UserCardSurface();
		suface.bulidData(activCodes);
		BaseController.bulidRequest("后台流量卡记录列表", "activation_code", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(suface);
	}

	// 已提交实名认证记录页面
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/certification/page")
	public ModelAndView certificationPage(Model model) {
		return index("/wrapped/certification/certification_list.jsp");
	}

	/**
	 * 实名认证列表数据
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/certification/datatable", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityDataJson(Model model,
			HttpServletRequest request,
			@RequestParam(value = "state", required = false) String state) {
		String query = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageSize = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int startPage = fromObj == null ? 0 : Integer.valueOf((String) fromObj);

		Pagination<Certification> results = certificationFacade
				.searchCertificationsForDatatable(query, startPage, pageSize,
						state);
		CertificationData data = new CertificationData();
		data.build(results.getResults());
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * 实名认证审核,该接口只处理‘拒绝’，‘撤销’操作
	 * 
	 * @param uuid
	 *            申请记录唯一标识
	 * @param status
	 *            需要设置的目标状态
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/certification/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityStatus(@RequestParam String uuid,
			@RequestParam int status,
			@RequestParam(required = false) String refuse) {
		Certification certification = certificationFacade
				.getCertificationByUuid(uuid);
		if (null == certification)
			return failureModel("该申请记录有误，请重新申请").toJSON();
		if (status == certification.getStatus()) {
			// 已经操作完毕，不需要重复操作
			return BaseAPIModel
					.makeWrappedSuccessDataJson(CertificationDataItem
							.build(certification));
		}
		certification.setStatus(status);
		if (refuse != null) {
			certification.setMark(refuse);
		} else {
			certification.setMark("");
		}
		Certification certification_updateResult = certificationFacade
				.updateCertificationStatus(certification);
		if (null != certification_updateResult) {
			/**
			 * 2016-3-16,添加发送模板消息
			 */
			getModelMessageFacade().realNameNotice(certification_updateResult,
					certification_updateResult.getUser());
			return BaseAPIModel
					.makeWrappedSuccessDataJson(CertificationDataItem
							.build(certification_updateResult));
		}
		return failureModel("审核出错").toJSON();
	}

	/**
	 * 讲者关联用户
	 * 
	 * @param checkedUserUuid
	 *            讲者要关联的目标用户唯一标识
	 * @param speakerUuid
	 *            欲操作讲者唯一标识
	 * @return Status
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/admin/speaker/associationUser/", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String speakerAssociationUser(
			@RequestParam String checkedUserUuid,
			@RequestParam String speakerUuid) {
		Speaker speaker = assetFacade.getSpeakerByUuid(speakerUuid);
		if (speaker == null || !speaker.isActive()) {
			return failureModel("讲者无效").toJSON();
		}
		if (null != speaker.getUser()) {
			return failureModel("您选择的用户已被关联或者该讲者已关联用户").toJSON();
		}
		User user = userFacade.getUserByUuid(checkedUserUuid);
		if (user == null || !speaker.isActive()) {
			return failureModel("用户无效").toJSON();
		}
		speaker.setUser(user);
		Status status = assetFacade.speakerAssociationUser(speaker);
		if (status == Status.SUCCESS) {
			SpeakerVo vo = new SpeakerVo();
			vo.setStatus(status);
			vo.build(speaker);
			return vo.toJSON();
		} else {
			return failureModel("您选择的用户已被关联或者该讲者已关联用户").toJSON();
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/admin/user/search", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String userListSearch(HttpServletRequest request,
			@RequestParam(required = false, value = "q") String queryKey) {
		List<User> list = userFacade.searchUserListByQ(queryKey);
		OptionAdditionList model = new OptionAdditionList();
		model.buildUser(list);
		model.setQ(queryKey);
		return gson.toJson(model);
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/admin/user/detail", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String showUserDetail(HttpServletRequest request,
			@RequestParam String userUuid) {
		if (StringUtils.isEmpty(userUuid)) {
			return failureModel("查看用户详情出错").toJSON();
		}
		User user = userFacade.getUserByUuid(userUuid);
		if (null == user) {
			return failureModel("查看用户详情出错").toJSON();
		}
		return BaseAPIModel
				.makeWrappedSuccessDataJson(new UserDetailResultItem()
						.build(user));
	}

	/**
	 * 获取申请记录详细信息
	 * 
	 * @param userId
	 * @return
	 */
	/*
	 * @RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	 * 
	 * @RequestMapping(value = "/certification/detail", method =
	 * RequestMethod.GET, produces = "text/json; charset=utf-8") public
	 * 
	 * @ResponseBody String getCertificationDetail(@RequestParam int userId ) {
	 * 
	 * UserExpand userExpand = userFacade.getUserExpendByUserId(userId); if
	 * (null == userExpand){ //如果当前userid没有对应的userExpand,则增加一条数据,默认为医生用户 Status
	 * status = this.onUserIdNotInUserExpand(userId); if(status !=
	 * Status.SUCCESS){ return failureModel("出错啦，请稍后重试.").toJSON(); } return
	 * BaseAPIModel.makeWrappedSuccessDataJson(CertificationUserDataItem
	 * .build(userFacade.getUserExpendByUserId(userId))); } return BaseAPIModel
	 * .makeWrappedSuccessDataJson(CertificationUserDataItem
	 * .build(userExpand)); }
	 */

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/certification/detail", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String getCertificationDetail(@RequestParam int userId) {

		UserExpand userExpand = userFacade.getUserExpendByUserId(userId);
		if (null == userExpand) {
			// 如果当前userid没有对应的userExpand,则增加一条数据,默认为医生用户
			Status status = this.onUserIdNotInUserExpand(userId);
			if (status != Status.SUCCESS) {
				return failureModel("出错啦，请稍后重试.").toJSON();
			}
		}
		CertificationUserDataItem dataItem = this.buildResultData(userId);
		return BaseAPIModel.makeWrappedSuccessDataJson(dataItem);
	}

	private CertificationUserDataItem buildResultData(int userId) {
		UserExpand userExp = userFacade.getUserExpendByUserId(userId);
		Certification certification = certificationFacade
				.getCertifiActiveByUserId(userExp.getUser());
		CertificationUserDataItem dataItem = CertificationUserDataItem.build(
				userExp, certification);
		Hospital hospital = userExp.getHospital();
		if (null != hospital) {
			Province province = hospital.getProvince();
			if (province != null) {
				List<City> cityList = EphemeralData.getIntansce(provinceMapper)
						.getCitys(province);
				dataItem.buildCity(cityList);
			}
			City city = hospital.getCity();
			if (city != null) {
				List<Hospital> hosList = hospitalMapper
						.selectHosListByCityId(city.getId());
				dataItem.buildHospital(hosList);
			}
		} else if (null != userExp.getCity()
				&& null != userExp.getCity().getProvince()) {
			List<City> cityList = EphemeralData.getIntansce(provinceMapper)
					.getCitys(userExp.getCity().getProvince());
			dataItem.buildCity(cityList);
		}
		return dataItem;
	}

	private Status onUserIdNotInUserExpand(int userId) {
		UserExpand userExpand = new UserExpand();
		userExpand.setUuid(Tools.getUUID());
		User user = new User();
		user.setId(userId);
		userExpand.setUser(user);
		userExpand.setStatus(1);
		userExpand.setIsActive(1);
		userExpand.setMark("");
		userExpand.setBirthday("");
		userExpand.setReceipt(0);
		userExpand.setType(UserExpand.USER_DOCTOR);
		userExpand.setSex("1");
		userExpand.setResume("");
		userExpand.setClassId("");
		userExpand.setQualificationId("");
		return userFacade.addUserExpand(userExpand);
	}

	/**
	 * 根据用户id查询该用户是否已关联讲者
	 * 
	 * @param userId
	 * @return josn中返回true为已关联，false为未关联
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/admin/speaker/speakerAssociationUserOrNot", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String speakerAssociationUserOrNot(
			@RequestParam int userId) {
		Speaker speaker = userFacade.speakerAssociationUserOrNot(userId);
		return BaseAPIModel.makeWrappedSuccessDataJson(BooleanResultVo
				.build(speaker));
	}

	// 获取讲者详情
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/admin/speaker/json", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String getSpeakerJsonData(
			@RequestParam String speakerUuid) {
		Speaker speaker = assetFacade.getSpeakerByUuid(speakerUuid);
		if (null != speaker) {
			return BaseAPIModel.makeWrappedSuccessDataJson(BooleanResultVo
					.build(speaker));
		}
		return failureModel("获取讲者信息出错啦").toJSON();
	}

	/**
	 * 实名审核，审核通过之前的操作
	 * 
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/certification/status/before", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String activityStatusBefore(
			@RequestParam String userRealName,
			@RequestParam int attestationType,
			@RequestParam String provinceUuid, @RequestParam String cityUuid,
			@RequestParam String hospitalUuid,
			@RequestParam String departmentsUuid,
			@RequestParam String professorDoctor,
			@RequestParam String companyUuid,
			@RequestParam String professorCompany,
			@RequestParam String certificationInfo, @RequestParam String uuid,
			@RequestParam int status, @RequestParam String speakerPhone,
			@RequestParam int speakerSex) {
		// 传入参数非空判断 TODO:暂没有判断是否选择医院
		if (UserExpand.USER_DOCTOR == attestationType) {
			if (StringUtils.isEmpty(userRealName)
					|| StringUtils.isEmpty(provinceUuid)
					|| StringUtils.isEmpty(cityUuid)
					|| StringUtils.isEmpty(departmentsUuid)
					|| StringUtils.isEmpty(professorDoctor)
					|| StringUtils.isEmpty(uuid) || speakerSex > 1
					|| speakerSex < 0 || StringUtils.isEmpty(certificationInfo)) {
				return failureModel("请完善申请者信息").toJSON();
			}
			if (status > 3 || status < 0) {
				return failureModel("提交失败，请重试").toJSON();
			}
		}
		if (UserExpand.USER_COMPANY == attestationType) {
			if (StringUtils.isEmpty(userRealName)
					|| StringUtils.isEmpty(companyUuid)
					|| StringUtils.isEmpty(professorCompany)
					|| StringUtils.isEmpty(certificationInfo)) {
				return failureModel("请完善申请者信息").toJSON();
			}
		}

		Certification certification = certificationFacade
				.getCertificationByUuid(uuid);
		// 判断该条记录是否已被审核
		if (Certification.HAS_VERIFY == certification.getStatus()
				|| Certification.NO_PASS_VERIFY == certification.getStatus()) {
			return failureModel("该记录已被操作，请刷新页面").toJSON();
		}
		if (null == certification) {
			return failureModel("出错啦，请刷新后重试.").toJSON();
		} else {
			// 构造certification数据
			certification.setStatus(status);
			certification.setCredentials(certificationInfo);
			certification.setName(userRealName);
			certification.setPinyin(Tools.getPinYin(userRealName));
		}

		// 构造userExpand数据
		UserExpand userExpand = userFacade.getUserExpendByUserId(OptionalUtils
				.traceInt(certification, "user.id"));
		if (null == userExpand) {
			return failureModel("出错啦，请刷新后重试.").toJSON();
		}
		if (UserExpand.USER_DOCTOR == attestationType) {
			City city = provinceMapper.selectCtiByUUid(cityUuid);
			Hospital hospital = hospitalMapper
					.selectHospitalByUUid(hospitalUuid);
			Departments departments = hospitalMapper
					.selectDePatByUuid(departmentsUuid);
			if (null == city || null == hospital || null == departments) {
				return failureModel("请完善申请者信息").toJSON();
			}
			userExpand.setCity(city);
			userExpand.setHospital(hospital);
			userExpand.setDepartments(departments);
			userExpand.setProfessor(professorDoctor);
			userExpand.setType(attestationType);
		}
		if (UserExpand.USER_COMPANY == attestationType) {
			userExpand.setProfessor(professorCompany);
			Manufacturer manufacturer = groupFacade
					.selectManufacturerByUuid(companyUuid);
			if (null == manufacturer) {
				return failureModel("请完善申请者信息").toJSON();
			}
			userExpand.setManufacturer(manufacturer);
			userExpand.setType(attestationType);
		}

		// 构造speaker参数
		Speaker speaker = new Speaker();
		speaker.setUuid(Tools.getUUID());
		speaker.setName(userRealName);
		speaker.setPinyin(Tools.getPinYin(userRealName));
		speaker.setMobile(speakerPhone);
		speaker.setSex(speakerSex);
		speaker.setPosition(professorDoctor);
		speaker.setHospital(hospitalMapper.selectHospitalByUUid(hospitalUuid));
		speaker.setDepartment(hospitalMapper.selectDePatByUuid(departmentsUuid));
		speaker.setStatus(BaseModel.UNAPPROVED);
		speaker.setIsActive(1);

		Certification certification_updateResult = certificationFacade
				.updateCertificationStatusWithOptional(certification,
						userExpand, speaker);

		if (null != certification_updateResult) {
			// 实名认证加积分
			if (certification_updateResult.getStatus() == Certification.HAS_VERIFY) {
				integralFacade.actionCertification(certification_updateResult
						.getUser());

				/**
				 * 2016-3-16,添加发送模板消息
				 */
				getModelMessageFacade().realNameNotice(certification_updateResult,
						certification_updateResult.getUser());
			}
			return BaseAPIModel
					.makeWrappedSuccessDataJson(CertificationDataItem
							.build(certification_updateResult));
		}
		return failureModel("出错啦，请刷新后重试.").toJSON();
	}

	/**
	 * 用户列表更新用户信息
	 * 
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/project/userReference/user/updatePage/{uuid}")
	public ModelAndView jumpToUserUpdatePage(HttpServletRequest request,
			@PathVariable(value = "uuid") String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			return new ModelAndView("/common/index");
		}
		request.setAttribute("userUuid", uuid);
		return index(request, "/wrapped/project_user_update.jsp");
	}

	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/project/userReference/user/getUpdateUserData", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String getUserData(HttpServletRequest request,
			@RequestParam String userUuid) {
		if (StringUtils.isEmpty(userUuid)) {
			BaseController
					.bulidRequest("后台更新用户信息", "user", null,
							Status.FAILURE.message(), null, "更新失败，检测到用户uuid为空",
							request);
			return BaseController.failureModel("服务繁忙，请稍后重试").toJSON();
		}
		// 构造用户回显所需信息

		String result = this.buildUpdataUserData(request, userUuid);
		if (null != result) {
			return result;
		}
		BaseController.bulidRequest("后台更新用户信息", "user", null,
				Status.FAILURE.message(), null, "更新失败，构造返回数据错误", request);
		return BaseController.failureModel("服务繁忙，请稍后重试").toJSON();
	}

	private String buildUpdataUserData(HttpServletRequest request, String uuid) {
		User user = userFacade.getUserByUuid(uuid);
		if (null == user) {
			BaseController.bulidRequest("后台更新用户信息", "user", null,
					Status.FAILURE.message(), null, "根据uuid查询用户失败", request);
			return BaseController.failureModel("服务繁忙，请稍后重试").toJSON();
		}

		if (null == user.getUserExpand()) {
			if (Status.SUCCESS
					.equals(this.onUserIdNotInUserExpand(user.getId()))) {
				CertificationUserDataItem selectorData = this
						.buildResultData(user.getId());
				return BaseAPIModel.makeWrappedSuccessDataJson(selectorData);
			}
			BaseController.bulidRequest("后台更新用户信息", "user", null,
					Status.FAILURE.message(), null, "新增userExpand失败", request);
			return BaseController.failureModel("服务繁忙，请稍后重试").toJSON();
		}
		CertificationUserDataItem selectorData = this.buildResultData(user
				.getId());
		return BaseAPIModel.makeWrappedSuccessDataJson(selectorData);
	}

	/**
	 * 超管更新用户数据提交
	 * 
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_ADMIN, logger = true)
	@RequestMapping(value = "/project/userReference/update/save/{userUuid}", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateUserData(HttpServletRequest request,
			@PathVariable String userUuid, @RequestParam int userType,
			@RequestParam(required = false) String provinceUuid,
			@RequestParam(required = false) String cityUuid,
			@RequestParam(required = false) String hospitalUuid,
			@RequestParam(required = false) String departmentUuid,
			@RequestParam(required = false) String companyUuid,
			@RequestParam String userName, @RequestParam String nickName,
			@RequestParam(required = false) String realName,
			@RequestParam String professor, @RequestParam String sex,
			@RequestParam String userRole, @RequestParam String mark) {
		// 请求数据非空判断
		if (UserExpand.USER_DOCTOR == userType) {
			if (StringUtils.isEmpty(provinceUuid)
					|| StringUtils.isEmpty(cityUuid)
					|| StringUtils.isEmpty(hospitalUuid)
					|| StringUtils.isEmpty(departmentUuid)) {
				return BaseController.failureModel("请完善表单信息").toJSON();
			}
		} else if (UserExpand.USER_COMPANY == userType) {
			if (StringUtils.isEmpty(companyUuid)) {
				return BaseController.failureModel("请完善表单信息").toJSON();
			}
		} else {
			return BaseController.failureModel("服务繁忙，请稍后重试").toJSON();
		}
		if (StringUtils.isEmpty(userUuid) || StringUtils.isEmpty(sex)
				|| StringUtils.isEmpty(userName)
				|| StringUtils.isEmpty(nickName)
				|| StringUtils.isEmpty(professor)
				|| StringUtils.isEmpty(userRole)) {
			return BaseController.failureModel("请完善表单信息").toJSON();
		}

		User user = userFacade.getUserByUuid(userUuid);
		if (null == user) {
			BaseController.bulidRequest("后台更新用户信息", "user", null,
					Status.FAILURE.message(), null, "根据userUuid查询user失败",
					request);
			return BaseController.failureModel("服务繁忙，请稍后重试").toJSON();
		}

		UserExpand userExpand = userFacade.getUserExpendByUserId(user.getId());
		if (null == userExpand) {
			return failureModel("出错啦，请刷新后重试.").toJSON();
		}

		City city = null;
		Hospital hospital = null;
		Departments departments = null;
		if (UserExpand.USER_DOCTOR == userType) {
			city = provinceMapper.selectCtiByUUid(cityUuid);
			hospital = hospitalMapper.selectHospitalByUUid(hospitalUuid);
			departments = hospitalMapper.selectDePatByUuid(departmentUuid);
			if (null == city || null == hospital || null == departments) {
				return failureModel("出错啦，请刷新后重试.").toJSON();
			}
		}

		Manufacturer manufacturer = null;
		if (UserExpand.USER_COMPANY == userType) {
			manufacturer = groupFacade.selectManufacturerByUuid(companyUuid);
			if (null == manufacturer) {
				return failureModel("出错啦，请刷新后重试.").toJSON();
			}
		}

		// TODO:List<UserReference> project = user.getUser_reference();
		Project projectTmp = new Project();
		projectTmp.setId(1);
		UserReference ref = userFacade.searchUserReference(user, projectTmp);
		Role role = getRoleByName(userRole);
		if (null != role) {
			ref.setRole(role);
		}

		Certification certification = certificationFacade
				.getCertifiActiveByUserId(user);
		if (null != certification && null != realName) {
			certification.setName(realName);
			certification.setPinyin(Tools.getPinYin(certification.getName()));
		}

		if (UserExpand.USER_DOCTOR == userType) {
			userExpand.setCity(city);
			userExpand.setHospital(hospital);
			userExpand.setDepartments(departments);
		}
		if (UserExpand.USER_COMPANY == userType) {
			userExpand.setManufacturer(manufacturer);
		}

		userExpand.setType(userType);
		userExpand.setProfessor(professor);
		userExpand.setSex(sex);

		user.setUsername(userName);
		user.setNickname(nickName);
		user.setPinyin(Tools.getPinYin(user.getNickname()));
		user.setMark(mark);

		ActionMessage action = userFacade.updateUserInfoBySuperAdmin(ref,
				certification, userExpand, user);
		if (action.isSuccess()) {
			BaseController.bulidRequest("超管更新用户信息",
					"user, user_expand, user_reference, certification", null,
					Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		}
		BaseController.bulidRequest("超管更新用户信息",
				"user, user_expand, user_reference, certification", null,
				Status.FAILURE.message(), null, "失败", request);
		return BaseController.failureModel("服务繁忙，请稍后重试").toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/game/{uuid}/play", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String userGamePlay(@PathVariable String uuid) {
		AwardResult result = gameMgrFacade.play(uuid,
				getCurrentUser(getHandleRequest()));
		return successModel("" + result.getTimes()).toJSON();
	}


	@RequestMapping(value = "/config", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String userGamePlay() {
		System.out.println(Config.cc_user_id);
		return Config.host;
	}
	
	/**
	 * @Description: 用户列表，webchatuser
	 *
	 * @author mayuan
	 * @createDate 2016年12月21日
	 * @modifyDate 2016年12月21日
	 */
	@RequestAuthority(value = Role.PRO_ADMIN, requiredProject = true, redirect = false)
	@RequestMapping(value = "/project/userReference/user/noreg/datatable", method = RequestMethod.GET)
	public @ResponseBody String webchatUsersDatatable(
			HttpServletRequest request, @RequestParam(required = false) String isReg) {
		User user = getCurrentUser(request);
		Project project = user.getStubProject();
		String query = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		
		Pagination<WebchatUser> users = webchatFacade.searchNoRegWechatUsers(query, from, size,
				project.getId());
		UserProjectClientData uc = new UserProjectClientData();
		uc.setiTotalDisplayRecords(users.getTotal());
		uc.setiTotalRecords(users.getPage_rows());
		uc.buildWebcahtUserData(users.getResults());
		// 记录日志
		BaseController.bulidRequest("后台用户查看用户列表", "user", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(uc);
	}
	
	/**
	 * @Description: 禁用未注册用户
	 *
	 * @author mayuan
	 * @createDate 2016年12月22日
	 * @modifyDate 2016年12月22日
	 */
	@RequestAuthority(value = Role.PRO_ADMIN, requiredProject = true, logger = false)
	@RequestMapping(value = "/project/manage/isAction/wechatuser", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String disableWecahtUser(HttpServletRequest request,
			@RequestParam String uuid) {
		WebchatUser wechatUser = webchatFacade.searchWebChatUserByUuid(uuid);
		if(null == wechatUser)
			return getStatusJson(Status.FAILURE);
		wechatUser.setIsActive(wechatUser.getIsActive() == BaseModel.DISABLE ? BaseModel.ACTIVE : BaseModel.DISABLE);
		Status status = webchatFacade.updateWebChatUser(wechatUser);
		if (status == Status.SUCCESS) {
			return getStatusJson(String.valueOf(wechatUser.getIsActive() ^ 1));
		}
		return getStatusJson(Status.FAILURE);
	}
	
}
