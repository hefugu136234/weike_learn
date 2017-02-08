package com.lankr.tv_cloud.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.lankr.tv_cloud.utils.Tools;

public class QrMessage {
	
	public final static int NEED_AUTH=1;//需要授权
	
	public final static int NO_AUTH=0;//不需要授权
	
	private String redictUrl;
	
	private int auth;
	
	private String title;
	
	private String cover;
	
	private String desc;

	public String getRedictUrl() {
		return redictUrl;
	}

	public void setRedictUrl(String redictUrl) {
		this.redictUrl = redictUrl;
	}

	public int getAuth() {
		return auth;
	}

	public void setAuth(int auth) {
		this.auth = auth;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public void buildData(String redictUrl,int auth,String title,String cover,String desc){
		this.setRedictUrl(redictUrl);
		this.setAuth(auth);
		this.setTitle(Tools.nullValueFilter(title));
		this.setCover(Tools.nullValueFilter(cover));
		this.setDesc(Tools.nullValueFilter(desc));
	}

	public String buildMessageJson(){
	    Gson gson =new  GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(this);
	}
	
	
	public static QrMessage jsonToMessage(String message){
		if(message==null||message.isEmpty()){
			return null;
		}
		Gson gson =new  GsonBuilder().disableHtmlEscaping().create();
		try {
			QrMessage qrMessage=gson.fromJson(message, QrMessage.class);
			return qrMessage;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
