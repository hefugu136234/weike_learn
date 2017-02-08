/** 
 *  @author Kalean.Xiang
 *  @createDate: 2016年3月28日
 * 	@modifyDate: 2016年3月28日
 *  
 */
package com.lankr.tv_cloud.facade.inner.resource;

import com.lankr.tv_cloud.model.Video;

/**
 * @author Kalean.Xiang
 *
 */
class VideoEmbed implements ResourceEmbed {

	Video mVideo = null;

	final static String APP_ID = "1251442335";

	VideoEmbed(){
		
	}
	
	VideoEmbed(Video video) {
		mVideo = video;
	}

	protected String getFileId() {
		return mVideo.getFileId();
	}

	@Override
	public String iframe() {
		StringBuffer sb = new StringBuffer("");
		sb.append(
				"<iframe src=\"http://play.video.qcloud.com/iplayer.html?$appid="
						+ APP_ID)
				.append("&$fileid=" + getFileId())
				.append("&$autoplay=0&$sw=1920&$sh=1080\" frameborder=\"0\" width=\"100%\" height=\"1080\" scrolling=\"no\"></iframe>")
				.append("<script src=\"http://qzonestyle.gtimg.cn/open/qcloud/video/h5/fixifmheight.js\" charset=\"utf-8\"></script>");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.inner.resource.ResourceEmbed#flash()
	 */
	@Override
	public String flash() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.inner.resource.ResourceEmbed#html()
	 */
	@Override
	public String html() {
		// TODO Auto-generated method stub
		return null;
	}

}
