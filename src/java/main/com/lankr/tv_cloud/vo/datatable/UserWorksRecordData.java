package com.lankr.tv_cloud.vo.datatable;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.IntegralRecord;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.UserWorksRecord;

public class UserWorksRecordData extends DataTableModel<UserWorksRecordDataItem> {

	public void build(List<Resource> userWorksRecords) {
		if (userWorksRecords != null && !userWorksRecords.isEmpty()) {
			setStatus(Status.SUCCESS);
			for (Resource resource : userWorksRecords) {
				aaData.add(UserWorksRecordDataItem.build(resource));
			}
		}
	}
}
