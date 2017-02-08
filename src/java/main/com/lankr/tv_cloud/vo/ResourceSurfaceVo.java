package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class ResourceSurfaceVo {
	private String name;
	private int viewCount;
	private String type;
	private String mark;
	private int state;
	//封装不同类别的uuid
	private UuidVo uuids = new UuidVo();
	public UuidVo getUuids() {
		return uuids;
	}
	public void setUuids(UuidVo uuids) {
		this.uuids = uuids;
	}

	private SpeakerVo speakerVo = new SpeakerVo();
	public SpeakerVo getSpeakerVo() {
		return speakerVo;
	}
	public void setSpeakerVo(SpeakerVo speakerVo) {
		this.speakerVo = speakerVo;
	}

	private String dates;
	private String createDate;

	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	private boolean relatedOups;//关联活动作品标志
	
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
	public boolean isRelatedOups() {
		return relatedOups;
	}
	public void setRelatedOups(boolean relatedOups) {
		this.relatedOups = relatedOups;
	}
	
	public void build(Resource resource) {
		if(null == resource)
			return;
		this.setName(resource.getName());
		this.setViewCount(resource.getViewCount());
		this.speakerVo.build(resource.getSpeaker());
		this.setType(resource.getType().name());
		this.setMark(resource.getMark());
		this.setState(resource.getStatus());
		this.uuids.build(resource);
		this.relatedOups = !Tools.isBlank(resource.getCode());
		try {
			this.setDates(Tools.df1.format(resource.getModifyDate()));
		} catch (Exception e) {
			//e.printStackTrace();
			this.setDates("暂无");
		}
		try {
			this.setCreateDate(Tools.df1.format(resource.getCreateDate()));
		} catch (Exception e) {
			//e.printStackTrace();
			this.setDates("暂无");
		}
	}
}
