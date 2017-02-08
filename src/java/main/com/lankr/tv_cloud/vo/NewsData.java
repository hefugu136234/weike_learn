package com.lankr.tv_cloud.vo;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.utils.Tools;

public class NewsData {

	private String uuid;
	private String title;
	private String createDate;
	private String content;
	private String author;
	private String label;
	private int isStatus;
	private String summary;
	private String categoryUuid;
	private String qrTaskId;
	private String modifyDate;

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	private String categoryName;

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

	public int getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(int isStatus) {
		this.isStatus = isStatus;
	}

	public String getQrTaskId() {
		return qrTaskId;
	}

	public void setQrTaskId(String qrTaskId) {
		this.qrTaskId = qrTaskId;
	}

	public void buildData(NewsInfo info, boolean flag) {
		if (info == null)
			return;
		this.setUuid(info.getUuid());
		this.setCreateDate(Tools.df1.format(info.getCreateDate()));
		this.setModifyDate(Tools.df1.format(info.getModifyDate()));
		this.setAuthor(Tools.nullValueFilter(HtmlUtils.htmlEscape(info
				.getAuthor())));
		if (flag) {
			this.setContent(Tools.nullValueFilter(info.getContent()));
		}
		this.setTitle(Tools.nullValueFilter(info.getTitle()));
		this.setLabel(Tools.nullValueFilter(info.getLabel()));
		this.setSummary(Tools.nullValueFilter(info.getSummary()));
		this.setIsStatus(info.getStatus());
		Category category = info.getCategory();
		if (category != null) {
			this.setCategoryUuid(category.getUuid());
			this.setCategoryName(category.getName());
		} else {
			this.setCategoryUuid("");
			this.setCategoryName("");
		}
		
		String taskId = Tools.nullValueFilter(info.getQrTaskId());
		if(taskId.startsWith("http")){
			this.setQrTaskId(taskId);
		}
		else{
			this.setQrTaskId("http://cloud.lankr.cn/api/image/" + taskId
					+ "?m/2/h/500/f/jpg");
		}
	}

}
