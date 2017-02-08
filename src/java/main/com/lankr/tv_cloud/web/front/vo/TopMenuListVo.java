package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.AssetFacade;
import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.WxSubjectFacade;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class TopMenuListVo extends BaseAPIModel {

	private String levelUuid;

	private String levelName;

	private List<TopMenuItem> itemList;
	
	private boolean active=false;

	public List<TopMenuItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<TopMenuItem> itemList) {
		this.itemList = itemList;
	}

	public String getLevelUuid() {
		return levelUuid;
	}

	public void setLevelUuid(String levelUuid) {
		this.levelUuid = levelUuid;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
	

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
		List<WxSubject> hasList = wxSubjectFacade
				.searchWxSubjectChildrenByWx(wxSubject.getId(),
						WxSubject.TYPE_CATEGORY, WxSubject.HAS_LEVEL);
		if (!Tools.isEmpty(hasList)) {
			for (WxSubject wxSubject2 : hasList) {
				List<WxSubject> chaList = wxSubjectFacade
						.searchWxSubjectChildrenByWx(wxSubject2.getId(),
								WxSubject.TYPE_CATEGORY, WxSubject.ALL_LEVEL);
				if (!Tools.isEmpty(chaList)) {
					list.addAll(chaList);
				}
			}
		}
		return list;
	}

	/**
	 * 一级根目录
	 * 
	 * @param list
	 * @param assetFacade
	 */
	public void buildCategoryData(List<WxSubject> list,
			AssetFacade assetFacade, WxSubjectFacade wxSubjectFacade,
			ResourceFacade cacheResourceFacade) {
		this.setStatus(Status.SUCCESS);
		if (list == null || list.isEmpty())
			return;
		itemList = new ArrayList<TopMenuItem>();
		for (WxSubject wxSubject : list) {
			// 显示
			TopMenuItem item = new TopMenuItem();
			item.buildData(wxSubject);
			// 子分类
			List<WxSubject> subjectChildrenList = getLastLevelSubList(
					wxSubjectFacade, wxSubject);
			List<Integer> searchList = new ArrayList<Integer>();
			// 不显示父分类资源
			// Integer id = categoryId(assetFacade, wxSubject.getReflectId());
			// if (id != null) {
			// searchList.add(id);
			// }
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
			item.setResCount(count);
			itemList.add(item);
		}
	}
	
	

	public Integer categoryId(AssetFacade assetFacade, int reflectId) {
		Category category = assetFacade.getCategoryById(reflectId);
		if (category != null) {
			return category.getId();
		}
		return null;
	}

	public void buildIndexContent(List<WxSubject> list,
			AssetFacade assetFacade, WxSubjectFacade wxSubjectFacade,
			ResourceFacade cacheResourceFacade) {
		itemList = new ArrayList<TopMenuItem>();
		if (list == null || list.isEmpty())
			return;
		for (WxSubject wxSubject : list) {
			// 显示
			TopMenuItem item = new TopMenuItem();
			item.buildData(wxSubject);
			// 子分类
			List<WxSubject> subjectChildrenList = getLastLevelSubList(
					wxSubjectFacade, wxSubject);
			List<Integer> searchList = new ArrayList<Integer>();
			// 不显示父分类资源
			// Integer id = categoryId(assetFacade, wxSubject.getReflectId());
			// if (id != null) {
			// searchList.add(id);
			// }
			for (WxSubject children : subjectChildrenList) {
				Integer id = categoryId(assetFacade, children.getReflectId());
				if (id != null) {
					searchList.add(id);
				}
			}
			int count = 0;
			List<Resource> resources = null;
			if (searchList.size() > 0) {
				count = cacheResourceFacade.resourceCountByCategory(searchList);
				resources = cacheResourceFacade.resourceLatestByCategory(
						searchList, 4);
			}
			item.buildResList(resources);
			item.setResCount(count);
			itemList.add(item);
		}
	}

}
