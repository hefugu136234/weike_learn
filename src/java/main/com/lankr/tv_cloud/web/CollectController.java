/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月24日
 * 	@modifyDate 2016年5月24日
 *  
 */
package com.lankr.tv_cloud.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.codes.CodeProvider;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollectQuestionnaire;
import com.lankr.tv_cloud.model.Questionnaire;
import com.lankr.tv_cloud.model.NormalCollect.Type;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceGroup;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.ChosenItem;
import com.lankr.tv_cloud.vo.CollectQuestionnaireVo;
import com.lankr.tv_cloud.vo.DynamicSearchVo;
import com.lankr.tv_cloud.vo.QuestionnaireVo;
import com.lankr.tv_cloud.vo.api.BaseAPIModel.JsonConvertor;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;
import com.lankr.tv_cloud.vo.snapshot.CourseChaptersItem;
import com.lankr.tv_cloud.vo.snapshot.NormalCollectItem;
import com.lankr.tv_cloud.vo.snapshot.ResourceGroupItem;
import com.lankr.tv_cloud.vo.snapshot.SpeakerItem;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

/**
 * @author Kalean.Xiang
 */

@Controller
public class CollectController extends AdminWebController {
	private static final String COURSE_KEY = "course_key";
	private static final String CHAPTER_KEY = "chapter_key";
	private static final String CHAPTER_RES_KEY = "chapter_res_key";
	private static final String COMILATION_KEY = "compilation_key";
	private static final String COMILATION_RES_KEY = "compilation_res_key";
	private static final String CHAPTER_QUEATIONNAIRE_KEY = "chapter_questionnaire_key";
	
	@Autowired
	private CodeProvider codeProvider;
	
	@Autowired
	private DataSourceTransactionManager transactionManager ;

