package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

public class QQVodPlayData {
	
	private int code;

	private String message;
	
	private List<QQVodPlayItem> playSet;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<QQVodPlayItem> getPlaySet() {
		return playSet;
	}

	public void setPlaySet(List<QQVodPlayItem> playSet) {
		this.playSet = playSet;
	}
	
	public static String vrPlayUrl(String json){
		QQVodPlayData data=palyData(json);
		if(data==null)
			return "";
		if(Tools.isEmpty(data.getPlaySet()))
			return "";
		Map<Integer, QQVodPlayItem> map=new HashMap<Integer, QQVodPlayItem>();
		for (QQVodPlayItem item : data.getPlaySet()) {
			map.put(item.getDefinition(), item);
		}
		QQVodPlayItem targe=map.get(30);
		String targeUrl=OptionalUtils.traceValue(targe, "url");
		if(!Tools.isBlank(targeUrl)){
			return targeUrl;
		}
		targe=map.get(20);
		targeUrl=OptionalUtils.traceValue(targe, "url");
		if(!Tools.isBlank(targeUrl)){
			return targeUrl;
		}
		targe=map.get(0);
		targeUrl=OptionalUtils.traceValue(targe, "url");
		return targeUrl;
		
	}
	
	public static QQVodPlayData palyData(String json){
		if(Tools.isBlank(json))
			return null;
		Gson gson=new Gson();
		return gson.fromJson(json, QQVodPlayData.class);
	}
 	
	

}
