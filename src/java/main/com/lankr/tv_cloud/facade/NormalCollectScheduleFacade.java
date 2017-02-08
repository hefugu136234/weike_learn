package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollectQuestionnaire;
import com.lankr.tv_cloud.model.NormalCollectSchedule;
import com.lankr.tv_cloud.model.User;

public interface NormalCollectScheduleFacade {

	public NormalCollectSchedule getNormalCollectScheduleByUuid(String uuid);

	// 列表课程的进度
	public NormalCollectSchedule selectCourseScheduleByUser(
			NormalCollect normalCollect, User user);

	// 一个课程下的视频的学习状态
	public List<NormalCollectSchedule> selectReScheduleByCourseUser(
			NormalCollect normalCollect, User user);

	// 章节考核的进度
	public NormalCollectSchedule selectChapterScheduleByUser(
			NormalCollectQuestionnaire normalCollectQuestionnaire, User user);
	
	//更新课程包的学习状态
	public ActionMessage<?> updateCourseSchedule(NormalCollectSchedule normalCollectSchedule,NormalCollect normalCollect);

}
