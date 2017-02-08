package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class UuidVo {
	private String resourceUuid;
	private String pdfUuid;
	private String newsUuid;
	private String vodeoUuid;
	private String threescreenUuid;
	
	public String getResourceUuid() {
		return resourceUuid;
	}
	public void setResourceUuid(String resourceUuid) {
		this.resourceUuid = resourceUuid;
	}
	public String getPdfUuid() {
		return pdfUuid;
	}
	public void setPdfUuid(String pdfUuid) {
		this.pdfUuid = pdfUuid;
	}
	public String getNewsUuid() {
		return newsUuid;
	}
	public void setNewsUuid(String newsUuid) {
		this.newsUuid = newsUuid;
	}
	public String getVodeoUuid() {
		return vodeoUuid;
	}
	public void setVodeoUuid(String vodeoUuid) {
		this.vodeoUuid = vodeoUuid;
	}
	public String getThreescreenUuid() {
		return threescreenUuid;
	}
	public void setThreescreenUuid(String threescreenUuid) {
		this.threescreenUuid = threescreenUuid;
	}
	
	public void build(Resource resource){
		if(null == resource){
			return;
		}
		this.setResourceUuid(resource.getUuid());
		this.setPdfUuid(OptionalUtils.traceValue(resource, "pdf.uuid"));
		this.setNewsUuid(OptionalUtils.traceValue(resource, "news.uuid"));
		this.setVodeoUuid(OptionalUtils.traceValue(resource, "video.uuid"));
		this.setThreescreenUuid(OptionalUtils.traceValue(resource, "threeScreen.uuid"));
	}
}
