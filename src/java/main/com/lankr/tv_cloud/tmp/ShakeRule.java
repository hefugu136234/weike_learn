/** 
 *  @author Kalean.Xiang
 *  @createDate: 2016年3月22日
 *  
 */
package com.lankr.tv_cloud.tmp;

import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import com.lankr.tv_cloud.utils.Tools;

public final class ShakeRule {

	public static final int LUCKY_NUMBER = 8;

//	public static Config[] configs = { new Config(LUCKY_NUMBER, "3.26摇一摇活动",
//			"2016-03-26 07:30:00", "2016-03-27 18:00:00", 3,
//			new Award[] { new Award(LUCKY_NUMBER, 20, 1) }) };
	
	public static Config[] configs = { new Config(LUCKY_NUMBER, "北京全国儿科哮喘变态反应学术研讨会",
	"2016-04-21 00:00:00", "2016-04-23 23:59:59", 3,
	new Award[] { new Award(LUCKY_NUMBER, 3, 1) }) };

	// new Config(188, "摇一摇活动二", "2016-3-22 12:00:00",
	// "2016-3-22 23:59:59", 10, new Award[] { new Award(
	// LUCKY_NUMBER, 20, 1) })

	public static class Shaker {

		public float money;

		public int code = YuanxiaoActive.CODE_DISABLE;

		public String mark;

		public int times;

		public transient Config config;

		public boolean isOk() {
			return code == YuanxiaoActive.CODE_OK;
		}

	}

	public static Shaker shake(int seed) {
		Shaker shake = new Shaker();
		for (int i = 0; i < configs.length; i++) {
			Config conf = configs[i];
			shake.code = conf.code();
			if (shake.isOk()) {
				int value = 1 + new Random().nextInt(seed);
				System.out.println(value);
				if (value == conf.lucky) {
					shake.money = value;
				}
				shake.mark = conf.mark;
				shake.config = conf;
				return shake;
			}
		}
		return shake;
	}

	public static class Config {

		public Config(int lucky, String mark, String dateStart, String endDate,
				int max_times, Award... awards) {
			this.mark = mark;
			this.dateStart = dateStart;
			this.endDate = endDate;
			this.max_times = max_times;
			this.awards = awards;
			this.lucky = lucky;
		}

		public final int lucky;

		public final String mark;

		public final String dateStart;

		public final String endDate;

		// 用户抽奖次数
		public final int max_times;

		public final Award[] awards;

		int code() {
			if (Tools.getCurrentDate().before(getStartDate())) {
				return YuanxiaoActive.CODE_NOT_START;
			}
			if (Tools.getCurrentDate().after(getEndDate())) {
				return YuanxiaoActive.CODE_OVER;
			}
			return YuanxiaoActive.CODE_OK;

		}

		public Date getStartDate() {
			try {
				return Tools.df1.parse(dateStart);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}

		public Date getEndDate() {
			try {
				return Tools.df1.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}

		public int getMaxTimes(float money) {
			if (awards == null)
				return 0;
			for (int i = 0; i < awards.length; i++) {
				Award a = awards[i];
				if (a.money == money) {
					return a.max_times;
				}
			}
			return 0;
		}

		public int getMaxWins(float money) {
			if (awards == null)
				return 0;
			for (int i = 0; i < awards.length; i++) {
				Award a = awards[i];
				if (a.money == money) {
					return a.max_wins;
				}
			}
			return 0;
		}
	}

	public static class Award {
		public Award(float money, int max_times, int max_wins) {
			super();
			this.money = money;
			this.max_times = max_times;
			this.max_wins = max_wins;
		}

		public final float money;
		// 奖金的数量
		public final int max_times;

		// 用户最多能抽中的次数
		public final int max_wins;
	}
	
}
