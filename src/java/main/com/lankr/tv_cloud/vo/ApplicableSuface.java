package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.ApplicableRecords;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class ApplicableSuface extends DataTableModel<ApplicableVo>{
	
	public void buildData(List<ApplicableRecords> list){
		if(list==null||list.size()==0){
			return;
		}
		for (ApplicableRecords applicableRecords : list) {
			build(applicableRecords);
		}
	}
	
	public void build(ApplicableRecords applicableRecords){
		if(applicableRecords==null)
			return;
		ApplicableVo data=new ApplicableVo();
		data.buildData(applicableRecords);
		aaData.add(data);
	}

}
