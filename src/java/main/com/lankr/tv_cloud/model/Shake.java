package com.lankr.tv_cloud.model;


public class Shake extends BaseModel {

	private static final long serialVersionUID = 8292360661855697354L;
	
	private User user;
	private Float money;
	private int status;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Float getMoney() {
		return money;
	}
	public void setMoney(Float money) {
		this.money = money;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
