/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月19日
 * 	@modifyDate 2016年5月19日
 *  
 */
package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.MediaCentral;

/**
 * @author Kalean.Xiang
 *
 */
public interface MediaCentralMapper {

	public int add(MediaCentral mediaCentral);

	public int update(MediaCentral mediaCentral);

	public MediaCentral searchMediaCentral(int referType, int referId, int sign);

	public List<MediaCentral> searchMediaCentrals(int referTypeCategory, int id);
	
}
