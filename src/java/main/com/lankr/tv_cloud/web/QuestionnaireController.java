package com.lankr.tv_cloud.web;

import java.io.Console;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;
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
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Questionnaire;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.Questionnaire.QuestionnaireProperty;
import com.lankr.tv_cloud.model.QuestionnaireAnswer;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.support.wenjuan.WenJuanUntil;
import com.lankr.tv_cloud.utils.Md5;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.AnswerViewDetail;
import com.lankr.tv_cloud.vo.DynamicSearchVo;
import com.lankr.tv_cloud.vo.QuestionnaireSurface;
import com.lankr.tv_cloud.vo.QuestionnaireVo;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = "/project/questionnaire")
public class QuestionnaireController extends AdminWebController {

	/**
	 * 2016-05-17 问卷相关
	 */

	@RequestAuthority(value = Role.PRO_EDITOR, logger = true)
	@RequestMapping(value = "/list/page")
	public ModelAndView listPage(HttpServletRequest request) {
		BaseController.bulidRequest("后台查看问卷列表", "wx_subject", null,
				Status.SUCCESS.message(), null, "成功", request);
		return index("/wrapped/questionnaire/questionnaire_list.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/list/data")
	public @ResponseBody String listData(HttpServletRequest request) {
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<Questionnaire> results = questionnaireFacade
				.searchQuestionnaireForTable(q, from, size);
		QuestionnaireSurface data = new QuestionnaireSurface();
		data.buildList(results.getResults());
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/add/page")
	public ModelAndView addPage(HttpServletRequest request) {
		return index("/wrapped/questionnaire/questionnaire_add.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/add/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String addData(@RequestParam String name,
			@RequestParam String mark, @RequestParam String urlLink,
			@RequestParam(required = false) String repeat,
			HttpServletRequest request) {
		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setUuid(Tools.getUUID());
		questionnaire.setName(name);
		questionnaire.setPinyin(Tools.getPinYin(name));
		questionnaire.setUrlLink(urlLink);
		questionnaire.setStatus(BaseModel.UNAPPROVED);
		questionnaire.setIsActive(BaseModel.ACTIVE);
		questionnaire.setMark(mark);
		if (repeat == null) {
			repeat = QuestionnaireProperty.No_REPEAT;
		}
		QuestionnaireProperty questionnaireProperty = questionnaire
				.instanceQProperty(repeat);
		String qProperty = Questionnaire.JsonToStirng(questionnaireProperty);
		questionnaire.setqProperty(qProperty);
		ActionMessage<?> actionMessage = questionnaireFacade
				.addQuestionnaire(questionnaire);
		return actionModel(actionMessage).toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/update/page/{uuid}")
	public ModelAndView updatePage(HttpServletRequest request,
			@PathVariable String uuid) {
		Questionnaire questionnaire = questionnaireFacade
				.selectQuestionnaireByUuid(uuid);
		if (questionnaire == null || !questionnaire.isActive()) {
			request.setAttribute("error", "此问卷不存在");
			// 跳回list
			return index("/wrapped/questionnaire/questionnaire_list.jsp");
		}
		QuestionnaireVo vo = new QuestionnaireVo();
		vo.updateNeedDate(questionnaire);
		request.setAttribute("ques_data", vo);
		return index("/wrapped/questionnaire/questionnaire_update.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/update/data", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String dataUpdate(@RequestParam String uuid,
			@RequestParam String name, @RequestParam String mark,
			HttpServletRequest request) {
		Questionnaire questionnaire = questionnaireFacade
				.selectQuestionnaireByUuid(uuid);
		if (questionnaire == null || !questionnaire.isActive()) {
			return failureModel("此问卷不存在").toJSON();
		}
		questionnaire.setName(name);
		questionnaire.setPinyin(Tools.getPinYin(name));
		questionnaire.setMark(mark);
		ActionMessage<?> actionMessage = questionnaireFacade
				.updateQuestionnaire(questionnaire);
		return actionModel(actionMessage).toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/update/status", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public @ResponseBody String statusUpdate(@RequestParam String uuid,
			HttpServletRequest request) {
		Questionnaire questionnaire = questionnaireFacade
				.selectQuestionnaireByUuid(uuid);
		if (questionnaire == null || !questionnaire.isActive()) {
			return failureModel("此问卷不存在").toJSON();
		}
		if (questionnaire.getStatus() == BaseModel.UNAPPROVED) {
			questionnaire.setStatus(BaseModel.APPROVED);
		} else {
			questionnaire
					.setStatus(questionnaire.getStatus() == BaseModel.APPROVED ? BaseModel.UNDERLINE
							: BaseModel.APPROVED);
		}
		ActionMessage<?> actionMessage = questionnaireFacade
				.updateQuestionnaireStatus(questionnaire);
		if (actionMessage.isSuccess()) {
			QuestionnaireVo vo = new QuestionnaireVo();
			vo.setStatus(Status.SUCCESS);
			vo.buildTableList(questionnaire);
			return vo.toJSON();
		}
		return actionModel(actionMessage).toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/answer/list/page/{uuid}")
	public ModelAndView answerListPage(HttpServletRequest request,
			@PathVariable String uuid) {
		request.setAttribute("uuid", uuid);
		Questionnaire questionnaire = questionnaireFacade
				.selectQuestionnaireByUuid(uuid);
		request.setAttribute("name",
				OptionalUtils.traceValue(questionnaire, "name"));
		return index("/wrapped/questionnaire/questionnaire_answer_list.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/answer/list/data/{uuid}")
	public @ResponseBody String answerListData(HttpServletRequest request,
			@PathVariable String uuid) {
		Questionnaire questionnaire = questionnaireFacade
				.selectQuestionnaireByUuid(uuid);
		int id = OptionalUtils.traceInt(questionnaire, "id");
		String q = nullValueFilter(request.getParameter("sSearch"));
		Object sizeObj = request.getParameter("iDisplayLength");
		int size = sizeObj == null ? 10 : Integer.valueOf((String) sizeObj);
		Object fromObj = request.getParameter("iDisplayStart");
		int from = fromObj == null ? 0 : Integer.valueOf((String) fromObj);
		Pagination<QuestionnaireAnswer> results = questionnaireFacade
				.searchQuestionnaireAnswerForTable(id, q, from, size);
		QuestionnaireSurface data = new QuestionnaireSurface();
		data.buildAnswerList(results.getResults(), certificationFacade);
		data.setiTotalDisplayRecords(results.getTotal());
		data.setiTotalRecords(results.getPage_rows());
		return data.toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/answer/view/{uuid}/{pro_uuid}")
	public ModelAndView answerView(HttpServletRequest request,
			@PathVariable String uuid, @PathVariable String pro_uuid) {
		System.out.println("pro_uuid:" + pro_uuid);
		request.setAttribute("pro_uuid", pro_uuid);
		QuestionnaireAnswer questionnaireAnswer = questionnaireFacade
				.selectQuestionnaireAnswerByUuid(uuid);
		if (questionnaireAnswer == null || !questionnaireAnswer.isActive()) {
			request.setAttribute("error", "此问卷不存在");
			// 跳回list
			return index("/wrapped/questionnaire/questionnaire_answer_view.jsp");
		}
		AnswerViewDetail vo = new AnswerViewDetail();
		vo.buildAnswerView(questionnaireAnswer);
		request.setAttribute("answer", vo);
		return index("/wrapped/questionnaire/questionnaire_answer_view.jsp");
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/wenjuan/link", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String wenJuanLink(HttpServletRequest request) {
		String apiUrl = WenJuanUntil.thirdLogin();
		return successModel(apiUrl).toJSON();
	}

	@RequestAuthority(value = Role.PRO_EDITOR, logger = false)
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	public @ResponseBody String searchQuestionnaire(HttpServletRequest request,
			@RequestParam String q) {
		// 根据search最多搜索15条
		List<Questionnaire> list = questionnaireFacade.selectQuestionnaireByQ(q);
		DynamicSearchVo search=DynamicSearchVo.buildQuestionnaire(list, q);
		return search.toJSON();
	}
	
//	@RequestAuthority(requiredProject = false, logger = false)
//	@RequestMapping(value = "/wenjuan/action/test", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
//	@ResponseBody
//	public String adviseRetroactionTest(HttpServletRequest request) {
//		Questionnaire questionnaire = questionnaireFacade
//				.selectQuesByName("问卷考试");
//		if (questionnaire == null) {
//			return failureModel("暂无此内容").toJSON();
//		}
//		User user = userFacade.getUserByUuid("4a015043-13b4-4320-a92b-1e453cf33277");
//		QuestionnaireProperty questionnaireProperty = Questionnaire
//				.JsonForObject(questionnaire.getqProperty());
//		String repeat = "1";
//		if (questionnaireProperty != null) {
//			repeat = questionnaireProperty.getRepeat();
//		}
//		String apiurl = WenJuanUntil.wxViewLink(user.getUuid(),
//				questionnaire.getUrlLink(), repeat, Config.host+"/f/web/index");
//		return successModel(apiurl).toJSON();
//	}
	public static void main(String[] args) {
		String val = "2016-05-18 15:26:00" + "helankr@lankr.cn" + "99999"
				+ "heshineng" + "9d15a674a6e621058f1ea9171413b7c0";
		System.out.println(Md5.getMd5String(val));
		long time1=Long.parseLong("1470211659217");
		long time2=Long.parseLong("1470211568330");
		time1=time1-time2;
		System.out.println(TimeUnit.MILLISECONDS.toSeconds(time1));
	}

}
