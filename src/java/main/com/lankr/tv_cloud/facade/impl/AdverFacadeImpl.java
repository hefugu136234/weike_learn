package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import com.lankr.tv_cloud.facade.AdverFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Advertisement;
import com.lankr.tv_cloud.model.AdvertisementPosition;

public class AdverFacadeImpl extends FacadeBaseImpl implements AdverFacade {

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "adver";
	}

	@Override
	public Pagination<Advertisement> searchAdverList(String search, int from,
			int size, int projectId) {
		String sql = "select count(id) from advertisement "
				+ "where projectId=" + projectId + " and name like '%"
				+ filterSQLSpecialChars(search) + "%'";
		Pagination<Advertisement> pagin = initPage(sql, from, size);
		SubParams params = new SubParams();
		params.id = projectId;
		params.query = filterSQLSpecialChars(search);
		List<Advertisement> results = adverDao.searchAllPagination(
				getSqlAlias("searchAdverList"), params, from, size);
		pagin.setResults(results);
		return pagin;
	}

	@Override
	public Advertisement searchAdverByUuid(String uuid) {
		return adverDao.getById(uuid, getSqlAlias("searchAdverByUuid"));
	}

	@Override
	public Status addAdver(Advertisement adver) {
		try {
			adverDao.add(adver, getSqlAlias("addAdver"));
		} catch (Exception e) {
			logger.error("add adver error", e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	public Status updateAdver(Advertisement adver) {
		try {
			adverDao.update(adver, getSqlAlias("updateAdver"));
		} catch (Exception e) {
			logger.error("update adver error for modify", e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}
	
	@Override
	public Status updateAdverToStatus(Advertisement adver) {
		try {
			adverDao.update(adver, getSqlAlias("updateAdverToStatus"));
		} catch (Exception e) {
			logger.error("update adver error for status", e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}
	
	@Override
	public List<AdvertisementPosition> searchAllPostion() {
		List<AdvertisementPosition> list = adPostionDao
				.searchAll(getSqlAlias("searchAllAdPostion"));
		return list;
	}
	

}
