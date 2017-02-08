/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月3日
 * 	@modifyDate 2016年6月3日
 *  
 */
package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Province;

/**
 * @author Kalean.Xiang
 *
 */
public class CityItem extends AbstractItem<City> {

	/**
	 * @param t
	 */
	public CityItem(City t) {
		super(t);
	}

	private AbstractItem<Province> province;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
		province = buildBaseModelProperty(t.getProvince(), ProvinceItem.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#createMarker()
	 */
	@Override
	public boolean createMarker() {
		/**
		 * @author Kalean.Xiang
		 * @createDate 2016年6月3日
		 * @modifyDate 2016年6月3日
		 * 
		 */
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#modifyMarker()
	 */
	@Override
	public boolean modifyMarker() {
		/**
		 * @author Kalean.Xiang
		 * @createDate 2016年6月3日
		 * @modifyDate 2016年6月3日
		 * 
		 */
		return false;
	}
}
