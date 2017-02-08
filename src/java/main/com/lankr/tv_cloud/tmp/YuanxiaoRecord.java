package com.lankr.tv_cloud.tmp;

import com.lankr.tv_cloud.annotations.DataAlias;

public class YuanxiaoRecord {

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	@DataAlias(column = "money")
	private float money;

	@DataAlias(column = "createDate", from = java.util.Date.class, target = Long.class)
	private long date;
}
