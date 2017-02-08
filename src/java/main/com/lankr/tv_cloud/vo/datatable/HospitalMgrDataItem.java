package com.lankr.tv_cloud.vo.datatable;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.vo.ItemVo;

public class HospitalMgrDataItem{

	private String name;
	private String grade;
	private int gradeValue;
	private String createDate;
	private String modifyDate;
	private String address;
	private String uuid;
	private String provinceName;
	private String provinceUuid;
	private String cityName;
	private String cityUuid;
	private String mobile;
	private List<ItemVo> cityList;

	public static HospitalMgrDataItem build(Hospital hospital) {
		if (hospital == null)
			return null;
		HospitalMgrDataItem item = new HospitalMgrDataItem();
		item.setName(HtmlUtils.htmlEscape(hospital.getName()));
		item.setGrade(HtmlUtils.htmlEscape(hospital.getGrade()));
		String tmp_grade = HtmlUtils.htmlEscape(hospital.getGrade());
		item.setCreateDate(Tools.formatYMDHMSDate(hospital.getCreateDate()));
		item.setModifyDate(Tools.formatYMDHMSDate(hospital.getModifyDate()));
		item.setAddress(HtmlUtils.htmlEscape(hospital.getAddress()));
		item.setUuid(hospital.getUuid());
		item.setProvinceName(OptionalUtils.traceValue(hospital, "province.name"));
		item.setProvinceUuid(OptionalUtils.traceValue(hospital, "province.uuid"));
		item.setCityName(OptionalUtils.traceValue(hospital, "city.name"));
		item.setCityUuid(OptionalUtils.traceValue(hospital, "city.uuid"));
		item.setMobile(HtmlUtils.htmlEscape(hospital.getMobile()));
        switch (tmp_grade) {
            case "一级医院":  item.setGradeValue(1);
                     break;
            case "二级医院":  item.setGradeValue(2);
                     break;
            case "三级医院":  item.setGradeValue(3);
                     break;
            case "未评级别医院":  item.setGradeValue(4);
                     break;
            case "民营医院":  item.setGradeValue(5);
		   		 	 break;
		    case "医学院校":	 item.setGradeValue(6);
		   		 	 break;        
            default: item.setGradeValue(0);
                     break;
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getGradeValue() {
		return gradeValue;
	}

	public void setGradeValue(int gradeValue) {
		this.gradeValue = gradeValue;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceUuid() {
		return provinceUuid;
	}

	public void setProvinceUuid(String provinceUuid) {
		this.provinceUuid = provinceUuid;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityUuid() {
		return cityUuid;
	}

	public void setCityUuid(String cityUuid) {
		this.cityUuid = cityUuid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<ItemVo> getCityList() {
		return cityList;
	}

	public void setCityList(List<ItemVo> cityList) {
		this.cityList = cityList;
	}
}
