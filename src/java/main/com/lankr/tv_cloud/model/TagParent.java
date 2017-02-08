package com.lankr.tv_cloud.model;

import java.util.ArrayList;
import java.util.List;

public class TagParent extends BaseModel{

	private static final long serialVersionUID = 1L; 
	
	private String name;
	private String pingyin;
	private List<TagChild> tagChilds = new ArrayList<TagChild>();

	
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
	public List<TagChild> getTagChilds() {
		return tagChilds;
	}
	public void setTagChilds(List<TagChild> tagChilds) {
		this.tagChilds = tagChilds;
	}
}
