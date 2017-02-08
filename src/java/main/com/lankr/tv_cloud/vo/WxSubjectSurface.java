package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.facade.AssetFacade;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class WxSubjectSurface extends DataTableModel<WxSubjectVo> {

	public void buildList(List<WxSubject> list, AssetFacade assetFacade) {
		if (list == null || list.isEmpty())
			return;
		for (WxSubject wxSubject : list) {
			Category category = assetFacade.getCategoryById(wxSubject
					.getReflectId());
			WxSubjectVo vo = new WxSubjectVo();
			vo.buildFirstList(wxSubject, category);
			aaData.add(vo);
		}
	}

	public void buildSubList(List<WxSubject> list, AssetFacade assetFacade, int rootType) {
		if (list == null || list.isEmpty())
			return;
		if(rootType==WxSubject.TYPE_ACTIVITY){
			//活动
			for (WxSubject wxSubject : list) {
				WxSubjectVo vo = new WxSubjectVo();
				vo.buildActivityList(wxSubject);
				aaData.add(vo);
			}
		}else if(rootType==WxSubject.TYPE_CATEGORY){
			//学科
			buildList(list, assetFacade);
		}

	}

}
