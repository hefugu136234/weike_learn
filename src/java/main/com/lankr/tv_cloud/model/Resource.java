package com.lankr.tv_cloud.model;

import java.util.Date;
import java.util.List;

public class Resource extends BaseModel {

	private static final long serialVersionUID = 2456018006108639113L;

	private String name;
	private String pinyin;
	private float rate;
	private int status;
	private Integer collectionNum;
	private int viewCount;
	private String coverTaskId;
	private String qrTaskId;
	private Video video;
	private PdfInfo pdf;
	private NewsInfo news;
	private ThreeScreen threeScreen;
	private Category category;
	private Speaker speaker;
	private int rank;
	private String code;// 视频的编号
	private List<TagChild> tags;
	

	public List<TagChild> getTags() {
		return tags;
	}

	public void setTags(List<TagChild> tags) {
		this.tags = tags;
	}

	// 用于结果查询
	public Date updated_at;

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	// XiaoMa
	private List<TagChild> tsgs;

	public List<TagChild> getTsgs() {
		return tsgs;
	}

	public void setTsgs(List<TagChild> tsgs) {
		this.tsgs = tsgs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getCoverTaskId() {
		return coverTaskId;
	}

	public void setCoverTaskId(String coverTaskId) {
		this.coverTaskId = coverTaskId;
	}

	public String getQrTaskId() {
		return qrTaskId;
	}

	public void setQrTaskId(String qrTaskId) {
		this.qrTaskId = qrTaskId;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public PdfInfo getPdf() {
		return pdf;
	}

	public void setPdf(PdfInfo pdf) {
		this.pdf = pdf;
	}

	public NewsInfo getNews() {
		return news;
	}

	public void setNews(NewsInfo news) {
		this.news = news;
	}

	public Integer getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(Integer collectionNum) {
		this.collectionNum = collectionNum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Speaker getSpeaker() {
		return speaker;
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public ThreeScreen getThreeScreen() {
		return threeScreen;
	}

	public void setThreeScreen(ThreeScreen threeScreen) {
		this.threeScreen = threeScreen;
	}

	public boolean isValid() {
		return isActive() && status == APPROVED;
	}

	// 资源是否已经审核
	public boolean isApproved() {
		return isActive() && status != UNAPPROVED;
	}

	public static enum Type {
		PDF, NEWS, VIDEO, THREESCREEN, BROKEN
	}

	public Type getType() {
		if (video != null) {
			return Type.VIDEO;
		} else if (pdf != null) {
			return Type.PDF;
		} else if (news != null) {
			return Type.NEWS;
		} else if (threeScreen != null) {
			return Type.THREESCREEN;
		}
		return Type.BROKEN;
	}
}
