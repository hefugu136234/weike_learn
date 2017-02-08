package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class OpusVo extends BaseAPIModel{
	
	private String uuid;
	
	private String name;
	
	private String desc;
	
	private String codeNum;
	
	private String categoryName;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	
	
	public String getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(String codeNum) {
		this.codeNum = codeNum;
	}
	

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void buildData(ActivityApplication activityApplication){
		this.setName(activityApplication.getName());
		this.setCodeNum(activityApplication.getCode());
		this.setDesc(activityApplication.getMark());
		this.setUuid(activityApplication.getUuid());
		this.setCategoryName(buildCateName(activityApplication.getCategory()));
	}
	
	public String buildCateName(Category category){
		String val="";
		if(category==null){
			return val;
		}
		val=category.getName();
		Category parent=category.getParent();
		int i=0;
		while (parent!=null) {
			if(i>13){
				break;
			}
			val=parent.getName()+"-"+val;
			parent=parent.getParent();
			i++;
		}
		return val;
	}

}
