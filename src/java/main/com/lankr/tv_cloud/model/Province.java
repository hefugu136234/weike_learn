package com.lankr.tv_cloud.model;


public class Province extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3355184722965165412L;
	
	private String name;
	
	private String pinyin;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	

}
