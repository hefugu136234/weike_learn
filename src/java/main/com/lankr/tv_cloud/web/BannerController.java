package com.lankr.tv_cloud.web;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.tools.Tool;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.orm.mybatis.mapper.BannerMapper;
import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.BannerSurface;
import com.lankr.tv_cloud.vo.BannerVo;
import com.lankr.tv_cloud.vo.PdfInfoVo;
import com.lankr.tv_cloud.vo.PdfSuface;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

/**
 * @author mayuan Banner 管理
 */
@SuppressWarnings("all")
@RequestMapping("/project/banner")
@Controller
public class BannerController extends AdminWebController {

	private static final String BANNER_UPLOAD_KEY = "banner_upload_key";
	private static final String BANNER_UPDATE_KEY = "banner_update_key";
	private static final String TASKID_NOTFOUND = "0";
	private static final int TYPE_DEFAULT = 0;
	private static final String IMAGEUPLOAD_TAG = "success"; // 图片上传标记

	/**
	 * 点击左侧菜单进入Banner管理页面
	 * 
	 * @param request
	 * @return 跳转到 banner_index.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/mgr", method = RequestMethod.GET)
	public ModelAndView bannerMgrIndex(HttpServletRequest request) {
		return index(request, "/wrapped/banner/banner_index.jsp");
	}

	/**
	 * 新增Banner页面跳转
	 * 
	 * @param request
	 * @return	/wrapped/banner/banner_add.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/addPage", method = RequestMethod.GET)
	public ModelAndView jumpToAddPage(HttpServletRequest request) {
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.setAttribute("signature", getQiniuUploaderSignature());
		request.getSession().setAttribute(BANNER_UPLOAD_KEY, token);
		return index(request, "/wrapped/banner/banner_add.jsp");
	}

	/**
	 * 新增Banner操作
	 * 
	 * @param request
	 * @param type
	 *            banner类型
	 * @param title
	 *            标题
	 * @param bannerTaskId
	 *            上传后返回的资源标识
	 * @param refUrl
	 *            点击图片触发的链接
	 * @param validDate
	 *            有效期
	 * @param mark
	 * @param token
	 *            图片上传uuid
	 * @return status
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String saveBanner(HttpServletRequest request,
			@RequestParam int type, @RequestParam String title,
			@RequestParam String bannerTaskId, @RequestParam String refUrl,
			@RequestParam String validDate, @RequestParam String mark,
			@RequestParam String token, @RequestParam String uploadTage,@RequestParam int position) {
		// 图片上传检查
		if (StringUtils.isEmpty(uploadTage)
				|| !(IMAGEUPLOAD_TAG.equals(uploadTage))) {
			return BaseController.failureModel("你还没有上传图片! 请点击 \"选择图片\" 按钮上传")
					.toJSON();
		}
		// 表单重复提交检查
		String repeat_token = toastRepeatSubmitToken(request, BANNER_UPLOAD_KEY);
		if (StringUtils.isNotEmpty(token) && !token.equals(repeat_token))
			return BaseController.failureModel("该页面已被提交过，请尝试刷新页面").toJSON();
		Banner bannerVo = new Banner();
		// 设置初始状态为 success
		Status status = Status.SUCCESS;
		try {
			String imgUrl = this.getBannerDownloadUrl(Tools
					.nullValueFilter(bannerTaskId))
					+ "?odconv/jpg/page/1/density/160/quality/80/resize/600";
			bannerVo.setUuid(Tools.getUUID());
			bannerVo.setTitle(Tools.nullValueFilter(title));
			bannerVo.setIsActive(BaseModel.APPROVED);
			bannerVo.setState(BaseModel.UNDERLINE);
			bannerVo.setMark(Tools.nullValueFilter(mark));
			bannerVo.setRefUrl(Tools.nullValueFilter(refUrl));
			bannerVo.setImageUrl(imgUrl);
			bannerVo.setType(type);
			bannerVo.setPosition(position);
			bannerVo.setValidDate(TimeUnit.DAYS.toSeconds(Long.parseLong(Tools
					.nullValueFilter(validDate))));
			bannerVo.setTaskId(bannerTaskId);
			// 执行保存数据
			status = bannerFacade.saveBanner(bannerVo);
		} catch (Exception e) {
			e.printStackTrace();
			BaseController.bulidRequest("新增banner", "banner", bannerVo.getId(),
					Status.FAILURE.message(), null, "失败", request);

			return BaseController.failureModel("添加 Banner 失败，请重试").toJSON();
		}
		if (Status.SUCCESS.equals(status)) {
			BaseController.bulidRequest("新增banner", "banner", bannerVo.getId(),
					Status.SUCCESS.message(), null, "成功", request);
		}
		return getStatusJson(status);
	}

	// 获取图片全路径
	private String getBannerDownloadUrl(String bannerTaskId) {
		return Config.qn_cdn_host + "/" + bannerTaskId;
	}

	/**
	 * 后台管理显示所有Banner列表
	 * 
	 * @param request
	 * @return json
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/showBannerList", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String showBannerList(HttpServletRequest request,
			@RequestParam(required = false) String type) {
		// 获取前台datatable传递的查询参数
		String searchValue = nullValueFilter(request.getParameter("sSearch"));
		Object iDisplayLength = request.getParameter("iDisplayLength");
		int pageSize = iDisplayLength == null ? 10 : Integer
				.valueOf((String) iDisplayLength);
		Object iDisplayStart = request.getParameter("iDisplayStart");
		int startPage = iDisplayStart == null ? 0 : Integer
				.valueOf((String) iDisplayStart);
		int type_int = TYPE_DEFAULT;
		if (StringUtils.isNotEmpty(type)) {
			type_int = Integer.parseInt(type);
		}
		BannerSurface suface = new BannerSurface();
		try {
			// 查询列表
			Pagination<Banner> bannerList = bannerFacade.selectBannerList(
					searchValue, startPage, pageSize, type_int);
			suface.setiTotalDisplayRecords(bannerList.getTotal());
			suface.setiTotalRecords(bannerList.getPage_rows());
			suface.buildData(bannerList.getResults());
			suface.setStatus(Status.SUCCESS);
			BaseController.bulidRequest("后台查询所有Banner列表", "banner", null,
					Status.SUCCESS.message(), null, "成功", request);
			return gson.toJson(suface);
		} catch (Exception e) {
			e.printStackTrace();
			suface.setStatus(Status.FAILURE);
			BaseController.bulidRequest("后台查询所有Banner列表", "banner", null,
					Status.FAILURE.message(), null, "失败", request);
		}
		return gson.toJson(suface);
	}

	/**
	 * 更新 Banner state 状态
	 * 
	 * @param request
	 * @param uuid
	 * @return 操作结果状态status
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/updateState", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String updateBannerState(HttpServletRequest request, String uuid) {
		// 根据请求中的 uuid 判断 Banner 是否存在
		Banner banner = bannerFacade.selectBannerByUuid(uuid);
		if (null == banner || !banner.isActive()) {
			BaseController.bulidRequest("后台Banner状态转换", "banner", null,
					Status.FAILURE.message(), null, "uuid=" + uuid
							+ " 该 Banner 不存在", request);
			return BaseController.failureModel("该 Banner 不存在！").toJSON();
		}
		// 设置状态
		banner.setState(BaseModel.UNDERLINE == banner.getState() ? BaseModel.APPROVED
				: BaseModel.UNDERLINE);
		// 更新 Banner
		Status status = bannerFacade.updateBannerState(banner);
		if (Status.SUCCESS != status) {
			return BaseController.failureModel("更新banner失败，请重试").toJSON();
		} else {
			BannerVo bannerVo = new BannerVo();
			bannerVo.buildData(bannerFacade.selectBannerByUuid(uuid));
			return gson.toJson(bannerVo);
		}
	}

	/**
	 * 跳转到编辑页面
	 * 
	 * @param request
	 * @return /wrapped/banner/banner_update.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/updatePage/{uuid}", method = RequestMethod.GET)
	public ModelAndView jumpToUpdatePage(HttpServletRequest request,
			@PathVariable String uuid) {
		// 根据请求中的 uuid 判断 Banner 是否存在
		Banner banner = bannerFacade.selectBannerByUuid(uuid);
		if (null == banner || !banner.isActive()) {
			// 携带错误信息到目标页面
			// request.setAttribute("BannerNotFound", "Banner 信息未找到");
			BaseController.bulidRequest("后台Banner编辑", "banner", null,
					Status.FAILURE.message(), null, "uuid=" + uuid
							+ " 该 Banner 不存在", request);
			return this.bannerMgrIndex(request);
		} else {
			/*
			 * int state = banner.getState(); if(state == 1){ return
			 * BaseController.failureModel("该资源正在展示! 如需修改，请先将改资源下线").toJSON(); }
			 */

