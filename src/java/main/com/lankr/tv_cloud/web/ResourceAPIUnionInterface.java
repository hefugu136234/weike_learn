package com.lankr.tv_cloud.web;

import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public interface ResourceAPIUnionInterface {

	public Resource getResourceByUuid(ResourceFacade service, String uuid);

	/**
	 * 用于返回资源的播放信息
	 * 
	 * @return 播放的json数据
	 * */
	public BaseAPIModel playPrepare(Resource resource);
	/**
	 * 用于放回资源的下载信息
	 * 
	 * @return 资源的下载的json数据
	 * */

	public BaseAPIModel downloadPrepare(Resource resource);
	
	
}
