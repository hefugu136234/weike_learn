package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.web.front.util.FrontWxOpenUtil;

public class WxVideo {
	private String fileId;
	private String videoTime;
	private String vrUrl;
	private int videoType;

	public String getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(String videoTime) {
		this.videoTime = videoTime;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getVrUrl() {
		return vrUrl;
	}

	public void setVrUrl(String vrUrl) {
		this.vrUrl = vrUrl;
	}

	public int getVideoType() {
		return videoType;
	}

	public void setVideoType(int videoType) {
		this.videoType = videoType;
	}

	public void buildData(Video video) {
		this.setFileId(OptionalUtils.traceValue(video, "fileId"));
		int tmp = OptionalUtils.traceInt(video, "duration");
		String time = FrontWxOpenUtil.videoTime(tmp);
		this.setVideoTime(time);
		this.videoType = OptionalUtils.traceInt(video, "videoType");
		if (this.videoType == 0) {
			this.setVrUrl(QQVodPlayData.vrPlayUrl(video.getPlays_metainfo()));
		} else if (this.videoType == 1) {
			this.setVrUrl(OptionalUtils.traceValue(video, "assetUrl"));
		}

	}
}
