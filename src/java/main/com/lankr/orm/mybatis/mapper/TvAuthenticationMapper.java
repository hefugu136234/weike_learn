package com.lankr.orm.mybatis.mapper;

import com.lankr.tv_cloud.model.TvAuthentication;

public interface TvAuthenticationMapper {

	public void addTvAuth(TvAuthentication auth);

	public int disableUserAuths(TvAuthentication tvauth);

	public TvAuthentication getTvAuthByToken(String token);
}
