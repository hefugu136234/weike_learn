package com.lankr.tv_cloud.model;

public class ApplicableRecords extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4965689678565548342L;
	
	public static final int INIT_STATUS=0;//初始状态
	
	public static final int CHECK_STATUS=1;//已审核
	
	public static final int SEND_STATUS=2;//已发货，激活状态另行判断

	//0=未审核 1=已审核
	private Integer status;

	private String applyName;

	private String pingYin;

	private String mobile;

	private Hospital hospital;

	private Departments departments;

	private WebchatUser webchatUser;

	private User user;

	public int getStatus() {
		return status == null ? 0 : status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public Departments getDepartments() {
		return departments;
	}

	public void setDepartments(Departments departments) {
		this.departments = departments;
	}

	public String getPingYin() {
		return pingYin;
	}

	public void setPingYin(String pingYin) {
		this.pingYin = pingYin;
	}

	public WebchatUser getWebchatUser() {
		return webchatUser;
	}

	public void setWebchatUser(WebchatUser webchatUser) {
		this.webchatUser = webchatUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}
