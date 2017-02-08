package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.Shake;

public interface ShakeMapper {

	List<Shake> searchExchangeRecordForDatatable(String queryKey,
			int startPage, int pageSize, @Param("money") String isWiner, @Param("status") String isHandle);

	Shake selectShakeByUuid(String uuid);

	int updateShakeStatus(Shake shake);
	
}
