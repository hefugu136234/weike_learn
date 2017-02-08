package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.TvLayout;
import com.lankr.tv_cloud.model.Widget;

public interface ProjectFacade {

	public Status addProject(Project project);

	public Project getProjectByUuid(String uuid);

	public Pagination<Project> searchProjects(String q, int from, int to);

	public List<Project> fetchUserRefsProjects(int userId);

	public Status addWidgets(Project project, List<Widget> widgets);

	public List<Widget> selectProjectWidgets(Project project);

	public Status addSubTvLayout(TvLayout layout);

	public Status updateSubTvLayout(TvLayout layout);

	public List<TvLayout> selectTvLayoutsByCategory(Category category);

	public List<TvLayout> selectTvLayoutsUIData(Category category);

	public TvLayout getTvLayoutByUuid(String uuid);

	public Status changeLayoutStatus(String uuid, int status);

	public Status delLayout(String uuid);

	public List<TvLayout> selectProjectTvHomeLayouts(Project project);

	public ActionMessage saveProjectHomeLabel(Project project, String label,
			int type, Category category);

	public ActionMessage disableHomeLabel(TvLayout layout);

	public ActionMessage updateTvHomeLayoutWidgets(TvLayout layout);

	/**
	 * @author Kalean.Xiang 2016-3-6 added
	 * */
	public List<TvLayout> tvHomeLayoutsForAPI(Project project);

	
	/** 
	 *  @author Kalean.Xiang
	 *  @createDate 2016年5月11日
	 * 	@modifyDate 2016年5月11日
	 *  子布局接口
	 */
	public List<TvLayout> tvSubLayoutsForAPI(Category category);

	public ActionMessage homeLayoutToTop(TvLayout tl, int type);

	public List<TvLayout> selectWidgetTvHomeLayouts(Category category);

	public List<TvLayout> selectProjectTvWidgetLayouts(Project stubProject,
			int id);

}
