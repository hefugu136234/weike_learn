package com.lankr.tv_cloud.model;

import java.util.Date;

public class TvBox extends BaseModel {

	// 设备唯一标示
	private String device;

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getValidateFrom() {
		return validateFrom;
	}

	public void setValidateFrom(Date validateFrom) {
		this.validateFrom = validateFrom;
	}

	public Date getValidateTo() {
		return validateTo;
	}

	public void setValidateTo(Date validateTo) {
		this.validateTo = validateTo;
	}


	// 设备的位置，如大门左侧，大门右侧
	private String position;

	// 有效开始时间
	private Date validateFrom;

	// 有效结束时间
	private Date validateTo;


}
