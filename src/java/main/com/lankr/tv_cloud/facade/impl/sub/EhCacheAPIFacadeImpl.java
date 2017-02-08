/** 
 *  author Kalean.Xiang
 *  createDate: 2016年3月21日
 * 	modifyDate: 2016年3月21日
 *  
 */
package com.lankr.tv_cloud.facade.impl.sub;

import org.springframework.cache.annotation.Cacheable;

import com.lankr.tv_cloud.cache.CacheBucket;
import com.lankr.tv_cloud.facade.impl.APIFacadeImpl;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.vo.ActivityTotalApiData;

/**
 * @author Kalean.Xiang
 *
 */
public class EhCacheAPIFacadeImpl extends APIFacadeImpl{

	/* (non-Javadoc)
	 * @see com.lankr.tv_cloud.facade.impl.APIFacadeImpl#activityCompletedJson(com.lankr.tv_cloud.model.Activity)
	 */
	@Override
	/**
	 * author Kalean.Xiang 
	 * createDate: 2016年3月18日 
	 * 缓存30秒后失效
	 * 
	 */
	@Cacheable(value = CacheBucket.ACTIVITYAPICOMPLETED, key = "#activity.uuid")
	public ActivityTotalApiData activityCompletedJson(Activity activity) {
		// TODO Auto-generated method stub
		return super.activityCompletedJson(activity);
	}
}
