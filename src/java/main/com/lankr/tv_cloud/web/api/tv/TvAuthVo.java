package com.lankr.tv_cloud.web.api.tv;

import java.util.Date;

import com.lankr.tv_cloud.model.TvAuthentication;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class TvAuthVo extends BaseAPIModel {

	private String token;

	private String username;

	private String nickname;

	private Date createdDate;

	private String avatar;

	private Date validDate;
	
	private long validDateLong;

	public long getValidDateLong() {
		return validDateLong;
	}

	public void setValidDateLong(long validDateLong) {
		this.validDateLong = validDateLong;
	}

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	private String deadline;

	void parse(TvAuthentication at) {
		if (at == null)
			return;
		token = at.getToken();
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
}
