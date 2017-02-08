package com.lankr.tv_cloud.vo;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.utils.Tools;

public class ClientVideo {
	private String uuid;
	
	private String description;
	
	private String imgUrl;
	
	private String videoName;
	
	private String createDate;
	
	private String ccvideoId;
	
	private VideoCategory videoCategory;
	

	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCcvideoId() {
		return ccvideoId;
	}

	public void setCcvideoId(String ccvideoId) {
		this.ccvideoId = ccvideoId;
	}

	
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	

	public VideoCategory getVideoCategory() {
		return videoCategory;
	}

	public void setVideoCategory(VideoCategory videoCategory) {
		this.videoCategory = videoCategory;
	}

	public void formatData(Video video) {
		try {
			uuid=video.getUuid();
			videoName = HtmlUtils
					.htmlEscape(Tools.nullValueFilter(video.getTitle()));
			createDate = Tools.df1.format(video.getCreateDate());
			imgUrl = Tools.nullValueFilter(video.getThumbnailSmall());
			description = HtmlUtils.htmlEscape(Tools.nullValueFilter(video
					.getMark()));
			ccvideoId = video.getCcVideoId();
			VideoCategory cate=new VideoCategory();
			cate.formatData(video.getCategory());
			videoCategory=cate;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
