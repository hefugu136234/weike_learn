package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class WxNews {
	
	private String title;
	
	private String summary;
	
	private String content;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public void buildDetail(NewsInfo info){
		this.setTitle(OptionalUtils.traceValue(info, "title"));
		this.setSummary(OptionalUtils.traceValue(info, "summary"));
		this.setContent(OptionalUtils.traceValue(info, "content"));
	}

}