	/**
	 * @Description: 合集/课程 列表页面跳转
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/list/page")
	public ModelAndView listPage(Model model) {
		return index("/wrapped/collect/list.jsp");
	}

	/**
	 * @Description: 合集/课程 列表数据展示
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/datatable", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String collectList(HttpServletRequest request) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<NormalCollect> pagination = normalCollectFacade
				.searchPaginationNormalCollections(q, from, size, new Type[] {
						Type.COURSE, Type.GENERAL });
		DataTableModel<?> d = DataTableModel.makeInstance(
				pagination.getResults(), NormalCollectItem.class);
		d.setiTotalDisplayRecords(pagination.getTotal());
		d.setiTotalRecords(pagination.getPage_rows());
		return d.toJSON();
	}

	/**
	 * @Description: 添加课程页面跳转
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/add")
	public ModelAndView addCoursePage(Model model) {
		model.addAttribute("token", makeRepeatSubmitToken(COURSE_KEY));
		return index("/wrapped/collect/course/mgr.jsp");
	}

	/**
	 * @Description: 讲者下拉框
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, requiredProject = false, logger = false)
	@RequestMapping(value = "/project/speaker/{uuid}", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String speakerData(@PathVariable String uuid) {
		Speaker speaker = assetFacade.getSpeakerByUuid(uuid);
		SpeakerItem item = new SpeakerItem(speaker);
		return item.itemApiJson(true);
	}

	/**
	 * @Description: 课程更新页面
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/update/{uuid}")
	public ModelAndView updateCoursePage(
			@PathVariable(value = "uuid") String uuid, Model model) {
		String token = makeRepeatSubmitToken(COURSE_KEY);
		model.addAttribute("token", token);
		NormalCollect collect = normalCollectFacade
				.getNormalCollectByUuid(uuid);
		if (null == collect || !collect.isCourse()) {
			model.addAttribute("error", "操作失败，请稍后重试!");
			return index("/wrapped/collect/list.jsp");
		}
		model.addAttribute("collect", collect);
		MediaCentral mediaCourseWebCover = mediaCentralFacade
				.getNormalCollectMedia(collect,
						MediaCentral.SIGN_COURSE_WEB_COVER);
		if (null != mediaCourseWebCover) {
			model.addAttribute("media_web_cover", mediaCourseWebCover);
		}
		MediaCentral mediaCourseWechatBg = mediaCentralFacade
				.getNormalCollectMedia(collect,
						MediaCentral.SIGN_COURSE_WECHAT_BG);
		if (null != mediaCourseWebCover) {
			model.addAttribute("media_wechat_bg", mediaCourseWechatBg);
		}
		Speaker speaker = collect.getSpeaker();
		if (null != speaker) {
			ChosenItem item = DynamicSearchVo.speakerItem(speaker);
			model.addAttribute("chosenItem", item);
		}
		return index("/wrapped/collect/course/mgr.jsp");
	}

	/**
	 * @Description: 课程数据保存
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String saveCourse(
			@RequestParam(required = false) String uuid,
			@RequestParam String name,
			@RequestParam String token,
			@RequestParam String webCover,
			@RequestParam String wechatBg,
			@RequestParam String description,
			@RequestParam(value = "speaker_selector", required = false) String speakerUuid,
			@RequestParam(required = false) String mark) {
		String token_ = toastRepeatSubmitToken(COURSE_KEY);
		if (!token.equals(token_)) {
			return failureModel("重复提交，或者页面已过期，请刷新重试").toJSON();
		}
		NormalCollect normalCollect = normalCollectFacade
				.getNormalCollectByUuid(uuid);
		if (null == normalCollect) {
			normalCollect = new NormalCollect();
			normalCollect.setUuid(Tools.getUUID());
		}
		if (!Tools.isBlank(speakerUuid)) {
			Speaker speaker = assetFacade.getSpeakerByUuid(speakerUuid);
			normalCollect.setSpeaker(speaker);
		}
		normalCollect.setName(name);
		normalCollect.setMark(mark);
		normalCollect.setDescription(description);

		Map<Integer, String> covers = new HashMap<Integer, String>();
		covers.put(MediaCentral.SIGN_COURSE_WEB_COVER, webCover);
		covers.put(MediaCentral.SIGN_COURSE_WECHAT_BG, wechatBg);

		ActionMessage<?> aciton = normalCollectFacade.saveCourseCollect(
				normalCollect, covers);
		return actionModel(aciton).toJSON();
	}

	/**
	 * @Description: 合集/课程 状态更新
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/status", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String statusOperation(@RequestParam String uuid) {
		NormalCollect collect = normalCollectFacade
				.getNormalCollectByUuid(uuid);
		if (null == collect) {
			return failureModel("操作失败，请稍后重试!").toJSON();
		}
		collect.setStatus(collect.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
				: BaseModel.APPROVED);
		ActionMessage<?> action = normalCollectFacade.update(collect);
		if (!action.isSuccess())
			return failureModel(action.getMessage()).toJSON();

		List<NormalCollect> list = new ArrayList<NormalCollect>();
		list.add(collect);
		DataTableModel<?> data = DataTableModel.makeInstance(list,
				NormalCollectItem.class);
		return data.toJSON();
	}

	/**
	 * @Description: 课程/合集 删除
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/remove", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String courseRemove(@RequestParam String uuid) {
		NormalCollect collect = normalCollectFacade
				.getNormalCollectByUuid(uuid);
		if (null == collect) {
			return failureModel("操作失败，请稍后重试!").toJSON();
		}
		if (collect.isGeneralCollect()) {
			List<ResourceGroup> resourceGroups = resourceGroupFacade
					.getGeneralCollectResourceGroups(
							ResourceGroup.TYPE_GENERAL_COLLECT, collect.getId());
			if (resourceGroups.size() > 0)
				return failureModel("操作失败，该合集关联多个资源，请清除本合集下的资源后重试!").toJSON();
		}
		if (collect.isCourse()) {
			List<NormalCollect> list = normalCollectFacade
					.getChapterListByParentId(collect.getId());
			if (list.size() > 0)
				return failureModel("操作失败，该课程下含有多个章节，请清除本课程下的章节后重试!").toJSON();
		}
		collect.setIsActive(BaseModel.DISABLE);
		ActionMessage<?> action = normalCollectFacade.update(collect);
		if (!action.isSuccess())
			return failureModel(action.getMessage()).toJSON();
		return successModel("删除成功").toJSON();
	}

	/* =================== */

