package com.lankr.tv_cloud.web.api.webchat.vo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.CastBanner;
import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.util.BroadcastPaltUrl;

public class WxBroadcastItem{
	
	/**
	 * 1.报名未开始 2.报名进行中 3.报名已结束 4.直播进行中 5.直播结束，无录播 6.直播结束有录播
	 */
	public static final int cast_book_no_start = 1, cast_book_ing = 2,
			cast_book_end = 3, cast_ing = 4,cast_no_record=5,cast_record=6;// 我要参加不可以点

	private String uuid;

	private String name;

	private int remainDays;// 距离结束的时间

	private int bookNum;

	private String dateTime;

	private String desc;

	private String endTime;

	private String cover;

	private String bg;

	private String description;

	private String liveUrl;

	private String date;

	private String sTime;

	private String eTime;

	private String dateWeek;

	private int castLimit;

	private long countDownStart;

	private int integral;

	private String integralVal;

	private int buttonType;

	private boolean needPinCode;

	private String banner;
	
	private String baiduAk;
	
	private String baiduUrl;
	
	
	
	

	public String getBaiduUrl() {
		return baiduUrl;
	}

	public void setBaiduUrl(String baiduUrl) {
		this.baiduUrl = baiduUrl;
	}

	public String getBaiduAk() {
		return baiduAk;
	}

