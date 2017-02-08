package com.lankr.tv_cloud.vo.datatable;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ActivityResource;

public class ActivityResData extends DataTableModel<ActivityResDataItem> {

	public void build(List<ActivityResource> activityResources) {
		if (activityResources != null && !activityResources.isEmpty()) {
			setStatus(Status.SUCCESS);
			for (ActivityResource activityResource : activityResources) {
				aaData.add(ActivityResDataItem.build(activityResource));
			}
		}
	}
}
