package com.lankr.tv_cloud.web;

import java.util.ArrayList;
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
import com.lankr.tv_cloud.facade.inner.resource.GeneralResourceEmbed;
import com.lankr.tv_cloud.facade.inner.resource.ResourceEmbed;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.ResourceVoteSubject;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.OupsVo;
import com.lankr.tv_cloud.vo.RecourceDetailVo;
import com.lankr.tv_cloud.vo.ResourceSurface;
import com.lankr.tv_cloud.vo.ResourceSurfaceVo;
import com.lankr.tv_cloud.vo.ResourceVotesData;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.api.SimpleData;
import com.lankr.tv_cloud.web.api.BaseAPIController;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
import com.lankr.tv_cloud.web.front.vo.FrontResourceItem;
import com.lankr.tv_cloud.web.front.vo.FrontThreeScreenVo;

@SuppressWarnings("all")
@Controller
@RequestMapping("/project/resource")
public class ResourceController extends AdminWebController {

	// @RequestAuthority(value = Role.PRO_EDITOR,logger=false)
	// @RequestMapping("/answer/page/{uuid}")
	// public ModelAndView pdfListPage(HttpServletRequest request,@PathVariable
	// String uuid) {
	// if(uuid!=null){
	// //返回视频列表页
	// System.out.println("uuid:"+uuid);
	// request.setAttribute("resource_msg", "提示信息");
	// return index(request, "/wrapped/answer/answer.jsp");
	// }else{
	// return new ModelAndView("redirect:/project/pdf/list/page");
	// }
	// }

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/{uuid}/vote/mgr")
	public ModelAndView newVote(HttpServletRequest request,
			@PathVariable String uuid, Model model) {
		/*
		 * Resource res = resourceFacade.getResource(assetFacade
		 * .getVideoByUUID(uuid));
		 */
		// 20160429 -->xm
		Resource res = resourceFacade.getResourceByUuid(uuid);

		model.addAttribute("resource", res);
		List<ResourceVoteSubject> subjects = resourceFacade
				.getVotesByResourceId(res);
		List<ResourceVotesData> votes = null;
		if (subjects != null) {
			votes = new ArrayList<ResourceVotesData>(subjects.size());
			for (ResourceVoteSubject resourceVoteSubject : subjects) {
				votes.add(new ResourceVotesData()
						.formatFromVote(resourceVoteSubject));
			}
		}
		model.addAttribute("votes", gson.toJson(votes));
		return index(request, "/wrapped/vote/mgr.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/vote/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String voteSave(@RequestParam String res_uuid,
			@RequestParam String vote_data) {
		Resource res = resourceFacade.getResourceByUuid(res_uuid);
		if (res.getStatus() == 1) {
			return BaseAPIController.failureModel("上线的资源不可更改投票内容").toJSON();
		}
		if (res == null || !res.isActive()) {
			return BaseAPIController.failureModel("资源不可用").toJSON();
		}
		ResourceVotesData data = gson.fromJson(vote_data,
				ResourceVotesData.class);
		if (data == null) {
			return BaseAPIController.failureModel("参数错误").toJSON();
		}
		ResourceVoteSubject subject = data.parseToSubject();
		if (subject == null) {
			BaseAPIController.failureModel("未设置必须的内容").toJSON();
		}
		subject.setResource(res);
		Status status = resourceFacade.addOrUpdateResourceVote(subject);
		if (status != Status.SUCCESS) {
			return BaseAPIController.failureModel(status.message()).toJSON();
		} else {
			return data.formatFromVote(subject).toJSON();
		}
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/vote/del", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String delVote(@RequestParam String uuid) {
		return BaseAPIController.getStatusJson(resourceFacade.delVote(uuid));
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/vote/option/del", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String delVoteOption(@RequestParam String uuid) {
		return BaseAPIController.getStatusJson(resourceFacade
				.delVoteOption(uuid));
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/{res_uuid}/result", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String getResourceVoteResults(
			@PathVariable String res_uuid) {
		Resource res = resourceFacade.getResourceByUuid(res_uuid);
		if (res == null || !res.isValid()) {
			return BaseAPIController.failureModel("资源无效").toJSON();
		}

		List<ResourceVoteSubject> votes = resourceFacade
				.getVotesByResourceId(res);

		return "";
	}

	// 分类管理页面资源显示 -->XiaoMa
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/categoryDeail/{categoryUuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String showCatetoryDetail(HttpServletRequest request,
			@PathVariable(value = "categoryUuid") String categoryUuid,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String state) {
		String searchValue = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int pageSize = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int startPage = fromObj == null ? 0 : Integer.valueOf((String) fromObj);

		Category category = null;
		if (StringUtils.isNotEmpty(categoryUuid)) {
			category = assetFacade.getCategoryByUuid(categoryUuid);
		}
		Pagination<Resource> pagination = resourceFacade.getResourcesList(
				searchValue, category, startPage, pageSize, type, state);
		if (null == pagination) {
			return BaseController.failureModel("查询出错，请重试").toJSON();
		}
		ResourceSurface suface = new ResourceSurface();
		suface.setiTotalDisplayRecords(pagination.getTotal());
		suface.setiTotalRecords(pagination.getPage_rows());
		// suface.bulid(pagination.getResults(), activityOpusFacade);
		suface.bulid(pagination.getResults());
		return gson.toJson(suface);
	}

	// 获取选中资源已存在标签
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/labeling/showExistTags/{resourceUuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String resourceTagsList(HttpServletRequest request,
			@PathVariable String resourceUuid) {
		if (StringUtils.isEmpty(resourceUuid)) {
			return failureModel("获取该资源对应的标签出错，请刷新页面，重新尝试").toJSON();
		}
		List<TagChild> tags = resourceFacade
				.getTagsByResourceUuid(resourceUuid);
		if (null != tags) {
			return gson.toJson(tags);
		}
		return this.getEmptyJson();
	}

	// 给资源关联标签
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/labeling/resAddLabel/", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String resAddLabel(
			@RequestParam String checkedTagsUuids,
			@RequestParam String resourceUuid, HttpServletRequest request) {
		if (StringUtils.isEmpty(checkedTagsUuids))
			return failureModel("请选择需要关联的标签").toJSON();
		if (StringUtils.isEmpty(resourceUuid))
			return failureModel("提交失败，请重试").toJSON();

		ActionMessage action = null;
		String[] uuids = checkedTagsUuids.split(",");
		// 获取用户提交的所有标签的id
		int[] requestTagsIds = new int[uuids.length];
		for (int i = 0; i < uuids.length; i++) {
			requestTagsIds[i] = tagFacade.selectChildTagByUuid(uuids[i])
					.getId();
		}

		Resource resource = resourceFacade.getResourceByUuid(resourceUuid);
		if (null != resource) {
			// 根据选中的资源信息查询该资源已存在的标签 id
			int[] existsTagIds = resourceFacade.getTagsIdsByResourceId(resource
					.getId());
			// 执行关联标签操作
			action = resourceFacade.resAddLabels(requestTagsIds, existsTagIds,
					resource.getId());
		}
		if (action.isSuccess()) {
			return successModel().toJSON();
		}
		return failureModel(action.getMessage()).toJSON();
	}

	// 资源关联的标签删除
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/labeling/del/", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String delResTags(HttpServletRequest request,
			@RequestParam String tagsUuid, @RequestParam String resourceUuid) {
		if (StringUtils.isEmpty(tagsUuid) || StringUtils.isEmpty(resourceUuid)) {
			return failureModel("操作失败，请重新尝试").toJSON();
		}
		Resource resource = resourceFacade.getResourceByUuid(resourceUuid);
		TagChild tagChild = tagFacade.selectChildTagByUuid(tagsUuid);
		if (null == resource || null == tagChild) {
			return failureModel("操作失败，请重新尝试").toJSON();
		}
		ActionMessage action = resourceFacade.delResTagByResourceIdAndTagId(
				resource.getId(), tagChild.getId());
		if (action.isSuccess()) {
			return successModel().toJSON();
		}
		return failureModel(action.getMessage()).toJSON();
	}

	// 获取资源关联的作品信息
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/related/oups/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String relatedOups(HttpServletRequest request,
			@PathVariable String uuid) {
		Resource res = resourceFacade.getResourceByUuid(uuid);
		if (res == null || !res.isActive()) {
			return failureModel("资源无效").toJSON();
		}
		ActivityApplication application = activityOpusFacade
				.relatedResOups(res);
		OupsVo vo = new OupsVo();
		vo.buildData(application);
		return vo.toJSON();
	}

	// 通过编号寻找作品
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/oups/search/code", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String oupsSearchCode(HttpServletRequest request,
			@RequestParam String code) {
		ActivityApplication activityApplication = activityOpusFacade
				.getApplicateByCode(code);
		OupsVo vo = new OupsVo();
		if (activityApplication == null) {
			vo.setStatus(Status.FAILURE);
			vo.setMessage("编号对应的作品不存在");
			return vo.toJSON();
		}
		if (activityApplication.getResource() != null
				&& activityApplication.getStatus() == 1) {
			vo.setStatus(Status.FAILURE);
			vo.setMessage("编号对应的作品已绑定资源");
			return vo.toJSON();
		}
		vo.buildData(activityApplication);
		return vo.toJSON();
	}

