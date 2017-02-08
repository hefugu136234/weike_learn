package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lankr.tv_cloud.facade.ReceiptAddressFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.IntegralRecord;
import com.lankr.tv_cloud.model.LogisticsInfo;
import com.lankr.tv_cloud.model.ReceiptAddress;

public class ReceiptAddressFacadeImp extends FacadeBaseImpl implements
		ReceiptAddressFacade {

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "com.lankr.orm.mybatis.mapper.ReceiptAddressMapper";
	}

	@Override
	public Status addReceiptAddress(ReceiptAddress receiptAddress) {
		// TODO Auto-generated method stub
		try {
			int effect = receiptAddressMapper.addReceiptAddress(receiptAddress);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("保存地址出错", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateReceiptAddress(ReceiptAddress receiptAddress) {
		// TODO Auto-generated method stub
		try {
			int effect = receiptAddressMapper
					.updateReceiptAddress(receiptAddress);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("更新地址", e);
		}
		return Status.FAILURE;
	}

	@Override
	public ReceiptAddress getReceiptAddressByUuid(String uuid) {
		// TODO Auto-generated method stub
		return receiptAddressMapper.getReceiptAddressByUuid(uuid);
	}

	@Override
	public Status updateReceiptAddressDefault(ReceiptAddress receiptAddress) {
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		try {
			transaction.execute(new TransactionCallback<Object>() {

				@Override
				public Object doInTransaction(TransactionStatus arg0) {
					// TODO Auto-generated method stub
					receiptAddressMapper.updateReceiptAddressNoDefault(receiptAddress);
					receiptAddressMapper.updateReceiptAddressDefault(receiptAddress);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	public ReceiptAddress getReceiptAddressDefault(int userId) {
		return receiptAddressMapper.getReceiptAddressDefault(userId);
	}

	@Override
	public List<ReceiptAddress> wxReceiptAddressPageLimit(int userId,
			String startTime, int size) {
		// TODO Auto-generated method stub
		return receiptAddressMapper.wxReceiptAddressPageLimit(userId,
				startTime, size);
	}

	@Override
	public LogisticsInfo getLogisticsInfoByUuid(String uuid) {
		// TODO Auto-generated method stub
		return receiptAddressMapper.getLogisticsInfoByUuid(uuid);
	}

	@Override
	public Status addLogisticsInfo(LogisticsInfo logisticsInfo) {
		// TODO Auto-generated method stub
		try {
			int effect = receiptAddressMapper.addLogisticsInfo(logisticsInfo);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("保存兑换商品的物流地址", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateLogisticsInfo(LogisticsInfo logisticsInfo) {
		try {
			int effect = receiptAddressMapper.updateLogisticsInfo(logisticsInfo);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("更新兑换商品的物流地址", e);
		}
		return Status.FAILURE;
	}

	@Override
	public LogisticsInfo getLogisticsInfoByRecordId(
			IntegralRecord integralRecord) {
		// TODO Auto-generated method stub
		return receiptAddressMapper.getLogisticsInfoByRecordId(integralRecord.getId());
	}

}
