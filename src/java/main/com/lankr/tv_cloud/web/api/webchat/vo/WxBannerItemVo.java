package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class WxBannerItemVo {
	
	private String cover;
	
	private String url;

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void buildData(Banner banner){
		this.setCover(OptionalUtils.traceValue(banner, "imageUrl"));
		this.setUrl(OptionalUtils.traceValue(banner, "refUrl"));
	}
	
	

}
