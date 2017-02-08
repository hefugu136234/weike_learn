package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.ResourceVoteAnswer;
import com.lankr.tv_cloud.model.ResourceVoteOption;
import com.lankr.tv_cloud.model.ResourceVoteSubject;

public interface ResourceVoteMapper {

	public void addVoteSubject(ResourceVoteSubject subject);

	public void addVoteOption(ResourceVoteOption option);

	public int updateVoteSubject(ResourceVoteSubject subject);

	public int updateVoteOption(ResourceVoteOption option);

	public ResourceVoteSubject getVoteSubjectByUuid(String uuid);

	public List<ResourceVoteSubject> getVotesByResourceId(int resId);

	public int disableVote(String uuid);

	public int disableVoteOption(String uuid);

	public int addResourceVoteAnswer(ResourceVoteAnswer answer);
	
	public int increaseOptionCount(int optionId);

	public ResourceVoteOption getVoteOptionByUuid(String uuid);

	public List<ResourceVoteAnswer> getUserListByOptionIdAndSubjectId(int optionId,
			int subjectId, String query, int startPage, int pageSize);

}
