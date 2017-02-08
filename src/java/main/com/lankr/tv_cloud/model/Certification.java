package com.lankr.tv_cloud.model;

public class Certification extends BaseModel{
	
	private static final long serialVersionUID = -1714109772429654871L;
	
	public final static int NO_VERIFY=0;		//未审核
	public final static int HAS_VERIFY=1;		//已审核
	public final static int NO_PASS_VERIFY=2;	//审核未通过
	private String name;
	private String pinyin;
	private int status;
	private User user;
	private String imageUrl;
	private String credentials;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getCredentials() {
		return credentials;
	}
	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}
	
	public boolean isCertificated(){
		return isActive() && status == APPROVED;
	}
}
