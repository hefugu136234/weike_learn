package com.lankr.tv_cloud.vo.datatable;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;

public class ActivityData extends DataTableModel<ActivityDataItem> {

	public void build(List<Activity> activities) {
		if (activities != null && !activities.isEmpty()) {
			setStatus(Status.SUCCESS);
			for (Activity activity : activities) {
				aaData.add(ActivityDataItem.build(activity));
			}
		}
	}
}
