package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.IntegralRecord;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class IntegralRecordWXItemVo {
	
	private String resName;
	
	private String actionName;
	
	private int score;
	
	private String dateTime;
	
	private int action;

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	
	
	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public void buildData(IntegralRecord record){
		this.setActionName(OptionalUtils.traceValue(record, "mark"));
		this.setDateTime(Tools.df1.format(record.getCreateDate()));
		this.setScore(record.getValue());
		this.setAction(OptionalUtils.traceInt(record, "action"));
		//this.setResName(OptionalUtils.traceValue(record, "resource.name"));
	}

}
