package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class BannerSurface extends DataTableModel<BannerVo> {
	public void buildData(List<Banner> list) {
		if (null == list || list.size() == 0)
			return;
		for (Banner banner : list) {
			if (null == list)
				return;
			BannerVo data = new BannerVo();
			data.buildData(banner);
			aaData.add(data);
		}
	}
}
