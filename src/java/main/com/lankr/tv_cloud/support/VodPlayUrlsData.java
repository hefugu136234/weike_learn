package com.lankr.tv_cloud.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.lankr.tv_cloud.utils.Tools;

public class VodPlayUrlsData {

	private int code = -1;

	private String message;

	private String json_data;

	public String getJson_data() {
		return json_data;
	}

	public void setJson_data(String json_data) {
		this.json_data = json_data;
	}

	private List<VodPlayItem> playSet;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<VodPlayItem> getPlaySet() {
		return playSet;
	}

	public void setPlaySet(List<VodPlayItem> playSet) {
		this.playSet = playSet;
	}



	public boolean isOk() {
		return 0 == code;
	}
}
