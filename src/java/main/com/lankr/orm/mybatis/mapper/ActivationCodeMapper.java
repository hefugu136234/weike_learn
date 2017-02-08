package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.ActivationCode;

public interface ActivationCodeMapper {

	List<ActivationCode> getByUserId(int userId);

}
