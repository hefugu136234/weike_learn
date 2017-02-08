package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class WxUserInfoChange {
	
	private String name;
	
	private int type;
	
	private String cityUuid;
	
	private String cityName;
	
	private String hospitalUuid;
	
	private String hospitalName;
	
	private String departmentUuid;
	
	private String departmentName;
	
	private String companyUuid;
	
	private String companyName;
	
	private String professor;
	
	private String title;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCityUuid() {
		return cityUuid;
	}

	public void setCityUuid(String cityUuid) {
		this.cityUuid = cityUuid;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getHospitalUuid() {
		return hospitalUuid;
	}

	public void setHospitalUuid(String hospitalUuid) {
		this.hospitalUuid = hospitalUuid;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getDepartmentUuid() {
		return departmentUuid;
	}

	public void setDepartmentUuid(String departmentUuid) {
		this.departmentUuid = departmentUuid;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}
	
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	private int certificFlag;
	
	private String certificUrl;
	
	private String certifiCredentials;
	
	private String certificMark;
	
	

	public int getCertificFlag() {
		return certificFlag;
	}

	public void setCertificFlag(int certificFlag) {
		this.certificFlag = certificFlag;
	}

	public String getCertificUrl() {
		return certificUrl;
	}

	public void setCertificUrl(String certificUrl) {
		this.certificUrl = certificUrl;
	}

	public String getCertifiCredentials() {
		return certifiCredentials;
	}

	public void setCertifiCredentials(String certifiCredentials) {
		this.certifiCredentials = certifiCredentials;
	}

	public String getCertificMark() {
		return certificMark;
	}

	public void setCertificMark(String certificMark) {
		this.certificMark = certificMark;
	}

	//信息修改展示
	public void changeUserInfo(User user){
		this.setName(OptionalUtils.traceValue(user, "nickname"));
		int type=OptionalUtils.traceInt(user, "userExpand.type");
		this.setType(type);
		if(type==UserExpand.USER_DOCTOR){
			this.setTitle("医生");
		}else if(type==UserExpand.USER_COMPANY){
			this.setTitle("企业");
		}
		String cityUuid=OptionalUtils.traceValue(user, "userExpand.city.uuid");
		this.setCityUuid(cityUuid);
		String cityName=OptionalUtils.traceValue(user, "userExpand.city.name");
		String provinceName=OptionalUtils.traceValue(user, "userExpand.city.province.name");
		if(!Tools.isBlank(cityName)){
			cityName=provinceName+"-"+cityName;
		}
		this.setCityName(cityName);
		String hospitalUuid=OptionalUtils.traceValue(user, "userExpand.hospital.uuid");
		this.setHospitalUuid(hospitalUuid);
		String hospitalName=OptionalUtils.traceValue(user, "userExpand.hospital.name");
		this.setHospitalName(hospitalName);
		String departmentUuid=OptionalUtils.traceValue(user, "userExpand.departments.uuid");
		this.setDepartmentUuid(departmentUuid);
		String departmentName=OptionalUtils.traceValue(user, "userExpand.departments.name");
		this.setDepartmentName(departmentName);
		String companyUuid=OptionalUtils.traceValue(user, "userExpand.manufacturer.uuid");
		this.setCompanyUuid(companyUuid);
		String companyName=OptionalUtils.traceValue(user, "userExpand.manufacturer.name");
		this.setCompanyName(companyName);
		String peofessor = OptionalUtils.traceValue(user, "userExpand.professor");
		if(peofessor.equals("on")){
			peofessor="";
		}
		this.setProfessor(peofessor);
	}
	
	//实名信息展示
	public void certificName(Certification certification,User user){
		this.changeUserInfo(user);
		//certificFlag =3 未上传
		if(certification==null){
			this.setCertificFlag(3);
		}else{
			this.setCertificFlag(certification.getStatus());
		}
		String certificUrl=OptionalUtils.traceValue(certification, "imageUrl");
		this.setCertificUrl(certificUrl);
		String name=OptionalUtils.traceValue(certification, "name");
		if(this.getCertificFlag()!=3){
			//审核通过
			this.setName(name);
		}
		String certifiCredentials=OptionalUtils.traceValue(certification, "credentials");
		this.setCertifiCredentials(certifiCredentials);
		String certificMark=OptionalUtils.traceValue(certification, "mark");
		this.setCertificMark(certificMark);
	}

}
