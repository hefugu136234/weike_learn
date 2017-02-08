package com.lankr.orm.mybatis.mapper;

import com.lankr.tv_cloud.model.AppAuthentication;

public interface AppAuthenticationMapper {

	public void addAppAuthentication(AppAuthentication auth);

	public int disableAllAuth(int userId);

	public AppAuthentication searchAppAuth(String token);
}
