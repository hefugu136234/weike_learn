package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.NormalCollectSchedule;

public interface NormalCollectScheduleMapper {

	public int addNormalCollectSchedule(
			NormalCollectSchedule normalCollectSchedule);

	public int updateNormalCollectSchedule(
			NormalCollectSchedule normalCollectSchedule);

	public NormalCollectSchedule getNormalCollectScheduleByUuid(String uuid);

	public NormalCollectSchedule selectScheduleByUser(
			@Param("referType") int referType, @Param("referId") int referId,
			@Param("userId") int userId);

	// 一包视频下学习完成的数量(人看完的数量)
	public Integer selectNumResByCourseUser(@Param("list") List<Integer> list,
			@Param("referType") int referType,@Param("studyStatus") int studyStatus,@Param("userId") int userId);
	
	//一包视频下的学习状态(人看完的状态)
	public List<NormalCollectSchedule> selectReScheduleByCourseUser(@Param("list") List<Integer> list,
			@Param("referType") int referType,@Param("userId") int userId);
	

}