	public void setBaiduAk(String baiduAk) {
		this.baiduAk = baiduAk;
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

	public int getRemainDays() {
		return remainDays;
	}

	public void setRemainDays(int remainDays) {
		this.remainDays = remainDays;
	}

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getBg() {
		return bg;
	}

	public void setBg(String bg) {
		this.bg = bg;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLiveUrl() {
		return liveUrl;
	}

	public void setLiveUrl(String liveUrl) {
		this.liveUrl = liveUrl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getDateWeek() {
		return dateWeek;
	}

	public void setDateWeek(String dateWeek) {
		this.dateWeek = dateWeek;
	}

	public int getCastLimit() {
		return castLimit;
	}

	public void setCastLimit(int castLimit) {
		this.castLimit = castLimit;
	}

	public long getCountDownStart() {
		return countDownStart;
	}

	public void setCountDownStart(long countDownStart) {
		this.countDownStart = countDownStart;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getIntegralVal() {
		return integralVal;
	}

	public void setIntegralVal(String integralVal) {
		this.integralVal = integralVal;
	}

	public int getButtonType() {
		return buttonType;
	}

	public void setButtonType(int buttonType) {
		this.buttonType = buttonType;
	}

	public boolean isNeedPinCode() {
		return needPinCode;
	}

	public void setNeedPinCode(boolean needPinCode) {
		this.needPinCode = needPinCode;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public void buildBaseData(Broadcast broadcast) {
		this.setUuid(broadcast.getUuid());
		this.setName(OptionalUtils.traceValue(broadcast, "name"));
	}

	public void buildListData(Broadcast broadcast) {
		buildBaseData(broadcast);
		this.setDateTime(Tools.formatYMDHMSDate(broadcast.getCreateDate()));
		this.setEndTime(Tools.formatYMDHMSDate(broadcast.getEndDate()));
		String desc = OptionalUtils.traceValue(broadcast, "mark");
		this.setDesc(desc);
		this.setCover(Tools.nullValueFilter(CastBanner.getImage(
				broadcast.getBanner(), CastBanner.BANNER_WX)));
	}
	
	public void buildWxSendMessage(Broadcast broadcast) {
		buildBaseData(broadcast);
		String desc = OptionalUtils.traceValue(broadcast, "mark");
		this.setDesc(desc);
		this.setBanner(Tools.nullValueFilter(CastBanner.getImage(
				broadcast.getBanner(), CastBanner.BANNER_WX)));
	}
	
	public void buildBaiduLive(Broadcast broadcast){
		buildBaseData(broadcast);
		buildShare(broadcast);
		this.setBaiduAk(Config.baidu_live_ak);
		this.setBaiduUrl(OptionalUtils.traceValue(broadcast, "castAction"));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.setsTime(dateFormat.format(broadcast.getStartDate()));
	}

	public void buildRemainDay(Date nowDate, Date endDate) {
		String endTime = Tools.formatYMDDate(endDate);
		endTime = endTime + " 23:59:59";
		try {
			endDate = Tools.df1.parse(endTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time = endDate.getTime() - nowDate.getTime();
		int day = (int) (TimeUnit.MILLISECONDS.toDays(time));
		this.setRemainDays(day);
	}

	public void buildBook(int book) {
		this.setBookNum(book);
	}

	public void buildShare(Broadcast broadcast) {
		this.setCover(Tools.nullValueFilter(CastBanner.getImage(
				broadcast.getBanner(), CastBanner.COVER_WX)));
		String mark = OptionalUtils.traceValue(broadcast, "mark");
		this.setDesc(mark);
	}

	public void buildFrist(Broadcast broadcast, int current,
			IntegralConsume consume) {
		buildBaseData(broadcast);
		buildShare(broadcast);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.setDate(dateFormat.format(broadcast.getStartDate()));
		this.setDateWeek(getWeekOfDate(broadcast.getStartDate()));
		dateFormat = new SimpleDateFormat("HH:mm");
		this.setsTime(dateFormat.format(broadcast.getStartDate()));
		this.seteTime(dateFormat.format(broadcast.getEndDate()));
		// 积分消耗
		if (consume == null) {
			this.setIntegral(0);
		} else {
			this.setIntegral(consume.getIntegral());
		}

		if (broadcast.getCastType() == Broadcast.PINGCODE_TYPE) {
			this.setNeedPinCode(true);
		} else {
			this.setNeedPinCode(false);
		}
		this.setCastLimit(broadcast.getLimitNum());
		this.buildBook(current);
		/**
		 * 判断按钮的类型
		 */
		int join = judyButtonType(broadcast);
		this.setButtonType(join);
		this.setCountDownStart(0);
		if (join<=cast_book_ing) {
			Date nowDate = new Date();
				long timeSends = broadcast.getBookEndDate().getTime()
						- nowDate.getTime();
				if (timeSends > 0) {
					timeSends = TimeUnit.MILLISECONDS.toSeconds(timeSends);
					this.setCountDownStart(timeSends);
				}
		}
	}
	
	public int judyButtonType(Broadcast broadcast) {
		Date nowDate = new Date();
		Date bookDateStart = broadcast.getBookStartDate();
		if(nowDate.before(bookDateStart)){
			//报名未开始
			return cast_book_no_start;
		}
		Date bookDateEnd=broadcast.getBookEndDate();
		if(nowDate.before(bookDateEnd)){
			return cast_book_ing;
		}
		Date startDate=broadcast.getStartDate();
		if(nowDate.before(startDate)){
			return cast_book_end;
		}
		Date dateEnd = broadcast.getEndDate();
		if (nowDate.before(dateEnd)) {
			return cast_ing;
		}
		if(broadcast.getResource()==null){
			return cast_no_record;
		}
		return cast_record;
	}

	public void buildDetail(Broadcast broadcast, User user) {
		buildBaseData(broadcast);
		buildShare(broadcast);
		this.setDescription(OptionalUtils.traceValue(broadcast, "description"));
		this.setBg(Tools.nullValueFilter(CastBanner.getImage(
				broadcast.getBanner(), CastBanner.BG_WX)));
		String liveUrl = BroadcastPaltUrl.platCastEntrance(user, broadcast,BroadcastPaltUrl.WX_PLAT);
		this.setLiveUrl(Tools.nullValueFilter(liveUrl));
	}

	/**
	 * 计算当前日期是周几
	 * 
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	public static void main(String[] args) {
		String v1 = "2016-04-22 09:05:06";
		String v2 = "2016-04-22 10:05:06";
		try {
			Date date1 = Tools.df1.parse(v1);
			Date date2 = Tools.df1.parse(v2);
			long time = date1.getTime() - date2.getTime();
			System.out.println(TimeUnit.MILLISECONDS.toDays(time));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
