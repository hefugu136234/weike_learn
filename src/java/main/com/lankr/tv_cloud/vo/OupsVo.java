package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class OupsVo extends BaseAPIModel{
	
	private String name;
	
	private String uuid;
	
	private String codeNum;
	
	private String activityName;
	
	private String applyUserName;
	
	private String categoryName;
	
	private int sendType;
	
	private String desc;
	
	private boolean bundled;
	
	private String resUuid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	

	

	public String getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(String codeNum) {
		this.codeNum = codeNum;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public void buildData(ActivityApplication application){
		this.setStatus(Status.SUCCESS);
		if(application==null)
			return ;
		this.setUuid(application.getUuid());
		this.setName(application.getName());
		this.setCodeNum(application.getCode());
		this.setActivityName(OptionalUtils.traceValue(application, "activity.name"));
		if(getActivityName().isEmpty()){
			setActivityName("普通作品");
		}
		this.setApplyUserName(OptionalUtils.traceValue(application, "user.nickname"));
		this.setCategoryName(OptionalUtils.traceValue(application, "category.name"));
		this.setSendType(application.getSendType());
		this.setDesc(Tools.nullValueFilter(application.getMark()));
		Resource res = application.getResource();
		this.bundled = application.isBundled();
		if(res != null){
			this.resUuid = res.getUuid();
		}
		
	}

}
