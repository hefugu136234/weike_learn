package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Questionnaire;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class DynamicSearchVo extends BaseAPIModel {

	private transient static String sep = " - ";

	private String q;

	private List<ChosenItem> items = new ArrayList<ChosenItem>();

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}
	
	

	public List<ChosenItem> getItems() {
		return items;
	}

	public void setItems(List<ChosenItem> items) {
		this.items = items;
	}

	public void addItem(String uuid, String text) {
		// Item item = new Item(uuid, text);
		// if(!items.contains(item)){
		addItem(new ChosenItem(uuid, text));
		// }
	}

	public void addItem(ChosenItem item) {
		items.add(item);
	}

	// text 为 资源名 + 类别 + 分类名 + 讲者
	public static DynamicSearchVo buildResources(List<Resource> resources,
			String q) {
		DynamicSearchVo vo = new DynamicSearchVo();
		vo.setStatus(Status.SUCCESS);
		vo.q = q;
		if (resources != null) {
			for (Resource resource : resources) {
				vo.addItem(resourceItem(resource));
			}
		}
		return vo;
	}
	
	public static DynamicSearchVo buildQuestionnaire(List<Questionnaire> questionnaires,
			String q) {
		DynamicSearchVo vo = new DynamicSearchVo();
		vo.setStatus(Status.SUCCESS);
		vo.q = q;
		if (questionnaires != null) {
			for (Questionnaire questionnaire : questionnaires) {
				vo.addItem(questionnaireItem(questionnaire));
			}
		}
		return vo;
	}

	public static ChosenItem resourceItem(Resource resource) {
		if (resource == null)
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append(resource.getName()).append(sep).append(resource.getType())
				.append(sep)
				.append(OptionalUtils.traceValue(resource, "category.name"))
				.append(sep)
				.append(OptionalUtils.traceValue(resource, "speaker.name"));
		ChosenItem item = new ChosenItem(resource.getUuid(), sb.toString());
		return item;
	}

	public static DynamicSearchVo buildActivities(List<Activity> activities,
			String q) {
		DynamicSearchVo vo = new DynamicSearchVo();
		vo.setStatus(Status.SUCCESS);
		vo.q = q;
		if (activities != null) {
			for (Activity activity : activities) {
				vo.addItem(activityItem(activity));
			}
		}
		return vo;
	}

	public static DynamicSearchVo buildBroadcasts(List<Broadcast> casts,
			String q) {
		DynamicSearchVo vo = new DynamicSearchVo();
		vo.setStatus(Status.SUCCESS);
		vo.q = q;
		if (casts != null) {
			for (Broadcast cast : casts) {
				vo.addItem(broadcastItem(cast));
			}
		}
		return vo;
	}

	public static ChosenItem activityItem(Activity activity) {
		if (activity == null)
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append(activity.getName());
		ChosenItem item = new ChosenItem(activity.getUuid(), sb.toString());
		return item;
	}

	public static ChosenItem broadcastItem(Broadcast broadcast) {
		if (broadcast == null)
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append(broadcast.getName());
		ChosenItem item = new ChosenItem(broadcast.getUuid(), sb.toString());
		return item;
	}

	public static DynamicSearchVo buildSpeakers(List<Speaker> speakers, String q) {
		DynamicSearchVo vo = new DynamicSearchVo();
		vo.setStatus(Status.SUCCESS);
		vo.q = q;
		if (!Tools.isEmpty(speakers)) {
			for (Speaker speaker : speakers) {
				vo.addItem(speakerItem(speaker));
			}
		}
		return vo;
	}

	public static ChosenItem speakerItem(Speaker speaker) {
		if (speaker == null)
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append(speaker.getName());
		sb.append(sep);
		sb.append(OptionalUtils.traceValue(speaker, "hospital.name"));
		sb.append(sep);
		sb.append(OptionalUtils.traceValue(speaker, "user.username", "未绑定"));
		ChosenItem item = new ChosenItem(speaker.getUuid(), sb.toString());
		return item;
	}
	
	public static ChosenItem questionnaireItem(Questionnaire questionnaire) {
		if (questionnaire == null)
			return null;
		
		ChosenItem item = new ChosenItem(questionnaire.getUuid(), questionnaire.getName());
		return item;
	}
}
