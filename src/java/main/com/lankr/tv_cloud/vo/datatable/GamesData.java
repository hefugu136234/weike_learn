package com.lankr.tv_cloud.vo.datatable;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Lottery;

public class GamesData extends DataTableModel<GamesDataItem> {

	public void build(List<Lottery> lotterys) {
		if (lotterys != null && !lotterys.isEmpty()) {
			setStatus(Status.SUCCESS);
			for (Lottery lottery : lotterys) {
				aaData.add(GamesDataItem.build(lottery));
			}
		}
	}
}
