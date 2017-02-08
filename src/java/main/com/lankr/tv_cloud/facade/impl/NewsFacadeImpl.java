package com.lankr.tv_cloud.facade.impl;

import java.util.Date;
import java.util.List;

import com.lankr.tv_cloud.facade.NewsFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.NewsInfo;

public class NewsFacadeImpl extends FacadeBaseImpl implements NewsFacade {

	// @Autowired
	// private NewsMaper newsDao;
	//
	//
	//
	// public void setNewsDao(NewsMaper newsDao) {
	// this.newsDao = newsDao;
	// }

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "com.lankr.orm.mybatis.mapper.NewsMaper";
	}

	/**
	 * 可查询分页
	 */
	@Override
	public Pagination<NewsInfo> selectInfoList(String searchValue, int from,
			int pageItemTotal) {
		searchValue=filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from news_info where isActive=1 and (title like '%"+
				searchValue+"%' or author like '%"+searchValue+"%')";
		System.out.println(sql);
		Pagination<NewsInfo> pagination = initPage(sql, from, pageItemTotal);
		List<NewsInfo> newsInfos = newsInfoDao.searchAllPagination(
				getSqlAlias("selectInfoList"),
				filterSQLSpecialChars(searchValue), from, pageItemTotal);
		pagination.setResults(newsInfos);
		return pagination;
	}

	@Override
	public NewsInfo selectInfoByUuid(String uuid) {
		return newsInfoDao.getById(uuid, getSqlAlias("selectInfoByUuid"));
	}

	@Override
	public Status addNewsInfo(NewsInfo newsInfo) {
		try {
			newsInfoDao.add(newsInfo, getSqlAlias("addInfo"));
			//添加到资源表
			recodeResource(newsInfo);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("newsInfo add occur a error",e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateNewsInfo(NewsInfo newsInfo) {
		try {
			newsInfoDao.update(newsInfo, getSqlAlias("updateInfo"));
			recodeResource(newsInfo);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("newsInfo update info occur a error", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateNewInfoStatus(NewsInfo newsInfo) {
		try {
			newsInfoDao.update(newsInfo, getSqlAlias("updateInfoStatus"));
			recodeResource(newsInfo);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("newsInfo update status occur a error", e);
		}
		return Status.FAILURE;
	}
	
	@Override
	public List<NewsInfo> searchAllNews() {
		// TODO Auto-generated method stub
		return newsInfoDao.searchAll(getSqlAlias("searchAllNews"));
	}
	
	@Override
	public List<NewsInfo> searchAllNews(int categoryId) {
		// TODO Auto-generated method stub
		return newsInfoDao.searchAll(getSqlAlias("searchAllNewsByName"), categoryId);
	}
	
	@Override
	public NewsInfo selectInfoByUuidOfApp(String uuid) {
		// TODO Auto-generated method stub
		return newsInfoDao.getById(uuid, getSqlAlias("selectInfoByUuidOfApp"));
	}
	
	@Override
	public List<NewsInfo> selectListByCategoryId(Date latestTime,
			int pageSize, int categoryId) {
		// TODO Auto-generated method stub
		if (pageSize < 0)
			pageSize = 0;
		if (pageSize >= 30)
			pageSize = 30;
		SubParams param=new SubParams();
		param.updated_at=latestTime;
		param.size=pageSize;
		param.id=categoryId;
		return newsInfoDao.searchAll(getSqlAlias("selectListByCategoryId"), param);
	}
	
	@Override
	public NewsInfo selectInfoWx(int categoryId) {
		// TODO Auto-generated method stub
		return newsInfoDao.getById(categoryId, getSqlAlias("selectInfoWx"));
	}
	
	@Override
	public List<NewsInfo> searchAllWx(int categoryId) {
		// TODO Auto-generated method stub
		return newsInfoDao.searchAll(getSqlAlias("searchAllWx"), categoryId);
	}

}
