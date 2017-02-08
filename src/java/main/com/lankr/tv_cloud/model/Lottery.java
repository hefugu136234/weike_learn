package com.lankr.tv_cloud.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.lankr.tv_cloud.utils.Tools;

public class Lottery extends BaseModel {
	private static final long serialVersionUID = 3905514422789705243L;

	// 次数已经用完
	public static final int TIMES_USELESS = -1;

	// 未开始
	public static final int TIMES_NOT_START = -2;

	// 已经结束
	public static final int TIMES_GAME_OVER = -3;

	// 游戏无效
	public static final int TIMES_GAME_DISABLE = -4;
	
	// 参数无效
	public static final int TIMES_GAME_ERROR = -5;
	
	//游戏未上线
	public static final int TIMES_GAME_UNDERLINE = -6;

	public static final int DEFALUT_RANDOM_SEED = 100;

	private String name;
	private String pinyin;
	private Date beginDate;
	private Date endDate;
	private int joinTye;
	private int joinTimes;
	private int status;
	private String page;
	private int templateId;
	private String rules;
	private List<Award> awards;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getJoinTye() {
		return joinTye;
	}

	public void setJoinTye(int joinTye) {
		this.joinTye = joinTye;
	}

	public int getJoinTimes() {
		return joinTimes;
	}

	public void setJoinTimes(int joinTimes) {
		this.joinTimes = joinTimes;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
	
	public int getTemplateId() {
		return templateId;
	}
	
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getRules() {
		return rules;
	}
	
	public void setRules(String rules) {
		this.rules = rules;
	}
	
	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}

	// default lottery seed for creating a random number
	private transient long mLotterySeed = DEFALUT_RANDOM_SEED;

	// lottery awards
	private transient List<Award> tmps;
	
	public List<Award> lotteryAwards() {
		return tmps;
	}

	public void lotteryPrepare() {
		if (awards == null || awards.isEmpty()) {
			return;
		}
		tmps = new ArrayList<Award>(awards.size());
		double min_con = Double.MAX_VALUE;
		for (int i = 0; i < awards.size(); i++) {
			Award a = awards.get(i);
			if (awards.get(i).apiUseable()) {
				tmps.add(a);
				if (a.getConditional() > 0) {
					min_con = Math.min(min_con, a.getConditional());
				}
			}
		}
		// sort
		Collections.sort(tmps);
		if (min_con <= 1) {
			mLotterySeed = (long) (DEFALUT_RANDOM_SEED / min_con);
		}
		long t = 0;
		for (Award award : tmps) {
			t = award.awardZoneComputer(t, mLotterySeed);
		}
	}

	public Award play() {
		if (tmps == null || tmps.isEmpty())
			return null;
		Random rand = new Random();
		int lucky = rand.nextInt((int) (mLotterySeed % Integer.MAX_VALUE));
		for (Award award : tmps) {
			if (award.isWin(lucky)) {
				return award;
			}
		}
		return null;
	}

	public boolean isGameOver() {
		return Tools.getCurrentDate().after(getEndDate());
	}

	public boolean isNotBegin() {
		return Tools.getCurrentDate().before(getBeginDate());
	}
}
