package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.annotations.DataAlias;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

//import com.lankr.tv_cloud.model.UserExpand;

public class SpeakerVo extends BaseAPIModel{
	
	@DataAlias
	String uuid = "";
	@DataAlias
	String name = "";
	@DataAlias
	int sex;
	String mobile = "";
	String date = "";
	@DataAlias
	String hospitalName = "";
	String department = "";
	String avatar = "";
	String professor = "";
	public int isStatus;
	private String userName;
	private String userNickName;
	private int userId;
	
	private String userUuid;
	
	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	@SuppressWarnings("unused")
	private final static transient SpeakerVo empty = new SpeakerVo();
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(int isStatus) {
		this.isStatus = isStatus;
	}
	

	public void build(Speaker speaker) {
		if (speaker == null || !speaker.hasPersisted())
			return;
		setStatus(Status.SUCCESS);
		avatar = Tools.nullValueFilter(speaker.getAvatar());
		uuid = Tools.nullValueFilter(speaker.getUuid());
		name = Tools.nullValueFilter(speaker.getName());
		sex = speaker.getSex();
		mobile = Tools.nullValueFilter(speaker.getMobile());
		date = Tools.nullValueFilter(Tools.formatYMDHMSDate(speaker
				.getModifyDate()));
		Hospital h = speaker.getHospital();
		if (h != null) {
			hospitalName = Tools.nullValueFilter(h.getName());
		}
		Departments de = speaker.getDepartment();
		if (de != null) {
			department = Tools.nullValueFilter(de.getName());
		}
		professor = Tools.nullValueFilter(speaker.getPosition());
		this.setIsStatus(speaker.getStatus());
		this.setUserName(OptionalUtils.traceValue(speaker, "user.username"));
		this.setUserNickName(OptionalUtils.traceValue(speaker, "user.nickname"));
		this.setUserId(OptionalUtils.traceInt(speaker, "user.id"));
		this.setUserUuid(OptionalUtils.traceValue(speaker, "user.uuid"));
	}
}