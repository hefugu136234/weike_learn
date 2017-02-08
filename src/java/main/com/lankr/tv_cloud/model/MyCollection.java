package com.lankr.tv_cloud.model;

public class MyCollection extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4856575484095739799L;

	private Integer status;
	
	private User user;
	
	private Resource resource;

	public int getStatus() {
		return status == null ? 0 : status;
	}

	public void setStatus(Integer status) {
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
