package com.lankr.tv_cloud.model;

import com.lankr.tv_cloud.model.Resource.Type;

public class ThreeScreen extends BaseModel implements Resourceable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7604342371835892331L;

	private String name;
	
	private String pinyin;
	
	private String fileId;
	
	private String videoCover;
	
	private int videoTime;
	
	private String pdfTaskId;
	
	private int pdfNum;
	
	private String coverTaskId;
	
	private Category category;
	
	private String division;
	
	private String plays_metainfo;
	
	public String getPlays_metainfo() {
		return plays_metainfo;
	}

	public void setPlays_metainfo(String plays_metainfo) {
		this.plays_metainfo = plays_metainfo;
	}

	private int status;

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

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getVideoCover() {
		return videoCover;
	}

	public void setVideoCover(String videoCover) {
		this.videoCover = videoCover;
	}

	public int getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(int videoTime) {
		this.videoTime = videoTime;
	}

	public String getPdfTaskId() {
		return pdfTaskId;
	}

	public void setPdfTaskId(String pdfTaskId) {
		this.pdfTaskId = pdfTaskId;
	}

	public int getPdfNum() {
		return pdfNum;
	}

	public void setPdfNum(int pdfNum) {
		this.pdfNum = pdfNum;
	}

	

	public String getCoverTaskId() {
		return coverTaskId;
	}

	public void setCoverTaskId(String coverTaskId) {
		this.coverTaskId = coverTaskId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public int getPrototypeId() {
		// TODO Auto-generated method stub
		return getId();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return getMark();
	}

	@Override
	public String getQr() {
		// TODO Auto-generated method stub
		return getCover();
	}

	@Override
	public BaseModel resource() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.THREESCREEN;
	}

	private Resource resource;

	@Override
	public Resource getResource() {
		return resource;
	}

	@Override
	public String getCover() {
		// TODO Auto-generated method stub
		if(coverTaskId==null||coverTaskId.isEmpty())
			return videoCover;
		return coverTaskId;
	}
	
	
	private Speaker speaker;

	public Speaker getSpeaker() {
		return speaker;
	}

	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}
	
	
	

}
