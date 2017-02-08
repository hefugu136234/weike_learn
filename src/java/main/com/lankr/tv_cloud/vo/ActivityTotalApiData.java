package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lankr.tv_cloud.facade.APIFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.api.tv.ResourceItem;
/** 
 *  author Kalean.Xiang
 *  createDate: 2016年3月18日
 * 	modifyDate: 2016年3月18日
 * 	@since 1.0
 *  
 */
public class ActivityTotalApiData extends BaseAPIModel {

	private ActivityVo activity_info;

	public List<ResourceItem> resources;

	public List<ResourceItem> recommends;

	public List<ResourceItem> reports;

	public List<ResourceItem> ranks;
	
	public String qr;

	public String getQr() {
		return qr;
	}

	//设置二维码
	public void setQr(String qr) {
		this.qr = qr;
	}

	public ActivityTotalApiData buildFirst(APIFacade apiFacade,
			Activity activity) {
		if (activity == null || apiFacade == null) {
			setStatus(Status.FAILURE);
			return this;
		}
		setStatus(Status.SUCCESS);
		// activity base info
		build(activity);

		// activity recommends
		recommends = internal(apiFacade
				.fetchActivityRecommendResources(activity));

		// activity resources
		resources = internal(apiFacade.fetchActivityResources(activity,
				new Date(), 20));

		// activity reports
		reports = internal(apiFacade.fetchActivityReports(activity, new Date(),
				10));

		ranks = internal(apiFacade.fetchActivityRanking(activity));
		return this;
	}

	public ActivityTotalApiData build(Activity activity) {
		activity_info = new ActivityVo().build(activity);
		return this;
	}

	public List<ResourceItem> internal(List<Resource> reses) {
		if (reses == null)
			return null;
		List<ResourceItem> items = new ArrayList<ResourceItem>();
		for (Resource res : reses) {
			items.add(new ResourceItem().build(res));
		}
		return items;
	}

}
