package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.facade.CertificationFacade;
import com.lankr.tv_cloud.facade.WebchatFacade;
import com.lankr.tv_cloud.model.BroadcastUser;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.SignUpUser;
import com.lankr.tv_cloud.model.WebchatUser;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class BroadcastUserVo extends BaseAPIModel{
	
	private String username;
	
	private String phone;
	
	private String realName;
	
	private String wxnickname;
	
	private String userType;
	
	private String bookDate;
	
	private String modifyDate;
	
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getBookDate() {
		return bookDate;
	}

	public void setBookDate(String bookDate) {
		this.bookDate = bookDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
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

	public void buildTableData(BroadcastUser broadcastUser,WebchatFacade webchatFacade,CertificationFacade certificationFacade){
		this.setUuid(broadcastUser.getUuid());
		this.setUsername(OptionalUtils.traceValue(broadcastUser, "user.username"));
		this.setPhone(OptionalUtils.traceValue(broadcastUser, "user.phone"));
		this.setBookDate(Tools.df1.format(broadcastUser.getCreateDate()));
		this.setModifyDate(Tools.df1.format(broadcastUser.getModifyDate()));
		this.setIsStatus(broadcastUser.getStatus());
		int type=OptionalUtils.traceInt(broadcastUser, "user.userExpand.type");
		if(type==0){
			this.setUserType("医生用户");
		}else{
			this.setUserType("企业用户");
		}
		WebchatUser webchatUser=webchatFacade.searchWebChatUserByUserId(broadcastUser.getUser().getId());
		this.setWxnickname(OptionalUtils.traceValue(webchatUser, "nickname"));
		Certification certification=certificationFacade.getCertifiActiveByUserId(broadcastUser.getUser());
		this.setRealName(OptionalUtils.traceValue(certification, "name"));
	}
	
	public void buildBookUser(SignUpUser signUpUser,WebchatFacade webchatFacade,CertificationFacade certificationFacade){
		this.setUuid(signUpUser.getUuid());
		this.setUsername(OptionalUtils.traceValue(signUpUser, "user.username"));
		this.setPhone(OptionalUtils.traceValue(signUpUser, "user.phone"));
		this.setBookDate(Tools.df1.format(signUpUser.getCreateDate()));
		this.setModifyDate(Tools.df1.format(signUpUser.getModifyDate()));
		this.setIsStatus(signUpUser.getStatus());
		int type=OptionalUtils.traceInt(signUpUser, "user.userExpand.type");
		if(type==0){
			this.setUserType("医生用户");
		}else{
			this.setUserType("企业用户");
		}
		WebchatUser webchatUser=webchatFacade.searchWebChatUserByUserId(signUpUser.getUser().getId());
		this.setWxnickname(OptionalUtils.traceValue(webchatUser, "nickname"));
		Certification certification=certificationFacade.getCertifiActiveByUserId(signUpUser.getUser());
		this.setRealName(OptionalUtils.traceValue(certification, "name"));
	}
	

}
