package com.lankr.tv_cloud.model;

import com.lankr.tv_cloud.model.Resource.Type;

public class Video extends BaseModel implements Resourceable {

	public static final int QQ_COMMON = 0;// 腾讯云普通视频

	public static final int QIUNIU_CLOUD = 1;// 七牛云视频

	/**
	 * 
	 */
	private static final long serialVersionUID = 6910296847051551103L;

	private int videoType;

	public int getVideoType() {
		return videoType;
	}

	public void setVideoType(int videoType) {
		this.videoType = videoType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getThumbnailSmall() {
		return thumbnailSmall;
	}

	public void setThumbnailSmall(String thumbnailSmall) {
		this.thumbnailSmall = thumbnailSmall;
	}

	public String getThumbnailMiddle() {
		return thumbnailMiddle;
	}

	public void setThumbnailMiddle(String thumbnailMiddle) {
		this.thumbnailMiddle = thumbnailMiddle;
	}

	public String getThumbnailFarther() {
		return thumbnailFarther;
	}

	public void setThumbnailFarther(String thumbnailFarther) {
		this.thumbnailFarther = thumbnailFarther;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCcVideoId() {
		return ccVideoId;
	}

	public void setCcVideoId(String ccVideoId) {
		this.ccVideoId = ccVideoId;
	}

	public String getAssetUrl() {
		return assetUrl;
	}

	public void setAssetUrl(String assetUrl) {
		this.assetUrl = assetUrl;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private String title;

	private String pinyin;

	private String thumbnailSmall;

	private String thumbnailMiddle;

	private String thumbnailFarther;

	private int status;

	private String ccVideoId;

	private String assetUrl;

	private Category category;

	private User user;

	private Project project;

	private Integer duration = 0;

	private boolean need_price;

	private float price;

	private String fileId;

	private long size;

	private String plays_metainfo;

	public String getPlays_metainfo() {
		return plays_metainfo;
	}

	public void setPlays_metainfo(String plays_metainfo) {
		this.plays_metainfo = plays_metainfo;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public boolean isNeed_price() {
		return need_price;
	}

	public void setNeed_price(boolean need_price) {
		this.need_price = need_price;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return getTitle();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return getMark();
	}

	@Override
	public String getCover() {
		// TODO Auto-generated method stub
		return getThumbnailSmall();
	}

	@Override
	public String getQr() {
		return "";
	}

	@Override
	public BaseModel resource() {
		return this;
	}

	@Override
	public Type getType() {
		return Type.VIDEO;
	}

	private Resource resource;

	@Override
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public int getPrototypeId() {
		// TODO Auto-generated method stub
		return getId();
	}
	
	public boolean isQiuniu(){
		return videoType==QIUNIU_CLOUD;
	}
}
