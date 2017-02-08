package com.lankr.tv_cloud.facade;

import java.util.Date;
import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.BroadcastUser;
import com.lankr.tv_cloud.model.User;

public interface BroadcastFacade {

	public Broadcast getCastById(int id);

	public Broadcast getCastByUuid(String uuid);

	public Status addBroadcast(Broadcast broadcast);

	public Status updateBroadcast(Broadcast broadcast);

	public Status updateBroadcastRes(Broadcast broadcast);

	public Status updateBroadcastStatus(Broadcast broadcast);

	public Status updateBroadcastCover(Broadcast broadcast);

	public Pagination<Broadcast> searchBroadcastForTable(String searchValue,
			int from, int size);

	public String getYiDuoUrl(String url, User user);

	public Status addBroadcastUser(BroadcastUser broadcastUser);

	public Status updateBroadcastUserStatus(BroadcastUser broadcastUser);

	public Pagination<BroadcastUser> searchBroadcastUserForTable(
			String searchValue, int from, int size, int broadcastId);

	public BroadcastUser searchBroadcastUserByUuid(String uuid);

	public BroadcastUser searchBroadcastUserByUserId(User user,
			Broadcast broadcast);

	public int broadcastBookCount(Broadcast broadcast);

	public List<Broadcast> searchBroadcastByQrSence(String searchValue);

	public List<Broadcast> searchCastRecordPage(String startTime, int size);

	public List<Broadcast> searchCastCarousel(int size);

	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月10日
	 * @modifyDate 2016年5月10日 返回api可用的直播数据
	 */
	public List<Broadcast> searchApiUseableBroadcast(String query);

	public Pagination<Broadcast> broadcastFrontList(int from, int size);

	/**
	 * @author Kalean.Xiang
	 * 获取最近最近即将开始的直播
	 * 
	 * @param 统计的起始时间
	 * */
	public Broadcast searchRecentBeginBroadcast(Date timeline);

	/**
	 * @author Kalean.Xiang
	 * 获取指定日期内的(即将开始播放的)直播列表
	 * 
	 * @param from
	 *            统计开始时间
	 * @param to
	 *            统计结束时间
	 * */
	public List<Broadcast> searchBeginingBroadcastsInterval(Date from, Date to);

	/**
	 * @Description: 获取该直播下的所有用户
	 *
	 * @author mayuan
	 * @createDate 2016年6月17日
	 * @modifyDate 2016年6月17日
	 */
	public List<BroadcastUser> searchBroadcastUserForMessage(int index, Broadcast cast);
	
	//微信获取即将开始直播
	public List<Broadcast> wxLiveComing();
	
	//微信获取正在直播
	public List<Broadcast> wxLiving();
	
	//微信获取已结束直播数量
	public int WxLivedCount();

}
