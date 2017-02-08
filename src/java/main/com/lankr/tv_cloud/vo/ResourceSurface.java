package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.facade.ActivityOpusFacade;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class ResourceSurface extends DataTableModel<ResourceSurfaceVo>{
	
	public void bulid(List<Resource> list){
		if(list==null||list.isEmpty())
			return ;
		for (Resource resource : list) {
			bulid(resource);
		}
	}
	
	public void bulid(Resource resource){
		if(resource==null)
			return ;
		ResourceSurfaceVo vo=new ResourceSurfaceVo();
		vo.build(resource);
//		boolean flag=activityOpusFacade.oupsResCount(resource);
//		vo.setRelatedOups(flag);
		aaData.add(vo);
	}

}
