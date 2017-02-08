package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class WxActivityList extends BaseAPIModel {

	private List<WxActivityItem> items;

	public List<WxActivityItem> getItems() {
		return items;
	}

	public void setItems(List<WxActivityItem> items) {
		this.items = items;
	}

	public void buildWonderList(List<Activity> list) {
		this.setStatus(Status.SUCCESS);
		if (Tools.isEmpty(list)) {
			return;
		}
		this.items = new ArrayList<WxActivityItem>();
		for (Activity activity : list) {
			WxActivityItem item = new WxActivityItem();
			item.buildWonderItem(activity);
			this.items.add(item);
		}
	}
}
