package com.lankr.tv_cloud.model;

import java.util.Date;

public class IntegralWeekReport {
	private int current;

	private int history;

	private Date maxCreateDate;

	private int userId;

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getHistory() {
		return history;
	}

	public void setHistory(int history) {
		this.history = history;
	}

	public Date getMaxCreateDate() {
		return maxCreateDate;
	}

	public void setMaxCreateDate(Date maxCreateDate) {
		this.maxCreateDate = maxCreateDate;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * 一段时间内的差值
	 * 正数为新增，负数为减少
	 */
	private int addition;

	public int getAddition() {
		return addition;
	}

	public void setAddition(int addition) {
		this.addition = addition;
	}

	//差值统计开始时间
	private Date stateStart;
	
	//差值统计结束时间
	private Date stateEnd;
}
