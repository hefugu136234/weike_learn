package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.orm.mybatis.mapper.NormalCollectQuestionnaireMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.NormalCollectQuestionnaireFacade;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.NormalCollectQuestionnaire;

public class NormalCollectQuestionnaireFacadeImpl extends FacadeBaseImpl implements NormalCollectQuestionnaireFacade{
	

	@Override
	public ActionMessage addNormalCollectQuestionnaire(NormalCollectQuestionnaire normalCollectQuestionnaire) {
		try {
			int effect = normalCollectQuestionnaireMapper.addNormalCollectQuestionnaire(normalCollectQuestionnaire);
			if (effect >= 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("保存发送记录失败");
	}

	@Override
	public ActionMessage updateNormalCollectQuestionnaire(NormalCollectQuestionnaire normalCollectQuestionnaire) {
		
		if (Message.hasPersisted(normalCollectQuestionnaire)) {
			try {
				int effect = normalCollectQuestionnaireMapper.updateNormalCollectQuestionnaire(normalCollectQuestionnaire);
				if (effect > 0) {
					return ActionMessage.successStatus();
				}
			} catch (Exception e) {
				if (logger.isErrorEnabled())
					logger.error("[MessageFacadeImpl.update] -> update error",
							e);
				return ActionMessage.failStatus("update error");
			}
		}
		return ActionMessage.failStatus("message is empty");
	}

	@Override
	public List<NormalCollectQuestionnaire> selectNormalCollectQuestionnaireByNormalCollect(int id, int type) {
		return normalCollectQuestionnaireMapper.selectNormalCollectQuestionnaireByNormalCollectId(id, type);
	}

	@Override
	protected String namespace() {
		return normalCollectQuestionnaireMapper.getClass().getName();
	}

	@Override
	public NormalCollectQuestionnaire selectByCollectAndQuestionnaire(int nId, int qId, int type) {
		// TODO Auto-generated method stub
		return normalCollectQuestionnaireMapper.selectByCollectAndQuestionnaire(nId, qId,type);
	}
	
	@Override
	public NormalCollectQuestionnaire selectQuestionnaireOne(int chapterId,
			int type) {
		// TODO Auto-generated method stub
		return normalCollectQuestionnaireMapper.selectQuestionnaireOne(chapterId, type);
	}
	
	@Override
	public NormalCollectQuestionnaire selectQuestionnaireOneByUuid(String uuid) {
		// TODO Auto-generated method stub
		return normalCollectQuestionnaireMapper.selectQuestionnaireOneByUuid(uuid);
	}

}
