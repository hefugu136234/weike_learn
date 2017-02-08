package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.District;
import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.ReceiptAddress;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class ExchangeComfirm {
	
	private String consumeUuid;
	
	private String consumeName;
	
	private int consumeIntegral;
	
	private String addressUuid;
	
	private String name;
	
	private String phone;
	
	private String address;
	
	private boolean needAddress;
	
	private String consumeCover;

	public String getConsumeUuid() {
		return consumeUuid;
	}

	public void setConsumeUuid(String consumeUuid) {
		this.consumeUuid = consumeUuid;
	}

	public String getConsumeName() {
		return consumeName;
	}

	public void setConsumeName(String consumeName) {
		this.consumeName = consumeName;
	}

	public int getConsumeIntegral() {
		return consumeIntegral;
	}

	public void setConsumeIntegral(int consumeIntegral) {
		this.consumeIntegral = consumeIntegral;
	}

	public String getAddressUuid() {
		return addressUuid;
	}

	public void setAddressUuid(String addressUuid) {
		this.addressUuid = addressUuid;
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
	
	
	
	public boolean isNeedAddress() {
		return needAddress;
	}

	public void setNeedAddress(boolean needAddress) {
		this.needAddress = needAddress;
	}
	
	

	public String getConsumeCover() {
		return consumeCover;
	}

	public void setConsumeCover(String consumeCover) {
		this.consumeCover = consumeCover;
	}

	public void buildData(IntegralConsume integralConsume,ReceiptAddress receiptAddress){
		this.setConsumeUuid(OptionalUtils.traceValue(integralConsume, "uuid"));
		this.setConsumeName(OptionalUtils.traceValue(integralConsume, "name"));
		this.setConsumeIntegral(OptionalUtils.traceInt(integralConsume, "integral"));
		this.setConsumeCover(OptionalUtils.traceValue(integralConsume, "cover"));
		this.setAddressUuid(OptionalUtils.traceValue(receiptAddress, "uuid"));
		this.setName(OptionalUtils.traceValue(receiptAddress, "name"));
		this.setPhone(OptionalUtils.traceValue(receiptAddress, "phone"));
		String address=OptionalUtils.traceValue(receiptAddress, "address");
		City city=receiptAddress.getCity();
		String listAddressString="";
		if(city!=null){
			Province province=city.getProvince();
			if(province!=null){
				listAddressString=province.getName();
			}
			listAddressString=listAddressString+city.getName();
			District district=receiptAddress.getDistrict();
			if(district!=null){
				listAddressString=listAddressString+district.getName();
			}
		}
		address=listAddressString+address;
		this.setAddress(address);
		this.setNeedAddress(true);
	}
	
	public void buildData(IntegralConsume integralConsume){
		this.setConsumeUuid(OptionalUtils.traceValue(integralConsume, "uuid"));
		this.setConsumeName(OptionalUtils.traceValue(integralConsume, "name"));
		this.setConsumeIntegral(OptionalUtils.traceInt(integralConsume, "integral"));
		this.setConsumeCover(OptionalUtils.traceValue(integralConsume, "cover"));
		this.setNeedAddress(false);
	}
	

}
