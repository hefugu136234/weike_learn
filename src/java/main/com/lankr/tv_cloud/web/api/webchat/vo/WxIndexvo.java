package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.AssetFacade;
import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.facade.WxSubjectFacade;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.utils.Tools;

public class WxIndexvo {

	private List<WxBannerItemVo> bannerList;

	private List<WxActivityItem> activityList;

	private List<WxMenuItem> menuList;

	private List<WxResourceItem> items;

	public List<WxBannerItemVo> getBannerList() {
		return bannerList;
	}

	public void setBannerList(List<WxBannerItemVo> bannerList) {
		this.bannerList = bannerList;
	}

	public List<WxActivityItem> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<WxActivityItem> activityList) {
		this.activityList = activityList;
	}

	public List<WxMenuItem> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<WxMenuItem> menuList) {
		this.menuList = menuList;
	}

	public List<WxResourceItem> getItems() {
		return items;
	}

	public void setItems(List<WxResourceItem> items) {
		this.items = items;
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

	public void buildActivity(List<Activity> activities) {
		if (Tools.isEmpty(activities)) {
			return;
		}
		this.activityList = new ArrayList<WxActivityItem>();
		for (Activity activity : activities) {
			WxActivityItem vo = new WxActivityItem();
			vo.buildIndexList(activity);
			this.activityList.add(vo);
		}
	}

	public void buildResource(List<Resource> resources) {
		if (Tools.isEmpty(resources)) {
			return;
		}
		this.items = new ArrayList<WxResourceItem>();
		for (Resource resource : resources) {
			WxResourceItem vo = new WxResourceItem();
			vo.buildBaseListItem(resource);
			this.items.add(vo);
		}
	}

	public void buildMenu(List<WxSubject> list, AssetFacade assetFacade,
			WxSubjectFacade wxSubjectFacade, ResourceFacade cacheResourceFacade) {
		if (Tools.isEmpty(list)) {
			return;
		}
		this.menuList = new ArrayList<WxMenuItem>();
		for (WxSubject wxSubject : list) {
			WxMenuItem vo = new WxMenuItem();
			vo.buildBaseData(wxSubject);
			vo.buildAllResCount(assetFacade, wxSubjectFacade, cacheResourceFacade, wxSubject);
			this.menuList.add(vo);
		}
	}




}
