/** 
 *  @author Kalean.Xiang
 *  @createDate: 2016年4月12日
 * 	@modifyDate: 2016年4月12日
 *  
 */
package com.lankr.tv_cloud.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.lankr.orm.mybatis.mapper.MessageMapper;
import com.lankr.tv_cloud.cache.CacheBucket;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.MessageFacade;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.Notification;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.front.vo.FrontCommentList;

public class MessageFacadeImpl extends FacadeBaseImpl implements MessageFacade {

	protected MessageMapper messageMapper;

	/**
	 * @param messageMapper
	 *            the messageMapper to set
	 */
	@Autowired
	public void setMessageMapper(MessageMapper messageMapper) {
		this.messageMapper = messageMapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#add(java.lang.Object)
	 */
	@Override
	public ActionMessage add(Message t) {
		// TODO Auto-generated method stub
		try {
			int effect = messageMapper.addMessage(t);
			if (effect >= 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("保存发送记录失败");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#del(java.lang.Object)
	 */
	@Override
	public ActionMessage del(Message t) {
		if (Message.hasPersisted(t)) {
			t.setIsActive(Message.DISABLE);
			return update(t);
		}
		return ActionMessage.failStatus("empty message");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#getByUuid(java.lang.String)
	 */
	@Override
	public Message getByUuid(String uuid) {
		// TODO Auto-generated method stub
		return messageMapper.getMessageByUuid(uuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#getByUuid(java.lang.String)
	 */
	@Override
	public Message getById(int id) {
		// TODO Auto-generated method stub
		return messageMapper.getMessageById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.BaseFacade#update(java.lang.Object)
	 */
	@Override
	public ActionMessage update(Message t) {
		if (Message.hasPersisted(t)) {
			try {
				int effect = messageMapper.updateMessage(t);
				if (effect > 0) {
					return ActionMessage.successStatus();
				}
			} catch (Exception e) {
				if (logger.isErrorEnabled())
					logger.error("[MessageFacadeImpl.update] -> update error",
							e);
				return ActionMessage.failStatus("update error");
			}
		}
		return ActionMessage.failStatus("message is empty");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.MessageFacade#addOpusMessage(com.lankr.tv_cloud
	 * .model.Message, com.lankr.tv_cloud.model.ActivityApplication)
	 */
	@Override
	public ActionMessage addOpusMessage(String msg, ActivityApplication opus,
			User user) {
		if (!User.hasPersisted(user)) {
			return ActionMessage.failStatus("message has empty user");
		}
		if (!ActivityApplication.hasPersisted(opus)) {
			return ActionMessage.failStatus("empty opus");
		}
		Message message = new Message();
		message.setUser(user);
		message.setBody(msg);
		message.setUuid(Tools.getUUID());
		message.setReferId(opus.getId());
		message.setReferType(Message.MESSAGE_REFER_TYPE_OPUS);
		message.setIsActive(Message.ACTIVE);
		message.setStatus(Message.APPROVED);
		return add(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.impl.FacadeBaseImpl#namespace()
	 */
	@Override
	protected String namespace() {
		return MessageMapper.class.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.MessageFacade#searchOpusMessages(com.lankr.
	 * tv_cloud.model.ActivityApplication)
	 */
	@Override
	public List<Message> searchOpusMessages(ActivityApplication opus) {
		if (!ActivityApplication.hasPersisted(opus))
			return null;
		return messageMapper
				.selectMoreMessages(
						opus.getId(),
						Message.MESSAGE_REFER_TYPE_OPUS,
						new Date(System.currentTimeMillis()
								+ TimeUnit.DAYS.toMillis(1)), 20);
	}

	@Override
	public List<Message> searchWxOupsPageMess(int oupsId, Date startTime,
			int size) {
		// TODO Auto-generated method stub
		return messageMapper.selectMoreMessages(oupsId,
				Message.MESSAGE_REFER_TYPE_OPUS, startTime, size);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.MessageFacade#addNotificationMessage(com.lankr
	 * .tv_cloud.model.Notification, com.lankr.tv_cloud.model.User)
	 */
	@Override
	public ActionMessage addNotificationMessage(Notification notification,
			User user) {
		if (!BaseModel.hasPersisted(notification)
				|| !BaseModel.hasPersisted(user)) {
			ActionMessage.failStatus("empty value");
		}
		Message message = new Message();
		message.setUser(user);
		message.setReferType(Message.REFER_TYPE_NOTIFICAITON);
		message.setReferId(notification.getId());
		message.setSign(Message.SIGN_DEF);
		message.setBody(notification.getBody());
		message.setUuid(Tools.getUUID());
		return add(message);

	}

	@Override
	public Pagination<Message> searchActivitieMessageDetailForDatatable(
			String notificationUuid, String queryKey, int startPage,
			int pageSize) {
		queryKey = filterSQLSpecialChars(queryKey);
		Notification notificaiton = notificaitonMapper
				.getByUuid(notificationUuid);
		int notificaitonId = 0;
		if (null != notificaiton) {
			notificaitonId = notificaiton.getId();
		}
		String total_sql = " select count(*) from message m left join user u on m.userId = u.id "
				+ " where m.isActive = 1 and m.referType = 2 and  m.sign = 0 "
				+ " and m.referId = "
				+ notificaitonId
				+ " and (m.body like '%"
				+ queryKey
				+ "%' or u.nickname like '%"
				+ queryKey
				+ "%' or u.pinyin like '%"
				+ queryKey
				+ "%' or u.phone like '%"
				+ queryKey + "%')";
		Pagination<Message> pagination = initPage(total_sql, startPage,
				pageSize);
		List<Message> activityMessages = messageMapper
				.searchActivitieMessageForDatatable(notificaitonId, queryKey,
						startPage, pageSize);
		pagination.setResults(activityMessages);
		return pagination;
	}

	@Override
	public List<Message> searchCommentsForResource(int id, int referType,
			int type, int startSize, int pageSize) {
		List<Message> messages = messageMapper.searchCommentsForResource(id,
				referType, type, startSize, pageSize);
		return messages;
	}

	@Override
	public int searchCountByResource(int id, int referType, int type) {
		// TODO Auto-generated method stub
		int count = messageMapper.searchCountByResource(id, referType, type);
		return count;
	}

	public ActionMessage removeResourceComment(Message message) {
		try {
			List<Message> messages = messageMapper.getSubMessages(message,
					Message.REFER_TYPE_MESSAGE, Message.REPLY_COMMENT_TYPE);
			if (null == messages || messages.size() == 0)
				return this.update(message);
			List<Integer> idsContent = new ArrayList<Integer>();
			idsContent.add(message.getId());
			this.getIds(idsContent, messages);
			return removeResourceCommentByIds(idsContent);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[MessageFacadeImpl.removeResourceCommentByIds] -> 删除评论失败",
						e);
			return ActionMessage.failStatus("删除评论失败");
		}
	}

	private void getIds(List<Integer> idsContent, List<Message> messages) {
		if (null != messages && messages.size() > 0) {
			for (Message mes : messages) {
				idsContent.add(mes.getId());
				messages = messageMapper.getSubMessages(mes,
						Message.REFER_TYPE_MESSAGE, Message.REPLY_COMMENT_TYPE);
				this.getIds(idsContent, messages);
			}
		}
	}

	private ActionMessage removeResourceCommentByIds(List<Integer> ids) {
		try {
			messageMapper.removeResourceCommentByIds(ids);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[MessageFacadeImpl.removeResourceCommentByIds] -> 批量删除评论失败",
						e);
			return ActionMessage.failStatus("批量删除评论失败");
		}
		return ActionMessage.successStatus();
	}

	@Override
	public List<Message> searchCommentsForWxResource(int id, int referType,
			int type, String createDate, int size) {
		return messageMapper.searchCommentsForWxResource(id, referType, type,
				createDate, size);
	}

	@Override
	public int updateForTransaction(Message message) {
		return messageMapper.updateMessage(message);
	}

	@Override
	public FrontCommentList searchAllComments(SubParams params) {
		FrontCommentList commentList = new FrontCommentList();
		List<Message> messages = new ArrayList<Message>();
		int commentCount = 0;
		try {
			messages = messageMapper.searchAllComments(
					Message.REFER_TYPE_RESOURCE, Message.REFER_TYPE_MESSAGE,
					Message.COMMENT_TYPE, Message.REPLY_COMMENT_TYPE, params);
			commentCount = this.searchAllCommentsCount(params);
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error(e);
		}
		commentList.setResCommentCount(commentCount);
		commentList.buildAllComments(messages);
		return commentList;
	}

	private int searchAllCommentsCount(SubParams params) {
		return messageMapper.searchAllCommentsCount(
				Message.REFER_TYPE_RESOURCE, Message.REFER_TYPE_MESSAGE,
				Message.COMMENT_TYPE, Message.REPLY_COMMENT_TYPE, params);
	}

}
