package com.lankr.tv_cloud.web.api.app.vo;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.utils.Tools;

public class NewsItem {
	private String uuid;
	private String title;
	private String createDate;
	private String content;
	private String author;
	private String label;
	private String summary;
	private String categoryName;
	private String categoryUuid;
	private String qrTaskId;

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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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
	
	

	public String getQrTaskId() {
		return qrTaskId;
	}

	public void setQrTaskId(String qrTaskId) {
		this.qrTaskId = qrTaskId;
	}

	public void buildData(NewsInfo info, boolean flag) {
		this.setUuid(info.getUuid());
		this.setCreateDate(Tools.df1.format(info.getCreateDate()));
		this.setAuthor(Tools.nullValueFilter(HtmlUtils.htmlEscape(info
				.getAuthor())));
		if (flag) {
			this.setContent(Tools.nullValueFilter(info.getContent()));
		}
		this.setTitle(Tools.nullValueFilter(info.getTitle()));
		this.setLabel(Tools.nullValueFilter(info.getLabel()));
		this.setQrTaskId(Tools.nullValueFilter(info.getQrTaskId()));
		this.setSummary(Tools.nullValueFilter(info.getSummary()));
		if(info.getCategory()!=null){
			this.setCategoryUuid(info.getCategory().getUuid());
			this.setCategoryName(info.getCategory().getName());
		}else{
			this.setCategoryUuid("");
			this.setCategoryName("");
		}
	}

}
