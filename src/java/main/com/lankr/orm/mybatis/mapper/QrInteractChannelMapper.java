package com.lankr.orm.mybatis.mapper;

import com.lankr.tv_cloud.model.QrInteractChannel;

public interface QrInteractChannelMapper {
	
	public int addQrInteractChannel(QrInteractChannel qrInteractChannel);
	
	public QrInteractChannel getQrInteractChannelByUuid(String uuid);
	
	public QrInteractChannel getQrInteractChannelByTicket(String ticket);
	
	public int updateQrInteractChannel(QrInteractChannel qrInteractChannel);

}
