package com.lankr.tv_cloud.model;

import java.util.Date;

public class UserReference extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6493344254291165247L;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	private User user;

	private Project project;

	private Role role;

	private Date createDate;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	private Date validDate;

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public boolean isDateValid(){
		if(validDate == null) return false;
		return validDate.after(new Date());
	}
	
	public String isDateStr(){
		if(validDate == null) return "no_use";
		if(validDate.after(new Date())){
			return "in_use";
		}else{
			return "late_use";
		}
	}
}
