/** 
 *  @author Kalean.Xiang
 *  @createDate: 2016年4月15日
 * 	@modifyDate: 2016年4月15日
 *  
 */
package com.lankr.tv_cloud.facade.po;

import com.lankr.tv_cloud.model.Award;

/**
 * @author Kalean.Xiang
 *
 */
public class AwardResult {

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public Award getAward() {
		return award;
	}

	public void setAward(Award award) {
		this.award = award;
	}

	// 剩余次数
	private int times;

	// 抽取到的奖品
	private Award award;

	public boolean isOk(){
		return times >= 0;
	}
}
