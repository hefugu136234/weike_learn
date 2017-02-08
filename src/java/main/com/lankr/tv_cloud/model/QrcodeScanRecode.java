package com.lankr.tv_cloud.model;

public class QrcodeScanRecode extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1334267456268355904L;

	private User user;
	
	private QrScene qrScene;
	
	private int scancount;
	
	private int viewcount;
	
	private int status;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public QrScene getQrScene() {
		return qrScene;
	}

	public void setQrScene(QrScene qrScene) {
		this.qrScene = qrScene;
	}

	public int getScancount() {
		return scancount;
	}

	public void setScancount(int scancount) {
		this.scancount = scancount;
	}

	public int getViewcount() {
		return viewcount;
	}

	public void setViewcount(int viewcount) {
		this.viewcount = viewcount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
