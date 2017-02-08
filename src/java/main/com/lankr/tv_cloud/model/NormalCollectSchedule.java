package com.lankr.tv_cloud.model;

public class NormalCollectSchedule extends BaseModel {
	// studyStatus 0=初始 1=完成 2=学习中
	// checkStatus 0=未考核 1=已通过 2=未通过

	public final static int STUDY_INIT = 0;

	public final static int STUDY_ING = 2;

	public final static int STUDY_FINISH = 1;

	public final static int CHECK_INIT = 0;

	public final static int CHECK_UNPASS = 2;

	public final static int CHECK_PASS = 1;

	// referType 状态

	public final static int TYPE_COURSE = 1;//课程
	public final static int TYPE_CHAPTERS = 2;//章节
	public final static int TYPE_RESOURCE = 3;//资源
	public final static int TYPE_CHAPTERS_REFER=4;//章节考试的对应关系

	/**
	 * 
	 */
	private static final long serialVersionUID = 5156615184894262929L;

	private int status;

	private int learnSchedule;

	private int referType;

	private int referId;

	private int learnTime;

	private int userId;

	private int checkStatus;
	
	private int studyStatus;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getLearnSchedule() {
		return learnSchedule;
	}

	public void setLearnSchedule(int learnSchedule) {
		this.learnSchedule = learnSchedule;
	}

	public int getReferType() {
		return referType;
	}

	public void setReferType(int referType) {
		this.referType = referType;
	}

	public int getReferId() {
		return referId;
	}

	public void setReferId(int referId) {
		this.referId = referId;
	}

	public int getLearnTime() {
		return learnTime;
	}

	public void setLearnTime(int learnTime) {
		this.learnTime = learnTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}

	public int getStudyStatus() {
		return studyStatus;
	}

	public void setStudyStatus(int studyStatus) {
		this.studyStatus = studyStatus;
	}
	
	

}
