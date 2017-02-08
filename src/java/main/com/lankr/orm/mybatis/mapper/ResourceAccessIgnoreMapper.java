package com.lankr.orm.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.ResourceAccessIgnore;

public interface ResourceAccessIgnoreMapper {

	public int add(ResourceAccessIgnore resourceAccess);

	public int del(ResourceAccessIgnore resourceAccess);

	public ResourceAccessIgnore getByUuid(String uuid);

	public int update(ResourceAccessIgnore resourceAccess);
	
	public ResourceAccessIgnore getByResourceId(@Param("id") int id);
}
