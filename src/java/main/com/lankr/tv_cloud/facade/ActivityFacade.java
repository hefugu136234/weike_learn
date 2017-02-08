package com.lankr.tv_cloud.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityConfig;
import com.lankr.tv_cloud.model.ActivityExpert;
import com.lankr.tv_cloud.model.ActivityMessage;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.ActivitySubject;
import com.lankr.tv_cloud.model.ActivityUser;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.Notification;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;

public interface ActivityFacade extends BaseFacade<Activity> {

	// public ActionMessage addActivity(Activity activity);
	//
	// public ActionMessage updateActivity(Activity activity);
	//
	// public Activity getActivityByUuid(String uuid);

	public Pagination<Activity> searchActivitiesForDatatable(String qurey,
			int from, int to);

	public Activity changeStatus(Activity activity, int status);

	public ActivityConfig getActivityConfig(Activity activity);

	// 保存活动配置
	public ActionMessage saveActivtyConfig(ActivityConfig config);

	// 记录参与活动的人
	public ActionMessage recordActivityUser(Activity activity, User user);

	// 记录参与活动的资源
	public ActionMessage recordActivityResource(Activity activity,
			Resource resource);

	// 获取推荐的活动
	public List<Activity> recommendActivities(int size);

	// 精彩活动
	public List<Activity> wonderActivites(String startTime, int size);

	public Pagination<ActivityResource> searchActivitieResForDatatable(
			String activityUuid, String queryKey, int startPage, int pageSize);

	public ActionMessage saveActivityResource(ActivityResource activityResource);

	public ActivityResource getActivityResourceByUuid(String uuid);

	public ActionMessage deleteActivityResource(
			ActivityResource activityResource);

	public Pagination<ActivityUser> searchActivitieUserForDatatable(
			String activityUuid, String queryKey, int startPage, int pageSize);

	public ActivityUser getActivityUserByUuid(String uuid);

	public ActionMessage deleteActivityUser(ActivityUser activityUser);

	public ActivityResource updateActivityResState(
			ActivityResource activityResource);

	public ActivityResource changeActivityResStatus(
			ActivityResource activityResource, int willing);

	public ActivityUser updateActivityUserIsAvtive(ActivityUser activityUser);

	public ActivityUser getActivityUserByUserId(int userId,int activityId);

	public Status addActivityUser(ActivityUser activityUser);

	public List<Activity> searchActivityByCateId(List<Integer> list);

	public ActivityResource searchResourceActivityUseIds(Activity activity,
			Resource resource);

	public Status addResouceActivityRelate(ActivityResource activityResource);

	public Status updateResouceActivityRelate(ActivityResource activityResource);

	public List<ActivityResource> getResActivityBYchat(Activity activity,
			String startTime, int size);

	public ActionMessage updateActivityWithConfig(ActivityConfig config);

	public ActivityResource recommendResource(ActivityResource activityRes);

	public Status updateActivityUserStatus(ActivityUser activityUser);

	public List<ActivityResource> recommendActivityVideo(Activity activity);

	public int getRecommendResourceCount(ActivityResource activityRes);

	// 征稿上传活动
	public List<Activity> collecteActivites(String startTime, int size);

	public Status addActivitySubject(ActivitySubject activitySubject);

	public ActivitySubject getActivitySubjectByUuid(String uuid);

	public Status updateActivitySubject(ActivitySubject activitySubject);

	public Status deteleActivitySubject(ActivitySubject activitySubject);
	
	public Pagination<ActivitySubject> searchActivitySubjectForTable(String searchValue,int activityId,
			int from, int size);
	
	public List<Activity> queryApiUseableActivities(String q);

	public List<Activity> searchActivityByQrSence(String searchValue);
	
	public Activity getActivityById(int id);
	
	public List<Activity> searchActivityByWxSubject(String searchValue);

	public Pagination<ActivityExpert> searchActivitieExpertForDatatable(
			String activityUuid, String queryKey, int startPage, int pageSize);

	public ActionMessage updateActivityExpertStatus(
			ActivityExpert activityExpert);

	public ActivityExpert getActivityExpertByUuid(String uuid);


	
	//微信前台返回专家列表(活动)
	public List<ActivityExpert> searchExpertByWx(int activityId,String startTime,int size);

	public ActionMessage saveActivityExpert(ActivityExpert nc, HashMap<Integer,String> images);

	public ActionMessage recommendExpert(ActivityExpert activityExpert);

	public List<ActivityUser> searchActivitieUserForMessage(int index, Activity activity);
	
	public int allActivityCount();

}
