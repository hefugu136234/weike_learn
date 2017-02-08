package com.lankr.tv_cloud.model;

public class BroadcastUser extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 894700625848733419L;

	private Broadcast broadcast;
	
	private User user;
	
	private int status;

	public Broadcast getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(Broadcast broadcast) {
		this.broadcast = broadcast;
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
	
	

}
