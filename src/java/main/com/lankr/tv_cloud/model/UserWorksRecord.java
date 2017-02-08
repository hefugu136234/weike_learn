package com.lankr.tv_cloud.model;

public class UserWorksRecord extends BaseModel{
	
	private Speaker speaker;
	private Resource resource;
	
	public Speaker getSpeaker() {
		return speaker;
	}
	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}
	
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
}
