package com.lankr.tv_cloud.model;

public class SharingRes extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2818606260438781057L;

	private int status;
	
	private User user;
	
	private User oriUser;
	
	private Resource resource;
	
	private String approach;
	
	private String shareCode;

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

	public String getApproach() {
		return approach;
	}

	public void setApproach(String approach) {
		this.approach = approach;
	}

	public String getShareCode() {
		return shareCode;
	}

	public void setShareCode(String shareCode) {
		this.shareCode = shareCode;
	}

	public User getOriUser() {
		return oriUser;
	}

	public void setOriUser(User oriUser) {
		this.oriUser = oriUser;
	}
	
	
	
	

}
