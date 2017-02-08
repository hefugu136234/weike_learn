/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月23日
 * 	@modifyDate 2016年5月23日
 *  
 */
package com.lankr.tv_cloud.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lankr.orm.mybatis.mapper.NormalCollectMapper;
import com.lankr.tv_cloud.codes.Code;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.MediaCentralFacade;
import com.lankr.tv_cloud.facade.NormalCollectFacade;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.NormalCollect.Type;
import com.lankr.tv_cloud.utils.Tools;

/**
 * @author Kalean.Xiang
 *
 */
public class NormalCollectFacadeImpl extends FacadeBaseImpl implements
		NormalCollectFacade {

	@Autowired
	private MediaCentralFacade mediaCentralFacade;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.impl.FacadeBaseImpl#namespace()
	 */
	@Override
	protected String namespace() {
		return NormalCollectMapper.class.getName();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.NormalCollectFacade#saveCourseCollect(com.lankr
	 *      .tv_cloud.model.NormalCollect)
	 */

	@Override
	public ActionMessage<?> saveCourseCollect(NormalCollect collect,
			Map<Integer, String> covers) {
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		try {
			return transaction.execute(new TransactionCallback<ActionMessage<?>>() {
				@Override
				public ActionMessage<?> doInTransaction(TransactionStatus arg0) {
					collect.setSign(NormalCollect.getCourseSign(false, false));
					// 保存课程数据
					saveCollect(collect);
					NormalCollect after = normalCollectMapper
							.getNormalCollectByUuid(collect.getUuid());
					for (Map.Entry<Integer, String> entry : covers.entrySet()) {
						// 保存课程图片
						mediaCentralFacade.saveNormalCollectMedia(after, entry.getKey(),
								entry.getValue());
					}
					return ActionMessage.successStatus();
				}
			});
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("保存课程数据出错", e);
			return ActionMessage.failStatus("保存课程数据出错");
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.NormalCollectFacade#saveSegmentCollect(com.
	 *      lankr.tv_cloud.model.NormalCollect,
	 *      com.lankr.tv_cloud.model.NormalCollect, boolean)
	 */
	@Override
	public ActionMessage saveSegmentCollect(NormalCollect parent,
			NormalCollect segment, boolean dependentPreviousPassed) {
		segment.setParent(parent);
		segment.setSign(NormalCollect.getCourseSegmentSign(false, false,
				dependentPreviousPassed));
		return saveCollect(segment).getActionMessage();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.NormalCollectFacade#saveGeneralCollect(com.
	 *      lankr.tv_cloud.model.NormalCollect)
	 */
	@Override
	public ActionMessage<?> saveGeneralCollect(NormalCollect compilation,
			Map<Integer, String> covers) {
		compilation.setSign(NormalCollect.getGeneralCollect(false, false));
		
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		try {
			return transaction.execute(new TransactionCallback<ActionMessage<?>>() {
				@Override
				public ActionMessage<?> doInTransaction(TransactionStatus arg0) {
					// 保存合集数据
					saveCollect(compilation).getActionMessage();
					NormalCollect after = normalCollectMapper
							.getNormalCollectByUuid(compilation.getUuid());
					for (Map.Entry<Integer, String> entry : covers.entrySet()) {
						// 保存合集图片
						mediaCentralFacade.saveNormalCollectMedia(after, entry.getKey(),
								entry.getValue());
					}
					return ActionMessage.successStatus();
				}
			});
		} catch (Exception e) {
			if(logger.isErrorEnabled())
				logger.error("保存课程数据出错", e);
			return ActionMessage.failStatus("保存课程数据出错");
		}
	}

	private Code saveCollect(NormalCollect collect) {
		if (collect == null) {
			return codeProvider.code(-4001);
		}
		int effect = 0;
		try {
			if (NormalCollect.hasPersisted(collect)) {
				effect = updateNormalCollect(collect);
			} else {
				effect = addNormalCollect(collect);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return codeProvider.code(-4002);
		}
		if (effect > 0) {
			return codeProvider.codeOk();
		}
		return codeProvider.code(-4003);
	}

	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月24日
	 * @modifyDate 2016年5月24日
	 * 
	 */
	private int addNormalCollect(NormalCollect collect) throws Exception {
		collect.setPinyin(Tools.getPinYin(collect.getName()));
		collect.setVersion(1.0f);
		return normalCollectMapper.addNormalCollection(collect);
	}

	private int updateNormalCollect(NormalCollect collect) throws Exception {
		collect.setPinyin(Tools.getPinYin(collect.getName()));
		collect.setVersion(collect.getVersion() + 0.1f);
		return normalCollectMapper.updateNormalCollection(collect);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.NormalCollectFacade#
	 * searchPaginationNormalCollections(java.lang.String, int, int,
	 * com.lankr.tv_cloud.model.NormalCollect.Type[])
	 */
	@Override
	public Pagination<NormalCollect> searchPaginationNormalCollections(
			String query, int from, int batch_size, Type... types) {
		int sign = Type.getSign(types);
		String q = filterSQLSpecialChars(query);
		String total_sql = "select count(id) from normal_collect where isActive = 1 and (sign & "
				+ sign
				+ ") <> 0 and (name like '%"
				+ q
				+ "%' or pinyin like '%" + q + "%')";
		Pagination<NormalCollect> page = initPage(total_sql, from, batch_size);
		page.setResults(normalCollectMapper
				.searchCollectionDatatablePagination(sign, q, page.getBegin(),
						page.getPage_rows()));
		return page;
	}

	@Override
	public NormalCollect getNormalCollectByUuid(String uuid) {
		NormalCollect collect = null;
		try {
			collect = normalCollectMapper.getNormalCollectByUuid(uuid);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[NormalCollectFacadeImpl.getCourseByUuid] -> 根据uuid查询课程出错",
						e);
		}
		return collect;
	}

	@Override
	public ActionMessage update(NormalCollect collect) {
		int tag;
		try {
			tag = normalCollectMapper.updateNormalCollection(collect);
			if (tag > 0)
				return ActionMessage.successStatus();
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("[NormalCollectFacadeImpl.update] -> 更新课程出错", e);
		}
		return ActionMessage.failStatus("操作失败,请稍后重试!");
	}

	@Override
	public Pagination<NormalCollect> searchPaginationCourseChapters(
			String query, int from, int size, NormalCollect collect) {
		// int sign = Type.getSign(new Type[] {
		// Type.SEGMENT});
		query = filterSQLSpecialChars(query);
		Pagination<NormalCollect> pagination = new Pagination<NormalCollect>();
		;
		try {
			String total_sql = "select count(id) from normal_collect where isActive = 1 and parentId = "
					+ collect.getId()
					+ " and (name like '%"
					+ query
					+ "%' or pinyin like '%" + query + "%')";
			pagination = initPage(total_sql, from, size);
			pagination.setResults(normalCollectMapper
					.searchPaginationCourseChapters(collect.getId(), query,
							pagination.getBegin(), pagination.getPage_rows()));
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[NormalCollectFacadeImpl.searchPaginationCourseChapters] -> 查询课程章节列表出错",
						e);
		}
		return pagination;
	}

	@Override
	public NormalCollect getNormalCollectById(int referId) {
		NormalCollect collect = null;
		try {
			collect = normalCollectMapper.getNormalCollectById(referId);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[NormalCollectFacadeImpl.getNormalCollectById] -> 根据id查询课程出错",
						e);
		}
		return collect;
	}

	@Override
	public List<NormalCollect> wxCourseList(String startTime, int size, int sign) {
		return normalCollectMapper.wxCourseList(startTime, size, sign);
	}

	@Override
	public List<NormalCollect> wxChapterList(int sign, int parentId) {
		return normalCollectMapper.wxChapterList(sign, parentId);
	}

	@Override
	public ActionMessage normalCollectChapterTop(NormalCollect coll) {
		try {
			normalCollectMapper.normalCollectChapterTop(coll);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[NormalCollectFacadeImpl.normalCollectChapterTop] -> 课程置顶出错",
						e);
			return ActionMessage.failStatus("操作出错");
		}
		return ActionMessage.successStatus();
	}

	@Override
	public Pagination<NormalCollect> webCourseList(int from, int size, int sign) {
		String sql = "select count(id) from normal_collect where isActive=1 and status=1 and sign="
				+ sign;
		Pagination<NormalCollect> pagination = initPage(sql, from, size);
		List<NormalCollect> list = normalCollectMapper.webCourseList(from,
				size, sign);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public List<NormalCollect> getChapterListByParentId(int parentId) {
		List<NormalCollect> chapters = new ArrayList<NormalCollect>();
		try {
			chapters = normalCollectMapper.getChapterListByParentId(parentId);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error(
						"[NormalCollectFacadeImpl.getChapterListByParentId] -> 查询课程章节集合出错",
						e);
		}
		return chapters;
	}
}
