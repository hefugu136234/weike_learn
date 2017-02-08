package com.lankr.tv_cloud.vo;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;

@SuppressWarnings("all")
public class UserDetailResultItem {

	private String uuid;
	private String username;
	private String nickname;
	private String createDate;
	private String phoneNumber;
	private String mark;
	//private int isActive;
	private String status;
	private String avatar;
	
	public static UserDetailResultItem build(User user) {
		if (user == null)
			return null;
		UserDetailResultItem item = new UserDetailResultItem();
		item.uuid = user.getUuid();
		item.username = HtmlUtils.htmlEscape(user.getUsername());
		item.createDate = Tools.formatYMDHMSDate(user.getCreateDate());
		item.nickname = HtmlUtils.htmlEscape(user.getNickname());
		item.phoneNumber = HtmlUtils.htmlEscape(user.getPhone());
		item.mark = HtmlUtils.htmlEscape(user.getMark());
		item.status = Status.SUCCESS.name();
		item.avatar = HtmlUtils.htmlEscape(user.getAvatar());
		return item;
	}
	
	

}
