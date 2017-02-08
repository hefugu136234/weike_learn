package com.lankr.tv_cloud.web.api.webchat.util;

import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.web.api.webchat.BaseWechatController;

public class WxResourceUtil {
	
	/**
	 * 2016-06-15 将废弃
	 * @param resource
	 * @return
	 */
	public static String getResourceUrI(Resource resource){
		String uri="";
		String uuid=resource.getUuid();
		if (resource.getType() == Type.VIDEO) {
			uri=BaseWechatController.WX_PRIOR+"/video/local/"+uuid;
		} else if (resource.getType() == Type.PDF) {
			uri=BaseWechatController.WX_PRIOR+"/pdf/local/view/"+uuid;
		} else if (resource.getType() == Type.THREESCREEN) {
			uri=BaseWechatController.WX_PRIOR+"/threescree/local/view/"+uuid;
		} else if (resource.getType() == Type.NEWS) {
			uri=BaseWechatController.WX_PRIOR+"/news/detail/"+uuid;
		}
		return uri;
	}
	

}
