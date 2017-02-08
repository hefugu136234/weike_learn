package com.lankr.tv_cloud.web.api.webchat.vo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.lankr.tv_cloud.model.SignUpUser;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class WxBookUserItem {

	private String uuid;

	private String name;

	private int checkStatus;

	private String bookTime;

	private String dateTime;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getBookTime() {
		return bookTime;
	}

	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public void buildDataList(SignUpUser signUpUser) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		this.uuid = signUpUser.getUuid();
		this.name = OptionalUtils.traceValue(signUpUser, "user.nickname");
		this.checkStatus = OptionalUtils.traceInt(signUpUser, "status");
		this.bookTime = df.format(signUpUser.getCreateDate());
		this.dateTime = Tools.formatYMDHMSDate(signUpUser.getCreateDate());
	}

}
