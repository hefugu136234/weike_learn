package com.lankr.tv_cloud.web.api.webchat.vo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lankr.tv_cloud.model.CastBanner;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.SignUpUser;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class WxOfflineActivityItem {

	private String uuid;

	private String name;

	private String desc;

	private String wxcover;

	private String webcover;

	private String priceShow;

	private String personNum;

	private String address;

	private String description;

	private boolean initiatorFlag;

	private String bookTimeShow;

	private boolean bookFlag;
	
	private String dateTime;
	
	private int bookNum;
	
	
	

	public int getBookNum() {
		return bookNum;
	}

	public void setBookNum(int bookNum) {
		this.bookNum = bookNum;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getWxcover() {
		return wxcover;
	}

	public void setWxcover(String wxcover) {
		this.wxcover = wxcover;
	}

	public String getWebcover() {
		return webcover;
	}

	public void setWebcover(String webcover) {
		this.webcover = webcover;
	}

	public String getPriceShow() {
		return priceShow;
	}

	public void setPriceShow(String priceShow) {
		this.priceShow = priceShow;
	}

	public String getPersonNum() {
		return personNum;
	}

	public void setPersonNum(String personNum) {
		this.personNum = personNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isInitiatorFlag() {
		return initiatorFlag;
	}

	public void setInitiatorFlag(boolean initiatorFlag) {
		this.initiatorFlag = initiatorFlag;
	}

	public String getBookTimeShow() {
		return bookTimeShow;
	}

	public void setBookTimeShow(String bookTimeShow) {
		this.bookTimeShow = bookTimeShow;
	}

	public boolean isBookFlag() {
		return bookFlag;
	}

	public void setBookFlag(boolean bookFlag) {
		this.bookFlag = bookFlag;
	}

	public void buildBaseData(OfflineActivity offlineActivity) {
		this.uuid = offlineActivity.getUuid();
		this.name = OptionalUtils.traceValue(offlineActivity, "name");
		this.address = OptionalUtils.traceValue(offlineActivity, "address");
	}
	
	public void buildListOfMy(OfflineActivity offlineActivity, int bookNum){
		this.buildBaseData(offlineActivity);
		this.wxcover = Tools.nullValueFilter(CastBanner.getImage(
				offlineActivity.getCover(), CastBanner.COVER_WX));
		OfflineActivity.OfflineActivityPrice activityPrice = OfflineActivity
				.priceForJsonObj(offlineActivity.getPrice());
		this.priceShow = buildPrice(activityPrice);
		this.bookNum=bookNum;
		this.personNum = buildPersonNum(offlineActivity);
		this.bookTimeShow = buildBookTime(offlineActivity);
		this.dateTime=Tools.formatYMDHMSDate(offlineActivity.getCreateDate());
	}

	public void buildDetail(OfflineActivity offlineActivity, int bookNum,
			User user) {
		this.buildListOfMy(offlineActivity, bookNum);
		this.webcover = Tools.nullValueFilter(CastBanner.getImage(
				offlineActivity.getCover(), CastBanner.COVER_TV));
		this.description = OptionalUtils.traceValue(offlineActivity,
				"description");
		this.buildIsInitiat(offlineActivity, user);
	}

	public void buildWxList(OfflineActivity offlineActivity) {
		this.buildBaseData(offlineActivity);
		this.dateTime=Tools.formatYMDHMSDate(offlineActivity.getCreateDate());
		this.wxcover = Tools.nullValueFilter(CastBanner.getImage(
				offlineActivity.getCover(), CastBanner.COVER_WX));
		this.desc=OptionalUtils.traceValue(offlineActivity, "mark");
	}

	public void buildWebList(OfflineActivity offlineActivity) {

	}

	public void buildIsBook(SignUpUser sign) {
		if (sign != null) {
			this.bookFlag = true;
		}
	}

	public void buildIsInitiat(OfflineActivity offlineActivity, User user) {
		this.initiatorFlag = false;
		int initiatId = OptionalUtils.traceInt(offlineActivity,
				"initiatorUser.id");
		int userId = OptionalUtils.traceInt(user, "id");
		if (initiatId > 0 && userId > 0) {
			if (initiatId == userId) {
				this.initiatorFlag = true;
			}
		}
	}

	public String buildPrice(OfflineActivity.OfflineActivityPrice activityPrice) {
		int price = OptionalUtils.traceInt(activityPrice, "price");
		int integral = OptionalUtils.traceInt(activityPrice, "integral");
		String showprice = null;
		if (price > 0) {
			showprice = "￥" + price;
		}
		if (integral > 0) {
			if (Tools.isBlank(showprice)) {
				showprice = "积分-" + integral;
			} else {
				showprice = showprice + "/" + "积分-" + integral;
			}
		}
		if (Tools.isBlank(showprice)) {
			showprice = "免费";
		}
		return showprice;
	}

	public String buildPersonNum(OfflineActivity offlineActivity) {
		String personNum = "";
		int limitNum = OptionalUtils.traceInt(offlineActivity, "limitNum");
		if (limitNum == 0) {
			personNum += "无限制";
		} else {
			personNum =String.valueOf(limitNum);
		}
		return personNum;
	}

	public String buildBookTime(OfflineActivity offlineActivity) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Date bookStart = offlineActivity.getBookStartDate();
		Date bookEnd = offlineActivity.getBookEndDate();
		String startTime = "";
		String endTime = "";
		if (bookStart != null) {
			startTime = df.format(bookStart);
		}
		if (bookEnd != null) {
			endTime = df.format(bookEnd);
		}
		if (Tools.isBlank(startTime) || Tools.isBlank(endTime)) {
			return "";
		}
		return startTime + "-" + endTime;
	}

}
