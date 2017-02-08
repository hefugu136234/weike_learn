package com.lankr.tv_cloud.vo;

import java.util.List;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class ProjectListInfo extends DataTableModel<ProjectItem> {

	public void addItem(Project pro) {
		if (pro == null)
			return;					
		ProjectItem item = new ProjectItem();
		item.setApply(HtmlUtils.htmlEscape(pro.getApply()));
		item.setMark(HtmlUtils.htmlEscape(pro.getMark()));
		item.setProject_name(HtmlUtils.htmlEscape(pro.getProjectName()));
		item.setUuid(HtmlUtils.htmlEscape(pro.getUuid()));
		item.setCreate_date(Tools.df1.format(pro.getCreateDate()));
		aaData.add(item);
	}

	public void addItem(List<Project> projects) {
		if (projects != null && !projects.isEmpty()) {
			for (Project project : projects) {
				addItem(project);
			}
		}
	}
}
