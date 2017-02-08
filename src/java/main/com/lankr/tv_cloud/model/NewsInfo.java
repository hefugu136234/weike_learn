package com.lankr.tv_cloud.model;

import com.lankr.tv_cloud.model.Resource.Type;

public class NewsInfo extends BaseModel implements Resourceable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2274274629581900429L;

	private String title;

	private String content;

	private String label;

	private String author;

	private int status;

	private String summary;

	private Category category;

	private String qrTaskId;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getQrTaskId() {
		return qrTaskId;
	}

	public void setQrTaskId(String qrTaskId) {
		this.qrTaskId = qrTaskId;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return getTitle();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return getSummary();
	}

	@Override
	public String getCover() {
		return getQrTaskId();
	}

	@Override
	public String getQr() {
		// TODO Auto-generated method stub
		return getQrTaskId();
	}

	@Override
	public BaseModel resource() {
		return NewsInfo.this;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.NEWS;
	}
	
	private Resource resource;

	@Override
	public Resource getResource() {
		return resource;
	}
	
	@Override
	public int getPrototypeId() {
		// TODO Auto-generated method stub
		return getId();
	}

}
