package com.lankr.orm.mybatis.mapper;
import java.util.List;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.SharingRes;
import com.lankr.tv_cloud.model.ViewSharing;

public interface ShareGiftMpper {
	
	public int getShareGiftCount(int resid);
	
	public void addShareRes(SharingRes sharingRes);
	
	public ViewSharing selectViewSharingExist(SubParams params);
	
	public SharingRes selectSharingResExist(SubParams params);
	
	public void addViewSharing(ViewSharing viewSharing);
	
	public void addViewShareCount(ViewSharing viewSharing);

	//modified by mayuan --> show sharedetail 
	public List<ViewSharing> searchAllPagination(SubParams params);

}
