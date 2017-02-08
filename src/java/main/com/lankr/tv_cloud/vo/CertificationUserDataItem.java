package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.datatable.IntegralExchangeDataItem;
import com.lankr.tv_cloud.web.api.webchat.vo.ItemVo;

public class CertificationUserDataItem {
	
	private String cityName;
	private String hospitalName;
	private String departmentName;
	private int cityId;
	private int hospitalId;
	private int departmentId;
	private String professor;
	private String companyName;
	private int companyId;
	private int type;
	private String userRealName;
	private String userName;
	private String userNickName;
	
	private String provinceUuid;
	private String provinceName;
	private String cityUuid;
	private String hospitalUuid;
	private String departmentUuid;
	private String companyUuid;
	
	private List<ItemVo> cityList;
	private List<ItemVo> hosList;
	
	private String userRole;
	private String sex;
	private String mark;
	private String userUuid;
	
	
	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

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

	
	public String getProvinceUuid() {
		return provinceUuid;
	}

	public void setProvinceUuid(String provinceUuid) {
		this.provinceUuid = provinceUuid;
	}

	public String getCityUuid() {
		return cityUuid;
	}

	public void setCityUuid(String cityUuid) {
		this.cityUuid = cityUuid;
	}

	public String getHospitalUuid() {
		return hospitalUuid;
	}

	public void setHospitalUuid(String hospitalUuid) {
		this.hospitalUuid = hospitalUuid;
	}

	public String getDepartmentUuid() {
		return departmentUuid;
	}

	public void setDepartmentUuid(String departmentUuid) {
		this.departmentUuid = departmentUuid;
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
	
	public static CertificationUserDataItem build(UserExpand userExpand, Certification certification) {
		if (null == userExpand)
			return null;
		CertificationUserDataItem item = new CertificationUserDataItem();
		item.setCityName(OptionalUtils.traceValue(userExpand, "city.name"));
		item.setHospitalName(OptionalUtils.traceValue(userExpand, "hospital.name"));
		item.setDepartmentName(OptionalUtils.traceValue(userExpand, "departments.name"));
		item.setCityId(OptionalUtils.traceInt(userExpand, "city.id"));
		item.setHospitalId(OptionalUtils.traceInt(userExpand, "hospital.id"));
		item.setDepartmentId(OptionalUtils.traceInt(userExpand, "departments.id"));
		item.setProfessor(OptionalUtils.traceValue(userExpand, "professor"));
		item.setCompanyName(OptionalUtils.traceValue(userExpand, "manufacturer.name"));
		item.setCompanyId(OptionalUtils.traceInt(userExpand, "manufacturer.id"));
		item.setType(OptionalUtils.traceInt(userExpand, "type"));
		item.setUserName(OptionalUtils.traceValue(userExpand, "user.username"));
		item.setUserNickName(OptionalUtils.traceValue(userExpand, "user.nickname"));
		
		item.setProvinceUuid(OptionalUtils.traceValue(userExpand, "city.province.uuid"));
		item.setProvinceName(OptionalUtils.traceValue(userExpand, "city.province.name"));
		item.setCityUuid(OptionalUtils.traceValue(userExpand, "city.uuid"));
		item.setHospitalUuid(OptionalUtils.traceValue(userExpand, "hospital.uuid"));
		item.setDepartmentUuid(OptionalUtils.traceValue(userExpand, "departments.uuid"));
		item.setCompanyUuid(OptionalUtils.traceValue(userExpand, "manufacturer.uuid"));
		item.setSex(OptionalUtils.traceValue(userExpand, "sex"));
		item.setMark(OptionalUtils.traceValue(userExpand, "user.mark"));
		item.setUserUuid(OptionalUtils.traceValue(userExpand, "user.uuid"));
		
		if(null != certification){
			item.setUserRealName(certification.getName());
		}else{
			item.setUserRealName("NO_CER");
		}
		if(null != userExpand.getUser()){
			List<UserReference> list = userExpand.getUser().getUser_reference();
			if(null != list && list.size()>0){
				UserReference ref = list.get(0);
				item.setUserRole(OptionalUtils.traceValue(ref, "role.roleName"));
			}
		}
		
		return item;
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
}
