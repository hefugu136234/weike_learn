package com.lankr.tv_cloud.model;

import java.util.Date;



public class PageRemain extends BaseModel{
	
	
	public static int RESOURCE_DETAIL_TYPE=1;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2149933609997777351L;

	private int status;
	
	private String accessUri;
	
	private String plateform;
	
	private int referType;
	
	private int referId;
	
	private String ip;
	
	private String openId;
	
	private String unionid;
	
	private int userId;
	
	private Date firstView;
	
	private Date lastView;
	
	private int remainTime;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAccessUri() {
		return accessUri;
	}

	public void setAccessUri(String accessUri) {
		this.accessUri = accessUri;
	}

	public String getPlateform() {
		return plateform;
	}

	public void setPlateform(String plateform) {
		this.plateform = plateform;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getFirstView() {
		return firstView;
	}

	public void setFirstView(Date firstView) {
		this.firstView = firstView;
	}

	public Date getLastView() {
		return lastView;
	}

	public void setLastView(Date lastView) {
		this.lastView = lastView;
	}

	public int getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(int remainTime) {
		this.remainTime = remainTime;
	}
	
	

}
