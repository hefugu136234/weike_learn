package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceVoteSubject;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class FrontResVote extends BaseAPIModel{

	private boolean hasVote;// 是否有投票

	private boolean abelVote;// 能否投票（1，无user不能投 2.所有题目投过，不能投）

	private List<FrontVoteQuestion> questions;

	public boolean isHasVote() {
		return hasVote;
	}

	public void setHasVote(boolean hasVote) {
		this.hasVote = hasVote;
	}

	public boolean isAbelVote() {
		return abelVote;
	}

	public void setAbelVote(boolean abelVote) {
		this.abelVote = abelVote;
	}

	public List<FrontVoteQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(List<FrontVoteQuestion> questions) {
		this.questions = questions;
	}

	public void build(Resource resource, User user,
			ResourceFacade cacheResourceFacade) {
		this.setStatus(Status.SUCCESS);
		List<ResourceVoteSubject> subjects = cacheResourceFacade
				.getVotesByResourceId(resource);
		if(subjects==null||subjects.isEmpty()){
			this.setHasVote(false);
			return ;
		}
		this.setHasVote(true);
		boolean abelVote=false;
		this.questions=new ArrayList<FrontVoteQuestion>();
		for (ResourceVoteSubject resourceVoteSubject : subjects) {
			List<Integer> list=null;
			FrontVoteQuestion question=new FrontVoteQuestion();
			if(user!=null){
				//为用户投过此题选项的id
				list=cacheResourceFacade.seachVotedSubjectOptions(user.getId(), resourceVoteSubject.getId());
			}
			question.build(resourceVoteSubject, list);
			this.questions.add(question);
			//只要有一题未投，就可以投票
			if(!question.isVoted()){
				abelVote=true;
			}
		}
		
		if(user!=null&&abelVote){
			abelVote=true;
		}else{
			abelVote=false;
		}
		
		this.setAbelVote(abelVote);
	}

}
