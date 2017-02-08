package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.ActivitySubject;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class ActivitySubjectSurface extends DataTableModel<ActivitySubjectItemVo>{
	
	public void buildList(List<ActivitySubject> list){
		if(list==null||list.isEmpty())
			return;
		for (ActivitySubject activitySubject : list) {
			ActivitySubjectItemVo vo=new ActivitySubjectItemVo();
			vo.buildTableData(activitySubject);
			aaData.add(vo);
		}
	}

}
