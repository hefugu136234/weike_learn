package com.lankr.tv_cloud.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class LogisticsAddress {
	
	private String name;
	
	private String phone;
	
	private String address;

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
	
	//生成json
	public static String buildAddressJson(ReceiptAddress receiptAddress){
		if(receiptAddress==null)
			return "";
		LogisticsAddress addressModel=new LogisticsAddress();
		addressModel.setName(OptionalUtils.traceValue(receiptAddress, "name"));
		addressModel.setPhone(OptionalUtils.traceValue(receiptAddress, "phone"));
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
		addressModel.setAddress(address);
		Gson gson=new Gson();
		return gson.toJson(addressModel);
		
	}
	
	//解析json
	public static LogisticsAddress resolveAddressJson(String logistics){
		Gson gson=new Gson();
		if(logistics==null){
			return null;
		}
		try {
			LogisticsAddress addressModel=gson.fromJson(logistics, LogisticsAddress.class);
			return addressModel;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	

}
