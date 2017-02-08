package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.Questionnaire;
import com.lankr.tv_cloud.model.QuestionnaireAnswer;

public interface QuestionnaireMapper {

	public int addQuestionnaire(Questionnaire questionnaire);

	public Questionnaire selectQuestionnaireByUuid(String uuid);

	public int updateQuestionnaire(Questionnaire questionnaire);

	public int updateQuestionnaireStatus(Questionnaire questionnaire);

	public List<Questionnaire> searchQuestionnaireForTable(String searchValue,
			int from, int size);

	public int addQuestionnaireAnswer(QuestionnaireAnswer questionnaireAnswer);

	public QuestionnaireAnswer selectQuestionnaireAnswerByUuid(String uuid);

	public int updateQuestionnaireAnswer(QuestionnaireAnswer questionnaireAnswer);

	public List<QuestionnaireAnswer> searchQuestionnaireAnswerForTable(
			int questionnaireId, String searchValue, int from, int size);

	public Questionnaire selectQuesByName(String name);
	
	public Questionnaire selectQuesByProId(String proid);

	public List<Questionnaire> selectAllQuestionnaire();

	public List<Questionnaire> selectQuestionnaireByQ(String q);
	
	public Questionnaire selectQuestionnaireById(int id);

}
