/** 
 *  @author Kalean.Xiang
 *  @createDate: 2016年4月12日
 * 	@modifyDate: 2016年4月12日
 *  
 */
package com.lankr.orm.mybatis.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.Message;

public interface MessageMapper {

	public Message getMessageByUuid(String uuid);

	public Message getMessageById(int id);

	public int addMessage(Message message);

	public int updateMessage(Message message);

	public List<Message> selectMoreMessages(int referId, int referType,
			Date updated, int batch_size);

	public List<Message> searchActivitieMessageForDatatable(int notificaitonId,
			String queryKey, int startPage, int pageSize);

	public List<Message> selectMoreMessagesForPage(int referId, int referType,
			int startPage, int PageSize);

	public List<Message> searchCommentsForResource(int id, int referType,
			int type, int startSize, int pageSize);

	public int searchCountByResource(int id, int referType, int type);

	public List<Message> getCommentUserRecord(int id, String query,
			int startPage, int pageSize, int typeRes, int typeMes,
			int commentType, int replyCommentType);

	public List<Message> getSubMessages(@Param("message") Message message,
			@Param("referType") Integer referType, @Param("type") Integer type);

	public int removeResourceCommentByIds(List<Integer> list);

	public List<Message> searchCommentsForWxResource(@Param("id")int id, @Param("referType")int referType,
			@Param("type")int type, @Param("createDate") String createDate, @Param("size") int size);

	public List<Message> searchAllComments(
			@Param("referTypeResource") int referTypeResource,
			@Param("referTypeMessage") int referTypeMessage,
			@Param("commentType") int commentType,
			@Param("replyCommentType") int replyCommentType,
			@Param("params") SubParams params);

	public int searchAllCommentsCount(
			@Param("referTypeResource") int referTypeResource,
			@Param("referTypeMessage") int referTypeMessage,
			@Param("commentType") int commentType,
			@Param("replyCommentType") int replyCommentType,
			@Param("params") SubParams params);

}
