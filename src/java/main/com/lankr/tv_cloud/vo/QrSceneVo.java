package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.model.QrMessage;
import com.lankr.tv_cloud.model.QrScene;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class QrSceneVo extends BaseAPIModel {

	private String uuid;

	private String name;

	private String createDate;

	private String qrProperty;

	private String qrType;

	private String messageTitle;

	private String messageCover;

	private String messageMark;

	private String qrUrl;

	private int scanCount;

	private int auth;
	
	private String redictUrl;
	
	private String qrCover;
	
	private String qrTitle;
	
	private String qrDesc;
	
	

	public String getQrCover() {
		return qrCover;
	}

	public void setQrCover(String qrCover) {
		this.qrCover = qrCover;
	}

	public String getQrTitle() {
		return qrTitle;
	}

	public void setQrTitle(String qrTitle) {
		this.qrTitle = qrTitle;
	}

	public String getQrDesc() {
		return qrDesc;
	}

	public void setQrDesc(String qrDesc) {
		this.qrDesc = qrDesc;
	}

	public String getRedictUrl() {
		return redictUrl;
	}

	public void setRedictUrl(String redictUrl) {
		this.redictUrl = redictUrl;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQrProperty() {
		return qrProperty;
	}

	public void setQrProperty(String qrProperty) {
		this.qrProperty = qrProperty;
	}

	public String getQrType() {
		return qrType;
	}

	public void setQrType(String qrType) {
		this.qrType = qrType;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getMessageCover() {
		return messageCover;
	}

	public void setMessageCover(String messageCover) {
		this.messageCover = messageCover;
	}

	public String getMessageMark() {
		return messageMark;
	}

	public void setMessageMark(String messageMark) {
		this.messageMark = messageMark;
	}

	public String getQrUrl() {
		return qrUrl;
	}

	public void setQrUrl(String qrUrl) {
		this.qrUrl = qrUrl;
	}

	public int getAuth() {
		return auth;
	}

	public void setAuth(int auth) {
		this.auth = auth;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getScanCount() {
		return scanCount;
	}

	public void setScanCount(int scanCount) {
		this.scanCount = scanCount;
	}

	public String judyType(int type) {
		if (type == QrScene.ACTIVTY_QR_TYPE) {
			return "活动";
		}
		if (type == QrScene.CAST_QR_TYPE) {
			return "直播";
		}
		if (type == QrScene.URL_TYPE) {
			return "URL";
		}
		if (type == QrScene.RESOURCE_TYPE) {
			return "资源";
		}
		if (type == QrScene.GAME_QR) {
			return "游戏";
		}
		return "未知";
	}

	public void buildTableData(QrScene qrScene, int count) {
		this.setUuid(qrScene.getUuid());
		this.setCreateDate(Tools.formatYMDHMSDate(qrScene.getCreateDate()));
		this.setName(OptionalUtils.traceValue(qrScene, "name"));
		int limitType = qrScene.getLimitType();
		if (limitType == QrScene.QR_TEMP) {
			this.setQrProperty("临时二维码");
		} else {
			this.setQrProperty("永久二维码");
		}
		String type = judyType(qrScene.getType());
		this.setQrType(type);
		this.setScanCount(count);
	}
	
	public void buildUpdatePage(QrScene qrScene){
		this.setUuid(qrScene.getUuid());
		this.setName(OptionalUtils.traceValue(qrScene, "name"));
		String type=judyType(qrScene.getType());
		this.setQrType(type);
		String message=qrScene.getMessage();
		QrMessage qrMessage=QrMessage.jsonToMessage(message);
		if(qrMessage!=null){
			this.setRedictUrl(qrMessage.getRedictUrl());
			this.setAuth(qrMessage.getAuth());
			this.setQrTitle(OptionalUtils.traceValue(qrMessage, "title"));
			this.setQrCover(OptionalUtils.traceValue(qrMessage, "cover"));
			this.setQrDesc(OptionalUtils.traceValue(qrMessage, "desc"));
		}
	}

}
