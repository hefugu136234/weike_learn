package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.IntegralConsume;

public interface IntegralConsumeMapper {

	public int addIntegralConsume(IntegralConsume integralConsume);

	public IntegralConsume getIntegralConsumeByUuid(String uuid);

	public List<IntegralConsume> searchIntegralConsumeList(String searchValue,
			int from, int size);

	public int updateIntegralConsume(IntegralConsume integralConsume);

	public int updateIntegralConsumeStatus(IntegralConsume integralConsume);

	public List<IntegralConsume> getLatestGoodsForWx();

	public IntegralConsume getIntegralConsumeByType(int type, int referId);
	
}
