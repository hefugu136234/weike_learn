package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ActivitySubject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class ActivitySubjectItemVo extends BaseAPIModel {

	private String uuid;

	private String name;

	private String categoryUuid;

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

	public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void buildTableData(ActivitySubject subject) {
		this.setCategoryName(OptionalUtils.traceValue(subject, "category.name"));
		this.setName(OptionalUtils.traceValue(subject, "name"));
		this.setUuid(subject.getUuid());
	}
	
	public void updatePageData(ActivitySubject subject){
		this.setStatus(Status.SUCCESS);
		this.setCategoryUuid(OptionalUtils.traceValue(subject, "category.uuid"));
		this.setCategoryName(OptionalUtils.traceValue(subject, "category.name"));
		this.setName(OptionalUtils.traceValue(subject, "name"));
		this.setUuid(subject.getUuid());
	}
	
	public void buildTableItem(ActivitySubject subject){
		this.setStatus(Status.SUCCESS);
		this.setCategoryName(OptionalUtils.traceValue(subject, "category.name"));
		this.setName(OptionalUtils.traceValue(subject, "name"));
		this.setUuid(subject.getUuid());
	}

}
