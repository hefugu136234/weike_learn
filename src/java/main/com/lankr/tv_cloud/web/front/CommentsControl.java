package com.lankr.tv_cloud.web.front;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lankr.tv_cloud.codes.CodeProvider;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.CommonPraise;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel.JsonConvertor;
import com.lankr.tv_cloud.vo.snapshot.ShowCommentItem;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
import com.lankr.tv_cloud.web.front.vo.FrontCommentList;

@Controller
@RequestMapping(value = BaseFrontController.PC_PRIOR)
public class CommentsControl extends BaseFrontController{
	
	@Autowired
	private CodeProvider codeProvider;
	
	/**
	 * web评论增加统一接口
	 * @author wang
	 * @createDate 2016.7.29
	 * @param uuid
	 * @param content
	 * @param parentId
	 * @param type
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/res/add/comment", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String resAddComment(@RequestParam String uuid,@RequestParam String content, Integer parentId, @RequestParam int type, HttpServletRequest request) {
		Resource resource = effectResourceFacade().getResourceByUuid(uuid) ; 
		if (resource == null || !resource.isValid()) 
			return failureModel("资源不可用或已经下线").toJSON() ;
		String addBefore=addCommentBeforCheck(request, type, parentId, content);
		if(!Tools.isBlank(addBefore)){
			return addBefore;
		}
		int resourceId = resource.getId() ;
		User user = getCurrentUser(request) ;
		Message message = buildNewComment(resourceId, parentId, content, type, user,Message.REFER_TYPE_RESOURCE) ;
		ActionMessage<?> actionMessage = messageFacade.add(message) ;
		if (actionMessage.getStatus() == Status.SUCCESS) {
			if (type == Message.COMMENT_TYPE) {
				bulidRequest("web添加资源评论",  "message",resourceId , Status.SUCCESS.message(), null, "成功", request);
				
			} else {
				bulidRequest("web添加回复评论",  "message",parentId , Status.SUCCESS.message(), null, "成功", request);
			}
			try {
				ShowCommentItem commentItem = buildReturnComment(message);
				commentItem.setDelete(true);
				//2016-08-19 新增资源评论增加积分
				int effectIntegral=integralFacade.userResourceComment(user, resource);
				commentItem.setIntegral(effectIntegral);
				return commentItem.itemApiJson() ;
			} catch (Exception e) {
				e.printStackTrace();
				return failureModel("加载出错").toJSON();
			}
		}
		
		return actionModel(actionMessage).toJSON() ;
	}
	
	

	/**
	 * 查看评论、分页
	 * @author wang
	 * @createDate 2016.7.29
	 * @param uuid
	 * @param size
	 * @param currentPage
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/res/page/comment", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String resListComment(@RequestParam String uuid, @RequestParam int size, @RequestParam int currentPage, HttpServletRequest request) {
		Resource resource = effectResourceFacade().getResourceByUuid(uuid) ;
		if (resource == null || !resource.isValid()) {
			return failureModel("资源不可用或已经下线").toJSON() ;
		}
		int id = resource.getId() ;
		return webShowMessageList(request, id, Message.REFER_TYPE_RESOURCE, currentPage, size);
	}
	
	/**
	 * 查看对话
	 * @author wang
	 * @createDate 2016.7.29
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = false, logger = true)
	@RequestMapping(value = "/res/detail/comment", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String resDetailComment(@RequestParam Integer id, HttpServletRequest request) {
		Message message = messageFacade.getById(id) ;
		if (message == null || !message.isActive()) {
			return failureModel("该对话不存在或已删除").toJSON() ;
		}
		User user = getCurrentUser(request);
		FrontCommentList commentList = new FrontCommentList() ;
		try {
			commentList.buildCommentVosDetail(user, message, messageFacade, webchatFacade) ;
		} catch (Exception e) {
			e.printStackTrace();
			return failureModel("加载出错").toJSON();
		}
		bulidRequest("web查看对话评论",  "message", message.getId() , Status.SUCCESS.message(), null, "成功", request);
		return commentList.toJSON(JsonConvertor.JACKSON);
	}
	
	/**
	 * 
	 * 删除评论
	 * @author wang
	 * @createDate 2016.7.29
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/res/delete/comment", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String resdeleteComment(@RequestParam Integer id, HttpServletRequest request) {
		User user = getCurrentUser(request) ;
		if (user == null || !user.isActive())
			return actionModel(codeProvider.code(511).getActionMessage())
					.toJSON();
		Message message = messageFacade.getById(id) ;
		if (message == null || !message.isActive()) 
			return failureModel("该评论不存在或已删除").toJSON() ;
		
		String uuid = OptionalUtils.traceValue(message.getUser(), "uuid");
		if (!user.getUuid().equals(uuid)) 
			return failureModel("您没有删除此评论的权限").toJSON() ;
		message.setIsActive(Message.FALSE);
		ActionMessage actionMessage = messageFacade.update(message) ;
		bulidRequest("web删除评论",  "message",message.getId() , Status.SUCCESS.message(), null, "成功", request);
		return actionModel(actionMessage).toJSON() ;
	}
	
	@RequestAuthority(requiredProject = true, logger = true)
	@RequestMapping(value = "/res/praise/comment", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String praiseComment(@RequestParam Integer id, HttpServletRequest request) {
		User user = getCurrentUser(request) ;
		if (user == null || !user.isActive())
			return actionModel(codeProvider.code(511).getActionMessage())
					.toJSON();
		CommonPraise commonPraise = initCommonPraise(CommonPraise.REFER_TYPE_COMMENT,id,user);
		synchronized (this) {
			Message message = messageFacade.getById(id) ;
			if (message == null || !message.isActive()) {
				return failureModel("该评论不存在或已删除").toJSON() ;
			}
			return saveCommonPraise(commonPraise,message);
		}
	}
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/offline/activity/add/comment", produces = "text/json; charset=utf-8", method = RequestMethod.POST)
	public @ResponseBody String offlineAddComment(@RequestParam String uuid,
			@RequestParam String content,
			@RequestParam(required = false) Integer parentId,
			@RequestParam int type, HttpServletRequest request) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.apiUseable()) {
			return failureModel("线下活动不可用或已经下线").toJSON();
		}
		String addBefore = addCommentBeforCheck(request, type, parentId,
				content);
		if (!Tools.isBlank(addBefore)) {
			return addBefore;
		}
		User user = getCurrentUser(request);
		int id = offlineActivity.getId();
		Message message = buildNewComment(id, parentId, content, type, user,
				Message.REFER_TYPE_OFFLINEACTIVITY);
		return addMessageResult(message, user);
	}
	
	@RequestAuthority(requiredProject = false, logger = false)
	@RequestMapping(value = "/offline/activity/comment/list", produces = "text/json; charset=utf-8",method = RequestMethod.GET)
	@ResponseBody
	public String offlineActivityCommonList(@RequestParam String uuid,
			@RequestParam int size, @RequestParam int currentPage,
			HttpServletRequest request) {
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.apiUseable()) {
			return failureModel("线下活动不可用或已经下线").toJSON();
		}
		int id = offlineActivity.getId();
		return webShowMessageList(request, id, Message.REFER_TYPE_OFFLINEACTIVITY, currentPage, size);
	}

}
