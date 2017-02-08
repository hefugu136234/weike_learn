package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.facade.CertificationFacade;
import com.lankr.tv_cloud.facade.WebchatFacade;
import com.lankr.tv_cloud.model.QrcodeScanRecode;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class QrcodeScanRecodeSurface extends DataTableModel<QrcodeScanRecodeVo> {

	public void buildList(List<QrcodeScanRecode> list,
			WebchatFacade webchatFacade, CertificationFacade certificationFacade) {
		if (list == null || list.isEmpty())
			return;
		for (QrcodeScanRecode qrcodeScanRecode : list) {
			QrcodeScanRecodeVo vo = new QrcodeScanRecodeVo();
			vo.buildTableData(qrcodeScanRecode, webchatFacade,
					certificationFacade);
			aaData.add(vo);
		}

	}

}
