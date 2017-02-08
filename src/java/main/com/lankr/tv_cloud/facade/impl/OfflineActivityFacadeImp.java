package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import com.lankr.orm.mybatis.mapper.OfflineActivityMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.OfflineActivityFacade;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.User;

public class OfflineActivityFacadeImp extends FacadeBaseImpl implements
		OfflineActivityFacade {

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return OfflineActivityMapper.class.getName();
	}

	@Override
	public Pagination<OfflineActivity> searchPaginationOfflineActivity(
			String searchValue, int from, int size) {
		// TODO Auto-generated method stub
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from offline_activity where isActive=1 and (name like '%"
				+ searchValue + "%' or pinyin like '%" + searchValue + "%')";
		Pagination<OfflineActivity> pagination = initPage(sql, from, size);
		List<OfflineActivity> list = offlineActivityMapper
				.searchPaginationOfflineActivity(searchValue, from, size);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public ActionMessage<?> addOfflineActivity(OfflineActivity offlineActivity) {
		// TODO Auto-generated method stub
		try {
			int effect = offlineActivityMapper
					.addOfflineActivity(offlineActivity);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("线下活动保存失败", e);
		}
		return ActionMessage.failStatus("线下活动保存失败");
	}

	@Override
	public OfflineActivity offlineActivityByUuid(String uuid) {
		// TODO Auto-generated method stub
		return offlineActivityMapper.getofflineActivityByUuid(uuid);
	}

	@Override
	public ActionMessage<?> updateOfflineActivity(
			OfflineActivity offlineActivity) {
		// TODO Auto-generated method stub
		try {
			int effect = offlineActivityMapper
					.updateOfflineActivity(offlineActivity);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("线下活动修改失败", e);
		}
		return ActionMessage.failStatus("线下活动修改失败");
	}

	@Override
	public ActionMessage<?> updateOfflineActivityStatus(
			OfflineActivity offlineActivity) {
		// TODO Auto-generated method stub
		try {
			int effect = offlineActivityMapper
					.updateOfflineActivityStatus(offlineActivity);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("线下活动修改状态失败", e);
		}
		return ActionMessage.failStatus("线下活动修改状态失败");
	}

	@Override
	public ActionMessage<?> bindInitatorUser(OfflineActivity offlineActivity) {
		// TODO Auto-generated method stub
		try {
			int effect = offlineActivityMapper
					.bindInitatorUser(offlineActivity);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("线下活动绑定发起人", e);
		}
		return ActionMessage.failStatus("线下活动绑定发起人");
	}

	@Override
	public List<OfflineActivity> wxOfflineActivityList(String startTime,
			int size) {
		// TODO Auto-generated method stub
		return offlineActivityMapper.wxOfflineActivityList(startTime, size);
	}

	@Override
	public Pagination<OfflineActivity> webOfflineActivityList(int from, int size) {
		// TODO Auto-generated method stub
		String sql = "select count(id) from offline_activity where isActive=1 and status=1";
		Pagination<OfflineActivity> pagination = initPage(sql, from, size);
		List<OfflineActivity> list = offlineActivityMapper
				.webOfflineActivityList(from, size);
		pagination.setResults(list);
		return pagination;
	}
	
	@Override
	public List<OfflineActivity> bookLineActivityOfUserList(int status,
			User user, String startTime, int size) {
		// TODO Auto-generated method stub
		if(user==null){
			return null;
		}
		return offlineActivityMapper.bookLineActivityOfUserList(status, user.getId(), startTime, size);
	}

}
