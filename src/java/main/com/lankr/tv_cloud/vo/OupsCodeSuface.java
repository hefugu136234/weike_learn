package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class OupsCodeSuface extends DataTableModel<OupsCodeVo>{
	
	public void buildData(List<ActivityApplication> list){
		if(list==null||list.size()==0){
			return ;
		}
		for (ActivityApplication activityApplication : list) {
			buildData(activityApplication);
		}
	}
	
	public void buildData(ActivityApplication activityApplication){
		if(activityApplication==null){
			return ;
		}
		OupsCodeVo oupsCodeVo=new OupsCodeVo();
		oupsCodeVo.build(activityApplication);
		aaData.add(oupsCodeVo);
	}

}
