package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.SignUpUser;
import com.lankr.tv_cloud.model.User;

public interface SignUpUserFacade {

	public ActionMessage<?> addSignUpUser(SignUpUser signUpUser);

	public SignUpUser selectSignUpUserByUuid(String uuid);

	public ActionMessage<?> updateSignUpUserStatus(SignUpUser signUpUser);

	public SignUpUser selectSignUpUserByUser(int referId, int referType,
			User user);

	public Pagination<SignUpUser> selectSignUpUserForTable(int referId, int referType,
			String searchValue, int from, int size);
	
	public int bookCountUser(int referId, int referType);
	
	public List<SignUpUser> wxbookUserList(int referId, int referType,String startTime,int size);
	
	//pc端获取线报名
	public Pagination<SignUpUser> webbookUserList(int referId, int referType,int from,int size);
	
	public int bookLineActivityOfUser(int status,User user);

}
