package com.lankr.tv_cloud.vo.datatable;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.LotteryRecord;
import com.lankr.tv_cloud.model.Shake;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.AbsHandlerIntercepor;
import com.lankr.tv_cloud.web.BaseController.Platform;
import com.lankr.tv_cloud.web.api.webchat.WebChatInterceptor;

@SuppressWarnings("unused")
public class LotteryRecordDataItem{

	private String lotteryName;
	private String userName;
	private String userUuid;
	private String createDate;
	private String isWinner;
	private String exchangeCode;
	private String awardName;
	private String modifyDate;
	private String mark;
	private int status;
	private String uuid;

	public static LotteryRecordDataItem build(LotteryRecord record) {
		if (record == null)
			return null;
		
		LotteryRecordDataItem item = new LotteryRecordDataItem();
		item.lotteryName = OptionalUtils.traceValue(record, "lottery.name");
		item.userName = OptionalUtils.traceValue(record, "user.nickname");
		item.userUuid = OptionalUtils.traceValue(record, "user.uuid");
		item.createDate = Tools.formatYMDHMSDate(record.getCreateDate());
		if(null == record.getAward()){
			item.isWinner = "NO";
		}else{
			item.isWinner = "YES";
		}
		item.awardName = OptionalUtils.traceValue(record, "award.name");
		if(!Platform.WECHAT.equals(new WebChatInterceptor().platform())){
			String exchangeCodeTmp = OptionalUtils.traceValue(record, "ticket");
			item.exchangeCode = Tools.replaceSubString(exchangeCodeTmp, 3);
		}
		item.exchangeCode = OptionalUtils.traceValue(record, "ticket");
		item.modifyDate = Tools.formatYMDHMSDate(record.getModifyDate());
		item.mark = HtmlUtils.htmlEscape(record.getMark());
		item.status = record.getStatus();
		item.uuid = record.getUuid();
		return item;
	}
}
