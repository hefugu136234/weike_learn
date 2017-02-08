package com.lankr.tv_cloud.vo;

public class ChosenItem {
	String id;
	String text;
	
	public ChosenItem(String id, String text) {
		this.id = id;
		this.text = text;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
