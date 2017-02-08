package com.lankr.tv_cloud.web.api.webchat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.web.api.webchat.vo.WxMenuItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxResourceList;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR + "/v2")
public class TempController extends BaseWechatController {

	/**
	 * 首页进入分类的二级目录
	 * 
	 * @param request
	 * @return
	 */
//	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
//	@RequestMapping(value = "/index/second/category/{uuid}", method = RequestMethod.GET)
//	public ModelAndView secondCategory(HttpServletRequest request,
//			@PathVariable String uuid) {
//		WxSubject wxSubject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
//		if (wxSubject == null || !wxSubject.apiUseable()) {
//			return redirectErrorPage(request, "此学科分类不存在或下线");
//		}
//		Category category = assetFacade.getCategoryById(wxSubject
//				.getReflectId());
//		if (category == null) {
//			return redirectErrorPage(request, "此学科分类不存在或下线");
//		}
//		WxMenuItem item = new WxMenuItem();
//		item.buildbSimpleData(wxSubject);
//		
//		List<WxSubject> activityChildrenList = wxSubjectFacade
//				.searchWxSubjectChildrenByWx(wxSubject.getId(),
//						WxSubject.TYPE_ACTIVITY);
//		item.buildSecondActivity(activityChildrenList, activityFacade);
//
//		// 子分类
//		List<WxSubject> subjectChildrenList = wxSubjectFacade
//				.searchWxSubjectChildrenByWx(wxSubject.getId(),
//						WxSubject.TYPE_CATEGORY);
//		item.buildSecondSubject(subjectChildrenList, effectResourceFacade());
//
//		request.setAttribute("vo_data", item);
//		bulidRequest("微信查看2级分类学科", "category", category.getId(),
//				Status.SUCCESS.message(), null, "成功", request);
//		return redirectPageView("wechat/subject/second_subject");
//	}

	/**
	 * 资源的列表页面
	 * 
	 * @param request
	 * @param uuid
	 * @param cateName
	 * @return 2016-04-27 之后有时间做分页处理 2016-06-24 @he 修改
	 */
	@RequestAuthority(requiredProject = true, logger = true, wxShareType = WxSignature.WX_COMMON_SHARE)
	@RequestMapping(value = "/home/list/page/{uuid}", method = RequestMethod.GET)
	public ModelAndView homeList(HttpServletRequest request,
			@PathVariable String uuid) {
		WxSubject wxSubject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		if (wxSubject == null || !wxSubject.apiUseable()) {
			return redirectErrorPage(request, "此学科分类不存在或下线");
		}
		Category category = assetFacade.getCategoryById(wxSubject
				.getReflectId());
		if (category == null) {
			return redirectErrorPage(request, "此学科分类不存在或下线");
		}
		bulidRequest("微信根据分类查看资源列表", "category", category.getId(),
				Status.SUCCESS.message(), null, "分类:" + category.getName(),
				request);
		WxMenuItem item = new WxMenuItem();
		item.setUuid(uuid);
		item.setName(wxSubject.getName());
		int resourceCount = effectResourceFacade().resourceCountByCategory(
				new ArrayList<Integer>(Arrays.asList(category.getId())));
		item.setResCount(resourceCount);
		request.setAttribute("vo_data", item);
		return redirectPageView("wechat/resource/resource_list");
	}

	/**
	 * @he 2016-06-24 根据分类分页查询资源
	 */
	@RequestAuthority(requiredProject = true, logger = false)
	@RequestMapping(value = "/resource/list/{uuid}", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String resourceList(HttpServletRequest request,
			@PathVariable String uuid, @RequestParam String startTime,
			@RequestParam int size) {
		WxSubject wxSubject = wxSubjectFacade.selectwxSubjectByUuid(uuid);
		if (wxSubject == null || !wxSubject.apiUseable()) {
			return failureModel("此学科分类不存在或下线").toJSON();
		}
		Category category = assetFacade.getCategoryById(wxSubject
				.getReflectId());
		if (category == null) {
			return failureModel("此学科分类不存在或下线").toJSON();
		}
		if (size > 10) {
			size = 10;
		}
		List<Resource> resources = effectResourceFacade().resourceWxPage(
				category.getId(), startTime, size);
		WxResourceList vo = new WxResourceList();
		vo.build(resources);
		return vo.toJSON();
	}
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/test/city", method = RequestMethod.GET)
	public ModelAndView testCity(HttpServletRequest request) {
		return redirectPageView("wechat/test/test");
	}

}
