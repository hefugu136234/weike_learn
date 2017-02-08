package com.lankr.tv_cloud.model;

import java.util.Date;

import com.google.gson.Gson;
import com.lankr.tv_cloud.utils.Tools;

public class OfflineActivity extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9205257028830475673L;
	
	//报名类型
	public static final int PUBLIC_ENROLLTYPE=0;//开放
	
	public static final int VERIFY_ENROLLTYPE=1;//审核

	private int status;
	
	private String name;
	
	private String pinyin;
	
	private String description;
	
	private Date bookStartDate;
	
	private Date bookEndDate;
	
	private String address;
	
	private int enrollType;
	
	private int limitNum;
	
	private String cover;
	
	private String price;
	
	private User initiatorUser;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getBookStartDate() {
		return bookStartDate;
	}

	public void setBookStartDate(Date bookStartDate) {
		this.bookStartDate = bookStartDate;
	}

	public Date getBookEndDate() {
		return bookEndDate;
	}

	public void setBookEndDate(Date bookEndDate) {
		this.bookEndDate = bookEndDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getEnrollType() {
		return enrollType;
	}

	public void setEnrollType(int enrollType) {
		this.enrollType = enrollType;
	}

	public int getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(int limitNum) {
		this.limitNum = limitNum;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	
	public User getInitiatorUser() {
		return initiatorUser;
	}

	public void setInitiatorUser(User initiatorUser) {
		this.initiatorUser = initiatorUser;
	}




	public class OfflineActivityPrice{
		int price;
		int integral;
		
		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		public int getIntegral() {
			return integral;
		}

		public void setIntegral(int integral) {
			this.integral = integral;
		}

		public OfflineActivityPrice(int price,int integral){
			this.price=price;
			this.integral=integral;
		}
	}

	public String getPriceJson(int price,int integral){
		Gson gson=new Gson();
		OfflineActivityPrice pActivityPrice=new OfflineActivityPrice(price, integral);
		return gson.toJson(pActivityPrice);
	}
	
	public static OfflineActivityPrice priceForJsonObj(String json){
		if(Tools.isBlank(json)){
			return null;
		}
		Gson gson=new Gson();
		OfflineActivityPrice price=gson.fromJson(json, OfflineActivityPrice.class);
		return price;
	}
	
	

}
