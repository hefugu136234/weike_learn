package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.facade.CertificationFacade;
import com.lankr.tv_cloud.model.Questionnaire;
import com.lankr.tv_cloud.model.QuestionnaireAnswer;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class QuestionnaireSurface extends DataTableModel<QuestionnaireVo>{
	
	public void buildList(List<Questionnaire> list){
		if(list==null||list.isEmpty())
			return;
		for (Questionnaire questionnaire : list) {
			QuestionnaireVo vo=new QuestionnaireVo();
			vo.buildTableList(questionnaire);
			aaData.add(vo);
		}
	}
	
	public void buildAnswerList(List<QuestionnaireAnswer> list,CertificationFacade certificationFacade){
		if(list==null||list.isEmpty())
			return;
		for (QuestionnaireAnswer questionnaireAnswer : list) {
			QuestionnaireVo vo=new QuestionnaireVo();
			vo.buildAnswerTable(questionnaireAnswer,certificationFacade);
			aaData.add(vo);
		}
	}

}
