/** 
 *  @author Kalean.Xiang
 *  @createDate: 2016年4月12日
 * 	@modifyDate: 2016年4月12日
 *  
 */
package com.lankr.tv_cloud.facade;

import java.util.Date;
import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.Notification;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.web.front.vo.FrontCommentList;

public interface MessageFacade extends BaseFacade<Message> {

	/**
	 * @author Kalean.Xiang
	 * @createDate: 2016年5月4日
	 * @modifyDate: 2016年5月4日
	 * 
	 */
	public ActionMessage addOpusMessage(String message,
			ActivityApplication opus, User user);
	
	public Message getById(int id) ;

	/**
	 * @return 返回最新的20条消息
	 * @param opus
	 *            关于作品的消息
	 */
	public List<Message> searchOpusMessages(ActivityApplication opus);

	public List<Message> searchWxOupsPageMess(int oupsId, Date startTime,
			int size);

	public ActionMessage addNotificationMessage(Notification notification,
			User user);
	
	public Pagination<Message> searchActivitieMessageDetailForDatatable(
			String notificationUuid, String queryKey, int startPage,
			int pageSize);

	public List<Message> searchCommentsForResource(int id, int referType, int Type, int startSize, int pageSize);
	
	public int searchCountByResource(int id, int referType, int type) ;
	
	public ActionMessage removeResourceComment(Message message);

	public List<Message> searchCommentsForWxResource(int id, int referType, int type, String createDate,
			int size);
	public int updateForTransaction(Message message);

	public FrontCommentList searchAllComments(SubParams params);
}
