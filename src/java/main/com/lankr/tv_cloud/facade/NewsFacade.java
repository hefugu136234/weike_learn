package com.lankr.tv_cloud.facade;

import java.util.Date;
import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.NewsInfo;

public interface NewsFacade {

	public Pagination<NewsInfo> selectInfoList(String searchValue, int from,
			int pageItemTotal);
	
	public NewsInfo selectInfoByUuid(String uuid);
	
	public Status addNewsInfo(NewsInfo newsInfo);
	
	public Status updateNewsInfo(NewsInfo newsInfo);
	
	public Status updateNewInfoStatus(NewsInfo newsInfo);
	
	public List<NewsInfo> searchAllNews();
	
	public List<NewsInfo> searchAllNews(int categoryId);
	
	public NewsInfo selectInfoByUuidOfApp(String uuid);
	
	public List<NewsInfo> selectListByCategoryId(Date latestTime,int pageSize,int categoryId);
	
	public NewsInfo selectInfoWx(int categoryId);
	
	public List<NewsInfo> searchAllWx(int categoryId);

}
