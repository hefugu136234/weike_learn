package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;

public class WxResourceItem {

	/**
	 * 于2016-06-15 修改 为微信返回前台的唯一resource内容list item
	 */

	private String uuid;

	private String name;

	private String cover;

	private String code;

	private String speakerName;

	private String speakerPhoto;

	private String hospitalName;

	private String catgoryName;

	private String dateTime;

	private String updateTime;

	private String type;

	private int viewCount;

	private int collectCount;

	private int praiseCount;

	private int shareCount;

	private String qr;

	private boolean collectStatus;

	private boolean praiseStatus;

	private String redirectUri;

	private String desc;

	private WxNews news;

	private WxPdf pdf;

	private WxVideo video;

	private WxThreeScreen threeScreen;

	private List<String> tags;
	
	private boolean vrFlag;
	
	private boolean bloody;

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSpeakerName() {
		return speakerName;
	}

	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getCatgoryName() {
		return catgoryName;
	}

	public void setCatgoryName(String catgoryName) {
		this.catgoryName = catgoryName;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(int collectCount) {
		this.collectCount = collectCount;
	}

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	public boolean isCollectStatus() {
		return collectStatus;
	}

	public void setCollectStatus(boolean collectStatus) {
		this.collectStatus = collectStatus;
	}

	public boolean isPraiseStatus() {
		return praiseStatus;
	}

	public void setPraiseStatus(boolean praiseStatus) {
		this.praiseStatus = praiseStatus;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSpeakerPhoto() {
		return speakerPhoto;
	}

	public void setSpeakerPhoto(String speakerPhoto) {
		this.speakerPhoto = speakerPhoto;
	}

	public WxNews getNews() {
		return news;
	}

	public void setNews(WxNews news) {
		this.news = news;
	}

	public WxPdf getPdf() {
		return pdf;
	}

	public void setPdf(WxPdf pdf) {
		this.pdf = pdf;
	}

	public WxVideo getVideo() {
		return video;
	}

	public void setVideo(WxVideo video) {
		this.video = video;
	}

	public WxThreeScreen getThreeScreen() {
		return threeScreen;
	}

	public void setThreeScreen(WxThreeScreen threeScreen) {
		this.threeScreen = threeScreen;
	}

	public int getShareCount() {
		return shareCount;
	}

	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	

	public boolean isVrFlag() {
		return vrFlag;
	}

	public void setVrFlag(boolean vrFlag) {
		this.vrFlag = vrFlag;
	}

	public boolean isBloody() {
		return bloody;
	}

	public void setBloody(boolean bloody) {
		this.bloody = bloody;
	}

	public void buildRankListItem(Resource resource) {
		buildBaseListItem(resource);
		String avatar = OptionalUtils.traceValue(resource, "speaker.avatar");
		avatar = WxUtil.getDefaultAvatar(avatar);
		this.setSpeakerPhoto(avatar);
	}

	public void buildBaseListItem(Resource resource) {
		this.setUuid(resource.getUuid());
		String name = OptionalUtils.traceValue(resource, "name");
		this.setName(name);
		this.setViewCount(OptionalUtils.traceInt(resource, "viewCount"));

		this.setType(resource.getType().name());

		String cover = OptionalUtils.traceValue(resource, "coverTaskId");
		cover = WxUtil.getResourceCover(cover);
		this.setCover(cover);

		String code = OptionalUtils.traceValue(resource, "code");
		this.setCode(getNamePop(code));

		String categoryName = OptionalUtils.traceValue(resource,
				"category.name");
		this.setCatgoryName(getNamePop(categoryName));

		String speakerName = OptionalUtils.traceValue(resource, "speaker.name");
		this.setSpeakerName(getNamePop(speakerName));

		String hospitalName = OptionalUtils.traceValue(resource,
				"speaker.hospital.name");

		this.setHospitalName(getNamePop(hospitalName));
		this.setDateTime(Tools.formatYMDHMSDate(resource.getCreateDate()));
		/**
		 * 2016-06-28 部分关联其他资源的变量（如 收藏，活动资源）
		 */
		Date updateDate = resource.getUpdated_at();
		if (updateDate != null) {
			this.setUpdateTime(Tools.formatYMDHMSDate(updateDate));
		}
		this.setDesc(OptionalUtils.traceValue(resource, "mark"));
		if (null != resource.getTags() && resource.getTags().size() > 0) {
			this.tags = new ArrayList<String>();
			for (TagChild tag : resource.getTags()) {
				String tagName=OptionalUtils.traceValue(tag, "name");
				if(Tools.isBlank(tagName))
					continue;
				if(tagName.toUpperCase().contains("VR")){
					this.setVrFlag(true);
					this.setType("VR");
				}
				if(tagName.contains("血")){
					this.setBloody(true);
				}
				tags.add(tagName);
			}
		}
	}

	public void buildDetailLittle(Resource resource) {
		buildBaseListItem(resource);

		if (resource.getType() == Type.PDF) {
			this.pdf = new WxPdf();
			pdf.build(resource.getPdf());
		} else if (resource.getType() == Type.THREESCREEN) {
			this.threeScreen = new WxThreeScreen();
			threeScreen.buildDetail(resource.getThreeScreen());
		} else if (resource.getType() == Type.NEWS) {
			this.news = new WxNews();
			news.buildDetail(resource.getNews());
		}else if(resource.getType() == Type.VIDEO){
			this.video=new WxVideo();
			video.buildData(resource.getVideo());
		}
	}

	public String getNamePop(String name) {
		if (name == null || name.isEmpty()) {
			name = "无";
		}
		return name;
	}
}
