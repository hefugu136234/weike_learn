package com.lankr.tv_cloud.web.front.vo;

import java.text.DecimalFormat;
import java.util.List;

import com.lankr.tv_cloud.model.ResourceVoteOption;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class FrontVoteAnswer {
	
	private String uuid;
	
	private String option;
	
	private boolean voted;
	
	private int votedCount;//该项被投过的次数
	
	private String precent;//该项被投所占的%
	
	private DecimalFormat format=new DecimalFormat("0.0");

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public boolean isVoted() {
		return voted;
	}

	public void setVoted(boolean voted) {
		this.voted = voted;
	}

	public int getVotedCount() {
		return votedCount;
	}

	public void setVotedCount(int votedCount) {
		this.votedCount = votedCount;
	}

	public String getPrecent() {
		return precent;
	}

	public void setPrecent(String precent) {
		this.precent = precent;
	}
	
	public void build(ResourceVoteOption resourceVoteOption,List<Integer> list,int totalCount){
		this.setUuid(resourceVoteOption.getUuid());
		this.setOption(OptionalUtils.traceValue(resourceVoteOption, "option"));
		int count=OptionalUtils.traceInt(resourceVoteOption, "count");
		this.setVotedCount(count);
		boolean voted=false;
		if(list!=null&&!list.isEmpty()){
			voted=list.contains(resourceVoteOption.getId());
		}
		this.setVoted(voted);
		String precent=buildprecent(count,totalCount);
		this.setPrecent(precent);
	}
	
	public String buildprecent(int count,int total){
		String precent="0.0%";
		if(total==0){
			return precent;
		}
		float countF=(float)count;
		float result=countF/total*100;
		precent=format.format(result)+"%";
		return precent;
	}
	
	public static void main(String[] args) {
		DecimalFormat format=new DecimalFormat("0.00");
		System.out.println(format.format(100));
	}
	

}
