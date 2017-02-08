package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.facade.MediaCentralFacade;
import com.lankr.tv_cloud.facade.ResourceGroupFacade;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.ResourceGroup;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;

public class FrontCoursePackageItem {

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

	private int checkStatus;

	private String desc;

	private List<FrontChapterItem> items;

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

	public List<FrontChapterItem> getItems() {
		return items;
	}

	public void setItems(List<FrontChapterItem> items) {
		this.items = items;
	}
	
	

	public String getBgcover() {
		return bgcover;
	}

	public void setBgcover(String bgcover) {
		this.bgcover = bgcover;
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
		// this.setDateTime(Tools.formatYMDHMSDate(normalCollect.getCreateDate()));
		// 未完
	}

	public void buildDetail(NormalCollect normalCollect,MediaCentralFacade mediaCentralFacade) {
		this.buildBaseData(normalCollect);
		this.setDesc(OptionalUtils.traceValue(normalCollect, "mark"));
		MediaCentral media = mediaCentralFacade.getNormalCollectMedia(
				normalCollect, MediaCentral.SIGN_COURSE_WECHAT_BG);
		this.setBgcover(OptionalUtils.traceValue(media, "url"));
		// 填充其他具体字段
	}

	public void buildUserData(UserFrontBaseView view) {
		this.setShowName(OptionalUtils.traceValue(view, "showName"));
		String photo=OptionalUtils.traceValue(view, "photo");
		photo=WxUtil.getDefaultAvatar(photo);
		this.setUserPhoto(photo);
	}

	public void buildChapterList(List<NormalCollect> list,
			ResourceGroupFacade resourceGroupFacade) {
		if (Tools.isEmpty(list)) {
			return;
		}
		this.items = new ArrayList<FrontChapterItem>();
		for (NormalCollect normalCollect : list) {
			FrontChapterItem item = new FrontChapterItem();
			item.buildData(normalCollect);
			List<ResourceGroup> groups = resourceGroupFacade
					.wxChapterResourceGroups(ResourceGroup.TYPE_COURSE_SEGMENT,
							normalCollect.getId());
			item.buildItems(groups);
			this.items.add(item);
		}
	}

}
