package com.lankr.tv_cloud.vo;

import com.lankr.tv_cloud.annotations.DataAlias;
import com.lankr.tv_cloud.model.User;

public class UserInfo {

	@DataAlias(column = "uuid")
	private String uuid;

	@DataAlias(column = "username")
	private String username;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@DataAlias(column = "nickname")
	private String nickname;

	private String avator;

	private boolean isSystemUser;

	public static UserInfo build(User user) {
		if (user == null)
			return null;
		UserInfo info = new UserInfo();
		info.nickname = user.getNickname();
		info.uuid = user.getUuid();
		try {
			// 是否是后台管理员
			info.isSystemUser = user.getMainRole().isSuperAdmin()
					|| user.getUserReference().getRole().isProUser();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!info.isSystemUser) {
			info.username = user.getUsername();
			info.avator = user.getAvatar();
		}

		return info;
	}

}
