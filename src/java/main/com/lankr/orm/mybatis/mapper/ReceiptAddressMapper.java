package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.LogisticsInfo;
import com.lankr.tv_cloud.model.ReceiptAddress;

public interface ReceiptAddressMapper {
	
	public int addReceiptAddress(ReceiptAddress receiptAddress);
	
	public int updateReceiptAddress(ReceiptAddress receiptAddress);
	
	public ReceiptAddress getReceiptAddressByUuid(String uuid);
	
	public int updateReceiptAddressNoDefault(ReceiptAddress receiptAddress);
	
	public int updateReceiptAddressDefault(ReceiptAddress receiptAddress);
	
	public ReceiptAddress getReceiptAddressDefault(int userId);
	
	public List<ReceiptAddress> wxReceiptAddressPageLimit(int userId,String startTime,int size);
	
	public LogisticsInfo getLogisticsInfoByUuid(String uuid);
	
	public int addLogisticsInfo(LogisticsInfo logisticsInfo);
	
	public int updateLogisticsInfo(LogisticsInfo logisticsInfo);
	
	public LogisticsInfo getLogisticsInfoByRecordId(int integralRecordId);

}
