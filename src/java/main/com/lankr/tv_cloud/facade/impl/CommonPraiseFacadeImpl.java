package com.lankr.tv_cloud.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.orm.mybatis.mapper.CommonPraiseMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.CommonPraiseFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.CommonPraise;

public class CommonPraiseFacadeImpl extends FacadeBaseImpl implements CommonPraiseFacade{
	
	protected CommonPraiseMapper commonPraiseMapper;

	@Autowired
	public void setCommonMapper(CommonPraiseMapper commonPraiseMapper) {
		this.commonPraiseMapper = commonPraiseMapper;
	}

	@Override
	public int saveCommonPraise(CommonPraise commonPraise) {
		int effect = commonPraiseMapper.saveCommonPraise(commonPraise);
		return effect;
	}

	@Override
	public int updateCommonPraise(CommonPraise commonPraise) {
		int effect = commonPraiseMapper.updateCommonPraise(commonPraise);
		return effect;
	}

	@Override
	public CommonPraise selectCommonPraiseByUuid(String uuid) {
		return commonPraiseMapper.selectCommonPraiseByUuid(uuid);
	}

	@Override
	public CommonPraise selectCommonPraiseById(int id) {
		return commonPraiseMapper.selectCommonPraiseById(id);
	}

	@Override
	public int selectCountByReferId(int referType, int referId) {
		return commonPraiseMapper.selectCountByReferId(referType, referId);
	}

	@Override
	public CommonPraise selectCommonPraiseByUser(int referType, int referId, int userId) {
		return commonPraiseMapper.selectCommonPraiseByUser(referType,referId,userId);
	}

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return commonPraiseMapper.getClass().getName();
	}

}
