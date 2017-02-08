package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.model.IntegralRecord;
import com.lankr.tv_cloud.model.LogisticsInfo;
import com.lankr.tv_cloud.model.ReceiptAddress;

public interface ReceiptAddressFacade {

	public Status addReceiptAddress(ReceiptAddress receiptAddress);

	public Status updateReceiptAddress(ReceiptAddress receiptAddress);

	public ReceiptAddress getReceiptAddressByUuid(String uuid);

	public Status updateReceiptAddressDefault(ReceiptAddress receiptAddress);

	public ReceiptAddress getReceiptAddressDefault(int userId);

	public List<ReceiptAddress> wxReceiptAddressPageLimit(int userId,
			String startTime, int size);

	public LogisticsInfo getLogisticsInfoByUuid(String uuid);

	public Status addLogisticsInfo(LogisticsInfo logisticsInfo);

	public Status updateLogisticsInfo(LogisticsInfo logisticsInfo);

	public LogisticsInfo getLogisticsInfoByRecordId(IntegralRecord integralRecord);

}
