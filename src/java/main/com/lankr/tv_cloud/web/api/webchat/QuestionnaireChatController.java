package com.lankr.tv_cloud.web.api.webchat;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Questionnaire;
import com.lankr.tv_cloud.model.Questionnaire.QuestionnaireProperty;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.support.wenjuan.WenJuanUntil;
import com.lankr.tv_cloud.web.BaseController;
import com.lankr.tv_cloud.web.auth.RequestAuthority;

@Controller
@RequestMapping(value = BaseWechatController.WX_PRIOR)
public class QuestionnaireChatController extends BaseWechatController {

	/**
	 * 微信问卷网
	 */
	/**
	 * 问卷网答题完毕的回调
	 * 
	 * @param request
	 * @return
	 * @he 2016-08-03 问卷网 v3接口 wj_respondent=user wj_short_id=proj_id
	 *     wj_respondent_status=status wj_timestamp=time wj_signature=md5
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/wenjuan/entrance", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String wenjuanCallBack(HttpServletRequest request,
			@RequestParam String chapter, @RequestParam String wj_respondent,
			@RequestParam String wj_short_id,
			@RequestParam String wj_respondent_status,
			@RequestParam String wj_timestamp, @RequestParam String wj_signature) {
		if (!wj_respondent_status.equals("1")) {
			return failureModel().toJSON();
		}
		Questionnaire questionnaire = questionnaireFacade
				.selectQuesByProId(wj_short_id);
		if (questionnaire != null) {
			User user = userFacade.getUserByUuid(wj_respondent);
			if (user != null) {
				// 拉取答案
				ActionMessage<?> actionMessage = questionnaireFacade
						.pullAnswerAndSave(questionnaire, user,chapter);
				if (actionMessage.isSuccess()) {
					request.setAttribute(BaseController.LOG_USERID,
							user.getId());
					bulidRequest("回答问卷", "questionnaire_answer", null,
							Status.SUCCESS.message(), questionnaire.getName(),
							"成功", request);
				}
			}
		}
		return successModel().toJSON();
	}

	/**
	 * 2016-05-19 暂时 之后要修改 意见反馈
	 */
//	@RequestAuthority(requiredProject = true, logger = false)
//	@RequestMapping(value = "/advise/retroaction", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
//	@ResponseBody
//	public String adviseRetroaction(HttpServletRequest request,
//			@RequestParam String wenjuan_name, @RequestParam String redirect_uri) {
//		Questionnaire questionnaire = questionnaireFacade
//				.selectQuesByName(wenjuan_name);
//		if (questionnaire == null) {
//			return failureModel("暂无此内容").toJSON();
//		}
//		User user = getCurrentUser(request);
//		QuestionnaireProperty questionnaireProperty = Questionnaire
//				.JsonForObject(questionnaire.getqProperty());
//		String repeat = "1";
//		if (questionnaireProperty != null) {
//			repeat = questionnaireProperty.getRepeat();
//		}
//		String apiurl = WenJuanUntil.wxViewLink(user.getUuid(),
//				questionnaire.getUrlLink(), repeat, redirect_uri);
//		return successModel(apiurl).toJSON();
//	}
}
