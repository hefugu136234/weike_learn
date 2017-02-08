package com.lankr.tv_cloud.facade.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lankr.orm.mybatis.mapper.TvSubWidgetMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.ProjectFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.TvLayout;
import com.lankr.tv_cloud.model.Widget;
import com.lankr.tv_cloud.utils.Tools;

public class ProjectFacadeImpl extends FacadeBaseImpl implements ProjectFacade {

	private TvSubWidgetMapper tvSubWidgetMapper;

	private static final int MAX_HOME_LAYOUTS = 5;

	@Autowired
	public void setTvSubWidgetMapper(TvSubWidgetMapper tvSubWidgetMapper) {
		this.tvSubWidgetMapper = tvSubWidgetMapper;
	}

	@Override
	public Status addProject(Project project) {
		try {
			projectDao.add(project, getSqlAlias("addProject"));
		} catch (Exception e) {
			logger.error("add project with an error", e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	protected String namespace() {
		return "project";
	}

	@Override
	public List<Project> fetchUserRefsProjects(int userId) {

		return projectDao.searchByForeignKey(userId,
				getSqlAlias("searchReferenceProjectByUserId"));
	}

	@Override
	public Project getProjectByUuid(String uuid) {
		return projectDao.getById(uuid, getSqlAlias("getProjectByUuid"));
	}

	@Override
	public Pagination<Project> searchProjects(String q, int page, int rows) {
		// TODO Auto-generated method stub
		Pagination<Project> pp = initPage(
				"select count(id) from project where isActive=1 and (projectName like '%"
						+ filterSQLSpecialChars(q) + "%' or pinyin like '%"
						+ filterSQLSpecialChars(q) + "%')", page, rows);
		List<Project> ps = projectDao.searchAllPagination(
				getSqlAlias("searchProject"), filterSQLSpecialChars(q), page,
				rows);
		pp.setResults(ps);
		return pp;
	}

	@Override
	public Status addWidgets(Project project, List<Widget> widgets) {
		try {
			synchronized (getLock(project.getId())) {
				// 获取目前最新的版本
				float new_ver = getMaxVersion(project.getId()) + 0.1f;
				for (int i = 0; i < widgets.size(); i++) {
					Widget widget = widgets.get(i);
					widget.setVersion(new_ver);
					widgetDao.add(widget, getSqlAlias("widget", "addWidget"));
				}
			}
		} catch (Exception e) {
			logger.error(e);
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	private Map<Integer, Integer> widget_lock_cache = new ConcurrentHashMap<Integer, Integer>();

	private Object getLock(int id) {
		Integer lock = widget_lock_cache.get(id);
		if (lock == null) {
			lock = new Integer(id);
			widget_lock_cache.put(id, lock);
		}
		return lock;
	}

	private float getMaxVersion(int projectId) {
		String sql = "select max(version) as max_ver from widget where projectId="
				+ projectId;
		return jdbcTemplate.query(sql, new ResultSetExtractor<Float>() {
			@Override
			public Float extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				while (rs.next()) {
					return rs.getFloat("max_ver");
				}
				return 1.0f;
			}
		});
	}

	@Override
	public List<Widget> selectProjectWidgets(Project project) {
		return widgetDao.searchByForeignKey(project.getId(),
				getSqlAlias("widget", "selectProjectWidgets"));
	}

	@Override
	public Status addSubTvLayout(TvLayout layout) {
		if (layout == null || !layout.isWidgetsValid()) {
			return Status.PARAM_ERROR;
		}
		try {
			tvSubWidgetMapper.addTvLayout(layout);
		} catch (Exception e) {
			e.printStackTrace();
			return Status.FAILURE;
		}
		return Status.SUCCESS;
	}

	@Override
	public TvLayout getTvLayoutByUuid(String uuid) {
		return tvSubWidgetMapper.getTvLayoutByUuid(uuid);
	}

	@Override
	public List<TvLayout> selectTvLayoutsByCategory(Category category) {
		if (category == null)
			return null;
		return tvSubWidgetMapper.selectTvLayoutsByCategoryId(category.getId());
	}

	@Override
	public Status changeLayoutStatus(String uuid, int status) {
		int effect = tvSubWidgetMapper.updateLayoutStatus(uuid, status);
		if (effect > 0) {
			return Status.SUCCESS;
		}
		return Status.FAILURE;
	}

	@Override
	public Status delLayout(String uuid) {
		int effect = tvSubWidgetMapper.delLayout(filterSQLSpecialChars(uuid));
		if (effect > 0) {
			return Status.SUCCESS;
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateSubTvLayout(TvLayout layout) {
		try {
			int effect = tvSubWidgetMapper.updateSubTvLayout(layout);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public List<TvLayout> selectTvLayoutsUIData(Category category) {
		if (category == null)
			return null;
		return tvSubWidgetMapper.selectTvLayoutsUIData(category.getId());
	}

	@Override
	public List<TvLayout> selectProjectTvHomeLayouts(Project project) {
		if (!Project.hasPersisted(project))
			return null;
		return tvSubWidgetMapper.selectAllTvHomeLayouts(project.getId());
	}

	@Override
	public ActionMessage saveProjectHomeLabel(Project project, String label,
			int type, Category category) {
		try {
			synchronized (project.getDefaultLock()) {
				int count_max = 0;
				int count_label = 0;
				
				//判断板块数量是否达上限(5个)
				if(type == TvLayout.TYPE_HOME){
					String max_limit = "select count(id) from tv_layout where isActive=1 and type=? and projectId=?";
					count_max = jdbcTemplate.queryForInt(max_limit, new Object[] {
							type, project.getId() });
				}else if(type == TvLayout.TYPE_INNER_V2){
					String max_limit = "select count(id) from tv_layout where isActive=1 and type=? and projectId=? and categoryId = ? ";
					count_max = jdbcTemplate.queryForInt(max_limit, new Object[] {
							type, project.getId(), category.getId() });
				}
				if (count_max >= MAX_HOME_LAYOUTS) {
					return ActionMessage.failStatus("板块数不能超过"
							+ MAX_HOME_LAYOUTS + "个");
				}
				
				//判断是否有同名的lable
				label = filterSQLSpecialChars(label);
				if(type == TvLayout.TYPE_HOME){
					String sql = "select count(id) from tv_layout where isActive=1 and type=? and projectId=? and name=?";
					count_label = jdbcTemplate.queryForInt(sql, new Object[] { type,
							project.getId(), label });
				}else if(type == TvLayout.TYPE_INNER_V2){
					String sql = "select count(id) from tv_layout where isActive=1 and type=? and projectId=? and name=? and categoryId = ? ";
					count_label = jdbcTemplate.queryForInt(sql, new Object[] { type,
							project.getId(), label, category.getId() });
				}
				if (count_label > 0) {
					return ActionMessage.failStatus("已存在该板块");
				}

				// 设置position
				Integer maxPosition = this.getMaxPosition(project, null, type);

				TvLayout tl = new TvLayout();
				tl.setName(label);
				tl.setUuid(Tools.getUUID());
				tl.setProject(project);
				tl.setType(type);
				tl.setStatus(BaseModel.APPROVED);
				tl.setIsActive(BaseModel.ACTIVE);
				if (null == maxPosition) {
					tl.setPosition(1);
				} else {
					tl.setPosition(maxPosition + 1);
				}
				if (TvLayout.TYPE_INNER_V2 == tl.getType() && null != category) {
					tl.setCategory(category);
				}
				tvSubWidgetMapper.addTvLayout(tl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ActionMessage.failStatus("添加板块失败");
		}
		return ActionMessage.successStatus();
	}

	private Integer getMaxPosition(Project project, Category category, int type) {
		String max_position = "select max(position) from tv_layout where isActive = 1 and type = ? and projectId = ? ";
		if (null != category) {
			max_position = max_position + " and categoryId = ? ";
			Integer maxPosition = jdbcTemplate.queryForInt(max_position,
					new Object[] { type, project.getId(), category.getId() });
			return maxPosition;
		}
		Integer maxPosition = jdbcTemplate.queryForInt(max_position,
				new Object[] { type, project.getId() });
		return maxPosition;
	}

	@Override
	public ActionMessage disableHomeLabel(TvLayout layout) {
		String sql = "update tv_layout set isActive=0 where id="
				+ layout.getId();
		jdbcTemplate.execute(sql);
		return ActionMessage.successStatus();
	}

	@Override
	public ActionMessage updateTvHomeLayoutWidgets(TvLayout layout) {
		try {
			int effect = tvSubWidgetMapper.updateSubTvLayout(layout);
			if (effect > 0) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("保存失败");
	}

	/**
	 * 共用后台得实现
	 * */
	@Override
	public List<TvLayout> tvHomeLayoutsForAPI(Project project) {
		return selectProjectTvHomeLayouts(project);
	}

	@Override
	public ActionMessage homeLayoutToTop(TvLayout tl, int type) {
		if (null != tl) {
			try {
				Integer maxPosition = this.getMaxPosition(tl.getProject(),
						tl.getCategory(), type);
				int tmp = maxPosition + 1;
				tl.setPosition(tmp);
				int effect = tvSubWidgetMapper.updateSubTvLayout(tl);
				if (effect > 0) {
					return ActionMessage.successStatus();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ActionMessage.failStatus("操作失败");
			}
		}
		return ActionMessage.failStatus("操作失败");
	}

	@Override
	public List<TvLayout> selectWidgetTvHomeLayouts(Category category) {
		if (!Project.hasPersisted(category))
			return null;
		return tvSubWidgetMapper.selectSubTvHomeLayouts(category.getId());
	}

	@Override
	public List<TvLayout> selectProjectTvWidgetLayouts(Project stubProject,
			int id) {
		if (!Project.hasPersisted(stubProject))
			return null;
		return tvSubWidgetMapper.selectAllTvWidgetLayouts(stubProject.getId(),
				id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.ProjectFacade#tvSubHomeLayoutsForAPI(com.lankr
	 * .tv_cloud.model.Category)
	 */
	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月11日
	 * @modifyDate 2016年5月11日
	 * 
	 */
	@Override
	public List<TvLayout> tvSubLayoutsForAPI(Category category) {
		if (!BaseModel.hasPersisted(category)) {
			return null;
		}
		return tvSubWidgetMapper.selectSubTvHomeLayouts(category.getId());
	}

}
