package com.lankr.tv_cloud.vo.datatable;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityUser;

public class ActivityUserData extends DataTableModel<ActivityUserDataItem> {

	public void build(List<ActivityUser> activitieUsers) {
		if (activitieUsers != null && !activitieUsers.isEmpty()) {
			setStatus(Status.SUCCESS);
			for (ActivityUser activitieUser : activitieUsers) {
				aaData.add(ActivityUserDataItem.build(activitieUser));
			}
		}
	}
}
