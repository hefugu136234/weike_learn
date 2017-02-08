package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityConfig;
import com.lankr.tv_cloud.model.ActivityExpert;
import com.lankr.tv_cloud.model.ActivityMessage;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.ActivitySubject;
import com.lankr.tv_cloud.model.ActivityUser;
import com.lankr.tv_cloud.model.Notification;

public interface ActivityMapper {

	public int addActivity(Activity activity);

	public int updateActivity(Activity activity);

	public Activity getActivityByUuid(String uuid);

	public List<Activity> searchDatatableActivities(String query, int from,
			int to);

	public int changeStatus(int id, int status);

	public int changeIsActive(int id, int isActive);

	public Activity getActivityById(int id);

	public ActivityConfig selectActivityConfig(int activityId);

	public int updateActivityConfig(ActivityConfig config);

	public int addActivityConfig(ActivityConfig config);

	public int addActivityResource(ActivityResource activityResource);

	public int updateResourceActivityUseIds(int activityId, int resourceId);

	public List<Activity> searchRecommendActivties(int size);

	public List<Activity> wonderActivites(String startTime, int size);

	public List<ActivityResource> searchActivitieResForDatatable(
			int activityId, String queryKey, int startPage, int pageSize);

	public ActivityResource getActivityResourceByUuid(String uuid);

	public int deleteActivityResource(ActivityResource activityResource);

	public List<ActivityUser> searchActivitieUserForDatatable(int activityId,
			String queryKey, int startPage, int pageSize);

	public ActivityUser getActivityUserByUuid(String uuid);

	public int deleteActivityUser(ActivityUser activityUser);

	public int updateActivityResState(ActivityResource activityResource);

	public ActivityResource getActivityResourceById(int id);

	public int changeActivityResStatus(int id, int status);

	public ActivityUser getActivityUserById(int id);

	public int updateActivityUserIsAvtive(int id, int isActive);

	public void addActivityUser(ActivityUser activityUser);

	public ActivityUser getActivityUserByUserId(int userId,int activityId);

	public List<Activity> searchActivityByCateId(List<Integer> list);

	public ActivityResource searchResourceActivityUseIds(int activityId,
			int resourceId);

	public int updateResourceActivityRelate(ActivityResource activityResource);

	public List<ActivityResource> getResActivityBYchat(int activityId,String startTime,
			 int size);

	@Deprecated
	public List<ActivityResource> oupsRankingByActivityResId(int activityId);

	public int updateActivtyResourceActive(ActivityResource activityResource);

	public int recommendResource(ActivityResource activityRes);

	public int recommendResourceUndo(ActivityResource activityRes);
	
	public int updateActivityUserStatus(ActivityUser activityUser);

	public int getRecommendResourceCount(ActivityResource activityRes);

	public List<ActivityResource> recommendActivityVideo(int activityId);
	
	public List<Activity> collecteActivites(String startTime, int size);
	
	/**
	 * 2016-04-07 添加活动的学科分类
	 */
	public int addActivitySubject(ActivitySubject activitySubject);
	
	public ActivitySubject getActivitySubjectByUuid(String uuid);
	
	public int updateActivitySubject(ActivitySubject activitySubject);
	
	public int deteleActivitySubject(ActivitySubject activitySubject);
	
	public List<ActivitySubject> searchActivitySubjectForWx(int activityId);
	
	public List<ActivitySubject> searchActivitySubjectForTable(String searchValue,int acvivityId, int from, int size);
	
	//kalean begin
	public List<Activity> queryApiUseableActivities(String q);
	//kalean end

	/**
	 * 2016-04-18
	 * 
	 */
	public List<Activity> searchActivityByQrSence(String searchValue);
	
	public List<Activity> searchActivityByWxSubject(String searchValue);

	public List<ActivityExpert> searchActivitieExpertForDatatable(
			int activityId, String queryKey, int startPage, int pageSize);

	public int updateActivityExpertStatus(ActivityExpert activityExpert);

	public ActivityExpert getActivityExpertByUuid(String uuid);

	public int saveActivityExpert(ActivityExpert activityExpert);

	public ActivityExpert getActivityExpertBySpeakerIdAndActivityId(
			ActivityExpert activityExpert);
	
	public List<ActivityExpert> searchExpertByWx(int activityId,String startTime,int size);

	public int recommendExpert(ActivityExpert activityExpert);

	public List<ActivityUser> searchActivitieUserForMessage(@Param("index") int index, @Param("id") int id);
	
	public Integer allActivityCount();

}
