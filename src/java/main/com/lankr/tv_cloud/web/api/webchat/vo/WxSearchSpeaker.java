package com.lankr.tv_cloud.web.api.webchat.vo;


public class WxSearchSpeaker {
	
	private String avatar;

	private String position;

	private String resume;

	private WxSearchHospital hospital;
	
	private String uuid ;
	
	private String name ;
	
	private long createTime;

	private long modifyTime;
	// 状态码
	private int _status;

	private String mark;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public WxSearchHospital getHospital() {
		return hospital;
	}

	public void setHospital(WxSearchHospital hospital) {
		this.hospital = hospital;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public int get_status() {
		return _status;
	}

	public void set_status(int _status) {
		this._status = _status;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
