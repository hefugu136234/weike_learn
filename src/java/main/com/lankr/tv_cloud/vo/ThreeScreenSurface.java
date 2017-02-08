package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.ThreeScreen;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class ThreeScreenSurface extends DataTableModel<ThreeScreenVo>{
	
	public void bulid(List<ThreeScreen> list){
		if(list==null||list.isEmpty())
			return ;
		for (ThreeScreen threeScreen : list) {
			bulid(threeScreen);
		}
	}
	
	public void bulid(ThreeScreen threeScreen){
		if(threeScreen==null)
			return ;
		ThreeScreenVo vo=new ThreeScreenVo();
		vo.build(threeScreen);
		aaData.add(vo);
	}

}
