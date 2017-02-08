package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.MediaCentralFacade;
import com.lankr.tv_cloud.facade.NormalCollectScheduleFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollectSchedule;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.front.vo.FrontCoursePackageItem;

public class WxCoursePackageListVo extends BaseAPIModel {

	private List<WxBannerItemVo> bannerList;

	private List<WxCoursePackageItem> items;
	
	public int itemTotalSize;

	public List<WxBannerItemVo> getBannerList() {
		return bannerList;
	}

	public void setBannerList(List<WxBannerItemVo> bannerList) {
		this.bannerList = bannerList;
	}

	public List<WxCoursePackageItem> getItems() {
		return items;
	}

	public void setItems(List<WxCoursePackageItem> items) {
		this.items = items;
	}
	
	

	public int getItemTotalSize() {
		return itemTotalSize;
	}

	public void setItemTotalSize(int itemTotalSize) {
		this.itemTotalSize = itemTotalSize;
	}

	public void buildBanner(List<Banner> banners) {
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

	public void buildWxItems(List<NormalCollect> list,
			MediaCentralFacade mediaCentralFacade,
			NormalCollectScheduleFacade normalCollectScheduleFacade, User user) {
		this.setStatus(Status.SUCCESS);
		if (Tools.isEmpty(list))
			return;
		this.items = new ArrayList<WxCoursePackageItem>();
		// list.forEach(normalCollect->{
		// WxCoursePackageItem item=new WxCoursePackageItem();
		// item.buildListData(normalCollect, mediaCentralFacade);
		// this.items.add(item);
		// });
		for (NormalCollect normalCollect : list) {
			WxCoursePackageItem item = new WxCoursePackageItem();
			item.buildListData(normalCollect, mediaCentralFacade);
			NormalCollectSchedule normalCollectSchedule = normalCollectScheduleFacade
					.selectCourseScheduleByUser(normalCollect, user);
			int studyStatus=OptionalUtils.traceInt(normalCollectSchedule, "studyStatus");
			item.setStudyStatus(studyStatus);
			if(studyStatus>NormalCollectSchedule.STUDY_INIT){
				item.setLearnSchedule(OptionalUtils.traceInt(normalCollectSchedule, "learnSchedule"));
			}
			this.items.add(item);
		}
	}
	
	public void buildFrontData(Pagination<NormalCollect> pagination,
			MediaCentralFacade mediaCentralFacade,
			NormalCollectScheduleFacade normalCollectScheduleFacade, User user) {
		this.setStatus(Status.SUCCESS);
		this.setItemTotalSize(pagination.getTotal());
		List<NormalCollect> list = pagination.getResults();
		if (Tools.isEmpty(list)) {
			return;
		}
		this.items = new ArrayList<WxCoursePackageItem>();
		for (NormalCollect normalCollect : list) {
			WxCoursePackageItem item = new WxCoursePackageItem();
			item.buildListData(normalCollect, mediaCentralFacade);
			NormalCollectSchedule normalCollectSchedule = normalCollectScheduleFacade
					.selectCourseScheduleByUser(normalCollect, user);
			int studyStatus=OptionalUtils.traceInt(normalCollectSchedule, "studyStatus");
			item.setStudyStatus(studyStatus);
			if(studyStatus>NormalCollectSchedule.STUDY_INIT){
				item.setLearnSchedule(OptionalUtils.traceInt(normalCollectSchedule, "learnSchedule"));
			}
			this.items.add(item);
		}
	}
}
