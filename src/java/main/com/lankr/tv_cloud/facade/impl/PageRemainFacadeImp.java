package com.lankr.tv_cloud.facade.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.PageRemainFacade;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollectSchedule;
import com.lankr.tv_cloud.model.PageRemain;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.util.WxCourseScheduleUtil;

public class PageRemainFacadeImp extends FacadeBaseImpl implements
		PageRemainFacade {

	private ExecutorService pool = Executors.newFixedThreadPool(20);

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "com.lankr.orm.mybatis.mapper.PageRemainMapper";
	}

	@Override
	public ActionMessage<?> addPageRemain(PageRemain pageRemain) {
		// TODO Auto-generated method stub
		try {
			int effect = pageRemainMapper.addPageRemain(pageRemain);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("新增页面停留时间出错", e);
		}
		return codeProvider.code(-3001).getActionMessage();
	}

	@Override
	public PageRemain selectPageRemainByUuid(String uuid) {
		// TODO Auto-generated method stub
		return pageRemainMapper.selectPageRemainByUuid(uuid);
	}

	@Override
	public ActionMessage<?> updatePageRemain(PageRemain pageRemain) {
		// TODO Auto-generated method stub
		try {
			int effect = pageRemainMapper.updatePageRemain(pageRemain);
			if (effect > 0) {
				// 重新核算资源进度
				updateResourceSchedule(pageRemain);
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("修改页面停留时间出错", e);
		}
		return codeProvider.code(-3002).getActionMessage();
	}

	@Override
	public int resViewTimeByUser(int referType, int referId, User user) {
		// TODO Auto-generated method stub
		if (user == null) {
			return 0;
		}
		Integer viewTime = pageRemainMapper.resViewTimeByUser(referType,
				referId, user.getId());
		if (viewTime != null) {
			return viewTime;
		}
		return 0;
	}

	// 核算个人资源的观看进度
	public void updateResourceSchedule(PageRemain pageRemain) {
		int userId = pageRemain.getUserId();
		if (userId == 0) {
			return;
		}
		int referType = pageRemain.getReferType();
		if (referType != PageRemain.RESOURCE_DETAIL_TYPE) {
			return;
		}
		int referId = pageRemain.getReferId();
		if (referType == 0) {
			return;
		}
		pool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 资源的进度
				NormalCollectSchedule normalCollectSchedule = normalCollectScheduleMapper
						.selectScheduleByUser(
								NormalCollectSchedule.TYPE_RESOURCE, referId,
								userId);
				User user = new User();
				user.setId(userId);
				Resource resource = resourceMapper.getResourceById(referId);
				if (normalCollectSchedule == null) {
					int viewTime = resViewTimeByUser(
							PageRemain.RESOURCE_DETAIL_TYPE, referId, user);
					normalCollectSchedule = new NormalCollectSchedule();
					normalCollectSchedule.setUuid(Tools.getUUID());
					normalCollectSchedule.setStatus(BaseModel.UNAPPROVED);
					normalCollectSchedule.setIsActive(BaseModel.ACTIVE);
					normalCollectSchedule
							.setReferType(NormalCollectSchedule.TYPE_RESOURCE);
					normalCollectSchedule.setReferId(referId);
					normalCollectSchedule.setUserId(user.getId());
					//normalCollectSchedule.setLearnSchedule(schedule);
					normalCollectSchedule.setLearnTime(viewTime);
					buildNormalCollectSchedule(resource,viewTime,normalCollectSchedule);
					normalCollectScheduleMapper
							.addNormalCollectSchedule(normalCollectSchedule);
				}else{
					int studyStatus=normalCollectSchedule.getStatus();
					if(studyStatus!=NormalCollectSchedule.STUDY_FINISH){
						//未完成
						int viewTime = resViewTimeByUser(
								PageRemain.RESOURCE_DETAIL_TYPE, referId, user);
						normalCollectSchedule.setLearnTime(viewTime);
						buildNormalCollectSchedule(resource,viewTime,normalCollectSchedule);
						normalCollectScheduleMapper.updateNormalCollectSchedule(normalCollectSchedule);
					}
				}

			}
		});

	}

	public void buildNormalCollectSchedule(Resource resource, int viewTime,
			NormalCollectSchedule normalCollectSchedule) {
		if (WxCourseScheduleUtil.calculationResourceFinish(
				resource, viewTime)) {
			normalCollectSchedule.setStudyStatus(NormalCollectSchedule.STUDY_FINISH);
			//normalCollectSchedule.setLearnSchedule(100);
		}else{
			normalCollectSchedule.setStudyStatus(NormalCollectSchedule.STUDY_ING);
		}

	}

	//课程更新进度
	@Override
	public void updateCourseSchedule(int schedule, NormalCollect normalCollect,
			User user) {
		// TODO Auto-generated method stub
		if (normalCollect == null) {
			return;
		}
		if (user == null) {
			return;
		}
		pool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				NormalCollectSchedule normalCollectSchedule = normalCollectScheduleMapper
						.selectScheduleByUser(
								NormalCollectSchedule.TYPE_COURSE,
								normalCollect.getId(), user.getId());
				if (normalCollectSchedule == null) {
					normalCollectSchedule = new NormalCollectSchedule();
					normalCollectSchedule.setUuid(Tools.getUUID());
					normalCollectSchedule.setStatus(BaseModel.UNAPPROVED);
					normalCollectSchedule.setIsActive(BaseModel.ACTIVE);
					normalCollectSchedule
							.setReferType(NormalCollectSchedule.TYPE_COURSE);
					normalCollectSchedule.setReferId(normalCollect.getId());
					normalCollectSchedule.setUserId(user.getId());
					normalCollectSchedule.setLearnSchedule(schedule);
					normalCollectSchedule.setStudyStatus(NormalCollectSchedule.STUDY_INIT);
					normalCollectScheduleMapper
							.addNormalCollectSchedule(normalCollectSchedule);
				} else {
					if (schedule != normalCollectSchedule.getLearnSchedule()) {
						normalCollectSchedule.setLearnSchedule(schedule);
						if(normalCollectSchedule.getStudyStatus()>NormalCollectSchedule.STUDY_INIT){
							if(schedule==100){
								normalCollectSchedule.setStudyStatus(NormalCollectSchedule.STUDY_FINISH);
							}else{
								normalCollectSchedule.setStudyStatus(NormalCollectSchedule.STUDY_ING);
							}
						}
						normalCollectScheduleMapper
								.updateNormalCollectSchedule(normalCollectSchedule);
					}
				}
			}
		});

	}

}
