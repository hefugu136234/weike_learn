package com.lankr.tv_cloud.web.front.vo;

import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class FrontNewsVo {
	
	private String title;
	
	private String content;
	
	private String date;

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
	
	
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void build(NewsInfo info){
		this.setTitle(OptionalUtils.traceValue(info, "title"));
		this.setContent(OptionalUtils.traceValue(info, "content"));
		this.setDate(Tools.formatYMDDate(info.getCreateDate()));
	}

}
