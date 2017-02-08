package com.lankr.tv_cloud.model;

public class Role extends BaseModel {
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	private int level;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	private String roleDesc;

	public static final int NULL = 0;

	// 超级管理员
	public static final int SUPER_ADMIN_LEVEL = 1;

	// 项目管理员
	public static final int PRO_ADMIN = 2;

	// 项目编辑人员
	public static final int PRO_EDITOR = 3;

	// 项目用户的标识
	public static final int PRO_USER_LEVEL = 4;

	// 项目客户端的用户（盒子的用户）
	public static final int PRO_CLIENT = 5;

	// 普通注册用户
	public static final int PRO_CUSTOMER = 6;

	public boolean isProAdmin() {
		return isSuperAdmin() || getLevel() == PRO_ADMIN;
	}

	public boolean isProUser() {
		return PRO_USER_LEVEL >= getLevel() && getLevel() > SUPER_ADMIN_LEVEL;
	}

	public boolean isSuperAdmin() {
		return SUPER_ADMIN_LEVEL == getLevel();
	}

	public boolean isCustomer() {
		return PRO_CUSTOMER == getLevel();
	}

	public boolean isProClient() {
		return PRO_CLIENT == getLevel();
	}

	public boolean isBoxUser(){
		return isProClient() || isCustomer();
	}
}
