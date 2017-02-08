package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.CommonPraise;


public interface CommonPraiseMapper {
	
	public int saveCommonPraise(CommonPraise commonPraise) ;
	
	public int updateCommonPraise(CommonPraise commonPraise);
	
	public CommonPraise selectCommonPraiseByUuid(String uuid);
	
	public CommonPraise selectCommonPraiseById(int id);
	
	public int selectCountByReferId(int referType, int referId);

	public CommonPraise selectCommonPraiseByUser(int referType, int referId, int userId);
	
	
}
