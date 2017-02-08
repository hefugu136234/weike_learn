package com.lankr.tv_cloud.web.api.tv;

public class CategoryVO {

	private String uuid;
	
	public CategoryVO(String uuid, String name, String parentUuid) {
		super();
		this.uuid = uuid;
		this.name = name;
		this.parentUuid = parentUuid;
	}

	private String name;
	
	
	private String cover;
	
	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	private String parentUuid;

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

	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}
	
	

}
