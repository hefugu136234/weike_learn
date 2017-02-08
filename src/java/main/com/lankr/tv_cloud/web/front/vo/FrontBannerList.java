package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.api.webchat.vo.WxBannerItemVo;

public class FrontBannerList extends BaseAPIModel{
	
	private List<WxBannerItemVo> bannerList;

	public List<WxBannerItemVo> getBannerList() {
		return bannerList;
	}

	public void setBannerList(List<WxBannerItemVo> bannerList) {
		this.bannerList = bannerList;
	}
	
	public void buildBanner(List<Banner> banners) {
		this.setStatus(Status.SUCCESS);
		if (Tools.isEmpty(banners)) {
			return;
		}
		this.bannerList = new ArrayList<WxBannerItemVo>();
		for (Banner banner : banners) {
			WxBannerItemVo vo = new WxBannerItemVo();
			vo.buildData(banner);
			this.bannerList.add(vo);
		}
	}

}
