package com.lankr.orm.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.PageRemain;

public interface PageRemainMapper {

	public int addPageRemain(PageRemain pageRemain);

	public PageRemain selectPageRemainByUuid(String uuid);

	public int updatePageRemain(PageRemain pageRemain);

	public Integer resViewTimeByUser(@Param("referType") int referType,
			@Param("referId") int referId, @Param("userId") int userId);

}
