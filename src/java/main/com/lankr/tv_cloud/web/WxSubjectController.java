package com.lankr.tv_cloud.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jdk.nashorn.internal.objects.annotations.Where;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityExpert;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.CategoryExpand;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.model.WxSubject.WxSubjectProperty;
import com.lankr.tv_cloud.support.qiniu.QiniuUtils;
import com.lankr.tv_cloud.support.qiniu.storage.BucketManager;
import com.lankr.tv_cloud.support.qiniu.storage.model.DefaultPutRet;
import com.lankr.tv_cloud.support.qiniu.util.Auth;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.CategoryExpandVo;
import com.lankr.tv_cloud.vo.OptionAdditionList;
import com.lankr.tv_cloud.vo.WxSubjectListVo;
import com.lankr.tv_cloud.vo.WxSubjectSurface;
import com.lankr.tv_cloud.vo.WxSubjectVo;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = "/project/wx/subject")
public class WxSubjectController extends AdminWebController {

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/list/page")
	public ModelAndView listPage(HttpServletRequest request) {
		BaseController.bulidRequest("后台查看查看微信学科分类列表", "wx_subject", null,
				Status.SUCCESS.message(), null, "成功", request);
		return index("/wrapped/wx_subject/subject_list.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/list/data")
	public @ResponseBody String listData(HttpServletRequest request) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<WxSubject> results = wxSubjectFacade
				.searchWxSubjectForTable(q, WxSubject.ROOT, from, size);
		WxSubjectSurface data = new WxSubjectSurface();
		data.buildList(results.getResults(), assetFacade);
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	/**
	 * 一级学科增加
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/first/add/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String subjectAddData(
			@RequestParam String categoryUuid, @RequestParam int rootType,
			@RequestParam String name, @RequestParam String cover,
			@RequestParam int type, HttpServletRequest request) {
		Category category = assetFacade.getCategoryByUuid(categoryUuid);
		if (category == null) {
			return failureModel("分类不存在，请重新选择").toJSON();
		}
		WxSubject subject = new WxSubject();
		subject.setUuid(Tools.getUUID());
		subject.setName(name);
		subject.setPinyin(Tools.getPinYin(name));
		subject.setReflectId(category.getId());
		subject.setRootType(rootType);
		subject.setIsRoot(WxSubject.ROOT);
		subject.setStatus(BaseModel.UNAPPROVED);
		subject.setIsActive(BaseModel.ACTIVE);
		WxSubjectProperty property = subject.instanceWxSubjectProperty(type,
				cover);
		String json = WxSubject.typeJsonToStirng(property);
		subject.setTypeProperty(json);
		Status status = wxSubjectFacade.addWxSubject(subject);
		if (status == Status.SUCCESS) {
			return successModel().toJSON();
		}
		return failureModel("保存失败").toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/update/need/page/data", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String updateNeedPage(@RequestParam String uuid,
			HttpServletRequest request) {
		WxSubject subject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		if (subject == null || !subject.isActive()) {
			return failureModel("此学科不存在").toJSON();
		}
		WxSubjectVo vo = new WxSubjectVo();
		vo.setStatus(Status.SUCCESS);
		if (subject.getRootType() == WxSubject.TYPE_CATEGORY) {
			Category category=assetFacade.getCategoryById(subject.getReflectId());
			vo.buildFirstUpdate(subject, category);
		}else if (subject.getRootType() == WxSubject.TYPE_ACTIVITY) {
			 Activity activity = activityFacade.getActivityById(subject.getReflectId());
			 vo.buildActivityUpdate(subject, activity);
		}
		return vo.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/update/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String dataUpdate(@RequestParam String uuid,
			@RequestParam(required = false) String reflectUuid,
			@RequestParam String name, @RequestParam String cover,
			@RequestParam int type, HttpServletRequest request) {
		WxSubject subject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		if (subject == null || !subject.isActive()) {
			return failureModel("此学科不存在").toJSON();
		}
		Category category = null;
		if (subject.getRootType() == WxSubject.TYPE_CATEGORY) {
			category = assetFacade.getCategoryByUuid(reflectUuid);
			if (category == null) {
				return failureModel("分类不存在，请重新选择").toJSON();
			}
			subject.setReflectId(category.getId());
		} else if (subject.getRootType() == WxSubject.TYPE_ACTIVITY) {
			// Activity activity = activityFacade.getByUuid(reflectUuid);
			// reflectId=activity.getId();
		}

		subject.setName(name);
		subject.setPinyin(Tools.getPinYin(name));
		WxSubjectProperty property = subject.instanceWxSubjectProperty(type,
				cover);
		String json = WxSubject.typeJsonToStirng(property);
		subject.setTypeProperty(json);
		Status status = wxSubjectFacade.updateWxSubject(subject);
		WxSubjectVo vo = new WxSubjectVo();
		if (status == Status.SUCCESS) {
			vo.setStatus(status);
			if (subject.getRootType() == WxSubject.TYPE_CATEGORY) {
				vo.buildFirstList(subject, category);
			} else if (subject.getRootType() == WxSubject.TYPE_ACTIVITY) {
				vo.buildActivityList(subject);
			}
			return vo.toJSON();
		}
		return failureModel("更新失败").toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/status/update", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String statusUpdate(@RequestParam String uuid,
			HttpServletRequest request) {
		WxSubject subject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		if (subject == null || !subject.isActive()) {
			return failureModel("此学科不存在").toJSON();
		}
		if (subject.getStatus() == BaseModel.UNAPPROVED) {
			subject.setStatus(BaseModel.APPROVED);
		} else {
			subject.setStatus(subject.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
					: BaseModel.APPROVED);
		}
		Status status = wxSubjectFacade.updateWxSubjectStatus(subject);
		if (status == Status.SUCCESS) {
			WxSubjectVo vo = new WxSubjectVo();
			vo.setStatus(status);
			if (subject.getRootType() == WxSubject.TYPE_CATEGORY) {
				Category category = assetFacade.getCategoryById(subject
						.getReflectId());
				vo.buildFirstList(subject, category);
			} else if (subject.getRootType() == WxSubject.TYPE_ACTIVITY) {
				vo.buildActivityList(subject);
			}
			return vo.toJSON();
		}
		return failureModel("更新状态失败").toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/delete/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String deleteData(@RequestParam String uuid,
			HttpServletRequest request) {
		WxSubject subject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		if (subject == null || !subject.isActive()) {
			return failureModel("此学科不存在").toJSON();
		}
		Status status = wxSubjectFacade.deteleWxSubject(subject);
		if (status == Status.SUCCESS) {
			return successModel().toJSON();
		}
		return failureModel("删除失败").toJSON();
	}

	/**
	 * 学科的子页面定制
	 * 
	 * @param request
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "sub/list/page/{uuid}")
	public ModelAndView subListPage(HttpServletRequest request,
			@PathVariable String uuid) {
		WxSubject subject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		request.setAttribute("sub_name",
				OptionalUtils.traceValue(subject, "name"));
		request.setAttribute("sub_uuid", uuid);
		return index("/wrapped/wx_subject/sub_subject_index.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/sub/list/data/{uuid}", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String sublistData(HttpServletRequest request,
			@PathVariable String uuid, @RequestParam int rootType) {
		WxSubject subject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		int id = OptionalUtils.traceInt(subject, "id");
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<WxSubject> results = wxSubjectFacade
				.searchWxSubjectChildrenForTable(q, id, from, size, rootType);
		WxSubjectSurface data = new WxSubjectSurface();
		data.buildSubList(results.getResults(), assetFacade, rootType);
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/get/activity", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String getActivity(HttpServletRequest request,
			@RequestParam String q) {
		OptionAdditionList optionAdditionList = new OptionAdditionList();
		List<Activity> activityList = activityFacade
				.searchActivityByWxSubject(q);
		optionAdditionList.buildActivityList(activityList);
		optionAdditionList.setStatus(Status.SUCCESS);
		return optionAdditionList.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/get/activity/cover", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String getActivityCover(HttpServletRequest request,
			@RequestParam String uuid) {
		Activity activity = activityFacade.getByUuid(uuid);
		WxSubjectVo vo = new WxSubjectVo();
		vo.buildActivityCover(activity);
		return vo.toJSON();
	}

	/**
	 * 学科子分类和总分类公共新增
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/common/add/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String commonAddData(@RequestParam String name,
			@RequestParam String cover, @RequestParam int type,
			@RequestParam int rootType, @RequestParam String reflectUuid,
			@RequestParam(required = false) String parentUuid,
			HttpServletRequest request) {
		int reflectId = 0;
		if (rootType == WxSubject.TYPE_CATEGORY) {
			Category category = assetFacade.getCategoryByUuid(reflectUuid);
			if (category == null) {
				return failureModel("分类不存在，请重新选择").toJSON();
			}
			reflectId = category.getId();
		} else if (rootType == WxSubject.TYPE_ACTIVITY) {
			Activity activity = activityFacade.getByUuid(reflectUuid);
			if (activity == null || !activity.apiUseable()) {
				return failureModel("活动不存在，或已下线").toJSON();
			}
			reflectId = activity.getId();
		}
		
		WxSubject subject = new WxSubject();
		subject.setUuid(Tools.getUUID());
		subject.setName(name);
		subject.setPinyin(Tools.getPinYin(name));
		subject.setReflectId(reflectId);
		subject.setRootType(rootType);
		subject.setStatus(BaseModel.UNAPPROVED);
		subject.setIsActive(BaseModel.ACTIVE);
		WxSubject parent = null;
		if (parentUuid != null) {
			parent = wxSubjectFacade.selectwxSubjectByUuid(parentUuid);
		}
		subject.setParent(parent);
		
		if(subject.getParent()==null){
			subject.setIsRoot(WxSubject.ROOT);
		}else{
			subject.setIsRoot(WxSubject.NOT_TOOT);
		}
		
		WxSubjectProperty property = subject.instanceWxSubjectProperty(type,
				cover);
		String json = WxSubject.typeJsonToStirng(property);
		subject.setTypeProperty(json);
		Status status = wxSubjectFacade.addWxSubject(subject);
		if (status == Status.SUCCESS) {
			return successModel().toJSON();
		}
		return failureModel("保存失败").toJSON();
	}
	
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/orgin/cover", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String wxSubjectOrgin(HttpServletRequest request,
			@RequestParam String uuid) {
		Category category = assetFacade.getCategoryByUuid(uuid);
		if (category == null || !category.isActive()) {
			return failureModel("分类不存在或不可用").toJSON();
		}
		MediaCentral mediaCentral = mediaCentralFacade.getCategoryMedia(category, MediaCentral.SIGN_WX_COVER);
		String cover=OptionalUtils.traceValue(mediaCentral, "url");
		if(!cover.isEmpty()){
			return successModel(cover).toJSON();
		}
		cover=OptionalUtils.traceValue(category, "expand.wxTaskId");
		cover=WxUtil.getResourceCover(cover);
		if(cover.isEmpty()){
			return successModel(cover).toJSON();
		}
		if(cover.contains("cloud.lankr.cn")){
			//上传七牛下载
			String forntImageUrl = null;
			try {
				Auth auth = Auth.create(Config.qn_access_key, Config.qn_secret_key);
				BucketManager bucketManager = new BucketManager(auth);
				DefaultPutRet defaultPutRet = bucketManager.fetch(cover,
						QiniuUtils.DEF_BUCKET);
				if (defaultPutRet != null && defaultPutRet.key != null
						&& !defaultPutRet.key.isEmpty()) {
					forntImageUrl = Config.qn_cdn_host + "/" + defaultPutRet.key;
					return successModel(forntImageUrl).toJSON();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return successModel("").toJSON();
	}
	
	/**
	 * @Description: 学科置顶
	 *
	 * @author mayuan
	 * @createDate 2016年7月1日
	 * @modifyDate 2016年7月1日
	 */
	@RequestAuthority(value = Role.PRO_ADMIN)
	@RequestMapping(value = "/recommend", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String recommendSubject(
			@RequestParam String uuid, HttpServletRequest request) {
		WxSubject subject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		if (null == subject) {
			return failureModel("该记录有误，请刷新页面重新尝试").toJSON();
		}
		ActionMessage action = wxSubjectFacade.recommendSubject(subject);
		if (action.isSuccess()) {
			BaseController.bulidRequest("后台置顶学科", "activity_expert", null,
					Status.SUCCESS.message(), null, "成功", request);
			return successModel().toJSON();
		}
		return actionModel(action).toJSON();
	}
	
	/**
	 * 获取更目录，及可以成为的二级目录
	 * @param request
	 * @param uuid
	 * @return
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/get/abled/parent", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@ResponseBody
	public String getAbledParent(HttpServletRequest request,
			@RequestParam String uuid) {
		//此目录为根目录
		WxSubject subject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		if (subject == null || !subject.isActive()) {
			return failureModel("此学科不存在").toJSON();
		}
		int parentId=0;
		WxSubject parent=subject.getParent();
		while(parent!=null){
			parentId=parent.getId();
			parent=parent.getParent();
		}
		List<WxSubject> list=wxSubjectFacade.getAbledParentSubject(parentId);
		WxSubjectListVo vo=new WxSubjectListVo();
		vo.buildList(subject, list);
		return vo.toJSON();
	}
	
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/change/parent", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String changeParent(
			@RequestParam String parentuuid,@RequestParam String uuid,HttpServletRequest request) {
		WxSubject subject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		if (subject == null || !subject.isActive()) {
			return failureModel("此学科不存在").toJSON();
		}
		WxSubject parent=wxSubjectFacade.selectwxSubjectByUuid(parentuuid);
		if (parent == null || !parent.isActive()) {
			return failureModel("修改的父目录学科不存在").toJSON();
		}
		subject.setParent(parent);
		Status status = wxSubjectFacade.updateSubjectParent(subject);
		if (status == Status.SUCCESS) {
			return successModel().toJSON();
		}
		return failureModel("修改失败").toJSON();
	}

}
