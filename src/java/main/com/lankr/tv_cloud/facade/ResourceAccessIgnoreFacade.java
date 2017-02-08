package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.model.ResourceAccessIgnore;

public interface ResourceAccessIgnoreFacade extends BaseFacade<ResourceAccessIgnore>{

	public ResourceAccessIgnore getByResourceId(int id);
}
