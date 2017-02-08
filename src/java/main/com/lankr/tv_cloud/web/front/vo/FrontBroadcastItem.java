package com.lankr.tv_cloud.web.front.vo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.CastBanner;
import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.util.BroadcastPaltUrl;

public class FrontBroadcastItem {

	private String uuid;

	private String name;

	private int remainDays;// 距离结束时间

	private String remainDesc;

	private int bookNum;

	private String endDate;

	private String cover;

	private String buttonVal;

	private long countDownStart;

	private int integral;

	private String integralVal;

	private String bgUrl;

	private int limitCount;

	private String limitCountVal;

	private String description;

	private String baiduAk;

	private String baiduUrl;

	private String sTime;
	
	private String liveUrl;
	
	private String desc;
	
	
	

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLiveUrl() {
		return liveUrl;
	}

	public void setLiveUrl(String liveUrl) {
		this.liveUrl = liveUrl;
	}

	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public String getBaiduAk() {
		return baiduAk;
	}

	public void setBaiduAk(String baiduAk) {
		this.baiduAk = baiduAk;
	}

	public String getBaiduUrl() {
		return baiduUrl;
	}

	public void setBaiduUrl(String baiduUrl) {
		this.baiduUrl = baiduUrl;
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

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRemainDesc() {
		return remainDesc;
	}

	public void setRemainDesc(String remainDesc) {
		this.remainDesc = remainDesc;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getButtonVal() {
		return buttonVal;
	}

	public void setButtonVal(String buttonVal) {
		this.buttonVal = buttonVal;
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

	public String getBgUrl() {
		return bgUrl;
	}

	public void setBgUrl(String bgUrl) {
		this.bgUrl = bgUrl;
	}

	public int getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	public String getLimitCountVal() {
		return limitCountVal;
	}

	public void setLimitCountVal(String limitCountVal) {
		this.limitCountVal = limitCountVal;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		if (day > 0) {
			this.setRemainDesc("剩余" + day + "天");
		} else {
			this.setRemainDesc("今日开始");
		}
	}

	public void buildBook(int book) {
		this.setBookNum(book);
	}

	public void buildBaiduLive(Broadcast broadcast) {
		this.setUuid(broadcast.getUuid());
		this.setName(OptionalUtils.traceValue(broadcast, "name"));
		this.setBaiduAk(Config.baidu_live_ak);
		this.setBaiduUrl(OptionalUtils.traceValue(broadcast, "castAction"));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.setsTime(dateFormat.format(broadcast.getStartDate()));
		String mark = OptionalUtils.traceValue(broadcast, "mark");
		this.setDesc(mark);
	}

	public void buildCommonData(Broadcast broadcast) {
		this.setUuid(broadcast.getUuid());
		this.setName(OptionalUtils.traceValue(broadcast, "name"));
		this.setCover(Tools.nullValueFilter(CastBanner.getImage(
				broadcast.getBanner(), CastBanner.BANNER_TV)));
		this.setEndDate(Tools.formatYMDHMSDate(broadcast.getEndDate()));
	}

	public void buildDetail(Broadcast broadcast, int bookCount,
			IntegralConsume consume) {
		this.setUuid(broadcast.getUuid());
		this.setName(OptionalUtils.traceValue(broadcast, "name"));
		this.setBgUrl(Tools.nullValueFilter(CastBanner.getImage(
				broadcast.getBanner(), CastBanner.BG_TV)));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		this.setEndDate(dateFormat.format(broadcast.getEndDate()));

		// 积分消耗
		String integralVal = "免费";
		int integral = OptionalUtils.traceInt(consume, "integral");
		if (integral > 0) {
			integralVal = integral + "积分";
		}
		this.setIntegralVal(integralVal);

		int limitCount = OptionalUtils.traceInt(broadcast, "limitNum");
		this.setLimitCount(limitCount);
		String limitCountVal = "";
		if (limitCount == 0) {
			limitCountVal = "无限制";
		} else {
			limitCountVal = limitCount + "人";
		}
		this.setLimitCountVal(limitCountVal);

		this.setBookNum(bookCount);

		long countDown = 0;
		Date nowDate = new Date();

		String buttonVal = "";
		if (nowDate.before(broadcast.getEndDate())) {
			buttonVal = "观看直播";
			long timeSends = broadcast.getEndDate().getTime()
					- nowDate.getTime();
			if (timeSends > 0) {
				countDown = TimeUnit.MILLISECONDS.toSeconds(timeSends);
			}
		} else {
			Resource resource = broadcast.getResource();
			if (resource == null) {
				buttonVal = "直播已结束";
			} else {
				buttonVal = "观看录播";
			}
		}
		this.setButtonVal(buttonVal);

		this.setCountDownStart(countDown);

		this.setDescription(OptionalUtils
				.traceValue(broadcast, "tvDescription"));
	}

	public void buildTvData(Broadcast broadcast, int current,
			IntegralConsume consume,User user) {
		buildDetail(broadcast,current,consume);
		String liveUrl = BroadcastPaltUrl.platCastEntrance(user, broadcast,BroadcastPaltUrl.TV_PLAT);
		this.setLiveUrl(Tools.nullValueFilter(liveUrl));
		
	}

	public static void main(String[] args) {
		Date date = new Date();
		String val1 = "2016-05-29 15:20:23";
		String val2 = "2016-05-31 23:59:59";
		try {
			System.out.println(Tools.formatYMDDate(date));
			Date date1 = Tools.df1.parse(val1);
			Date date2 = Tools.df1.parse(val2);
			long time = date2.getTime() - date1.getTime();
			int day = 24 * 60 * 60 * 1000;
			System.out.println(day);
			int days = (int) (time % day);
			System.out.println(days);
			System.out.println(TimeUnit.MILLISECONDS.toDays(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
