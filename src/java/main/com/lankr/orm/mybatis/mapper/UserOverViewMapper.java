package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.MyCollection;
import com.lankr.tv_cloud.model.Praise;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.UserWorksRecord;

public interface UserOverViewMapper {

	List<Resource> searchUserWorksRecordPagination(int id,
			String filterSQLSpecialChars, int startPage, int pageSize);
	
}
