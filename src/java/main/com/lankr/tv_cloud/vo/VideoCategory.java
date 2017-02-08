package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.model.Category;

public class VideoCategory {
	private String uuid;
	private String categoryName;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public void formatData(Category catgory){
		uuid=catgory.getUuid();
		categoryName=catgory.getName();
	}

}
