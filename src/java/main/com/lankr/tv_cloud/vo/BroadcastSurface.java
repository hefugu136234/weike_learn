package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class BroadcastSurface extends DataTableModel<BroadcastWebVo>{
	
	public void buildList(List<Broadcast> list){
		if(list==null||list.isEmpty())
			return;
		for (Broadcast broadcast : list) {
			BroadcastWebVo vo=new BroadcastWebVo();
			vo.buildTableData(broadcast);
			aaData.add(vo);
		}
	}
	

}
