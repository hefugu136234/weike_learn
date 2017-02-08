package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceGroup;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class FrontChapterItem {

	private String uuid;

	private String name;
	
	private List<FrontChapterResourse> items;

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

	public List<FrontChapterResourse> getItems() {
		return items;
	}

	public void setItems(List<FrontChapterResourse> items) {
		this.items = items;
	}
	
	public void buildData(NormalCollect normalCollect){
		this.setUuid(normalCollect.getUuid());
		this.setName(OptionalUtils.traceValue(normalCollect, "name"));
	}
	
	public void buildItems(List<ResourceGroup> list){
		if(Tools.isEmpty(list))
			return;
		this.items=new ArrayList<FrontChapterResourse>();
		for (ResourceGroup resourceGroup : list) {
			FrontChapterResourse item=new FrontChapterResourse();
			item.buildBaseData(resourceGroup);
			Resource resource=resourceGroup.getResource();
			if(resource==null){
				continue;
			}
			item.buildResource(resource);
			this.items.add(item);
		}
		
	}
	
	

}
