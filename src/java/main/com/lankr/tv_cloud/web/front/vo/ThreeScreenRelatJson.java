package com.lankr.tv_cloud.web.front.vo;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class ThreeScreenRelatJson {
	
	private int video;
	
	private int pdf;
	
	private String title;
	
	private String description;

	public int getVideo() {
		return video;
	}

	public void setVideo(int video) {
		this.video = video;
	}

	public int getPdf() {
		return pdf;
	}

	public void setPdf(int pdf) {
		this.pdf = pdf;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	 public static List<ThreeScreenRelatJson> getJsonList(String json){
		  if(json==null||json.isEmpty()){
			  return null;
		  }
		  Gson gson=new GsonBuilder().disableHtmlEscaping()
					.create();
		  try {
			List<ThreeScreenRelatJson> list = gson.fromJson(json,
						new TypeToken<List<ThreeScreenRelatJson>>() {}.getType());
			return list;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		 return null; 
	  }
	

}
