package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.ActivationCode;
import com.lankr.tv_cloud.model.ProductGroup;
import com.lankr.tv_cloud.model.ViewSharing;

public interface ActivationCodeFacade {
	public Pagination<ActivationCode> selectActivationList(String searchValue,
			int from, int pageItemTotal);

	public Status addActivation(ActivationCode activationCode);
	
	public Status addBuildActivite(int numint,ProductGroup productGroup,int timeint);
	
	public ActivationCode getActivationByCode(String code);
	
	public ActivationCode getActivationByUuid(String uuid);
	
	public Status updateDisCode(ActivationCode activationCode);

	//modified by mayuan --> show sharedetail
	public Pagination<ViewSharing> selectShareList(String searchValue, int start, int pageSize);

	//modified by mayuan -->根据用户id查询流量卡
	public List<ActivationCode> getActivationByUserId(int userId);

}
