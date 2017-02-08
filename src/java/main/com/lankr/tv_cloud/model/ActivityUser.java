package com.lankr.tv_cloud.model;

import java.util.Date;

@SuppressWarnings("all")
public class ActivityUser extends BaseModel{

	private static final long serialVersionUID = 1L;
	
	private Activity activity;
	private int status;
	private User user;
	
	
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
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
}
