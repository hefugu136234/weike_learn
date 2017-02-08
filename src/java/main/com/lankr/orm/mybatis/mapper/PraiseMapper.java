package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.Praise;

public interface PraiseMapper {
	
	public void addPraise(Praise praise);
	
	public Praise selectPraiseById(int id);
	
	public Praise selectPraiseByUuid(String uuid);
	
	public Praise selectPraiseByReIdAndUserId(SubParams param);
	
	public int selectCountByReId(int id);
	
	public void updatePraiseStatus(Praise praise);

	public List<Praise> getUserPraiseRecord(int id, String query,
			int startPage, int pageSize);

	public List<Praise> getPraiseUserRecord(int id, String query,
			int startPage, int pageSize);

}
