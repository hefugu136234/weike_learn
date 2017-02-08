package com.lankr.tv_cloud.web.api.tv;

import com.lankr.tv_cloud.model.TvLayout;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class LayoutPageItem {

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWidgets() {
		return widgets;
	}

	public void setWidgets(String widgets) {
		this.widgets = widgets;
	}

	private String uuid;

	private String name;

	private String widgets;

	public LayoutPageItem build(TvLayout layout) {
		if (layout != null)
			uuid = layout.getUuid();
		name = OptionalUtils.traceValue(layout, "name");
		widgets = OptionalUtils.traceValue(layout, "widgets");
		return this;
	}
}
