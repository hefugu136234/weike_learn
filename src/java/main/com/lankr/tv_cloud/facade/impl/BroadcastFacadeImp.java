package com.lankr.tv_cloud.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.orm.mybatis.mapper.BroadcastMapper;
import com.lankr.tv_cloud.facade.BroadcastFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ActivityUser;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.BroadcastUser;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;

public class BroadcastFacadeImp extends FacadeBaseImpl implements
		BroadcastFacade {

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "com.lankr.orm.mybatis.mapper.BroadcastMapper";
	}

	@Override
	public Broadcast getCastById(int id) {
		// TODO Auto-generated method stub
		return broadMapper.getCastById(id);
	}

	@Override
	public Broadcast getCastByUuid(String uuid) {
		// TODO Auto-generated method stub
		return broadMapper.getCastByUuid(uuid);
	}

	@Override
	public Status addBroadcast(Broadcast broadcast) {
		// TODO Auto-generated method stub
		try {
			int effect = broadMapper.addBroadcast(broadcast);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateBroadcast(Broadcast broadcast) {
		// TODO Auto-generated method stub
		try {
			int effect = broadMapper.updateBroadcast(broadcast);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateBroadcastRes(Broadcast broadcast) {
		try {
			int effect = broadMapper.updateBroadcastRes(broadcast);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateBroadcastStatus(Broadcast broadcast) {
		// TODO Auto-generated method stub
		try {
			int effect = broadMapper.updateBroadcastStatus(broadcast);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateBroadcastCover(Broadcast broadcast) {
		// TODO Auto-generated method stub
		try {
			int effect = broadMapper.updateBroadcastCover(broadcast);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public Pagination<Broadcast> searchBroadcastForTable(String searchValue,
			int from, int size) {
		// TODO Auto-generated method stub
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from broadcast where isActive=1 and (name like '%"
				+ searchValue + "%' or pinyin like '%" + searchValue + "%')";
		Pagination<Broadcast> pagination = initPage(sql, from, size);
		List<Broadcast> list = broadMapper.searchBroadcastForTable(searchValue,
				from, size);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public String getYiDuoUrl(String url, User user) {
		// TODO Auto-generated method stub
		// 测试的先返回
		url = url + "&username=" + user.getUsername() + "&mobile="
				+ user.getUsername() + "&regist=true";
		return url;
	}

	@Override
	public Status addBroadcastUser(BroadcastUser broadcastUser) {
		// TODO Auto-generated method stub
		try {
			int effect = broadMapper.addBroadcastUser(broadcastUser);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public Pagination<BroadcastUser> searchBroadcastUserForTable(
			String searchValue, int from, int size, int broadcastId) {
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(broad.id) from broadcast_user broad left join user u on broad.userId=u.id where broad.isActive=1 and broad.broadcastId="
				+ broadcastId
				+ " and (u.username like '%"
				+ searchValue
				+ "%' or u.nickname like '%"
				+ searchValue
				+ "%' or u.phone like '%" + searchValue + "%')";
		Pagination<BroadcastUser> pagination = initPage(sql, from, size);
		List<BroadcastUser> list = broadMapper.searchBroadcastUserForTable(
				broadcastId, searchValue, from, size);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public Status updateBroadcastUserStatus(BroadcastUser broadcastUser) {
		// TODO Auto-generated method stub
		try {
			int effect = broadMapper.updateBroadcastUserStatus(broadcastUser);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public BroadcastUser searchBroadcastUserByUuid(String uuid) {
		// TODO Auto-generated method stub
		return broadMapper.searchBroadcastUserByUuid(uuid);
	}

	@Override
	public BroadcastUser searchBroadcastUserByUserId(User user,
			Broadcast broadcast) {
		return broadMapper.searchBroadcastUserByUserId(user.getId(),
				broadcast.getId());
	}

	@Override
	public int broadcastBookCount(Broadcast broadcast) {
		// TODO Auto-generated method stub
		int num = broadMapper.broadcastBookCount(broadcast.getId());
		return num;
	}

	@Override
	public List<Broadcast> searchBroadcastByQrSence(String searchValue) {
		// TODO Auto-generated method stub
		return broadMapper.searchBroadcastByQrSence(searchValue);
	}

	@Override
	public List<Broadcast> searchCastRecordPage(String startTime, int size) {
		// TODO Auto-generated method stub
		return broadMapper.searchCastRecordPage(startTime, size, new Date());
	}

	@Override
	public List<Broadcast> searchCastCarousel(int size) {
		// TODO Auto-generated method stub
		return broadMapper.searchCastCarousel(size, new Date());
	}

	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月10日
	 * @modifyDate 2016年5月10日
	 * 
	 */
	@Override
	public List<Broadcast> searchApiUseableBroadcast(String query) {
		return broadMapper.searchApiUseableBroadcast(query);
	}

	@Override
	public Pagination<Broadcast> broadcastFrontList(int from, int size) {
		Date date = new Date();
		String nowDate = Tools.formatYMDHMSDate(date);
		String sql = "select count(id) from broadcast where isActive=1 and status=1 and endDate <'"
				+ nowDate + "'";
		Pagination<Broadcast> pagination = initPage(sql, from, size);
		List<Broadcast> list = broadMapper.broadcastFrontList(date, from, size);
		pagination.setResults(list);
		return pagination;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.BroadcastFacade#searchRecentBeginBroadcast()
	 */
	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月10日
	 * @modifyDate 2016年5月10日
	 */
	@Override
	public Broadcast searchRecentBeginBroadcast(Date timeline) {
		return broadMapper.searchRecentBeginBroadcast(timeline);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.BroadcastFacade#searchBeginingBroadcastsInterval
	 * (java.util.Date, java.util.Date)
	 */
	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月10日
	 * @modifyDate 2016年5月10日
	 * 
	 */
	@Override
	public List<Broadcast> searchBeginingBroadcastsInterval(Date from, Date to) {
		return broadMapper.searchBeginingBroadcastsInterval(from, to);
	}

	@Override
	public List<BroadcastUser> searchBroadcastUserForMessage(int index, Broadcast cast) {
		List<BroadcastUser> users = broadMapper.searchBroadcastUserForMessage(index, cast.getId());
		if(null == users){
			users = new ArrayList<BroadcastUser>();
		}
		return users;
	}
	
	@Override
	public List<Broadcast> wxLiveComing() {
		// TODO Auto-generated method stub
		return broadMapper.wxLiveComing(new Date());
	}
	
	@Override
	public int WxLivedCount() {
		// TODO Auto-generated method stub
		Integer count=broadMapper.WxLivedCount(new Date());
		if(count==null){
			return 0;
		}
		return count;
	}
	
	@Override
	public List<Broadcast> wxLiving() {
		// TODO Auto-generated method stub
		return broadMapper.wxLiving(new Date());
	}

}
