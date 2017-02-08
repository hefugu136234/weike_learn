package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.TvLayout;

public interface TvSubWidgetMapper {

	public void addTvLayout(TvLayout layout);

	public List<TvLayout> selectTvLayoutsByCategoryId(int categoryId);

	public TvLayout getTvLayoutByUuid(String uuid);

	public int updateLayoutStatus(String uuid, int status);

	public int delLayout(String uuid);

	public int updateSubTvLayout(TvLayout layout);

	public List<TvLayout> selectTvLayoutsUIData(int categoryId);

	public List<TvLayout> selectAllTvHomeLayouts(int projectId);

	public List<TvLayout> selectSubTvHomeLayouts(int id);

	public List<TvLayout> selectAllTvWidgetLayouts(int projectId, int categoryId);

}
