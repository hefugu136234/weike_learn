package com.lankr.tv_cloud.model;

import java.util.Date;


public class ProjectCode extends BaseModel{
	
	public static final int REFER_OFFLINEACTIVITY=1;//线下活动
	
	public static final int CODE_INVITE=1;//邀请码
	
	public static final int CODE_EXCHANGE=2;//兑换码
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3523270488264729031L;

	private int referId;
	
	private int referType;
	
	private int codeType;
	
	private User user;
	
	private int status;
	
	private Date activeTime;
	
	private String projectCode;

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

	public int getCodeType() {
		return codeType;
	}

	public void setCodeType(int codeType) {
		this.codeType = codeType;
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

	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	
	

}
