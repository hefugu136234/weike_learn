/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年6月3日
 * 	@modifyDate 2016年6月3日
 *  
 */
package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.Speaker;

/**
 * @author Kalean.Xiang
 *
 */
public class NormalCollectItem extends InfiniteItem<NormalCollect> {

	private String description;

	private int level;

	private boolean isPrivate;

	private boolean needCertificated;

	private float version;

	private SpeakerItem speaker;

	private int passScore;

	private String type;

	/**
	 * @param t
	 */
	public NormalCollectItem(NormalCollect t) {
		super(t);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
		super.buildextra();
		description = t.getDescription();
		level = t.getLevel();
		isPrivate = t.isPrivate();
		needCertificated = t.needCertificated();
		version = t.getVersion();
		speaker = (SpeakerItem) buildBaseModelProperty(t.getSpeaker(),
				SpeakerItem.class);
		passScore = t.getPassScore();
		type = t.getType().name();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.InfiniteItem#makeParent()
	 */
	@Override
	public NormalCollect makeParent() {
		return t.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.InfiniteItem#childClass()
	 */
	@Override
	public Class<? extends InfiniteItem<NormalCollect>> childClass() {
		return getClass();
	}
}
