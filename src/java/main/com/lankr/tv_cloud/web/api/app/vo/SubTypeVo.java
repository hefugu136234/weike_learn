package com.lankr.tv_cloud.web.api.app.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.api.tv.SubscribeEnum;

public class SubTypeVo extends BaseAPIModel {

	private List<Type> item;

	public void build() {
		item = new ArrayList<SubTypeVo.Type>();
		setStatus(Status.SUCCESS);
		for (SubscribeEnum t : SubscribeEnum.values()) {
			Type type = new Type(t.name().toLowerCase(),t.getDescription());
			item.add(type);
		}
	}

	private class Type {
		String code;
		String desc;

		Type(String c, String d) {
			code = c;
			desc = d;
		}
	}
}
