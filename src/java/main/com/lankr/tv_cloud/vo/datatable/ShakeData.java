package com.lankr.tv_cloud.vo.datatable;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Shake;

public class ShakeData extends DataTableModel<ShakeDataItem> {

	public void build(List<Shake> shakes) {
		if (shakes != null && !shakes.isEmpty()) {
			setStatus(Status.SUCCESS);
			for (Shake shake : shakes) {
				aaData.add(ShakeDataItem.build(shake));
			}
		}
	}
}
