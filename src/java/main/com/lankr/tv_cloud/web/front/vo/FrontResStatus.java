package com.lankr.tv_cloud.web.front.vo;

import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class FrontResStatus extends BaseAPIModel {

	private boolean reStatus;

	private int reCount;

	private int integral;

	public boolean isReStatus() {
		return reStatus;
	}

	public void setReStatus(boolean reStatus) {
		this.reStatus = reStatus;
	}

	public int getReCount() {
		return reCount;
	}

	public void setReCount(int reCount) {
		this.reCount = reCount;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getCollectMessage() {
		if (this.isReStatus()) {
			return "收藏";
		}
		return "取消收藏";
	}

	public String getZanMessage() {
		if (this.isReStatus()) {
			return "点赞";
		}
		return "取消点赞";
	}

}
