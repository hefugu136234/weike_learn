package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lankr.tv_cloud.facade.BroadcastFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class WxBroadcastList extends BaseAPIModel{
	
	private List<WxBroadcastItem> itemList;
	
	private String startTime;
	
	private boolean todayFlag;
	
	private Date todayDate=new Date();

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public List<WxBroadcastItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<WxBroadcastItem> itemList) {
		this.itemList = itemList;
	}
	
	
	public boolean isTodayFlag() {
		return todayFlag;
	}

	public void setTodayFlag(boolean todayFlag) {
		this.todayFlag = todayFlag;
	}
	
	public WxBroadcastList(){
		
	}
	
	public WxBroadcastList(String startTime,boolean todayFlag){
		this.startTime=startTime;
		this.todayFlag=todayFlag;
		this.itemList=new ArrayList<WxBroadcastItem>();
	}

	public void buildData(List<Broadcast> list,BroadcastFacade broadcastFacade){
		this.setStatus(Status.SUCCESS);
		if(list==null||list.isEmpty()){
			return ;
		}
		this.itemList=new ArrayList<WxBroadcastItem>();
		for (Broadcast broadcast : list) {
			int existNUm = broadcastFacade.broadcastBookCount(broadcast);
			WxBroadcastItem vo=new WxBroadcastItem();
			vo.buildListData(broadcast);
			vo.buildBook(existNUm);
			this.itemList.add(vo);
		}
	}
	
	public void buildCarousel(List<Broadcast> list,BroadcastFacade broadcastFacade){
		this.setStatus(Status.SUCCESS);
		if(list==null||list.isEmpty()){
			return ;
		}
		this.itemList=new ArrayList<WxBroadcastItem>();
		Date nowDate=new Date();
		for (Broadcast broadcast : list) {
			int existNUm = broadcastFacade.broadcastBookCount(broadcast);
			WxBroadcastItem vo=new WxBroadcastItem();
			vo.buildListData(broadcast);
			vo.buildBook(existNUm);
			vo.buildRemainDay(nowDate, broadcast.getEndDate());
			this.itemList.add(vo);
		}
	}
	
	public void buildSigleData(Broadcast broadcast,BroadcastFacade broadcastFacade){
		int existNUm = broadcastFacade.broadcastBookCount(broadcast);
		WxBroadcastItem vo=new WxBroadcastItem();
		vo.buildListData(broadcast);
		vo.buildBook(existNUm);
		this.itemList.add(vo);
	}
	
	public void buildSigleCarousel(Broadcast broadcast,BroadcastFacade broadcastFacade){
		int existNUm = broadcastFacade.broadcastBookCount(broadcast);
		WxBroadcastItem vo=new WxBroadcastItem();
		vo.buildListData(broadcast);
		vo.buildBook(existNUm);
		vo.buildRemainDay(todayDate, broadcast.getEndDate());
		this.itemList.add(vo);
	}

}
