package com.lankr.tv_cloud.support.rocketMQ;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.rocketmq.common.message.Message;

public class SendMessageToMQ implements Runnable{
	
	private String name ;
	
	private String type ;
	
	private String data ;
	
	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 
	 * @param name 资源类型 如 hospital resource
	 * @param type 操作类型  add update delete
	 * @param data 需要传输的数据，json格式
	 * @return
	 */
	public boolean sendMessage(String name, String type, String data) throws UnsupportedOperationException {
		if (data == null)
			return false ;
		String uuid = null ;
		String json = null ;
		try {
			JSONObject jsonObject = new JSONObject(data);
			if (jsonObject == null)
				return false ;
			json = jsonObject.getString("data") ;
			JSONObject jsonObj = new JSONObject(json);
			uuid = jsonObj.getString("uuid") ;
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		Message msg = new Message(name, type, uuid, json.getBytes()) ;
		try {
			Producer.instance().send(msg) ;
		} catch (Exception e) {
			e.printStackTrace();
			return false ;
		}
		return true ;
	}

	@Override
	public void run() {
		sendMessage(name, type, data) ;
	}
}
