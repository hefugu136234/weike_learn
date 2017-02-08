/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月29日
 * 	@modifyDate 2016年6月29日
 *  
 */
package com.lankr.orm.mybatis.mapper;

import com.lankr.tv_cloud.model.TvQrAuth;

/**
 * @author Kalean.Xiang
 *
 */
public interface QrAuthMapper {

	public int addQrAuth(TvQrAuth auth);

	public TvQrAuth getByUuid(String uuid);
	
	public int updateQrAuth(TvQrAuth auth);
	
	public TvQrAuth getAuthByTicket(String key);
}
