package com.lankr.tv_cloud.model;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class CastBanner {
	
	public static final String  BG_TV="bg_tv";
	
	public static final String  BG_WX="bg_wx";
	
	public static final String  BANNER_TV="banner_tv";
	
	public static final String  BANNER_WX="banner_wx";
	
	public static final String  COVER_TV="cover_tv";
	
	public static final String  COVER_WX="cover_wx";
	
	private String type;
	
	private String url;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
  public static String getImage(String json,String key){
	  String url=null;
	  if(json==null){
		  return url;
	  }
	  Gson gson=new Gson();
	  try {
		List<CastBanner> castBannerList = gson.fromJson(json,
					new TypeToken<List<CastBanner>>() {}.getType());
		for (CastBanner castBanner : castBannerList) {
			if(castBanner.getType().equals(key)){
				url=castBanner.getUrl();
				break;
			}
		}
	} catch (JsonSyntaxException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	}
	 return url; 
  }
  

}
