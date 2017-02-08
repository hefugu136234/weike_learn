package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.rocketmq.common.filter.impl.Op;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.District;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.ReceiptAddress;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class WxReceiptAddressVo {

	private String uuid;

	private String name;

	private String phone;

	private String address;

	private int defaultAdress;

	private String postCode;

	private String selectProvinceUuid;

	private String selectProvinceName;

	private List<ItemVo> provinceList;

	private String selectCityUuid;

	private String selectCityName;

	private List<ItemVo> cityList;

	private String selectDistrictUuid;

	private String selectDistrictName;

	private List<ItemVo> districtList;

	private String listAddress;

	private String dateTime;

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getListAddress() {
		return listAddress;
	}

	public void setListAddress(String listAddress) {
		this.listAddress = listAddress;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getDefaultAdress() {
		return defaultAdress;
	}

	public void setDefaultAdress(int defaultAdress) {
		this.defaultAdress = defaultAdress;
	}

	public String getSelectProvinceUuid() {
		return selectProvinceUuid;
	}

	public void setSelectProvinceUuid(String selectProvinceUuid) {
		this.selectProvinceUuid = selectProvinceUuid;
	}

	public String getSelectProvinceName() {
		return selectProvinceName;
	}

	public void setSelectProvinceName(String selectProvinceName) {
		this.selectProvinceName = selectProvinceName;
	}

	public List<ItemVo> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<ItemVo> provinceList) {
		this.provinceList = provinceList;
	}

	public String getSelectCityUuid() {
		return selectCityUuid;
	}

	public void setSelectCityUuid(String selectCityUuid) {
		this.selectCityUuid = selectCityUuid;
	}

	public String getSelectCityName() {
		return selectCityName;
	}

	public void setSelectCityName(String selectCityName) {
		this.selectCityName = selectCityName;
	}

	public List<ItemVo> getCityList() {
		return cityList;
	}

	public void setCityList(List<ItemVo> cityList) {
		this.cityList = cityList;
	}

	public String getSelectDistrictUuid() {
		return selectDistrictUuid;
	}

	public void setSelectDistrictUuid(String selectDistrictUuid) {
		this.selectDistrictUuid = selectDistrictUuid;
	}

	public String getSelectDistrictName() {
		return selectDistrictName;
	}

	public void setSelectDistrictName(String selectDistrictName) {
		this.selectDistrictName = selectDistrictName;
	}

	public List<ItemVo> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<ItemVo> districtList) {
		this.districtList = districtList;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public void listData(ReceiptAddress receiptAddress) {
		this.setUuid(receiptAddress.getUuid());
		this.setName(OptionalUtils.traceValue(receiptAddress, "name"));
		this.setPhone(OptionalUtils.traceValue(receiptAddress, "phone"));
		this.setDefaultAdress(receiptAddress.getDefaultAddress());
		String listAddressString = "";
		City city = receiptAddress.getCity();
		if (city != null) {
			Province province = city.getProvince();
			if (province != null) {
				listAddressString = province.getName();
			}
			listAddressString = listAddressString + city.getName();
			District district = receiptAddress.getDistrict();
			if (district != null) {
				listAddressString = listAddressString + district.getName();
			}
		}
		listAddressString = listAddressString
				+ Tools.nullValueFilter(receiptAddress.getAddress());
		this.setListAddress(listAddressString);
		this.setDateTime(Tools.formatYMDHMSDate(receiptAddress.getCreateDate()));
	}

	public void updateData(ReceiptAddress receiptAddress) {
		this.setUuid(receiptAddress.getUuid());
		this.setName(OptionalUtils.traceValue(receiptAddress, "name"));
		this.setPhone(OptionalUtils.traceValue(receiptAddress, "phone"));
		this.setAddress(OptionalUtils.traceValue(receiptAddress, "address"));
		this.setPostCode(OptionalUtils.traceValue(receiptAddress, "postCode"));
		this.setDefaultAdress(receiptAddress.getDefaultAddress());
		String selectCityUuid = OptionalUtils.traceValue(receiptAddress,
				"city.uuid");
		this.setSelectCityUuid(selectCityUuid);
		String proName = OptionalUtils.traceValue(receiptAddress,
				"city.province.name");
		String selectCityName = OptionalUtils.traceValue(receiptAddress,
				"city.name");
		if (!Tools.isBlank(selectCityName)) {
			selectCityName = proName + "-" + selectCityName;
		}
		this.setSelectCityName(selectCityName);
		String selectDistrictName = OptionalUtils.traceValue(receiptAddress,
				"district.name");
		this.setSelectDistrictName(selectDistrictName);
		String selectDistrictUuid = OptionalUtils.traceValue(receiptAddress,
				"district.uuid");
		this.setSelectDistrictUuid(selectDistrictUuid);
	}

	public void buildProvince(Province province, List<Province> list) {
		if (list != null && !list.isEmpty()) {
			provinceList = new ArrayList<ItemVo>();
			for (Province model : list) {
				ItemVo app = new ItemVo();
				app.setUuid(model.getUuid());
				app.setName(model.getName());
				provinceList.add(app);
			}
		}
		if (province != null) {
			this.setSelectProvinceUuid(province.getUuid());
			this.setSelectProvinceName(province.getName());
		}
	}

	public void buildCity(City city, List<City> list) {
		if (list != null && !list.isEmpty()) {
			cityList = new ArrayList<ItemVo>();
			for (City model : list) {
				ItemVo app = new ItemVo();
				app.setUuid(model.getUuid());
				app.setName(model.getName());
				cityList.add(app);
			}
		}
		if (city != null) {
			this.setSelectCityUuid(city.getUuid());
			this.setSelectCityName(city.getName());
		}
	}

	public void buildDistrict(District district, List<District> list) {
		if (list != null && !list.isEmpty()) {
			districtList = new ArrayList<ItemVo>();
			for (District model : list) {
				ItemVo app = new ItemVo();
				app.setUuid(model.getUuid());
				app.setName(model.getName());
				districtList.add(app);
			}
		}
		if (district != null) {
			this.setSelectDistrictUuid(district.getUuid());
			this.setSelectCityName(district.getName());
		}
	}

}
