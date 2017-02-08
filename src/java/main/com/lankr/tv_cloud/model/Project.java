package com.lankr.tv_cloud.model;

public class Project extends BaseModel {

	private static final long serialVersionUID = -8831414066110018131L;

	

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getApply() {
		return apply;
	}

	public void setApply(String apply) {
		this.apply = apply;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String projectName;

	private String pinyin;

	private String apply;

	private String logoUrl;

	private int status;
	
	private int stubRoleId;

	public int getStubRoleId() {
		return stubRoleId;
	}

	public void setStubRoleId(int stubRoleId) {
		this.stubRoleId = stubRoleId;
	}

}
