package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.Praise;

@SuppressWarnings("unused")
public class UserPraiseItem extends AbstractItem<Praise>{
	private ResourceSimpleItem resource;
	private long date;

	public UserPraiseItem(Praise t) {
		super(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
		resource = (ResourceSimpleItem) buildBaseModelProperty(t.getResource(),
					ResourceSimpleItem.class);
		date = t.getCreateDate().getTime();
	}

}
