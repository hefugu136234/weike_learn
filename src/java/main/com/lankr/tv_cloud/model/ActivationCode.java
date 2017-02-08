package com.lankr.tv_cloud.model;

public class ActivationCode extends BaseModel {

	public static final int STATUS_USEABLE = 0;
	public static final int STATUS_INUSED = 1;
	private static final long serialVersionUID = 7030613525394704048L;
	private String activeCode;
	private ProductGroup productGroup;
	private String cardNum;
	private User user;
	private int status;
	private long deadline;
	

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public ProductGroup getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(ProductGroup productGroup) {
		this.productGroup = productGroup;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isValidCard() {
		return isActive() && getStatus() == STATUS_USEABLE;
	}
	
	public boolean isAbled(){
		return isActive();
	}
	
	public boolean isUsed(){
		return getStatus() == STATUS_USEABLE;
	}
}
