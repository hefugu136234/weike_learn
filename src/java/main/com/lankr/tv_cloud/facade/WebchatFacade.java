package com.lankr.tv_cloud.facade;

import javax.servlet.http.HttpServletRequest;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.web.api.webchat.util.WxAccseeTokenByCode;


public interface WebchatFacade {
	
	public String handleMessage(HttpServletRequest request);
	
	public WebchatUser searchWebChatUserByOpenid(String openId);
	
	public int searchCountByOpenid(String openId) ;
	
	public Status addWebChatUser(WebchatUser webchatUser);
	
	public Status updateWebChatUserStatus(WebchatUser webchatUser);
	
	public WebchatUser searchWebChatUserByUserId(int userid);
	
	public Status updateWebChatUser(WebchatUser webchatUser);
	
	/** 
	 *  @author Kalean.Xiang
	 *  @createDate 2016年5月9日
	 * 	@modifyDate 2016年5月9日
	 *  用于用户解除微信的绑定 
	 */
	public ActionMessage unbindWechat(String openId);
	
	
	public WebchatUser searchWebChatUserByUnionid(String unionid);
	
	//订阅时，处理WebchatUser相关信息
	public WebchatUser buildTotalWebUser(String openId);
	
	//在系统中获取WebchatUser相关信息
	public void subscribeUpdateWxInfo(WebchatUser webchatUser);
	
	//系统获取游客的信息，并保存
	public void visitorWxInfo(WxAccseeTokenByCode accseeTokenByCode);

	public Pagination<WebchatUser> searchNoRegWechatUsers(String q, int from, int to,
			int projectId);

	public WebchatUser searchWebChatUserByUuid(String uuid);
}
