package com.lankr.tv_cloud.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.StringUtils;

import com.lankr.tv_cloud.facade.BannerFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.web.BaseController;

@SuppressWarnings("all")
public class BannerFacadeImpl extends FacadeBaseImpl implements BannerFacade {

	@Override
	public Status saveBanner(Banner banner) throws Exception {
		try {
			bannerMapper.saveBanner(banner);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Save Banner Error", e);
			throw new Exception("保存 Banner 失败");
		}
		return Status.SUCCESS;
	}

	@Override
	public Status updateBanner(Banner banner) {
		try {
			bannerMapper.updateBanner(banner);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Update Banner Error", e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	public Status updateBannerState(Banner banner) {
		try {
			bannerMapper.updateBannerState(banner);
			return Status.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Update Banner Error", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Banner selectBannerByUuid(String uuid) {
		Banner banner = null;
		try {
			banner = bannerMapper.selectBannerByUuid(uuid);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Query Banner By Uuid Error", e);
		}
		return banner;
	}

	@Override
	public Pagination<Banner> selectBannerList(String searchValue,
			int startPage, int pageSize, int type) {
		//设置查询参数
		SubParams subParams = new SubParams();
		subParams.setSize(pageSize);
		subParams.setStart(startPage);
		subParams.setQuery(searchValue);
		subParams.setType(type);
		
		// 初始化分页信息
		searchValue = filterSQLSpecialChars(searchValue);
		StringBuffer sqlBuffer = new StringBuffer(" select count(id) from banner where isActive=1 ");
		if(type != 0){
			sqlBuffer.append(" and type = '" + type + "' or type = '0' ");
		}
		sqlBuffer.append(" and (title like '%" + searchValue + "%' or mark like '%" + searchValue + "%')");
		Pagination<Banner> pagination = initPage(sqlBuffer.toString(), startPage, pageSize);
		
		//查询数据
		List<Banner> bannerList = bannerMapper.selectBannerList(subParams);
		pagination.setResults(bannerList);
		return pagination;
	}

	@Override
	protected String namespace() {
		return null;
	}

	@Override
	public Status deleteBanner(Banner banner) {
		try {
			bannerMapper.deleteBanner(banner);
			return Status.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Delete Banner Error", e);
		}
		return Status.FAILURE;
	}
	
	/*
	@Override
	public Pagination<Banner> selectCallbackBannerList(String searchValue,
			int startPage, int pageSize) {
		//设置查询参数
		SubParams subParams = new SubParams();
		subParams.setSize(pageSize);
		subParams.setStart(startPage);
		subParams.setQuery(searchValue);
		// 初始化分页信息
		searchValue = filterSQLSpecialChars(searchValue);
		StringBuffer sqlBuffer = new StringBuffer(" select count(id) from banner where isActive=0 ");
		sqlBuffer.append(" and (title like '%" + searchValue + "%' or mark like '%" + searchValue + "%')");
		Pagination<Banner> pagination = initPage(sqlBuffer.toString(), startPage, pageSize);
		//查询数据
		List<Banner> bannerList = bannerMapper.selectCallbackBannerList(subParams);
		pagination.setResults(bannerList);
		return pagination;
	}*/
	
	@Override
	public List<Banner> getWxBanner(int type,int position) {
		// TODO Auto-generated method stub
		List<Banner> list=bannerMapper.getWxBanner(type,position);
		if(list==null){
			return null;
		}
		List<Banner> deadTimeList=new ArrayList<Banner>();
		for (Banner banner : list) {
			long time=banner.getCreateDate().getTime();
			time=time+TimeUnit.SECONDS.toMillis(banner.getValidDate());
			long current=new Date().getTime();
			if(time<current){
				deadTimeList.add(banner);
			}
		}
		list.removeAll(deadTimeList);
		return list;
	}

}
