package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.MyCollection;

@SuppressWarnings("unused")
public class CollectionUserItem extends AbstractItem<MyCollection>{
	private UserItem user;
	private long date;

	public CollectionUserItem(MyCollection t) {
		super(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
		user = (UserItem) buildBaseModelProperty(t.getUser(),
				UserItem.class);
		date = t.getCreateDate().getTime();
	}

}
