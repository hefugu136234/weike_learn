package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ResourceAccessIgnore;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class ResourceAccessIgnoreVo extends BaseAPIModel {
	private List<String> permissions;

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public void build(ResourceAccessIgnore resourceAccessIgnore) {
		if (resourceAccessIgnore == null)
			return;
		setStatus(Status.SUCCESS);
		permissions = Tools.makeListWithStrUseSemicolon(resourceAccessIgnore
				.getDetail());
	}

}
