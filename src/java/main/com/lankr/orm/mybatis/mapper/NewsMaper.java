package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.NewsInfo;

public interface NewsMaper {
	
	public NewsInfo selectInfoById(int id);
	
	public NewsInfo selectInfoByUuid(String uuid);
	
	public void addInfo(NewsInfo newsInfo);
	
	public void updateInfo(NewsInfo newsInfo);
	
	public List<NewsInfo> selectInfoList(String query);
	
	public void updateInfoStatus(NewsInfo newsInfo);

}
