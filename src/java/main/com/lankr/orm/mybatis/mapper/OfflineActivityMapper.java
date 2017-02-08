package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.OfflineActivity;

public interface OfflineActivityMapper {

	public int addOfflineActivity(OfflineActivity offlineActivity);

	public OfflineActivity getofflineActivityByUuid(String uuid);

	public List<OfflineActivity> searchPaginationOfflineActivity(
			@Param("searchValue") String searchValue, @Param("from") int from,
			@Param("size") int size);

	public int updateOfflineActivityStatus(OfflineActivity offlineActivity);

	public int updateOfflineActivity(OfflineActivity offlineActivity);

	public int bindInitatorUser(OfflineActivity offlineActivity);

	public List<OfflineActivity> wxOfflineActivityList(
			@Param("startTime") String startTime, @Param("size") int size);

	public List<OfflineActivity> webOfflineActivityList(
			@Param("from") int from, @Param("size") int size);

	public List<OfflineActivity> bookLineActivityOfUserList(
			@Param("status") int status, @Param("userId") int userId,
			@Param("startTime") String startTime, @Param("size") int size);

}
