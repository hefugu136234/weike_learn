package com.lankr.tv_cloud.web.front;

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
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollectQuestionnaire;
import com.lankr.tv_cloud.model.NormalCollectSchedule;
import com.lankr.tv_cloud.model.Questionnaire;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceGroup;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.Questionnaire.QuestionnaireProperty;
import com.lankr.tv_cloud.support.wenjuan.WenJuanUntil;
import com.lankr.tv_cloud.vo.api.BaseAPIModel.JsonConvertor;
import com.lankr.tv_cloud.web.api.webchat.vo.WxCoursePackageItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxCoursePackageListVo;
import com.lankr.tv_cloud.web.api.webchat.vo.WxExamineItem;
import com.lankr.tv_cloud.web.api.webchat.vo.WxSignature;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseFrontController.PC_PRIOR)
public class FrontCourseController extends BaseFrontController {
	/**
	 * 2016-08-02 pc端课程包
	 */

	// pc端课程包列表
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/course/package/list/page", method = RequestMethod.GET)
	public ModelAndView coursePackageListPage(HttpServletRequest request) {
		bulidRequest("web课程包列表", null, null, Status.SUCCESS.message(), null,
				"成功", request);
		// 课程 banner;
		getCommonBanner(request, Banner.POSITION_COURSE);

		Pagination<NormalCollect> pagination = normalCollectFacade
				.webCourseList(0, 10, NormalCollect.SIGN_TYPE_COURSE);
		WxCoursePackageListVo vo = new WxCoursePackageListVo();
		User user = getCurrentUser(request);
		vo.buildFrontData(pagination, mediaCentralFacade,normalCollectScheduleFacade,user);

		request.setAttribute("vo_data", vo);
		return redirectPageView("web/package/list");
	}

	
	
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/course/package/list/data", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String coursePackageListData(HttpServletRequest request,
			@RequestParam int size, @RequestParam int currentPage) {
		int from = Math.max(currentPage - 1, 0);
		if (size > 10) {
			size = 10;
		}
		from = from * size;
		Pagination<NormalCollect> pagination = normalCollectFacade
				.webCourseList(from, size, NormalCollect.SIGN_TYPE_COURSE);
		User user = getCurrentUser(request);
		WxCoursePackageListVo vo = new WxCoursePackageListVo();
		vo.buildFrontData(pagination, mediaCentralFacade,normalCollectScheduleFacade,user);
		return vo.toJSON();
	}

	
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/course/package/detail/{uuid}", method = RequestMethod.GET)
	public ModelAndView coursePackageDetail(HttpServletRequest request,
			@PathVariable String uuid) {
		NormalCollect normalCollect = normalCollectFacade
				.getNormalCollectByUuid(uuid);
		if (normalCollect == null || !normalCollect.apiUseable()) {
			return redirectErrorPage(request, "此课程包不存在或已下线");
		}
		// SIGN_TYPE_COURSE_SEGMENT
		bulidRequest("web课程包详情", "normal_collect", normalCollect.getId(),
				Status.SUCCESS.message(), null, "成功", request);
		User user = getCurrentUser(request);

		WxCoursePackageItem vo = new WxCoursePackageItem();
		vo.buildDetail(normalCollect, mediaCentralFacade, user);
		
		// 是否学习课程
		NormalCollectSchedule normalCollectSchedule = normalCollectScheduleFacade
				.selectCourseScheduleByUser(normalCollect, user);
		vo.buildLearn(normalCollectSchedule);

		// WxUserShowInfo info = wxGetUserinfo(user);
		// vo.buildUserData(info);
		// 课程包下所有资源的学些进度
		List<NormalCollectSchedule> schedules = normalCollectScheduleFacade
				.selectReScheduleByCourseUser(normalCollect, user);
		// 章节
		List<NormalCollect> list = normalCollectFacade.wxChapterList(
				NormalCollect.SIGN_TYPE_COURSE_SEGMENT, normalCollect.getId());
		vo.buildChapterList(list, resourceGroupFacade, praiseFacade, schedules,
				user, normalCollectQuestionnaireFacade,
				normalCollectScheduleFacade);

		// 更新学习进度
		pageRemainFacade.updateCourseSchedule(vo.getLearnSchedule(),
				normalCollect, user);

		request.setAttribute("vo_data", vo);
		return redirectPageView("web/package/show");
	}

	// 课程包中的课程学习
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/course/learn/{uuid}", method = RequestMethod.GET)
	public ModelAndView courseLearn(HttpServletRequest request,
			@PathVariable String uuid) {
		ResourceGroup resourceGroup = resourceGroupFacade
				.getResourceGroupByUuid(uuid);
		if (resourceGroup == null || !resourceGroup.apiUseable()) {
			return redirectErrorPage(request, "此课程不存在或已下线");
		}

		// bulidRequest("微信课程包详情", "resource_group", resourceGroup.getId(),
		// Status.SUCCESS.message(), null, "成功", request);
		Resource resource = resourceGroup.getResource();
		return resorceDetailCommon(request, resource);
	}
	
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/course/study/action", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String courseStudy(HttpServletRequest request,
			@RequestParam String uuid) {
		User user=getCurrentUser(request);
		if(user==null){
			return failureModel("not login").toJSON();
		}
		NormalCollect normalCollect = normalCollectFacade
				.getNormalCollectByUuid(uuid);
		if (normalCollect == null || !normalCollect.apiUseable()) {
			return failureModel("此课程包不存在或已下线").toJSON();
		}
		NormalCollectSchedule normalCollectSchedule = normalCollectScheduleFacade
				.selectCourseScheduleByUser(normalCollect, user);
		if(normalCollectSchedule==null){
			return failureModel("学习此课程失败").toJSON();
		}
		if(normalCollectSchedule.getStudyStatus()==NormalCollectSchedule.STUDY_INIT){
			if(normalCollectSchedule.getLearnSchedule()==100){
				normalCollectSchedule.setStudyStatus(NormalCollectSchedule.STUDY_FINISH);
			}else{
				normalCollectSchedule.setStudyStatus(NormalCollectSchedule.STUDY_ING);
			}
			normalCollectScheduleFacade.updateCourseSchedule(normalCollectSchedule, normalCollect);
		}
		return successModel(String.valueOf(normalCollectSchedule.getLearnSchedule())).toJSON();
	}
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/course/chapter/examine/detail/{uuid}", produces = "text/json; charset=utf-8", method = RequestMethod.GET)
	@ResponseBody
	public String courseChapterExamine(HttpServletRequest request,
			@PathVariable String uuid) {
		NormalCollectQuestionnaire normalCollectQuestionnaire = normalCollectQuestionnaireFacade
				.selectQuestionnaireOneByUuid(uuid);
		if (normalCollectQuestionnaire == null) {
			return failureModel("此章节考试不存在或已下线").toJSON();
		}
		NormalCollect normalCollect = normalCollectFacade
				.getNormalCollectById(normalCollectQuestionnaire
						.getNormalCollectId());
		WxExamineItem item = new WxExamineItem();
		item.buildDetail(normalCollectQuestionnaire, normalCollect);
		return item.toJSON();
	}
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/normalCollect/questionnaire", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String buildQuestionnaire(@RequestParam String uuid,
			@RequestParam String redirect_uri, HttpServletRequest request) {
		User user = getCurrentUser(request);
		if (user == null || !user.isActive()) {
			return failureModel("not login").toJSON();
		}
		NormalCollectQuestionnaire collect = normalCollectQuestionnaireFacade
				.selectQuestionnaireOneByUuid(uuid);
		if (collect != null && !collect.apiUseable()) {
			Questionnaire questionnaire = collect.getQuestionnaire();
			QuestionnaireProperty questionnaireProperty = Questionnaire
					.JsonForObject(questionnaire.getqProperty());
			String repeat = "1";
			if (questionnaireProperty != null) {
				repeat = questionnaireProperty.getRepeat();
			}
			String apiurl = WenJuanUntil.wxViewLink(user.getUuid(),
					questionnaire.getUrlLink(), repeat, redirect_uri,
					collect.getUuid());
			return successModel(apiurl).toJSON();
		}
		return failureModel("该章节没有考试").toJSON(JsonConvertor.JACKSON);
	}

}
