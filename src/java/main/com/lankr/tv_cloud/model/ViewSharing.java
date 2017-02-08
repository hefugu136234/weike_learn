package com.lankr.tv_cloud.model;

public class ViewSharing extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6923331126133894195L;

	private String openid;
	
	private User user;
	
	private int status;
	
	private Resource resource;
	
	private User shareUser;
	
	private int viewCount;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public User getShareUser() {
		return shareUser;
	}

	public void setShareUser(User shareUser) {
		this.shareUser = shareUser;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	
	
	

}
