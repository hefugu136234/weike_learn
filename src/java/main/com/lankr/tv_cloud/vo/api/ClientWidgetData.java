package com.lankr.tv_cloud.vo.api;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Widget;
import com.lankr.tv_cloud.vo.BannerWidget;

public class ClientWidgetData extends BaseAPIModel {

	private List<BannerWidget> widgets;

	public List<BannerWidget> getWidgets() {
		return widgets;
	}

	public void setWidgets(List<BannerWidget> widgets) {
		this.widgets = widgets;
	}

	public void format(List<Widget> original_data) throws Exception {
		setStatus(Status.SUCCESS);
		if (original_data == null || original_data.isEmpty())
			return;
		widgets = new ArrayList<BannerWidget>();
		for (int i = 0; i < original_data.size(); i++) {
			Widget w = original_data.get(i);
			widgets.add(new BannerWidget(null, w.getTv_cover(), w.getCategory()
					.getUuid(), w.getCategory().getName(), w.getX(), w.getY(),
					w.getOffset_x(), w.getOffset_y(),w.getMark()));
		}
	}
}
