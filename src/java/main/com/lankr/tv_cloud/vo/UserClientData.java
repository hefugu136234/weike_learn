package com.lankr.tv_cloud.vo;

import java.util.List;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class UserClientData extends DataTableModel<UserClientItem> {

	public void buildData(User user) {
		if (user == null)
			return;
		UserClientItem item = new UserClientItem();
		item.setUsername(HtmlUtils.htmlEscape(user.getUsername()));
		item.setMark(Tools.nullValueFilter(HtmlUtils.htmlEscape(user.getMark())));
		item.setNickname(Tools.nullValueFilter(HtmlUtils.htmlEscape(user.getNickname())));
		item.setPhoneNumber(Tools.nullValueFilter(HtmlUtils.htmlEscape(user.getPhone())));
		item.setUuid(user.getUuid());
		item.setCreateDate(Tools.df1.format(user.getModifyDate()));
		item.setIsActive(user.getIsActive());
		aaData.add(item);
	}

	public void buildData(List<User> users) {
		if (users == null || users.isEmpty())
			return;
		for (User user : users) {
			buildData(user);
		}
	}
}
