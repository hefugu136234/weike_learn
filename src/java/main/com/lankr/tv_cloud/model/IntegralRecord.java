package com.lankr.tv_cloud.model;

public class IntegralRecord extends BaseModel {

	private static final long serialVersionUID = -7248627164665944434L;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}
	
	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}



	private User user;// 积分记录拥有者

	private int action;

	private int value;

	private Resource resource;

	private User fromUser; // 用于记录产生积分的用户
	
	private int status;		//0 待审核, 1 兑换成功, 2 兑换失败
	
	private IntegralConsume consume;

	public IntegralConsume getConsume() {
		return consume;
	}

	public void setConsume(IntegralConsume consume) {
		this.consume = consume;
	}
}
