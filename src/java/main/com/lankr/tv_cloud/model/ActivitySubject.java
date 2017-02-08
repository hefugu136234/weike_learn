package com.lankr.tv_cloud.model;

public class ActivitySubject extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7766409988494218434L;

	private String name;
	
	private String pinyin;
	
	private int status;
	
	private Activity activity;
	
	private Category category;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	

}
