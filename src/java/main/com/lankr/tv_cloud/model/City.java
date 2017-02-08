package com.lankr.tv_cloud.model;


public class City extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4135713878741411433L;

	private String name;
	
	private Province province;
	
	private String pinyin;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	
	

}
