package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.MyCollection;
import com.lankr.tv_cloud.model.ResourceVoteAnswer;

/**
 * @author mayuan
 * @date 2016年7月12日
 */
@SuppressWarnings("unused")
public class ResourceVoteAnswerItem extends AbstractItem<ResourceVoteAnswer>{
	private UserItem user;
	private long date;

	public ResourceVoteAnswerItem(ResourceVoteAnswer t) {
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
