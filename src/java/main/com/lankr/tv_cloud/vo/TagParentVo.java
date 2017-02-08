package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.model.TagParent;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class TagParentVo {
	
	private String name;
	private String createDate;
	private String modifyDate;
	private String mark;
	private String uuid_1;
	private String uuid_2;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getUuid_1() {
		return uuid_1;
	}
	public void setUuid_1(String uuid_1) {
		this.uuid_1 = uuid_1;
	}
	public String getUuid_2() {
		return uuid_2;
	}
	public void setUuid_2(String uuid_2) {
		this.uuid_2 = uuid_2;
	}
	
	
	public void buildData(TagParent tagParent) {
		if(null == tagParent)
			return ;
		
		this.setName(OptionalUtils.traceValue(tagParent,"name"));
		this.setMark(OptionalUtils.traceValue(tagParent,"mark"));
		this.setCreateDate(Tools.df1.format(tagParent.getCreateDate()));
		this.setModifyDate(Tools.df1.format(tagParent.getCreateDate()));
		this.setUuid_1(OptionalUtils.traceValue(tagParent,"uuid"));
		this.setUuid_2(OptionalUtils.traceValue(tagParent,"uuid"));
	}
}
