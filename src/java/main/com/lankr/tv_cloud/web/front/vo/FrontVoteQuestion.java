package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.ResourceVoteOption;
import com.lankr.tv_cloud.model.ResourceVoteSubject;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class FrontVoteQuestion {
	
	private String uuid;
	
	private String title;
	
	private int type;
	
	private boolean voted;
	
	private List<FrontVoteAnswer> answerList;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isVoted() {
		return voted;
	}

	public void setVoted(boolean voted) {
		this.voted = voted;
	}

	public List<FrontVoteAnswer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<FrontVoteAnswer> answerList) {
		this.answerList = answerList;
	}
	
	public void build(ResourceVoteSubject voteSubject,List<Integer> list){
		this.setUuid(voteSubject.getUuid());
		this.setTitle(OptionalUtils.traceValue(voteSubject, "title"));
		int type=OptionalUtils.traceInt(voteSubject, "type");
		this.setType(type);
		boolean voted=false;
		if(list!=null&&!list.isEmpty()){
			voted=true;
		}
		this.setVoted(voted);
		this.answerList=new ArrayList<FrontVoteAnswer>();
		List<ResourceVoteOption> options=voteSubject.getOptions();
		if(options!=null&&!options.isEmpty()){
			//第一遍循环计算此题中各项投票的总数
			int totalCount=0;
			for (ResourceVoteOption resourceVoteOption : options) {
				int count=OptionalUtils.traceInt(resourceVoteOption, "count");
				totalCount=totalCount+count;
			}
			
			for (ResourceVoteOption resourceVoteOption : options) {
				FrontVoteAnswer answer=new FrontVoteAnswer();
				answer.build(resourceVoteOption, list,totalCount);
				this.answerList.add(answer);
			}
		}
		
	}
}