	/**
	 * @Description: 章节列表
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/list/page/{courseUuid}")
	public ModelAndView chaptersListPage(Model model,
			@PathVariable(value = "courseUuid") String courseUuid) {
		NormalCollect collect = normalCollectFacade
				.getNormalCollectByUuid(courseUuid);
		if (null == collect || !collect.isCourse()) {
			return index("/wrapped/collect/list.jsp");
		}
		model.addAttribute("collect", collect);
		return index("/wrapped/collect/course/chapters/list.jsp");
	}

	/**
	 * @Description: 章节列表数据
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/list/data/{courseUuid}", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String chaptersListData(HttpServletRequest request,
			@PathVariable(value = "courseUuid") String courseUuid) {
		NormalCollect collect = normalCollectFacade
				.getNormalCollectByUuid(courseUuid);
		if (null == collect || !collect.isCourse()) {
			return failureModel("操作失败，请稍后重试!").toJSON();
		}
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<NormalCollect> pagination = normalCollectFacade
				.searchPaginationCourseChapters(q, from, size, collect);
		DataTableModel<?> d = DataTableModel.makeInstance(
				pagination.getResults(), CourseChaptersItem.class);
		d.setiTotalDisplayRecords(pagination.getTotal());
		d.setiTotalRecords(pagination.getPage_rows());
		return d.toJSON();
	}

	/**
	 * @Description: 编辑章节
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/update/{chapterUuid}")
	public ModelAndView updateCourseChaptersPage(
			@PathVariable(value = "chapterUuid") String chapterUuid, Model model) {
		String token = makeRepeatSubmitToken(CHAPTER_KEY);
		model.addAttribute("token", token);
		NormalCollect chapter = normalCollectFacade
				.getNormalCollectByUuid(chapterUuid);
		if (null == chapter || !chapter.isCourseSegment()
				|| null == chapter.getParent()) {
			model.addAttribute("error", "操作失败，请稍后重试!");
			return index("/wrapped/collect/list.jsp");
		}
		model.addAttribute("chapter", chapter);
		model.addAttribute("course", chapter.getParent());
		if (chapter.getSign() == NormalCollect.getCourseSegmentSign(false,
				false, true)) {
			model.addAttribute("type", 1);
		} else if (chapter.getSign() == NormalCollect.getCourseSegmentSign(
				false, false, false)) {
			model.addAttribute("type", 0);
		}
		return index("/wrapped/collect/course/chapters/mgr.jsp");
	}

	/**
	 * @Description: 新建章节
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/add/page/{courseUuid}")
	public ModelAndView addCourseChaptersPage(Model model,
			@PathVariable(value = "courseUuid") String courseUuid) {
		NormalCollect course = normalCollectFacade
				.getNormalCollectByUuid(courseUuid);
		if (null == course || !course.isCourse()) {
			model.addAttribute("error", "操作失败，请稍后重试!");
			return index("/wrapped/collect/list.jsp");
		}
		model.addAttribute("token", makeRepeatSubmitToken(CHAPTER_KEY));
		model.addAttribute("course", course);
		return index("/wrapped/collect/course/chapters/mgr.jsp");
	}

	/**
	 * @Description: 章节数据保存
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String saveCourseChapters(
			@RequestParam(required = false) String chapterUuid,
			@RequestParam String courseUuid, @RequestParam String name,
			@RequestParam int passScore, @RequestParam int type,
			@RequestParam String token, @RequestParam String description,
			@RequestParam(required = false) String mark) {
		String token_ = toastRepeatSubmitToken(CHAPTER_KEY);
		if (!token.equals(token_)) {
			return failureModel("重复提交，或者页面已过期，请刷新重试").toJSON();
		}
		NormalCollect course = normalCollectFacade
				.getNormalCollectByUuid(courseUuid);
		if (null == course)
			return failureModel("操作失败，请稍后重试!").toJSON();

		boolean dependentPreviousPassed = false;
		if (1 == type)
			dependentPreviousPassed = true;

		NormalCollect chapter = normalCollectFacade
				.getNormalCollectByUuid(chapterUuid);
		if (null == chapter) {
			// chapter 为null，新增操作
			chapter = new NormalCollect();
			chapter.setUuid(Tools.getUUID());
		}
		chapter.setName(name);
		chapter.setMark(mark);
		chapter.setDescription(description);
		chapter.setPassScore(passScore);

		ActionMessage<?> aciton = normalCollectFacade.saveSegmentCollect(
				course, chapter, dependentPreviousPassed);
		return actionModel(aciton).toJSON();
	}

	/**
	 * @Description: 章节状态更新
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/status", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String chaptersStatusOperation(
			@RequestParam String uuid) {
		NormalCollect collect = normalCollectFacade
				.getNormalCollectByUuid(uuid);
		if (null == collect || !collect.isCourseSegment()) {
			return failureModel("操作失败，请稍后重试!").toJSON();
		}
		collect.setStatus(collect.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
				: BaseModel.APPROVED);
		ActionMessage<?> action = normalCollectFacade.update(collect);
		if (!action.isSuccess())
			return failureModel(action.getMessage()).toJSON();

		List<NormalCollect> list = new ArrayList<NormalCollect>();
		list.add(collect);
		DataTableModel<?> data = DataTableModel.makeInstance(list,
				CourseChaptersItem.class);
		return data.toJSON();
	}

	/**
	 * @Description: 章节删除
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/remove", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String courseChaptersRemove(@RequestParam String uuid) {
		NormalCollect collect = normalCollectFacade
				.getNormalCollectByUuid(uuid);
		if (null == collect || !collect.isCourseSegment()) {
			return failureModel("操作失败，请稍后重试!").toJSON();
		}
		List<ResourceGroup> resourceGroups = resourceGroupFacade
				.getCourseSementResourceGroups(
						ResourceGroup.TYPE_COURSE_SEGMENT, collect.getId());
		if (resourceGroups.size() > 0)
			return failureModel("操作失败，该章节关联多个资源，请清除本章节下的资源后重试!").toJSON();
		collect.setIsActive(BaseModel.DISABLE);
		ActionMessage<?> action = normalCollectFacade.update(collect);
		if (!action.isSuccess())
			return failureModel(action.getMessage()).toJSON();
		return successModel("删除成功").toJSON();
	}

	/**
	 * @Description: 章节置顶
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/top", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String courseChapterTop(@RequestParam String uuid) {
		NormalCollect chapter = normalCollectFacade
				.getNormalCollectByUuid(uuid);
		if (null == chapter || !chapter.isCourseSegment())
			return failureModel("操作失败，请稍后重试!").toJSON();
		ActionMessage<?> action = normalCollectFacade
				.normalCollectChapterTop(chapter);
		if (!action.isSuccess())
			return failureModel(action.getMessage()).toJSON();
		return successModel("操作成功").toJSON();
	}

	/* =================== */