	/**
	 * 绑定编号对应作品资源
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/oups/relate/res/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String oupsRelateResSave(HttpServletRequest request,
			@RequestParam String resUuid, @RequestParam String oupsUUid) {
		Resource res = resourceFacade.getResourceByUuid(resUuid);
		if (res == null || !res.isActive()) {
			BaseController.bulidRequest("后台编号绑定资源",
					"activity_resource_application", null,
					Status.FAILURE.message(), null, "资源不存在或已下线", request);
			return failureModel("资源无效").toJSON();
		}
		ActivityApplication application = activityOpusFacade
				.getApplicateByUuid(oupsUUid);
		if (application == null) {
			BaseController.bulidRequest("后台编号绑定资源",
					"activity_resource_application", null,
					Status.FAILURE.message(), null, "作品不存在", request);
			return failureModel("作品不存在").toJSON();
		}
		Activity activity = application.getActivity();
		if (application.isBundled()) {
			BaseController.bulidRequest("后台编号绑定资源",
					"activity_resource_application", application.getId(),
					Status.FAILURE.message(), null, "编号作品已绑定资源", request);
			return failureModel("编号作品已绑定资源").toJSON();
		}

		application.setResource(res);
		application.setStatus(ActivityApplication.STATUS_RECEIVED);
		Status status = activityOpusFacade.updateRelateOups(application);
		/**
		 * 查看活动中资源是否存在
		 */
		if (activity != null) {
			ActivityResource activityResource = activityFacade
					.searchResourceActivityUseIds(activity, res);
			if (activityResource == null) {
				// 活动下不存在该资源
				activityResource = new ActivityResource();
				activityResource.setName(res.getName());
				activityResource.setUuid(Tools.getUUID());
				activityResource.setActivity(activity);
				activityResource.setResource(res);
				activityResource.setStatus(2);// 下线
				activityResource.setIsActive(1);
				activityFacade.addResouceActivityRelate(activityResource);
			} else {
				// 活动下存在该资源
				int isActivie = activityResource.getIsActive();
				if (isActivie != 1) {
					// 更新状态
					activityFacade
							.updateResouceActivityRelate(activityResource);
				}
			}
		}

