package com.lankr.tv_cloud.facade.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestParam;

import com.lankr.orm.mybatis.mapper.ActivityMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.ActivityFacade;
import com.lankr.tv_cloud.facade.MediaCentralFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityConfig;
import com.lankr.tv_cloud.model.ActivityExpert;
import com.lankr.tv_cloud.model.ActivityMessage;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.ActivitySubject;
import com.lankr.tv_cloud.model.ActivityUser;
import com.lankr.tv_cloud.model.Award;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.Notification;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.tmp.YuanxiaoActive;
import com.lankr.tv_cloud.tmp.YuanxiaoRecord;
import com.lankr.tv_cloud.tmp.YuanxiaoRule;
import com.lankr.tv_cloud.utils.Tools;
import com.sun.org.apache.bcel.internal.generic.INEG;

@SuppressWarnings("all")
public class ActivityFacadeImpl extends FacadeBaseImpl implements
		ActivityFacade {

	@Autowired
	private MediaCentralFacade mediaCentralFacade;

	// @Override
	// public ActionMessage addActivity(Activity activity) {
	// // 验证activity
	// if (activity == null) {
	// return ActionMessage.failStatus("activity is empty");
	// }
	// try {
	// if (Tools.isBlank(activity.getUuid())) {
	// activity.setUuid(Tools.getUUID());
	// }
	// activity.setPinyin(Tools.getPinYin(activity.getName()));
	// activity.setCode(Tools.generateShortUuid(6));
	// int effect = activityMapper.addActivity(activity);
	// if (effect == 1) {
	// return ActionMessage.successStatus();
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return ActionMessage.failStatus("save activity failure");
	// }

	@Override
	protected String namespace() {
		return ActivityMapper.class.getName();
	}

	// @Override
	// public ActionMessage updateActivity(Activity activity) {
	// try {
	// activity.setPinyin(Tools.getPinYin(activity.getName()));
	// int effect = activityMapper.updateActivity(activity);
	// if (effect == 1) {
	// return ActionMessage.successStatus();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return ActionMessage.failStatus("活动更新失败");
	// }

	@Override
	public Pagination<Activity> searchActivitiesForDatatable(String query,
			int from, int to) {
		query = filterSQLSpecialChars(query);
		String total_sql = "select count(*) from activity where isActive=1 and (name like '%"
				+ query + "%' or pinyin like '%" + query + "%')";
		Pagination<Activity> pagination = initPage(total_sql, from, to);
		List<Activity> as = activityMapper.searchDatatableActivities(query,
				from, to);
		pagination.setResults(as);
		return pagination;
	}

	// @Override
	// public Activity getActivityByUuid(String uuid) {
	// return activityMapper.getActivityByUuid(uuid);
	// }

	@Override
	public Activity changeStatus(Activity activity, int status) {
		if (activity == null)
			return null;
		int effect = activityMapper.changeStatus(activity.getId(), status);
		return activityMapper.getActivityById(activity.getId());
	}

	@Override
	public ActivityConfig getActivityConfig(Activity activity) {
		if (activity == null)
			return null;
		return activityMapper.selectActivityConfig(activity.getId());
	}

	@Override
	public ActionMessage saveActivtyConfig(ActivityConfig config) {
		if (config == null)
			return ActionMessage.failStatus("没有任何配置");
		int effect = 0;
		try {
			if (config.hasPersisted()) {
				effect = activityMapper.updateActivityConfig(config);
			} else {
				config.setUuid(Tools.getUUID());
				config.setStatus(ActivityConfig.APPROVED);
				effect = activityMapper.addActivityConfig(config);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		if (effect > 0) {
			return ActionMessage.successStatus();
		}
		return ActionMessage.failStatus("保存失败");
	}

	@Override
	public ActionMessage recordActivityUser(Activity activity, User user) {
		if (!BaseModel.hasPersisted(activity) || !BaseModel.hasPersisted(user))
			return ActionMessage.failStatus("数据错误");

		return null;
	}

	@Override
	public ActionMessage recordActivityResource(Activity activity,
			Resource resource) {
		try {
			if (!BaseModel.hasPersisted(resource)
					|| !BaseModel.hasPersisted(activity)) {
				return ActionMessage.failStatus("参数错误");
			}
			int effect = activityMapper.updateResourceActivityUseIds(
					activity.getId(), resource.getId());
			if (effect < 1) {
				ActivityResource ar = new ActivityResource();
				ar.setUuid(Tools.getUUID());
				ar.setStatus(resource.getStatus());
				ar.setIsActive(BaseModel.ACTIVE);
				ar.setActivity(activity);
				ar.setResource(resource);
				activityMapper.addActivityResource(ar);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ActionMessage.failStatus("保存失败");
		}
		return ActionMessage.successStatus();
	}

	@Override
	public List<Activity> recommendActivities(int size) {
		return activityMapper.searchRecommendActivties(size);
	}

	@Override
	public List<Activity> wonderActivites(String startTime, int size) {
		return activityMapper.wonderActivites(startTime, size);
	}

	@Override
	public Pagination<ActivityResource> searchActivitieResForDatatable(
			String activityUuid, String queryKey, int startPage, int pageSize) {
		queryKey = filterSQLSpecialChars(queryKey);
		Activity activity = activityMapper.getActivityByUuid(activityUuid);
		int activityId = 0;
		if (null != activity) {
			activityId = activity.getId();
		}
		String total_sql = "select count(*) from activity_resource where isActive=1 and activityId ="
				+ activityId + " and (name like '%" + queryKey + "%')";
		Pagination<ActivityResource> pagination = initPage(total_sql,
				startPage, pageSize);
		List<ActivityResource> as = activityMapper
				.searchActivitieResForDatatable(activityId, queryKey,
						startPage, pageSize);
		pagination.setResults(as);
		return pagination;
	}

	@Override
	public ActionMessage saveActivityResource(ActivityResource activityResource) {
		if (activityResource == null || !activityResource.isCompleted())
			return ActionMessage.failStatus("has empty value");
		if (!activityResource.getResource().isApproved()) {
			ActionMessage.failStatus("资源还未审核");
		}
		// 默认上线
		try {
			activityResource.setStatus(Resource.APPROVED);
			int updated = activityMapper
					.updateActivtyResourceActive(activityResource);
			if (updated == 0) {
				activityResource.setUuid(Tools.getUUID());
				updated = activityMapper.addActivityResource(activityResource);
			}
			if (updated > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("failure adding resource to activity");
	}

	@Override
	public ActivityResource getActivityResourceByUuid(String uuid) {
		return activityMapper.getActivityResourceByUuid(uuid);
	}

	@Override
	public ActionMessage deleteActivityResource(
			ActivityResource activityResource) {
		try {
			int effect = activityMapper
					.deleteActivityResource(activityResource);
			if (effect == 1) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("活动资源删除失败");
	}

	@Override
	public Pagination<ActivityUser> searchActivitieUserForDatatable(
			String activityUuid, String queryKey, int startPage, int pageSize) {
		queryKey = filterSQLSpecialChars(queryKey);
		Activity activity = activityMapper.getActivityByUuid(activityUuid);
		int activityId = 0;
		if (null != activity) {
			activityId = activity.getId();
		}
		String total_sql = "select count(*) from activity_user where isActive=1 and activityId ="
				+ activityId + " and (mark like '%" + queryKey + "%')";
		Pagination<ActivityUser> pagination = initPage(total_sql, startPage,
				pageSize);
		List<ActivityUser> as = activityMapper.searchActivitieUserForDatatable(
				activityId, queryKey, startPage, pageSize);
		pagination.setResults(as);
		return pagination;
	}

	@Override
	public ActivityUser getActivityUserByUuid(String uuid) {
		ActivityUser user = activityMapper.getActivityUserByUuid(uuid);
		return user;
	}

	@Override
	public ActionMessage deleteActivityUser(ActivityUser activityUser) {
		try {
			int effect = activityMapper.deleteActivityUser(activityUser);
			if (effect == 1) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("活动成员删除失败");
	}

	@Override
	public ActivityResource updateActivityResState(
			ActivityResource activityResource) {
		ActivityResource res = null;
		try {
			if (activityResource == null)
				return null;
			int effect = activityMapper
					.updateActivityResState(activityResource);
			res = activityMapper.getActivityResourceById(activityResource
					.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public ActivityResource changeActivityResStatus(
			ActivityResource activityResource, int status) {
		if (activityResource == null)
			return null;
		int effect = activityMapper.changeActivityResStatus(
				activityResource.getId(), status);
		return activityMapper.getActivityResourceById(activityResource.getId());
	}

	@Override
	public ActivityUser updateActivityUserIsAvtive(ActivityUser activityUser) {
		ActivityUser user = null;
		if (activityUser == null)
			return null;
		int effect = activityMapper.updateActivityUserIsAvtive(
				activityUser.getId(), activityUser.getIsActive());
		user = activityMapper.getActivityUserById(activityUser.getId());
		return user;
	}

	@Override
	public Status addActivityUser(ActivityUser activityUser) {
		try {
			activityMapper.addActivityUser(activityUser);
			return Status.SUCCESS;
		} catch (Exception e) {
			logger.error("保存参加活动的人员出错", e);
		}
		return Status.FAILURE;
	}

	@Override
	public ActivityUser getActivityUserByUserId(int userId,int activityId) {
		return activityMapper.getActivityUserByUserId(userId,activityId);
	}

	@Override
	public List<Activity> searchActivityByCateId(List<Integer> list) {
		return activityMapper.searchActivityByCateId(list);
	}

	@Override
	public ActivityResource searchResourceActivityUseIds(Activity activity,
			Resource resource) {
		return activityMapper.searchResourceActivityUseIds(activity.getId(),
				resource.getId());
	}

	@Override
	public Status addResouceActivityRelate(ActivityResource activityResource) {
		try {
			int effect = activityMapper.addActivityResource(activityResource);
			if (effect == 1) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateResouceActivityRelate(ActivityResource activityResource) {
		try {
			int effect = activityMapper
					.updateResourceActivityRelate(activityResource);
			if (effect == 1) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public List<ActivityResource> getResActivityBYchat(Activity activity,
			String startTime, int size) {
		return activityMapper.getResActivityBYchat(activity.getId(), startTime,
				size);
	}

	@Override
	public ActionMessage updateActivityWithConfig(ActivityConfig config) {
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		boolean resultTag;
		try {
			resultTag = transaction.execute(new TransactionCallback<Boolean>() {
				@Override
				public Boolean doInTransaction(TransactionStatus arg0) {
					activityMapper.updateActivity(config.getActivity());
					activityMapper.updateActivityConfig(config);
					return true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return ActionMessage.failStatus("更新活动失败");
		}
		if (!resultTag) {
			return ActionMessage.failStatus("更新活动失败");
		}
		return ActionMessage.successStatus();
	}

	@Override
	public ActivityResource recommendResource(ActivityResource activityRes) {
		if (activityRes == null)
			return null;
		int effect = 0;
		try {
			if (null == activityRes.getRecommendDate()) {
				effect = activityMapper.recommendResource(activityRes);
			} else {
				effect = activityMapper.recommendResourceUndo(activityRes);
			}
			if (effect == 1) {
				return activityMapper.getActivityResourceById(activityRes
						.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Status updateActivityUserStatus(ActivityUser activityUser) {
		try {
			int effect = activityMapper.updateActivityUserStatus(activityUser);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public int getRecommendResourceCount(ActivityResource activityRes) {
		return activityMapper.getRecommendResourceCount(activityRes);
	}

	@Override
	public List<ActivityResource> recommendActivityVideo(Activity activity) {
		return activityMapper.recommendActivityVideo(activity.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#add(java.lang.Object)
	 */
	@Override
	public ActionMessage add(Activity activity) {
		if (activity == null) {
			return ActionMessage.failStatus("activity is empty");
		}
		try {
			if (Tools.isBlank(activity.getUuid())) {
				activity.setUuid(Tools.getUUID());
			}
			activity.setPinyin(Tools.getPinYin(activity.getName()));
			activity.setCode(Tools.generateShortUuid(6));
			int effect = activityMapper.addActivity(activity);
			if (effect == 1) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("save activity failure");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#del(java.lang.Object)
	 */
	@Override
	public ActionMessage del(Activity t) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#getByUuid(java.lang.String)
	 */
	@Override
	public Activity getByUuid(String uuid) {
		return activityMapper.getActivityByUuid(uuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#update(java.lang.Object)
	 */
	@Override
	public ActionMessage update(Activity activity) {
		try {
			activity.setPinyin(Tools.getPinYin(activity.getName()));
			int effect = activityMapper.updateActivity(activity);
			if (effect == 1) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("活动更新失败");
	}

	@Override
	public List<Activity> collecteActivites(String startTime, int size) {
		// TODO Auto-generated method stub
		return activityMapper.collecteActivites(startTime, size);
	}

	@Override
	public Status addActivitySubject(ActivitySubject activitySubject) {
		try {
			int effect = activityMapper.addActivitySubject(activitySubject);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加活动学科失败", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status deteleActivitySubject(ActivitySubject activitySubject) {
		try {
			int effect = activityMapper.deteleActivitySubject(activitySubject);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除活动学科失败", e);
		}
		return Status.FAILURE;
	}

	@Override
	public ActivitySubject getActivitySubjectByUuid(String uuid) {
		return activityMapper.getActivitySubjectByUuid(uuid);
	}

	@Override
	public Status updateActivitySubject(ActivitySubject activitySubject) {
		try {
			int effect = activityMapper.updateActivitySubject(activitySubject);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("更新活动学科失败", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Pagination<ActivitySubject> searchActivitySubjectForTable(
			String searchValue, int activityId, int from, int size) {
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from activity_subject where isActive=1 and activityId="
				+ activityId
				+ " and (name like '%"
				+ searchValue
				+ "%' or pinyin like '%" + searchValue + "%')";
		Pagination<ActivitySubject> pagination = initPage(sql, from, size);
		List<ActivitySubject> list = activityMapper
				.searchActivitySubjectForTable(searchValue, activityId, from,
						size);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public List<Activity> searchActivityByQrSence(String searchValue) {
		return activityMapper.searchActivityByQrSence(searchValue);
	}

	@Override
	public Activity getActivityById(int id) {
		return activityMapper.getActivityById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.ActivityFacade#queryApiUseableActivities(java
	 * .lang.String)
	 */
	@Override
	public List<Activity> queryApiUseableActivities(String q) {
		q = filterSQLSpecialChars(q);
		return activityMapper.queryApiUseableActivities(q);
	}

	@Override
	public List<Activity> searchActivityByWxSubject(String searchValue) {
		return activityMapper.searchActivityByWxSubject(searchValue);
	}

	@Override
	public Pagination<ActivityExpert> searchActivitieExpertForDatatable(
			String activityUuid, String queryKey, int startPage, int pageSize) {
		queryKey = filterSQLSpecialChars(queryKey);
		Activity activity = activityMapper.getActivityByUuid(activityUuid);
		int activityId = 0;
		if (null != activity) {
			activityId = activity.getId();
		}
		String total_sql = "select count(*) from activity_expert left join speaker on activity_expert.speakerId = speaker.id where activity_expert.isActive=1 and speaker.isActive=1 and activity_expert.activityId ="
				+ activityId
				+ " and (speaker.name like '%"
				+ queryKey
				+ "%' or speaker.pinyin like '%" + queryKey + "%')";
		Pagination<ActivityExpert> pagination = initPage(total_sql, startPage,
				pageSize);
		List<ActivityExpert> activityExperts = activityMapper
				.searchActivitieExpertForDatatable(activityId, queryKey,
						startPage, pageSize);
		pagination.setResults(activityExperts);
		return pagination;
	}

	@Override
	public ActionMessage updateActivityExpertStatus(
			ActivityExpert activityExpert) {
		try {
			int effect = activityMapper
					.updateActivityExpertStatus(activityExpert);
			if (effect == 1) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("更新状态失败");
	}

	@Override
	public ActivityExpert getActivityExpertByUuid(String uuid) {
		return activityMapper.getActivityExpertByUuid(uuid);
	}

	@Override
	public ActionMessage saveActivityExpert(ActivityExpert activityExpert,
			HashMap<Integer, String> bgImags) {
		if (null == activityExpert || null == bgImags || bgImags.size() < 0) {
			return ActionMessage.failStatus("参数不完整，请重新尝试");
		}

		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		ActionMessage message;
		try {
			message = transaction
					.execute(new TransactionCallback<ActionMessage>() {
						@Override
						public ActionMessage doInTransaction(
								TransactionStatus arg0) {
							// 保存专家基本数据
							{
								int tag = activityMapper
										.updateActivityExpertStatus(activityExpert);
								if (tag <= 0) {
									ActivityExpert expert = activityMapper
											.getActivityExpertBySpeakerIdAndActivityId(activityExpert);
									if (null != expert
											&& BaseModel.ACTIVE == expert
													.getIsActive()) {
										return ActionMessage
												.failStatus("专家已存在");
									} else if (null != expert
											&& BaseModel.DISABLE == expert
													.getIsActive()) {
										activityExpert.setId(expert.getId());
										activityMapper
												.updateActivityExpertStatus(activityExpert);
									} else {
										activityMapper
												.saveActivityExpert(activityExpert);
									}
								}
							}

							// 保存专家图片数据
							for (Map.Entry<Integer, String> entry : bgImags
									.entrySet()) {
								mediaCentralFacade.saveActivityExpertMedia(
										activityExpert, entry.getKey(),
										entry.getValue());
							}
							return ActionMessage.successStatus();
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			return ActionMessage.failStatus("添加专家失败");
		}
		return message;
	}

	@Override
	public List<ActivityExpert> searchExpertByWx(int activityId,
			String startTime, int size) {
		return activityMapper.searchExpertByWx(activityId, startTime, size);
	}

	@Override
	public ActionMessage recommendExpert(ActivityExpert activityExpert) {
		try {
			int effect = activityMapper.recommendExpert(activityExpert);
			if (effect == 1) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("专家置顶失败");
	}

	@Override
	public List<ActivityUser> searchActivitieUserForMessage(int index,
			Activity activity) {
		List<ActivityUser> users = activityMapper
				.searchActivitieUserForMessage(index, activity.getId());
		if (null == users) {
			users = new ArrayList<ActivityUser>();
		}
		return users;
	}

	@Override
	public int allActivityCount() {
		Integer count=activityMapper.allActivityCount();
		if(count==null){
			return 0;
		}
		return count;
	}

}
