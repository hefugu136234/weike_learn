package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.SignUpUser;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class WxBookUserList extends BaseAPIModel{
	
	private List<WxBookUserItem> items;
	
	private int itemTotalSize;
	
	
	public void setItemTotalSize(int itemTotalSize) {
		this.itemTotalSize = itemTotalSize;
	}

	public int getItemTotalSize() {
		return itemTotalSize;
	}

	public List<WxBookUserItem> getItems() {
		return items;
	}

	public void setItems(List<WxBookUserItem> items) {
		this.items = items;
	}
	
	public void buildList(List<SignUpUser> list){
		this.setStatus(Status.SUCCESS);
		if(Tools.isEmpty(list)){
			return ;
		}
		items=new ArrayList<WxBookUserItem>();
		for (SignUpUser signUpUser : list) {
			WxBookUserItem item=new WxBookUserItem();
			item.buildDataList(signUpUser);
			items.add(item);
		}
	}
	
	public void webBuildList(Pagination<SignUpUser> pagination){
		this.setStatus(Status.SUCCESS);
		this.setItemTotalSize(pagination.getTotal());
		List<SignUpUser> list=pagination.getResults();
		if(Tools.isEmpty(list)){
			return ;
		}
		items=new ArrayList<WxBookUserItem>();
		for (SignUpUser signUpUser : list) {
			WxBookUserItem item=new WxBookUserItem();
			item.buildDataList(signUpUser);
			items.add(item);
		}
	}
	
	

}
