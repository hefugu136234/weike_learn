package com.lankr.tv_cloud.vo.datatable;


import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.IntegralRecord;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

//@SuppressWarnings("all")
public class IntegralExchangeDataItem{

	private String recordUserName;	//兑换人
	private String recordDate;		//兑换申请日期
	private String processDate;		//申请处理日期
	private String expend;			//消耗积分
	private String consumeName;		//兑换物品名称
	private int consumeStock;		//所兑换物品当前库存
	private String mark;			//本次兑换记录描述 (用户行为）
	private int status;				//积分兑换状态
	private String uuid;			//积分兑换记录唯一标识
	
	private String userUuid;

	//以下字段，用户积分明细使用
	private int value;				
	private int integralType;
	private String createDate;
	private int action;
	private String resourceName;
	private String formUser;
	private String resourceSpeaker;
	
	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	public String getResourceSpeaker() {
		return resourceSpeaker;
	}

	public void setResourceSpeaker(String resourceSpeaker) {
		this.resourceSpeaker = resourceSpeaker;
	}

	public String getFormUser() {
		return formUser;
	}

	public void setFormUser(String formUser) {
		this.formUser = formUser;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getRecordUserName() {
		return recordUserName;
	}

	public void setRecordUserName(String recordUserName) {
		this.recordUserName = recordUserName;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	
	public String getProcessDate() {
		return processDate;
	}

	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	public String getExpend() {
		return expend;
	}

	public void setExpend(String expend) {
		this.expend = expend;
	}

	public String getConsumeName() {
		return consumeName;
	}

	public void setConsumeName(String consumeName) {
		this.consumeName = consumeName;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public int getConsumeStock() {
		return consumeStock;
	}

	public void setConsumeStock(int consumeStock) {
		this.consumeStock = consumeStock;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getIntegralType() {
		return integralType;
	}

	public void setIntegralType(int integralType) {
		this.integralType = integralType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static IntegralExchangeDataItem build(IntegralRecord integralRecord) {
		if (integralRecord == null)
			return null;
		IntegralExchangeDataItem item = new IntegralExchangeDataItem();
		item.setRecordUserName(OptionalUtils.traceValue(integralRecord, "user.nickname"));
		item.setRecordDate(Tools.formatYMDHMSDate(integralRecord.getCreateDate()));
		item.setProcessDate(Tools.formatYMDHMSDate(integralRecord.getModifyDate()));
		item.setExpend(String.valueOf(OptionalUtils.traceInt(integralRecord, "consume.integral")));
		item.setConsumeName(OptionalUtils.traceValue(integralRecord, "consume.name"));
		item.setConsumeStock(OptionalUtils.traceInt(integralRecord, "consume.number"));
		item.setMark(HtmlUtils.htmlEscape(integralRecord.getMark()));
		item.setStatus(integralRecord.getStatus());
		item.setUuid(integralRecord.getUuid());
		item.setUserUuid(OptionalUtils.traceValue(integralRecord, "user.uuid"));
		
		item.setValue(OptionalUtils.traceInt(integralRecord, "value"));
		item.setIntegralType(OptionalUtils.traceInt(integralRecord, "consume.type"));
		item.setCreateDate(Tools.formatYMDHMSDate(integralRecord.getCreateDate()));
		item.setAction(OptionalUtils.traceInt(integralRecord, "action"));
		item.setResourceName(OptionalUtils.traceValue(integralRecord, "resource.name"));
		item.setFormUser(OptionalUtils.traceValue(integralRecord, "fromUser.username"));
		item.setResourceSpeaker(OptionalUtils.traceValue(integralRecord, "resource.speaker.name"));
		return item;
	}
}
