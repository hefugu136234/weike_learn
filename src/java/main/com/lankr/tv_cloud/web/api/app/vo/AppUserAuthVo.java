package com.lankr.tv_cloud.web.api.app.vo;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class AppUserAuthVo extends BaseAPIModel {

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private AppUser user;

	public void build(User model) {
		setStatus(Status.SUCCESS);
		if (model == null)
			return;
		user = new AppUser();
		user.id = model.getUuid();
		user.username = model.getUsername();
		user.nickname = model.getNickname();
		user.email = model.getEmail();
		user.address = model.getAddress();
		user.phone = model.getPhone();
		user.avatar = model.getAvatar();
	}

	private class AppUser {
		String id;
		String username;
		String nickname;
		String phone;
		String email;
		String address;
		String avatar;
	}
}
