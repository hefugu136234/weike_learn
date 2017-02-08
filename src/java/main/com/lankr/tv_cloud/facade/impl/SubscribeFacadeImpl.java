package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.SubscribeFacade;
import com.lankr.tv_cloud.model.Subscribe;

public class SubscribeFacadeImpl extends FacadeBaseImpl implements
		SubscribeFacade {

	@Override
	protected String namespace() {
		return "user_subscribe";
	}

	@Override
	public Status addSubscribe(Subscribe subscribe) {
		// TODO Auto-generated method stub
		try {
			subscribeDao.add(subscribe, getSqlAlias("addSubscribe"));
			return Status.SUCCESS;
		} catch (Exception e) {
			logger.error("add Subscribe occur error", e);
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public Pagination<Subscribe> searchPagenation(int from, int pageItemTotal,
			String searchValue) {
		System.out.println("1:"+searchValue);
		searchValue=filterSQLSpecialChars(searchValue);
		System.out.println("2:"+searchValue);
		Pagination<Subscribe> pu = initPage(
				"select count(id) from subscribe where  (name like '%"
						+ searchValue
						+ "%' or mobile like '%"
						+ searchValue
						+ "%')", from,
				pageItemTotal);
		List<Subscribe> results=null;
		try {
			results = subscribeDao
					.searchAllPagination(getSqlAlias("selectSubscribesPage"),
							searchValue, from, pageItemTotal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pu.setResults(results);
		return pu;
	}


	@Override
	public Subscribe getSubscribeByUUid(String uuid) {
		// TODO Auto-generated method stub
		return subscribeDao.getById(uuid, getSqlAlias("getSubscribeByUUid"));
	}

	@Override
	public Status changeSubscribeStatus(Subscribe subscribe) {
		// TODO Auto-generated method stub
		try {
			subscribeDao
					.update(subscribe, getSqlAlias("changeSubscribeStatus"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("update status Subscribe occur error", e);
		}
		return Status.FAILURE;
	}

}
