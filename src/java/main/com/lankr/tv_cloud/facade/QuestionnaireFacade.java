package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Questionnaire;
import com.lankr.tv_cloud.model.QuestionnaireAnswer;
import com.lankr.tv_cloud.model.User;

public interface QuestionnaireFacade {

	public ActionMessage<?> addQuestionnaire(Questionnaire questionnaire);

	public Questionnaire selectQuestionnaireByUuid(String uuid);

	public ActionMessage<?> updateQuestionnaire(Questionnaire questionnaire);

	public ActionMessage<?> updateQuestionnaireStatus(Questionnaire questionnaire);

	public Pagination<Questionnaire> searchQuestionnaireForTable(
			String searchValue, int from, int size);

	public ActionMessage<?> addQuestionnaireAnswer(
			QuestionnaireAnswer questionnaireAnswer);

	public QuestionnaireAnswer selectQuestionnaireAnswerByUuid(String uuid);

	public ActionMessage<?> updateQuestionnaireAnswer(
			QuestionnaireAnswer questionnaireAnswer);

	public Pagination<QuestionnaireAnswer> searchQuestionnaireAnswerForTable(
			int questionnaireId, String searchValue, int from, int size);
	
	public Questionnaire selectQuesByName(String name);
	
	public Questionnaire selectQuesByProId(String proid);
	
	public ActionMessage<?> pullAnswerAndSave(Questionnaire questionnaire,User user,String chapter);

	public List<Questionnaire> selectAllQuestionnaire();

	public List<Questionnaire> selectQuestionnaireByQ(String q);

}
