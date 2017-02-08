package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.QuestionnaireFacade;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollectQuestionnaire;
import com.lankr.tv_cloud.model.NormalCollectSchedule;
import com.lankr.tv_cloud.model.Questionnaire;
import com.lankr.tv_cloud.model.QuestionnaireAnswer;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.support.wenjuan.WenJuanUntil;
import com.lankr.tv_cloud.utils.HttpUtils;
import com.lankr.tv_cloud.utils.JSONObject;
import com.lankr.tv_cloud.utils.Tools;

public class QuestionnaireFacadeImp extends FacadeBaseImpl implements
		QuestionnaireFacade {

	@Override
	protected String namespace() {
		return "com.lankr.orm.mybatis.mapper.QuestionnaireMapper";
	}

	@Override
	public ActionMessage<?> addQuestionnaire(Questionnaire questionnaire) {
		try {
			int effect = questionnaireMapper.addQuestionnaire(questionnaire);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增问卷出错", e);
		}
		return codeProvider.code(-3001).getActionMessage();
	}

	@Override
	public Questionnaire selectQuestionnaireByUuid(String uuid) {
		return questionnaireMapper.selectQuestionnaireByUuid(uuid);
	}

	@Override
	public ActionMessage<?> updateQuestionnaire(Questionnaire questionnaire) {
		try {
			int effect = questionnaireMapper.updateQuestionnaire(questionnaire);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改问卷出错", e);
		}
		return codeProvider.code(-3002).getActionMessage();
	}

	@Override
	public ActionMessage<?> updateQuestionnaireStatus(
			Questionnaire questionnaire) {
		try {
			int effect = questionnaireMapper
					.updateQuestionnaireStatus(questionnaire);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改问卷状态出错", e);
		}
		return codeProvider.code(-3003).getActionMessage();
	}

	@Override
	public Pagination<Questionnaire> searchQuestionnaireForTable(
			String searchValue, int from, int size) {
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from questionnaire where isActive=1 and (name like '%"
				+ searchValue + "%' or pinyin like '%" + searchValue + "%')";
		Pagination<Questionnaire> pagination = initPage(sql, from, size);
		List<Questionnaire> list = questionnaireMapper
				.searchQuestionnaireForTable(searchValue, from, size);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public ActionMessage<?> addQuestionnaireAnswer(
			QuestionnaireAnswer questionnaireAnswer) {
		try {
			int effect = questionnaireMapper
					.addQuestionnaireAnswer(questionnaireAnswer);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新建问卷答案出错", e);
		}
		return codeProvider.code(-3001).getActionMessage();
	}

	@Override
	public QuestionnaireAnswer selectQuestionnaireAnswerByUuid(String uuid) {
		return questionnaireMapper.selectQuestionnaireAnswerByUuid(uuid);
	}

	@Override
	public ActionMessage<?> updateQuestionnaireAnswer(
			QuestionnaireAnswer questionnaireAnswer) {
		try {
			int effect = questionnaireMapper
					.updateQuestionnaireAnswer(questionnaireAnswer);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改问卷答案出错", e);
		}
		return codeProvider.code(-3002).getActionMessage();
	}

	@Override
	public Pagination<QuestionnaireAnswer> searchQuestionnaireAnswerForTable(
			int questionnaireId, String searchValue, int from, int size) {
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(q.id) from questionnaire_answer q left join user u on q.userId=u.id where q.isActive=1 and q.questionnaireId="
				+ questionnaireId
				+ " and (u.username like '%"
				+ searchValue
				+ "%' or u.nickname like '%"
				+ searchValue
				+ "%' or u.phone like '%" + searchValue + "%')";
		Pagination<QuestionnaireAnswer> pagination = initPage(sql, from, size);
		List<QuestionnaireAnswer> list = questionnaireMapper
				.searchQuestionnaireAnswerForTable(questionnaireId,
						searchValue, from, size);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public Questionnaire selectQuesByName(String name) {
		return questionnaireMapper.selectQuesByName(name);
	}

	@Override
	public Questionnaire selectQuesByProId(String proid) {
		return questionnaireMapper.selectQuesByProId(proid);
	}

	@Override
	public ActionMessage<?> pullAnswerAndSave(Questionnaire questionnaire,
			User user, String chapter) {
		if (questionnaire == null || !questionnaire.isActive()) {
			return codeProvider.code(-3004).getActionMessage();
		}
		if (user == null) {
			return codeProvider.code(-2001).getActionMessage();
		}
		String apiUrl = WenJuanUntil.pullAnswerLatest(
				questionnaire.getUrlLink(), user.getUuid());
		String message = HttpUtils.sendGetRequest(apiUrl);
		if (message == null) {
			return codeProvider.code(-3005).getActionMessage();
		}
		QuestionnaireAnswer questionnaireAnswer = new QuestionnaireAnswer();
		questionnaireAnswer.setUuid(Tools.getUUID());
		questionnaireAnswer.setUser(user);
		questionnaireAnswer.setQuestionnaire(questionnaire);
		questionnaireAnswer.setStatus(BaseModel.UNAPPROVED);
		questionnaireAnswer.setIsActive(BaseModel.ACTIVE);
		questionnaireAnswer.setAnswer(message);
		try {
			JSONObject jsonObject = new JSONObject(message);
			if (jsonObject != null) {
				if (jsonObject.has("score")) {
					double score = jsonObject.getDouble("score");
					questionnaireAnswer.setScore((float) score);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		// 添加问卷答案
		questionnaireMapper.addQuestionnaire(questionnaire);
		// 增加问卷章节考试进度
		NormalCollectQuestionnaire normalCollectQuestionnaire = normalCollectQuestionnaireMapper
				.selectQuestionnaireOneByUuid(chapter);
		if (normalCollectQuestionnaire != null) {
			NormalCollectSchedule normalCollectSchedule = normalCollectScheduleMapper
					.selectScheduleByUser(
							NormalCollectSchedule.TYPE_CHAPTERS_REFER,
							normalCollectQuestionnaire.getId(), user.getId());
			NormalCollect normalCollect = normalCollectMapper
					.getNormalCollectById(normalCollectQuestionnaire
							.getNormalCollectId());
			if (normalCollect != null) {
				int passScore = normalCollect.getPassScore();
				int checkStatus = NormalCollectSchedule.CHECK_INIT;
				if (normalCollectSchedule != null) {
					// 进度存在
					if (normalCollectSchedule.getCheckStatus() != NormalCollectSchedule.CHECK_PASS) {
						if (questionnaireAnswer.getScore() >= passScore) {
							checkStatus = NormalCollectSchedule.CHECK_PASS;
						} else {
							checkStatus = NormalCollectSchedule.CHECK_UNPASS;
						}
						normalCollectSchedule.setCheckStatus(checkStatus);
						normalCollectScheduleMapper.updateNormalCollectSchedule(normalCollectSchedule);
					}
				} else {
					if (questionnaireAnswer.getScore() >= passScore) {
						checkStatus = NormalCollectSchedule.CHECK_PASS;
					} else {
						checkStatus = NormalCollectSchedule.CHECK_UNPASS;
					}
					normalCollectSchedule = new NormalCollectSchedule();
					normalCollectSchedule.setUuid(Tools.getUUID());
					normalCollectSchedule.setStatus(BaseModel.UNAPPROVED);
					normalCollectSchedule.setIsActive(BaseModel.ACTIVE);
					normalCollectSchedule
							.setReferType(NormalCollectSchedule.TYPE_CHAPTERS_REFER);
					normalCollectSchedule.setReferId(normalCollectQuestionnaire
							.getId());
					normalCollectSchedule.setUserId(user.getId());
					normalCollectSchedule.setCheckStatus(checkStatus);
					normalCollectScheduleMapper
							.addNormalCollectSchedule(normalCollectSchedule);
				}
			}
		}
		// 增加章节考试
		return ActionMessage.successStatus();
	}

	public static void main(String[] args) {
		double mai = 3.30f;
		System.out.println((float) mai);
	}

	@Override
	public List<Questionnaire> selectAllQuestionnaire() {
		return questionnaireMapper.selectAllQuestionnaire();
	}

	@Override
	public List<Questionnaire> selectQuestionnaireByQ(String q) {
		return questionnaireMapper.selectQuestionnaireByQ(q);
	}

}
