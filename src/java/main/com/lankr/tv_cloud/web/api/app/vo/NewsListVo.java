package com.lankr.tv_cloud.web.api.app.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class NewsListVo extends BaseAPIModel{
	
	private List<NewsItem> items;
	
	public void buildData(List<NewsInfo> list){
		items=new ArrayList<NewsItem>();
		for (NewsInfo newsInfo : list) {
			NewsItem item=new NewsItem();
			item.buildData(newsInfo, false);
			items.add(item);
		}
	}
	

}
