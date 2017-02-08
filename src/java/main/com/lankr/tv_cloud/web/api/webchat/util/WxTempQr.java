package com.lankr.tv_cloud.web.api.webchat.util;

public class WxTempQr {
	
	public final static String  QR_SCENE="QR_SCENE";//临时
	
	public final static String  QR_LIMIT_SCENE="QR_LIMIT_SCENE";//永久
	
	public final static String  QR_LIMIT_STR_SCENE="QR_LIMIT_STR_SCENE";//永久字符串
	
	private long expire_seconds;
	
	private String action_name;
	
	private ActionInfo action_info;
	
	
	
	public long getExpire_seconds() {
		return expire_seconds;
	}

	public void setExpire_seconds(long expire_seconds) {
		this.expire_seconds = expire_seconds;
	}

	public String getAction_name() {
		return action_name;
	}

	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}

	public ActionInfo getAction_info() {
		return action_info;
	}

	public void setAction_info(ActionInfo action_info) {
		this.action_info = action_info;
	}

	
	
	

}
