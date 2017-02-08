package com.lankr.tv_cloud.model;

import java.util.Date;
import java.util.List;

public class User extends BaseModel {

	private static final long serialVersionUID = -8294068531086996325L;

	private String username;
	private String password;
	private String nickname;
	private String pinyin;
	private String phone;
	private String email;
	private String address;
	private String company;
	private String avatar;

	private Role mainRole;
	private List<UserReference> user_reference;
	private UserExpand userExpand;
	private transient UserReference handlerReference;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setMainRole(Role mainRole) {
		this.mainRole = mainRole;
	}

	public Role getMainRole() {
		return mainRole;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Role getProUserRole() {
		if (handlerReference != null) {
			return handlerReference.getRole();
		}
		if (user_reference == null || user_reference.isEmpty())
			return null;

		return user_reference.get(0).getRole();
	}

	public UserReference getUserReference() {
		if (user_reference == null || user_reference.isEmpty())
			return null;
		return user_reference.get(0);
	}

	public List<UserReference> getUser_reference() {
		return user_reference;
	}

	public void setUser_reference(List<UserReference> user_reference) {
		this.user_reference = user_reference;
	}

	public Project getStubProject() {
		if (handlerReference != null) {
			Role r = handlerReference.getRole();
			if (r != null && r.isProUser()) {
				return handlerReference.getProject();
			}
		}
		return null;
	}

	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年7月5日
	 * @modifyDate 2016年7月5日
	 * vip权限日期
	 */
	public Date vipDateTo() {
		UserReference ur = getUserReference();
		if (ur != null) {
			return ur.getValidDate();
		}
		return null;
	}

	public UserReference getHandlerReference() {
		return handlerReference;
	}

	public void setHandlerReference(UserReference handlerReference) {
		this.handlerReference = handlerReference;
	}

	public UserExpand getUserExpand() {
		return userExpand;
	}

	public void setUserExpand(UserExpand userExpand) {
		this.userExpand = userExpand;
	}
}
