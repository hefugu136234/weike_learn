package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import com.lankr.orm.mybatis.mapper.NormalCollectScheduleMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.NormalCollectScheduleFacade;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollectQuestionnaire;
import com.lankr.tv_cloud.model.NormalCollectSchedule;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;

public class NormalCollectScheduleFacadeImp extends FacadeBaseImpl implements
		NormalCollectScheduleFacade {

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return NormalCollectScheduleMapper.class.getName();
	}

	@Override
	public NormalCollectSchedule getNormalCollectScheduleByUuid(String uuid) {
		// TODO Auto-generated method stub
		return normalCollectScheduleMapper.getNormalCollectScheduleByUuid(uuid);
	}

	@Override
	public NormalCollectSchedule selectCourseScheduleByUser(
			NormalCollect normalCollect, User user) {
		// TODO Auto-generated method stub
		if (normalCollect == null) {
			return null;
		}
		if (user == null) {
			return null;
		}
		return normalCollectScheduleMapper.selectScheduleByUser(
				NormalCollectSchedule.TYPE_COURSE, normalCollect.getId(),
				user.getId());
	}

	// 课程下所有资源的学习状态
	@Override
	public List<NormalCollectSchedule> selectReScheduleByCourseUser(
			NormalCollect normalCollect, User user) {
		// TODO Auto-generated method stub
		if (normalCollect == null) {
			return null;
		}
		if (user == null) {
			return null;
		}
		List<Integer> list = resourceGroupMapper.selectResidByCourse(
				normalCollect.getId(), NormalCollect.SIGN_TYPE_COURSE_SEGMENT);
		if (Tools.isEmpty(list)) {
			return null;
		}
		return normalCollectScheduleMapper.selectReScheduleByCourseUser(list,
				NormalCollectSchedule.TYPE_RESOURCE, user.getId());
	}
	
	@Override
	public NormalCollectSchedule selectChapterScheduleByUser(
			NormalCollectQuestionnaire normalCollectQuestionnaire, User user) {
		// TODO Auto-generated method stub
		if (normalCollectQuestionnaire == null) {
			return null;
		}
		if (user == null) {
			return null;
		}
		return normalCollectScheduleMapper.selectScheduleByUser(
				NormalCollectSchedule.TYPE_CHAPTERS_REFER, normalCollectQuestionnaire.getId(),
				user.getId());
	}
	
	@Override
	public ActionMessage<?> updateCourseSchedule(
			NormalCollectSchedule normalCollectSchedule,
			NormalCollect normalCollect) {
		// TODO Auto-generated method stub
		try {
			normalCollectScheduleMapper.updateNormalCollectSchedule(normalCollectSchedule);
			normalCollectMapper.updateCollectionNums(normalCollect);
		} catch (Exception e) {
			// TODO: handle exception
			return codeProvider.code(-3002).getActionMessage();
		}
		return ActionMessage.successStatus();
	}

}
