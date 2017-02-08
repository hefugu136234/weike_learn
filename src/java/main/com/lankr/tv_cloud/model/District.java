package com.lankr.tv_cloud.model;

public class District extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7132514786390102467L;

	private String name;
	
	private Province province;
	
	private City city;
	
	private String pinyin;

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

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

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
	

}
