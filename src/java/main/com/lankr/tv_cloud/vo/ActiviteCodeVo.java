package com.lankr.tv_cloud.vo;

import java.util.concurrent.TimeUnit;

import com.lankr.tv_cloud.model.ActivationCode;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class ActiviteCodeVo {
	
	private String cardNum;
	
	private String createDate;
	
	private String manufacturer;
	
	private String group;
	
	private String activeCode;
	
	private int isStatus;
	
	private int isActive;
	
	private long deadline;
	
	private String modifyDate;
	
	private String name;
	
	private String uuid;
	
	private String userUuid;
	
	

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public int getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(int isStatus) {
		this.isStatus = isStatus;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
	
	
	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}
	
	

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void build(ActivationCode code){
		this.setUuid(code.getUuid());
		this.setCardNum(Tools.nullValueFilter(code.getCardNum()));
		this.setCreateDate(Tools.df1.format(code.getCreateDate()));
		this.setActiveCode(Tools.nullValueFilter(code.getActiveCode()));
		this.setGroup(OptionalUtils.traceValue(code, "productGroup.name"));
		this.setManufacturer(OptionalUtils.traceValue(code, "productGroup.manufacturer.name"));
		this.setIsStatus(code.getStatus());
		long deadlinedata=code.getDeadline();
		this.setDeadline(TimeUnit.SECONDS.toDays(deadlinedata));
		this.setName(OptionalUtils.traceValue(code, "user.nickname"));
		this.setModifyDate(Tools.df1.format(code.getModifyDate()));
		this.setUserUuid(OptionalUtils.traceValue(code, "user.uuid"));
	}

}
