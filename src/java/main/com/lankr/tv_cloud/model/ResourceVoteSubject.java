package com.lankr.tv_cloud.model;

import java.util.List;

public class ResourceVoteSubject extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public List<ResourceVoteOption> getOptions() {
		return options;
	}

	public void setOptions(List<ResourceVoteOption> options) {
		this.options = options;
	}

	private String title;
	private int index;
	private int status;
	private Resource resource;
	private int type;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<ResourceVoteOption> options;
	
	private int optionsCountSum;

	public int getOptionsCountSum() {
		int count = 0;
		for(ResourceVoteOption option: options){
			count += option.getCount();
		}
		return count;
	}

	public void setOptionsCountSum(int optionsCountSum) {
		this.optionsCountSum = optionsCountSum;
	}

}
