package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.NewsInfo;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class NewsSuface extends DataTableModel<NewsData>{
	
	public void buildData(List<NewsInfo> list){
		if(list==null||list.size()==0){
			return;
		}
		for (NewsInfo newsInfo : list) {
			build(newsInfo);
		}
	}
	
	public void build(NewsInfo info){
		if(info==null)
			return;
		NewsData data=new NewsData();
		data.buildData(info,false);
		aaData.add(data);
	}

}
