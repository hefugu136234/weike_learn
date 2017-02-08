/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年7月22日
 * 	@modifyDate 2016年7月22日
 *  
 */
package com.lankr.tv_cloud.model;

import java.util.Date;

/**
 * @author Kalean.Xiang
 *
 */
public class QrInteractChannel extends BaseModel {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.model.BaseModel#apiUseable()
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 3148778915510769208L;

	public final static int SIGN_SINGLE = 0; // 支持单个连接

	public final static int SIGN_MULTIPART = 1; // 支持多个连接

	public final static int REFERTYPE_RESOURCE = 1; // 资源

	public static final int STATUS_CREATED = 0;
	public static final int STATUS_SCANNED = 1;
	public static final int STATUS_CANCEL = 2;
	public static final int STATUS_INTERACT = 3; // 进入交互模式
	public static final int STATUS_FINISHED = 4; // 授权完成 --指用户端已经完成

	public User getScanUser() {
		return scanUser;
	}

	public void setScanUser(User scanUser) {
		this.scanUser = scanUser;
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

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public Date getLastScanDate() {
		return lastScanDate;
	}

	public void setLastScanDate(Date lastScanDate) {
		this.lastScanDate = lastScanDate;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getQrUrl() {
		return qrUrl;
	}

	public void setQrUrl(String qrUrl) {
		this.qrUrl = qrUrl;
	}

	private User scanUser;

	private int referType;

	private int referId;

	private String ip;

	private String device;

	private Date lastScanDate;

	private String ticket;

	private int sign;

	private int status;

	private String qrUrl;

	@Override
	public boolean apiUseable() {
		return isActive();
	}

	public boolean isResourceTarget() {
		return referType == REFERTYPE_RESOURCE;
	}
}
