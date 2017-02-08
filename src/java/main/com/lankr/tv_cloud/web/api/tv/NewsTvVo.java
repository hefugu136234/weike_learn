package com.lankr.tv_cloud.web.api.tv;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.api.app.vo.NewsItem;


public class NewsTvVo extends BaseAPIModel{
	
	private List<NewsItem> newsList;

	public List<NewsItem> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<NewsItem> newsList) {
		this.newsList = newsList;
	}
	
	public void fommatData(List<NewsInfo> list){
		if(list==null||list.isEmpty())
			return;
		this.newsList=new ArrayList<NewsItem>();
		for (NewsInfo newsInfo : list) {
			NewsItem data=new NewsItem();
			data.buildData(newsInfo, false);
			this.newsList.add(data);
		}
	}
	
}
