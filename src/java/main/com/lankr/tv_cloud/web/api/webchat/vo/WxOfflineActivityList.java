package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.SignUpUserFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.SignUpUser;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class WxOfflineActivityList extends BaseAPIModel{
	
	private int itemTotalSize;
	
	private List<WxOfflineActivityItem> items;

	public int getItemTotalSize() {
		return itemTotalSize;
	}

	public void setItemTotalSize(int itemTotalSize) {
		this.itemTotalSize = itemTotalSize;
	}

	public List<WxOfflineActivityItem> getItems() {
		return items;
	}

	public void setItems(List<WxOfflineActivityItem> items) {
		this.items = items;
	}
	
	public void wxBuildList(List<OfflineActivity> list){
		this.setStatus(Status.SUCCESS);
		if(Tools.isEmpty(list)){
			return ;
		}
		items=new ArrayList<WxOfflineActivityItem>();
		for (OfflineActivity offlineActivity : list) {
			WxOfflineActivityItem item=new WxOfflineActivityItem();
			item.buildWxList(offlineActivity);
			items.add(item);
		}
	}
	
	public void wxBuildListOfUser(List<OfflineActivity> list,SignUpUserFacade signUpUserFacade){
		this.setStatus(Status.SUCCESS);
		if(Tools.isEmpty(list)){
			return ;
		}
		items=new ArrayList<WxOfflineActivityItem>();
		for (OfflineActivity offlineActivity : list) {
			WxOfflineActivityItem item=new WxOfflineActivityItem();
			int bookNum = signUpUserFacade.bookCountUser(offlineActivity.getId(),
					SignUpUser.REFER_OFFLINEACTIVITY);
			item.buildListOfMy(offlineActivity, bookNum);
			items.add(item);
		}
	}
	
	public void webBuildList(Pagination<OfflineActivity> pagination){
		this.setStatus(Status.SUCCESS);
		this.setItemTotalSize(pagination.getTotal());
		List<OfflineActivity> list=pagination.getResults();
		items=new ArrayList<WxOfflineActivityItem>();
		for (OfflineActivity offlineActivity : list) {
			WxOfflineActivityItem item=new WxOfflineActivityItem();
			item.buildWebList(offlineActivity);
			items.add(item);
		}
	}
	

}
