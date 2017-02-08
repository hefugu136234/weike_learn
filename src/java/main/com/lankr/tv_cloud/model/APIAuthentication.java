package com.lankr.tv_cloud.model;


import com.lankr.tv_cloud.utils.Tools;

public class APIAuthentication extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7506402908348565441L;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	protected String token;
	protected String remoteIp;

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public static <T extends APIAuthentication> T makePrototype(Class<T> t,
			String remoteIp) throws Exception {
		T clazz = t.newInstance();
		clazz.token = Tools.mkAccessToken();
		clazz.remoteIp = remoteIp;		
		return clazz;
	}
}
