package com.lankr.tv_cloud.vo;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Advertisement;
import com.lankr.tv_cloud.utils.Tools;

public class AdverClientItem {
	private String uuid;
	private String name;
	private String createDate;
	private String content_type;
	private int limit_time;
	private String mark;
	private String createName;
	private int ad_status;
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
	public String getContent_type() {
		return content_type;
	}
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}
	public int getLimit_time() {
		return limit_time;
	}
	public void setLimit_time(int limit_time) {
		this.limit_time = limit_time;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	
	


	
	public int getAd_status() {
		return ad_status;
	}
	public void setAd_status(int ad_status) {
		this.ad_status = ad_status;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public void format(Advertisement adver) {
		try {
			uuid = adver.getUuid();
			name =  HtmlUtils.htmlEscape(Tools.nullValueFilter(adver.getName()));
			createDate = Tools.df1.format(adver.getCreateDate());
			createName = HtmlUtils.htmlEscape(Tools.nullValueFilter(adver.getUser().getNickname()));
			content_type = HtmlUtils.htmlEscape(Tools.nullValueFilter(adver.getContent_type()));
			limit_time = adver.getLimit_time();
			mark = HtmlUtils.htmlEscape(Tools.nullValueFilter(adver.getMark()));
			ad_status=adver.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