	/**
	 * @Description: 章节资源列表
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/resource/list/page/{chapterUuid}")
	public ModelAndView chapterResListPage(Model model,
			@PathVariable(value = "chapterUuid") String chapterUuid) {
		NormalCollect chapter = normalCollectFacade
				.getNormalCollectByUuid(chapterUuid);
		if (null == chapter || !chapter.isCourseSegment()) {
			model.addAttribute("error", "操作失败，请稍后重试!");
			return index("/wrapped/collect/list.jsp");
		}
		NormalCollect course = chapter.getParent();
		if (null != course)
			model.addAttribute("course", course);
		model.addAttribute("chapter", chapter);
		return index("/wrapped/collect/course/chapters/resource/list.jsp");
	}

	/**
	 * @Description: 章节资源列表数据展示
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/resource/list/data/{chapterUuid}", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String chapterResListData(HttpServletRequest request,
			@PathVariable(value = "chapterUuid") String chapterUuid) {
		NormalCollect chapter = normalCollectFacade
				.getNormalCollectByUuid(chapterUuid);
		if (null == chapter || !chapter.isCourseSegment()) {
			return failureModel("操作失败，请稍后重试!").toJSON();
		}
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<ResourceGroup> pagination = resourceGroupFacade
				.searchPaginationChapterResource(q, from, size, chapter);
		DataTableModel<?> d = DataTableModel.makeInstance(
				pagination.getResults(), ResourceGroupItem.class);
		d.setiTotalDisplayRecords(pagination.getTotal());
		d.setiTotalRecords(pagination.getPage_rows());
		return d.toJSON();
	}

	/**
	 * @Description: 编辑章节资源
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/resource/update/page/{chapterResUuid}")
	public ModelAndView updateChapterResourcePage(
			@PathVariable(value = "chapterResUuid") String chapterResUuid,
			Model model) {
		String token = makeRepeatSubmitToken(CHAPTER_RES_KEY);
		model.addAttribute("token", token);
		ResourceGroup resourceGroup = resourceGroupFacade
				.getResourceGroupByUuid(chapterResUuid);
		if (null == resourceGroup
				|| ResourceGroup.TYPE_COURSE_SEGMENT != resourceGroup.getType()) {
			model.addAttribute("error", "操作失败，请稍后重试!");
			return index("/wrapped/collect/list.jsp");
		}
		NormalCollect chapter = normalCollectFacade
				.getNormalCollectById(resourceGroup.getReferId());
		if (null != chapter)
			model.addAttribute("chapter", chapter);
		Resource res = resourceGroup.getResource();
		if (null != res) {
			ChosenItem item = DynamicSearchVo.resourceItem(res);
			model.addAttribute("resourceItem", item);
		}
		model.addAttribute("resourceGroup", resourceGroup);
		return index("/wrapped/collect/course/chapters/resource/mgr.jsp");
	}

	/**
	 * @Description: 添加章节资源
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/resource/add/page/{chapterUuid}")
	public ModelAndView addChapterResourcePage(Model model,
			@PathVariable(value = "chapterUuid") String chapterUuid) {
		NormalCollect chapter = normalCollectFacade
				.getNormalCollectByUuid(chapterUuid);
		if (null == chapter || !chapter.isCourseSegment()) {
			model.addAttribute("error", "操作失败，请稍后重试!");
			return index("/wrapped/collect/list.jsp");
		}
		model.addAttribute("token", makeRepeatSubmitToken(CHAPTER_RES_KEY));
		model.addAttribute("chapter", chapter);
		return index("/wrapped/collect/course/chapters/resource/mgr.jsp");
	}

	/**
	 * @Description: 章节资源保存
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/resource/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String saveChapterResource(
			@RequestParam String chapterUuid,
			@RequestParam String resourceUuid,
			@RequestParam(required = false) String resourceGroupUuid,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String mark,
			@RequestParam String token) {
		String token_ = toastRepeatSubmitToken(CHAPTER_RES_KEY);
		if (!token.equals(token_)) {
			return failureModel("重复提交，或者页面已过期，请刷新重试").toJSON();
		}
		Resource resource = effectResourceFacade().getResourceByUuid(
				resourceUuid);
		if (null == resource)
			return failureModel("请选择要添加的资源!").toJSON();
		NormalCollect chapter = normalCollectFacade
				.getNormalCollectByUuid(chapterUuid);
		if (null == chapter || !chapter.isCourseSegment()) {
			return failureModel("操作失败，请稍后重试!").toJSON();
		}
		ResourceGroup resourceGroup = resourceGroupFacade
				.getResourceGroupByUuid(resourceGroupUuid);
		if (null == resourceGroup) {
			resourceGroup = new ResourceGroup();
			resourceGroup.setUuid(Tools.getUUID());
			resourceGroup.setStatus(BaseModel.UNAPPROVED);
			resourceGroup.setIsActive(BaseModel.ACTIVE);
		}
		resourceGroup.setResource(resource);
		resourceGroup.setName(StringUtils.isEmpty(name) ? resource.getName()
				: name);
		resourceGroup.setMark(StringUtils.isEmpty(mark) ? resource.getMark()
				: mark);
		resourceGroup.setReferId(chapter.getId());
		resourceGroup.setType(ResourceGroup.TYPE_COURSE_SEGMENT);

		ActionMessage<?> aciton = resourceGroupFacade
				.saveOrUpateNormalCollectResouce(resourceGroup);
		if (aciton.isSuccess())
			return BaseController.successModel("操作成功").toJSON();
		return actionModel(aciton).toJSON();
	}

	/**
	 * @Description: 章节资源状态更新
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/resource/status", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String chapterResStatusOperation(
			@RequestParam String uuid) {
		ResourceGroup resourceGroup = resourceGroupFacade
				.getResourceGroupByUuid(uuid);
		if (null == resourceGroup
				|| ResourceGroup.TYPE_COURSE_SEGMENT != resourceGroup.getType())
			return failureModel("操作失败，请稍后重试!").toJSON();
		resourceGroup
				.setStatus(resourceGroup.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
						: BaseModel.APPROVED);
		ActionMessage<?> action = resourceGroupFacade
				.saveOrUpateNormalCollectResouce(resourceGroup);
		if (!action.isSuccess())
			return failureModel(action.getMessage()).toJSON();
		List<ResourceGroup> list = new ArrayList<ResourceGroup>();
		list.add(resourceGroup);
		DataTableModel<?> data = DataTableModel.makeInstance(list,
				ResourceGroupItem.class);
		return data.toJSON();
	}

	/**
	 * @Description: 章节资源删除
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/resource/remove", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String courseChapterResRemove(@RequestParam String uuid) {
		ResourceGroup resourceGroup = resourceGroupFacade
				.getResourceGroupByUuid(uuid);
		if (null == resourceGroup
				|| ResourceGroup.TYPE_COURSE_SEGMENT != resourceGroup.getType())
			return failureModel("操作失败，请稍后重试!").toJSON();
		resourceGroup.setIsActive(BaseModel.DISABLE);
		ActionMessage<?> action = resourceGroupFacade
				.saveOrUpateNormalCollectResouce(resourceGroup);
		if (!action.isSuccess())
			return failureModel(action.getMessage()).toJSON();
		return successModel("删除成功").toJSON();
	}

	/**
	 * @Description: 章节资源置顶
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/resource/top", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String courseChapterResTop(@RequestParam String uuid) {
		ResourceGroup resourceGroup = resourceGroupFacade
				.getResourceGroupByUuid(uuid);
		if (null == resourceGroup
				|| ResourceGroup.TYPE_COURSE_SEGMENT != resourceGroup.getType())
			return failureModel("操作失败，请稍后重试!").toJSON();
		ActionMessage<?> action = resourceGroupFacade
				.normalCollectResouceTop(resourceGroup);
		if (!action.isSuccess())
			return failureModel(action.getMessage()).toJSON();
		return successModel("操作成功").toJSON();
	}

	/* =================== */

