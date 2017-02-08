package com.lankr.tv_cloud.web;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.model.TagParent;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.BannerSurface;
import com.lankr.tv_cloud.vo.BannerVo;
import com.lankr.tv_cloud.vo.TagChildSurface;
import com.lankr.tv_cloud.vo.TagParentSurface;
import com.lankr.tv_cloud.vo.TagParentVo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * @author mayuan Tag 管理
 */

@SuppressWarnings("all")
@Controller
@RequestMapping(value = "/tag")
public class TagController extends AdminWebController {

	private static final String TAGSAVE_KEY = "tagSave_key";
	private static final String TAGUPDATE_KEY = "tagUpdate_key";
	private static final String P_UUID = "parent_uuid";
	private static final String P_ID = "parent_id";
	private static final String P_NAME = "parent_name";

	/**
	 * 显示父标签列表
	 * 
	 * @param request
	 * @return /wrapped/tag/tagParent_list.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/showParentTagPag")
	public ModelAndView jumpToParentTagPage(HttpServletRequest request) {
		return index(request, "/wrapped/tag/tagParent_list.jsp");
	}

	/**
	 * 跳转到添加父标签页面
	 * 
	 * @param request
	 * @return /wrapped/tag/tagParent_add.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/addParentTagPage", method = RequestMethod.GET)
	public ModelAndView jumpToAddParentTagPage(HttpServletRequest request) {
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.getSession().setAttribute(TAGSAVE_KEY, token);
		return index(request, "/wrapped/tag/tagParent_add.jsp");
	}

	/**
	 * 新建父标签
	 * 
	 * @param request
	 * @param name
	 * @param mark
	 * @param token
	 * @return status
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/parentTagSave", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String saveParentTag(HttpServletRequest request,
			@RequestParam String name, @RequestParam String mark,
			@RequestParam String token) {

		// 表单重复提交检查
		String repeat_token = toastRepeatSubmitToken(request, TAGSAVE_KEY);
		if (StringUtils.isNotEmpty(token) && !token.equals(repeat_token))
			return BaseController.failureModel("该页面已被提交过，请尝试刷新页面").toJSON();

		TagParent tParent = new TagParent();
		// 设置初始状态为 success
		Status status = Status.SUCCESS;
		try {
			tParent.setUuid(Tools.getUUID());
			tParent.setIsActive(BaseModel.APPROVED);
			tParent.setMark(Tools.nullValueFilter(mark));
			tParent.setName(Tools.nullValueFilter(name));
			tParent.setPingyin(Tools.getPinYin(name));
			// 执行保存数据
			status = tagFacade.saveParentTag(tParent);
		} catch (Exception e) {
			e.printStackTrace();
			BaseController.bulidRequest("新增标签", "tag_parent", tParent.getId(),
					Status.FAILURE.message(), null, "失败", request);

			return BaseController.failureModel("添加父标签失败，请重试").toJSON();
		}
		if (Status.SUCCESS.equals(status)) {
			BaseController.bulidRequest("新增父标签", "tag_parent", tParent.getId(),
					Status.SUCCESS.message(), null, "成功", request);
		}
		return getStatusJson(status);
	}

	/**
	 * 显示父标签列表数据
	 * 
	 * @param request
	 * @return json
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/showParentTagList", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String showParentTagList(HttpServletRequest request) {
		// 获取前台datatable传递的查询参数
		String searchValue = nullValueFilter(request.getParameter("sSearch"));
		Object iDisplayLength = request.getParameter("iDisplayLength");
		int pageSize = iDisplayLength == null ? 10 : Integer
				.valueOf((String) iDisplayLength);
		Object iDisplayStart = request.getParameter("iDisplayStart");
		int startPage = iDisplayStart == null ? 0 : Integer
				.valueOf((String) iDisplayStart);

		TagParentSurface suface = new TagParentSurface();
		try {
			// 查询列表
			Pagination<TagParent> tagParentList = tagFacade.queryParentTagList(
					searchValue, startPage, pageSize);
			suface.setiTotalDisplayRecords(tagParentList.getTotal());
			suface.setiTotalRecords(tagParentList.getPage_rows());
			suface.buildParenData(tagParentList.getResults());
			suface.setStatus(Status.SUCCESS);
			BaseController.bulidRequest("后台查询父标签列表", "tagParent", null,
					Status.SUCCESS.message(), null, "成功", request);
			return gson.toJson(suface);
		} catch (Exception e) {
			e.printStackTrace();
			suface.setStatus(Status.SAVE_ERROR);
			BaseController.bulidRequest("后台查询父标签列表", "tagParent", null,
					Status.FAILURE.message(), null, "失败", request);
		}
		return gson.toJson(suface);
	}

	/**
	 * 跳转到子标签列表
	 * 
	 * @param request
	 * @param parent_uuid
	 * @return /wrapped/tag/tagChild_list.jsp
	 */
	@RequestMapping(value = "/showChildTagPag/{parent_uuid}")
	public ModelAndView jumpToShowChildTagPage(HttpServletRequest request,
			@PathVariable String parent_uuid) {
		if (StringUtils.isNotEmpty(parent_uuid)) {
			TagParent tParent = tagFacade.selectParentTagByUuid(parent_uuid);
			if (null != tParent) {
				int parent_id = tParent.getId();
				String parent_name = tParent.getName();
				request.setAttribute(P_ID, parent_id);
				request.setAttribute(P_NAME, parent_name);
			}
		}
		request.setAttribute(P_UUID, parent_uuid);
		return index(request, "/wrapped/tag/tagChild_list.jsp");
	}

