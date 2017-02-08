package com.lankr.tv_cloud.model;

import java.util.Date;

@SuppressWarnings("serial")
public class ActivityMessage extends BaseModel{
	private User send;
	private Date sendDate;
	private String body;
	private User receive;
	private int status;
	
	public int getStatus(){
		return status;
	}
	public void setStatus(int status){
		this.status = status;
	}
	public User getSend() {
		return send;
	}
	public void setSend(User send) {
		this.send = send;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public User getReceive() {
		return receive;
	}
	public void setReceive(User receive) {
		this.receive = receive;
	}
}
