package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class PdfSuface extends DataTableModel<PdfInfoVo>{
	
	public void buildData(List<PdfInfo> list){
		if(list==null||list.size()==0){
			return;
		}
		for (PdfInfo pdfInfo : list) {
			build(pdfInfo);
		}
	}
	
	public void build(PdfInfo pdfInfo){
		if(pdfInfo==null)
			return;
		PdfInfoVo data=new PdfInfoVo();
		data.buildData(pdfInfo);
		aaData.add(data);
	}

}
