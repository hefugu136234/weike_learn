package com.lankr.tv_cloud.model;

@Deprecated
public class Media extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7447972273251776421L;

	private String taskId;

	private String type;

	private int clinicId;

	private String description;

	private String originHost;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getClinicId() {
		return clinicId;
	}

	public void setClinicId(int clinicId) {
		this.clinicId = clinicId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOriginHost() {
		return originHost;
	}

	public void setOriginHost(String originHost) {
		this.originHost = originHost;
	}


}
