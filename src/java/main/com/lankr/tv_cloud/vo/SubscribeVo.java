package com.lankr.tv_cloud.vo;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Subscribe;
import com.lankr.tv_cloud.utils.Tools;

public class SubscribeVo {

	public Integer getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(Integer isStatus) {
		this.isStatus = isStatus;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getMobile() {
		return mobile;
	}

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

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}



	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}



	private Integer isStatus;

	private String uuid;

	private String mobile; // 患者的手机号码

	private String name; // 患者的名字

	private String mail;

	private String company;

	private String group;

	private String position;

	private String userType;

	private String hospital;

	private String departments;

	private String phone;
	
	private String createDate;
	
	private String mark;
	
	private String userUuid;

	public void build(Subscribe subscribe) {
		this.setIsStatus(subscribe.getStatus());
		this.setUuid(subscribe.getUuid());
		this.setName(Tools.nullValueFilter(HtmlUtils.htmlEscape(subscribe
				.getName())));
		this.setMobile(Tools.nullValueFilter(subscribe.getMobile()));
		this.setCreateDate(Tools.df1.format(subscribe.getCreateDate()));
		this.setMail(Tools.nullValueFilter(subscribe.getMail()));
		this.setCompany(Tools.nullValueFilter(HtmlUtils.htmlEscape(subscribe
		.getCompany())));
		this.setGroup(Tools.nullValueFilter(HtmlUtils.htmlEscape(subscribe
		.getGroup())));
		this.setPosition(Tools.nullValueFilter(subscribe.getPosition()));
		this.setUserType(Tools.nullValueFilter(subscribe.getUserType()));
		this.setPhone(Tools.nullValueFilter(subscribe.getPhone()));
		if(subscribe.getHospital()!=null){
			this.setHospital(subscribe.getHospital().getName());
		}else{
			this.setHospital("");
		}
		if(subscribe.getDepartments()!=null){
			this.setDepartments(subscribe.getDepartments().getName());
		}else{
			this.setDepartments("");
		}
		this.setMark(Tools.nullValueFilter(subscribe.getMark()));
	}

}
