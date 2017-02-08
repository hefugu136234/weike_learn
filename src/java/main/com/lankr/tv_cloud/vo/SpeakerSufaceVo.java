package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.vo.ItemVo;

public class SpeakerSufaceVo {
	
	private String uuid;
	
	private String name;

	private String mobile;
	
	private String province;
	
	private String city;
	
	private String hospital;

	private String department;// 科室

	private String position;// 职位

	private String resume;
	
	private String avatar;

	private int sex;

	private List<ItemVo> proList;
	
	private List<ItemVo> cityList;
	
	private List<ItemVo> hosList;
	
	private List<ItemVo> desList;
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}
	
	


	
	public List<ItemVo> getProList() {
		return proList;
	}

	public void setProList(List<ItemVo> proList) {
		this.proList = proList;
	}

	public List<ItemVo> getCityList() {
		return cityList;
	}

	public void setCityList(List<ItemVo> cityList) {
		this.cityList = cityList;
	}

	public List<ItemVo> getHosList() {
		return hosList;
	}

	public void setHosList(List<ItemVo> hosList) {
		this.hosList = hosList;
	}

	public List<ItemVo> getDesList() {
		return desList;
	}

	public void setDesList(List<ItemVo> desList) {
		this.desList = desList;
	}

	public void buildData(Speaker speaker){
		if(speaker==null)
			return;
		this.setUuid(speaker.getUuid());
		this.setName(Tools.nullValueFilter(speaker.getName()));
		this.setSex(speaker.getSex());
		this.setMobile(Tools.nullValueFilter(speaker.getMobile()));
		this.setPosition(Tools.nullValueFilter(speaker.getPosition()));
		this.setResume(Tools.nullValueFilter(speaker.getResume()));
		this.setProvince(OptionalUtils.traceValue(speaker, "hospital.province.uuid"));
		this.setCity(OptionalUtils.traceValue(speaker, "hospital.city.uuid"));
		this.setHospital(OptionalUtils.traceValue(speaker, "hospital.uuid"));
		this.setDepartment(OptionalUtils.traceValue(speaker, "department.uuid"));
		this.setAvatar(Tools.nullValueFilter(speaker.getAvatar()));
	}
	
	public void buildProvince(List<Province> list) {
		if(list==null||list.size()==0)
			return ;
		this.proList = new ArrayList<ItemVo>();
		for (Province province : list) {
			ItemVo app = new ItemVo();
			app.setUuid(province.getUuid());
			app.setName(province.getName());
			this.proList.add(app);
		}
	}

	public void buildCity(List<City> list) {
		if(list==null||list.size()==0)
			return ;
		this.cityList = new ArrayList<ItemVo>();
		for (City city : list) {
			ItemVo app = new ItemVo();
			app.setUuid(city.getUuid());
			app.setName(city.getName());
			this.cityList.add(app);
		}
	}

	public void buildHospital(List<Hospital> list) {
		if(list==null||list.size()==0)
			return ;
		this.hosList = new ArrayList<ItemVo>();
		for (Hospital hospital : list) {
			ItemVo app = new ItemVo();
			app.setUuid(hospital.getUuid());
			app.setName(hospital.getName());
			this.hosList.add(app);
		}
	}

	public void buildDepartments(List<Departments> list) {
		if(list==null||list.size()==0)
			return ;
		this.desList = new ArrayList<ItemVo>();
		for (Departments departments : list) {
			ItemVo app = new ItemVo();
			app.setUuid(departments.getUuid());
			app.setName(departments.getName());
			this.desList.add(app);
		}
	}

}
