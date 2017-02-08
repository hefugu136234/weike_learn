package com.lankr.tv_cloud.vo.snapshot;

import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;

/**
 * @author mayuan
 * @modify By wang
 * @date 2016年6月14日
 */
public class UserItem extends AbstractItem<User> {
	
	private String showName;
	
	private String photo;
	
	public String getShowName() {
		return showName;
	}

	public String getPhoto() {
		return photo;
	}


	private String userName;

	private String avatar;

	public UserItem(User t) {
		super(t);
	}

	@Override
	public String getName() {
		return t.getNickname();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.vo.snapshot.AbstractItem#buildextra()
	 */
	@Override
	protected void buildextra() {
		userName = t.getUsername();
		this.uuid = OptionalUtils.traceValue(t,"uuid");
		this.showName = OptionalUtils.traceValue(t, "nickname");
		String photo = OptionalUtils.traceValue(t, "avatar");
		photo = WxUtil.getDefaultAvatar(photo);
		this.photo = photo ;
	}
}
