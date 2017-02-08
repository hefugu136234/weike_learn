package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.ApplicableRecords;
import com.lankr.tv_cloud.model.InvitcodeRecord;

public interface ApplicableFacade {
	
	public Status addApplicable(ApplicableRecords applicableRecords);


	public Status updateApplicableStatus(ApplicableRecords applicableRecords);

	public ApplicableRecords selectApplicableByUuid(String uuid);
	
	public ApplicableRecords selectApplicableByUserId(int userId);
	
	public ApplicableRecords selectApplicableById(int id);

	public Pagination<ApplicableRecords> selectApplicableList(String searchValue,
			int from, int pageItemTotal);
	
	public Status addInvitcode(InvitcodeRecord record);
	
	public InvitcodeRecord selectInvitcodeById(int id);
	
	public InvitcodeRecord selectInvitcodeByUuid(String uuid);
	
	public InvitcodeRecord selectInvitcodeByCode(String code);
	
	public Status updateInvitcodeStatus(InvitcodeRecord record);
	
	public InvitcodeRecord selectInvitcodeByRecordId(int applyId);
	

}
