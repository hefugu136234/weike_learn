package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class TopMenuItem {

	private String uuid;

	private String name;

	private int resCount;
	
	private boolean active=false;
	
	private List<FrontResourceItem> itemList;
	
	

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getResCount() {
		return resCount;
	}

	public void setResCount(int resCount) {
		this.resCount = resCount;
	}
	
	
	
	public List<FrontResourceItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<FrontResourceItem> itemList) {
		this.itemList = itemList;
	}
	
	

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void buildData(WxSubject wxSubject){
		this.setUuid(wxSubject.getUuid());
		this.setName(OptionalUtils.traceValue(wxSubject, "name"));
	}
	
	public void buildResList(List<Resource> resources){
		itemList=new ArrayList<FrontResourceItem>();
		if(resources==null||resources.isEmpty()){
			return ;
		}
		for (Resource resource : resources) {
			FrontResourceItem item=new FrontResourceItem();
			item.buildItemList(resource);
			itemList.add(item);
		}
	}

}
