package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.ProjectCode;

public interface ProjectCodeFacade {

	public ProjectCode selectProjectCodeByUuid(String uuid);
	
	public ActionMessage<?> addProjectCode(ProjectCode projectCode);

	public ActionMessage<?> addBathCodeByOffline(OfflineActivity activity,int type,int size);

	public ActionMessage<?> updateProjectCode(ProjectCode projectCode);

	public ProjectCode selectOnlineCodeByInvite(
			OfflineActivity offlineActivity, String code);

	public ProjectCode selectOnlineCodeByExchange(
			OfflineActivity offlineActivity, String code);

	public Pagination<ProjectCode> selectProjectCodeForTable(int referId,
			int referType, int codeType, String searchValue, int from, int size);

}
