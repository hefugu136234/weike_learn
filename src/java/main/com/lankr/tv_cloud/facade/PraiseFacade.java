package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.model.Praise;

public interface PraiseFacade {

	public Status addPraise(Praise praise);

	public Praise selectPraiseById(int id);

	public Praise selectPraiseByUuid(String uuid);

	public boolean selectPraiseByReIdAndUserId(int resId,int userId);
	
	public Praise selectPraiseByReIdUserId(int resId,int userId);

	public int selectCountByReId(int id);

	public Status updatePraiseStatus(Praise praise);

}
