package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.QrCode;
import com.lankr.tv_cloud.model.QrScene;
import com.lankr.tv_cloud.model.QrcodeScanRecode;

public interface QrCodeMapper {
	
	public QrScene getQrSceneByActivitId(SubParams subParams);
	
	public Long  selectMaxForeverSceneid();
	
	public Long selectMaxTempSceneid();
	
	public void addQrScene(QrScene qrScene);
	
	public void addQrCode(QrCode qrCode);
	
	public QrScene selectQrSceneByScenId(long scenid);
	
	public QrCode selectQrCodeByScenId(long scenid);
	
	public void updateQrSaoCount(QrCode qrCode);
	
	//临时的二维码场景查询
	public QrScene getQrSceneTemp(SubParams subParams);
	
	public QrScene getQrSceneByCommonOups(int type);
	
	public int updateQrCodeNew(QrCode qrCode);
	
	public QrScene selectQrSenceByUuid(String uuid);
	
	public List<QrScene> searchQrsenceForTable(String searchValue, int from, int size, 
												@Param("limitType")String limitType, @Param("judyType")String judyType);
	
	public QrScene getQrSceneByTypeBuss(int reflectId, int businessId, int type);
	
	public int addQrcodeScanRecode(QrcodeScanRecode qrcodeScanRecode);
	
	public QrcodeScanRecode selectRecodeByUser(int userId,int qrsceneId);
	
	public int updateQrcodeScanRecode(QrcodeScanRecode qrcodeScanRecode);
	
	public QrScene selectQrSceneByUuid(String uuid);
	
	public int updateQrScene(QrScene qrScene);
	
	public List<QrcodeScanRecode> searchQrcodeScanRecodeForTable(int qrsceneId, String searchValue, int from, int size);

}
