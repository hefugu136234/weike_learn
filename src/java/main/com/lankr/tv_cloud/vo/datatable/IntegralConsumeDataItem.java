package com.lankr.tv_cloud.vo.datatable;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;


public class IntegralConsumeDataItem{
	
	private String name;
	private String createDate;
	private String cover;
	private int integral;
	private int type;
	private int status;
	private String uuid;
	
	public static IntegralConsumeDataItem build(IntegralConsume ingegralConsume) {
		if (ingegralConsume == null)
			return null;
		IntegralConsumeDataItem item = new IntegralConsumeDataItem();
		
		item.setName(OptionalUtils.traceValue(ingegralConsume, "name"));
		item.setCreateDate(Tools.formatYMDHMSDate(ingegralConsume.getCreateDate()));
		item.setCover(OptionalUtils.traceValue(ingegralConsume, "cover"));
		//item.setCover(HtmlUtils.htmlEscape(ingegralConsume.getCover()));
		item.setIntegral(OptionalUtils.traceInt(ingegralConsume, "integral"));
		item.setType(OptionalUtils.traceInt(ingegralConsume, "type"));
		item.setStatus(ingegralConsume.getStatus());
		item.setUuid(ingegralConsume.getUuid());
		
		return item;
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
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	private String price;
	
	private String mark;
	
	private int sign;
	


	public int getSign() {
		return sign;
	}


	public void setSign(int sign) {
		this.sign = sign;
	}


	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}


	public int getUserLimited() {
		return userLimited;
	}


	public void setUserLimited(int userLimited) {
		this.userLimited = userLimited;
	}

	private int number;
	
	private int userLimited;
	
	
	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public String getMark() {
		return mark;
	}


	public void setMark(String mark) {
		this.mark = mark;
	}


	public static IntegralConsumeDataItem updateData(IntegralConsume ingegralConsume) {
		if (ingegralConsume == null)
			return null;
		IntegralConsumeDataItem item = new IntegralConsumeDataItem();
		
		item.setName(OptionalUtils.traceValue(ingegralConsume, "name"));
		item.setCreateDate(Tools.formatYMDHMSDate(ingegralConsume.getCreateDate()));
		item.setCover(OptionalUtils.traceValue(ingegralConsume, "cover"));
		item.setIntegral(OptionalUtils.traceInt(ingegralConsume, "integral"));
		item.setType(OptionalUtils.traceInt(ingegralConsume, "type"));
		item.setStatus(ingegralConsume.getStatus());
		if(ingegralConsume.getPrice()>0){
			item.setPrice(String.valueOf(ingegralConsume.getPrice()));
		}
		item.setMark(Tools.nullValueFilter(ingegralConsume.getDescription()));
		item.setUuid(ingegralConsume.getUuid());
		item.setNumber(OptionalUtils.traceInt(ingegralConsume, "number"));
		item.setUserLimited(OptionalUtils.traceInt(ingegralConsume, "userLimited"));
		item.setSign(OptionalUtils.traceInt(ingegralConsume, "sign"));
		return item;
	}
}


