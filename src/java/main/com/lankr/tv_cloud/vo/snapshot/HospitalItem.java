/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月2日
 * 	@modifyDate 2016年6月2日
 *  
 */
package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.Hospital;

/**
 * @author Kalean.Xiang
 *
 */
public class HospitalItem extends AbstractItem<Hospital> {

	private String grade;

	private String address;

	private CityItem city;

	/**
	 * @param t
	 */
	public HospitalItem(Hospital t) {
		super(t);
	}

	@Override
	public String getName() {
		return t.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#createMarker()
	 */
	@Override
	public boolean createMarker() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() {
		grade = t.getGrade();
		address = t.getAddress();
		city = (CityItem) buildBaseModelProperty(t.getCity(), CityItem.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#build()
	 */

}
