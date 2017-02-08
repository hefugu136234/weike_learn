package com.lankr.tv_cloud.model;

public class ResourceAccessIgnore extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	public static final String NO_REGISTERD = "#1#";
	public static final String HAS_REGISTERD_NO_RELNAME = "#2#";
	public static final String HAS_RELNAME = "#3#";
	public static final String IGNORE_PRE_VIEW = "#1#";
	public static final String IGNORE_WATCH = "#2#";
	
	private Resource resource;
	private String detail;
	private int status;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
