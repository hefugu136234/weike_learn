/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月29日
 * 	@modifyDate 2016年6月29日
 *  
 */
package com.lankr.tv_cloud.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.orm.mybatis.mapper.QrAuthMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.QrAuthFacade;
import com.lankr.tv_cloud.model.QrInteractChannel;
import com.lankr.tv_cloud.model.TvQrAuth;
import com.lankr.tv_cloud.support.qiniu.QiniuUtils;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;

/**
 * @author Kalean.Xiang
 *
 */
public class QrAuthFacadeImpl extends FacadeBaseImpl implements QrAuthFacade {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.QrAuthFacade#addQrAuth(com.lankr.tv_cloud.model
	 * .TvQrAuth)
	 */
	private QrAuthMapper qrAuthMapper;

	@Autowired
	public void setQrAuthMapper(QrAuthMapper qrAuthMapper) {
		this.qrAuthMapper = qrAuthMapper;
	}
	

	@Override
	public ActionMessage<TvQrAuth> addQrAuth(TvQrAuth auth) {
		try {
			String authLink = WebChatMenu.LOCAL_URL + "/tv/auth/link?qr_uuid="
					+ auth.getUuid();
			String url = QiniuUtils.fetchQrUrl(authLink);
			auth.setQrUrl(url);
			qrAuthMapper.addQrAuth(auth);
			return codeProvider.codeOk().getActionMessage();
		} catch (Exception e) {
			logger.error("save tv qr auth error", e);
			return codeProvider.code(-1110).getActionMessage();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.impl.FacadeBaseImpl#namespace()
	 */
	@Override
	protected String namespace() {
		return QrAuthMapper.class.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.QrAuthFacade#getQrAuthByUuid(java.lang.String)
	 */
	@Override
	public TvQrAuth getQrAuthByUuid(String uuid) {
		return qrAuthMapper.getByUuid(uuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.QrAuthFacade#update(com.lankr.tv_cloud.model
	 * .TvQrAuth)
	 */
	@Override
	public ActionMessage update(TvQrAuth auth) {
		try {
			if (qrAuthMapper.updateQrAuth(auth) < 1) {
				throw new Exception();
			}
			return ActionMessage.successStatus();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("update qr_auth accur an error ", e);
		}
		return codeProvider.code(-1111).getActionMessage();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.QrAuthFacade#getAuthByTicket(java.lang.String)
	 */
	@Override
	public TvQrAuth getAuthByTicket(String key) {
		return qrAuthMapper.getAuthByTicket(key);
	}

	@Override
	public ActionMessage<QrInteractChannel> addQrInteractChannel(
			QrInteractChannel qrInteractChannel) {
		// TODO Auto-generated method stub
		try {
			String searchLink = WebChatMenu.LOCAL_URL + "/tv/search/resource/link?qr_uuid="
					+ qrInteractChannel.getUuid();
			String url = QiniuUtils.fetchQrUrl(searchLink);
			qrInteractChannel.setQrUrl(url);
			qrInteractChannelMapper.addQrInteractChannel(qrInteractChannel);
			return codeProvider.codeOk().getActionMessage();
		} catch (Exception e) {
			logger.error("save tv qr_interact_channel search resource error", e);
			return codeProvider.code(-1110).getActionMessage();
		}
	}
	


	@Override
	public QrInteractChannel getQrInteractChannelByUuid(String uuid) {
		// TODO Auto-generated method stub
		return qrInteractChannelMapper.getQrInteractChannelByUuid(uuid);
	}


	@Override
	public QrInteractChannel getQrInteractChannelByTicket(String ticket) {
		// TODO Auto-generated method stub
		return qrInteractChannelMapper.getQrInteractChannelByTicket(ticket);
	}


	@Override
	public ActionMessage<?> updateQrInteractChannel(
			QrInteractChannel qrInteractChannel) {
		// TODO Auto-generated method stub
		try {
			if (qrInteractChannelMapper.updateQrInteractChannel(qrInteractChannel) < 1) {
				throw new Exception();
			}
			return ActionMessage.successStatus();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("update qr_interact_channel accur an error ", e);
		}
		return codeProvider.code(-1111).getActionMessage();
	}
}
