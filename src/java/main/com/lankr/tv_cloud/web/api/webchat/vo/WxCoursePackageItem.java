package com.lankr.tv_cloud.web.api.webchat.vo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lankr.tv_cloud.facade.MediaCentralFacade;
import com.lankr.tv_cloud.facade.NormalCollectQuestionnaireFacade;
import com.lankr.tv_cloud.facade.NormalCollectScheduleFacade;
import com.lankr.tv_cloud.facade.PageRemainFacade;
import com.lankr.tv_cloud.facade.PraiseFacade;
import com.lankr.tv_cloud.facade.ResourceGroupFacade;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollectQuestionnaire;
import com.lankr.tv_cloud.model.NormalCollectSchedule;
import com.lankr.tv_cloud.model.ResourceGroup;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.util.WxCourseScheduleUtil;

public class WxCoursePackageItem {

	private String uuid;

	private String name;

	private String cover;

	private String bgcover;

	private int learnCount;

	private int praiseCount;

	private int commentCount;

	private int learnSchedule;

	private String dateTime;

	private String userPhoto;

	private String showName;

	private int checkStatus;//该课程是否学习 0=未学 1=学习

	private String desc;

	private boolean logined;
	
	private int studyStatus;

	private List<WxChapterItem> items;
	
	

	public int getStudyStatus() {
		return studyStatus;
	}

