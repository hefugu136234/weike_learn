package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.NormalCollectQuestionnaire;

public interface NormalCollectQuestionnaireMapper {

	public int addNormalCollectQuestionnaire(
			NormalCollectQuestionnaire normalCollectionQuestionnaire);

	public int updateNormalCollectQuestionnaire(
			NormalCollectQuestionnaire normalCollectQuestionnaire);

	public List<NormalCollectQuestionnaire> selectNormalCollectQuestionnaireByNormalCollectId(
			int id, int type);

	public NormalCollectQuestionnaire selectByCollectAndQuestionnaire(int nId,
			int qId, int type);

	public NormalCollectQuestionnaire selectQuestionnaireOne(
			@Param("chapterId") int chapterId, @Param("type") int type);
	
	public NormalCollectQuestionnaire selectQuestionnaireOneByUuid(String uuid);

}
