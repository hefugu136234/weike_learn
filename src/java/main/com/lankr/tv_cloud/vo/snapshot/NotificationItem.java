package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.Notification;

@SuppressWarnings("unused")
public class NotificationItem extends AbstractItem<Notification>{
	private UserItem send;
	private long sendDate;
	private String body;

	public NotificationItem(Notification t) {
		super(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() throws Exception {
		send = (UserItem) buildBaseModelProperty(t.getUser(),
				UserItem.class);
		sendDate = t.getSendDate().getTime();
		body = t.getBody();
	}


}
