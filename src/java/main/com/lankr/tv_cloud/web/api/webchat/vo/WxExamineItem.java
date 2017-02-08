package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.facade.NormalCollectScheduleFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollectQuestionnaire;
import com.lankr.tv_cloud.model.NormalCollectSchedule;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class WxExamineItem extends BaseAPIModel{

	private String uuid;

	private String name;

	private String desc;

	private String courseUuid;

	private String cover;

	// 0=未参加 1=已通过 2=未通过
	private int examineStatus;
	
	private int examineNum;
	
	private int examineTime;
	
	private int qualifiedScore;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCourseUuid() {
		return courseUuid;
	}

	public void setCourseUuid(String courseUuid) {
		this.courseUuid = courseUuid;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public int getExamineStatus() {
		return examineStatus;
	}

	public void setExamineStatus(int examineStatus) {
		this.examineStatus = examineStatus;
	}
	
	

	public int getExamineNum() {
		return examineNum;
	}

	public void setExamineNum(int examineNum) {
		this.examineNum = examineNum;
	}

	public int getExamineTime() {
		return examineTime;
	}

	public void setExamineTime(int examineTime) {
		this.examineTime = examineTime;
	}

	public int getQualifiedScore() {
		return qualifiedScore;
	}

	public void setQualifiedScore(int qualifiedScore) {
		this.qualifiedScore = qualifiedScore;
	}

	public void buildData(
			NormalCollectQuestionnaire normalCollectQuestionnaire, User user,
			NormalCollectScheduleFacade normalCollectScheduleFacade) {
		this.setUuid(normalCollectQuestionnaire.getUuid());
		this.setName(OptionalUtils.traceValue(normalCollectQuestionnaire,
				"name"));
		this.setCover(OptionalUtils.traceValue(normalCollectQuestionnaire,
				"cover"));
		if (user == null) {
			this.setExamineStatus(0);
		} else {
			NormalCollectSchedule mCollectSchedule = normalCollectScheduleFacade
					.selectChapterScheduleByUser(normalCollectQuestionnaire, user);
			if (mCollectSchedule == null) {
				this.setExamineStatus(0);
			} else {
				this.setExamineStatus(mCollectSchedule.getCheckStatus());
			}

		}

	}
	
	public void buildDetail(NormalCollectQuestionnaire normalCollectQuestionnaire,NormalCollect normalCollect){
		this.setStatus(Status.SUCCESS);
		this.setUuid(normalCollectQuestionnaire.getUuid());
		this.setName(OptionalUtils.traceValue(normalCollectQuestionnaire,
				"name"));
		this.setCover(OptionalUtils.traceValue(normalCollectQuestionnaire,
				"cover"));
		this.setDesc(OptionalUtils.traceValue(normalCollectQuestionnaire, "mark"));
		this.setExamineNum(OptionalUtils.traceInt(normalCollectQuestionnaire, "collectNum"));
		this.setExamineTime(OptionalUtils.traceInt(normalCollectQuestionnaire, "collectTime"));
		this.setQualifiedScore(OptionalUtils.traceInt(normalCollect, "passScore"));
	}

}
