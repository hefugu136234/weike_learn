/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月14日
 * 	@modifyDate 2016年6月14日
 *  
 */
package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Notification;

/**
 * @author Kalean.Xiang
 *
 */
public interface NotificaitonMapper {

	int saveNotification(Notification t);

	Notification getByUuid(String uuid);

	int updateNotification(Notification t);

	Notification broadcastRecentlyNotification(Broadcast cast);

	List<Notification> searchActivitieMessageForDatatable(int activityId,
			String queryKey, int startPage, int pageSize);

}
