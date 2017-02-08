package com.lankr.tv_cloud.web.api.webchat.util;

public class Scene {
	
	/**
	 * 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
	 */
	private long scene_id;
	
	private String scene_str;

	public long getScene_id() {
		return scene_id;
	}

	public void setScene_id(long scene_id) {
		this.scene_id = scene_id;
	}

	public String getScene_str() {
		return scene_str;
	}

	public void setScene_str(String scene_str) {
		this.scene_str = scene_str;
	}

}
