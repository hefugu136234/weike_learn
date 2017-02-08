package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.MediaCentralFacade;
import com.lankr.tv_cloud.facade.NormalCollectScheduleFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollectSchedule;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class FrontCoursePackageList extends BaseAPIModel {

	public int itemTotalSize;

	private List<FrontCoursePackageItem> items;

	public int getItemTotalSize() {
		return itemTotalSize;
	}

	public void setItemTotalSize(int itemTotalSize) {
		this.itemTotalSize = itemTotalSize;
	}

	public List<FrontCoursePackageItem> getItems() {
		return items;
	}

	public void setItems(List<FrontCoursePackageItem> items) {
		this.items = items;
	}

	public void buildData(Pagination<NormalCollect> pagination,
			MediaCentralFacade mediaCentralFacade,
			NormalCollectScheduleFacade normalCollectScheduleFacade, User user) {
		this.setStatus(Status.SUCCESS);
		this.setItemTotalSize(pagination.getTotal());
		List<NormalCollect> list = pagination.getResults();
		if (Tools.isEmpty(list)) {
			return;
		}
		this.items = new ArrayList<FrontCoursePackageItem>();
		for (NormalCollect normalCollect : list) {
			FrontCoursePackageItem item = new FrontCoursePackageItem();
			item.buildListData(normalCollect, mediaCentralFacade);
			NormalCollectSchedule normalCollectSchedule = normalCollectScheduleFacade
					.selectCourseScheduleByUser(normalCollect, user);
			item.setLearnSchedule(OptionalUtils.traceInt(normalCollectSchedule, "learnSchedule"));
			this.items.add(item);
		}
	}

}
