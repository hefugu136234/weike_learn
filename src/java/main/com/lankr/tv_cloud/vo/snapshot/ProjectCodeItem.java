package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.ProjectCode;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class ProjectCodeItem extends AbstractItem<ProjectCode> {

	private String createDate;

	private String userNickName;

	private String activteTime;
	
	private String projectCode;

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getActivteTime() {
		return activteTime;
	}

	public void setActivteTime(String activteTime) {
		this.activteTime = activteTime;
	}
	
	
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	@Override
	public boolean createMarker() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean modifyMarker() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public ProjectCodeItem(ProjectCode code) {
		// TODO Auto-generated constructor stub
		super(code);
	}

	@Override
	protected void buildextra() throws Exception {
		// TODO Auto-generated method stub
		this.createDate=Tools.formatYMDHMSDate(t.getCreateDate());
		this.userNickName=OptionalUtils.traceValue(t, "user.nickname");
		this.projectCode=OptionalUtils.traceValue(t, "projectCode");
		if(t.getActiveTime()!=null){
			this.activteTime=Tools.formatYMDHMSDate(t.getActiveTime());
		}else{
			this.activteTime="";
		}

	}

}
