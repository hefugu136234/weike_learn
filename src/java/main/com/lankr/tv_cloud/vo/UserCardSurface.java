package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.ActivationCode;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class UserCardSurface extends DataTableModel<UserCardVo>{

	public void bulidData(List<ActivationCode> activationCodes) {
		if(null != activationCodes){
			for(ActivationCode activationCode:activationCodes){
				UserCardVo vo = new UserCardVo();
				vo.bulidData(activationCode);
				aaData.add(vo);
			}
		}
	}
}
