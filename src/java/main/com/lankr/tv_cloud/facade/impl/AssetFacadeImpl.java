package com.lankr.tv_cloud.facade.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.lankr.orm.mybatis.mapper.SpeakerMapper;
import com.lankr.tv_cloud.facade.AssetFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.AssetPrice;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.SpeakerVo;

public class AssetFacadeImpl extends FacadeBaseImpl implements AssetFacade {

	@Override
	public List<Category> fetchProjectRootCategory(Project project) {
		return categoryDao.searchByForeignKey(project.getId(),
				getSqlAlias("fetchRootCategory"));
	}

	@Override
	public Status addCategory(Category category) {
		try {
			categoryDao.add(category, getSqlAlias("addCategory"));
			// 更改标识
			categoryOrderInternal(category);
		} catch (Exception e) {
			logger.error(e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	public Status updateCategory(Category category) {
		try {
			categoryDao.update(category, getSqlAlias("updateCategory"));
			// 更改标识
			// categoryOrderInternal(category);
		} catch (Exception e) {
			logger.error(e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	public Category getCategoryByUuid(String uuid) {
		return categoryDao.getById(uuid, getSqlAlias("getCategoryByUuId"));
	}

	@Override
	public Category getCategoryById(int id) {
		// TODO Auto-generated method stub
		return categoryDao.getById(id, getSqlAlias("getCategoryById"));
	}

	@Override
	public Category getCategoryByName(String name) {
		// TODO Auto-generated method stub
		return categoryDao.getById(name, getSqlAlias("getCategoryByName"));
	}

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "asset";
	}

	@Override
	public Status addVideo(Video video) {
		try {
			videoDao.add(video, getSqlAlias("addVideo"));
			// 添加到资源表
			recodeResource(video);
		} catch (Exception e) {
			logger.error("", e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	public Video getVideoByUUID(String uuid) {
		return videoDao.getById(uuid, getSqlAlias("getVideoByUuid"));
	}

	// modified by mayuan --> 添加 "categoryUuid" 字段
	@Override
	public Pagination<Video> searchVideos(int projectId, String q, int from,
			int to, String sortValString, String sortvalueString,
			String categoryUuid) {
		String query = filterSQLSpecialChars(q);
		SubParams param = new SubParams();
		param.id = projectId;
		param.query = query;
		param.sortVlaue = sortValString;
		param.comlunName = sortvalueString;
		// 添加categoryUid字段
		param.setCategoryUuid(categoryUuid);

		Pagination<Video> pv;
		if (StringUtils.isEmpty(categoryUuid)) {
			pv = initPage(
					"select count(asset.id) from asset where asset.isActive=1 and asset.projectId="
							+ projectId + " and (title like '%" + query
							+ "%' or pinyin like '%" + query + "%')", from, to);

			List<Video> videos = videoDao.searchAllPagination(
					getSqlAlias("searchVideos"), param, from, to);
			pv.setResults(videos);
		} else {
			pv = initPage(
					"select count(asset.id) from asset left join category on asset.categoryId = category.id "
							+ " where "
							+ " asset.isActive=1 "
							+ " and "
							+ " asset.projectId = "
							+ projectId
							+ " and "
							+ " category.uuid = "
							+ "'"
							+ categoryUuid
							+ "'"
							+ " and "
							+ " (asset.title like '%"
							+ query
							+ "%' or asset.pinyin like '%" + query + "%')",
					from, to);

			List<Video> videos = videoDao.searchAllPagination(
					getSqlAlias("searchVideosUseButton"), param, from, to);
			pv.setResults(videos);
		}
		return pv;
	}

	@Override
	public Status updateVideo(Video video) {
		try {
			videoDao.update(video, getSqlAlias("updateVideo"));
			recodeResource(video);
		} catch (Exception e) {
			logger.error("update video error ", e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	public Status deleteCategory(Category category) {
		try {
			categoryDao.del(category, getSqlAlias("deletecategory"));
		} catch (Exception e) {
			logger.error("del category error ", e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	public Status addAssetPrice(AssetPrice price) {
		try {
			priceDao.add(price, getSqlAlias("addAssetPrice"));
			return Status.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return Status.SAVE_ERROR;
	}

	// 最多获取到第1层子分类
	private int root_deep = 1;

	@Override
	public List<Category> findAllChildrenCategory(Category root_category) {
		List<Category> container = new ArrayList<>();
		root_category.setParent(null);
		container.add(root_category);
		collapseChildren(container, root_category, root_deep);
		return container;
	}

	private void collapseChildren(List<Category> container, Category root,
			int deep) {
		if (deep == 0)
			return;
		List<Category> children = root.getChildren();
		if (children != null && !children.isEmpty()) {
			for (Category category : children) {
				container.add(category);
				collapseChildren(container, category, deep - 1);
			}
		}
	}

	@Override
	public Status addSpeaker(Speaker speaker) {
		try {
			speakerMapper.addSpeaker(speaker);
			return Status.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return Status.FAILURE;
		}
	}

	@Override
	public Status updateSpeaker(Speaker speaker) {
		try {
			speakerMapper.updateSpeaker(speaker);
			return Status.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return Status.FAILURE;
		}
	}

	@Override
	public Status updateSpeakerStatus(Speaker speaker) {
		// TODO Auto-generated method stub
		try {
			speakerMapper.updateSpeakerStatus(speaker);
			return Status.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return Status.FAILURE;
		}
	}

	@Override
	public Speaker getSpeakerByUuid(String uuid) {
		// TODO Auto-generated method stub
		return speakerMapper.getSpeakerByUuid(uuid);
	}

	@Override
	public Pagination<Speaker> searchAllSpeakersPaginatio(int from, int rows,
			String query) {
		query = filterSQLSpecialChars(query);
		Pagination<Speaker> speakers = initPage(
				"select count(*) from speaker where isActive=1 and (name like '%"
						+ query + "%' or pinyin like '%" + query + "%')", from,
				rows);

		speakers.setResults(speakerMapper.searchSpeakerPagination(from, rows,
				query));
		return speakers;
	}

	@Override
	public List<SpeakerVo> fetchAllSimpleSpeakers() {
		String sqlString = "select s.uuid,s.name,s.sex ,h.name as 'hospitalName' from speaker s left join base_hospital h on s.hospitalId=h.id where s.isActive=1 and s.status=1 order by s.modifyDate desc";
		return searchSql(sqlString, null, SpeakerVo.class);
	}

	@Override
	public Status speakerAssociationUser(Speaker speaker) {
		int userId = speaker.getUser().getId();
		Speaker existSpeaker = speakerMapper.getSpeakerByUserId(userId);
		if (null != existSpeaker) {
			return Status.FAILURE;
		}
		try {
			int tag = speakerMapper.speakerAssociationUser(speaker);
			if (tag > 0)
				return Status.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("后台讲者绑定用户出错", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateSpeakerCleanUser(Speaker speaker) {
		try {
			int tag = speakerMapper.updateSpeakerCleanUser(speaker);
			if (tag > 0)
				return Status.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("后台讲者解除绑定出错", e);
		}
		return Status.FAILURE;
	}

	private int horizon = 0;

	private synchronized void categoryOrderInternal(Category category) {
		Project p = category.getProject();
		horizon = 0;
		if (p == null)
			return;
		List<Category> root = fetchProjectRootCategory(p);
		if (Tools.isEmpty(root))
			return;
		for (int i = 0; i < root.size(); i++) {
			Category c = root.get(i);
			int right = markCategory(c, 1);
			recordCategory(c, right, 1);
		}
	}

	private int markCategory(Category c, int depth) {
		if (c == null)
			return horizon;
		c.setLeft(++horizon);
		List<Category> cs = c.getChildren();
		if (Tools.isEmpty(c.getChildren())) {
			return ++horizon;
		}
		depth += 1;
		for (int i = 0; i < cs.size(); i++) {
			Category cate = cs.get(i);
			int right = markCategory(cate, depth);
			recordCategory(cate, right, depth);
		}
		return ++horizon;
	}

	private void recordCategory(Category category, int right, int depth) {
		category.setRight(right);
		category.setHierarchy(depth);
		updateCategoryHieraychy(category);

	}

	private void updateCategoryHieraychy(Category category) {
		try {
			logger.info(category.getId() + " " + category.getName()
					+ " hierarchy:" + category.getHierarchy() + " left:"
					+ category.getLeft() + " right:" + category.getRight());
			categoryDao
					.update(category, getSqlAlias("updateCategoryHieraychy"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
