package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.Role;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserReference;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class OupsCommonItem {
	
	private String uuid;
	
	private String commonUser;
	
	private String commonUserImg;
	
	private String dateTime;
	
	private String common;
	
	private boolean deltel; 
	
	private boolean manage;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCommonUser() {
		return commonUser;
	}

	public void setCommonUser(String commonUser) {
		this.commonUser = commonUser;
	}

	public String getCommonUserImg() {
		return commonUserImg;
	}

	public void setCommonUserImg(String commonUserImg) {
		this.commonUserImg = commonUserImg;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String common) {
		this.common = common;
	}

	public boolean isDeltel() {
		return deltel;
	}

	public void setDeltel(boolean deltel) {
		this.deltel = deltel;
	}

	public boolean isManage() {
		return manage;
	}

	public void setManage(boolean manage) {
		this.manage = manage;
	}
	
	
	public void buildData(User currentUser,Message message){
		this.setUuid(message.getUuid());
		this.setDateTime(Tools.formatYMDHMSDate(message.getCreateDate()));
		this.setCommonUser(OptionalUtils.traceValue(message, "user.nickname"));
		this.setCommonUserImg(OptionalUtils.traceValue(message, "user.avatar"));
		this.setCommon(OptionalUtils.traceValue(message, "body"));
		User messsageUser=message.getUser();
		if(messsageUser!=null&&messsageUser.getId()==currentUser.getId()){
			this.setDeltel(true);
		}else{
			this.setDeltel(false);
		}
		
		if(messsageUser!=null){
			UserReference frReference=messsageUser.getUserReference();
			if(frReference!=null){
				if(frReference.getRole().getLevel()<Role.PRO_CLIENT){
					this.setManage(true);
					return ;
				}
			}
		}
		this.setManage(false);
	}

}
