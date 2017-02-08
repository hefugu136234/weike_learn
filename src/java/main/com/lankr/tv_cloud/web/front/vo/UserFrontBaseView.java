package com.lankr.tv_cloud.web.front.vo;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.WebchatFacade;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;

public class UserFrontBaseView extends BaseAPIModel{
	
	private int id;
	
	private boolean login;//是否登录
	
	private String uuid;
	
	private String showName;
	
	private boolean realName;//是否实名
	
	private String vip;//vip的状态
	
	private String vipDeadTime;
	
	private String realNameInfo;//实名信息
	
	private String photo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public boolean isRealName() {
		return realName;
	}

	public void setRealName(boolean realName) {
		this.realName = realName;
	}

	

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getVipDeadTime() {
		return vipDeadTime;
	}

	public void setVipDeadTime(String vipDeadTime) {
		this.vipDeadTime = vipDeadTime;
	}

	public String getRealNameInfo() {
		return realNameInfo;
	}

	public void setRealNameInfo(String realNameInfo) {
		this.realNameInfo = realNameInfo;
	}
	
	
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public void buildUserView(User user,WebchatFacade webchatFacade) {
		this.id = user.getId() ;
		this.uuid = user.getUuid();
		this.showName = OptionalUtils.traceValue(user, "nickname");
		String photo = OptionalUtils.traceValue(user, "avatar");
		photo = WxUtil.getDefaultAvatar(photo);
		this.photo = photo ;
	}

	public static String notLogin(){
		UserFrontBaseView view=new UserFrontBaseView();
		view.setStatus(Status.SUCCESS);
		view.setLogin(false);
		return view.toJSON();
	}

}
