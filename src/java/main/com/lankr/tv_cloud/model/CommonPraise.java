package com.lankr.tv_cloud.model;

public class CommonPraise extends BaseModel{
	
	
	public static final int REFER_TYPE_COMMENT = 1;

	private User user;
	
	private int referType;
	
	private int referId;
	
	private int status;
	
	private int sign;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getReferType() {
		return referType;
	}

	public void setReferType(int referType) {
		this.referType = referType;
	}

	public int getReferId() {
		return referId;
	}

	public void setReferId(int referId) {
		this.referId = referId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}
}
