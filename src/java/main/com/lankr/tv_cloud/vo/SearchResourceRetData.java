/** 
 *  @author Kalean.Xiang
 *  @createDate: 2016年4月7日
 * 	@modifyDate: 2016年4月7日
 *  
 */
package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.api.tv.ResourceItem;

/**
 * @author Kalean.Xiang
 *
 */
public class SearchResourceRetData extends BaseAPIModel {

	private String query;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	private int hits;
	
	

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	private List<ResourceItem> resources;
	
	

	public List<ResourceItem> getResources() {
		return resources;
	}

	public void setResources(List<ResourceItem> resources) {
		this.resources = resources;
	}

	public void build(List<Resource> ress) {
		setStatus(Status.SUCCESS);
		if (ress != null && !ress.isEmpty()) {
			hits = ress.size();
			resources = new ArrayList<ResourceItem>();
			for (Resource resource : ress) {
				resources.add(new ResourceItem().build(resource));
			}
		}
	}
}
