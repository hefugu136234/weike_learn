package com.lankr.orm.mybatis.mapper;

import com.lankr.tv_cloud.model.UserExpand;

public interface UserExpandMapper {
	
	public int addUserExpand(UserExpand userExpand);
	
	public int updateUserExpand(UserExpand userExpand);
	
	public UserExpand selectUserExpandById(int userId);
	
	public int updateReceiptExpand(UserExpand userExpand);
	
	public int updateSexExpand(UserExpand userExpand);

}
