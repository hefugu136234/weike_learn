package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.SignUpUser;

public interface SignUpUserMapper {

	public int addSignUpUser(SignUpUser signUpUser);

	public SignUpUser selectSignUpUserByUuid(String uuid);

	public int updateSignUpUserStatus(SignUpUser signUpUser);

	public SignUpUser selectSignUpUserByUser(@Param("referId") int referId,
			@Param("referType") int referType, @Param("userId") int userId);

	public List<SignUpUser> selectSignUpUserForTable(
			@Param("referId") int referId, @Param("referType") int referType,
			@Param("searchValue") String searchValue, @Param("from") int from,
			@Param("size") int size);

	public Integer bookCountUser(@Param("referId") int referId,
			@Param("referType") int referType);

	public List<SignUpUser> wxbookUserList(@Param("referId") int referId,
			@Param("referType") int referType,
			@Param("startTime") String startTime, @Param("size") int size);

	public Integer countBookOfModelByUser(@Param("status") int status,
			@Param("referType") int referType, @Param("userId") int userId);
	
	public List<SignUpUser> webbookUserList(@Param("referId") int referId,
			@Param("referType") int referType,
			@Param("from") int from, @Param("size") int size);

}
