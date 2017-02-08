package com.lankr.tv_cloud.utils;

public class QQSecret {
	private String secretId;
	
	private String secretKey;

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public QQSecret(String secretId, String secretKey) {
		super();
		this.secretId = secretId;
		this.secretKey = secretKey;
	}
	

}
