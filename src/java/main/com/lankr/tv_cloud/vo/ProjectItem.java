package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.annotations.DataAlias;

public class ProjectItem {
	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getApply() {
		return apply;
	}

	public void setApply(String apply) {
		this.apply = apply;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	@DataAlias(column = "uuid")
	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@DataAlias(column = "projectName")
	private String project_name;

	@DataAlias(column = "createDate")
	private String create_date;

	@DataAlias(column = "apply")
	private String apply;

	@DataAlias(column = "mark")
	private String mark;
}
