/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月23日
 * 	@modifyDate 2016年5月23日
 *  
 */
package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.NormalCollect;

/**
 * @author Kalean.Xiang
 *
 */
public interface NormalCollectMapper {

	public int addNormalCollection(NormalCollect collect);

	public int updateNormalCollection(NormalCollect collect);

	public List<NormalCollect> searchCollectionDatatablePagination(
			int sign_logic, String query, int from, int batch_size);

	public NormalCollect getNormalCollectByUuid(@Param("uuid") String uuid);

	public List<NormalCollect> searchPaginationCourseChapters(int id,
			String query, int begin, int page_rows);

	public NormalCollect getNormalCollectById(int referId);

	public List<NormalCollect> wxCourseList(
			@Param("startTime") String startTime, @Param("size") int size,
			@Param("sign") int sign);

	public List<NormalCollect> wxChapterList(@Param("sign") int sign,
			@Param("parentId") int parentId);

	public void normalCollectChapterTop(NormalCollect coll);

	public List<NormalCollect> webCourseList(@Param("from") int from,
			@Param("size") int size, @Param("sign") int sign);

	public List<NormalCollect> getChapterListByParentId(int parentId);
	
	public int updateCollectionNums(NormalCollect collect);
}
