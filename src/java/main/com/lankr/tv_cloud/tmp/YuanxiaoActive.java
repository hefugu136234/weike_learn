package com.lankr.tv_cloud.tmp;

public class YuanxiaoActive {

	public transient static final int CODE_OK = 0;
	public transient static final int CODE_DISABLE = 1;
	public transient static final int CODE_NOT_START = 2;
	public transient static final int CODE_OVER = 3;

	private int times;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	private float money;

	private int code; // 0正常，，1、次数已经用完 2、未开始 3，已结束

	public boolean isOk() {
		return code == CODE_OK;
	}

}
