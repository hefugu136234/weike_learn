package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.ViewSharing;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class ShowShareListSurface extends DataTableModel<ShowShareListVo>{
	public void build(List<ViewSharing> list){
		if(list==null||list.isEmpty())
			return ;
		for (ViewSharing viewSharing : list) {
			build(viewSharing);
		}
	}
	
	public void build(ViewSharing share){
		if(share==null)
			return;
		ShowShareListVo vo=new ShowShareListVo();
		vo.build(share);
		aaData.add(vo);
	} 

}

