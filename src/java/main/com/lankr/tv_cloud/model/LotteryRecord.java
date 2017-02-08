/** 
 *  @author Kalean.Xiang
 *  @createDate: 2016年4月15日
 * 	@modifyDate: 2016年4月15日
 *  
 */
package com.lankr.tv_cloud.model;

/**
 * @author Kalean.Xiang
 *
 */
public class LotteryRecord extends BaseModel {

	public static final int TYPE_NORMAL = 0;

	private User user;
	private int type;
	private String ticket;
	private Lottery lottery;
	private Award award;
	private float amount;
	private int status;		//标记管理人员操作状态，1 为已处理，0 为未处理, 默认状态为未处理
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Lottery getLottery() {
		return lottery;
	}

	public void setLottery(Lottery lottery) {
		this.lottery = lottery;
	}

	public Award getAward() {
		return award;
	}

	public void setAward(Award award) {
		this.award = award;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
