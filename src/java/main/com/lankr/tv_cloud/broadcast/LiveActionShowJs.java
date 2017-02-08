package com.lankr.tv_cloud.broadcast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LiveActionShowJs {
	// 组织者加入（不带token）
	private String attendeeAShortJoinUrl;
	// 组织者加入
	private String attendeeJoinUrl;
	// 普通参加者口令
	private String attendeeToken;
	// 结果代码 0 代表成功
	private String code;
	// 直播id
	private String id;
	// 直播编号
	private String number;

	// 组织者介入url
	private String organizerJoinUrl;
	// 组织者口令
	private String organizerToken;
	// 嘉宾加入url
	private String panelistJoinUrl;
	// 嘉宾口令
	private String panelistToken;

	private String message;

	public String getAttendeeAShortJoinUrl() {
		return attendeeAShortJoinUrl;
	}

	public void setAttendeeAShortJoinUrl(String attendeeAShortJoinUrl) {
		this.attendeeAShortJoinUrl = attendeeAShortJoinUrl;
	}

	public String getAttendeeJoinUrl() {
		return attendeeJoinUrl;
	}

	public void setAttendeeJoinUrl(String attendeeJoinUrl) {
		this.attendeeJoinUrl = attendeeJoinUrl;
	}

	public String getAttendeeToken() {
		return attendeeToken;
	}

	public void setAttendeeToken(String attendeeToken) {
		this.attendeeToken = attendeeToken;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getOrganizerJoinUrl() {
		return organizerJoinUrl;
	}

	public void setOrganizerJoinUrl(String organizerJoinUrl) {
		this.organizerJoinUrl = organizerJoinUrl;
	}

	public String getOrganizerToken() {
		return organizerToken;
	}

	public void setOrganizerToken(String organizerToken) {
		this.organizerToken = organizerToken;
	}

	public String getPanelistJoinUrl() {
		return panelistJoinUrl;
	}

	public void setPanelistJoinUrl(String panelistJoinUrl) {
		this.panelistJoinUrl = panelistJoinUrl;
	}

	public String getPanelistToken() {
		return panelistToken;
	}

	public void setPanelistToken(String panelistToken) {
		this.panelistToken = panelistToken;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean creatSuccess() {
		return this.getCode().equals("0");
	}

	public static LiveActionShowJs parseMessage(String message) {
		if (message == null) {
			return null;
		}
		try {
			Gson gson = new Gson();
			LiveActionShowJs liveActionShowJs = gson.fromJson(message,
					LiveActionShowJs.class);
			return liveActionShowJs;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return null;
	}

}
