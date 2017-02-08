/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月14日
 * 	@modifyDate 2016年6月14日
 *  
 */
package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Notification;
import com.lankr.tv_cloud.model.User;

/**
 * @author Kalean.Xiang
 *
 */
public interface NotificationFacade extends BaseFacade<Notification> {

	public Notification addActivityResourceChangedNotification(
			Activity activity, User user, String body);

	public ActionMessage addBroadcastNotification(Broadcast broadcast,
			String body);

	public Notification broadcastRecentlyNotification(Broadcast cast);
	
	public Pagination<Notification> searchActivitieMessageForDatatable(
			String activityUuid, String queryKey, int startPage, int pageSize);
}
