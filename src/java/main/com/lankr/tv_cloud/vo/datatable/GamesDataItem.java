package com.lankr.tv_cloud.vo.datatable;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Lottery;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class GamesDataItem{

	private String name;
	private String createDate;
	private String beginDate;
	private String endDate;
	private String mark;
	private int currentStatus;
	private String uuid;

	public static GamesDataItem build(Lottery lottery) {
		if (lottery == null)
			return null;
		GamesDataItem item = new GamesDataItem();
		item.name = HtmlUtils.htmlEscape(lottery.getName());
		item.createDate = Tools.formatYMDHMSDate(lottery.getCreateDate());
		item.beginDate = Tools.formatYMDHMSDate(lottery.getBeginDate());
		item.endDate = Tools.formatYMDHMSDate(lottery.getEndDate());
		item.mark = HtmlUtils.htmlEscape(lottery.getMark());
		item.currentStatus = OptionalUtils.traceInt(lottery, "status");
		item.uuid = HtmlUtils.htmlEscape(lottery.getUuid());
		return item;
	}
}
