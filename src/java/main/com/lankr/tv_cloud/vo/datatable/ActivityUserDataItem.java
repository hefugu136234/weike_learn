package com.lankr.tv_cloud.vo.datatable;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityUser;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

@SuppressWarnings("all")
public class ActivityUserDataItem{
	
	//activity_user
	private String uuid;
	private String username;
	//activity_user
	private String createDate;
	//activity_user
	private String modifyDate;
	//activity_user
	private String mark;
	private String phone;
	private String email;
	private String address;
	private String company;
	private int isActive;
	private String userUuid;

	
	public static ActivityUserDataItem build(ActivityUser activityUser) {
		if (activityUser == null)
			return null;
		
		ActivityUserDataItem item = new ActivityUserDataItem();
		item.userUuid = OptionalUtils.traceValue(activityUser, "user.uuid");
		item.uuid = activityUser.getUuid();
		item.username = OptionalUtils.traceValue(activityUser, "user.nickname");
		item.createDate = Tools.formatYMDHMSDate(activityUser.getCreateDate());
		item.modifyDate = Tools.formatYMDHMSDate(activityUser.getModifyDate());
		item.mark = HtmlUtils.htmlEscape(activityUser.getMark());
		item.phone = OptionalUtils.traceValue(activityUser, "user.phone");
		item.email = OptionalUtils.traceValue(activityUser, "user.email");
		item.address = OptionalUtils.traceValue(activityUser, "user.address");
		item.company = OptionalUtils.traceValue(activityUser, "user.company");
		item.isActive = OptionalUtils.traceInt(activityUser, "isActive");
		return item;
	}
}
