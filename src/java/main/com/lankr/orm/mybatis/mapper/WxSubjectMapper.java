package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.WxSubject;

public interface WxSubjectMapper {

	public WxSubject selectwxSubjectById(int id);

	public WxSubject selectwxSubjectByUuid(String uuid);

	public int addWxSubject(WxSubject wxSubject);

	public int updateWxSubject(WxSubject wxSubject);

	public int updateWxSubjectStatus(WxSubject wxSubject);

	public int deteleWxSubject(WxSubject wxSubject);

	public List<WxSubject> searchWxSubjectForTable(String searchValue,
			int isRoot, int from, int size);

	public List<WxSubject> searchWxSubjectChildrenForTable(String searchValue,
			int parentId, int from, int size, int type);

	public List<WxSubject> searchWxSubjectByWx(int isRoot, int type);

	public List<WxSubject> searchWxSubjectChildrenByWx(
			@Param("parentId") int parentId, @Param("type") int type,
			@Param("level") String level);

	public WxSubject searchWxSubjectByreflectId(int reflectId, int rootType);

	public int recommendSubject(WxSubject subject);

	public List<WxSubject> getAbledParentSubject(int parentId);

	public int updateSubjectParent(WxSubject wxSubject);

}
