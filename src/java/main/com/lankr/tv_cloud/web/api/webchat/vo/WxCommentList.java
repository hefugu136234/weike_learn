package com.lankr.tv_cloud.web.api.webchat.vo;

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

public class WxCommentList extends BaseAPIModel{
	
	private int commentCount ;
	
//	private int resCommentCount ;
	
//	public int getResCommentCount() {
//		return resCommentCount;
//	}
//
//	public void setResCommentCount(int resCommentCount) {
//		this.resCommentCount = resCommentCount;
//	}
//
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
	
	public void buildCommentVos(User user, List<Message> comments, MessageFacade messageFacade, WebchatFacade webchatFacade, CommonPraiseFacade commonPraiseFacade) {
		this.setStatus(Status.SUCCESS.message());
		this.setCommentCount(comments.size());
		commentVos = new ArrayList<ShowCommentItem>() ;
		if (comments != null && comments.size() > 0) {
			this.commentCount = comments.size() ;
			for (Message comment : comments)  {
				ShowCommentItem commentVo = new ShowCommentItem(comment,1) ;
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
				commentVo.build();
				commentVos.add(commentVo) ;
			}
		}
	}

}
