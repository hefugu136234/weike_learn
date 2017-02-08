package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.Banner;

public interface BannerMapper {
	
	public int saveBanner(Banner banner);
	
	public int updateBanner(Banner banner);
	
	public int updateBannerState(Banner banner);
	
	public Banner selectBannerByUuid(String uuid);
	
	public int deleteBanner(Banner banner);
	
	public List<Banner> selectBannerList(SubParams params);

	/*
	public List<Banner> selectCallbackBannerList(SubParams subParams);*/
	
	public List<Banner> getWxBanner(int type,int position);
}
