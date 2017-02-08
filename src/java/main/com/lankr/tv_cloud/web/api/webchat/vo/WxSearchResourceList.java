package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.List;

import com.lankr.tv_cloud.vo.api.BaseAPIModel;


public class WxSearchResourceList extends BaseAPIModel{

	private List<WxSearchResourceItem> resourceVoItems ;

	public List<WxSearchResourceItem> getResourceVoItems() {
		return resourceVoItems;
	}

	public void setResourceVoItems(List<WxSearchResourceItem> resourceVoItems) {
		this.resourceVoItems = resourceVoItems;
	}
	
	
}
