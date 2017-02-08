package com.lankr.tv_cloud.web.api.webchat.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lankr.tv_cloud.utils.Tools;

public class WxAccseeTokenByCode {
	
	private String access_token;
	
	private int expires_in;
	
	private String refresh_token;
	
	private String openid;
	
	private String scope;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public static WxAccseeTokenByCode instanceToken(String message){
		if(Tools.isBlank(message))
			return null;
		if(message.contains("errmsg")){
			//此次请求opneid失败，code被回调2次;
			return null;
		}
		Gson gson=new Gson();
		WxAccseeTokenByCode accseeTokenByCode=null;
		try {
			accseeTokenByCode=gson.fromJson(message, WxAccseeTokenByCode.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accseeTokenByCode;
	}

}
