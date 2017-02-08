package com.lankr.tv_cloud.web.api.webchat.util;

public class ModelKeyNote {
	
	public static final String BULE_COLOR="#173177";
	
	private String value;
	
	private String color;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public ModelKeyNote(){
		
	}
	
    public ModelKeyNote(String color,String value){
		this.color=color;
		this.value=value;
	}

}
