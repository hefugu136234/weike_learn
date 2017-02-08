package com.lankr.tv_cloud.model;

public class InvitcodeRecord extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7846222217697417397L;

	private String invitcode;

	// 0=已使用，1=可用
	private Integer status;
	// 0=申请审核  1=按钮生成
	private Integer source;

	// 使用者
	private User user;

	private ApplicableRecords applicableRecords;

	public String getInvitcode() {
		return invitcode;
	}

	public void setInvitcode(String invitcode) {
		this.invitcode = invitcode;
	}

	public int getStatus() {
		return status == null ? 0 : status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ApplicableRecords getApplicableRecords() {
		return applicableRecords;
	}

	public void setApplicableRecords(ApplicableRecords applicableRecords) {
		this.applicableRecords = applicableRecords;
	}

	public boolean isValid() {
		return isActive() && status == 1;
	}

}