			String token = Tools.getUUID();
			request.setAttribute("token", token);
			request.getSession().setAttribute(BANNER_UPDATE_KEY, token);

			BannerVo bannerVo = new BannerVo();
			bannerVo.buildData(banner);
			request.setAttribute("bannerVo", bannerVo);
		}
		return index(request, "/wrapped/banner/banner_update.jsp");
	}

	/**
	 * 更新 banner 数据
	 * 
	 * @return Status
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/updateBanner", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String updateBanner(HttpServletRequest request,
			@RequestParam String token, @RequestParam String title,
			@RequestParam String refUrl, @RequestParam String type,
			@RequestParam int position,
			@RequestParam String validDate, @RequestParam String mark,
			@RequestParam String uuid) {
		// 表单重复提交检查
		String repeat_token = toastRepeatSubmitToken(request, BANNER_UPDATE_KEY);
		if (StringUtils.isNotEmpty(token) && !token.equals(repeat_token))
			return BaseController.failureModel("该页面已被提交过，请尝试刷新页面").toJSON();
		// 根据请求中的 uuid 判断 Banner 是否存在
		Banner banner = bannerFacade.selectBannerByUuid(uuid);
		if (null == banner || !banner.isActive()) {
			BaseController.bulidRequest("后台Banner编辑", "banner", null,
					Status.FAILURE.message(), null, "uuid=" + uuid
							+ " 该 Banner 不存在", request);
			return getStatusJson(Status.FAILURE);
		} else {
			banner.setTitle(Tools.nullValueFilter(title));
			banner.setRefUrl(Tools.nullValueFilter(refUrl));
			if (StringUtils.isEmpty(type)) {
				banner.setType(0);
			} else {
				banner.setType(Integer.parseInt(type));
			}
			banner.setPosition(position);
			banner.setValidDate(TimeUnit.DAYS.toSeconds(Long.parseLong(Tools
					.nullValueFilter(validDate))));
			banner.setMark(Tools.nullValueFilter(mark));
			Status status = bannerFacade.updateBanner(banner);
			if (Status.SUCCESS.equals(status)) {
				BaseController.bulidRequest("更新banner", "banner",
						banner.getId(), Status.SUCCESS.message(), null, "成功",
						request);
			} else {
				BaseController.bulidRequest("更新banner", "banner",
						banner.getId(), Status.FAILURE.message(), null, "失败",
						request);
				return BaseController.failureModel("添加 Banner 失败，请重试").toJSON();
			}
		}
		return getStatusJson(Status.SUCCESS);
	}

	/**
	 * 删除 banner
	 * 
	 * @param request
	 * @param uuid
	 * @return Status
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/del", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String deleteBanner(HttpServletRequest request,
			@RequestParam String uuid) {
		// 根据请求中的 uuid 判断 Banner 是否存在
		Banner banner = bannerFacade.selectBannerByUuid(uuid);
		if (null == banner || !banner.isActive()) {
			BaseController.bulidRequest("后台Banner软删除banner", "banner", null,
					Status.FAILURE.message(), null, "uuid=" + uuid
							+ " 该 Banner 不存在", request);
			return BaseController.failureModel("该 Banner 不存在！").toJSON();
		}
		// 软删除 Banner
		if (banner.getState() == 1) {
			return BaseController.failureModel("该资源正在展示中，请先将改资源下线!").toJSON();
		}
		Status status = bannerFacade.deleteBanner(banner);
		if (Status.SUCCESS != status) {
			return BaseController.failureModel("删除banner失败，请重试").toJSON();
		}
		return getStatusJson(Status.SUCCESS);
	}
	
}
