package com.lankr.tv_cloud.vo.datatable;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Shake;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

@SuppressWarnings("unused")
public class ShakeDataItem{

	private String userName;
	private String userNickName;
	private String createDate;
	private String isWinner;
	private String modifyDate;
	private String mark;
	private int status;
	private String uuid;
	private String userUuid;

	public static ShakeDataItem build(Shake shake) {
		if (shake == null)
			return null;
		
		ShakeDataItem item = new ShakeDataItem();
		
		item.userName = OptionalUtils.traceValue(shake, "user.username");
		item.userNickName = OptionalUtils.traceValue(shake, "user.nickname");
		item.createDate = Tools.formatYMDHMSDate(shake.getCreateDate());
		item.modifyDate = Tools.formatYMDHMSDate(shake.getModifyDate());
		item.mark = HtmlUtils.htmlEscape(shake.getMark());
		item.status = shake.getStatus();
		item.uuid = shake.getUuid();
		item.userUuid = OptionalUtils.traceValue(shake, "user.uuid");
		
		if(shake.getMoney()>0){
			item.isWinner = "true";
		}else{
			item.isWinner = "false";
		}
		return item;
	}
}
