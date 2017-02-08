package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.ThreeScreen;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class RecourceTypeData extends BaseAPIModel{
	/**
	 * 视频 pdf 三分屏 新闻 资源(新闻资源暂不整合【包含分享有礼，和精彩推荐】)
	 */
	private String uuid;//具体这个资源的uuid
	
	private String name;
	
	private String type;
	
	private String cover;
	
	private String fileId;
	
	private String taskId;
	
	private int pageNum;
	
	private String title;
	
	private String content;
	
	private String summary;
	
	private int duration;
	
	private String division;
	
	

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	
	
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void buildData(PdfInfo info){
		if(info==null){
			return ;
		}
		this.setName(Tools.nullValueFilter(info.getName()));
		this.setUuid(info.getUuid());
		this.setCover(Tools.nullValueFilter(info.getCoverTaskId()));
		this.setFileId("");
		this.setPageNum(Integer.parseInt(info.getPdfnum()));
		this.setTaskId(info.getTaskId());
		this.setType(info.getType().name());
	}
	
	/**
	 * 新闻信息
	 * @param info
	 */
	public void buildData(NewsInfo info){
		if(info==null){
			return ;
		}
		this.setName(Tools.nullValueFilter(info.getName()));
		this.setUuid(info.getUuid());
		this.setCover(Tools.nullValueFilter(info.getCover()));
		this.setFileId("");
		this.setPageNum(0);
		this.setSummary(Tools.nullValueFilter(info.getSummary()));
		this.setTitle(Tools.nullValueFilter(info.getTitle()));
		this.setContent(Tools.nullValueFilter(info.getContent()));
		this.setType(info.getType().name());
		
	}
	
	public void buildData(Video info){
		if(info==null){
			return ;
		}
		this.setName(Tools.nullValueFilter(info.getName()));
		this.setUuid(info.getUuid());
		this.setCover(Tools.nullValueFilter(info.getCover()));
		this.setFileId(info.getFileId());
		this.setPageNum(0);
		this.setDuration(info.getDuration());
		this.setType(info.getType().name());
	}
	
	public void buildData(ThreeScreen info){
		if(info==null){
			return ;
		}
		this.setName(Tools.nullValueFilter(info.getName()));
		this.setUuid(info.getUuid());
		this.setCover(Tools.nullValueFilter(info.getCover()));
		this.setFileId(info.getFileId());
		this.setPageNum(info.getPdfNum());
		this.setTaskId(info.getPdfTaskId());
		this.setType(info.getType().name());
		this.setDuration(info.getVideoTime());
		this.setDivision(Tools.nullValueFilter(info.getDivision()));
	}
	

}
