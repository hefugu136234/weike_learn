package com.lankr.tv_cloud.broadcast;

public class LivePlatform {
	
	public static final String POST_METHOD="post";
	
	public static final String GET_METHOD="get";
	
	//约定的直播平台id
	private int platfromId;
	//约定的直播平台的名称
	private String platfromName;
	
	private String requestUrl;
	
	private String method;
	public int getPlatfromId() {
		return platfromId;
	}
	public void setPlatfromId(int platfromId) {
		this.platfromId = platfromId;
	}
	public String getPlatfromName() {
		return platfromName;
	}
	public void setPlatfromName(String platfromName) {
		this.platfromName = platfromName;
	}
	
	
	
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public LivePlatform(){}
	
	
	public LivePlatform(int platfromId,String platfromName,String url,String method){
		this.platfromId=platfromId;
		this.platfromName=platfromName;
		this.requestUrl=url;
		this.method=method;
	}

}
