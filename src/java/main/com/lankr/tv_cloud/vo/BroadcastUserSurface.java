package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.facade.CertificationFacade;
import com.lankr.tv_cloud.facade.WebchatFacade;
import com.lankr.tv_cloud.model.BroadcastUser;
import com.lankr.tv_cloud.model.SignUpUser;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class BroadcastUserSurface extends DataTableModel<BroadcastUserVo>{
	
	public void buildList(List<BroadcastUser>  list,WebchatFacade webchatFacade,CertificationFacade certificationFacade){
		if(list==null||list.isEmpty())
			return;
		for (BroadcastUser broadcastUser : list) {
			BroadcastUserVo vo=new BroadcastUserVo();
			vo.buildTableData(broadcastUser, webchatFacade, certificationFacade);
			aaData.add(vo);
		}
		
	}
	
	
	public void buildBookUser(List<SignUpUser>  list,WebchatFacade webchatFacade,CertificationFacade certificationFacade){
		if(list==null||list.isEmpty())
			return;
		for (SignUpUser signUpUser : list) {
			BroadcastUserVo vo=new BroadcastUserVo();
			vo.buildBookUser(signUpUser, webchatFacade, certificationFacade);
			aaData.add(vo);
		}
		
	}

}
