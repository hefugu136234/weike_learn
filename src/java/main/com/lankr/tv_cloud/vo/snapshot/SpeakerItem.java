/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月2日
 * 	@modifyDate 2016年6月2日
 *  
 */
package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Speaker;

/**
 * @author Kalean.Xiang
 *
 */
public class SpeakerItem extends AbstractItem<Speaker> {

	/**
	 * @param t
	 */

	private String avatar;

	private String position;

	private String resume;

	private String departmentName;

	private HospitalItem hospital;

	public SpeakerItem(Speaker t) {
		super(t);
	}

	@Override
	public String getName() {
		return t.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() {
		hospital = (HospitalItem) buildBaseModelProperty(t.getHospital(),
				HospitalItem.class);
		avatar = traceValue("avatar");
		position = traceValue("position");
		resume = traceValue("resume");
		departmentName = traceValue("department.name");
	}
}
