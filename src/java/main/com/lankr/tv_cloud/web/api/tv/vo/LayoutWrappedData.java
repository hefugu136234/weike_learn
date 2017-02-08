package com.lankr.tv_cloud.web.api.tv.vo;

import java.util.List;

import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class LayoutWrappedData extends BaseAPIModel {

	private List<LayoutItemV2> data;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<LayoutItemV2> getData() {
		return data;
	}

	public void setData(List<LayoutItemV2> data) {
		this.data = data;
	}

	private int label_left_margin = 240;// px

	// 设置字间距
	private int label_interval = 200;// px

	private String logoUrl;

	private String backgroudUrl;

	public boolean isHome;

	public boolean isHome() {
		return isHome;
	}

	public void setHome(boolean isHome) {
		this.isHome = isHome;
	}

}
