package com.lankr.tv_cloud.model;

public class ResourceVoteOption extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String option;
	public int index;
	private int count;
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ResourceVoteSubject subject;

	public ResourceVoteSubject getSubject() {
		return subject;
	}

	public void setSubject(ResourceVoteSubject subject) {
		this.subject = subject;
	}

}
