package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.WxSubjectFacade;
import com.lankr.tv_cloud.model.WxSubject;

public class WxSubjectFacadeImp extends FacadeBaseImpl implements
		WxSubjectFacade {

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "com.lankr.orm.mybatis.mapper.WxSubjectMapper";
	}

	@Override
	public WxSubject selectwxSubjectById(int id) {
		// TODO Auto-generated method stub
		return wxSubjectMapper.selectwxSubjectById(id);
	}

	@Override
	public WxSubject selectwxSubjectByUuid(String uuid) {
		// TODO Auto-generated method stub
		return wxSubjectMapper.selectwxSubjectByUuid(uuid);
	}

	@Override
	public Status addWxSubject(WxSubject wxSubject) {
		// TODO Auto-generated method stub
		try {
			int effect = wxSubjectMapper.addWxSubject(wxSubject);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加微信学科失败", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateWxSubject(WxSubject wxSubject) {
		// TODO Auto-generated method stub
		try {
			int effect = wxSubjectMapper.updateWxSubject(wxSubject);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("更新微信学科信息失败", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateWxSubjectStatus(WxSubject wxSubject) {
		// TODO Auto-generated method stub
		try {
			int effect = wxSubjectMapper.updateWxSubjectStatus(wxSubject);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("更新微信学科信息状态失败", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status deteleWxSubject(WxSubject wxSubject) {
		// TODO Auto-generated method stub
		try {
			int effect = wxSubjectMapper.deteleWxSubject(wxSubject);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("删除微信学科信息失败", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Pagination<WxSubject> searchWxSubjectForTable(String searchValue,
			int isRoot, int from, int size) {
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from wx_subject where isActive=1 and isRoot="
				+ isRoot
				+ " and (name like '%"
				+ searchValue
				+ "%' or pinyin like '%" + searchValue + "%')";
		Pagination<WxSubject> pagination = initPage(sql, from, size);
		List<WxSubject> list = wxSubjectMapper.searchWxSubjectForTable(
				searchValue, isRoot, from, size);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public Pagination<WxSubject> searchWxSubjectChildrenForTable(
			String searchValue, int parentId, int from, int size, int type) {
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from wx_subject where isActive=1 and rootType="
				+ type
				+ " and parentId="
				+ parentId
				+ " and (name like '%"
				+ searchValue + "%' or pinyin like '%" + searchValue + "%')";
		Pagination<WxSubject> pagination = initPage(sql, from, size);
		List<WxSubject> list = wxSubjectMapper.searchWxSubjectChildrenForTable(
				searchValue, parentId, from, size, type);
		pagination.setResults(list);
		return pagination;

	}

	@Override
	public List<WxSubject> searchWxSubjectByWx(int isRoot, int type) {
		return wxSubjectMapper.searchWxSubjectByWx(isRoot, type);
	}

	@Override
	public List<WxSubject> searchWxSubjectChildrenByWx(int parentId, int type,String level) {
		// TODO Auto-generated method stub
		return wxSubjectMapper.searchWxSubjectChildrenByWx(parentId, type,level);
	}

	@Override
	public WxSubject searchWxSubjectByCategoryId(int categoryId) {
		// TODO Auto-generated method stub
		return wxSubjectMapper.searchWxSubjectByreflectId(categoryId,
				WxSubject.TYPE_CATEGORY);
	}

	@Override
	public ActionMessage recommendSubject(WxSubject subject) {
		try {
			int effect = wxSubjectMapper.recommendSubject(subject);
			if (effect == 1) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("学科置顶失败");
	}

	@Override
	public List<WxSubject> getAbledParentSubject(int parentId) {
		// TODO Auto-generated method stub
		return wxSubjectMapper.getAbledParentSubject(parentId);
	}
	
	@Override
	public Status updateSubjectParent(WxSubject wxSubject) {
		// TODO Auto-generated method stub
		try {
			int effect = wxSubjectMapper.updateSubjectParent(wxSubject);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("修改微信学科父目录失败", e);
		}
		return Status.FAILURE;
	}

}
