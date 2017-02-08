package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class WxSignature extends BaseAPIModel {
	/**
	 * 微信分享的返回数据
	 */
	public static final int WX_NO_SHARE = 0;// 不分享

	public static final int WX_COMMON_SHARE = 1;// 公共分享，默认的模板

	public static final int WX_RESOURCE_SHARE = 2;// 资源的分享

	public static final int WX_ACTIVITY_SHARE = 3;// 活动分享

	public static final int WX_LIVE_SHARE = 4;// 直播分享

	public static final int WX_ACTIVITY_EXPERT_SHARE = 5;// 活动专家分享

	public int shareType;

	public String appId;

	public long timestamp;

	public String nonceStr;

	public String signature;

	private String url;

	private String title;

	private String picUrl;

	private String desc;

	private String shareJson;
	
	private String oriUserUuid;//来自谁的分享

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getShareJson() {
		return shareJson;
	}

	public void setShareJson(String shareJson) {
		this.shareJson = shareJson;
	}

	public int getShareType() {
		return shareType;
	}

	public void setShareType(int shareType) {
		this.shareType = shareType;
	}
	
	

	public String getOriUserUuid() {
		return oriUserUuid;
	}

	public void setOriUserUuid(String oriUserUuid) {
		this.oriUserUuid = oriUserUuid;
	}

	public void buildBase(long timestamp, String nonceStr, String appid,
			String signature) {
		this.setTimestamp(timestamp);
		this.setNonceStr(nonceStr);
		this.setAppId(appid);
		this.setSignature(signature);
	}
	
	public void buildDetail(String title,String picUrl,String desc,String url){
		this.setTitle(title);
		this.setPicUrl(picUrl);
		this.setDesc(desc);
		this.setUrl(url);
	} 
	

}
