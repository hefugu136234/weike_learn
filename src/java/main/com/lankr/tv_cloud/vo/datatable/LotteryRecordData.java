package com.lankr.tv_cloud.vo.datatable;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.LotteryRecord;
import com.lankr.tv_cloud.model.Shake;

public class LotteryRecordData extends DataTableModel<LotteryRecordDataItem> {

	public void build(List<LotteryRecord> records) {
		if (records != null && !records.isEmpty()) {
			setStatus(Status.SUCCESS);
			for (LotteryRecord lotteryRecord : records) {
				aaData.add(LotteryRecordDataItem.build(lotteryRecord));
			}
		}
	}

	public List<LotteryRecordDataItem> buildForWechat(List<LotteryRecord> list) {
		List<LotteryRecordDataItem> items = new ArrayList<LotteryRecordDataItem>();
		for (LotteryRecord lotteryRecord : list) {
			items.add(LotteryRecordDataItem.build(lotteryRecord));
		}
		return items;
	}
}
