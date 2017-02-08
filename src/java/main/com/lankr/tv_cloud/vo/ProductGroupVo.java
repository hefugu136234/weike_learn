package com.lankr.tv_cloud.vo;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.ProductGroup;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class ProductGroupVo extends BaseAPIModel{
	
	private String uuid;

	private String name;

	private String createDate;
	
	private int isStatus;
	
	private String address;
	
	private String taskId;
	
	private String manufacturer;
	
	private String manufacturerUuid;
	
	private String serialNum;
	
	
	
	

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getManufacturerUuid() {
		return manufacturerUuid;
	}

	public void setManufacturerUuid(String manufacturerUuid) {
		this.manufacturerUuid = manufacturerUuid;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(int isStatus) {
		this.isStatus = isStatus;
	}
	
	
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void buildData(Manufacturer muManufacturer){
		this.setUuid(muManufacturer.getUuid());
		this.setName(Tools.nullValueFilter(HtmlUtils.htmlEscape(muManufacturer
				.getName())));
		this.setCreateDate(Tools.df1.format(muManufacturer.getCreateDate()));
		this.setIsStatus(muManufacturer.getStatus());
		this.setAddress(Tools.nullValueFilter(muManufacturer.getAddress()));
		this.setTaskId(Tools.nullValueFilter(muManufacturer.getTaskId()));
		this.setSerialNum(Tools.nullValueFilter(muManufacturer.getSerialNum()));
	}
	
	public void buildData(ProductGroup productGroup){
		this.setUuid(productGroup.getUuid());
		this.setName(Tools.nullValueFilter(HtmlUtils.htmlEscape(productGroup
				.getName())));
		this.setCreateDate(Tools.df1.format(productGroup.getCreateDate()));
		this.setIsStatus(productGroup.getStatus());
		this.setManufacturer(OptionalUtils.traceValue(productGroup, "manufacturer.name"));
		this.setManufacturerUuid(OptionalUtils.traceValue(productGroup, "manufacturer.uuid"));
		this.setSerialNum(Tools.nullValueFilter(productGroup.getSerialNum()));
	}

}
