package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.ApplicableRecords;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class VipInfo extends BaseAPIModel {

	private int showPage;// 显示页面的div 0=未实名 1=未申请 2=已申请

	private int showProcess;

	private String deadTime;// vip的截止时间

	public int getShowPage() {
		return showPage;
	}

	public void setShowPage(int showPage) {
		this.showPage = showPage;
	}

	public int getShowProcess() {
		return showProcess;
	}

	public void setShowProcess(int showProcess) {
		this.showProcess = showProcess;
	}

	public String getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(String deadTime) {
		this.deadTime = deadTime;
	}

	public void buildPage(boolean flag, ApplicableRecords applicableRecords) {
		if (!flag) {
			this.setShowPage(0);
			return;
		}
		if (applicableRecords == null) {
			this.setShowPage(1);
			return;
		}
		this.setShowPage(2);
	}

	public void buildVipStatus(ApplicableRecords applicableRecords,
			String isDateValid) {
//		public static final int INIT_STATUS=0;//初始状态
//		
//		public static final int CHECK_STATUS=1;//已审核
//		
//		public static final int SEND_STATUS=2;//已发货，激活状态另行判断
		if (this.getShowPage() == 2) {
			//显示home页
			this.showProcess = 0;
			if (isDateValid.equals("no_use")) {
				// 从未激活
				this.showProcess = applicableRecords.getStatus();
			} else if (isDateValid.equals("in_use")) {
				// 激活中
				this.showProcess = 3;
			} else if (isDateValid.equals("late_use")) {
				// 已过期，激活过
				this.showProcess = 2;
			}
		}
	}

}
