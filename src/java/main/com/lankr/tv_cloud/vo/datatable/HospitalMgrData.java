package com.lankr.tv_cloud.vo.datatable;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Hospital;

public class HospitalMgrData extends DataTableModel<HospitalMgrDataItem> {

	public void build(List<Hospital> hospitals) {
		if (hospitals != null && !hospitals.isEmpty()) {
			setStatus(Status.SUCCESS);
			for (Hospital hospital : hospitals) {
				aaData.add(HospitalMgrDataItem.build(hospital));
			}
		}
	}
}
