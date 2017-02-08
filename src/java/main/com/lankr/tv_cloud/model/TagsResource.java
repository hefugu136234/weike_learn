package com.lankr.tv_cloud.model;

import java.util.List;

public class TagsResource extends BaseModel{

	private static final long serialVersionUID = 2719572143991822009L;
	
	private List<TagChild> tags;
	private List<Resource> resources;
	private int status;
	private int tagsId;
	private int resourceId;
	
	
	public List<TagChild> getTags() {
		return tags;
	}
	public void setTags(List<TagChild> tags) {
		this.tags = tags;
	}
	public List<Resource> getResources() {
		return resources;
	}
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTagsId() {
		return tagsId;
	}
	public void setTagsId(int tagsId) {
		this.tagsId = tagsId;
	}
	public int getResourceId() {
		return resourceId;
	}
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
}
