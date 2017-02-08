package com.lankr.tv_cloud.model;

public class ResourceVoteAnswer extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1860197853656003159L;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public ResourceVoteOption getOption() {
		return option;
	}

	public void setOption(ResourceVoteOption option) {
		this.option = option;
	}

	private User user;
	
	private String openId;
	
	private String ip;
	
	private String device;
	
	private ResourceVoteOption option;

	private ResourceVoteSubject subject;

	public ResourceVoteSubject getSubject() {
		return subject;
	}

	public void setSubject(ResourceVoteSubject subject) {
		this.subject = subject;
	}
}
