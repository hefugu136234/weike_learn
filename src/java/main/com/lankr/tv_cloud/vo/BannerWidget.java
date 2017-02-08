package com.lankr.tv_cloud.vo;

@SuppressWarnings("all")
public class BannerWidget {
	
	private String categoryId, categoryName, mark;
	private int x, y, offset_x, offset_y;
	private String scriptId;
	private String imageUrl;

	public BannerWidget(String scriptId, String imageUrl, String categoryId,
			String categoryName, int x, int y, int offset_x, int offset_y,
			String mark) {
		this.scriptId = scriptId;
		this.imageUrl = imageUrl;
		this.categoryId = categoryId;
		this.x = x;
		this.y = y;
		this.offset_x = offset_x;
		this.offset_y = offset_y;
		this.categoryName = categoryName;
		this.mark = mark;
	}

	public BannerWidget() { }
	
	
	
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getScriptId() {
		return scriptId;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getOffset_x() {
		return offset_x;
	}

	public void setOffset_x(int offset_x) {
		this.offset_x = offset_x;
	}

	public int getOffset_y() {
		return offset_y;
	}

	public void setOffset_y(int offset_y) {
		this.offset_y = offset_y;
	}
}
