package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lankr.tv_cloud.facade.BroadcastFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class FrontAcvitiyPageList extends BaseAPIModel {

	private List<FrontActivityItem> activityList;

	private List<FrontBroadcastItem> latestLiveList;

	private List<FrontBroadcastItem> wondeReviewList;

	private int liveCount;

	public List<FrontActivityItem> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<FrontActivityItem> activityList) {
		this.activityList = activityList;
	}

	public List<FrontBroadcastItem> getLatestLiveList() {
		return latestLiveList;
	}

	public void setLatestLiveList(List<FrontBroadcastItem> latestLiveList) {
		this.latestLiveList = latestLiveList;
	}

	public List<FrontBroadcastItem> getWondeReviewList() {
		return wondeReviewList;
	}

	public void setWondeReviewList(List<FrontBroadcastItem> wondeReviewList) {
		this.wondeReviewList = wondeReviewList;
	}

	public int getLiveCount() {
		return liveCount;
	}

	public void setLiveCount(int liveCount) {
		this.liveCount = liveCount;
	}

	public FrontAcvitiyPageList() {

	}

	public FrontAcvitiyPageList(boolean flag) {
		if (flag) {
			this.activityList = new ArrayList<FrontActivityItem>();
			this.latestLiveList = new ArrayList<FrontBroadcastItem>();
			this.wondeReviewList = new ArrayList<FrontBroadcastItem>();
		}
	}
	
	public void buildActivityList(List<Activity> list){
		if(list==null||list.isEmpty()){
			return ;
		}
		for (Activity activity : list) {
			FrontActivityItem item=new FrontActivityItem();
			item.buildIndexActivity(activity);
			this.activityList.add(item);
		}
	}
	
	public void buildLiving(List<Broadcast> list,BroadcastFacade broadcastFacade){
		if(list==null||list.isEmpty()){
			return ;
		}
		Date nowDate=new Date();
		for (Broadcast broadcast : list) {
			FrontBroadcastItem item=new FrontBroadcastItem();
			int existNUm = broadcastFacade.broadcastBookCount(broadcast);
			item.buildCommonData(broadcast);
			item.buildRemainDay(nowDate, broadcast.getEndDate());
			item.buildBook(existNUm);
			this.latestLiveList.add(item);
		}
	}
	
	public void buildLived(Pagination<Broadcast> lived,BroadcastFacade broadcastFacade){
		this.setStatus(Status.SUCCESS);
		this.setLiveCount(lived.getTotal());
		List<Broadcast> list=lived.getResults();
		if(list==null||list.isEmpty()){
			return ;
		}
		if(this.wondeReviewList==null){
			this.wondeReviewList=new ArrayList<FrontBroadcastItem>();
		}
		for (Broadcast broadcast : list) {
			FrontBroadcastItem item=new FrontBroadcastItem();
			int existNUm = broadcastFacade.broadcastBookCount(broadcast);
			item.buildCommonData(broadcast);
			item.buildBook(existNUm);
			this.wondeReviewList.add(item);
		}
	}

}
