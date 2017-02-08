package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Subscribe;

public interface SubscribeFacade {

	public Status addSubscribe(Subscribe subscribe);

	// 可查询分页
	public Pagination<Subscribe> searchPagenation(int from, int pageItemTotal,
			String searchVal);


	public Subscribe getSubscribeByUUid(String uuid);

	public Status changeSubscribeStatus(Subscribe subscribe);


}
