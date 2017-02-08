package com.lankr.tv_cloud.model;

public class IntegralConfig extends BaseModel {

	/**
	 * 用户注册成功
	 * */
	public static final int ACTION_REGISTER = 1;
	/**
	 * 实名认证成功
	 * */
	public static final int ACTION_CERTIFICATION = 2;
	/**
	 * 通过分享有礼，邀请好友并注册成功
	 * */
	public static final int ACTION_SHARE_REGISTER = 3;

	/**
	 * 三分屏资源拥有者
	 * */
	public static final int ACTION_CONTRIBUTE_THREESCREEN = 4;

	/**
	 * 视频拥有者
	 * */
	public static final int ACTION_CONTRIBUTE_VIDEO = 5;
	/**
	 * PDF拥有者
	 * */
	public static final int ACTION_CONTRIBUTE_PDF = 6;
	/**
	 * 资源被播放，资源拥有者加分
	 * */
	public static final int ACTION_RESOURCE_PLAYED = 7;
	/**
	 * 资源被点赞，资源拥有者加分
	 * */
	public static final int ACTION_RESOURCE_PRAISED = 8;
	/**
	 * 资源被分享，资源拥有者加分
	 * */
	public static final int ACTION_RESOURCE_SHARED = 9;
	/**
	 * 用户播放资源
	 * */
	public static final int ACTION_USER_PLAY_RESOURCE = 10;
	/**
	 * 用户参与投票
	 * */
	public static final int ACTION_USER_VOTE_RESOURCE = 11;
	/**
	 * 用户点赞
	 * */
	public static final int ACTION_USER_PRAISE_RESOURCE = 12;
	/**
	 * 用户分享资源被点击
	 * */
	public static final int ACTION_USER_SHARING_VIEWED = 13;

	/**
	 * 系统管理员添加积分
	 * */
	public static final int ACTION_SYSTEM_ADDITION = 14;
	
	/**
	 * 用户兑换积分
	 * */
	public static final int ACTION_USER_CONSUME_INTEGRAL = -1;
	
	/**
	 * 用户评论资源
	 * */
	public static final int ACTION_USER_RESOURCE_COMMENT = 15;
	
	
	/**
	 * 
	 * */
	/**
	 * 系统管理员扣除积分
	 * */
	public static final int ACTION_SYSTEM_DEDUCTING = -2;

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	private int action;

	private int value;

	private int status;

}
