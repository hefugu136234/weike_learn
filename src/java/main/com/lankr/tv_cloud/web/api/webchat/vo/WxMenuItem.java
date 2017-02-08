package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lankr.tv_cloud.facade.ActivityFacade;
import com.lankr.tv_cloud.facade.AssetFacade;
import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.facade.WxSubjectFacade;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.model.WxSubject.WxSubjectProperty;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;
import com.lankr.tv_cloud.web.front.vo.FrontActivityItem;

public class WxMenuItem {

	private String uuid;

	private String name;

	private int rootType;

	private String url;

	private int resCount;

	private String cover;

	private boolean hasSubItems;

	private List<WxMenuItem> subItems;// 子学科

	private List<WxActivityItem> activityList;// 所属分类的活动

	private List<WxMenuLevel> sublevel;// 带层级的子目录

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

	public int getRootType() {
		return rootType;
	}

	public void setRootType(int rootType) {
		this.rootType = rootType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getResCount() {
		return resCount;
	}

	public void setResCount(int resCount) {
		this.resCount = resCount;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public List<WxMenuItem> getSubItems() {
		return subItems;
	}

	public void setSubItems(List<WxMenuItem> subItems) {
		this.subItems = subItems;
	}

	public List<WxActivityItem> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<WxActivityItem> activityList) {
		this.activityList = activityList;
	}

	public boolean isHasSubItems() {
		return hasSubItems;
	}

	public void setHasSubItems(boolean hasSubItems) {
		this.hasSubItems = hasSubItems;
	}

	public List<WxMenuLevel> getSublevel() {
		return sublevel;
	}

	public void setSublevel(List<WxMenuLevel> sublevel) {
		this.sublevel = sublevel;
	}

	public void buildbSimpleData(WxSubject wxSubject) {
		this.setUuid(wxSubject.getUuid());
		this.setName(OptionalUtils.traceValue(wxSubject, "name"));
	}

	public void buildBaseData(WxSubject wxSubject) {
		buildbSimpleData(wxSubject);

		String typeProperty = OptionalUtils.traceValue(wxSubject,
				"typeProperty");
		WxSubjectProperty wxSubjectProperty = WxSubject
				.typeJsonForObject(typeProperty);
		int type = OptionalUtils.traceInt(wxSubjectProperty, "type");
		String cover = OptionalUtils.traceValue(wxSubjectProperty, "cover");
		this.setCover(cover);
		String url = "";
		if (type == WxSubjectProperty.SUB_PAGE_TYPE) {
			// 子页面
			url = BaseWechatController.WX_PRIOR + "/index/second/category/"
					+ wxSubject.getUuid();
		} else if (type == WxSubjectProperty.RES_PAGE_TYPE) {
			// 资源页面
			url = BaseWechatController.WX_PRIOR + "/home/list/page/"
					+ wxSubject.getUuid();
		}
		this.setUrl(url);
	}

	/**
	 * 一个大学科下底层子目录(第3层)
	 */
	// 一个大分类下的子分类
	public List<WxSubject> getLastLevelSubList(WxSubjectFacade wxSubjectFacade,
			WxSubject wxSubject) {
		List<WxSubject> list = new ArrayList<WxSubject>();

		// 无层级子分类
		List<WxSubject> noList = wxSubjectFacade.searchWxSubjectChildrenByWx(
				wxSubject.getId(), WxSubject.TYPE_CATEGORY, WxSubject.NO_LEVEL);
		if (!Tools.isEmpty(noList)) {
			list.addAll(noList);
		}
		// 有层级子分类
		List<WxSubject> hasList = wxSubjectFacade.searchWxSubjectChildrenByWx(
				wxSubject.getId(), WxSubject.TYPE_CATEGORY, WxSubject.HAS_LEVEL);
		if(!Tools.isEmpty(hasList)){
			for (WxSubject wxSubject2 : hasList) {
				List<WxSubject> chaList=wxSubjectFacade.searchWxSubjectChildrenByWx(
						wxSubject2.getId(), WxSubject.TYPE_CATEGORY, WxSubject.ALL_LEVEL);
				if(!Tools.isEmpty(chaList)){
					list.addAll(chaList);
				}
			}
		}
		return list;
	}

	public void buildAllResCount(AssetFacade assetFacade,
			WxSubjectFacade wxSubjectFacade,
			ResourceFacade cacheResourceFacade, WxSubject wxSubject) {
		// 子分类
		List<WxSubject> subjectChildrenList = getLastLevelSubList(wxSubjectFacade,wxSubject);

		List<Integer> searchList = new ArrayList<Integer>();

		for (WxSubject children : subjectChildrenList) {
			Integer id = categoryId(assetFacade, children.getReflectId());
			if (id != null) {
				searchList.add(id);
			}
		}
		int count = 0;
		if (searchList.size() > 0) {
			count = cacheResourceFacade.resourceCountByCategory(searchList);
		}
		this.setResCount(count);
	}

	public void buildSimpleResCount(ResourceFacade cacheResourceFacade,
			int reflectId) {
		int resourceCount = cacheResourceFacade
				.resourceCountByCategory(new ArrayList<Integer>(Arrays
						.asList(reflectId)));
		this.setResCount(resourceCount);
	}

	public Integer categoryId(AssetFacade assetFacade, int reflectId) {
		Category category = assetFacade.getCategoryById(reflectId);
		if (category != null) {
			return category.getId();
		}
		return null;
	}

	// 2级目录下的活动kv
	public void buildSecondActivity(List<WxSubject> activityChildrenList,
			ActivityFacade activityFacade) {
		if (Tools.isEmpty(activityChildrenList)) {
			return;
		}
		this.activityList = new ArrayList<WxActivityItem>();
		for (WxSubject activityWx : activityChildrenList) {
			Activity activity = activityFacade.getActivityById(activityWx
					.getReflectId());
			String typeProperty = OptionalUtils.traceValue(activityWx,
					"typeProperty");
			WxSubjectProperty wxSubjectProperty = WxSubject
					.typeJsonForObject(typeProperty);
			// int type = OptionalUtils.traceInt(wxSubjectProperty, "type");
			String kv = OptionalUtils.traceValue(wxSubjectProperty, "cover");
			WxActivityItem item = new WxActivityItem();
			item.buildSecondSubjectKv(activity, kv);
			this.activityList.add(item);
		}
	}

	// 2级目录无层级
	public void buildNoLevelSubject(List<WxSubject> subjectChildrenList,
			ResourceFacade cacheResourceFacade) {
		if (Tools.isEmpty(subjectChildrenList)) {
			return;
		}
		this.setHasSubItems(true);
		this.subItems = new ArrayList<WxMenuItem>();
		for (WxSubject subject : subjectChildrenList) {
			WxMenuItem item = new WxMenuItem();
			item.buildBaseData(subject);
			item.buildSimpleResCount(cacheResourceFacade,
					OptionalUtils.traceInt(subject, "reflectId"));
			this.subItems.add(item);
		}
	}

	// 2级目录有层级数据
	public void buildHasLevel(List<WxSubject> subjectChildrenList,
			ResourceFacade cacheResourceFacade, WxSubjectFacade wxSubjectFacade) {
		if (Tools.isEmpty(subjectChildrenList)) {
			return;
		}
		this.setHasSubItems(true);
		this.sublevel = new ArrayList<WxMenuLevel>();
		for (WxSubject wxSubject : subjectChildrenList) {
			WxMenuLevel level=new WxMenuLevel();
			level.build(wxSubject, wxSubjectFacade, cacheResourceFacade);
			this.sublevel.add(level);
		}

	}
}
