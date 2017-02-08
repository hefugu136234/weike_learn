package com.lankr.tv_cloud.model;

public class TempleMessageRecord extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 681929397165133533L;
	
	public final static int broadCastBook=1;
	
	public final static int realNameNotice=2;
	
	public final static int oupsCheck=3;
	
	public final static int registerSuccess=4;
	
	public final static int realNameSubmit=5;
	
	public final static int exchageGoods=6;
	
	public final static int deliverGoods=7;
	
	public final static int oupsSubmit=8;
	
	public final static int integralDaily=9;
	
	public final static int broadCastBookCheck=10;
	
	public final static int vipDeliverGoods=11;
	
	public final static int onActivityResourceChanged=12;//活动资源变更主动发消息
	
	public final static int beforeHourBroadcast=13;//直播一个小时开始
	

	private int userId;
	
	private String openId;
	
	private String message;
	
	private int status;
	
	private int interfaceType;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(int interfaceType) {
		this.interfaceType = interfaceType;
	}
	
	

}
