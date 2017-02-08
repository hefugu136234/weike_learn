package com.lankr.tv_cloud.vo.datatable;


import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.UserWorksRecord;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class UserWorksRecordDataItem{
	private String workName;
	private String category;
	private String date;
	private int clickCount;
	private int status;

	public static UserWorksRecordDataItem build(Resource resource) {
		if (null == resource)
			return null;
		UserWorksRecordDataItem item = new UserWorksRecordDataItem();
		item.setWorkName(OptionalUtils.traceValue(resource, "name"));
		item.setCategory(OptionalUtils.traceValue(resource, "category.name"));
		item.setDate(Tools.formatYMDHMSDate(resource.getCreateDate()));
		item.setClickCount(OptionalUtils.traceInt(resource, "viewCount"));
		item.setStatus(resource.getStatus());
		return item;
	}
	
	public String getWorkName() {
		return workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
