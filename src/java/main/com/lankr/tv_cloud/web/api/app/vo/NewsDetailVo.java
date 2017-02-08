package com.lankr.tv_cloud.web.api.app.vo;

import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class NewsDetailVo extends BaseAPIModel{
	private NewsItem item;
	
	public void buildData(NewsInfo info){
		item=new NewsItem();
		item.buildData(info, true);
	}

}
