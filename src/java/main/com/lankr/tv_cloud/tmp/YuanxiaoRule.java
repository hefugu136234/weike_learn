package com.lankr.tv_cloud.tmp;

import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import com.lankr.tv_cloud.utils.Tools;

public class YuanxiaoRule {

	public static final float MONEY_50 = 50;

	public static final float MONEY_20 = 20;

	public static final float MONEY_10 = 10;

	public static final float MONEY_0 = 0;

	public static final String START_DATE = "2016-2-22 00:00:00";

	public static final String END_DATE = "2016-2-26 23:59:59";

	public static final int MONEY_50_MAX_TIMES_DAY = 5;

	public static final int MONEY_20_MAX_TIMES_DAY = 10;

	public static final int MONEY_10_MAX_TIMES_DAY = 20;

	public static final int USER_MAX_TIMES_DAY = 3;
	
	public static final int WIN_START_NUMBER = 70;

	public static int moneyMaxTimesPerDay(float money) {
		if (money == MONEY_50) {
			return MONEY_50_MAX_TIMES_DAY;
		} else if (money == MONEY_20) {
			return MONEY_20_MAX_TIMES_DAY;
		} else if (money == MONEY_10) {
			return MONEY_10_MAX_TIMES_DAY;
		}
		return 0;
	}

	public static float randomMoney(int seed) {
		int value = 1 + new Random().nextInt(seed);
		if (value > 95) {
			return MONEY_50;
		}
		if (value > 85 && value <= 95) {
			return MONEY_20;
		}
		if (value > WIN_START_NUMBER && value <= 85)
			return MONEY_10;

		return MONEY_0;
	}

	public static Date getStartDate() {
		try {
			return Tools.df1.parse(START_DATE);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Date getEndDate() {
		try {
			return Tools.df1.parse(END_DATE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String dateStart(Date date) {
		return Tools.formatYMDDate(date) + Tools.SPACE_STRING + "00:00:00";
	}

	public static String dateEnd(Date date) {
		return Tools.formatYMDDate(date) + Tools.SPACE_STRING + "23:59:59";
	}

}
