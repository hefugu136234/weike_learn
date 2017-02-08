package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Advertisement;
import com.lankr.tv_cloud.model.AdvertisementPosition;

public interface AdverFacade {
	public Pagination<Advertisement> searchAdverList(String search,int from,int size,int projectId);
	
	public Advertisement searchAdverByUuid(String uuid);
	
	public Status updateAdver(Advertisement adver);
	
	public Status addAdver(Advertisement adver);
	
	public Status updateAdverToStatus(Advertisement adver);
	
	public List<AdvertisementPosition> searchAllPostion();
	
	

}
