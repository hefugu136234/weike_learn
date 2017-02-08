package com.lankr.tv_cloud.model;

import java.util.Date;

@SuppressWarnings("all")
public class ActivityExpert extends BaseModel{

	private static final long serialVersionUID = 1L;
	
	private Activity activity;
	private int status;
	private Speaker speaker;
	
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Speaker getSpeaker() {
		return speaker;
	}
	public void setSpeaker(Speaker speaker) {
		this.speaker = speaker;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
