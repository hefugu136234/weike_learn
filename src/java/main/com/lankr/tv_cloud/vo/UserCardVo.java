package com.lankr.tv_cloud.vo;

import java.util.Date;

import com.lankr.tv_cloud.model.ActivationCode;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class UserCardVo {
	private String cardNum;
	private String activationDate;
	private String manufacturerName;
	private String duration;	//流量卡的时长
	private String uuid;
	
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	
	public void bulidData(ActivationCode activationCode) {
		if(null == activationCode){
			return;
		}
		this.setCardNum(OptionalUtils.traceValue(activationCode, "cardNum"));
		Date modifyDate = activationCode.getModifyDate();
		this.setActivationDate(Tools.formatYMDHMSDate(modifyDate));
		this.setManufacturerName(OptionalUtils.traceValue(activationCode, "productGroup.manufacturer.name"));
		this.setDuration(OptionalUtils.traceValue(activationCode, "deadline"));
		this.setUuid(OptionalUtils.traceValue(activationCode, "uuid"));
	}
}
