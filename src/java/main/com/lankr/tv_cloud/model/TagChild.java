package com.lankr.tv_cloud.model;

import java.util.List;

public class TagChild extends BaseModel{
	
	private static final long serialVersionUID = -3574917911193653367L;
	
	private String name;
	private String pingyin;
	private TagParent tagParent;
	private int parent_id;
	private List<Resource> resources;
	
	
	public List<Resource> getResources() {
		return resources;
	}
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPingyin() {
		return pingyin;
	}
	public void setPingyin(String pingyin) {
		this.pingyin = pingyin;
	}
	public TagParent getTagParent() {
		return tagParent;
	}
	public void setTagParent(TagParent tagParent) {
		this.tagParent = tagParent;
	}
}
