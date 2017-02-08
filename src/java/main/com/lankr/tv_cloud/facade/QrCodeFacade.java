package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Lottery;
import com.lankr.tv_cloud.model.QrCode;
import com.lankr.tv_cloud.model.QrScene;
import com.lankr.tv_cloud.model.QrcodeScanRecode;
import com.lankr.tv_cloud.model.Resource;

public interface QrCodeFacade {

	/**
	 * 2016-4-2 修改 所有二维码的统一入口
	 * 
	 * @param activity
	 * @return
	 */

	// 后台二维码table列表数据
	public Pagination<QrScene> searchQrsenceForTable(String searchValue,
			int from, int size, String limitType, String judyType);

	// 通过senceid找到qrcode
	public QrCode selectQrCodeByScenId(long scenid);

	/**
	 * @he 2016-4-19 二维码的新生成方式
	 */
	// 查询对应的二维码，对应类型，对应业务的二维码场景
	public QrScene getQrSceneByTypeBuss(int reflectId, int businessId, int type);

	public ActionMessage addQrsence(QrScene qrScene);

	public Status addQrcodeScanRecode(QrcodeScanRecode qrcodeScanRecode);

	public QrcodeScanRecode selectRecodeByUser(int userId, int qrsceneId);

	public Status updateQrcodeScanRecode(QrcodeScanRecode qrcodeScanRecode);

	public QrScene selectQrSceneByScenId(long scenid);

	public Status updateQrSaoCount(QrCode qrCode);

	public QrScene selectQrSceneByUuid(String uuid);

	public Status updateQrScene(QrScene qrScene);

	public Pagination<QrcodeScanRecode> searchQrcodeScanRecodeForTable(
			int qrsceneId, String searchValue, int from, int size);
	
	public QrCode getQrByActivity(Activity activity);
	
	public QrCode getQrByResource(Resource resource);
	
	public QrCode getQrByCast(Broadcast broadcast);
	
	public QrCode getQrByGame(Lottery lottery);

}
