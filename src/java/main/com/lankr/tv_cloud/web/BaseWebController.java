package com.lankr.tv_cloud.web;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.servlet.ModelAndView;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.CommonPraise;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.MyCollection;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.PageRemain;
import com.lankr.tv_cloud.model.Praise;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceAccessIgnore;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.SignUpUser;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.support.qiniu.QiniuUtils;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.snapshot.ShowCommentItem;
import com.lankr.tv_cloud.web.api.webchat.util.ResourceAuthority;
import com.lankr.tv_cloud.web.api.webchat.util.ResourceAuthorityUtil;
import com.lankr.tv_cloud.web.auth.RequestAuthority;
import com.lankr.tv_cloud.web.front.vo.FrontResStatus;
import com.lankr.tv_cloud.web.front.vo.FrontResVote;

public abstract class BaseWebController extends BaseController {

	public static final String PAGE_REAMIN_UUID = "page_reamin_uuid";

	protected static Log logger = LogFactory.getLog(BaseWebController.class);

	@Autowired
	private DataSourceTransactionManager transactionManager;

	public abstract User getCurrentUser(HttpServletRequest request);

	protected GlobalUserCache getUserCache() {
		return GlobalUserCache.getInstance(userFacade);
	}

	public Role getRole(int role_level) {
		List<Role> sys_roles = userFacade.loadRoles();
		if (sys_roles == null)
			return null;
		for (int i = 0; i < sys_roles.size(); i++) {
			Role role = sys_roles.get(i);
			if (role.getLevel() == role_level) {
				return role;
			}
		}
		return null;
	}

	public Role getRoleByName(String roleName) {
		List<Role> sys_roles = userFacade.loadRoles();
		if (sys_roles == null)
			return null;
		for (int i = 0; i < sys_roles.size(); i++) {
			Role role = sys_roles.get(i);
			if (role.getRoleName().equals(roleName)) {
				return role;
			}
		}
		return null;
	}

	protected String toastRepeatSubmitToken(HttpServletRequest request,
			String key) {
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute(key);
		session.removeAttribute(key);
		return token;
	}

	protected String toastRepeatSubmitToken(String key) {
		return toastRepeatSubmitToken(getHandleRequest(), key);
	}

	protected String makeRepeatSubmitToken(String key) {
		HttpServletRequest request = getHandleRequest();
		HttpSession session = request.getSession();
		String token = Tools.getUUID();
		session.setAttribute(key, token);
		return token;
	}

	public String getQiniuUploaderSignature() {
		String token = QiniuUtils.getSimpleUploadPolicy(null, 1,
				TimeUnit.SECONDS);
		return token;
	}

	@Override
	public BaseAPIModel playPrepare(Resource resource) {
		return null;
	}

	@Override
	public BaseAPIModel downloadPrepare(Resource resource) {
		return null;
	}

	protected BaseAPIModel rebuildTokenModel(String key, String message) {
		return new RebuildTokenApiModel(key, message);
	}

	private class RebuildTokenApiModel extends BaseAPIModel {

		private String token;

		RebuildTokenApiModel(String key, String message) {
			setStatus(Status.FAILURE);
			setMessage(message);
			token = makeRepeatSubmitToken(key);
		}
	}

	public boolean validCode(String reqCode, String key) {
		HttpServletRequest request = getHandleRequest();
		HttpSession session = request.getSession();
		String code = (String) session.getAttribute(key);
		if (code == null)
			return false;
		if (code.equalsIgnoreCase(reqCode)) {
			clearValidCodeMark(session, key);
			return true;
		}
		return false;
	}

