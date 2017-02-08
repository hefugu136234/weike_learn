/** 
 *  @author Kalean.Xiang
 *  @createDate: 2016年3月28日
 * 	@modifyDate: 2016年3月28日
 *  
 */
package com.lankr.tv_cloud.facade.inner.resource;

import com.lankr.tv_cloud.Config;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;

public class GeneralResourceEmbed implements ResourceEmbed {

	public final static String TYPE_IFRAME = "iframe";
	public final static String TYPE_HTML = "html";
	public final static String TYPE_FLASH = "flash";
	private Resource mRes;

	public GeneralResourceEmbed(Resource res) {
		mRes = res;
	}

	public static ResourceEmbed getResourceEmbed(Resource resource)
			throws Exception {
		if (resource == null)
			return null;
		if (resource.getType() == Type.VIDEO) {
			return new GeneralResourceEmbed(resource);
		} else if (resource.getType() == Type.THREESCREEN) {
			return new GeneralResourceEmbed(resource);
		} else if (resource.getType() == Type.PDF) {
			return new GeneralResourceEmbed(resource);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.inner.resource.ResourceEmbed#iframe()
	 */
	@Override
	public String iframe() {
		return "<iframe height=480 width=640 src=\"" + Config.host
				+ "/project/resource/shared/" + mRes.getUuid()
				+ "\" frameborder=0 allowfullscreen></iframe>";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.inner.resource.ResourceEmbed#flash()
	 */
	@Override
	public String flash() {
		// TODO Auto-generated method stub
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
