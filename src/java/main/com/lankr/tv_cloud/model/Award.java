package com.lankr.tv_cloud.model;

public class Award extends BaseModel implements Comparable<Award> {

	private static final long serialVersionUID = -4081919122553104725L;

	public static final int AWARD_LOSS = -1;

	private String name;
	private Lottery lottery;
	private int number;
	private int maxWinTimes;
	private int status;
	private double conditional;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Lottery getLottery() {
		return lottery;
	}

	public void setLottery(Lottery lottery) {
		this.lottery = lottery;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getMaxWinTimes() {
		return maxWinTimes;
	}

	public void setMaxWinTimes(int maxWinTimes) {
		this.maxWinTimes = maxWinTimes;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getConditional() {
		return conditional;
	}

	public void setConditional(double conditional) {
		this.conditional = conditional;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Award o) {
		if (conditional == o.getConditional()) {
			return o.getId() - getId();
		}
		double d = o.getConditional() - conditional;
		if (d < 0) {
			return -1;
		}
		return 1;
	}

	//
	private transient long min_zone;

	private transient long max_zone;

	public long awardMinZone() {
		return min_zone;
	}

	public long awardMaxZone() {
		return max_zone;
	}

	/**
	 * @author Kalean.Xiang
	 * @createDate: 2016年4月15日
	 * @modifyDate: 2016年4月15日 return max_zone
	 */
	long awardZoneComputer(long begin_zone, long lotterySeed) {
		begin_zone = Math.max(0, begin_zone);
		if (begin_zone >= lotterySeed || conditional <= 0) {
			min_zone = AWARD_LOSS;
			max_zone = AWARD_LOSS;
		} else {
			min_zone = begin_zone;
			max_zone = min_zone
					+ Math.round(conditional * lotterySeed
							/ Lottery.DEFALUT_RANDOM_SEED);
		}
		return max_zone;
	}

	public boolean isWin(int luckyNumber) {
		System.out.println(name + " : [" + min_zone + "," + max_zone + "]");
		return luckyNumber >= min_zone && luckyNumber < max_zone;
	}
}
