package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.PageRemain;
import com.lankr.tv_cloud.model.User;

public interface PageRemainFacade {
	
	public ActionMessage<?> addPageRemain(PageRemain pageRemain);
	
	public PageRemain selectPageRemainByUuid(String uuid);
	
	public ActionMessage<?> updatePageRemain(PageRemain pageRemain);
	
	//统计某一资源的观看时长(s)
	public int resViewTimeByUser(int referType,int referId,User user);
	
	//计算课程进度，统一更新
	public void updateCourseSchedule(int schedule,NormalCollect normalCollect,User user);

}