	/**
	 * @Description: 添加合集页面跳转
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/compilation/add")
	public ModelAndView addCompilationPage(Model model) {
		model.addAttribute("token", makeRepeatSubmitToken(COMILATION_KEY));
		return index("/wrapped/collect/compilation/mgr.jsp");
	}

	/**
	 * @Description: 合集更新页面跳转
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/compilation/update/{uuid}")
	public ModelAndView updateCompilationPage(
			@PathVariable(value = "uuid") String uuid, Model model) {
		String token = makeRepeatSubmitToken(COMILATION_KEY);
		model.addAttribute("token", token);
		NormalCollect collect = normalCollectFacade
				.getNormalCollectByUuid(uuid);
		if (null == collect || !collect.isGeneralCollect()) {
			model.addAttribute("error", "操作失败，请稍后重试!");
			return index("/wrapped/collect/list.jsp");
		}
		model.addAttribute("compilation", collect);
		MediaCentral media = mediaCentralFacade.getNormalCollectMedia(collect,
				MediaCentral.SIGN_COMPILATION_WEB_COVER);
		if (null != media) {
			model.addAttribute("media", media);
		}
		return index("/wrapped/collect/compilation/mgr.jsp");
	}

	/**
	 * @Description: 合集数据保存
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/compilation/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String saveCompilation(
			@RequestParam(required = false) String uuid,
			@RequestParam String name, @RequestParam String token,
			@RequestParam String webCover, @RequestParam String description,
			@RequestParam(required = false) String mark) {
		String token_ = toastRepeatSubmitToken(COMILATION_KEY);
		if (!token.equals(token_)) {
			return failureModel("重复提交，或者页面已过期，请刷新重试").toJSON();
		}

		NormalCollect normalCollect = normalCollectFacade
				.getNormalCollectByUuid(uuid);
		if (null == normalCollect) {
			normalCollect = new NormalCollect();
			normalCollect.setUuid(Tools.getUUID());
		}
		normalCollect.setName(name);
		normalCollect.setMark(mark);
		normalCollect.setDescription(description);

		Map<Integer, String> covers = new HashMap<Integer, String>();
		covers.put(MediaCentral.SIGN_COMPILATION_WEB_COVER, webCover);

		ActionMessage<?> aciton = normalCollectFacade.saveGeneralCollect(
				normalCollect, covers);
		return actionModel(aciton).toJSON();
	}

	/* =================== */

