package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.Advertisement;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class AdverClientData extends DataTableModel<AdverClientItem>{
	public void buildData(Advertisement adver) {
		if (adver == null)
			return;
		AdverClientItem item = new AdverClientItem();
		item.format(adver);
		aaData.add(item);
	}

	public void buildData(List<Advertisement> list) {
		if (list == null || list.isEmpty())
			return;
		for (Advertisement model : list) {
			buildData(model);
		}
	}

}
