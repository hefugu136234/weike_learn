package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.CategoryExpand;
import com.lankr.tv_cloud.model.CategoryExpandStatus;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class CategoryExpandVo extends BaseAPIModel {

	private List<CategoryExpandSufaces> cateExpands;

	public void build(CategoryExpand categoryExpand) {
		cateExpands = new ArrayList<CategoryExpandSufaces>();
		if (categoryExpand == null || categoryExpand.getUuid() == null) {
			return;
		}
		if (categoryExpand.getWxTaskId() != null) {
			CategoryExpandSufaces suface = new CategoryExpandSufaces();
			suface.setTypeId(CategoryExpandStatus.WX_PLATFORM);
			suface.setTaskId(categoryExpand.getWxTaskId());
			cateExpands.add(suface);
		}
		if (categoryExpand.getAppTaskId() != null) {
			CategoryExpandSufaces suface = new CategoryExpandSufaces();
			suface.setTypeId(CategoryExpandStatus.APP_PLATFORM);
			suface.setTaskId(categoryExpand.getAppTaskId());
			cateExpands.add(suface);
		}
		if (categoryExpand.getTvTaskId() != null) {
			CategoryExpandSufaces suface = new CategoryExpandSufaces();
			suface.setTypeId(CategoryExpandStatus.TV_PLATFORM);
			suface.setTaskId(categoryExpand.getTvTaskId());
			cateExpands.add(suface);
		}
		if (categoryExpand.getWebTaskId() != null) {
			CategoryExpandSufaces suface = new CategoryExpandSufaces();
			suface.setTypeId(CategoryExpandStatus.WEB_PLATFORM);
			suface.setTaskId(categoryExpand.getWebTaskId());
			cateExpands.add(suface);
		}

	}

	public class CategoryExpandSufaces {

		private String uuid;
		// 是哪个平台的类型1=wx 2=app 3=tv 4=web平台
		private Integer typeId;

		private String taskId;

		private String byname;

		private Integer isStatus;

		private Integer affect;

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		public Integer getTypeId() {
			return typeId;
		}

		public void setTypeId(Integer typeId) {
			this.typeId = typeId;
		}

		public String getTaskId() {
			return taskId;
		}

		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}

		public String getByname() {
			return byname;
		}

		public void setByname(String byname) {
			this.byname = byname;
		}

		public Integer getIsStatus() {
			return isStatus;
		}

		public void setIsStatus(Integer isStatus) {
			this.isStatus = isStatus;
		}

		public Integer getAffect() {
			return affect;
		}

		public void setAffect(Integer affect) {
			this.affect = affect;
		}

		// public void buildData(CategoryExpand categoryExpand) {
		// this.setUuid(categoryExpand.getUuid());
		// this.setAffect(categoryExpand.getAffect());
		// this.setByname(Tools.nullValueFilter(categoryExpand.getByname()));
		// this.setIsStatus(categoryExpand.getStatus());
		// this.setTypeId(categoryExpand.getTypeId());
		// if (categoryExpand.getWxTaskId() == CategoryExpandStatus.WX_PLATFORM)
		// {
		// this.setTaskId(categoryExpand.getWxTaskId());
		// }
		// if (categoryExpand.getTypeId() == CategoryExpandStatus.APP_PLATFORM)
		// {
		// this.setTaskId(categoryExpand.getAppTaskId());
		// } else if (categoryExpand.getTypeId() ==
		// CategoryExpandStatus.TV_PLATFORM) {
		// this.setTaskId(categoryExpand.getTvTaskId());
		// } else if (categoryExpand.getTypeId() ==
		// CategoryExpandStatus.WEB_PLATFORM) {
		// this.setTaskId(categoryExpand.getWebTaskId());
		// }
		// }

	}

}