	public void setStudyStatus(int studyStatus) {
		this.studyStatus = studyStatus;
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

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public int getLearnCount() {
		return learnCount;
	}

	public void setLearnCount(int learnCount) {
		this.learnCount = learnCount;
	}

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getLearnSchedule() {
		return learnSchedule;
	}

	public void setLearnSchedule(int learnSchedule) {
		this.learnSchedule = learnSchedule;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public int getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<WxChapterItem> getItems() {
		return items;
	}

	public void setItems(List<WxChapterItem> items) {
		this.items = items;
	}

	public String getBgcover() {
		return bgcover;
	}

	public void setBgcover(String bgcover) {
		this.bgcover = bgcover;
	}

	public boolean isLogined() {
		return logined;
	}

	public void setLogined(boolean logined) {
		this.logined = logined;
	}

	public void buildBaseData(NormalCollect normalCollect) {
		this.setUuid(normalCollect.getUuid());
		this.setName(OptionalUtils.traceValue(normalCollect, "name"));
	}

	public void buildListData(NormalCollect normalCollect,
			MediaCentralFacade mediaCentralFacade) {
		this.buildBaseData(normalCollect);
		MediaCentral media = mediaCentralFacade.getNormalCollectMedia(
				normalCollect, MediaCentral.SIGN_COURSE_WEB_COVER);
		this.setCover(OptionalUtils.traceValue(media, "url"));
		this.setDateTime(Tools.formatYMDHMSDate(normalCollect.getCreateDate()));
		this.setLearnCount(OptionalUtils.traceInt(normalCollect, "numbers"));
		// 未完
	}

	public void buildDetail(NormalCollect normalCollect,
			MediaCentralFacade mediaCentralFacade, User user) {
		this.buildBaseData(normalCollect);
		this.setDesc(OptionalUtils.traceValue(normalCollect, "mark"));
		MediaCentral media = mediaCentralFacade.getNormalCollectMedia(
				normalCollect, MediaCentral.SIGN_COURSE_WECHAT_BG);
		this.setBgcover(OptionalUtils.traceValue(media, "url"));
		// 填充其他具体字段
		this.setLearnCount(OptionalUtils.traceInt(normalCollect, "numbers"));
		if (user != null) {
			this.setLogined(true);
		}
	}
	
	public void buildLearn(NormalCollectSchedule normalCollectSchedule){
		if(normalCollectSchedule==null){
			this.setStudyStatus(0);
		}else{
			this.setStudyStatus(normalCollectSchedule.getStudyStatus());
		}
	}

	public void buildUserData(WxUserShowInfo info) {
		this.setShowName(OptionalUtils.traceValue(info, "showName"));
		this.setUserPhoto(OptionalUtils.traceValue(info, "photo"));
	}

	public void buildChapterList(List<NormalCollect> list,
			ResourceGroupFacade resourceGroupFacade,
			PageRemainFacade pageRemainFacade, PraiseFacade praiseFacade,
			User user) {
		if (Tools.isEmpty(list))
			return;
		this.items = new ArrayList<WxChapterItem>();
		for (NormalCollect normalCollect : list) {
			WxChapterItem item = new WxChapterItem();
			item.buildData(normalCollect);
			List<ResourceGroup> groups = resourceGroupFacade
					.wxChapterResourceGroups(ResourceGroup.TYPE_COURSE_SEGMENT,
							normalCollect.getId());
			item.buildItems(groups, pageRemainFacade, praiseFacade, user);
			this.items.add(item);
		}

		if (user == null) {
			return;
		}
		int totalNum = 0, passNum = 0;
		if (Tools.isEmpty(this.items)) {
			return;
		}
		for (WxChapterItem wxChapterItem : this.items) {
			if (!Tools.isEmpty(wxChapterItem.getItems())) {
				totalNum += wxChapterItem.getItems().size();
				passNum += wxChapterItem.getPassNum();
			}
		}
		this.setLearnSchedule(WxCourseScheduleUtil.calculationProgress(
				totalNum, passNum));
	}

	// 新的章节计算
	public void buildChapterList(List<NormalCollect> list,
			ResourceGroupFacade resourceGroupFacade, PraiseFacade praiseFacade,
			List<NormalCollectSchedule> schedules, User user,
			NormalCollectQuestionnaireFacade normalCollectQuestionnaireFacade,
			NormalCollectScheduleFacade normalCollectScheduleFacade) {
		if (Tools.isEmpty(list))
			return;
		Map<Integer, NormalCollectSchedule> map = creatMapS(schedules);
		this.items = new ArrayList<WxChapterItem>();
		WxExamineItem preExamine = null;
		// 暂时未加考试
		for (NormalCollect normalCollect : list) {
			WxChapterItem item = new WxChapterItem();
			item.buildData(normalCollect);
			//章节的登录
			item.loginData(user);
			List<ResourceGroup> groups = resourceGroupFacade
					.wxChapterResourceGroups(ResourceGroup.TYPE_COURSE_SEGMENT,
							normalCollect.getId());
			item.buildItems(groups, map, praiseFacade);
			
			//章节的点击
			boolean preConfirm=normalCollect.needPreviousPassed();
			if(preConfirm){
				//需要通过前一章节考试
				int status=OptionalUtils.traceInt(preExamine, "examineStatus");
				if(status==1){
					item.setDisableClick(1);
				}else{
					item.setDisableClick(0);
				}
			}else{
				//不需要
				item.setDisableClick(1);
			}
			
			// 考卷
			NormalCollectQuestionnaire normalCollectQuestionnaire = normalCollectQuestionnaireFacade
					.selectQuestionnaireOne(normalCollect.getId(),
							NormalCollectQuestionnaire.TYPE_NORMALCOLLECT);
			if (normalCollectQuestionnaire != null
					&& !normalCollectQuestionnaire.apiUseable()) {
				WxExamineItem wxExamineItem = new WxExamineItem();
				wxExamineItem.buildData(normalCollectQuestionnaire, user,
						normalCollectScheduleFacade);
				item.setExamineItem(wxExamineItem);
				preExamine=wxExamineItem;
			} else {
				preExamine = null;
			}
			this.items.add(item);
		}
		int totalNum = 0, passNum = 0;
		if (Tools.isEmpty(this.items)) {
			return;
		}
		for (WxChapterItem wxChapterItem : this.items) {
			if (!Tools.isEmpty(wxChapterItem.getItems())) {
				totalNum += wxChapterItem.getItems().size();
				passNum += wxChapterItem.getPassNum();
			}
		}
		this.setLearnSchedule(WxCourseScheduleUtil.calculationProgress(
				totalNum, passNum));

	}

	public Map<Integer, NormalCollectSchedule> creatMapS(
			List<NormalCollectSchedule> schedules) {
		Map<Integer, NormalCollectSchedule> map = new HashMap<Integer, NormalCollectSchedule>();
		if (Tools.isEmpty(schedules)) {
			return map;
		}
		for (NormalCollectSchedule normalCollectSchedule : schedules) {
			map.put(normalCollectSchedule.getReferId(), normalCollectSchedule);
		}
		return map;
	}

	public static void main(String[] args) {
		int a = 0;
		float f = (float) a;
		DecimalFormat format = new DecimalFormat("#");
		float result = f / 3 * 100;
		System.out.println(result);
		System.out.println(format.format(result));
	}

}
