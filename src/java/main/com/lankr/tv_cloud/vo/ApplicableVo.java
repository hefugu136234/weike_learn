package com.lankr.tv_cloud.vo;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.ApplicableRecords;
import com.lankr.tv_cloud.model.InvitcodeRecord;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class ApplicableVo extends BaseAPIModel{

	private Integer isStatus;

	private String applyName;

	private String mobile;

	private String hospital;

	private String departments;

	private String uuid;

	private String createDate;
	
	private String invitcode;
	
	private String userUuid;
	
	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getInvitcode() {
		return invitcode;
	}

	public void setInvitcode(String invitcode) {
		this.invitcode = invitcode;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(Integer isStatus) {
		this.isStatus = isStatus;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void buildData(ApplicableRecords record) {
		this.setUuid(record.getUuid());
		this.setApplyName(HtmlUtils.htmlEscape(record.getApplyName()));
		this.setMobile(Tools.nullValueFilter(record.getMobile()));
		if (record.getHospital() != null) {
			this.setHospital(record.getHospital().getName());
		} else {
			this.setHospital("");
		}
		if(record.getDepartments()!=null){
			this.setDepartments(record.getDepartments().getName());
		}else{
			this.setDepartments("");
		}
		
		this.setIsStatus(record.getStatus());
		this.setCreateDate(Tools.df1.format(record.getCreateDate()));
		this.setUserUuid(OptionalUtils.traceValue(record, "user.uuid"));
	}
	
	public static ApplicableVo buildData(InvitcodeRecord record) {
		if(null == record){
			return null;
		}
		ApplicableVo vo = new ApplicableVo();
		vo.setInvitcode(record.getInvitcode());
		return vo;
	}

}
