package com.lankr.tv_cloud.web.api.tv;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.TvLayout;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class ResourcesData extends BaseAPIModel {

	// 分类结构
	List<CategoryVO> categories;

	List<ResourceItem> items;

	CategoryVO category;

	List<LayoutPageItem> pages;

	public void buildCategory(Category trunk) {
		if (trunk == null)
			return;
		category = new CategoryVO(trunk.getUuid(), trunk.getName(), "");
	}

	public void buildCategories(List<Category> cs) {
		if (cs == null || cs.isEmpty())
			return;
		categories = new ArrayList<CategoryVO>(cs.size());
		for (Category c : cs) {
			Category parent = c.getParent();
			CategoryVO vo = new CategoryVO(c.getUuid(), c.getName(),
					parent == null ? "" : parent.getUuid());			
			vo.setCover(OptionalUtils.traceValue(c, "expand.tvTaskId"));
			categories.add(vo);
		}
	}

	public void buildLayoutPages(List<TvLayout> layouts) {
		if (layouts == null || layouts.isEmpty())
			return;
		pages = new ArrayList<LayoutPageItem>();
		for (TvLayout tvLayout : layouts) {
			pages.add(new LayoutPageItem().build(tvLayout));
		}
	}

	public void buildResource(List<Resource> resources) {
		if (resources == null || resources.isEmpty())
			return;
		items = new ArrayList<ResourceItem>(resources.size());
		for (Resource resource : resources) {
			ResourceItem item = new ResourceItem();
			item.build(resource);
			items.add(item);
		}
	}
}
