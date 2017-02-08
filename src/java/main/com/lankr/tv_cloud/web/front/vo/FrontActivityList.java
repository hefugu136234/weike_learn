package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class FrontActivityList extends BaseAPIModel{
	
	private List<FrontActivityItem> itemList;

	public List<FrontActivityItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<FrontActivityItem> itemList) {
		this.itemList = itemList;
	}
	
	public void buildIndexActivity(List<Activity> list){
		itemList=new ArrayList<FrontActivityItem>();
		if(list!=null&&!list.isEmpty()){
			for (Activity activity : list) {
				FrontActivityItem item=new FrontActivityItem();
				item.buildIndexActivity(activity);
				itemList.add(item);
			}
		}
	}

}
