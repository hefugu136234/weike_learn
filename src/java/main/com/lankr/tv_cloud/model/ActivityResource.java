package com.lankr.tv_cloud.model;

import java.util.Date;

public class ActivityResource extends BaseModel {

	private static final long serialVersionUID = -2819679917530146251L;

	private String name;
	private Activity activity;
	private Resource resource;
	private int viewCount;
	private int status;
	private Date recommendDate;

	public Date getRecommendDate() {
		return recommendDate;
	}

	public void setRecommendDate(Date recommendDate) {
		this.recommendDate = recommendDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isCompleted() {
		return hasPersisted(activity) && hasPersisted(resource);
	}

}
