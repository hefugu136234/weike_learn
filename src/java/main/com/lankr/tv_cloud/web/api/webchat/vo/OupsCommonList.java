package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class OupsCommonList extends BaseAPIModel{
	
	private List<OupsCommonItem> itemList;

	public List<OupsCommonItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<OupsCommonItem> itemList) {
		this.itemList = itemList;
	}
	
	public void buildData(User user,List<Message> list){
		this.setStatus(Status.SUCCESS);
		if(list==null||list.isEmpty()){
			return ;
		}
		this.itemList=new ArrayList<OupsCommonItem>();
		for (Message message : list) {
			OupsCommonItem item=new OupsCommonItem();
			item.buildData(user, message);
			itemList.add(item);
		}
	}

}
