package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.facade.CertificationFacade;
import com.lankr.tv_cloud.facade.WebchatFacade;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.QrcodeScanRecode;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class QrcodeScanRecodeVo {

	private String username;

	private String phone;

	private String realName;

	private String wxnickname;

	private int scanCount;

	private int viewCount;

	private String uuid;

	private int isStatus;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getWxnickname() {
		return wxnickname;
	}

	public void setWxnickname(String wxnickname) {
		this.wxnickname = wxnickname;
	}

	public int getScanCount() {
		return scanCount;
	}

	public void setScanCount(int scanCount) {
		this.scanCount = scanCount;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(int isStatus) {
		this.isStatus = isStatus;
	}

	public void buildTableData(QrcodeScanRecode qrcodeScanRecode,
			WebchatFacade webchatFacade, CertificationFacade certificationFacade) {
		this.setUuid(qrcodeScanRecode.getUuid());
		this.setUsername(OptionalUtils.traceValue(qrcodeScanRecode,
				"user.username"));
		this.setPhone(OptionalUtils.traceValue(qrcodeScanRecode, "user.phone"));
		this.setIsStatus(qrcodeScanRecode.getStatus());
		WebchatUser webchatUser = webchatFacade
				.searchWebChatUserByUserId(qrcodeScanRecode.getUser().getId());
		this.setWxnickname(OptionalUtils.traceValue(webchatUser, "nickname"));
		Certification certification = certificationFacade
				.getCertifiActiveByUserId(qrcodeScanRecode.getUser());
		this.setRealName(OptionalUtils.traceValue(certification, "name"));
		this.setScanCount(OptionalUtils.traceInt(qrcodeScanRecode, "scancount"));
		this.setViewCount(OptionalUtils.traceInt(qrcodeScanRecode, "viewcount"));
	}

}
