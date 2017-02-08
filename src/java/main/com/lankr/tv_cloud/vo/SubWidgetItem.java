package com.lankr.tv_cloud.vo;

public class SubWidgetItem {

//	public transient final static String ITEM_TYPE_CATEGORY = "CATEGORY";
//	public transient final static String ITEM_TYPE_RESOURCE = "RESOURCE";

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


//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}


	private int x;
	private int y;
	private int offset_x;
	private int offset_y;
	private String categoryId;
	private String imageUrl;
//	private String type;
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

//	public boolean isResource() {
//		return ITEM_TYPE_RESOURCE.equals(type);
//	}
//
//	public boolean isCategory() {
//		return ITEM_TYPE_CATEGORY.equals(type);
//	}

}