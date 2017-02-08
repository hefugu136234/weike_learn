package com.lankr.tv_cloud.model;

public class Subscribe extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1917480103534462981L;

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int getStatus() {
		return status == null ? 0 : status;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	private Integer status;

	private String mobile; // 患者的手机号码

	private String name; // 患者的名字

	private String mail;

	private String company;

	private String group;

	private String position;

	private String userType;

	private Hospital hospital;

	private Departments departments;

	private String phone;

}
