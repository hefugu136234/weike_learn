package com.lankr.tv_cloud.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.AdverFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.SubscribeFacade;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Advertisement;
import com.lankr.tv_cloud.model.AdvertisementPosition;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.AdverClientData;
import com.lankr.tv_cloud.vo.AdverClientItem;
import com.lankr.tv_cloud.vo.AdverDetail;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
public class AdvertisementController extends AdminWebController {

	@Autowired
	private AdverFacade adverFacade;

	@Autowired
	protected SubscribeFacade subscribeFacade;

	private static String adver_save_key = "adver_save_key";

	/**
	 * 加载项目下的广告列表页面
	 */
	@RequestAuthority(value = Role.PRO_USER_LEVEL, requiredProject = true, logger = false)
	@RequestMapping(value = "/project/adver/list/page", method = RequestMethod.GET)
	public ModelAndView loadadverListPage(HttpServletRequest request) {
		return index(request, "/wrapped/advert_list.jsp");
	}

	/**
	 * 加载广告列表中的数据
	 */
	@RequestAuthority(value = Role.PRO_USER_LEVEL, requiredProject = true)
	@RequestMapping(value = "/project/adver/list", method = RequestMethod.GET)
	public @ResponseBody String getAdverListData(HttpServletRequest request) {
		User user = getCurrentUser(request);
		Project project = user.getStubProject();
		String search = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Advertisement> adverObject = adverFacade.searchAdverList(
				search, from, size, project.getId());
		AdverClientData adverData = new AdverClientData();
		adverData.setiTotalDisplayRecords(adverObject.getTotal());
		adverData.setiTotalRecords(adverObject.getPage_rows());
		adverData.buildData(adverObject.getResults());
		// 记录日志
		BaseController.bulidRequest("后台查看广告列表", "advertisement", null,
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(adverData);

	}

	/**
	 * 改变广告状态
	 */
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/project/change/adver/stats", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String changeAdverStatus(HttpServletRequest request,
			@RequestParam String uuid) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		Advertisement adver = adverFacade.searchAdverByUuid(uuid);
		if (adver == null) {
			BaseController.bulidRequest("后台更改广告状态", "advertisement", null,
					Status.FAILURE.message(), null, "uuid=" + uuid + " 此广告不可用",
					request);
			return getStatusJson(Status.NOT_FOUND);
		}
		if (!user.getStubProject().isSame(adver.getProject())) {
			return getStatusJson(Status.NO_PERMISSION);
		}
		// 上下线转换
		adver.setStatus(adver.getStatus() == BaseModel.UNAPPROVED ? BaseModel.APPROVED
				: BaseModel.UNAPPROVED);

