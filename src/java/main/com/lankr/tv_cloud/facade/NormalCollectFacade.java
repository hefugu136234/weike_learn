/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月23日
 * 	@modifyDate 2016年5月23日
 *  
 */
package com.lankr.tv_cloud.facade;

import java.util.List;
import java.util.Map;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollect.Type;
import com.lankr.tv_cloud.model.ResourceGroup;

/**
 * @author Kalean.Xiang
 *
 */
public interface NormalCollectFacade {

	// 课程
	public ActionMessage saveCourseCollect(NormalCollect collect,
			Map<Integer, String> covers);

	/**
	 * 添加等级
	 * 
	 * @param parent
	 *            课程
	 * @param segment
	 *            分片 （等级）
	 * @param dependentPreviousPassed
	 *            是否依赖上一个segment的及格
	 */
	public ActionMessage saveSegmentCollect(NormalCollect parent,
			NormalCollect segment, boolean dependentPreviousPassed);

	// 合集
	public ActionMessage saveGeneralCollect(NormalCollect collect,
			Map<Integer, String> covers);

	/**
	 * @param query
	 * @param from
	 * @param batch_size
	 * @param types
	 *            types that you want to selected
	 * */
	public Pagination<NormalCollect> searchPaginationNormalCollections(
			String query, int from, int batch_size, Type... types);

	public NormalCollect getNormalCollectByUuid(String uuid);

	public ActionMessage update(NormalCollect collect);

	public Pagination<NormalCollect> searchPaginationCourseChapters(String q,
			int from, int size, NormalCollect normalCollect);

	public NormalCollect getNormalCollectById(int referId);
	
	//微信获取课程列表
	public List<NormalCollect> wxCourseList(String startTime,int size,int sign);
	
	//微信获取课程的章节
	public List<NormalCollect> wxChapterList(int sign,int parentId);

	public ActionMessage normalCollectChapterTop(NormalCollect chapter);
	
	//pc端获取课程列表
	public Pagination<NormalCollect> webCourseList(int from,int size,int sign);
	
	/**
	 * @Description: 获取指定学科下的章节集合(fiter: isActive=1)
	 */
	public List<NormalCollect> getChapterListByParentId(int parentId);

}
