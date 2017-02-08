package com.lankr.tv_cloud.vo.datatable;

import java.util.Date;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.UuidVo;

@SuppressWarnings("all")
public class ActivityResDataItem {

	private String name;
	private String createDate;
	private String modifyDate;
	private int status;
	private String mark;
	private int viewCount;
	private String speaker;
	private String type;
	private String uuid;
	//private UuidVo uuids = new UuidVo();
	private String recommendDate;
	private String resourceUuid;
	

	public static ActivityResDataItem build(ActivityResource activityResource) {
		if (null == activityResource)
			return null;
		ActivityResDataItem activityResDataItem = new ActivityResDataItem();
		activityResDataItem.name = OptionalUtils.traceValue(activityResource, "resource.name");
		activityResDataItem.createDate = Tools
				.formatYMDHMSDate(activityResource.getCreateDate());
		activityResDataItem.modifyDate = Tools
				.formatYMDHMSDate(activityResource.getModifyDate());
		activityResDataItem.status = activityResource.getStatus();
		activityResDataItem.mark = HtmlUtils.htmlEscape(activityResource
				.getMark());
		activityResDataItem.viewCount = OptionalUtils.traceInt(activityResource, "viewCount");
		activityResDataItem.speaker = OptionalUtils.traceValue(activityResource, "resource.speaker.name");
		activityResDataItem.type = activityResource.getResource().getType().name();
		activityResDataItem.uuid = OptionalUtils.traceValue(activityResource, "uuid");
		//if(null != activityResource.getResource())
			//activityResDataItem.uuids.build(activityResource.getResource());
		activityResDataItem.recommendDate =  Tools
				.formatYMDHMSDate(activityResource.getRecommendDate());
		activityResDataItem.resourceUuid = OptionalUtils.traceValue(activityResource, "resource.uuid");
		return activityResDataItem;
	}

}
