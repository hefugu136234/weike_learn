/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月20日
 * 	@modifyDate 2016年5月20日
 *  
 */
package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceGroup;

/**
 * @author Kalean.Xiang
 *
 */
public interface ResourceGroupFacade {

	// 课程中添加资源
	public ActionMessage saveOrUpateNormalCollectResouce(ResourceGroup resourceGroup);

	public Pagination<ResourceGroup> searchPaginationChapterResource(String q,
			int from, int size, NormalCollect chapter);

	public ResourceGroup getResourceGroupByUuid(String chapterResUuid);

	public ActionMessage normalCollectResouceTop(ResourceGroup resourceGroup);
	
	public List<ResourceGroup> wxChapterResourceGroups(int type,int referId);

	public Pagination<ResourceGroup> searchPaginationCompilationResource(
			String q, int from, int size, NormalCollect compilation);

	/**
	 * @Description: 获取集合资源(filter: isActive=1)
	 */
	public List<ResourceGroup> getGeneralCollectResourceGroups(
			int typeGeneralCollect, int id);

	/**
	 * @Description: 获取章节资源(filter: isActive=1)
	 */
	public List<ResourceGroup> getCourseSementResourceGroups(
			int typeCourseSegment, int id);
}
