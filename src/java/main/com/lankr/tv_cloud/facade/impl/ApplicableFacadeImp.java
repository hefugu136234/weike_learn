package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import com.lankr.tv_cloud.facade.ApplicableFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ApplicableRecords;
import com.lankr.tv_cloud.model.InvitcodeRecord;

public class ApplicableFacadeImp extends FacadeBaseImpl implements ApplicableFacade{

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "applicable";
	}

	@Override
	public Status addApplicable(ApplicableRecords applicableRecords) {
		// TODO Auto-generated method stub
		try {
			applicableDao.add(applicableRecords, getSqlAlias("addApplicable"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("add applicable_records occur an error", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateApplicableStatus(ApplicableRecords applicableRecords) {
		// TODO Auto-generated method stub
		try {
			applicableDao.update(applicableRecords, getSqlAlias("updateApplicableStatus"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("update applicable_records status occur an error", e);
		}
		return Status.FAILURE;
	}

	@Override
	public ApplicableRecords selectApplicableByUuid(String uuid) {
		// TODO Auto-generated method stub
		return applicableDao.getById(uuid, getSqlAlias("selectApplicableByUuid"));
	}
	
	@Override
	public ApplicableRecords selectApplicableByUserId(int userId) {
		// TODO Auto-generated method stub
		return applicableDao.getById(userId, getSqlAlias("selectApplicableByUserId"));
	}

	@Override
	public Pagination<ApplicableRecords> selectApplicableList(
			String searchValue, int from, int pageItemTotal) {
		// TODO Auto-generated method stub
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from applicable_records where isActive=1 and (applyName like '%"
				+ searchValue + "%' or pingYin like '%" + searchValue + "%' or mobile like"
						+ " '%"+searchValue +"%')";
		Pagination<ApplicableRecords> pagination = initPage(sql, from, pageItemTotal);
		List<ApplicableRecords> list = applicableDao.searchAllPagination(
				getSqlAlias("selectApplicableList"), searchValue, from,
				pageItemTotal);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public ApplicableRecords selectApplicableById(int id) {
		// TODO Auto-generated method stub
		return applicableDao.getById(id, getSqlAlias("selectApplicableById "));
	}

	@Override
	public Status addInvitcode(InvitcodeRecord record) {
		try {
			inviteDao.add(record, getSqlAlias("addInvitcode"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("add InvitcodeRecord occur an error", e);
		}
		return Status.SAVE_ERROR;
	}

	@Override
	public InvitcodeRecord selectInvitcodeById(int id) {
		// TODO Auto-generated method stub
		return inviteDao.getById(id, getSqlAlias("selectInvitcodeById"));
	}

	@Override
	public InvitcodeRecord selectInvitcodeByUuid(String uuid) {
		// TODO Auto-generated method stub
		return inviteDao.getById(uuid, getSqlAlias("selectInvitcodeByUuid"));
	}

	@Override
	public InvitcodeRecord selectInvitcodeByCode(String code) {
		// TODO Auto-generated method stub
		 return inviteDao.getById(code, getSqlAlias("selectInvitcodeByCode"));
	}

	@Override
	public Status updateInvitcodeStatus(InvitcodeRecord record) {
		// TODO Auto-generated method stub
		 try {
			inviteDao.update(record, getSqlAlias("updateInvitcodeStatus"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("update InvitcodeRecord occur an error", e);
		}
		 return Status.FAILURE;
	}
	
	@Override
	public InvitcodeRecord selectInvitcodeByRecordId(int applyId) {
		// TODO Auto-generated method stub
		return inviteDao.getById(applyId, getSqlAlias("selectInvitcodeByRecordId"));
	}

}
