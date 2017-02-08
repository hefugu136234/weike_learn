package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.api.webchat.util.WxResourceUtil;

public class BroadcastBookVo extends BaseAPIModel{
	
	private int currentBook;
	
	private int bookLimit;
	
	private String hasRes;
	
	private String resUuid;
	
	private String resUrl;
	
	
	
	
	


	public String getResUrl() {
		return resUrl;
	}

	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}

	public String getHasRes() {
		return hasRes;
	}

	public void setHasRes(String hasRes) {
		this.hasRes = hasRes;
	}

	public String getResUuid() {
		return resUuid;
	}

	public void setResUuid(String resUuid) {
		this.resUuid = resUuid;
	}

	public int getCurrentBook() {
		return currentBook;
	}

	public void setCurrentBook(int currentBook) {
		this.currentBook = currentBook;
	}

	public int getBookLimit() {
		return bookLimit;
	}

	public void setBookLimit(int bookLimit) {
		this.bookLimit = bookLimit;
	}
	
	
	public void buildSuccess(){
		this.setStatus(Status.SUCCESS);
	}
	
	public void buildError(String error){
		this.setStatus(Status.ERROR);
		this.setMessage(error);
	}
	
	public void buildRecord(Broadcast broadcast){
		this.setStatus("record");
		Resource resource=broadcast.getResource();
		//String mresurl=broadcast.getResourceUrl();
		if(resource!=null){
			this.setHasRes("hasRes");
			this.setResUuid(resource.getUuid());
			return ;
		}
		
//		if(mresurl!=null&&!mresurl.isEmpty()){
//			this.setHasRes("hasUrl");
//			this.setResUrl(mresurl);
//			return ;
//		}
		
		this.setHasRes("nores");
		this.setMessage("直播已结束，请稍后查看录播");
		
	}
	
	public void buildFaile(int limit,int current,String message){
		this.setStatus(Status.FAILURE);
		this.setBookLimit(limit);
		this.setCurrentBook(current);
		this.setMessage(message);
	}
	

}
