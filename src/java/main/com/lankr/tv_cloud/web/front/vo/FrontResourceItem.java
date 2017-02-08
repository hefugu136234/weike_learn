package com.lankr.tv_cloud.web.front.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.tv.ResourceItem;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;

public class FrontResourceItem {

	private String uuid;

	private String name;

	private int viewCount;

	private String cover;

	private String code;

	private String speakerName;

	private String speakerPhoto;

	private String catgoryName;

	private String hospitalName;

	private String desc;

	private String dateTime;

	private int collectCount;

	private int praiseCount;

	private boolean collectStatus;

	private boolean praiseStatus;

	private String qr;

	private String type;

	private FrontNewsVo newsVo;

	private FrontVideoVo videoVo;

	private FrontThreeScreenVo threeScreenVo;

	private FrontPdfVo pdfVo;

	private List<TopMenuItem> menuList;

	private List<String> tags;

	private boolean vrFlag;

	private boolean bloody;
	
	

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

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
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

	public String getCatgoryName() {
		return catgoryName;
	}

	public void setCatgoryName(String catgoryName) {
		this.catgoryName = catgoryName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
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

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	public FrontNewsVo getNewsVo() {
		return newsVo;
	}

	public void setNewsVo(FrontNewsVo newsVo) {
		this.newsVo = newsVo;
	}

	public FrontVideoVo getVideoVo() {
		return videoVo;
	}

	public void setVideoVo(FrontVideoVo videoVo) {
		this.videoVo = videoVo;
	}

	public FrontThreeScreenVo getThreeScreenVo() {
		return threeScreenVo;
	}

	public void setThreeScreenVo(FrontThreeScreenVo threeScreenVo) {
		this.threeScreenVo = threeScreenVo;
	}

	public FrontPdfVo getPdfVo() {
		return pdfVo;
	}

	public void setPdfVo(FrontPdfVo pdfVo) {
		this.pdfVo = pdfVo;
	}

	public List<TopMenuItem> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<TopMenuItem> menuList) {
		this.menuList = menuList;
	}

	public String getSpeakerPhoto() {
		return speakerPhoto;
	}

	public void setSpeakerPhoto(String speakerPhoto) {
		this.speakerPhoto = speakerPhoto;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void buildItemList(Resource resource) {
		this.setUuid(resource.getUuid());
		String name = OptionalUtils.traceValue(resource, "name");
		this.setName(name);
		this.setViewCount(OptionalUtils.traceInt(resource, "viewCount"));

		String cover = OptionalUtils.traceValue(resource, "coverTaskId");
		cover = WxUtil.getResourceCover(cover);
		this.setCover(cover);

		this.setType(resource.getType().name());

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
		this.setDesc(OptionalUtils.traceValue(resource, "mark"));
		if (null != resource.getTags() && resource.getTags().size() > 0) {
			tags = new ArrayList<String>();
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
				tags.add(tag.getName());
			}
		}
	}

	public String getNamePop(String name) {
		if (name == null || name.isEmpty()) {
			name = "无";
		}
		return name;
	}

	public void buildActivityRes(ResourceItem item) {
		this.setUuid(OptionalUtils.traceValue(item, "uuid"));
		this.setName(OptionalUtils.traceValue(item, "name"));
		this.setViewCount(OptionalUtils.traceInt(item, "viewCount"));
		String cover = OptionalUtils.traceValue(item, "cover");
		this.setCover(cover);

		this.setType(OptionalUtils.traceValue(item, "type"));

		String code = OptionalUtils.traceValue(item, "code");
		this.setCode(getNamePop(code));

		String categoryName = OptionalUtils.traceValue(item, "category.name");
		this.setCatgoryName(getNamePop(categoryName));

		String speakerName = OptionalUtils.traceValue(item, "speaker.name");
		this.setSpeakerName(getNamePop(speakerName));

		this.setSpeakerPhoto(OptionalUtils.traceValue(item, "speaker.avatar"));

		String hospitalName = OptionalUtils.traceValue(item,
				"speaker.hospitalName");
		this.setHospitalName(getNamePop(hospitalName));

		this.setDesc(OptionalUtils.traceValue(item, "descript"));

		this.setDateTime(OptionalUtils.traceValue(item, "date"));

		if (null != item.getTags() && item.getTags().size() > 0) {
			tags = new ArrayList<String>();
			for (String tag : item.getTags()) {
				if(tag.toUpperCase().contains("VR")){
					this.setVrFlag(true);
					this.setType("VR");
				}
				if(tag.contains("血")){
					this.setBloody(true);
				}
				tags.add(tag);
			}
		}

	}

}
