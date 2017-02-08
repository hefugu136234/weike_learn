/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月29日
 * 	@modifyDate 2016年6月29日
 *  
 */
package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.model.QrInteractChannel;
import com.lankr.tv_cloud.model.TvQrAuth;

/**
 * @author Kalean.Xiang
 *
 */
public interface QrAuthFacade {

	public ActionMessage<TvQrAuth> addQrAuth(TvQrAuth auth);

	public TvQrAuth getQrAuthByUuid(String uuid);

	public ActionMessage update(TvQrAuth auth);

	public TvQrAuth getAuthByTicket(String key);

	public ActionMessage<QrInteractChannel> addQrInteractChannel(
			QrInteractChannel qrInteractChannel);

	public QrInteractChannel getQrInteractChannelByUuid(String uuid);

	public QrInteractChannel getQrInteractChannelByTicket(String ticket);
	
	public ActionMessage<?> updateQrInteractChannel(QrInteractChannel qrInteractChannel);
}
