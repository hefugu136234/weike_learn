package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.WxSubject;

public interface WxSubjectFacade {

	public WxSubject selectwxSubjectById(int id);

	public WxSubject selectwxSubjectByUuid(String uuid);

	public Status addWxSubject(WxSubject wxSubject);

	public Status updateWxSubject(WxSubject wxSubject);

	public Status updateWxSubjectStatus(WxSubject wxSubject);

	public Status deteleWxSubject(WxSubject wxSubject);

	public Pagination<WxSubject> searchWxSubjectForTable(String searchValue,
			int isRoot, int from, int size);

	public Pagination<WxSubject> searchWxSubjectChildrenForTable(
			String searchValue, int parentId, int from, int size, int type);

	public List<WxSubject> searchWxSubjectByWx(int isRoot, int type);

	public List<WxSubject> searchWxSubjectChildrenByWx(int parentId, int type,String level);
	
	public WxSubject searchWxSubjectByCategoryId(int categoryId);

	public ActionMessage recommendSubject(WxSubject subject);
	
	public List<WxSubject> getAbledParentSubject(int parentId);
	
	public Status updateSubjectParent(WxSubject wxSubject);

}
