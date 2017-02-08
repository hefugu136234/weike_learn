package com.lankr.tv_cloud.vo.datatable;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.IntegralRecord;

public class IntegralExchangeData extends DataTableModel<IntegralExchangeDataItem> {

	public void build(List<IntegralRecord> integralRecords) {
		if (integralRecords != null && !integralRecords.isEmpty()) {
			setStatus(Status.SUCCESS);
			for (IntegralRecord integralRecord : integralRecords) {
				aaData.add(IntegralExchangeDataItem.build(integralRecord));
			}
		}
	}
}
