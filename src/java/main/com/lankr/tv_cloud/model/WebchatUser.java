package com.lankr.tv_cloud.model;

public class WebchatUser extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5205221811761595336L;

	private String openId;
	
	private User user;
	
	private int status;
	
	private String wxlabel;
	
	private String photo;
	
	private String nickname;
	
	private String wxinfo;
	
	private String unionid;
	
	private String sex;//1=男 2=女

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
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

	public String getWxlabel() {
		return wxlabel;
	}

	public void setWxlabel(String wxlabel) {
		this.wxlabel = wxlabel;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getWxinfo() {
		return wxinfo;
	}

	public void setWxinfo(String wxinfo) {
		this.wxinfo = wxinfo;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	

}
