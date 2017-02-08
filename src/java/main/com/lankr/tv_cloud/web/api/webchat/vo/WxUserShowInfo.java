package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;

public class WxUserShowInfo {
	
	private String photo;//头像
	
	private boolean realFlag;//是否实名
	
	private String vipStatus;//vip充值后的状态
	
	private String vipDeadTime;//vip的到期时间
	
	private int score;//当前积分
	
	private String showName;//展示的名称
	
	private String realNameInfo;//实名后信息
	
	private String userUuid;

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public boolean isRealFlag() {
		return realFlag;
	}

	public void setRealFlag(boolean realFlag) {
		this.realFlag = realFlag;
	}

	public String getVipStatus() {
		return vipStatus;
	}

	public void setVipStatus(String vipStatus) {
		this.vipStatus = vipStatus;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getRealNameInfo() {
		return realNameInfo;
	}

	public void setRealNameInfo(String realNameInfo) {
		this.realNameInfo = realNameInfo;
	}

	public String getVipDeadTime() {
		return vipDeadTime;
	}

	public void setVipDeadTime(String vipDeadTime) {
		this.vipDeadTime = vipDeadTime;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	
	
	public static WxUserShowInfo authShow(String username,String photo){
		WxUserShowInfo showInfo=new WxUserShowInfo();
		showInfo.setShowName(username);
		showInfo.setPhoto(photo);
		return showInfo;
	}
	
	public void buildCommentUserShow(User user) {
		String photo = OptionalUtils.traceValue(user, "avatar");
		photo = WxUtil.getDefaultAvatar(photo);
		this.photo = photo ;
		this.showName = user.getNickname() ;
		this.userUuid = user.getUuid() ;
	}
	

}
