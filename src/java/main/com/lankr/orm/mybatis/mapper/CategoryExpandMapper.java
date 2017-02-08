package com.lankr.orm.mybatis.mapper;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.CategoryExpand;

public interface CategoryExpandMapper {
	
	public CategoryExpand selectExpandById(int id);
	
	public CategoryExpand selectExpandByUuid(String uuid);
	
	public void addExpand(CategoryExpand categoryExpand);
	
	public void updateExpand(CategoryExpand categoryExpand);
	
	public void updateExpandStatus(CategoryExpand categoryExpand);
	
	public CategoryExpand selectExpandByCateId(int categoryId);
	
	public CategoryExpand selectExpandBycateType(SubParams subParams);

}