	public void clearValidCodeMark(HttpSession session, String key) {
		if (session == null)
			return;
		session.removeAttribute(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.web.BaseController#onLoginToRedirected(com.lankr.tv_cloud
	 * .web.auth.RequestAuthority, com.lankr.tv_cloud.model.User)
	 */
	@Override
	protected boolean onLoginToRedirected(RequestAuthority target_auth,
			User currentUser) throws Exception {
		return super.onLoginToRedirected(target_auth, currentUser);
	}

	public static boolean loginIsAbel(User user) {
		if (user == null) {
			return false;
		}
		UserReference reference = user.getUserReference();
		if (reference == null || !reference.apiUseable()) {
			return false;
		}
		Role r = reference.getRole();
		if (r == null || !r.isBoxUser()) {
			return false;
		}
		// Role role = reference.getRole();
		// if (role == null || role.getLevel() < 5) {
		// return false;
		// }
		return true;
	}

	/**
	 * web公共处理需要使用的方法
	 * 
	 * @param value
	 * @param request
	 * @param key
	 */
	protected void setKeySession(String value, HttpServletRequest request,
			String key) {
		request.getSession().setAttribute(key, value);
	}

	protected String getKeySession(String key, HttpServletRequest request) {
		return request.getSession().getAttribute(key) == null ? null : request
				.getSession().getAttribute(key).toString();
	}

	protected void removeKeySession(HttpServletRequest request, String key) {
		request.getSession().removeAttribute(key);
	}

	protected ModelAndView redirectUrlView(String url) {
		return new ModelAndView("redirect:" + url);
	}

	protected ModelAndView redirectPageView(String page) {
		return new ModelAndView(page);
	}

	public ModelAndView redirectErrorPage(HttpServletRequest request,
			String errorMessage, String page) {
		request.setAttribute("error", errorMessage);
		return redirectPageView(page);
	}

	/**
	 * 2016-06-16 新增页面停留时间
	 */
	public PageRemain buildInitRemain(HttpServletRequest request,
			int referType, int referId) {
		PageRemain remain = new PageRemain();
		remain.setUuid(Tools.getUUID());
		remain.setReferType(referType);
		remain.setReferId(referId);
		remain.setStatus(BaseModel.APPROVED);
		remain.setIsActive(BaseModel.ACTIVE);
		remain.setAccessUri(request.getRequestURI());
		remain.setIp(getClientIpAddr(request));
		remain.setFirstView(new Date());
		remain.setRemainTime(0);
		return remain;
	}

	/**
	 * 2016-06-16 更新页面的停留时间
	 */
	public String updateRemainTime(String uuid) {
		PageRemain remain = pageRemainFacade.selectPageRemainByUuid(uuid);
		if (remain == null) {
			return failureModel("未找到停留信息页面").toJSON();
		}
		Date date = new Date();
		long duration = date.getTime() - remain.getFirstView().getTime();
		duration = TimeUnit.MILLISECONDS.toSeconds(duration);
		int time = (int) duration;
		remain.setLastView(date);
		remain.setRemainTime(time);
		ActionMessage<?> actionMessage = pageRemainFacade
				.updatePageRemain(remain);
		if (actionMessage.isSuccess()) {
			return successModel().toJSON();
		}
		return failureModel("更新页面时长失败").toJSON();
	}

	/**
	 * 2016-06-16 微信和pc公用方法
	 * 
	 * @param user
	 * @param res
	 * @return
	 */
	public FrontResStatus updateCollection(User user, Resource res) {
		FrontResStatus vo = new FrontResStatus();
		SubParams params = new SubParams();
		params.id = res.getId();
		params.userId = user.getId();
		MyCollection myCollection = myCollectionFacade
				.selectCollectionByReIdAndUserId(params);
		Status status = Status.SUCCESS;
		if (myCollection != null && myCollection.getUuid() != null) {
			myCollection
					.setStatus(myCollection.getStatus() == BaseModel.UNAPPROVED ? BaseModel.APPROVED
							: BaseModel.UNAPPROVED);
			status = myCollectionFacade.updateCollectionStatus(myCollection);
		} else {
			myCollection = new MyCollection();
			myCollection.setUuid(Tools.getUUID());
			myCollection.setResource(res);
			myCollection.setUser(user);
			myCollection.setIsActive(BaseModel.ACTIVE);
			myCollection.setStatus(BaseModel.APPROVED);
			myCollection.setMark("");
			status = myCollectionFacade.addCollection(myCollection);
		}

		if (Status.SUCCESS == status) {
			int num = myCollectionFacade.selectCountByReId(res.getId());
			vo.setStatus(status);
			vo.setReCount(num);
		} else {
			vo.setStatus(Status.FAILURE);
			if (myCollection.getStatus() == BaseModel.APPROVED) {
				vo.setMessage("收藏失败");
			} else {
				vo.setMessage("取消收藏失败");
			}
		}

		if (myCollection.getStatus() == BaseModel.APPROVED) {
			vo.setReStatus(true);
		} else {
			vo.setReStatus(false);
		}
		return vo;
	}

	/**
	 * 2016-06-16 微信和pc公用方法
	 * 
	 * @param user
	 * @param res
	 * @return
	 */
	public FrontResStatus updatePraise(User user, Resource res) {
		FrontResStatus vo = new FrontResStatus();
		Praise praise = praiseFacade.selectPraiseByReIdUserId(res.getId(),
				user.getId());
		Status status = Status.SUCCESS;
		if (praise != null && praise.getUuid() != null) {
			praise.setStatus(praise.getStatus() == BaseModel.UNAPPROVED ? BaseModel.APPROVED
					: BaseModel.UNAPPROVED);
			status = praiseFacade.updatePraiseStatus(praise);
		} else {
			praise = new Praise();
			praise.setUuid(Tools.getUUID());
			praise.setResource(res);
			praise.setUser(user);
			praise.setIsActive(BaseModel.ACTIVE);
			praise.setStatus(BaseModel.APPROVED);
			praise.setMark("");
			status = praiseFacade.addPraise(praise);
		}

		if (Status.SUCCESS == status) {
			int num = praiseFacade.selectCountByReId(res.getId());
			vo.setStatus(status);
			vo.setReCount(num);
			if (praise.getStatus() == BaseModel.APPROVED) {
				// 增加积分
				int effct = integralFacade.actionUserPraiseResource(user, res);
				vo.setIntegral(effct);
			}
		} else {
			vo.setStatus(Status.FAILURE);
			if (praise.getStatus() == BaseModel.APPROVED) {
				vo.setMessage("点赞失败");
			} else {
				vo.setMessage("取消点赞失败");
			}
		}

		if (praise.getStatus() == BaseModel.APPROVED) {
			vo.setReStatus(true);
		} else {
			vo.setReStatus(false);
		}
		return vo;
	}

	/**
	 * 2016-06-16 获取投票的公共数据 pc和微信公用
	 * 
	 * @param request
	 * @param resource
	 * @return
	 */
	public FrontResVote buildCommonResVote(User user, Resource resource) {
		FrontResVote vote = new FrontResVote();
		vote.build(resource, user, effectResourceFacade());
		return vote;
	}

	/**
	 * 2016-7-29 资源的新增评论
	 * 
	 * @return
	 */
	public Message buildNewComment(int modelId, Integer parentId, String content,
			int type, User user,int referType) {
		Message message = new Message();
		message.setIsActive(Message.ACTIVE);
		message.setStatus(Message.APPROVED);
		message.setUser(user);
		message.setUuid(Tools.getUUID());
		message.setBody(content);
		message.setCreateDate(new Date());
		message.setModifyDate(new Date());
		message.setSign(Message.SIGN_DEF);
		message.setReferId(modelId);
		if (type == Message.COMMENT_TYPE) {
			message.setReferType(referType);
			message.setType(Message.COMMENT_TYPE);
		} else {
			message.setParentId(parentId==null?0:parentId);
			message.setReferType(Message.REFER_TYPE_MESSAGE);
			message.setType(Message.REPLY_COMMENT_TYPE);
		}
		return message;
	}

	/**
	 * @param commonPraise
	 * @param message
	 * @return
	 */
	protected String saveCommonPraise(CommonPraise commonPraise, Message message) {
		CommonPraise cp = commonPraiseFacade.selectCommonPraiseByUser(
				commonPraise.getReferType(), commonPraise.getReferId(),
				commonPraise.getUser().getId());
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			if (cp == null) {
				commonPraise.setIsActive(BaseModel.ACTIVE);
				commonPraise.setUuid(Tools.getUUID());
				message.setPraise(message.getPraise() + 1);
				messageFacade.updateForTransaction(message);
				commonPraiseFacade.saveCommonPraise(commonPraise);
			} else {
				if (cp.isActive()) {
					cp.setIsActive(BaseModel.DISABLE);
					if (message.getPraise() > 0) {
						message.setPraise(message.getPraise() - 1);
						messageFacade.updateForTransaction(message);
					}
				} else {
					cp.setIsActive(BaseModel.ACTIVE);
					message.setPraise(message.getPraise() + 1);
					messageFacade.updateForTransaction(message);
				}
				commonPraiseFacade.updateCommonPraise(cp);
			}
			transactionManager.commit(status);
		} catch (Exception e) {
			e.printStackTrace();
			status.setRollbackOnly();
			transactionManager.rollback(status);
			return failureModel("操作失败").toJSON();
		}
		return successModel().toJSON();
	}

