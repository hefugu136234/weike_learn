/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月3日
 * 	@modifyDate 2016年6月3日
 *  
 */
package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.NormalCollect;

/**
 * @author Kalean.Xiang
 *
 */
public class CourseChaptersItem extends AbstractItem<NormalCollect> {

	private String description;

	private int level;

	private boolean isPrivate;

	private boolean needCertificated;

	private int passScore;

	/**
	 * @param t
	 */
	public CourseChaptersItem(NormalCollect t) {
		super(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
		description = t.getDescription();
		level = t.getLevel();
		isPrivate = t.isPrivate();
		needCertificated = t.needCertificated();
		passScore = t.getPassScore();
	}
}
