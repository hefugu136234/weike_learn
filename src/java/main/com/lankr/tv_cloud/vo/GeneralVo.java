package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.model.BaseModel;

public interface GeneralVo<T extends BaseModel> {

	public void format(T t);
	
}
