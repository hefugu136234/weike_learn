package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import com.lankr.orm.mybatis.mapper.ProjectCodeMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.ProjectCodeFacade;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.ProjectCode;
import com.lankr.tv_cloud.utils.Tools;

public class ProjectCodeFacadeImp extends FacadeBaseImpl implements
		ProjectCodeFacade {

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return ProjectCodeMapper.class.getName();
	}

	@Override
	public ProjectCode selectProjectCodeByUuid(String uuid) {
		// TODO Auto-generated method stub
		return projectCodeMapper.selectProjectCodeByUuid(uuid);
	}

	@Override
	public ActionMessage<?> addProjectCode(ProjectCode projectCode) {
		// TODO Auto-generated method stub
		try {
			projectCode.setUuid(Tools.getUUID());
			projectCode.setStatus(BaseModel.UNAPPROVED);
			projectCode.setIsActive(BaseModel.ACTIVE);
			int effect = projectCodeMapper.addProjectCode(projectCode);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("添加code失败", e);
		}
		return ActionMessage.failStatus("添加code失败");
	}

	@Override
	public ActionMessage<?> addBathCodeByOffline(OfflineActivity activity,
			int type, int size) {
		// TODO Auto-generated method stub
		if (activity == null)
			return ActionMessage.failStatus("参数不合法");
		size = Math.max(size, 10);
		size = Math.min(size, 100);
		return addBathCode(activity.getId(), ProjectCode.REFER_OFFLINEACTIVITY,
				type, size);
	}

	public ActionMessage<?> addBathCode(int referId, int referType,
			int codeType, int size) {
		// TODO Auto-generated method stub
		for (int i = 0; i < size; i++) {
			ProjectCode projectCode = new ProjectCode();
			projectCode.setReferId(referId);
			projectCode.setReferType(referType);
			projectCode.setCodeType(codeType);
			projectCode.setProjectCode(Tools.generateShortUuid(6));
			addProjectCode(projectCode);
		}
		return ActionMessage.successStatus();
	}

	@Override
	public ActionMessage<?> updateProjectCode(ProjectCode projectCode) {
		// TODO Auto-generated method stub
		try {
			int effect = projectCodeMapper.updateProjectCode(projectCode);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("修改code失败", e);
		}
		return ActionMessage.failStatus("修改code失败");
	}

	@Override
	public ProjectCode selectOnlineCodeByInvite(
			OfflineActivity offlineActivity, String code) {
		// TODO Auto-generated method stub
		if (offlineActivity == null)
			return null;
		return projectCodeMapper.projectCodeByParma(offlineActivity.getId(),
				ProjectCode.REFER_OFFLINEACTIVITY, ProjectCode.CODE_INVITE,
				code);
	}

	@Override
	public ProjectCode selectOnlineCodeByExchange(
			OfflineActivity offlineActivity, String code) {
		// TODO Auto-generated method stub
		if (offlineActivity == null)
			return null;
		return projectCodeMapper.projectCodeByParma(offlineActivity.getId(),
				ProjectCode.REFER_OFFLINEACTIVITY, ProjectCode.CODE_EXCHANGE,
				code);
	}

	@Override
	public Pagination<ProjectCode> selectProjectCodeForTable(int referId,
			int referType, int codeType, String searchValue, int from, int size) {
		// TODO Auto-generated method stub
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from project_code where isActive=1 and referId="
				+ referId
				+ " and referType="
				+ referType
				+ " and codeType="
				+ codeType + " and projectCode like '%" + searchValue + "%'";
		Pagination<ProjectCode> pagination = initPage(sql, from, size);
		List<ProjectCode> list = projectCodeMapper.selectProjectCodeForTable(
				referId, referType, codeType, searchValue, from, size);
		pagination.setResults(list);
		return pagination;
	}

}
