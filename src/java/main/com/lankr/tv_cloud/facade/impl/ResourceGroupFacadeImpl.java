/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年7月27日
 * 	@modifyDate 2016年7月27日
 *  
 */
package com.lankr.tv_cloud.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.orm.mybatis.mapper.ResourceGroupMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.ResourceGroupFacade;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceGroup;
import com.lankr.tv_cloud.utils.Tools;

/**
 * @author Kalean.Xiang
 *
 */
public class ResourceGroupFacadeImpl extends FacadeBaseImpl implements
		ResourceGroupFacade {


	@Override
	protected String namespace() {
		return ResourceGroupMapper.class.getName();
	}

	@Override
	public ActionMessage saveOrUpateNormalCollectResouce(
			ResourceGroup resourceGroup) {
		resourceGroup.setSign(ResourceGroup.SIGN_DEFAULT);
		int effect = 0;
		try {
			if (NormalCollect.hasPersisted(resourceGroup)) {
				effect = updateNormalCollectResouce(resourceGroup);
			} else {
				effect = saveNormalCollectResouce(resourceGroup);
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceGroupFacadeImpl.saveOrUpateNormalCollectResouce] -> 保存资源数据出错",
						e);
			return ActionMessage.failStatus("操作失败，请重新尝试");
		}
		if (effect > 0)
			return ActionMessage.successStatus();
		return ActionMessage.failStatus("操作失败，请重新尝试");
	}

	private int saveNormalCollectResouce(ResourceGroup resourceGroup) {
		return resourceGroupMapper.saveNormalCollectResouce(resourceGroup);
	}

	private int updateNormalCollectResouce(ResourceGroup resourceGroup) {
		return resourceGroupMapper.updateNormalCollectResouce(resourceGroup);
	}

	@Override
	public Pagination<ResourceGroup> searchPaginationChapterResource(
			String query, int from, int size, NormalCollect chapter) {
		query = filterSQLSpecialChars(query);
		Pagination<ResourceGroup> pagination = new Pagination<ResourceGroup>();
		try {
			String total_sql = "select count(id) from resource_group where isActive = 1 and type = "
					+ ResourceGroup.TYPE_COURSE_SEGMENT
					+ " and referId = "
					+ chapter.getId()
					+ " and (name like '%"
					+ query
					+ "%' or mark like '%" + query + "%')";
			pagination = initPage(total_sql, from, size);
			pagination.setResults(resourceGroupMapper
					.searchPaginationChapterResource(chapter.getId(), query,
							pagination.getBegin(), pagination.getPage_rows()));
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceGroupFacadeImpl.searchPaginationChapterResource] -> 查询章节资源列表出错",
						e);
		}
		return pagination;
	}

	@Override
	public ResourceGroup getResourceGroupByUuid(String chapterResUuid) {
		ResourceGroup resourceGroup = null;
		try {
			resourceGroup = resourceGroupMapper
					.getResourceGroupByUuid(chapterResUuid);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceGroupFacadeImpl.getResourceGroupByUuid] -> 根据uuid查询章节资源出错",
						e);
		}
		return resourceGroup;
	}

	@Override
	public ActionMessage normalCollectResouceTop(ResourceGroup resourceGroup) {
		try {
			resourceGroupMapper.normalCollectResouceTop(resourceGroup);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceGroupFacadeImpl.normalCollectResouceTop] -> 资源置顶出错",
						e);
			return ActionMessage.failStatus("操作出错");
		}
		return ActionMessage.successStatus();
	}

	
	@Override
	public List<ResourceGroup> wxChapterResourceGroups(int type, int referId) {
		return resourceGroupMapper.wxChapterResourceGroups(type, referId);
	}

	@Override
	public Pagination<ResourceGroup> searchPaginationCompilationResource(
			String query, int from, int size, NormalCollect compilation) {
		query = filterSQLSpecialChars(query);
		Pagination<ResourceGroup> pagination = new Pagination<ResourceGroup>();
		try {
			String total_sql = "select count(id) from resource_group where isActive = 1 and type = "
					+ ResourceGroup.TYPE_GENERAL_COLLECT
					+ " and referId = "
					+ compilation.getId()
					+ " and (name like '%"
					+ query
					+ "%' or mark like '%" + query + "%')";
			pagination = initPage(total_sql, from, size);
			pagination.setResults(resourceGroupMapper
					.searchPaginationCompilationResource(compilation.getId(), query,
							pagination.getBegin(), pagination.getPage_rows()));
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceGroupFacadeImpl.searchPaginationCompilationResource] -> 查询合集资源列表出错",
						e);
		}
		return pagination;
	}

	@Override
	public List<ResourceGroup> getGeneralCollectResourceGroups(
			int typeGeneralCollect, int id) {
		List<ResourceGroup> resources = new ArrayList<ResourceGroup>();
		try {
			resources = resourceGroupMapper.getResourceGroups(typeGeneralCollect, id);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceGroupFacadeImpl.getGeneralCollectResourceGroups] -> 查询集合资源集合出错",
						e);
		}
		return resources;
	}

	@Override
	public List<ResourceGroup> getCourseSementResourceGroups(
			int typeCourseSegment, int id) {
		List<ResourceGroup> resources = new ArrayList<ResourceGroup>();
		try {
			resources = resourceGroupMapper.getResourceGroups(typeCourseSegment, id);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[ResourceGroupFacadeImpl.getCourseSementResourceGroups] -> 查询章节资源集合出错",
						e);
		}
		return resources;
	}

}
