package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.LogisticsAddress;
import com.lankr.tv_cloud.model.LogisticsInfo;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class LogisticsInfoVo extends BaseAPIModel {

	private String name;

	private String phone;

	private String address;

	private String uuid;

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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void buildData(LogisticsInfo logisticsInfo) {
		this.setStatus(Status.SUCCESS);
		String logistics = logisticsInfo.getLogistics();
		LogisticsAddress logisticsAddress = LogisticsAddress
				.resolveAddressJson(logistics);
		if (logisticsAddress != null) {
			this.setUuid(logisticsInfo.getUuid());
			this.setName(logisticsAddress.getName());
			this.setPhone(logisticsAddress.getPhone());
			this.setAddress(logisticsAddress.getAddress());
		}
	}
}
