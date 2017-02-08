package com.lankr.tv_cloud.facade.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.QrCodeFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Lottery;
import com.lankr.tv_cloud.model.QrCode;
import com.lankr.tv_cloud.model.QrMessage;
import com.lankr.tv_cloud.model.QrScene;
import com.lankr.tv_cloud.model.QrcodeScanRecode;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.support.qiniu.QiniuUtils;
import com.lankr.tv_cloud.support.qiniu.storage.BucketManager;
import com.lankr.tv_cloud.support.qiniu.storage.UploadManager;
import com.lankr.tv_cloud.support.qiniu.storage.model.DefaultPutRet;
import com.lankr.tv_cloud.support.qiniu.util.Auth;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;
import com.lankr.tv_cloud.web.api.webchat.util.WxBusinessCommon;

public class QrCodeFacadeImp extends FacadeBaseImpl implements QrCodeFacade {

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "com.lankr.orm.mybatis.mapper.QrCodeMapper";
	}

	/**
	 * 活动作品直接扫描的二维码
	 */
	// @Override
	// public QrCode getQrByActiveOups(Activity activity) {
	// // TODO Auto-generated method stub
	// QrCode qrCode = null;
	// if (activity == null || !activity.isActive()) {
	// return qrCode;
	// }
	// QrScene qrScene = getQrSceneByActivitId(activity.getId(),
	// QrScene.ACTIVTY_APPLY_QR_TYPE);
	// if (qrScene == null) {
	// // 没有就创建，有直接返回二维码
	// // int limitType=QrScene.QR_FORVER;
	// AddQrResultMark mark = addQrForeverScene(activity.getId(),
	// QrScene.ACTIVTY_APPLY_QR_TYPE);
	// if (mark.isFlag()) {
	// qrCode = mark.getQrCode();
	// }
	// } else {
	// qrCode = qrCodeMapper.selectQrCodeByScenId(qrScene.getSceneid());
	// }
	// return qrCode;
	// }

	// @Override
	// public QrCode getQrByActiveOupsCommon() {
	// // TODO Auto-generated method stub
	// QrCode qrCode = null;
	// QrScene qrScene = qrCodeMapper
	// .getQrSceneByCommonOups(QrScene.ACTIVTY_APPLY_QR_COMMON);
	// if (qrScene == null) {
	// // 没有就创建，有直接返回二维码
	// // int limitType=QrScene.QR_FORVER;
	// AddQrResultMark mark = addQrForeverScene(0,
	// QrScene.ACTIVTY_APPLY_QR_COMMON);
	// if (mark.isFlag()) {
	// qrCode = mark.getQrCode();
	// }
	// } else {
	// qrCode = qrCodeMapper.selectQrCodeByScenId(qrScene.getSceneid());
	// }
	// return qrCode;
	// }

	public QrScene getQrSceneByActivitId(int activityId, int type) {
		SubParams params = new SubParams();
		params.setId(activityId);
		params.setType(type);
		return qrCodeMapper.getQrSceneByActivitId(params);
	}

	public QrScene getQrSceneTemp(int id, int type) {
		SubParams params = new SubParams();
		params.setId(id);
		params.setType(type);
		return qrCodeMapper.getQrSceneTemp(params);
	}

	public QrScene buildQrScene(int id, int type, long sceneid, int limitType,
			String redictUrl) {
		QrScene qrScene = new QrScene();
		qrScene.setUuid(Tools.getUUID());
		qrScene.setReflectId(id);
		qrScene.setSceneid(sceneid);
		qrScene.setType(type);
		qrScene.setLimitType(limitType);
		qrScene.setStatus(1);
		qrScene.setIsActive(1);
		return qrScene;
	}

	/**
	 * deadLine为过期时间，永久为null，无过期时间，临时为30天
	 * 
	 * @param title
	 * @param mark
	 * @param qrurl
	 * @param sceneid
	 * @param deadline
	 * @return
	 */
	public QrCode buildQrCode(String title, String mark, long sceneid,
			Date deadLine, String qrurl) {
		QrCode qrCode = new QrCode();
		qrCode.setUuid(Tools.getUUID());
		qrCode.setDeadline(deadLine);
		qrCode.setTitle(title);
		qrCode.setMark(mark);
		qrCode.setQrurl(qrurl);
		qrCode.setScancount(0);
		qrCode.setSceneid(sceneid);
		qrCode.setStatus(1);
		qrCode.setIsActive(1);
		return qrCode;
	}

	// 临时二维码的更新重新操作
	public QrCode updateQrTempScene(QrCode qrCode) {
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		String qrurl = WebChatMenu.createTempQr(qrCode.getSceneid(), 30, 3);
		// 临时二维码29天后过期
		long time = TimeUnit.DAYS.toMillis(29);
		time += System.currentTimeMillis();
		Date date = new Date();
		date.setTime(time);
		qrCode.setDeadline(date);
		qrCode.setQrurl(qrurl);
		try {
			transaction.execute(new TransactionCallback<Boolean>() {

				@Override
				public Boolean doInTransaction(TransactionStatus arg0) {
					qrCodeMapper.updateQrCodeNew(qrCode);
					return true;
				}

			});
			return qrCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public QrCode getQrByResource(Resource resource) {
		QrCode qrCode = null;
		if (resource == null) {
			return qrCode;
		}
		QrScene qrScene = getQrSceneByTypeBuss(resource.getId(),
				QrScene.DEFAULT_BUSINESSID, QrScene.RESOURCE_TYPE);
		if (qrScene == null) {
			// 创建
			// 生成二维码
			String uuid = Tools.getUUID();
			qrScene = new QrScene();
			qrScene.setLimitType(QrScene.QR_FORVER_SEFT);
			qrScene.setReflectId(resource.getId());
			qrScene.setBusinessId(QrScene.DEFAULT_BUSINESSID);
			qrScene.setType(QrScene.RESOURCE_TYPE);
//			qrScene.rootPath = rootPath;
//			qrScene.resourceUuid = resource.getUuid();
			qrScene.setUuid(uuid);
			String name = "资源：" + resource.getName();
			qrScene.setName(name);
			qrScene.setPinyin(Tools.getPinYin(name));
			qrScene.setStatus(BaseModel.APPROVED);
			qrScene.setIsActive(BaseModel.ACTIVE);
			QrMessage qrMessage = new QrMessage();
			qrMessage.buildData(
					"/api/webchat/resource/first/page/" + resource.getUuid(),
					QrMessage.NEED_AUTH, "", "", "");
			qrScene.setMessage(qrMessage.buildMessageJson());
			ActionMessage message = addQrsence(qrScene);
			if (message.isSuccess()) {
				qrScene = selectQrSceneByUuid(uuid);
				qrCode = selectQrCodeByScenId(qrScene.getSceneid());
			}
		} else {
			qrCode = selectQrCodeByScenId(qrScene.getSceneid());
		}
		return qrCode;
	}

	@Override
	public Pagination<QrScene> searchQrsenceForTable(String searchValue,
			int from, int size, String limitType, String judyType) {
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from qrscene where isActive=1 and (name like '%"
				+ searchValue + "%' or pinyin like '%" + searchValue + "%')";
		StringBuilder sBuilder = new StringBuilder(sql);
		if(StringUtils.isNotBlank(limitType) && NumberUtils.isNumber(limitType)){
            int tmp = Integer.valueOf(limitType);
            String tmpSql = " and limitType = " + tmp;
            sBuilder.append(tmpSql);
        }
		if(StringUtils.isNotBlank(judyType) && NumberUtils.isNumber(judyType)){
            int tmp = Integer.valueOf(judyType);
            String tmpSql = " and type = " + tmp;
            sBuilder.append(tmpSql);
        }
		Pagination<QrScene> pagination = initPage(sBuilder.toString(), from, size);
		List<QrScene> list = qrCodeMapper.searchQrsenceForTable(searchValue,
				from, size, limitType, judyType);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public QrCode selectQrCodeByScenId(long scenid) {
		// TODO Auto-generated method stub
		// 临时二维码重新更新
		QrCode qrCode = qrCodeMapper.selectQrCodeByScenId(scenid);
		if (qrCode != null) {
			Date deadlineDate = qrCode.getDeadline();
			if (deadlineDate == null) {
				return qrCode;
			} else {
				if (qrCode.getDeadline().after(new Date())) {
					return qrCode;
				} else {
					// 重新更新
					return updateQrTempScene(qrCode);
				}
			}
		}
		return null;
	}

	@Override
	public QrScene getQrSceneByTypeBuss(int reflectId, int businessId, int type) {
		// TODO Auto-generated method stub
		return qrCodeMapper.getQrSceneByTypeBuss(reflectId, businessId, type);
	}

	/**
	 * 2016-04-19 最新生成二维码的的方式 所有二维码的集合
	 */
	@Override
	public synchronized ActionMessage addQrsence(QrScene qrScene) {
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		if (qrScene == null) {
			ActionMessage.failStatus("数据出错");
		}
		int limitType = qrScene.getLimitType();
		Long sceneid = null;
		// 生成最新的sceneid
		if (limitType == QrScene.QR_FORVER) {
			// 微信永久
			sceneid = qrCodeMapper.selectMaxForeverSceneid();
			if (sceneid == null) {
				sceneid = Long.parseLong("1");
			} else {
				sceneid += 1;
			}
		} else {
			// 非微信永久
			sceneid = qrCodeMapper.selectMaxTempSceneid();
			if (sceneid == null) {
				sceneid = Long.parseLong("10001");
			} else {
				sceneid += 1;
			}
		}
		// 创建二维码
		String qrUrl = "";
		Date date = null;
		if (limitType == QrScene.QR_FORVER) {
			qrUrl = WebChatMenu.createLimitsceneQr(sceneid, 3);
		} else if (limitType == QrScene.QR_TEMP) {
			qrUrl = WebChatMenu.createTempQr(sceneid, 30, 3);
			// 临时二维码29天后过期
			long time = TimeUnit.DAYS.toMillis(29);
			time += System.currentTimeMillis();
			date = new Date();
			date.setTime(time);
		} else if (limitType == QrScene.QR_FORVER_SEFT) {
			QrMessage qrMessage = QrMessage.jsonToMessage(qrScene.getMessage());
			if (qrMessage == null) {
				return ActionMessage.failStatus("资源二维码信息错误");
			}
			String authUrl = WxBusinessCommon.qrAuthCommonUrl(
					qrMessage.getRedictUrl(),
					QrScene.isLoginByQrsence(qrScene.getType()), sceneid);
//			qrUrl = creatLocalQr(qrScene.rootPath, qrScene.resourceUuid,
//					authUrl);
			qrUrl=QiniuUtils.fetchQrUrl(authUrl);
		} else {
			return ActionMessage.failStatus("未知二维码类型");
		}

		if (qrUrl == null) {
			return ActionMessage.failStatus("二维码图片链接生成失败，请重新生成");
		}

		qrScene.setSceneid(sceneid);
		QrCode qrCode = buildQrCode(null, null, sceneid, date, qrUrl);
		try {
			transaction.execute(new TransactionCallback<Boolean>() {

				@Override
				public Boolean doInTransaction(TransactionStatus arg0) {
					// TODO Auto-generated method stub
					qrCodeMapper.addQrScene(qrScene);
					qrCodeMapper.addQrCode(qrCode);
					return true;
				}
			});
			return ActionMessage.successStatus();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ActionMessage.failStatus("二维码数据保存失败，请重新生成");
	}

	// 生成本地的二维码
	public String creatLocalQr(String rootPath, String uuid, String authUrl) {
		String folderPath = rootPath + File.separator + "assets"
				+ File.separator + "qr" + File.separator + uuid;
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(folder, uuid + ".png");
		if (!file.exists() || file.length() == 0) {
			// 创建二维码
			Tools.makeQr(file, authUrl);
		}
		String qrImageUrl = null;
		// 图片存储七牛
		try {
			Auth auth = Auth.create(Config.qn_access_key, Config.qn_secret_key);
			BucketManager bucketManager = new BucketManager(auth);
			// 二维码的网络路径
			String url = Config.host + "/assets/qr/" + uuid + "/" + uuid
					+ ".png";
			DefaultPutRet defaultPutRet = bucketManager.fetch(url,
					QiniuUtils.DEF_BUCKET);
			if (defaultPutRet != null && defaultPutRet.key != null
					&& !defaultPutRet.key.isEmpty()) {
				qrImageUrl = Config.qn_cdn_host + "/" + defaultPutRet.key;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return qrImageUrl;
	}

	@Override
	public Status addQrcodeScanRecode(QrcodeScanRecode qrcodeScanRecode) {
		// TODO Auto-generated method stub
		try {
			int effect = qrCodeMapper.addQrcodeScanRecode(qrcodeScanRecode);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public QrcodeScanRecode selectRecodeByUser(int userId, int qrsceneId) {
		// TODO Auto-generated method stub
		return qrCodeMapper.selectRecodeByUser(userId, qrsceneId);
	}

	@Override
	public Status updateQrcodeScanRecode(QrcodeScanRecode qrcodeScanRecode) {
		// TODO Auto-generated method stub
		try {
			int effect = qrCodeMapper.updateQrcodeScanRecode(qrcodeScanRecode);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public QrScene selectQrSceneByScenId(long scenid) {
		// TODO Auto-generated method stub
		return qrCodeMapper.selectQrSceneByScenId(scenid);
	}

	@Override
	public Status updateQrSaoCount(QrCode qrCode) {
		// TODO Auto-generated method stub
		try {
			qrCodeMapper.updateQrSaoCount(qrCode);
			;
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public QrScene selectQrSceneByUuid(String uuid) {
		// TODO Auto-generated method stub
		return qrCodeMapper.selectQrSceneByUuid(uuid);
	}

	@Override
	public Status updateQrScene(QrScene qrScene) {
		// TODO Auto-generated method stub
		try {
			qrCodeMapper.updateQrScene(qrScene);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public Pagination<QrcodeScanRecode> searchQrcodeScanRecodeForTable(
			int qrsceneId, String searchValue, int from, int size) {
		// TODO Auto-generated method stub
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(q.id) from qrcode_scan_recode q left join user u on q.userId=u.id where q.isActive=1 and q.qrsceneId="
				+ qrsceneId
				+ " and (u.username like '%"
				+ searchValue
				+ "%' or u.nickname like '%"
				+ searchValue
				+ "%' or u.phone like '%" + searchValue + "%')";
		Pagination<QrcodeScanRecode> pagination = initPage(sql, from, size);
		List<QrcodeScanRecode> list = qrCodeMapper
				.searchQrcodeScanRecodeForTable(qrsceneId, searchValue, from,
						size);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public QrCode getQrByActivity(Activity activity) {
		QrCode qrCode = null;
		if (activity == null || !activity.isActive()) {
			return qrCode;
		}
		QrScene qrScene = getQrSceneByTypeBuss(activity.getId(),
				QrScene.DEFAULT_BUSINESSID, QrScene.ACTIVTY_QR_TYPE);
		if (qrScene != null) {
			qrCode = selectQrCodeByScenId(qrScene.getSceneid());
		}
		return qrCode;
	}

	/**
	 * 直播的临时二维码
	 */
	@Override
	public QrCode getQrByCast(Broadcast broadcast) {
		QrCode qrCode = null;
		if (broadcast == null || !broadcast.isActive()) {
			return qrCode;
		}
		QrScene qrScene = getQrSceneByTypeBuss(broadcast.getId(),
				QrScene.DEFAULT_BUSINESSID, QrScene.CAST_QR_TYPE);
		if (qrScene != null) {
			qrCode = selectQrCodeByScenId(qrScene.getSceneid());
		}
		return qrCode;
	}

	@Override
	public QrCode getQrByGame(Lottery lottery) {
		QrCode qrCode = null;
		if (lottery == null || !lottery.isActive()) {
			return qrCode;
		}
		QrScene qrScene = getQrSceneByTypeBuss(lottery.getId(),
				QrScene.DEFAULT_BUSINESSID, QrScene.GAME_QR);
		if (qrScene != null) {
			qrCode = selectQrCodeByScenId(qrScene.getSceneid());
		}
		return qrCode;
	}

	public static void main(String[] args) {
//		long time = TimeUnit.DAYS.toMillis(29);
//		time += System.currentTimeMillis();
//		Date date = new Date();
//		date.setTime(time);
//		System.err.println(Tools.df1.format(date));
		
//		UploadManager um = new UploadManager();
//		Tools.makeQr(file, text);
//		um.put

	}
	
	

}
