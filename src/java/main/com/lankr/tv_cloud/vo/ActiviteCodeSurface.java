package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.ActivationCode;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class ActiviteCodeSurface extends DataTableModel<ActiviteCodeVo>{
	public void build(List<ActivationCode> list){
		if(list==null||list.isEmpty())
			return ;
		for (ActivationCode activationCode : list) {
			build(activationCode);
		}
	}
	
	public void build(ActivationCode code){
		if(code==null)
			return;
		ActiviteCodeVo vo=new ActiviteCodeVo();
		vo.build(code);
		aaData.add(vo);
	} 

}
