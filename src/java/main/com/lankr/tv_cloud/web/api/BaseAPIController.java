package com.lankr.tv_cloud.web.api;

import com.lankr.tv_cloud.facade.APIFacade;
import com.lankr.tv_cloud.facade.ResourceFacade;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.QrCode;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.ActivityTotalApiData;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.BaseController;

public class BaseAPIController extends BaseController {


	
	@Override
	protected ResourceFacade effectResourceFacade() {
		return cacheResourceFacade;

		// test
		// return resourceFacade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.web.BaseController#effectApiFacade()
	 */
	@Override
	protected APIFacade effectApiFacade() {
		return cacheAPIFacade;

		// test
		// return apiFacade;
	}

	

	

	protected String activityTVCompletedJson(Activity activity) {
		ActivityTotalApiData data = effectApiFacade().activityCompletedJson(
				activity);
		if (data == null)
			return failureModel("empty data").toJSON();
		if (Tools.isBlank(data.getQr())) {
			QrCode qrCode = qrCodeFacade.getQrByActivity(activity);
			if (qrCode != null)
				data.setQr(qrCode.getQrurl());
		}
		return data.toJSON();
	}

	@Override
	public BaseAPIModel playPrepare(Resource resource) {

		return null;
	}

	@Override
	public BaseAPIModel downloadPrepare(Resource resource) {
		return null;
	}



	

}
