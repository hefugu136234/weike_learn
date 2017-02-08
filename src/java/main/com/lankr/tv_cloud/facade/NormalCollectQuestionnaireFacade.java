package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.model.NormalCollectQuestionnaire;

public interface NormalCollectQuestionnaireFacade {
	
	public ActionMessage addNormalCollectQuestionnaire(NormalCollectQuestionnaire normalCollectQuestionnaire);
	
	public ActionMessage updateNormalCollectQuestionnaire(NormalCollectQuestionnaire normalCollectQuestionnaire);
	
	public List<NormalCollectQuestionnaire> selectNormalCollectQuestionnaireByNormalCollect(int id, int type);

	public NormalCollectQuestionnaire selectByCollectAndQuestionnaire(int nId, int qId, int type);
	
	public NormalCollectQuestionnaire selectQuestionnaireOne(int chapterId, int type);
	
	public NormalCollectQuestionnaire selectQuestionnaireOneByUuid(String uuid);
}
