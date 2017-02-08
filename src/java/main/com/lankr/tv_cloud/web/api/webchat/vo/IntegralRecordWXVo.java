package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.IntegralRecord;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class IntegralRecordWXVo extends BaseAPIModel{
	
	private List<IntegralRecordWXItemVo> list;

	public List<IntegralRecordWXItemVo> getList() {
		return list;
	}

	public void setList(List<IntegralRecordWXItemVo> list) {
		this.list = list;
	}
	
	
	public void buildData(List<IntegralRecord> list){
		this.setStatus(Status.SUCCESS);
		if(list==null||list.isEmpty()){
			return;
		}
		this.list=new ArrayList<IntegralRecordWXItemVo>();
		for (IntegralRecord integralRecord : list) {
			IntegralRecordWXItemVo vo=new IntegralRecordWXItemVo();
			vo.buildData(integralRecord);
			this.list.add(vo);
		}
	}
	

}
