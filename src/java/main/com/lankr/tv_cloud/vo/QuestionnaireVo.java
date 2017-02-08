package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.CertificationFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.Questionnaire;
import com.lankr.tv_cloud.model.Questionnaire.QuestionnaireProperty;
import com.lankr.tv_cloud.model.QuestionnaireAnswer;
import com.lankr.tv_cloud.utils.JSONException;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class QuestionnaireVo extends BaseAPIModel {

	private String uuid;

	private String name;

	private String createDate;

	private int isStatus;

	private String repeat;

	private String urlLink;

	private String mark;

	private String realName;

	private float score;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(int isStatus) {
		this.isStatus = isStatus;
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public String getUrlLink() {
		return urlLink;
	}

	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public  float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public void buildTableList(Questionnaire questionnaire) {
		this.setUuid(questionnaire.getUuid());
		this.setName(OptionalUtils.traceValue(questionnaire, "name"));
		this.setIsStatus(questionnaire.getStatus());
		this.setCreateDate(Tools.formatYMDHMSDate(questionnaire.getCreateDate()));
	}

	public void updateNeedDate(Questionnaire questionnaire) {
		this.setUuid(questionnaire.getUuid());
		this.setName(OptionalUtils.traceValue(questionnaire, "name"));
		this.setMark(OptionalUtils.traceValue(questionnaire, "mark"));
		this.setUrlLink(OptionalUtils.traceValue(questionnaire, "urlLink"));
		QuestionnaireProperty questionnaireProperty = Questionnaire
				.JsonForObject(questionnaire.getqProperty());
		this.setRepeat(OptionalUtils
				.traceValue(questionnaireProperty, "repeat"));
	}

	public void buildAnswerTable(QuestionnaireAnswer questionnaireAnswer,
			CertificationFacade certificationFacade) {
		this.setUuid(questionnaireAnswer.getUuid());
		this.setName(OptionalUtils.traceValue(questionnaireAnswer,
				"user.username"));
		this.setCreateDate(Tools.formatYMDHMSDate(questionnaireAnswer
				.getCreateDate()));
		this.setIsStatus(questionnaireAnswer.getStatus());
		this.setScore(OptionalUtils.traceFloat(questionnaireAnswer, "score"));
		Certification certification = certificationFacade
				.getCertifiActiveByUserId(questionnaireAnswer.getUser());
		this.setRealName(OptionalUtils.traceValue(certification, "name"));

	}

}
