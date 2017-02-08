package com.lankr.tv_cloud.vo;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.annotations.DataAlias;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class VideoClientItem extends BaseAPIModel {
	
	@DataAlias
	String uuid;
	@DataAlias
	String title;

	String createDate;

	String modifyDate;
	@DataAlias
	String categoryName;
	@DataAlias
	int video_status;
	@DataAlias(column = "username")
	String createUser;
	@DataAlias
	int duration;
	@DataAlias(column = "thumbnailSmall")
	String cover;
	@DataAlias
	String categoryId;
	@DataAlias(column = "mark")
	String description;

	String ccVideoId;

	String fileId;

	String plays_metainfo;

	boolean need_price;
	
	float price;
	
	private SpeakerVo speaker = new SpeakerVo();
	

	public SpeakerVo getSpeaker() {
		return speaker;
	}

	public void setSpeaker(SpeakerVo speaker) {
		this.speaker = speaker;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getVideo_status() {
		return video_status;
	}

	public void setVideo_status(int video_status) {
		this.video_status = video_status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public boolean isNeed_price() {
		return need_price;
	}

	public void setNeed_price(boolean need_price) {
		this.need_price = need_price;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void format(Video video) {
		try {
			uuid = HtmlUtils.htmlEscape(Tools.nullValueFilter(video.getUuid()));
			title = HtmlUtils
					.htmlEscape(Tools.nullValueFilter(video.getTitle()));
			video_status = video.getStatus();
			createDate = Tools.df1.format(video.getCreateDate());
			modifyDate = Tools.df1.format(video.getModifyDate());
			categoryId = video.getCategory().getUuid();
			createUser = HtmlUtils.htmlEscape(Tools.nullValueFilter(video
					.getUser().getUsername()));
			categoryName = HtmlUtils.htmlEscape(Tools.nullValueFilter(video
					.getCategory().getName()));
			cover = Tools.nullValueFilter(video.getThumbnailSmall());
			description = HtmlUtils.htmlEscape(Tools.nullValueFilter(video
					.getMark()));
			duration = (video.getDuration() == null ? 0 : video.getDuration());
			ccVideoId = video.getCcVideoId();
			need_price = video.isNeed_price();
			price = Float.valueOf(String.format("%.2f", video.getPrice()));
			fileId = video.getFileId();
			plays_metainfo = video.getPlays_metainfo();
			Resource res = video.getResource();
			if(res != null){
				speaker.build(res.getSpeaker());
			}
			setStatus(Status.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
