/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月14日
 * 	@modifyDate 2016年6月14日
 *  
 */
package com.lankr.tv_cloud.model;

import java.util.Date;

/**
 * @author Kalean.Xiang
 *
 */
public class Notification extends BaseModel {

	private static final long serialVersionUID = 1L;

	public static final int TYPE_ACTIVITY = 1;

	public static final int TYPE_BROADCAST = 2;

	public static final int SIGN_CONTENT_UPDATED = 1;

	public static final int SIGN_IMMEDIATELY = 2;

	// 通知取消
	public static final int STATUS_INVALID = 0;

	// 通知创建
	public static final int STATUS_CREATED = 1;

	// 正在发送中
	public static final int STATUS_PROCESSING = 2;

	// 发送完毕
	public static final int STATUS_FINISHED = 3;

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private Date sendDate;

	private String body;

	private int referType;

	private int referId;

	private int sign;

	private int status;


	// 是否已经发送
	public boolean hasSend() {
		return status == STATUS_PROCESSING || status == STATUS_FINISHED;
	}


	private User user;

}
