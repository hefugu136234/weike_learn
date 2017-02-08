package com.lankr.tv_cloud.facade.impl.sub;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.lankr.tv_cloud.cache.CacheBucket;
import com.lankr.tv_cloud.facade.impl.ResourceFacadeImp;
import com.lankr.tv_cloud.model.Resource;

public class EhCacheResourceImpl extends ResourceFacadeImp {

	@Cacheable(value = CacheBucket.RESOURCEITEM, key = "#uuid")
	@Override
	public Resource getResourceByUuid(String uuid) {
		return super.getResourceByUuid(uuid);
	}
	
	@Cacheable(value = CacheBucket.RESOURCEITEMRELATED, key = "#resource.uuid")	
	@Override
	public List<Resource> getResourceRelated(Resource resource) {
		return super.getResourceRelated(resource);
	}
}
