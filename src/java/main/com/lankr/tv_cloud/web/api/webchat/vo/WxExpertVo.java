package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.MyCollectionFacade;
import com.lankr.tv_cloud.facade.PraiseFacade;
import com.lankr.tv_cloud.model.ActivityExpert;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;

public class WxExpertVo {

	private String uuid;

	private String cover;

	private String name;

	private String professor;

	private String wxbg;

	private String joinProfessor;

	private String desc;

	private String qr;

	private List<WxResourceItem> items;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public String getWxbg() {
		return wxbg;
	}

	public void setWxbg(String wxbg) {
		this.wxbg = wxbg;
	}

	public String getJoinProfessor() {
		return joinProfessor;
	}

	public void setJoinProfessor(String joinProfessor) {
		this.joinProfessor = joinProfessor;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	public List<WxResourceItem> getItems() {
		return items;
	}

	public void setItems(List<WxResourceItem> items) {
		this.items = items;
	}

	public void buildData(ActivityExpert expert) {
		this.setUuid(expert.getUuid());
		this.setName(OptionalUtils.traceValue(expert, "speaker.name"));
		String avatar=OptionalUtils.traceValue(expert, "speaker.avatar");
		avatar=WxUtil.getDefaultAvatar(avatar);
		this.setCover(avatar);
		String position=OptionalUtils.traceValue(expert, "speaker.position");
		position=getNamePop(position);
		this.setProfessor(position);
	}

	

	/**
	 * 2016-06-29 以后数据分离优化
	 * @param expert
	 * @param list
	 * @param media
	 * @param myCollectionFacade
	 * @param praiseFacade
	 */
	public void buildDetail(ActivityExpert expert, List<Resource> list,
			MediaCentral media, MyCollectionFacade myCollectionFacade,
			PraiseFacade praiseFacade) {
		this.setUuid(expert.getUuid());
		this.setName(OptionalUtils.traceValue(expert, "speaker.name"));
		this.setWxbg(OptionalUtils.traceValue(media, "url"));
		String avatar = OptionalUtils.traceValue(expert, "speaker.avatar");
		avatar = WxUtil.getDefaultAvatar(avatar);
		this.setCover(avatar);
		String hospitalName = OptionalUtils.traceValue(expert,
				"speaker.hospital.name");
		String department = OptionalUtils.traceValue(expert,
				"speaker.department.name");
		String professor = OptionalUtils.traceValue(expert, "speaker.position");
		String joinProfessor = hospitalName + department + professor;
		this.setJoinProfessor(joinProfessor);
		this.setDesc(OptionalUtils.traceValue(expert, "speaker.resume"));
		if (list != null && !list.isEmpty()) {
			this.items = new ArrayList<WxResourceItem>();
			for (Resource resource : list) {
				WxResourceItem item = new WxResourceItem();
				item.buildBaseListItem(resource);
				int collectionCount = myCollectionFacade.selectCountByReId(resource
						.getId());
				item.setCollectCount(collectionCount);
				int praiseCount = praiseFacade.selectCountByReId(resource.getId());
				item.setPraiseCount(praiseCount);
				this.items.add(item);
			}
		}
	}
	
	public String getNamePop(String name) {
		if (name == null || name.isEmpty()) {
			name = "无";
		}
		return name;
	}

}
