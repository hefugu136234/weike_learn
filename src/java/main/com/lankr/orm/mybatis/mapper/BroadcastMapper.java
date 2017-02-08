package com.lankr.orm.mybatis.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.BroadcastUser;

public interface BroadcastMapper {

	public Broadcast getCastByUuid(String uuid);

	public Broadcast getCastById(int id);

	public int addBroadcast(Broadcast broadcast);

	public int updateBroadcast(Broadcast broadcast);

	public int updateBroadcastRes(Broadcast broadcast);

	public int updateBroadcastStatus(Broadcast broadcast);

	public int updateBroadcastCover(Broadcast broadcast);

	public List<Broadcast> searchBroadcastForTable(String searchValue,
			int from, int size);

	public int addBroadcastUser(BroadcastUser broadcastUser);

	public int updateBroadcastUserStatus(BroadcastUser broadcastUser);

	public List<BroadcastUser> searchBroadcastUserForTable(int broadcastId,
			String searchValue, int from, int size);

	public BroadcastUser searchBroadcastUserByUuid(String uuid);

	public BroadcastUser searchBroadcastUserByUserId(int userId, int broadcastId);

	public int broadcastBookCount(int broadcastId);

	public List<Broadcast> searchBroadcastByQrSence(String searchValue);

	public List<Broadcast> searchCastRecordPage(String startTime, int size,
			Date nowDate);

	public List<Broadcast> searchCastCarousel(int size, Date nowDate);

	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月10日
	 * @modifyDate 2016年5月10日
	 * 
	 */
	public List<Broadcast> searchApiUseableBroadcast(String query);

	public List<Broadcast> broadcastFrontList(Date nowDate, int from, int size);

	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月10日
	 * @modifyDate 2016年5月10日
	 * 
	 */
	public Broadcast searchRecentBeginBroadcast(Date timeline);

	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月10日
	 * @modifyDate 2016年5月10日
	 * 
	 */
	public List<Broadcast> searchBeginingBroadcastsInterval(Date from, Date to);

	public List<BroadcastUser> searchBroadcastUserForMessage(@Param("index") int index, @Param("id") int id);
	
	public List<Broadcast> wxLiveComing(Date nowDate);
	
	public Integer WxLivedCount(Date nowDate);
	
	public List<Broadcast> wxLiving(Date nowDate);

}
