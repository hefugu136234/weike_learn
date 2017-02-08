package com.lankr.tv_cloud.web.api.tv;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class QrSubscribeVo extends BaseAPIModel {

	private List<QrSubscribe> items;

	public void addQr(QrSubscribe qr) {
		if (items == null) {
			items = new ArrayList<QrSubscribe>(3);
		}
		items.add(qr);
	}
}

class QrSubscribe extends BaseAPIModel{
	private String type;
	private String qr_url;
	private String description;

	public QrSubscribe(String qr_url, String type, String description) {
		this.qr_url = qr_url;
		this.type = type;
		this.description = description;
	}

}
