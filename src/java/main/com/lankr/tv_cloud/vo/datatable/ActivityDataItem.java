package com.lankr.tv_cloud.vo.datatable;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class ActivityDataItem{

	private String uuid;

	private String name;

	private String createDate;

	private String username;

	private String categoryName;

	private int members;

	private String mark;

	private int _status;

	public static ActivityDataItem build(Activity activity) {
		if (activity == null)
			return null;
		ActivityDataItem item = new ActivityDataItem();
		item.uuid = activity.getUuid();
		item.name = HtmlUtils.htmlEscape(activity.getName());
		item.createDate = Tools.formatYMDHMSDate(activity.getCreateDate());
		item.username = OptionalUtils.traceValue(activity, "user.username");
		item.categoryName = OptionalUtils.traceValue(activity, "category.name");
		item.members = OptionalUtils.traceInt(activity, "members");
		item.mark = HtmlUtils.htmlEscape(activity.getMark());
		item._status = activity.getStatus();
		return item;
	}
}