	/**
	 * 
	 * @param referType
	 * @param id
	 * @param user
	 * @return
	 */
	protected CommonPraise initCommonPraise(int referType, Integer id, User user) {
		CommonPraise cp = new CommonPraise();
		cp.setReferType(referType);
		cp.setReferId(id);
		cp.setUser(user);
		return cp;
	}

	/**
	 * 2016-08-30 新增评论 之前的公共检测方法
	 */
	public String addCommentBeforCheck(HttpServletRequest request,
			int messageType, Integer parentId, String content) {
		if (messageType != Message.COMMENT_TYPE
				&& messageType != Message.REPLY_COMMENT_TYPE) {
			return failureModel("提交评论数据无效").toJSON();
		}
		if (messageType == Message.REPLY_COMMENT_TYPE) {
			Message parentMessage = messageFacade.getById(parentId);
			if (parentMessage == null || !parentMessage.isActive()) {
				return failureModel("您回复的评论不存在或已经删除").toJSON();
			}
		}
		User user = getCurrentUser(request);
		if (user == null) {
			return actionModel(codeProvider.code(511).getActionMessage())
					.toJSON();
		}
		if (Tools.isBlank(content)) {
			return failureModel("请输入内容再提交评论").toJSON();
		}
		content = StringEscapeUtils.escapeHtml4(content);
		if (content.length() > 240){
			return failureModel("您输入的内容过长，请重新输入").toJSON();
		}
		return null;

	}
	
