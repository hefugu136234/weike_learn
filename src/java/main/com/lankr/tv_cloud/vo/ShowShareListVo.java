package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.model.ViewSharing;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class ShowShareListVo {
	private String uuid;
	private String resourceName;
	private String shareUserName;
	private String userName;
	private String createDate;
	private int viewCount;

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getShareUserName() {
		return shareUserName;
	}

	public void setShareUserName(String shareUserName) {
		this.shareUserName = shareUserName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void build(ViewSharing share) {
		this.setViewCount(share.getViewCount());
		this.setResourceName(OptionalUtils.traceValue(share, "resource.name"));
		this.setShareUserName(OptionalUtils.traceValue(share, "shareUser.nickname"));
		this.setUserName(OptionalUtils.traceValue(share, "user.nickname"));
		this.setCreateDate(Tools.df1.format(share.getCreateDate()));
	}

}
