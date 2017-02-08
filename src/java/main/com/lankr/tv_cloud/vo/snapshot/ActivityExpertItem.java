package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.ActivityExpert;

public class ActivityExpertItem extends AbstractItem<ActivityExpert> {

	// private String mark;
	private SpeakerItem speaker;

	public ActivityExpertItem(ActivityExpert t) {
		super(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
		// mark = t.getMark();
		// if (useable(t.getSpeaker())) {
		// speaker = new SpeakerItem(t.getSpeaker());
		// speaker.build();
		// }
		speaker = (SpeakerItem) buildBaseModelProperty(t.getSpeaker(),
				SpeakerItem.class);
	}
}
