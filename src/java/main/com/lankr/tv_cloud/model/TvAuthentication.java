package com.lankr.tv_cloud.model;

import com.lankr.tv_cloud.web.GlobalUserCache;
import com.lankr.tv_cloud.web.auth.Authentic;

public class TvAuthentication extends APIAuthentication implements Authentic {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6288754583169320146L;

	// public TvBox getBox() {
	// return box;
	// }
	//
	// public void setBox(TvBox box) {
	// this.box = box;
	// }

	// private TvBox box;

	private User user;

	private String device;

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
