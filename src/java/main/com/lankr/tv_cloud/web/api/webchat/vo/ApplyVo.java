package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.List;

import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class ApplyVo {
	
	private List<Province> proList;
	
	private List<City> cityList;
	
	private List<Hospital> hosList;
	
	private List<Departments> depList;
	
	private String proUUid;
	
	private String cityUUid;
	
	private String hosUUid;
	
	private String depUUid;
	
	private String professor;
	
	private String phone;
	
	private String name;
	
	private int type;
	
	private List<Manufacturer> manuList;
	
	private String selectManuUuid;

	public List<Province> getProList() {
		return proList;
	}

	public void setProList(List<Province> proList) {
		this.proList = proList;
	}

	public List<City> getCityList() {
		return cityList;
	}

	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}

	public List<Hospital> getHosList() {
		return hosList;
	}

	public void setHosList(List<Hospital> hosList) {
		this.hosList = hosList;
	}

	public List<Departments> getDepList() {
		return depList;
	}

	public void setDepList(List<Departments> depList) {
		this.depList = depList;
	}

	public String getProUUid() {
		return proUUid;
	}

	public void setProUUid(String proUUid) {
		this.proUUid = proUUid;
	}

	public String getCityUUid() {
		return cityUUid;
	}

	public void setCityUUid(String cityUUid) {
		this.cityUUid = cityUUid;
	}

	public String getHosUUid() {
		return hosUUid;
	}

	public void setHosUUid(String hosUUid) {
		this.hosUUid = hosUUid;
	}

	public String getDepUUid() {
		return depUUid;
	}

	public void setDepUUid(String depUUid) {
		this.depUUid = depUUid;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Manufacturer> getManuList() {
		return manuList;
	}

	public void setManuList(List<Manufacturer> manuList) {
		this.manuList = manuList;
	}

	public String getSelectManuUuid() {
		return selectManuUuid;
	}

	public void setSelectManuUuid(String selectManuUuid) {
		this.selectManuUuid = selectManuUuid;
	}
	
	
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	//展示user的个人信息
	//UserExpand type=0 医生 type=1 企业
	public void buildUserShow(User user){
		this.setName(OptionalUtils.traceValue(user, "nickname"));
		int type=OptionalUtils.traceInt(user, "userExpand.type");
		this.setType(type);
		
	}
	

}
