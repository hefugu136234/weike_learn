package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.ProjectCode;

public interface ProjectCodeMapper {

	public ProjectCode selectProjectCodeById(int id);

	public ProjectCode selectProjectCodeByUuid(String uuid);

	public int addProjectCode(ProjectCode projectCode);

	public int updateProjectCode(ProjectCode projectCode);

	public ProjectCode projectCodeByParma(@Param("referId") int referId,
			@Param("referType") int referType, @Param("codeType") int codeType,
			@Param("projectCode") String projectCode);

	public List<ProjectCode> selectProjectCodeForTable(
			@Param("referId") int referId, @Param("referType") int referType,
			@Param("codeType") int codeType,
			@Param("searchValue") String searchValue, @Param("from") int from,
			@Param("size") int size);

}
