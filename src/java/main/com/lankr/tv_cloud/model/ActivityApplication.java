package com.lankr.tv_cloud.model;

import java.util.Date;

public class ActivityApplication extends BaseModel {

	public static final int DELIVERY_TYPE = 1;// 快递方式

	public static final int NETDISH_TYPE = 2;// 网盘方式

	public static final int STATUS_PRODUCTED = 0; // 编号生成
	public static final int STATUS_RELATED = 1; // 编号绑定
	public static final int STATUS_RECEIVED = 3; // 收到作品
	public static final int STATUS_INITIAL = 4; // 初审
	public static final int STATUS_ENCODE = 5; // 作品转码
	public static final int STATUS_PROFESS = 6; // 专业审核
	public static final int STATUS_SUCCESS = 8; // 审核成功
	public static final int STATUS_FAILURE = 10; // 失败

	/**
	 * 
	 */
	private static final long serialVersionUID = 9150961663133420592L;

	private String name;

	private Date matchDate;

	private String code;

	private User user;

	private Activity activity;

	private Resource resource;

	private Category category;

	private int status;

	private int sendType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSendType() {
		return sendType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.model.BaseModel#apiUseable()
	 */

	@Override
	public boolean apiUseable() {
		return isActive();
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public boolean isBundled(){
		return resource != null;
	}
	// public static interface OnStatusChangedListener {
	// public void onStatusChanged(int originStatus, int currentStatus);
	// }
	
	public static boolean validStatus(int status){
		return status == STATUS_PRODUCTED ||
				status == STATUS_ENCODE ||
				status == STATUS_INITIAL ||
				status == STATUS_PROFESS ||
				status == STATUS_RECEIVED ||
				status == STATUS_SUCCESS ||
				status == STATUS_FAILURE;
	}
	
}
