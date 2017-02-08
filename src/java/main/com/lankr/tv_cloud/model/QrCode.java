package com.lankr.tv_cloud.model;

import java.util.Date;

public class QrCode extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6012996448233629990L;

	private Date deadline;
	
	private String title;
	
	private String qrurl;
	
	private int scancount;
	
	private long sceneid;
	
	private int status;

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQrurl() {
		return qrurl;
	}

	public void setQrurl(String qrurl) {
		this.qrurl = qrurl;
	}

	public int getScancount() {
		return scancount;
	}

	public void setScancount(int scancount) {
		this.scancount = scancount;
	}

	public long getSceneid() {
		return sceneid;
	}

	public void setSceneid(long sceneid) {
		this.sceneid = sceneid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
