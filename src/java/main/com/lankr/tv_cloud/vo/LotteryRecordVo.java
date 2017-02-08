package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.datatable.LotteryRecordDataItem;

public class LotteryRecordVo extends BaseAPIModel{

	private List<LotteryRecordDataItem> items;

	public List<LotteryRecordDataItem> getItems() {
		return items;
	}

	public void setItems(List<LotteryRecordDataItem> items) {
		this.items = items;
	}
	
}
