package com.lankr.tv_cloud.vo;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class PdfInfoVo {

	private String uuid;

	private String name;

	private String createDate;
	
	private String modifyDate;

	private String taskId;

	private String mark;

	private String categoryName;

	private String categoryUuid;

	private int isStatus;

	private String qrTaskId;

	private String coverTaskId;
	
	private int showType;
	
	private String spaker;
	
	//modified by mayuan 
	private String speakerUuid;
	

	public String getSpeakerUuid() {
		return speakerUuid;
	}

	public void setSpeakerUuid(String speakerUuid) {
		this.speakerUuid = speakerUuid;
	}

	public String getSpaker() {
		return spaker;
	}

	public void setSpaker(String spaker) {
		this.spaker = spaker;
	}

	public int getShowType() {
		return showType;
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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(int isStatus) {
		this.isStatus = isStatus;
	}

	public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

	public String getQrTaskId() {
		return qrTaskId;
	}

	public void setQrTaskId(String qrTaskId) {
		this.qrTaskId = qrTaskId;
	}

	public String getCoverTaskId() {
		return coverTaskId;
	}

	public void setCoverTaskId(String coverTaskId) {
		this.coverTaskId = coverTaskId;
	}
	
	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public void buildData(PdfInfo pdfInfo) {
		this.setUuid(pdfInfo.getUuid());
		this.setName(Tools.nullValueFilter(HtmlUtils.htmlEscape(pdfInfo
				.getName())));
		if (pdfInfo.getCategory() != null) {
			this.setCategoryName(pdfInfo.getCategory().getName());
			this.setCategoryUuid(pdfInfo.getCategory().getUuid());
		} else {
			this.setCategoryName("");
			this.setCategoryUuid("");
		}
		this.setQrTaskId(Tools.nullValueFilter(pdfInfo.getQrTaskId()));
		this.setCreateDate(Tools.df1.format(pdfInfo.getCreateDate()));
		this.setModifyDate(Tools.df1.format(pdfInfo.getModifyDate()));
		this.setTaskId(Tools.nullValueFilter(pdfInfo.getTaskId()));
		this.setMark(Tools.nullValueFilter(HtmlUtils.htmlEscape(pdfInfo
				.getMark())));
		this.setIsStatus(pdfInfo.getStatus());
		this.showType = pdfInfo.getShowType();
		this.setSpaker(OptionalUtils.traceValue(pdfInfo, "resource.speaker.name"));
		this.setSpeakerUuid(OptionalUtils.traceValue(pdfInfo, "resource.speaker.uuid"));
		
		String taskId = Tools.nullValueFilter(pdfInfo.getCoverTaskId());
		if(taskId.startsWith("http")){
			this.setCoverTaskId(taskId);
		}
		else{
			this.setCoverTaskId("http://cloud.lankr.cn/api/image/" + taskId
					+ "?m/2/h/500/f/jpg");
		}
	}

}
