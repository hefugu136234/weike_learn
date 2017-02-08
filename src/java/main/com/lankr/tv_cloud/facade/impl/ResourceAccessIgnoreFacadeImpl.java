package com.lankr.tv_cloud.facade.impl;

import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.ResourceAccessIgnoreFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ResourceAccessIgnore;

public class ResourceAccessIgnoreFacadeImpl extends FacadeBaseImpl implements
		ResourceAccessIgnoreFacade {
	@Override
	public ActionMessage add(ResourceAccessIgnore resAccess) {
		if (null == resAccess)
			return ActionMessage.failStatus("保存失败");
		int effect = 0;
		try {
			effect = resourceAccessIgnoreMapper.add(resAccess);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		if (effect > 0) {
			return ActionMessage.successStatus();
		}
		return ActionMessage.failStatus("保存失败");
	}

	@Override
	public ActionMessage del(ResourceAccessIgnore t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceAccessIgnore getByUuid(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionMessage update(ResourceAccessIgnore resAccess) {
		if (null == resAccess)
			return ActionMessage.failStatus("操作失败");
		int effect = 0;
		try {
			effect = resourceAccessIgnoreMapper.update(resAccess);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		if (effect > 0) {
			return ActionMessage.successStatus();
		}
		return ActionMessage.failStatus("操作失败");
	}

	@Override
	protected String namespace() {
		return this.getClass().getName();
	}

	@Override
	public ResourceAccessIgnore getByResourceId(int id) {
		ResourceAccessIgnore resourceAccess = null;
		try {
			resourceAccess = resourceAccessIgnoreMapper.getByResourceId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resourceAccess;
	}
}
