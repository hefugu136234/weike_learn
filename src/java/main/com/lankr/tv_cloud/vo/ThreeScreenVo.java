package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.model.ThreeScreen;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class ThreeScreenVo extends BaseAPIModel{

	private String uuid;

	private String name;

	private String cover;

	private String categoryName;

	private String categoryUuid;

	private String division;

	private int isStatus;

	private String createDate;
	
	private String modifyDate;

	private String mark;
	
	private String speaker;
	
	private String speakerUuid;
	
	private String fileId;
	
	private String pdfTaskId;
	
	private int pdfNum;
	
	private int videoTime;
	
	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public int getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(int isStatus) {
		this.isStatus = isStatus;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}
	
	

	public String getSpeakerUuid() {
		return speakerUuid;
	}

	public void setSpeakerUuid(String speakerUuid) {
		this.speakerUuid = speakerUuid;
	}
	
	

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
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
	
	

	public int getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(int videoTime) {
		this.videoTime = videoTime;
	}

	public void build(ThreeScreen threeScreen) {
		this.setUuid(threeScreen.getUuid());
		this.setName(Tools.nullValueFilter(threeScreen.getName()));
		this.setCategoryName(OptionalUtils.traceValue(threeScreen,
				"category.name"));
		this.setCategoryUuid(OptionalUtils.traceValue(threeScreen,
				"category.uuid"));
		this.setCreateDate(Tools.df1.format(threeScreen.getCreateDate()));
		this.setModifyDate(Tools.df1.format(threeScreen.getModifyDate()));
		this.setDivision(Tools.nullValueFilter(threeScreen.getDivision()));
		this.setIsStatus(threeScreen.getStatus());
		this.setMark(Tools.nullValueFilter(threeScreen.getMark()));
		this.setSpeaker(OptionalUtils.traceValue(threeScreen, "resource.speaker.name"));
		this.setSpeakerUuid(OptionalUtils.traceValue(threeScreen, "resource.speaker.uuid"));
		this.setFileId(threeScreen.getFileId());
		this.setPdfTaskId(threeScreen.getPdfTaskId());
		this.setPdfNum(threeScreen.getPdfNum());
		this.setVideoTime(threeScreen.getVideoTime());
		
		String taskId = Tools.nullValueFilter(threeScreen.getCover());
		if(taskId.startsWith("http")){
			this.setCover(taskId);
		}
		else{
			this.setCover("http://cloud.lankr.cn/api/image/" + taskId
					+ "?m/2/h/500/f/jpg");
		}
	}
}
