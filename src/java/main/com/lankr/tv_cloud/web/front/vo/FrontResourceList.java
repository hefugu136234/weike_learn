package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.ActivityFacade;
import com.lankr.tv_cloud.facade.AssetFacade;
import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.WxSubjectFacade;
import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class FrontResourceList extends BaseAPIModel {

	private List<TopMenuItem> totalMenuList;

	private List<TopMenuItem> subMenuList;

	private List<TopMenuListVo> hasLevelList;

	private List<FrontActivityItem> activityList;

	private String selectName;

	private String selectUuid;

	private List<FrontResourceItem> subResList;

	private int subResCount;

	public List<TopMenuItem> getTotalMenuList() {
		return totalMenuList;
	}

	public void setTotalMenuList(List<TopMenuItem> totalMenuList) {
		this.totalMenuList = totalMenuList;
	}

	public List<TopMenuItem> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(List<TopMenuItem> subMenuList) {
		this.subMenuList = subMenuList;
	}

	public List<FrontActivityItem> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<FrontActivityItem> activityList) {
		this.activityList = activityList;
	}

	public String getSelectName() {
		return selectName;
	}

	public void setSelectName(String selectName) {
		this.selectName = selectName;
	}

	public List<FrontResourceItem> getSubResList() {
		return subResList;
	}

	public void setSubResList(List<FrontResourceItem> subResList) {
		this.subResList = subResList;
	}

	public int getSubResCount() {
		return subResCount;
	}

	public void setSubResCount(int subResCount) {
		this.subResCount = subResCount;
	}

	public String getSelectUuid() {
		return selectUuid;
	}

	public void setSelectUuid(String selectUuid) {
		this.selectUuid = selectUuid;
	}

	public List<TopMenuListVo> getHasLevelList() {
		return hasLevelList;
	}

	public void setHasLevelList(List<TopMenuListVo> hasLevelList) {
		this.hasLevelList = hasLevelList;
	}

	public FrontResourceList() {

	}

	public FrontResourceList(boolean flag) {
		if (flag) {
			this.totalMenuList = new ArrayList<TopMenuItem>();
			this.subMenuList = new ArrayList<TopMenuItem>();
			this.activityList = new ArrayList<FrontActivityItem>();
			this.subResList = new ArrayList<FrontResourceItem>();
		}
	}

	public void buildCatePageData(WxSubject wxSubject,
			WxSubjectFacade wxSubjectFacade,
			ResourceFacade cacheResourceFacade, AssetFacade assetFacade,
			ActivityFacade activityFacade) {
		int root = wxSubject.getIsRoot();

		int selectParentId = 0, selectSubId = 0;

		if (root == WxSubject.ROOT) {// (选中)根目录
			selectParentId = wxSubject.getId();
			// 子目录默认选中第一个
		} else {// 选中子目录
			WxSubject parent = wxSubject.getParent();
			while (parent != null) {
				selectParentId = parent.getId();
				parent = parent.getParent();
			}
			selectSubId = wxSubject.getId();
		}

		// 一级目录
		List<WxSubject> wxSubjects = wxSubjectFacade.searchWxSubjectByWx(
				WxSubject.ROOT, WxSubject.TYPE_CATEGORY);
		if (wxSubjects != null && !wxSubjects.isEmpty()) {
			for (WxSubject total : wxSubjects) {
				// 总菜单
				TopMenuItem item = new TopMenuItem();
				item.buildData(total);
				if (total.getId() == selectParentId) {
					item.setActive(true);
				}
				totalMenuList.add(item);
			}
		}

		Integer selectCategory = null;
		boolean selectFlag = false;
		// 无层级子分类
		List<WxSubject> noLevelChildrenList = wxSubjectFacade
				.searchWxSubjectChildrenByWx(selectParentId,
						WxSubject.TYPE_CATEGORY, WxSubject.NO_LEVEL);
		if (!Tools.isEmpty(noLevelChildrenList)) {
			for (int i = 0; i < noLevelChildrenList.size(); i++) {
				WxSubject sub = noLevelChildrenList.get(i);
				TopMenuItem item = new TopMenuItem();
				item.buildData(sub);
				Integer id = categoryId(assetFacade, sub.getReflectId());
				int count = 0;
				if (id != null) {
					count = cacheResourceFacade.resourceCountByCateId(id);
				}
				item.setResCount(count);
				if (selectSubId == sub.getId()) {
					selectFlag = true;
					selectCategory = id;
					item.setActive(true);
					this.setSelectName(OptionalUtils.traceValue(sub, "name"));
					this.setSelectUuid(sub.getUuid());
				}
				subMenuList.add(item);
			}
			// 默认选中一个子分类
			if (!selectFlag) {
				selectCategory = categoryId(assetFacade, noLevelChildrenList
						.get(0).getReflectId());
				subMenuList.get(0).setActive(true);
				this.setSelectName(OptionalUtils.traceValue(
						noLevelChildrenList.get(0), "name"));
				this.setSelectUuid(noLevelChildrenList.get(0).getUuid());
			}
		}

		// 有层级子分类
		List<WxSubject> hasLevelChildrenList = wxSubjectFacade
				.searchWxSubjectChildrenByWx(selectParentId,
						WxSubject.TYPE_CATEGORY, WxSubject.HAS_LEVEL);
		if (!Tools.isEmpty(hasLevelChildrenList)) {
			WxSubject firstLevel = null;// 默认选中的一个子分类
			this.hasLevelList = new ArrayList<TopMenuListVo>();
			for (int i = 0; i < hasLevelChildrenList.size(); i++) {
				TopMenuListVo topMenuListVo = new TopMenuListVo();
				WxSubject sub = hasLevelChildrenList.get(i);
				topMenuListVo.setLevelName(sub.getName());
				topMenuListVo.setLevelUuid(sub.getUuid());
				List<WxSubject> childList = wxSubjectFacade
						.searchWxSubjectChildrenByWx(sub.getId(),
								WxSubject.TYPE_CATEGORY, WxSubject.ALL_LEVEL);
				if (!Tools.isEmpty(hasLevelChildrenList)) {
					if (i == 0) {
						firstLevel = childList.get(0);
					}
					List<TopMenuItem> itemList = new ArrayList<TopMenuItem>();
					for (WxSubject wxSubject2 : childList) {
						TopMenuItem item = new TopMenuItem();
						item.buildData(wxSubject2);
						Integer id = categoryId(assetFacade,
								wxSubject2.getReflectId());
						int count = 0;
						if (id != null) {
							count = cacheResourceFacade
									.resourceCountByCateId(id);
						}
						item.setResCount(count);
						if (selectSubId == wxSubject2.getId()) {
							selectFlag = true;
							selectCategory = id;
							item.setActive(true);
							topMenuListVo.setActive(true);
							this.setSelectName(OptionalUtils.traceValue(
									wxSubject2, "name"));
							this.setSelectUuid(wxSubject2.getUuid());
						}
						itemList.add(item);
					}
					topMenuListVo.setItemList(itemList);
				}
				hasLevelList.add(topMenuListVo);
			}
			// 若没选中
			// 默认选中一个子分类
			if (!selectFlag && firstLevel != null) {
				selectCategory = categoryId(assetFacade,
						firstLevel.getReflectId());
				if (!Tools.isEmpty(hasLevelList.get(0).getItemList())) {
					hasLevelList.get(0).getItemList().get(0).setActive(true);
					hasLevelList.get(0).setActive(true);
				}
				this.setSelectName(OptionalUtils.traceValue(firstLevel, "name"));
				this.setSelectUuid(firstLevel.getUuid());
			}
		}

		// 选中的子目录下的10个资源
		if (selectCategory != null) {
			Pagination<Resource> pageData = cacheResourceFacade
					.resourceFrontPage(selectCategory, 0, 10);
			this.setSubResCount(pageData.getTotal());
			List<Resource> resourcesList = pageData.getResults();
			if (resourcesList != null && !resourcesList.isEmpty()) {
				for (Resource resource : resourcesList) {
					FrontResourceItem resourceItem = new FrontResourceItem();
					resourceItem.buildItemList(resource);
					subResList.add(resourceItem);
				}
			}
		}

		// if(this.getSubResCount()==0){
		// this.setSelectName("");
		// }

		// 选中的父目录下活动banner
		List<WxSubject> activityChildrenList = wxSubjectFacade
				.searchWxSubjectChildrenByWx(selectParentId,
						WxSubject.TYPE_ACTIVITY, WxSubject.ALL_LEVEL);
		if (activityChildrenList != null && !activityChildrenList.isEmpty()) {
			for (WxSubject activityWx : activityChildrenList) {
				Activity activity = activityFacade.getActivityById(activityWx
						.getReflectId());
				if (activity != null && activity.apiUseable()) {
					FrontActivityItem item = new FrontActivityItem();
					item.buildCategeoryBanner(activity);
					activityList.add(item);
				}
			}
		}
	}

	public Integer categoryId(AssetFacade assetFacade, int reflectId) {
		Category category = assetFacade.getCategoryById(reflectId);
		if (category != null) {
			return category.getId();
		}
		return null;
	}

	public void buildPageItem(Pagination<Resource> pageData) {
		this.setStatus(Status.SUCCESS);
		this.setSubResCount(pageData.getTotal());
		List<Resource> resourcesList = pageData.getResults();
		if (resourcesList != null && !resourcesList.isEmpty()) {
			subResList = new ArrayList<FrontResourceItem>();
			for (Resource resource : resourcesList) {
				FrontResourceItem resourceItem = new FrontResourceItem();
				resourceItem.buildItemList(resource);
				subResList.add(resourceItem);
			}
		}
	}

}
