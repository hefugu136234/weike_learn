package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.Subscribe;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class SubscribeSuface extends DataTableModel<SubscribeVo> {

	public void buildData(List<Subscribe> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		for (Subscribe subscribe : list) {
			build(subscribe);
		}
	}

	public void build(Subscribe subscribe) {
		if (subscribe == null)
			return;
		SubscribeVo data = new SubscribeVo();
		data.build(subscribe);
		aaData.add(data);
	}

}
