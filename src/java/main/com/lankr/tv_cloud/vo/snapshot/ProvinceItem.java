/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月3日
 * 	@modifyDate 2016年6月3日
 *  
 */
package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.Province;

/**
 * @author Kalean.Xiang
 *
 */
public class ProvinceItem extends AbstractItem<Province> {

	/**
	 * @param t
	 */
	public ProvinceItem(Province t) {
		super(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#modifyMarker()
	 */
	@Override
	public boolean modifyMarker() {
		return false;
	}

	public boolean createMarker() {
		return false;
	}

}
