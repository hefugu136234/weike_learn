package com.lankr.tv_cloud.vo;

import java.util.concurrent.TimeUnit;

import com.lankr.tv_cloud.model.Banner;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class BannerVo {
	// 图片显示
	private String imageUrls;
	private String title;
	private String createDate;
	private String modifyDate;
	private String refUrl;
	// 1 cloud 2 web 3 wechat
	private int type;
	// 0 未审核 1已上线 2已下线
	private int state;
	private String validDate;
	private String mark;
	private String uuid;
	private int position;
	private String positionStr;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(String imageUrls) {
		this.imageUrls = imageUrls;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getRefUrl() {
		return refUrl;
	}

	public void setRefUrl(String refUrl) {
		this.refUrl = refUrl;
	}

	public String getValidDate() {
		return validDate;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getPositionStr() {
		return positionStr;
	}

	public void setPositionStr(String positionStr) {
		this.positionStr = positionStr;
	}

	public void buildData(Banner banner) {
		if (null == banner)
			return;
		this.setImageUrls(OptionalUtils.traceValue(banner, "imageUrl"));
		this.setTitle(OptionalUtils.traceValue(banner, "title"));
		this.setCreateDate(Tools.df1.format(banner.getCreateDate()));
		// this.setModifyDate(Tools.df1.format(banner.getCreateDate()));
		this.setRefUrl(OptionalUtils.traceValue(banner, "refUrl"));
		this.setType(banner.getType());
		this.setState(banner.getState());
		this.setValidDate(this.setValidDateHelper(banner.getValidDate()));
		this.setMark(OptionalUtils.traceValue(banner, "mark"));
		this.setUuid(OptionalUtils.traceValue(banner, "uuid"));
		int postion=OptionalUtils.traceInt(banner, "position");
		this.setPosition(postion);
		this.setPositionStr(showArea(postion));
	}

	public String showArea(int postion) {
		String postionStr = "";
		switch (postion) {
		case Banner.POSITION_ALL:
			postionStr = "全部";
			break;
		case Banner.POSITION_INDEX:
			postionStr = "首页";
			break;
		case Banner.POSITION_BORADCAST:
			postionStr = "直播";
			break;
		case Banner.POSITION_ACTIVITY:
			postionStr = "活动";
			break;
		case Banner.POSITION_COURSE:
			postionStr = "课程";
			break;
		default:
			postionStr = "全部";
			break;
		}
		return postionStr;
	}

	private String setValidDateHelper(Long traceValue) {
		if (null == traceValue)
			return "";
		long days = TimeUnit.SECONDS.toDays(traceValue);
		return String.valueOf(days);
	}
}
