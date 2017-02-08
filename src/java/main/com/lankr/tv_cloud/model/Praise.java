package com.lankr.tv_cloud.model;

public class Praise extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1313547685553870824L;

	private int status;

	private User user;

	private Resource resource;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	

}
