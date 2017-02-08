package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lankr.orm.mybatis.mapper.ShareGiftMpper;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.vo.snapshot.ResourceSimpleItem;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;

public class WxResourceList extends BaseAPIModel {
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

	private List<WxResourceItem> items;

	public List<WxResourceItem> getItems() {
		return items;
	}

	public void setItems(List<WxResourceItem> items) {
		this.items = items;
	}

	public void build(List<Resource> list) {
		this.setStatus(Status.SUCCESS);
		if (Tools.isEmpty(list)) {
			return;
		}
		this.setHits(list.size());
		this.items = new ArrayList<WxResourceItem>();
		for (Resource resource : list) {
			WxResourceItem vo = new WxResourceItem();
			vo.buildBaseListItem(resource);
			this.items.add(vo);
		}
	}
	
	public void buildSearch(List<WxSearchResourceItem> list){
		this.setStatus(Status.SUCCESS);
		if (Tools.isEmpty(list)) {
			return;
		}
		this.setHits(list.size());
		this.items = new ArrayList<WxResourceItem>();
		for (WxSearchResourceItem wxSearchResourceItem : list) {
			WxResourceItem vo = buildSimpleItem(wxSearchResourceItem);
			this.items.add(vo);
		}
	}
	
	public WxResourceItem buildSimpleItem(WxSearchResourceItem resource){
		WxResourceItem vo = new WxResourceItem();
		vo.setUuid(resource.getUuid());
		String name = OptionalUtils.traceValue(resource, "name");
		vo.setName(name);
		vo.setViewCount(OptionalUtils.traceInt(resource, "viewCount"));

		vo.setType(OptionalUtils.traceValue(resource, "type"));

		String cover = OptionalUtils.traceValue(resource, "cover");
		cover = WxUtil.getResourceCover(cover);
		vo.setCover(cover);

		String code = OptionalUtils.traceValue(resource, "code");
		vo.setCode(vo.getNamePop(code));

		String categoryName = OptionalUtils.traceValue(resource,
				"category.name");
		vo.setCatgoryName(vo.getNamePop(categoryName));

		String speakerName = OptionalUtils.traceValue(resource, "speaker.name");
		vo.setSpeakerName(vo.getNamePop(speakerName));

		String hospitalName = OptionalUtils.traceValue(resource,
				"speaker.hospital.name");

		vo.setHospitalName(vo.getNamePop(hospitalName));
		Date date=new Date(resource.getCreateTime());
		vo.setDateTime(Tools.formatYMDHMSDate(date));
		vo.setDesc(OptionalUtils.traceValue(resource, "mark"));
		return vo;
	}
	
	public void buildDownOups(List<ActivityApplication> list){
		this.setStatus(Status.SUCCESS);
		if (Tools.isEmpty(list)) {
			return;
		}
		this.items = new ArrayList<WxResourceItem>();
		for (ActivityApplication activityApplication : list) {
			WxResourceItem vo = new WxResourceItem();
			Resource resource=activityApplication.getResource();
			if(resource==null){
				continue;
			}
			vo.buildBaseListItem(resource);
			vo.setUuid(activityApplication.getUuid());
			this.items.add(vo);
		}
	}
	
	public void buildNotDownOups(List<ActivityApplication> list){
		this.setStatus(Status.SUCCESS);
		if (Tools.isEmpty(list)) {
			return;
		}
		this.items = new ArrayList<WxResourceItem>();
		for (ActivityApplication activityApplication : list) {
			WxResourceItem vo = new WxResourceItem();
			vo.setUuid(activityApplication.getUuid());
			String code = OptionalUtils.traceValue(activityApplication, "code");
			code=vo.getNamePop(code);
			vo.setCode(code);
			String categoryName = OptionalUtils.traceValue(activityApplication,
					"category.name");
			categoryName=vo.getNamePop(categoryName);
			vo.setCatgoryName(categoryName);
			vo.setName(OptionalUtils.traceValue(activityApplication, "name"));
			vo.setDateTime(Tools.formatYMDHMSDate(activityApplication.getCreateDate()));
			this.items.add(vo);
		}
	}
	
	public void buildResNews(List<Resource> list,ShareGiftMpper shareGiftMpper){
		this.setStatus(Status.SUCCESS);
		if (Tools.isEmpty(list)) {
			return;
		}
		this.items = new ArrayList<WxResourceItem>();
		for (Resource resource : list) {
			WxResourceItem vo = new WxResourceItem();
			vo.buildBaseListItem(resource);
			String categroyName=OptionalUtils.traceValue(resource, "category.name");
			if(categroyName.equals("分享有礼")){
				int count = shareGiftMpper.getShareGiftCount(resource.getId());
				vo.setShareCount(count);
			}
			this.items.add(vo);
		}
	}
}
