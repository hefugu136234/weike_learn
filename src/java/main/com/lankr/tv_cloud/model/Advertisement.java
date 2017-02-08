package com.lankr.tv_cloud.model;

public class Advertisement extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3871656658051210162L;

	/**
	 * 
	 */
	//3871656658051210162

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getRes_url() {
		return res_url;
	}

	public void setRes_url(String res_url) {
		this.res_url = res_url;
	}

	public int getLimit_time() {
		return limit_time;
	}

	public void setLimit_time(int limit_time) {
		this.limit_time = limit_time;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public AdvertisementPosition getAdvertisementPosition() {
		return advertisementPosition;
	}

	public void setAdvertisementPosition(
			AdvertisementPosition advertisementPosition) {
		this.advertisementPosition = advertisementPosition;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	private String content_type;

	private String res_url;

	private int limit_time;

	private User user;

	private Project project;

	private AdvertisementPosition advertisementPosition;

	private int status;
	
	private String name;
}
