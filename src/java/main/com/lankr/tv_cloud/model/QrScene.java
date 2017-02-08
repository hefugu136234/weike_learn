package com.lankr.tv_cloud.model;

import com.lankr.tv_cloud.web.api.webchat.util.WxBusinessCommon;

public class QrScene extends BaseModel{
	
	public static final int ACTIVTY_QR_TYPE=1;//扫描进入活动的二维码,永久
	
	public static final int ACTIVTY_APPLY_QR_TYPE=2;//扫描进入活动申请作品页面(此处为有活动的作品)
	
	public static final int CAST_QR_TYPE=3;//直播的临时二维码
	
	public static final int URL_TYPE=4;//一般的url,生成二维码
	
	public static final int ACTIVTY_APPLY_QR_COMMON=5;//扫描进入一般的活动的作品申请
	
	public static final int RESOURCE_TYPE=6;//资源的二维码
	
	public static final int GAME_QR=7;//游戏的二维码
	
	public static final int QR_FORVER=1;//微信永久的二维码
	
	public static final int QR_TEMP=2;//微信临时的二维码
	
	public static final int QR_FORVER_SEFT=3;//自制的永久二维码
	
	public static final int DEFAULT_BUSINESSID=0;//默认业务0
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4936997194671984754L;

	private int reflectId;
	
	private String name;
	
	private long sceneid;
	
	private int type;
	
	private int status;
	
	private int limitType;
	
	private String message;
	
	private String pinyin;
	
	private int businessId;//业务id 默认为0

	public int getReflectId() {
		return reflectId;
	}

	public void setReflectId(int reflectId) {
		this.reflectId = reflectId;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSceneid() {
		return sceneid;
	}

	public void setSceneid(long sceneid) {
		this.sceneid = sceneid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getLimitType() {
		return limitType;
	}

	public void setLimitType(int limitType) {
		this.limitType = limitType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public int getBusinessId() {
		return businessId;
	}

	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}
	
	public String rootPath;//多余的属性填充
	
	public String resourceUuid;//多余的属性填充
	
	
	public static int isLoginByQrsence(int type){
		int login=WxBusinessCommon.NEED_LOGIN;
		switch (type) {
		case ACTIVTY_QR_TYPE:
			login=WxBusinessCommon.NEED_LOGIN;
			break;
		case ACTIVTY_APPLY_QR_TYPE:
			login=WxBusinessCommon.NEED_LOGIN;
			break;
		case CAST_QR_TYPE:
			login=WxBusinessCommon.NEED_LOGIN;
			break;
		case URL_TYPE:
			login=WxBusinessCommon.NO_LOGIN;
			break;
		case RESOURCE_TYPE:
			login=WxBusinessCommon.NO_LOGIN;
			break;
		case GAME_QR:
			login=WxBusinessCommon.NEED_LOGIN;
			break;
		default:
			break;
		}
		return login;
	}

}
