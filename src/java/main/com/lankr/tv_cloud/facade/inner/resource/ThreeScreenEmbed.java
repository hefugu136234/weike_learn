/** 
 *  @author Kalean.Xiang
 *  @createDate: 2016年3月28日
 * 	@modifyDate: 2016年3月28日
 *  
 */
package com.lankr.tv_cloud.facade.inner.resource;

import com.lankr.tv_cloud.model.ThreeScreen;

public class ThreeScreenEmbed extends VideoEmbed {

	ThreeScreen mThreeScreen;

	ThreeScreenEmbed(ThreeScreen threeScreen) {
		mThreeScreen = threeScreen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.inner.resource.VideoEmbed#getFileId()
	 */
	@Override
	protected String getFileId() {
		return mThreeScreen.getFileId();
	}

}
