/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月28日
 * 	@modifyDate 2016年6月28日
 *  
 */
package com.lankr.tv_cloud.model;

import java.util.Date;

/**
 * @author Kalean.Xiang
 *
 */
public class TvQrAuth extends BaseModel {

	public static final int STATUS_CREATED = 0;
	public static final int STATUS_SCANNED = 1;
	public static final int STATUS_CANCEL = 2;
	public static final int STATUS_AUTHED = 3; //
	public static final int STATUS_FINISHED = 4; // 授权完成 --指用户端已经完成
	public static final int STATUS_GRANTED = 5; // 客户端已经获取到权限码 --指客户端已经拿到数据

	public Date getAuthDate() {
		return authDate;
	}

	public void setAuthDate(Date authDate) {
		this.authDate = authDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	private Date authDate;

	private User user;

	private String key;

	private String ip;

	private String qrUrl;

	private Date lastScanDate;

	public Date getLastScanDate() {
		return lastScanDate;
	}

	public void setLastScanDate(Date lastScanDate) {
		this.lastScanDate = lastScanDate;
	}

	public String getQrUrl() {
		return qrUrl;
	}

	public void setQrUrl(String qrUrl) {
		this.qrUrl = qrUrl;
	}

	private String device;

	private int status;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.model.BaseModel#apiUseable()
	 */
	@Override
	public boolean apiUseable() {
		return isActive();
	}

	public boolean hasAuth() {
		return hasPersisted(user);
	}

}
