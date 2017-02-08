package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import com.lankr.orm.mybatis.mapper.SignUpUserMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.SignUpUserFacade;
import com.lankr.tv_cloud.model.SignUpUser;
import com.lankr.tv_cloud.model.User;

public class SignUpUserFacadeImp extends FacadeBaseImpl implements
		SignUpUserFacade {

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return SignUpUserMapper.class.getName();
	}

	@Override
	public ActionMessage<?> addSignUpUser(SignUpUser signUpUser) {
		// TODO Auto-generated method stub
		try {
			int effect = signUpUserMapper.addSignUpUser(signUpUser);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("添加报名人员失败", e);
		}
		return ActionMessage.failStatus("添加报名人员失败");
	}

	@Override
	public SignUpUser selectSignUpUserByUser(int referId, int referType,
			User user) {
		// TODO Auto-generated method stub
		if (user == null) {
			return null;
		}
		return signUpUserMapper.selectSignUpUserByUser(referId, referType,
				user.getId());
	}

	@Override
	public SignUpUser selectSignUpUserByUuid(String uuid) {
		// TODO Auto-generated method stub
		return signUpUserMapper.selectSignUpUserByUuid(uuid);
	}

	@Override
	public Pagination<SignUpUser> selectSignUpUserForTable(int referId,
			int referType, String searchValue, int from, int size) {
		// TODO Auto-generated method stub
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(s.id) from signup_user s left join user u on s.userId=u.id where s.isActive=1 and s.referId="
				+ referId
				+ " and s.referType="
				+ referType
				+ " and (u.username like '%"
				+ searchValue
				+ "%' or u.nickname like '%"
				+ searchValue
				+ "%' or u.phone like '%" + searchValue + "%')";
		Pagination<SignUpUser> pagination = initPage(sql, from, size);
		List<SignUpUser> list = signUpUserMapper.selectSignUpUserForTable(
				referId, referType, searchValue, from, size);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public ActionMessage<?> updateSignUpUserStatus(SignUpUser signUpUser) {
		// TODO Auto-generated method stub
		try {
			int effect = signUpUserMapper.updateSignUpUserStatus(signUpUser);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("修改报名人员失败", e);
		}
		return ActionMessage.failStatus("修改报名人员失败");
	}

	@Override
	public int bookCountUser(int referId, int referType) {
		// TODO Auto-generated method stub
		Integer count = signUpUserMapper.bookCountUser(referId, referType);
		if (count == null) {
			return 0;
		}
		return count;
	}

	@Override
	public List<SignUpUser> wxbookUserList(int referId, int referType,
			String startTime, int size) {
		// TODO Auto-generated method stub
		return signUpUserMapper.wxbookUserList(referId, referType, startTime,
				size);
	}

	@Override
	public int bookLineActivityOfUser(int status, User user) {
		// TODO Auto-generated method stub
		if (user == null) {
			return 0;
		}
		Integer count = signUpUserMapper.countBookOfModelByUser(status,
				SignUpUser.REFER_OFFLINEACTIVITY, user.getId());
		if (count == null) {
			return 0;
		}
		return count;
	}

	@Override
	public Pagination<SignUpUser> webbookUserList(int referId, int referType,
			int from, int size) {
		// TODO Auto-generated method stub
		String sql = "select count(id) from signup_user where isActive=1 and referId="
				+ referId + " and referType=" + referType;
		Pagination<SignUpUser> pagination = initPage(sql, from, size);
		List<SignUpUser> list = signUpUserMapper.webbookUserList(referId,
				referType, from, size);
		pagination.setResults(list);
		return pagination;
	}

}
