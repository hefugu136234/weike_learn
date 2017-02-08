package com.lankr.tv_cloud.model;

public class SignUpUser extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2944182355607969763L;

	public static final int REFER_OFFLINEACTIVITY=1;//线下活动
	
	private int status;
	
	private int referId;
	
	private int referType;
	
	private User user;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getReferId() {
		return referId;
	}

	public void setReferId(int referId) {
		this.referId = referId;
	}

	public int getReferType() {
		return referType;
	}

	public void setReferType(int referType) {
		this.referType = referType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}