	/**
	 * @Description: 合集资源列表页面跳转
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/compilation/resource/list/page/{compilationUuid}")
	public ModelAndView compilationResListPage(Model model,
			@PathVariable(value = "compilationUuid") String compilationUuid) {
		NormalCollect compilation = normalCollectFacade
				.getNormalCollectByUuid(compilationUuid);
		if (null == compilation || !compilation.isGeneralCollect()) {
			model.addAttribute("error", "操作失败，请稍后重试!");
			return index("/wrapped/collect/list.jsp");
		}
		model.addAttribute("compilation", compilation);
		return index("/wrapped/collect/compilation/resource/list.jsp");
	}

	/**
	 * @Description: 合集资源列表数据
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/compilation/resource/list/data/{compilationUuid}", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	public @ResponseBody String compilationResListData(
			HttpServletRequest request,
			@PathVariable(value = "compilationUuid") String compilationUuid) {
		NormalCollect compilation = normalCollectFacade
				.getNormalCollectByUuid(compilationUuid);
		if (null == compilation || !compilation.isGeneralCollect()) {
			return failureModel("操作失败，请稍后重试!").toJSON();
		}
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<ResourceGroup> pagination = resourceGroupFacade
				.searchPaginationCompilationResource(q, from, size, compilation);
		DataTableModel<?> d = DataTableModel.makeInstance(
				pagination.getResults(), ResourceGroupItem.class);
		d.setiTotalDisplayRecords(pagination.getTotal());
		d.setiTotalRecords(pagination.getPage_rows());
		return d.toJSON();
	}

	/**
	 * @Description: 添加合集资源页面跳转
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/compilation/resource/add/page/{compilationUuid}")
	public ModelAndView addCompilationResourcePage(Model model,
			@PathVariable(value = "compilationUuid") String compilationUuid) {
		NormalCollect compilation = normalCollectFacade
				.getNormalCollectByUuid(compilationUuid);
		if (null == compilation || !compilation.isGeneralCollect()) {
			model.addAttribute("error", "操作失败，请稍后重试!");
			return index("/wrapped/collect/list.jsp");
		}
		model.addAttribute("token", makeRepeatSubmitToken(COMILATION_RES_KEY));
		model.addAttribute("compilation", compilation);
		return index("/wrapped/collect/compilation/resource/mgr.jsp");
	}

	/**
	 * @Description: 合集资源编辑页面跳转
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/compilation/resource/update/page/{compilationResUuid}")
	public ModelAndView updateCompilationResourcePage(
			@PathVariable(value = "compilationResUuid") String compilationResUuid,
			Model model) {
		String token = makeRepeatSubmitToken(COMILATION_RES_KEY);
		model.addAttribute("token", token);
		ResourceGroup resourceGroup = resourceGroupFacade
				.getResourceGroupByUuid(compilationResUuid);
		if (null == resourceGroup
				|| ResourceGroup.TYPE_GENERAL_COLLECT != resourceGroup
						.getType()) {
			model.addAttribute("error", "操作失败，请稍后重试!");
			return index("/wrapped/collect/list.jsp");
		} else
			model.addAttribute("resourceGroup", resourceGroup);
		NormalCollect compilation = normalCollectFacade
				.getNormalCollectById(resourceGroup.getReferId());
		if (null != compilation)
			model.addAttribute("compilation", compilation);
		Resource res = resourceGroup.getResource();
		if (null != res) {
			ChosenItem item = DynamicSearchVo.resourceItem(res);
			model.addAttribute("resourceItem", item);
		}
		return index("/wrapped/collect/compilation/resource/mgr.jsp");
	}

	/**
	 * @Description: 合集资源保存
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/compilation/resource/save", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String saveCompilationResource(
			@RequestParam String compilationUuid,
			@RequestParam String resourceUuid,
			@RequestParam(required = false) String resourceGroupUuid,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String mark,
			@RequestParam String token) {
		String token_ = toastRepeatSubmitToken(COMILATION_RES_KEY);
		if (!token.equals(token_)) {
			return failureModel("重复提交，或者页面已过期，请刷新重试").toJSON();
		}
		Resource resource = effectResourceFacade().getResourceByUuid(
				resourceUuid);
		if (null == resource)
			return failureModel("请选择要添加的资源!").toJSON();
		NormalCollect compilation = normalCollectFacade
				.getNormalCollectByUuid(compilationUuid);
		if (null == compilation || !compilation.isGeneralCollect()) {
			return failureModel("操作失败，请稍后重试!").toJSON();
		}
		ResourceGroup resourceGroup = resourceGroupFacade
				.getResourceGroupByUuid(resourceGroupUuid);
		if (null == resourceGroup) {
			resourceGroup = new ResourceGroup();
			resourceGroup.setUuid(Tools.getUUID());
			resourceGroup.setStatus(BaseModel.UNAPPROVED);
			resourceGroup.setIsActive(BaseModel.ACTIVE);
		}
		resourceGroup.setResource(resource);
		resourceGroup.setName(StringUtils.isEmpty(name) ? resource.getName()
				: name);
		resourceGroup.setMark(StringUtils.isEmpty(mark) ? resource.getMark()
				: mark);
		resourceGroup.setReferId(compilation.getId());
		resourceGroup.setType(ResourceGroup.TYPE_GENERAL_COLLECT);

		ActionMessage<?> aciton = resourceGroupFacade
				.saveOrUpateNormalCollectResouce(resourceGroup);
		if (aciton.isSuccess())
			return BaseController.successModel("操作成功").toJSON();
		return actionModel(aciton).toJSON();
	}

	/**
	 * @Description: 合集资源状态更新
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/compilation/resource/status", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String compilationResStatusOperation(
			@RequestParam String uuid) {
		ResourceGroup resourceGroup = resourceGroupFacade
				.getResourceGroupByUuid(uuid);
		if (null == resourceGroup
				|| ResourceGroup.TYPE_GENERAL_COLLECT != resourceGroup
						.getType())
			return failureModel("操作失败，请稍后重试!").toJSON();
		resourceGroup
				.setStatus(resourceGroup.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
						: BaseModel.APPROVED);
		ActionMessage<?> action = resourceGroupFacade
				.saveOrUpateNormalCollectResouce(resourceGroup);
		if (!action.isSuccess())
			return failureModel(action.getMessage()).toJSON();
		List<ResourceGroup> list = new ArrayList<ResourceGroup>();
		list.add(resourceGroup);
		DataTableModel<?> data = DataTableModel.makeInstance(list,
				ResourceGroupItem.class);
		return data.toJSON();
	}

	/**
	 * @Description: 合集资源删除
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/compilation/resource/remove", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String compilationChapterResRemove(
			@RequestParam String uuid) {
		ResourceGroup resourceGroup = resourceGroupFacade
				.getResourceGroupByUuid(uuid);
		if (null == resourceGroup
				|| ResourceGroup.TYPE_GENERAL_COLLECT != resourceGroup
						.getType())
			return failureModel("操作失败，请稍后重试!").toJSON();
		resourceGroup.setIsActive(BaseModel.DISABLE);
		ActionMessage<?> action = resourceGroupFacade
				.saveOrUpateNormalCollectResouce(resourceGroup);
		if (!action.isSuccess())
			return failureModel(action.getMessage()).toJSON();
		return successModel("删除成功").toJSON();
	}

	/**
	 * @Description: 合集资源置顶
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/compilation/resource/top", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	public @ResponseBody String compilationResTop(@RequestParam String uuid) {
		ResourceGroup resourceGroup = resourceGroupFacade
				.getResourceGroupByUuid(uuid);
		if (null == resourceGroup
				|| ResourceGroup.TYPE_GENERAL_COLLECT != resourceGroup
						.getType())
			return failureModel("操作失败，请稍后重试!").toJSON();
		ActionMessage<?> action = resourceGroupFacade
				.normalCollectResouceTop(resourceGroup);
		if (!action.isSuccess())
			return failureModel(action.getMessage()).toJSON();
		return successModel("操作成功").toJSON();
	}
	
	/**
	 * @Description: 选择章节试题
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/select/questionnaire", method = RequestMethod.GET, produces = "text/json;charset=utf-8")
	@ResponseBody
	public String selectCourseChaptersQuestionnaire(@RequestParam String uuid) {
		NormalCollect normalCollect = normalCollectFacade.getNormalCollectByUuid(uuid);
		if (normalCollect == null || !normalCollect.isActive()) {
			return failureModel("该章节不存在或已下线").toJSON(JsonConvertor.JACKSON);
		}
		NormalCollectQuestionnaire collect = normalCollectQuestionnaireFacade
				.selectQuestionnaireOne(normalCollect.getId(),
						NormalCollectQuestionnaire.TYPE_NORMALCOLLECT);
		CollectQuestionnaireVo colletQuestionnaire = new CollectQuestionnaireVo();
		List<Questionnaire> questionnaires = questionnaireFacade.selectAllQuestionnaire();
		colletQuestionnaire.buildData(normalCollect, collect, questionnaires);
		return colletQuestionnaire.toJSON();
	}
	
	/**
	 * @Description: 添加或修改章节试题
	 */
	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/project/collect/course/chapters/update/questionnaire", method = RequestMethod.POST, produces = "text/json;charset=utf-8")
	@ResponseBody
	public String UpdateCourseChaptersQuestionnaire(
			@RequestParam String uuid, @RequestParam String questUuid,
			@RequestParam String picUrl,@RequestParam String name,
			@RequestParam int collectTime,@RequestParam int collectNum,
			@RequestParam String mark) {
		NormalCollect normalCollect = normalCollectFacade.getNormalCollectByUuid(uuid);
		if (normalCollect == null || !normalCollect.isActive()) {
			return failureModel("该章节不存在或已下线").toJSON(JsonConvertor.JACKSON);
		}
		int nId = normalCollect.getId();
		
		Questionnaire questionnaire = questionnaireFacade.selectQuestionnaireByUuid(questUuid);
		if (questionnaire == null || !questionnaire.isActive()) {
			return failureModel("该试题不存在或已下线").toJSON(JsonConvertor.JACKSON);
		}
		int qId = questionnaire.getId();
		
		NormalCollectQuestionnaire normalCollectQuestionnaire = initNormalCollectQuestionnaire(normalCollect, 
				questionnaire, NormalCollectQuestionnaire.TYPE_NORMALCOLLECT,picUrl);
		normalCollectQuestionnaire.setName(name);
		normalCollectQuestionnaire.setMark(mark);
		normalCollectQuestionnaire.setCollectNum(collectNum);
		normalCollectQuestionnaire.setCollectTime(collectTime);
		ActionMessage action =  saveNormalCollectQuestionnaire(normalCollectQuestionnaire);
		if (action.getStatus() == Status.SUCCESS) {
			return successModel().toJSON();
		} else {
			return failureModel("操作失败").toJSON();
		}
	}

