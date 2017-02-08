package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.ResourceGroup;

public interface ResourceGroupMapper {

	public List<ResourceGroup> searchPaginationChapterResource(int id,
			String query, int begin, int page_rows);

	public ResourceGroup getResourceGroupByUuid(String chapterResUuid);

	public int saveNormalCollectResouce(ResourceGroup resourceGroup);

	public int updateNormalCollectResouce(ResourceGroup resourceGroup);

	public void normalCollectResouceTop(ResourceGroup resourceGroup);

	public int getResourceGroupMinPosition(ResourceGroup resourceGroup);

	public List<ResourceGroup> wxChapterResourceGroups(@Param("type") int type,
			@Param("referId") int referId);

	public List<ResourceGroup> searchPaginationCompilationResource(int id,
			String query, int begin, int page_rows);

	public List<ResourceGroup> getResourceGroups(int type, int referId);

	// 一个课程包下的视频数量
	public Integer selectNumResByCourse(@Param("courseType") int courseType,
			@Param("referId") int referId, @Param("chaterType") int chaterType);

	// 一个课程包下的资源id list
	public List<Integer> selectResidByCourse(@Param("referId") int referId,
			@Param("chaterType") int chaterType);

}
