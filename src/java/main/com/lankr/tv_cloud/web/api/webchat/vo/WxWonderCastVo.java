package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.lankr.tv_cloud.facade.BroadcastFacade;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.utils.Tools;

public class WxWonderCastVo {
	
	private List<WxBannerItemVo> bannerList;
	
	private int livedCount;
	
	private int livingCount;
	
	private List<WxBroadcastList> livings;
	
	private int bookLiveCount;
	
	private List<WxBroadcastList> books;
	
	private String todayTime=Tools.formatYMDDate(new Date());

	public List<WxBannerItemVo> getBannerList() {
		return bannerList;
	}

	public void setBannerList(List<WxBannerItemVo> bannerList) {
		this.bannerList = bannerList;
	}
	
	public int getLivingCount() {
		return livingCount;
	}

	public void setLivingCount(int livingCount) {
		this.livingCount = livingCount;
	}

	public int getLivedCount() {
		return livedCount;
	}

	public void setLivedCount(int livedCount) {
		this.livedCount = livedCount;
	}

	public List<WxBroadcastList> getLivings() {
		return livings;
	}

	public void setLivings(List<WxBroadcastList> livings) {
		this.livings = livings;
	}

	public int getBookLiveCount() {
		return bookLiveCount;
	}

	public void setBookLiveCount(int bookLiveCount) {
		this.bookLiveCount = bookLiveCount;
	}

	public List<WxBroadcastList> getBooks() {
		return books;
	}

	public void setBooks(List<WxBroadcastList> books) {
		this.books = books;
	}

	public void buildBanner(List<Banner> banners) {
		if (Tools.isEmpty(banners)) {
			return;
		}
		this.bannerList = new ArrayList<WxBannerItemVo>();
		for (Banner banner : banners) {
			WxBannerItemVo vo = new WxBannerItemVo();
			vo.buildData(banner);
			this.bannerList.add(vo);
		}
	}
	
	public void buildLivComing(List<Broadcast> liveComing,BroadcastFacade broadcastFacade){
		if(Tools.isEmpty(liveComing)){
			this.setBookLiveCount(0);
			return ;
		}
		this.setBookLiveCount(liveComing.size());
		Map<String, WxBroadcastList> map = new HashMap<String, WxBroadcastList>();
		for (Broadcast broadcast : liveComing) {
			String targetTime=Tools.formatYMDDate(broadcast.getBookStartDate());
			if(!judyGroup(targetTime,map)){
				WxBroadcastList item=new WxBroadcastList(targetTime,judyToday(targetTime));
				map.put(targetTime, item);
			}
			WxBroadcastList item=map.get(targetTime);
			item.buildSigleData(broadcast, broadcastFacade);
		}
		this.books=new ArrayList<WxBroadcastList>(map.values());
	}
	
	public void buildLiving(List<Broadcast> living,BroadcastFacade broadcastFacade){
		if(Tools.isEmpty(living)){
			this.setLivingCount(0);
			return ;
		}
		this.setLivingCount(living.size());
		Map<String, WxBroadcastList> map = new HashMap<String, WxBroadcastList>();
		for (Broadcast broadcast : living) {
			String targetTime=Tools.formatYMDDate(broadcast.getStartDate());
			if(!judyGroup(targetTime,map)){
				WxBroadcastList item=new WxBroadcastList(targetTime,judyToday(targetTime));
				map.put(targetTime, item);
			}
			WxBroadcastList item=map.get(targetTime);
			item.buildSigleCarousel(broadcast, broadcastFacade);
		}
		this.livings=new ArrayList<WxBroadcastList>(map.values());
	}
	
	//判断是否为一个分组
	public boolean judyGroup(String targetTime,Map<String, WxBroadcastList> map){
		if(map.containsKey(targetTime)){
			return true;
		}
		return false;
	}
	
	public boolean judyToday(String targetTime){
		if(this.todayTime.equals(targetTime)){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		WxWonderCastVo vo=new WxWonderCastVo();
		System.out.println(vo.todayTime);
	}

}
