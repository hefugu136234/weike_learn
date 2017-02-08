package com.lankr.tv_cloud.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.orm.mybatis.mapper.PraiseMapper;
import com.lankr.tv_cloud.facade.PraiseFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Praise;

public class PraiseFacadeImp extends FacadeBaseImpl implements PraiseFacade{
	

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status addPraise(Praise praise) {
		// TODO Auto-generated method stub
		try {
			praiseMapper.addPraise(praise);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加点赞数据失败", e);
		}
		return Status.SAVE_ERROR;
	}

	@Override
	public Praise selectPraiseById(int id) {
		// TODO Auto-generated method stub
		return praiseMapper.selectPraiseById(id);
	}

	@Override
	public Praise selectPraiseByUuid(String uuid) {
		// TODO Auto-generated method stub
		return praiseMapper.selectPraiseByUuid(uuid);
	}

	@Override
	public boolean selectPraiseByReIdAndUserId(int resId, int userId) {
		// TODO Auto-generated method stub
		SubParams param=new SubParams();
		param.id=resId;
		param.userId=userId;
		Praise praise=praiseMapper.selectPraiseByReIdAndUserId(param);
		if(praise!=null&&praise.getUuid()!=null){
			if(praise.getStatus()==1){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Praise selectPraiseByReIdUserId(int resId, int userId) {
		// TODO Auto-generated method stub
		SubParams param=new SubParams();
		param.id=resId;
		param.userId=userId;
		Praise praise=praiseMapper.selectPraiseByReIdAndUserId(param);
		return praise; 
	}

	@Override
	public int selectCountByReId(int id) {
		// TODO Auto-generated method stub
		return praiseMapper.selectCountByReId(id);
	}

	@Override
	public Status updatePraiseStatus(Praise praise) {
		// TODO Auto-generated method stub
		try {
			praiseMapper.updatePraiseStatus(praise);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("更改点赞状态失败", e);
		}
		return Status.FAILURE;
	}

}
