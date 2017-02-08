package com.lankr.tv_cloud.vo;

import java.util.Date;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.web.api.tv.CategoryVO;

public class ActivityVo {

	private String name;

	private String uuid;

	private CategoryVO category;

	private int joinType;

	private int plimit;

	private boolean collected;

	private String mark;

	private String description;

	private String notification;
	
	private String medias; 
	

	public ActivityVo build(Activity activity) {
		if (!BaseModel.hasPersisted(activity)) {
			return null;
		}
		name = activity.getName();
		uuid = activity.getUuid();
		category = new CategoryVO(OptionalUtils.traceValue(activity,
				"category.uuid"), OptionalUtils.traceValue(activity,
				"category.name"), null);
		joinType = activity.getJoinType();
		plimit = activity.getPlimit();
		collected = activity.getCollected() == 1;
		mark = activity.getMark();
		description = activity.getDescription();
		notification = OptionalUtils
				.traceValue(activity, "config.notification");
		medias = OptionalUtils
				.traceValue(activity, "config.medias");
		return this;
	}
}