	/**
	 * 2016-08-31 线下活动的报名
	 */
	public String addOfflineBook(HttpServletRequest request,String uuid){
		User user = getCurrentUser(request);
		if (user == null) {
			return actionModel(codeProvider.code(511).getActionMessage())
					.toJSON();
		}
		OfflineActivity offlineActivity = offlineActivityFacade
				.offlineActivityByUuid(uuid);
		if (offlineActivity == null || !offlineActivity.apiUseable()) {
			return failureModel("此活动不存在或已下线").toJSON();
		}
		Date nowDate = Tools.getCurrentDate();
		Date bookStart = offlineActivity.getBookStartDate();
		if (bookStart != null) {
			if (nowDate.before(bookStart)) {
				return failureModel("此活动报名还未开始").toJSON();
			}
		}
		Date bookEnd = offlineActivity.getBookEndDate();
		if (bookEnd != null) {
			if (nowDate.after(bookEnd)) {
				return failureModel("此活动报名已经结束").toJSON();
			}
		}
		int limitNum = OptionalUtils.traceInt(offlineActivity, "limitNum");
		if (limitNum > 0) {
			int bookNum = signUpUserFacade.bookCountUser(
					offlineActivity.getId(), SignUpUser.REFER_OFFLINEACTIVITY);
			if (bookNum >= limitNum) {
				return failureModel("活动报名人数已达上限").toJSON();
			}
		}
		SignUpUser signUpUser = new SignUpUser();
		signUpUser.setUser(user);
		signUpUser.setUuid(Tools.getUUID());
		signUpUser.setStatus(BaseModel.UNAPPROVED);
		signUpUser.setIsActive(BaseModel.ACTIVE);
		signUpUser.setReferId(offlineActivity.getId());
		signUpUser.setReferType(SignUpUser.REFER_OFFLINEACTIVITY);
		ActionMessage<?> actionMessage = signUpUserFacade
				.addSignUpUser(signUpUser);
		if (actionMessage.isSuccess()) {
			return successModel().toJSON();
		}
		return failureModel("报名失败").toJSON();
	}
	
	public String addMessageResult(Message message,User user){
		ActionMessage<?> actionMessage = messageFacade.add(message);
		if (actionMessage.getStatus() == Status.SUCCESS) {
			try {
				ShowCommentItem commentItem = buildReturnComment(message);
				commentItem.setDelete(true);
				// 2016-08-30  评论增加积分
				int effectIntegral = integralFacade.userCommentAddIntegeral(user);
				commentItem.setIntegral(effectIntegral);
				return commentItem.itemApiJson();
			} catch (Exception e) {
				e.printStackTrace();
				return failureModel("加载出错").toJSON();
			}
		}
		return actionModel(actionMessage).toJSON();
	}
	
	public ShowCommentItem buildReturnComment(Message message) {
		ShowCommentItem commentVo = new ShowCommentItem(message, 1);
		commentVo.build();
		return commentVo;
	}
	
	/**
	 * 首页资源权限控制页面
	 * @param user
	 * @return
	 * 2016-12-16 
	 * @he 资源的权限控制总览
	 */
	public ResourceAuthorityUtil resourceAuthority(User user,Resource resource){
		// 资源权限数据
		ResourceAccessIgnore resourceAccessIgnore = resourceAccessIgnoreFacade
						.getByResourceId(resource.getId());
		//权限控制转化
		ResourceAuthority authority=ResourceAuthority.changeAuthority(resourceAccessIgnore);
		if(authority==null)
			return null;
		boolean realNamed=judyRealNamed(user);
		boolean hasUser=(user==null?false:true);
		ResourceAuthorityUtil authorityUtil=ResourceAuthorityUtil.changeAuthorityUtil(authority, hasUser, realNamed);
		return authorityUtil;
	}
	
	public String getCurrentUserScene(User user) {
		if (null == user)
			return ResourceAccessIgnore.NO_REGISTERD;
		Certification certification = certificationFacade
				.getCertifiActiveByUserId(user);
		if (null == certification)
			return ResourceAccessIgnore.HAS_REGISTERD_NO_RELNAME;
		else
			return ResourceAccessIgnore.HAS_RELNAME;
	}
	
	//判断是否实名
	public boolean judyRealNamed(User user){
		String flag=getCurrentUserScene(user);
		if(flag.equals(ResourceAccessIgnore.HAS_RELNAME)){
			return true;
		}
		return false;
	}
}
