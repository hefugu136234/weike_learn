package com.lankr.tv_cloud.vo;

public class CCVideoData {

	public CCVideo getVideo() {
		return video;
	}

	public void setVideo(CCVideo video) {
		this.video = video;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	private CCVideo video;

	private String error;

	public boolean videoAvailable() {
		return !"INVALID_REQUEST".equals(error);
	}
}
