package com.lankr.tv_cloud.web.api.tv.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.model.TvLayout;

public class LayoutItemV2 {

	private String name;

	private String uuid;

	// widget默认缩放比例
	private float scale = 1.5f;

	// widget默认单位长度 px
	private int widget_base_dimensions = 10;

	// widget默认间距单位长度px
	private int widget_margins = 10;

	// 设置布局开始的位置
	private int layout_start = 240; // px

	private List<Widget> widgets;

	private static transient Gson gson = new Gson();

	public static LayoutItemV2 build(TvLayout layout, ResourceFacade facade) {
		if (layout == null)
			return null;
		LayoutItemV2 v2 = new LayoutItemV2();
		v2.name = layout.getName();
		v2.uuid = layout.getUuid();
		v2.widgets = gson.fromJson(layout.getWidgets(),
				new com.google.gson.reflect.TypeToken<ArrayList<Widget>>() {
				}.getType());
		if (v2.widgets != null) {
			for (int i = 0; i < v2.widgets.size(); i++) {
				v2.widgets.get(i).buildResouce(facade);
			}
		}
		return v2;
	}

	public static List<LayoutItemV2> build(List<TvLayout> layouts,
			ResourceFacade facade) {
		if (layouts != null) {
			List<LayoutItemV2> list = new ArrayList<LayoutItemV2>(
					layouts.size());
			for (TvLayout tvLayout : layouts) {
				list.add(build(tvLayout, facade));
			}
			return list;
		}
		return Collections.EMPTY_LIST;
	}
}
