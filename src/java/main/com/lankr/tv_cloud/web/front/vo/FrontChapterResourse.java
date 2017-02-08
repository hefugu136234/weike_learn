package com.lankr.tv_cloud.web.front.vo;

import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceGroup;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;
import com.lankr.tv_cloud.web.front.util.FrontWxOpenUtil;

public class FrontChapterResourse {
	private String uuid;

	private String name;

	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String cover;

	private String showMessage;

	private int learnCount;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getShowMessage() {
		return showMessage;
	}

	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}

	public int getLearnCount() {
		return learnCount;
	}

	public void setLearnCount(int learnCount) {
		this.learnCount = learnCount;
	}

	public void buildBaseData(ResourceGroup resourceGroup) {
		this.setUuid(resourceGroup.getUuid());
		this.setName(OptionalUtils.traceValue(resourceGroup, "name"));
	}

	public void buildResource(Resource resource) {
		this.setType(resource.getType().name());
		String cover = OptionalUtils.traceValue(resource, "coverTaskId");
		cover = WxUtil.getResourceCover(cover);
		this.setCover(cover);
		if (resource.getType() == Type.PDF) {
			String num = OptionalUtils.traceValue(resource, "pdf.pdfnum");
			num += "页";
			this.setShowMessage(num);
		} else if (resource.getType() == Type.THREESCREEN) {
			int videoTime = OptionalUtils.traceInt(resource,
					"threeScreen.videoTime");
			String time = FrontWxOpenUtil.videoTime(videoTime);
			this.setShowMessage(time);
		} else if (resource.getType() == Type.VIDEO) {
			int tmp = OptionalUtils.traceInt(resource, "video.duration");
			String time = FrontWxOpenUtil.videoTime(tmp);
			this.setShowMessage(time);
		}
	}

}
