package com.lankr.tv_cloud.model;

public class CategoryExpand extends BaseModel{
	
	
	private static final long serialVersionUID = -4020738377975639761L;

	private Integer affect;
	private Category category;
	private String appTaskId;
	private String wxTaskId;
	private String tvTaskId;
	private String webTaskId;
	private String byname;
	private Integer status;
	
	
	public Integer getAffect() {
		return affect;
	}

	public void setAffect(Integer affect) {
		this.affect = affect;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}


	public String getAppTaskId() {
		return appTaskId;
	}

	public void setAppTaskId(String appTaskId) {
		this.appTaskId = appTaskId;
	}

	public String getWxTaskId() {
		return wxTaskId;
	}

	public void setWxTaskId(String wxTaskId) {
		this.wxTaskId = wxTaskId;
	}

	public String getTvTaskId() {
		return tvTaskId;
	}

	public void setTvTaskId(String tvTaskId) {
		this.tvTaskId = tvTaskId;
	}

	public String getByname() {
		return byname;
	}

	public void setByname(String byname) {
		this.byname = byname;
	}

	public int getStatus() {
		return status == null ? 0 : status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getWebTaskId() {
		return webTaskId;
	}

	public void setWebTaskId(String webTaskId) {
		this.webTaskId = webTaskId;
	}

}
