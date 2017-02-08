/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年7月22日
 * 	@modifyDate 2016年7月22日
 *  
 */
package com.lankr.tv_cloud.qr;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.cache.lru.LruCache;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.model.QrInteractChannel;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;

/**
 * @author Kalean.Xiang
 *
 */
@Service(value = "qrSearchManagement")
public class QrSearchManagement extends AbstractQrManagement<QrInteractChannel> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.qr.AbstractQrManagement#cacheValid(com.lankr.tv_cloud
	 * .model.BaseModel)
	 */
	/**
	 * 
	 */
	public QrSearchManagement() {
		qrCache = new LruCache<Serializable, QrInteractChannel>() {
			@Override
			protected int configMaxMemorySize() {
				// TODO Auto-generated method stub
				return 5000;
			}
		};
	}

	@Override
	public boolean cacheValid(QrInteractChannel t) {
		return t.getStatus() == QrInteractChannel.STATUS_CREATED
				|| t.getStatus() == QrInteractChannel.STATUS_SCANNED;
	}

	@Override
	public ActionMessage<QrInteractChannel> onPersist(String ip, String device) {
		// TODO Auto-generated method stub
		QrInteractChannel qrInteractChannel = new QrInteractChannel();
		qrInteractChannel.setUuid(Tools.getUUID());
		qrInteractChannel.setCreateDate(Tools.getCurrentDate());
		qrInteractChannel.setModifyDate(Tools.getCurrentDate());
		qrInteractChannel.setReferType(QrInteractChannel.REFERTYPE_RESOURCE);
		qrInteractChannel.setIp(ip);
		qrInteractChannel.setDevice(device);
		qrInteractChannel.setTicket("key_" + Tools.getUUID());
		qrInteractChannel.setSign(QrInteractChannel.SIGN_SINGLE);
		qrInteractChannel.setStatus(QrInteractChannel.STATUS_CREATED);
		qrInteractChannel.setIsActive(QrInteractChannel.ACTIVE);
		ActionMessage<QrInteractChannel> actionMessage = qrAuthFacade
				.addQrInteractChannel(qrInteractChannel);
		actionMessage.setData(qrInteractChannel);
		return actionMessage;
	}

	public ActionMessage<QrInteractChannel> addSearchQrForResource(String ip,
			String device) {
		// TODO Auto-generated method stub
		return product(ip, device);
	}

	/**
	 * 轮询得到此二维码相关的最新数据
	 * 
	 * @param ip
	 * @param device
	 * @param uuid
	 * @return
	 */
	public ActionMessage<QrInteractChannel> pollingSearchQr(String ip,
			String device, String uuid) {
		device = Tools.subString(device, 50);
		ActionMessage message = codeProvider.code(200).getActionMessage();
		QrInteractChannel qrInteractChannel = qrCache.get(uuid, false);
		if (qrInteractChannel == null) {
			qrInteractChannel = qrAuthFacade.getQrInteractChannelByUuid(uuid);
		}
		if (qrInteractChannel == null) {
			return codeProvider.code(404).getActionMessage();
		}
		synchronized (qrInteractChannel.getDefaultLock()) {
			if (!ip.equals(qrInteractChannel.getIp())
					|| !device.equals(qrInteractChannel.getDevice())) {
				return codeProvider.code(400).getActionMessage();
			}
			long deadline = getExpiredTime();
			if (System.currentTimeMillis()
					- qrInteractChannel.getCreateDate().getTime() > deadline) {
				qrCache.remove(uuid);
				return codeProvider.code(401).getActionMessage();
			}
			// 客户端已经扫描
			if (qrInteractChannel.getStatus() == QrInteractChannel.STATUS_SCANNED) {
				return codeProvider.code(209).getActionMessage();
			}
			if (qrInteractChannel.getStatus() == QrInteractChannel.STATUS_INTERACT) {
				// 进入交互状态
				message = codeProvider.code(207).getActionMessage();
				// 拿到数据之后更新为已经完成
				message.setData(qrInteractChannel);
			}
		}
		return message;
	}

	// 扫描二维码
	public ActionMessage scanQrSearch(String uuid, User user) {
		QrInteractChannel qrInteractChannel = qrCache.get(uuid, false);
		if (qrInteractChannel == null) {
			qrInteractChannel = qrAuthFacade.getQrInteractChannelByUuid(uuid);
		}
		if (qrInteractChannel == null) {
			return codeProvider.code(404).getActionMessage();
		}
		long deadline = TimeUnit.MINUTES.toMillis(Config.qr_search_deadline);
		if (System.currentTimeMillis()
				- qrInteractChannel.getCreateDate().getTime() > deadline) {
			qrCache.remove(uuid);
			return codeProvider.code(401).getActionMessage();
		}
		if (qrInteractChannel.getStatus() > QrInteractChannel.STATUS_SCANNED) {
			// 被扫描过的二维码过期
			return codeProvider.code(405).getActionMessage();
		}
		if (qrInteractChannel.getStatus() == QrInteractChannel.STATUS_CREATED) {
			if (user != null) {
				// 优先扫码记录
				qrInteractChannel.setScanUser(user);
			}
			qrInteractChannel.setStatus(QrInteractChannel.STATUS_SCANNED);
			qrAuthFacade.updateQrInteractChannel(qrInteractChannel);
		}
		return ActionMessage.successStatus();
	}

	public ActionMessage searchPageAlive(String uuid, String ticket) {
		QrInteractChannel qrInteractChannel = qrCache.get(uuid, false);
		if (qrInteractChannel == null) {
			qrInteractChannel = qrAuthFacade.getQrInteractChannelByUuid(uuid);
		}
		if (qrInteractChannel == null) {
			return codeProvider.code(404).getActionMessage();
		}
		long deadline = TimeUnit.MINUTES.toMillis(Config.qr_search_deadline);
		if (System.currentTimeMillis()
				- qrInteractChannel.getCreateDate().getTime() > deadline) {
			qrCache.remove(uuid);
			return codeProvider.code(401).getActionMessage();
		}
		if (qrInteractChannel.getStatus() == QrInteractChannel.STATUS_SCANNED) {
			return ActionMessage.successStatus();
		}
		if (qrInteractChannel.getStatus() == QrInteractChannel.STATUS_INTERACT) {
			if (qrInteractChannel.getLastScanDate() == null) {
				qrInteractChannel.setLastScanDate(Tools.getCurrentDate());
				qrAuthFacade.updateQrInteractChannel(qrInteractChannel);
				return ActionMessage.successStatus();
			} else {
				ticket = Tools.nullValueFilter(ticket);
				if (ticket.equals(qrInteractChannel.getTicket())) {
					qrInteractChannel.setLastScanDate(Tools.getCurrentDate());
					qrAuthFacade.updateQrInteractChannel(qrInteractChannel);
					return ActionMessage.successStatus();
				} else {
					// 已被抢占，过期,给一个15s交错时间
					long interlock = TimeUnit.SECONDS.toMillis(15);
					if (System.currentTimeMillis()
							- qrInteractChannel.getLastScanDate().getTime() > interlock) {
						return codeProvider.code(406).getActionMessage();
					}
					return ActionMessage.successStatus();
				}
			}
		}
		if (qrInteractChannel.getStatus() > QrInteractChannel.STATUS_INTERACT) {
			return codeProvider.code(401).getActionMessage();
		}
		return ActionMessage.successStatus();
	}

	// 扫搜资源投屏操作
	public ActionMessage<QrInteractChannel> shadowAction(String uuid,
			int referId, String ticket) {
		ActionMessage message = codeProvider.code(200).getActionMessage();
		QrInteractChannel qrInteractChannel = qrCache.get(uuid, false);
		if (qrInteractChannel == null) {
			qrInteractChannel = qrAuthFacade.getQrInteractChannelByUuid(uuid);
		}
		if (qrInteractChannel == null) {
			return codeProvider.code(404).getActionMessage();
		}
		long deadline = TimeUnit.MINUTES.toMillis(Config.qr_search_deadline);
		if (System.currentTimeMillis()
				- qrInteractChannel.getCreateDate().getTime() > deadline) {
			qrCache.remove(uuid);
			return codeProvider.code(401).getActionMessage();
		}
		if (qrInteractChannel.getStatus() == QrInteractChannel.STATUS_SCANNED) {
			// 二维码处于扫码阶段
			qrInteractChannel.setReferId(referId);
			qrInteractChannel.setStatus(QrInteractChannel.STATUS_INTERACT);
			qrInteractChannel.setLastScanDate(Tools.getCurrentDate());
			qrAuthFacade.updateQrInteractChannel(qrInteractChannel);
			message = codeProvider.code(210).getActionMessage();
			message.setData(qrInteractChannel);
			return message;
		}
		if (qrInteractChannel.getStatus() == QrInteractChannel.STATUS_INTERACT) {
			// 处于交互状态时，使用优先抢占原则
			ticket = Tools.nullValueFilter(ticket);
			if (ticket.equals(qrInteractChannel.getTicket())) {
				if (qrInteractChannel.getReferId() != referId) {
					qrInteractChannel.setReferId(referId);
					qrAuthFacade.updateQrInteractChannel(qrInteractChannel);
				}
				message = codeProvider.code(210).getActionMessage();
				message.setData(qrInteractChannel);
			} else {
				// 已被抢占
				message = codeProvider.code(407).getActionMessage();
			}
			return message;
		}
		if (qrInteractChannel.getStatus() > QrInteractChannel.STATUS_INTERACT) {
			message = codeProvider.code(401).getActionMessage();
			return message;
		}
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.qr.AbstractQrManagement#getExpiredTime()
	 */
	@Override
	public long getExpiredTime() {
		return TimeUnit.MINUTES.toMillis(Config.qr_search_deadline);
	}

}
