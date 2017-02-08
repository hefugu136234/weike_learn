package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class BroadCastBandVo extends BaseAPIModel{
	
	private String uuid;
	
	private String name;
	
	private String resourceUrl;
	
	private String selectItem;
	
	private List<ChosenItem> optionAdditions;
	
	

	
	public String getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(String selectItem) {
		this.selectItem = selectItem;
	}

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

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public List<ChosenItem> getOptionAdditions() {
		return optionAdditions;
	}

	public void setOptionAdditions(List<ChosenItem> optionAdditions) {
		this.optionAdditions = optionAdditions;
	}
	
	public void buildData(Broadcast broadcast,List<Resource> list){
		this.setStatus(Status.SUCCESS);
		this.setUuid(broadcast.getUuid());
		this.setName(broadcast.getName());
		this.setResourceUrl(Tools.nullValueFilter(broadcast.getResourceUrl()));
		Resource resourceOr=broadcast.getResource();
		if(resourceOr!=null){
			this.setSelectItem(resourceOr.getUuid());
		}
		optionAdditions=new ArrayList<ChosenItem>();
		DynamicSearchVo search=DynamicSearchVo.buildResources(list, "");
		optionAdditions=search.getItems();
	}

}
