package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lankr.tv_cloud.facade.NormalCollectScheduleFacade;
import com.lankr.tv_cloud.facade.PageRemainFacade;
import com.lankr.tv_cloud.facade.PraiseFacade;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollectQuestionnaire;
import com.lankr.tv_cloud.model.NormalCollectSchedule;
import com.lankr.tv_cloud.model.PageRemain;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceGroup;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class WxChapterItem {

	private String uuid;

	private String name;

	private int passNum;

	private List<WxChapterResource> items;

	private WxExamineItem examineItem;// 考试

	private int logined;// 0=未登录 1=登录

	private int disableClick;// 0=不能点击 1=可以点击

	public WxExamineItem getExamineItem() {
		return examineItem;
	}

	public void setExamineItem(WxExamineItem examineItem) {
		this.examineItem = examineItem;
	}

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

	public List<WxChapterResource> getItems() {
		return items;
	}

	public void setItems(List<WxChapterResource> items) {
		this.items = items;
	}

	public int getPassNum() {
		return passNum;
	}

	public void setPassNum(int passNum) {
		this.passNum = passNum;
	}

	public int getLogined() {
		return logined;
	}

	public void setLogined(int logined) {
		this.logined = logined;
	}

	public int getDisableClick() {
		return disableClick;
	}

	public void setDisableClick(int disableClick) {
		this.disableClick = disableClick;
	}

	public void buildData(NormalCollect normalCollect) {
		this.setUuid(normalCollect.getUuid());
		this.setName(OptionalUtils.traceValue(normalCollect, "name"));
	}
	
	public void loginData(User user){
		if(user==null){
			this.setLogined(0);
		}else{
			this.setLogined(1);
		}
	}

	public void buildItems(List<ResourceGroup> list,
			PageRemainFacade pageRemainFacade, PraiseFacade praiseFacade,
			User user) {
		if (Tools.isEmpty(list))
			return;
		this.items = new ArrayList<WxChapterResource>();
		for (ResourceGroup resourceGroup : list) {
			WxChapterResource item = new WxChapterResource();
			item.buildBaseData(resourceGroup);
			Resource resource = resourceGroup.getResource();
			if (resource == null) {
				continue;
			}
			int viewTime = 0;
			if (user != null) {
				// item.setLogined(true);
				viewTime = pageRemainFacade
						.resViewTimeByUser(PageRemain.RESOURCE_DETAIL_TYPE,
								resource.getId(), user);
			}
			item.buildResource(resource, viewTime);
			int praiseCount = praiseFacade.selectCountByReId(resource.getId());
			item.setPraiseCount(praiseCount);
			this.items.add(item);
			if (item.isPass()) {
				this.passNum++;
			}
		}

	}

	public void buildItems(List<ResourceGroup> list,
			Map<Integer, NormalCollectSchedule> map, PraiseFacade praiseFacade) {
		if (Tools.isEmpty(list))
			return;
		this.items = new ArrayList<WxChapterResource>();
		for (ResourceGroup resourceGroup : list) {
			WxChapterResource item = new WxChapterResource();
			item.buildBaseData(resourceGroup);
			Resource resource = resourceGroup.getResource();
			if (resource == null) {
				continue;
			}
			item.buildResource(resource);
			int praiseCount = praiseFacade.selectCountByReId(resource.getId());
			item.setPraiseCount(praiseCount);
			item.setPass(calculationResourceFinish(map, resource));
			this.items.add(item);
			if (item.isPass()) {
				this.passNum++;
			}
		}
	}

	public boolean calculationResourceFinish(
			Map<Integer, NormalCollectSchedule> map, Resource resource) {
		boolean flag = false;
		if (map != null) {
			NormalCollectSchedule normalCollectSchedule = map.get(resource
					.getId());
			if (normalCollectSchedule != null) {
				int studyStatus = normalCollectSchedule.getStudyStatus();
				if (studyStatus == NormalCollectSchedule.STUDY_FINISH) {
					flag = true;
				}
			}
		}
		return flag;
	}



}
