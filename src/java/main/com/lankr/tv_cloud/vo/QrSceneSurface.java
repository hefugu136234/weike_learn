package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.facade.QrCodeFacade;
import com.lankr.tv_cloud.model.QrCode;
import com.lankr.tv_cloud.model.QrScene;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class QrSceneSurface extends DataTableModel<QrSceneVo>{
	
	public void buildList(List<QrScene> list,QrCodeFacade qrCodeFacade){
		if(list==null||list.isEmpty())
			return;
		for (QrScene qrScene : list) {
			QrSceneVo vo=new QrSceneVo();
			QrCode qrCode=qrCodeFacade.selectQrCodeByScenId(qrScene.getSceneid());
			int count=OptionalUtils.traceInt(qrCode, "scancount");
			vo.buildTableData(qrScene,count);
			aaData.add(vo);
		}
	}

}
