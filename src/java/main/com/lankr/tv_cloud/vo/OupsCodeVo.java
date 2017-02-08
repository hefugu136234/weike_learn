package com.lankr.tv_cloud.vo;

import org.omg.CORBA.PRIVATE_MEMBER;

import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class OupsCodeVo extends BaseAPIModel{
	
	private String uuid;
	
	private String name;
	
	private String applyCateName;
	
	private String activityName;
	
	private String codeNum;
	
	private String oupsMark;
	
	private String resName;
	
	private String cateName;
	
	private int isStatus;
	
	private String resType;
	
	private String resMark;
	
	private String createDate;
	
	private String applyUserName;
	
	private String sendType;
	
	private String userUuid;
	
	private boolean bundled;

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(String codeNum) {
		this.codeNum = codeNum;
	}

	public String getOupsMark() {
		return oupsMark;
	}

	public void setOupsMark(String oupsMark) {
		this.oupsMark = oupsMark;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public int getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(int isStatus) {
		this.isStatus = isStatus;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getResMark() {
		return resMark;
	}

	public void setResMark(String resMark) {
		this.resMark = resMark;
	}

	public String getApplyCateName() {
		return applyCateName;
	}

	public void setApplyCateName(String applyCateName) {
		this.applyCateName = applyCateName;
	}
	
	
	
	
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}
	
	

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public void build(ActivityApplication application){
		this.setUuid(application.getUuid());
		this.setName(Tools.nullValueFilter(application.getName()));
		this.setApplyCateName(OptionalUtils.traceValue(application, "category.name"));
		this.setActivityName(OptionalUtils.traceValue(application, "activity.name"));
		if(getActivityName().isEmpty()){
			setActivityName("普通作品");
		}
		this.setCodeNum(Tools.nullValueFilter(application.getCode()));
		this.setOupsMark(Tools.nullValueFilter(application.getMark()));
		this.setResName(OptionalUtils.traceValue(application, "resource.name"));
		Resource resource=application.getResource();
		if(resource==null){
			this.setResType("");
		}else{
			this.setResType(resource.getType().name());
		}
		this.setResMark(OptionalUtils.traceValue(application, "resource.mark"));
		this.setCateName(OptionalUtils.traceValue(application, "resource.category.name"));
		this.setIsStatus(application.getStatus());
	    this.setCreateDate(Tools.df1.format(application.getCreateDate()));
	    this.setApplyUserName(OptionalUtils.traceValue(application, "user.nickname"));
	    if(application.getSendType()==1){
	    	this.setSendType("快递交付");
	    }
	    else if(application.getSendType()==2){
	    	this.setSendType("网盘交付");
	    }else{
	    	this.setSendType("");
	    }
	    this.setUserUuid(OptionalUtils.traceValue(application, "user.uuid"));
	    this.bundled = application.isBundled();
	}
	
	

}
