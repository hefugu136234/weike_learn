package com.lankr.tv_cloud.vo.datatable;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ActivityExpert;
import com.lankr.tv_cloud.model.ActivityResource;

public class ActivityExpertData extends DataTableModel<ActivityExpertDataItem> {

	public void build(List<ActivityExpert> activityExperts) {
		if (activityExperts != null && !activityExperts.isEmpty()) {
			setStatus(Status.SUCCESS);
			for (ActivityExpert activityExpert : activityExperts) {
				aaData.add(ActivityExpertDataItem.build(activityExpert));
			}
		}
	}
}
