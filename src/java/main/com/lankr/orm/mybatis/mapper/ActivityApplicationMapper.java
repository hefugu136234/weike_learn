package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityApplication;

public interface ActivityApplicationMapper {

	public void addActivityapplication(ActivityApplication activityApplication);

	public ActivityApplication getApplicateByUuid(String uuid);

	public ActivityApplication getApplicateByCode(String code);

	public List<ActivityApplication> searchApplicateList(int activityId);

	public List<ActivityApplication> searchApplicateByUserId(
			@Param("type") int type, @Param("userId") int userId,
			@Param("startTime") String startTime, @Param("size") int size);

	public String maxOpusCode();

	public Integer oupsResCount(int resId);

	public ActivityApplication relatedResOups(int resId);

	public int updateRelateOups(ActivityApplication activityApplication);

	public List<ActivityApplication> selectOupsPage(SubParams params);

	// public int removeBinding(ActivityApplication activityApplication);

	public List<ActivityApplication> getOupsByActivityId(int activityId);

	public List<ActivityApplication> oupsRankingByActivityId(int activityId);

}
