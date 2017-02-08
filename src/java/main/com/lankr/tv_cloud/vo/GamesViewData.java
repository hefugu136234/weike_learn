package com.lankr.tv_cloud.vo;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Lottery;
import com.lankr.tv_cloud.utils.Tools;

public class GamesViewData {
	
	private String name;
	private String uuid;
	private String context;
	private String mark;
	private String url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public static GamesViewData buildViewData(Lottery lottery) {
		GamesViewData data = new GamesViewData();
		if(null == lottery)
			return data;
		data.setName(Tools.nullValueFilter(lottery.getName()));
		data.setUuid(Tools.nullValueFilter(lottery.getUuid()));
		data.setContext(Tools.nullValueFilter(lottery.getPage()));
		data.setMark(Tools.nullValueFilter(lottery.getMark()));
		return data;
	}
}
