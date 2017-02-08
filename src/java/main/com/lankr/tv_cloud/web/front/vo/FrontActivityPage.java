package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.vo.ActivityTotalApiData;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.api.tv.ResourceItem;

public class FrontActivityPage extends BaseAPIModel {

	private FrontActivityItem activityData;

	private List<FrontResourceItem> recommenList;

	private List<FrontResourceItem> reportList;

	private List<FrontResourceItem> rankList;

	private List<FrontResourceItem> allOupsList;

	private int allOupsCount;

	public FrontActivityItem getActivityData() {
		return activityData;
	}

	public void setActivityData(FrontActivityItem activityData) {
		this.activityData = activityData;
	}

	public List<FrontResourceItem> getRecommenList() {
		return recommenList;
	}

	public void setRecommenList(List<FrontResourceItem> recommenList) {
		this.recommenList = recommenList;
	}

	public List<FrontResourceItem> getReportList() {
		return reportList;
	}

	public void setReportList(List<FrontResourceItem> reportList) {
		this.reportList = reportList;
	}

	public List<FrontResourceItem> getRankList() {
		return rankList;
	}

	public void setRankList(List<FrontResourceItem> rankList) {
		this.rankList = rankList;
	}

	public int getAllOupsCount() {
		return allOupsCount;
	}

	public void setAllOupsCount(int allOupsCount) {
		this.allOupsCount = allOupsCount;
	}

	public List<FrontResourceItem> getAllOupsList() {
		return allOupsList;
	}

	public void setAllOupsList(List<FrontResourceItem> allOupsList) {
		this.allOupsList = allOupsList;
	}

	public FrontActivityPage() {

	}

	public FrontActivityPage(boolean flag) {
		if (flag) {
			this.activityData = new FrontActivityItem();
			this.recommenList = new ArrayList<FrontResourceItem>();
			this.reportList = new ArrayList<FrontResourceItem>();
			this.rankList = new ArrayList<FrontResourceItem>();
			this.allOupsList = new ArrayList<FrontResourceItem>();
		}

	}

	public void buildActivityTotal(Activity activity,
			ActivityTotalApiData data, ResourceFacade cacheResourceFacade) {
		this.activityData.buildDetail(activity);

		List<ResourceItem> recommends = data.recommends;
		addItemList(recommends, this.recommenList, 4);
		
		List<ResourceItem> reports=data.reports;
		addItemList(reports, this.reportList);
		
		List<ResourceItem> ranks=data.ranks;
		addItemList(ranks, this.rankList,10);
		
		Pagination<Resource> pagination=cacheResourceFacade.resourceActivityFrontPage(activity.getId(), 0, 10);
		this.setAllOupsCount(pagination.getTotal());
		
		List<Resource> resources=pagination.getResults();
		addItemResList(resources, this.allOupsList);

	}
	
	public void addItemResList(List<Resource> list,List<FrontResourceItem> addList){
		if (list == null || list.isEmpty()) {
			return ;
		}
		for (Resource resource : list) {
			FrontResourceItem item=new FrontResourceItem();
			item.buildItemList(resource);
			addList.add(item);
		}
	}

	public void addItemList(List<ResourceItem> list,
			List<FrontResourceItem> addList) {
		if (list == null || list.isEmpty()) {
			return ;
		}
		for (ResourceItem orItem : list) {
			FrontResourceItem item=new FrontResourceItem();
			item.buildActivityRes(orItem);
			addList.add(item);
		}
	}

	public void addItemList(List<ResourceItem> list,
			List<FrontResourceItem> addList,int size) {
		if (list == null || list.isEmpty()) {
			return ;
		}
		if(list.size()<size){
			size=list.size();
		}
		for (int i = 0; i < size; i++) {
			ResourceItem orItem=list.get(i);
			FrontResourceItem item=new FrontResourceItem();
			item.buildActivityRes(orItem);
			addList.add(item);
		}
	}

}
