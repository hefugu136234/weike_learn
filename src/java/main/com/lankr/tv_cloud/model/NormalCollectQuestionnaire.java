package com.lankr.tv_cloud.model;

public class NormalCollectQuestionnaire extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2891570773782590222L;

	public final static int TYPE_NORMALCOLLECT = 1;
	
	private int status;
	
	private int normalCollectId;
	
	private Questionnaire questionnaire;
	
	private int type;
	
	private int position;
	
	private String name;
	
	private String cover;
	
	private int collectNum;
	
	private int collectTime;
	
	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getNormalCollectId() {
		return normalCollectId;
	}

	public void setNormalCollectId(int normalCollectId) {
		this.normalCollectId = normalCollectId;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(int collectNum) {
		this.collectNum = collectNum;
	}

	public int getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(int collectTime) {
		this.collectTime = collectTime;
	}
	
	

}
