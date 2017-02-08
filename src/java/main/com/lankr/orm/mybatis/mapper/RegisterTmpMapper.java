package com.lankr.orm.mybatis.mapper;

import com.lankr.tv_cloud.model.RegisterTmp;

public interface RegisterTmpMapper {

	public void addRegisterTmp(RegisterTmp register);

	public RegisterTmp getLatestRegisterByPhoneNumber(int type, String mobile);

	public RegisterTmp getRegisterTmpByUuid(String uuid);

	public RegisterTmp searchRegisterTmpBycode(String code, String mobile,
			int type);
	
	public int updateRegisterTmp(RegisterTmp register);
}
