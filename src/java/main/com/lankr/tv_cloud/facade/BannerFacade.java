package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Banner;

public interface BannerFacade {

	public Status saveBanner(Banner banner) throws Exception;

	public Status updateBanner(Banner banner);

	public Status updateBannerState(Banner banner);

	public Banner selectBannerByUuid(String uuid);
	
	public Status deleteBanner(Banner banner);

	public Pagination<Banner> selectBannerList(String searchValue,
			int startPage, int pageSize, int type);

	/*
	public Pagination<Banner> selectCallbackBannerList(String searchValue,
			int startPage, int pageSize);*/
	
	public List<Banner> getWxBanner(int type,int position);
}
