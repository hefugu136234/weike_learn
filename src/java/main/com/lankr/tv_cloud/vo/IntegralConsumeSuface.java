package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;
import com.lankr.tv_cloud.vo.datatable.IntegralConsumeDataItem;

public class IntegralConsumeSuface extends DataTableModel<IntegralConsumeDataItem>{
	
	public void build(List<IntegralConsume> list){
		if(list==null||list.isEmpty())
			return ;
		for (IntegralConsume ingegralConsume : list) {
			aaData.add(IntegralConsumeDataItem.build(ingegralConsume));
		}
		//aaData.addAll(list);
//		for (IntegralConsume integralConsume : list) {
//			if(integralConsume.getCover()==null){
//				integralConsume.setCover("");
//			}
//			aaData.add(integralConsume);
//		}
	}

}