		Status status = adverFacade.updateAdverToStatus(adver);
		if (status == Status.SUCCESS) {
			AdverClientItem item = new AdverClientItem();
			item.format(adver);
			// 记录日志
			BaseController.bulidRequest("后台更改广告状态", "advertisement", null,
					status.message(), null, "成功=" + adver.getStatus(), request);
			return gson.toJson(item);

		} else {
			BaseController.bulidRequest("后台更改广告状态", "advertisement", null,
					status.message(), null, "失败=" + adver.getStatus(), request);
		}
		return getStatusJson(Status.FAILURE);
	}

	/**
	 * 加载新增广告页面
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/adver/add/page", method = RequestMethod.GET)
	public ModelAndView adverAddPage(HttpServletRequest request, Status status) {
		if (status != null)
			request.setAttribute("adver_status", status.message());
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.getSession().setAttribute(adver_save_key, token);
		return index(request, "/wrapped/adver_add.jsp");
	}

	/**
	 * 加载广告位位置
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/adver/postion/data", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String loadAdverPostion(HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		List<AdvertisementPosition> listPosition = adverFacade
				.searchAllPostion();
		if (listPosition == null || listPosition.size() == 0)
			return getStatusJson(Status.NO_VALUE);
		return gson.toJson(listPosition);
	}

	/**
	 * 新增一个广告数据
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/project/adver/add/save", method = RequestMethod.POST)
	public ModelAndView adverSave(HttpServletRequest request,
			@RequestParam String name, @RequestParam String positon,
			@RequestParam String adtime, @RequestParam String token,
			@RequestParam(required = false) String img_url,
			@RequestParam(required = false) String description) {
		User user = getCurrentUser(request);
		Status status = Status.SUCCESS;
		if (user == null)
			return adverAddPage(request, Status.NO_PERMISSION);
		if (!token.equals(toastRepeatSubmitToken(request, adver_save_key))) {
			return adverAddPage(request, Status.SUBMIT_REPEAT);
		}
		Project project = user.getStubProject();
		if (project == null)
			return adverAddPage(request, Status.NO_PERMISSION);
		if (status == Status.SUCCESS) {
			Advertisement adverModel = new Advertisement();
			adverModel.setProject(project);
			AdvertisementPosition postionModel = new AdvertisementPosition();
			postionModel.setId(Integer.parseInt(positon));
			adverModel.setAdvertisementPosition(postionModel);
			adverModel.setContent_type("image");
			adverModel.setIsActive(BaseModel.ACTIVE);
			adverModel.setLimit_time(Integer.parseInt(adtime));
			adverModel.setMark(description);
			adverModel.setName(name);
			adverModel.setRes_url(img_url);
			adverModel.setStatus(BaseModel.UNAPPROVED);
			adverModel.setUser(user);
			String uuid = Tools.getUUID();
			adverModel.setUuid(uuid);
			status = adverFacade.addAdver(adverModel);
			if (status == Status.SUCCESS)
				BaseController.bulidRequest("后台新增广告", "advertisement",
						adverModel.getId(), status.message(), null, "成功",
						request);
		} else {
			BaseController.bulidRequest("后台新增广告", "advertisement", null,
					status.message(), null, "失败 ", request);
		}
		return adverAddPage(request, status);
	}

	/**
	 * 加载编辑广告所需要的数据
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, redirect = false)
	@RequestMapping(value = "/project/adver/{uuid}/detail", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String adverDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		Advertisement adver = adverFacade.searchAdverByUuid(uuid);
		if (adver == null) {
			BaseController.bulidRequest("后台查看广告详情", "advertisement", null,
					Status.FAILURE.message(), null, "uuid=" + uuid + " 此广告不存在",
					request);
			return getStatusJson(Status.NOT_FOUND);
		}
		if (!user.getStubProject().isSame(adver.getProject())) {
			return getStatusJson(Status.NO_PERMISSION);
		}
		AdverDetail detail = new AdverDetail();
		List<AdvertisementPosition> listPosition = adverFacade
				.searchAllPostion();
		detail.formatData(adver, listPosition);
		BaseController.bulidRequest("后台新增广告", "advertisement", adver.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		return gson.toJson(detail);
	}

	/**
	 * 编辑完广告后，更新广告信息
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, redirect = false)
	@RequestMapping(value = "/porject/adver/update", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String adverUpdate(HttpServletRequest request,
			@RequestParam String uuid, @RequestParam String adname,
			@RequestParam String adpostion, @RequestParam String adtime,
			@RequestParam(required = false) String adimg,
			@RequestParam(required = false) String admark) {
		User user = getCurrentUser(request);
		if (user == null || user.getStubProject() == null)
			return getStatusJson(Status.NO_PERMISSION);
		Advertisement adver = adverFacade.searchAdverByUuid(uuid);
		if (adver == null) {
			BaseController.bulidRequest("后台修改广告", "advertisement", null,
					Status.FAILURE.message(), null, "uuid=" + uuid + " 此广告不存在",
					request);
			return getStatusJson(Status.NOT_FOUND);
		}
		if (!user.getStubProject().isSame(adver.getProject())) {
			return getStatusJson(Status.NO_PERMISSION);
		}
		adver.setLimit_time(Integer.parseInt(adtime));
		adver.setName(adname);
		adver.setMark(admark);
		adver.setRes_url(adimg);
		AdvertisementPosition postionModel = new AdvertisementPosition();
		postionModel.setId(Integer.parseInt(adpostion));
		adver.setAdvertisementPosition(postionModel);
		Status status = adverFacade.updateAdver(adver);
		if (status == Status.SUCCESS) {
			AdverClientItem item = new AdverClientItem();
			item.format(adver);
			BaseController.bulidRequest("后台修改广告", "advertisement",
					adver.getId(), Status.SUCCESS.message(), null, "成功",
					request);
			return gson.toJson(item);
		}else{
			BaseController.bulidRequest("后台修改广告", "advertisement",
					adver.getId(), Status.FAILURE.message(), null, "失败",
					request);
		}
		return getStatusJson(Status.FAILURE);
	}

}
