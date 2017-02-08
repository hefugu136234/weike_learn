package com.lankr.tv_cloud.model;

public class AssetPrice extends BaseModel {

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	private float price;

	private Video video;
}
