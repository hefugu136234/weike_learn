package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.CommonPraiseFacade;
import com.lankr.tv_cloud.facade.MessageFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.WebchatFacade;
import com.lankr.tv_cloud.model.CommonPraise;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.snapshot.ShowCommentItem;

public class FrontCommentList extends BaseAPIModel{
	
	private int commentCount ;
	
	private int resCommentCount ;
	
	public int getResCommentCount() {
		return resCommentCount;
	}

	public void setResCommentCount(int resCommentCount) {
		this.resCommentCount = resCommentCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	private List<ShowCommentItem>  commentVos ;

	public List<ShowCommentItem> getCommentVos() {
		return commentVos;
	}

	public void setCommentVos(List<ShowCommentItem> commentVos) {
		this.commentVos = commentVos;
	}
	
	public void buildCommentVos(User user, List<Message> comments, MessageFacade messageFacade, WebchatFacade webchatFacade, CommonPraiseFacade commonPraiseFacade) throws Exception {
		this.setStatus(Status.SUCCESS.message());
		this.setCommentCount(comments.size());
		commentVos = new ArrayList<ShowCommentItem>() ;
		if (comments != null && comments.size() > 0) {
			this.commentCount = comments.size() ;
			for (Message comment : comments)  {
				ShowCommentItem commentVo = new ShowCommentItem(comment,1) ;
				commentVo.build();
				if (user != null) {
					String uuid = OptionalUtils.traceValue(comment.getUser(), "uuid");
					if (user.getUuid().equals(uuid)) {
						commentVo.setDelete(true);
					}
					CommonPraise commonPraise = commonPraiseFacade.selectCommonPraiseByUser(CommonPraise.REFER_TYPE_COMMENT,comment.getId(),user.getId());
					if (commonPraise != null && commonPraise.isActive()) {
						commentVo.setPraise(true);
					}
				}
				commentVos.add(commentVo) ;
			}
		}
	}

	public void buildCommentVosPage(User user, List<Message> messages, MessageFacade messageFacade, WebchatFacade webchatFacade, CommonPraiseFacade commonPraiseFacade,int referType) throws Exception {
		buildCommentVos(user, messages, messageFacade, webchatFacade, commonPraiseFacade) ;
		int count = 0 ;
		if (messages != null && messages.size()>0) {
			Message message = messages.get(0) ;
			count = messageFacade.searchCountByResource(message.getReferId(), referType, message.getType()) ; 
		}
		this.resCommentCount = count ;
	}

	public void buildCommentVosDetail(User user, Message message, MessageFacade messageFacade, WebchatFacade webchatFacade) throws Exception {
		this.setStatus(Status.SUCCESS.message());
		this.commentVos = new ArrayList<ShowCommentItem>() ;
		buildCommentDetail(user, commentVos, message, messageFacade, webchatFacade) ;
		this.commentCount = commentVos.size() ;
	}

	private void buildCommentDetail(User user, List<ShowCommentItem> commentVos, Message message, MessageFacade messageFacade, WebchatFacade webchatFacade ) throws Exception {
		int id = message.getParentId() ;
		if (id != 0) {
			Message parentMessage = messageFacade.getById(id) ;
			ShowCommentItem commentVo = new ShowCommentItem(message,1) ;
			if (parentMessage != null) {
				makeComment(commentVo,commentVos);
				buildCommentDetail(user, commentVos, parentMessage, messageFacade, webchatFacade) ;
			} else {
				makeComment(commentVo,commentVos);
			}
		} else {
			ShowCommentItem commentVo = new ShowCommentItem(message,1) ;
			makeComment(commentVo,commentVos);
		}
	}
	
	private void makeComment(ShowCommentItem commentVo, List<ShowCommentItem> commentVos2) throws Exception {
		commentVo.build();
		commentVos.add(commentVo) ;
	}

	public void buildAllComments(List<Message> comments) {
		this.setStatus(Status.SUCCESS.message());
		this.setCommentCount(comments.size());
		commentVos = new ArrayList<ShowCommentItem>() ;
		if (comments != null && comments.size() > 0) {
			for (Message comment : comments)  {
				ShowCommentItem commentVo = new ShowCommentItem(comment,1) ;
				commentVo.build();
				commentVos.add(commentVo) ;
			}
		}
	}
	
}