	/**
	 * 点击父标签显示相应的子标签
	 * 
	 * @param request
	 * @param parent_uuid
	 * @return json
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/showChildTagList/{parent_uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String showChildTagList(HttpServletRequest request,
			@PathVariable String parent_uuid) {
		// 获取前台datatable传递的查询参数
		String searchValue = nullValueFilter(request.getParameter("sSearch"));
		Object iDisplayLength = request.getParameter("iDisplayLength");
		int pageSize = iDisplayLength == null ? 10 : Integer
				.valueOf((String) iDisplayLength);
		Object iDisplayStart = request.getParameter("iDisplayStart");
		int startPage = iDisplayStart == null ? 0 : Integer
				.valueOf((String) iDisplayStart);
		int parent_id = 0;
		if (StringUtils.isNotEmpty(parent_uuid)) {
			TagParent tParent = tagFacade.selectParentTagByUuid(parent_uuid);
			if (null != tParent) {
				parent_id = tParent.getId();
				parent_uuid = tParent.getUuid();
				request.setAttribute(P_ID, parent_id);
				request.setAttribute(P_UUID, parent_uuid);
			}
		}
		TagChildSurface childTagSurface = new TagChildSurface();
		try {
			// 查询列表
			Pagination<TagChild> childTagList = tagFacade.queryChildTagList(
					searchValue, startPage, pageSize, parent_id);
			childTagSurface.setiTotalDisplayRecords(childTagList.getTotal());
			childTagSurface.setiTotalRecords(childTagList.getPage_rows());
			childTagSurface.buildChildData(childTagList.getResults());
			childTagSurface.setStatus(Status.SUCCESS);
			BaseController.bulidRequest("后台查询父标签对应的子标签列表", "tagParent", null,
					Status.SUCCESS.message(), null, "成功", request);
			return gson.toJson(childTagSurface);
		} catch (Exception e) {
			e.printStackTrace();
			childTagSurface.setStatus(Status.SAVE_ERROR);
			BaseController.bulidRequest("后台查询父标签对应的子标签列表", "tagParent", null,
					Status.FAILURE.message(), null, "失败", request);
		}
		return gson.toJson(childTagSurface);
	}

	/**
	 * 删除父标签
	 * 
	 * @param request
	 * @param uuid
	 * @return Status
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/deleteParentTag", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String deleteBanner(HttpServletRequest request,
			@RequestParam String uuid) {
		TagParent tagParent = tagFacade.selectParentTagByUuid(uuid);
		if (null == tagParent || !tagParent.isActive()) {
			BaseController.bulidRequest("后台删除父标签", "tags_parent", null,
					Status.FAILURE.message(), null, "uuid=" + uuid + " 该标签不存在",
					request);
			return BaseController.failureModel("该标签不存在！").toJSON();
		}
		List<TagChild> childs = tagFacade.queryChildTagListWithoutPageOption(tagParent.getId());
		if(childs != null && childs.size() > 0){
			return BaseController.failureModel("该分类下存在多个标签，请先清除该分类下的标签再进行操作！").toJSON(); 
		}
		Status status = tagFacade.deleteParentTag(tagParent);
		if (Status.SUCCESS != status) {
			return BaseController.failureModel("删除父标签失败，请重试").toJSON();
		}
		return getStatusJson(Status.SUCCESS);
	}

	/**
	 * 跳转到添加子标签页面
	 * 
	 * @param request
	 * @param parent_id
	 * @param parent_uuid
	 * @return /wrapped/tag/tagChild_add.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/addChildTagPage/{parent_id}/{parent_uuid}", method = RequestMethod.GET)
	public ModelAndView jumpToAddChildTagPage(HttpServletRequest request,
			@PathVariable String parent_id, @PathVariable String parent_uuid) {
		String token = Tools.getUUID();
		request.setAttribute("token", token);
		request.getSession().setAttribute(TAGSAVE_KEY, token);
		request.setAttribute(P_ID, parent_id);
		request.setAttribute(P_UUID, parent_uuid);
		return index(request, "/wrapped/tag/tagChild_add.jsp");
	}

	/**
	 * 添加子标签
	 * 
	 * @param request
	 * @param name
	 * @param mark
	 * @param token
	 * @param parent_id
	 * @return Status
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/childTagSave", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String saveChildTag(HttpServletRequest request,
			@RequestParam String name, @RequestParam String mark,
			@RequestParam String token, @RequestParam int parent_id) {

		// 表单重复提交检查
		String repeat_token = toastRepeatSubmitToken(request, TAGSAVE_KEY);
		if (StringUtils.isNotEmpty(token) && !token.equals(repeat_token))
			return BaseController.failureModel("该页面已被提交过，请尝试刷新页面").toJSON();

		TagChild tChild = new TagChild();
		// 设置初始状态为 success
		Status status = Status.SUCCESS;
		try {
			tChild.setUuid(Tools.getUUID());
			tChild.setIsActive(BaseModel.APPROVED);
			tChild.setMark(Tools.nullValueFilter(mark));
			tChild.setName(Tools.nullValueFilter(name));
			tChild.setPingyin(Tools.getPinYin(name));
			tChild.setParent_id(parent_id);
			// 执行保存数据
			status = tagFacade.saveChildTag(tChild);
		} catch (Exception e) {
			e.printStackTrace();
			BaseController.bulidRequest("新增子标签", "tags_child", tChild.getId(),
					Status.FAILURE.message(), null, "失败", request);

			return BaseController.failureModel("添加标签失败，请重试").toJSON();
		}
		if (Status.SUCCESS.equals(status)) {
			BaseController.bulidRequest("新增子标签", "tags_child", tChild.getId(),
					Status.SUCCESS.message(), null, "成功", request);
		}
		return getStatusJson(status);
	}

	/**
	 * 删除子标签
	 * 
	 * @param request
	 * @param uuid
	 * @return Status
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/deleteChildTag", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String deleteChildTag(HttpServletRequest request,
			@RequestParam String uuid) {
		TagChild tagChild = tagFacade.selectChildTagByUuid(uuid);
		if (null == tagChild || !tagChild.isActive()) {
			BaseController.bulidRequest("后台删除父标签", "tags_parent", null,
					Status.FAILURE.message(), null, "uuid=" + uuid + " 该标签不存在",
					request);
			return BaseController.failureModel("该标签不存在！").toJSON();
		}
		Status status = tagFacade.deleteChildTag(tagChild);
		if (Status.SUCCESS != status) {
			return BaseController.failureModel("删除父标签失败，请重试").toJSON();
		}
		return getStatusJson(Status.SUCCESS);
	}

	/**
	 * 跳转到父标签编辑页面
	 * 
	 * @param request
	 * @param uuid
	 * @return /wrapped/tag/tagParent_update.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/updateParentTagPage/{uuid}", method = RequestMethod.GET)
	public ModelAndView jumpToUpdateParentTagPage(HttpServletRequest request,
			@PathVariable String uuid) {
		TagParent tagParent = tagFacade.selectParentTagByUuid(uuid);
		if (null == tagParent || !tagParent.isActive()) {
			BaseController.bulidRequest("编辑父标签", "tags_parent", null,
					Status.FAILURE.message(), null, "uuid=" + uuid + " 该标签不存在",
					request);
			return this.jumpToParentTagPage(request);
		} else {
			String token = Tools.getUUID();
			request.setAttribute("token", token);
			request.getSession().setAttribute(TAGUPDATE_KEY, token);

			/*
			 * TagParentVo parentVo = new TagParentVo();
			 * parentVo.buildData(tagParent);
			 */
			request.setAttribute("parentVo", tagParent);
		}
		return index(request, "/wrapped/tag/tagParent_update.jsp");
	}

	/**
	 * 编辑父标签
	 * 
	 * @param request
	 * @param token
	 * @param name
	 * @param mark
	 * @param uuid
	 * @return Status
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/updateParentTag", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String updateParentTag(HttpServletRequest request,
			@RequestParam String token, @RequestParam String name,
			@RequestParam String mark, @RequestParam String uuid) {
		// 表单重复提交检查
		String repeat_token = toastRepeatSubmitToken(request, TAGUPDATE_KEY);
		if (StringUtils.isNotEmpty(token) && !token.equals(repeat_token))
			return BaseController.failureModel("该页面已被提交过，请尝试刷新页面").toJSON();

		TagParent tagParent = tagFacade.selectParentTagByUuid(uuid);
		if (null == tagParent || !tagParent.isActive()) {
			BaseController.bulidRequest("编辑父标签", "tags_parent", null,
					Status.FAILURE.message(), null, "uuid=" + uuid + " 该标签不存在",
					request);
			return getStatusJson(Status.FAILURE);
		} else {
			tagParent.setMark(Tools.nullValueFilter(mark));
			tagParent.setName(Tools.nullValueFilter(name));
			tagParent.setPingyin(Tools.getPinYin(name));
			Status status = tagFacade.updateParentTag(tagParent);
			if (Status.SUCCESS.equals(status)) {
				BaseController.bulidRequest("更新父标签", "banner",
						tagParent.getId(), Status.SUCCESS.message(), null,
						"成功", request);
			} else {
				BaseController.bulidRequest("更新父标签", "banner",
						tagParent.getId(), Status.FAILURE.message(), null,
						"失败", request);
				return BaseController.failureModel("编辑标签失败，请重试").toJSON();
			}
		}
		return getStatusJson(Status.SUCCESS);
	}

	/**
	 * 跳转到子标签编辑页面
	 * 
	 * @param request
	 * @param uuid
	 * @param p_uuid
	 * @return /wrapped/tag/tagChild_update.jsp
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/updateChildTagPage/{uuid}/{p_uuid}", method = RequestMethod.GET)
	public ModelAndView jumpToUpdateChildTagPage(HttpServletRequest request,
			@PathVariable String uuid, @PathVariable String p_uuid) {
		TagChild tagChild = tagFacade.selectChildTagByUuid(uuid);
		if (null == tagChild || !tagChild.isActive()) {
			BaseController.bulidRequest("编辑子标签", "tags_parent", null,
					Status.FAILURE.message(), null, "uuid=" + uuid + " 该标签不存在",
					request);
			return this.jumpToParentTagPage(request);
		} else {
			String token = Tools.getUUID();
			request.setAttribute("token", token);
			request.getSession().setAttribute(TAGUPDATE_KEY, token);

			/*
			 * TagParentVo parentVo = new TagParentVo();
			 * parentVo.buildData(tagParent);
			 */
			request.setAttribute("childVo", tagChild);
			request.setAttribute(P_UUID, p_uuid);
		}
		return index(request, "/wrapped/tag/tagChild_update.jsp");
	}

	/**
	 * 编辑子标签
	 * 
	 * @param request
	 * @param token
	 * @param name
	 * @param mark
	 * @param uuid
	 * @return Status
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/updateChildTag", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String updateChildTag(HttpServletRequest request,
			@RequestParam String token, @RequestParam String name,
			@RequestParam String mark, @RequestParam String uuid) {
		// 表单重复提交检查
		String repeat_token = toastRepeatSubmitToken(request, TAGUPDATE_KEY);
		if (StringUtils.isNotEmpty(token) && !token.equals(repeat_token))
			return BaseController.failureModel("该页面已被提交过，请尝试刷新页面").toJSON();

		TagChild tagChild = tagFacade.selectChildTagByUuid(uuid);
		if (null == tagChild || !tagChild.isActive()) {
			BaseController.bulidRequest("编辑子标签", "tags_parent", null,
					Status.FAILURE.message(), null, "uuid=" + uuid + " 该标签不存在",
					request);
			return getStatusJson(Status.FAILURE);
		} else {
			tagChild.setMark(Tools.nullValueFilter(mark));
			tagChild.setName(Tools.nullValueFilter(name));
			tagChild.setPingyin(Tools.getPinYin(name));
			Status status = tagFacade.updateChildTag(tagChild);
			if (Status.SUCCESS.equals(status)) {
				BaseController.bulidRequest("更新子标签", "banner",
						tagChild.getId(), Status.SUCCESS.message(), null, "成功",
						request);
			} else {
				BaseController.bulidRequest("更新子标签", "banner",
						tagChild.getId(), Status.FAILURE.message(), null, "失败",
						request);
				return BaseController.failureModel("编辑子标签失败，请重试").toJSON();
			}
		}
		return getStatusJson(Status.SUCCESS);
	}
	
	/*
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value="tagNameValid", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String validTagName(HttpServletRequest request, @RequestParam String tagName){
		if(StringUtils.isNotEmpty(tagName)){
			boolean parent = tagFacade.selectParentTagByName(tagName);
			boolean child = tagFacade.selectChildTagByName(tagName);
			if(parent && child){
				return getStatusJson(Status.SUCCESS);
			}
		}
		return getStatusJson(Status.FAILURE);
	}*/
	
	/**
	 * 显示父标签列表数据（无分页）
	 * 
	 * @param request
	 * @return json
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/showParentTagListWithoutPageOption", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String showParentTagListWithutPageOption(HttpServletRequest request) {
		TagParentSurface suface = new TagParentSurface();
		try {
			// 查询列表
			suface.buildParenData(tagFacade.queryParentTagListWithoutPageOption());
			suface.setStatus(Status.SUCCESS);
			BaseController.bulidRequest("资源关联标签，查询父标签列表", "tagParent", null,
					Status.SUCCESS.message(), null, "成功", request);
			return gson.toJson(suface);
		} catch (Exception e) {
			e.printStackTrace();
			suface.setStatus(Status.SAVE_ERROR);
			BaseController.bulidRequest("资源关联标签，查询父标签列表", "tagParent", null,
					Status.FAILURE.message(), null, "失败", request);
		}
		return gson.toJson(suface);
	}

	/**
	 * 点击父标签显示相应的子标签（无分页）
	 * 
	 * @param request
	 * @param parent_uuid
	 * @return json
	 */
	@RequestAuthority(value = Role.PRO_EDITOR)
	@RequestMapping(value = "/showChildTagListWithoutPageOption/{parent_uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String showChildTagListWithoutPageOption(HttpServletRequest request,
			@PathVariable String parent_uuid) {
		int parent_id = 0;
		if (StringUtils.isNotEmpty(parent_uuid)) {
			TagParent tParent = tagFacade.selectParentTagByUuid(parent_uuid);
			parent_id = tParent.getId();
		}
		TagChildSurface childTagSurface = new TagChildSurface();
		try {
			// 查询列表
			childTagSurface.buildChildData(tagFacade.queryChildTagListWithoutPageOption(parent_id));
			childTagSurface.setStatus(Status.SUCCESS);
			BaseController.bulidRequest("后台查询父标签对应的子标签列表", "tagParent", null,
					Status.SUCCESS.message(), null, "成功", request);
			return gson.toJson(childTagSurface);
		} catch (Exception e) {
			e.printStackTrace();
			childTagSurface.setStatus(Status.SAVE_ERROR);
			BaseController.bulidRequest("后台查询父标签对应的子标签列表", "tagParent", null,
					Status.FAILURE.message(), null, "失败", request);
		}
		return gson.toJson(childTagSurface);
	}
}
