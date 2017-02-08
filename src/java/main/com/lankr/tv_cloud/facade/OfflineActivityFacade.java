package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.User;

public interface OfflineActivityFacade {

	public Pagination<OfflineActivity> searchPaginationOfflineActivity(
			String query, int from, int batch_size);

	public ActionMessage<?> addOfflineActivity(OfflineActivity offlineActivity);

	public OfflineActivity offlineActivityByUuid(String uuid);

	public ActionMessage<?> updateOfflineActivityStatus(OfflineActivity offlineActivity);

	public ActionMessage<?> updateOfflineActivity(OfflineActivity offlineActivity);
	
	public ActionMessage<?> bindInitatorUser(OfflineActivity offlineActivity);
	
	public List<OfflineActivity> wxOfflineActivityList(String startTime,int size);
	
	public Pagination<OfflineActivity> webOfflineActivityList(int from,int size);
	
	public List<OfflineActivity> bookLineActivityOfUserList(int status,User user,String startTime,int size);

}
