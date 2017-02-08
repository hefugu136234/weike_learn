package com.lankr.tv_cloud.vo.datatable;

import java.util.Date;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.ActivityExpert;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.UuidVo;

@SuppressWarnings("unused")
public class ActivityExpertDataItem {

	private String header;
	private String name;
	private int sex;
	private String hospital;
	private String department;
	private String position;
	private String mark;
	private int status;
	private String uuid;

	public static ActivityExpertDataItem build(ActivityExpert activityExpert) {
		if (null == activityExpert)
			return null;
		ActivityExpertDataItem activityResDataItem = new ActivityExpertDataItem();
		activityResDataItem.header = OptionalUtils.traceValue(activityExpert, "speaker.avatar");
		activityResDataItem.name = OptionalUtils.traceValue(activityExpert, "speaker.name");
		activityResDataItem.sex = OptionalUtils.traceInt(activityExpert, "speaker.sex");
		activityResDataItem.hospital = OptionalUtils.traceValue(activityExpert, "speaker.hospital.name");
		activityResDataItem.department = OptionalUtils.traceValue(activityExpert, "speaker.department.name");
		activityResDataItem.position = OptionalUtils.traceValue(activityExpert, "speaker.position");
		activityResDataItem.mark = OptionalUtils.traceValue(activityExpert, "speaker.resume");
		activityResDataItem.status = activityExpert.getStatus();
		activityResDataItem.uuid = activityExpert.getUuid();
		return activityResDataItem;
	}
}
