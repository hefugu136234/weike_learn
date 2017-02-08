package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class TagChildVo {
	
	private String name;
	private String createDate;
	private String modifyDate;
	private String mark;
	private String uuid;
	
	
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
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
	public void buildData(TagChild tagChild) {
		if(null == tagChild)
			return ;
		
		this.setName(OptionalUtils.traceValue(tagChild,"name"));
		this.setMark(OptionalUtils.traceValue(tagChild,"mark"));
		this.setCreateDate(Tools.df1.format(tagChild.getCreateDate()));
		this.setModifyDate(Tools.df1.format(tagChild.getCreateDate()));
		this.setUuid(OptionalUtils.traceValue(tagChild,"uuid"));
	}
}
