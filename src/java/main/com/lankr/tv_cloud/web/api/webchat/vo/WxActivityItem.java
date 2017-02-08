package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityConfigMedia;
import com.lankr.tv_cloud.model.ActivityExpert;
import com.lankr.tv_cloud.model.ActivityUser;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ActivityConfigMedia.Snapshot;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class WxActivityItem {
	// 每一个item 的详情

	private String uuid;
	private String cover;
	private String kv;
	private String background;
	private String mark;// 简介,用于list展示
	private String notice;// 报名须知
	private String description;// 详情 活动详情页展示
	private String name;
	private String dateTime;
	private boolean collected;// 是否可以提交作品
	private boolean signUp;// 是否已经报名参加

	private List<WxResourceItem> ranks;

	private List<WxExpertVo> expertList;

	private List<WxResourceItem> recommends;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getKv() {
		return kv;
	}

	public void setKv(String kv) {
		this.kv = kv;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public List<WxResourceItem> getRanks() {
		return ranks;
	}

	public void setRanks(List<WxResourceItem> ranks) {
		this.ranks = ranks;
	}

	public List<WxExpertVo> getExpertList() {
		return expertList;
	}

	public void setExpertList(List<WxExpertVo> expertList) {
		this.expertList = expertList;
	}

	public List<WxResourceItem> getRecommends() {
		return recommends;
	}

	public void setRecommends(List<WxResourceItem> recommends) {
		this.recommends = recommends;
	}

	public boolean isCollected() {
		return collected;
	}

	public void setCollected(boolean collected) {
		this.collected = collected;
	}

	public boolean isSignUp() {
		return signUp;
	}

	public void setSignUp(boolean signUp) {
		this.signUp = signUp;
	}

	public String getTypeSrc(String json, int key) {
		String val = "";
		if (json == null || json.isEmpty())
			return val;
		try {
			List<ActivityConfigMedia> list = Snapshot.convertMedias(json);
			if (list != null && !list.isEmpty()) {
				for (ActivityConfigMedia activityConfigMedia : list) {
					if (activityConfigMedia.getType() == key) {
						val = activityConfigMedia.getUrl();
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return val;
	}

	public void buildbSimpleData(Activity activity) {
		this.setUuid(activity.getUuid());
		this.setName(OptionalUtils.traceValue(activity, "name"));
	}

	public void buildIndexList(Activity activity) {
		buildbSimpleData(activity);
		String json = OptionalUtils.traceValue(activity, "config.medias");
		String cover = getTypeSrc(json, ActivityConfigMedia.MEDIA_WX_COVER);
		this.setCover(cover);
	}

	public void buildSecondSubjectKv(Activity activity, String kv) {
		buildbSimpleData(activity);
		this.setKv(kv);
	}

	public void buildWonderItem(Activity activity) {
		buildbSimpleData(activity);
		String json = OptionalUtils.traceValue(activity, "config.medias");
		String kv = getTypeSrc(json, ActivityConfigMedia.MEDIA_WX_KV);
		this.setKv(kv);
		this.setMark(OptionalUtils.traceValue(activity, "mark"));
		this.setDateTime(Tools.formatYMDHMSDate(activity.getCreateDate()));
	}

	public void buildRank(Activity activity) {
		buildbSimpleData(activity);
		String json = OptionalUtils.traceValue(activity, "config.medias");
		String background = getTypeSrc(json,
				ActivityConfigMedia.MEDIA_WX_BACKGROUND);
		this.setBackground(background);
		//做分享使用
		String kv=getTypeSrc(json,
				ActivityConfigMedia.MEDIA_WX_KV);
		this.setKv(kv);
	}

	public void buildRankList(List<Resource> resources) {
		if (Tools.isEmpty(resources)) {
			return;
		}
		this.ranks = new ArrayList<WxResourceItem>();
		for (Resource resource : resources) {
			WxResourceItem item = new WxResourceItem();
			item.buildRankListItem(resource);
			this.ranks.add(item);
		}
	}

	public void buildDetail(Activity activity) {
		buildRank(activity);
		this.setCollected(activity.allowCollected());
		this.setDescription(OptionalUtils.traceValue(activity, "description"));
		this.setMark(OptionalUtils.traceValue(activity, "mark"));
		//this.setNotice(OptionalUtils.traceValue(activity, "config.notification"));//作品提交须知
		
	}

	public void buildSignUp(ActivityUser activityUser) {
		if (activityUser == null || !activityUser.apiUseable()) {
			this.setSignUp(false);
		} else {
			this.setSignUp(true);
		}
	}

	public void buildRecommend(List<Resource> resources) {
		if (Tools.isEmpty(resources)) {
			return;
		}
		this.recommends = new ArrayList<WxResourceItem>();
		for (Resource resource : resources) {
			WxResourceItem item = new WxResourceItem();
			item.buildBaseListItem(resource);
			this.recommends.add(item);
		}
	}

	public void buildExpert(List<ActivityExpert> list) {
		if (Tools.isEmpty(list)) {
			return;
		}
		this.expertList = new ArrayList<WxExpertVo>();
		for (ActivityExpert activityExpert : list) {
			WxExpertVo vo = new WxExpertVo();
			vo.buildData(activityExpert);
			this.expertList.add(vo);
		}
	}

}
