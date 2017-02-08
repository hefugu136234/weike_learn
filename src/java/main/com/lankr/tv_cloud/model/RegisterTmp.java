package com.lankr.tv_cloud.model;

public class RegisterTmp extends BaseModel {

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	private String mobile;

	private String smsCode;

	private String device;

	private String ip;

	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static final int TYPE_REGISTER = 0;

	public static final int TYPE_SUBSCRIBE = 1;
	
	public static final int TYPE_FORGETPASSWORD = 2;
}