	private NormalCollectQuestionnaire initNormalCollectQuestionnaire(NormalCollect normalCollect,
			Questionnaire questionnaire, int type, String picUrl) {
		NormalCollectQuestionnaire cq = new NormalCollectQuestionnaire();
		cq.setQuestionnaire(questionnaire);
		cq.setNormalCollectId(normalCollect.getId());
		cq.setType(type);
		cq.setCover(picUrl);
		return cq;
	}

	private ActionMessage saveNormalCollectQuestionnaire(NormalCollectQuestionnaire normalCollectQuestionnaire) {
		NormalCollectQuestionnaire cq = normalCollectQuestionnaireFacade.selectByCollectAndQuestionnaire(normalCollectQuestionnaire.getNormalCollectId(),
				normalCollectQuestionnaire.getQuestionnaire().getId(), normalCollectQuestionnaire.getType());
		if (cq == null) {
			normalCollectQuestionnaire.setIsActive(BaseModel.ACTIVE);
			normalCollectQuestionnaire.setUuid(Tools.getUUID());
			ActionMessage<?> action = normalCollectQuestionnaireFacade.addNormalCollectQuestionnaire(normalCollectQuestionnaire);
			return action;
		} else {
			cq.setIsActive(BaseModel.ACTIVE);
			cq.setName(normalCollectQuestionnaire.getName());
			cq.setMark(normalCollectQuestionnaire.getMark());
			cq.setCover(normalCollectQuestionnaire.getCover());
			cq.setCollectNum(normalCollectQuestionnaire.getCollectNum());
			cq.setCollectTime(normalCollectQuestionnaire.getCollectTime());
			ActionMessage<?> action = normalCollectQuestionnaireFacade.updateNormalCollectQuestionnaire(cq);
			return action;
		}
	}



	// @RequestAuthority(value = Role.PRO_EDITOR, requiredProject = false,
	// logger = false)
	// @RequestMapping(value = "/project/category/{uuid}/{deep}", method =
	// RequestMethod.GET, produces = "text/json;charset=utf-8")
	// public @ResponseBody String categoryData(@PathVariable String uuid,
	// @PathVariable int deep) {
	// Category c = assetFacade.getCategoryByUuid(uuid);
	// CategoryItem item = new CategoryItem(c, deep);
	// return item.itemApiJson(true);
	// }
	//
	// @RequestAuthority(value = Role.PRO_EDITOR, requiredProject = false,
	// logger = false)
	// @RequestMapping(value = "/project/resource/{uuid}", method =
	// RequestMethod.GET, produces = "text/json;charset=utf-8")
	// public @ResponseBody String resourceData(@PathVariable String uuid) {
	//
	// Resource res = effectResourceFacade().getResourceByUuid(uuid);
	//
	// ResourceSimpleItem item = new ResourceSimpleItem(res);
	// String data = item.itemApiJson(true);
	// // SendMessageToMQ.sendMessage("resource", "update", data) ;
	// return item.itemApiJson(true);
	// }
}