		if (status == Status.SUCCESS) {
			BaseController.bulidRequest("后台编号绑定资源",
					"activity_resource_application", application.getId(),
					Status.SUCCESS.message(), null, "成功", request);
			/**
			 * 2016-3-16,添加发送模板消息
			 */
			getModelMessageFacade().oupsCheck(application,
					application.getUser());

			ResourceSurfaceVo resourceSurfaceVo = new ResourceSurfaceVo();
			Resource resource = resourceFacade.getResourceByUuid(resUuid);
			resourceSurfaceVo.build(resource);
			resourceSurfaceVo.setRelatedOups(activityOpusFacade
					.oupsResCount(resource));
			return BaseAPIModel.makeWrappedSuccessDataJson(resourceSurfaceVo);
		} else {
			BaseController.bulidRequest("后台编号绑定资源",
					"activity_resource_application", application.getId(),
					Status.FAILURE.message(), null, "后台编号绑定资源失败", request);
			return failureModel("作品绑定资源失败").toJSON();
		}
	}

	// 获取资源外链的数据
	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/{uuid}/embed/{type}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String resourceShareEmbed(@PathVariable String uuid,
			@PathVariable String type) {
		Resource resource = effectResourceFacade().getResourceByUuid(uuid);
		if (resource == null || !resource.apiUseable()) {
			return failureModel("资源不可用").toJSON();
		}
		ResourceEmbed embed;
		try {
			embed = GeneralResourceEmbed.getResourceEmbed(resource);
			if (embed == null) {
				return failureModel("不支持外部分享").toJSON();
			}
			if (GeneralResourceEmbed.TYPE_IFRAME.equals(type)) {
				String iframe = embed.iframe();
				return BaseAPIModel
						.makeSimpleSuccessInnerDataJson(new SimpleData(
								"iframe", iframe));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return failureModel("该资源不支持外部分享").toJSON();
	}

	/**
	 * 预览页
	 * 
	 * @param uuid
	 * @param model
	 * @return
	 */
	@RequestAuthority(value = Role.NULL, logger = true)
	@RequestMapping(value = "/shared/{uuid}", method = RequestMethod.GET)
	public ModelAndView resourceShared(@PathVariable String uuid, Model model) {
		Resource resource = cacheResourceFacade.getResourceByUuid(uuid);
		model.addAttribute("resource", resource);
		return new ModelAndView("embed/resource_preview");
	}

	@RequestAuthority(value = Role.NULL, logger = true)
	@RequestMapping(value = "/shared/{uuid}/play", method = RequestMethod.GET)
	public ModelAndView resourcePlay(@PathVariable String uuid, Model model,
			HttpServletRequest request) {
		Resource res = resourceFacade.getResourceByUuid(uuid);
		String data_error = "";
		if (res == null || !res.isValid()) {
			return new ModelAndView("embed/error");
		}
		bulidRequest("embed资源 查看", res, uuid, request);
		model.addAttribute("title", res.getName());
		if (res.getType() == Type.PDF) {
			PdfInfo info = res.getPdf();
			int pageNum = Integer.parseInt(info.getPdfnum());
			String taskId = info.getTaskId();
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("taskId", taskId);
			RecourceDetailVo vo = new RecourceDetailVo();
			vo.buildPdfDetail(res);
			request.setAttribute("data_json", vo.toJSON());
			// 添加观看次数
			resourceFacade.countResourceView(res);
			return new ModelAndView("embed/pdf_view");
		} else if (res.getType() == Type.THREESCREEN) {
			// 编辑返回前台的数据
			bulidRequest("embed资源 查看", res, uuid, request);
			// 添加观看次数
			resourceFacade.countResourceView(res);
			FrontResourceItem vo = new FrontResourceItem();
			// 基本数据
			vo.buildItemList(res);
			FrontThreeScreenVo threeScreenVo = new FrontThreeScreenVo();
			threeScreenVo.buildData(res.getThreeScreen());
			vo.setThreeScreenVo(threeScreenVo);
			request.setAttribute("res_data", vo);
			return new ModelAndView("embed/three_screen_play");
		}
		return new ModelAndView("embed/error");

	}

	@RequestAuthority(value = Role.NULL, logger = true)
	@RequestMapping(value = "/article/{uuid}/preview", method = RequestMethod.GET)
	public ModelAndView articalPreview(@PathVariable String uuid, Model model,
			HttpServletRequest request) {
		Resource res = effectResourceFacade().getResourceByUuid(uuid);
		String error = null;
		if (res == null || !res.apiUseable()) {
			error = "资源下线或者不可用";
		} else if (res.getType() != Type.NEWS) {
			error = "无法预料";
		} else {

			model.addAttribute("news", res.getNews());
		}
		model.addAttribute("error", error);
		return new ModelAndView("embed/resource_article_preview");
	}

}
