package com.lankr.tv_cloud.vo.datatable;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Certification;

public class CertificationData extends DataTableModel<CertificationDataItem> {

	public void build(List<Certification> certifications) {
		if (certifications != null && !certifications.isEmpty()) {
			setStatus(Status.SUCCESS);
			for (Certification certification : certifications) {
				aaData.add(CertificationDataItem.build(certification));
			}
		}
	}
}
