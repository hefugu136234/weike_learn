package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.Message;

@SuppressWarnings("unused")
public class MessageItem extends AbstractItem<Message>{
	private UserItem receive;
	private long createDate;
	private String body;

	public MessageItem(Message t) {
		super(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
		receive = (UserItem) buildBaseModelProperty(t.getUser(),
				UserItem.class);
		createDate = t.getCreateDate().getTime();
		body = t.getBody();
	}

}
