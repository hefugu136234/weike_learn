package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class WxSubjectListVo extends BaseAPIModel{
	
	private List<WxSubjectVo> items;

	public List<WxSubjectVo> getItems() {
		return items;
	}

	public void setItems(List<WxSubjectVo> items) {
		this.items = items;
	}
	
	public void buildList(WxSubject subject,List<WxSubject> list){
		this.setStatus(Status.SUCCESS);
		String parentUuid=OptionalUtils.traceValue(subject, "parent.uuid");
		this.items=new ArrayList<WxSubjectVo>();
//		if(parent!=null){
//			WxSubjectVo vo=new WxSubjectVo();
//			vo.buildParentList(parent, parentUuid);
//			this.items.add(vo);
//		}
		if(Tools.isEmpty(list)){
			return ;
		}
		for (WxSubject wxSubject : list) {
			WxSubjectVo vo=new WxSubjectVo();
			vo.buildParentList(wxSubject, parentUuid);
			this.items.add(vo);
		}
	}
	

}
