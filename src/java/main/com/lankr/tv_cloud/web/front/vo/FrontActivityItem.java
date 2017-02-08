package com.lankr.tv_cloud.web.front.vo;

import java.util.List;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityConfigMedia;
import com.lankr.tv_cloud.model.ActivityConfigMedia.Snapshot;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class FrontActivityItem {

	private String uuid;
	private String cover;
	private String name;
	private String kv;
	private String description;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKv() {
		return kv;
	}
	public void setKv(String kv) {
		this.kv = kv;
	}
	public void buildIndexActivity(Activity activity){
		this.setUuid(activity.getUuid());
		String json = OptionalUtils.traceValue(activity, "config.medias");
		String cover=getTypeSrc(json,ActivityConfigMedia.MEDIA_TV_COVER);
		this.setCover(cover);
	}
	
	public void buildCategeoryBanner(Activity activity){
		this.setUuid(activity.getUuid());
		String json = OptionalUtils.traceValue(activity, "config.medias");
		String kv=getTypeSrc(json,ActivityConfigMedia.MEDIA_TV_KV);
		this.setKv(kv);
	}
	
	public void buildDetail(Activity activity){
		this.setUuid(activity.getUuid());
		this.setName(OptionalUtils.traceValue(activity, "name"));
		String json = OptionalUtils.traceValue(activity, "config.medias");
		String kv=getTypeSrc(json,ActivityConfigMedia.MEDIA_TV_KV);
		this.setKv(kv);
		this.setDescription(OptionalUtils.traceValue(activity, "description"));
	}
	
	public String getTypeSrc(String json,int key){
		String val="";
		if(json==null||json.isEmpty())
			return val;
			try {
				List<ActivityConfigMedia> list = Snapshot.convertMedias(json);
				if(list!=null&&!list.isEmpty()){
					for (ActivityConfigMedia activityConfigMedia : list) {
						if(activityConfigMedia.getType()==key){
							val=activityConfigMedia.getUrl();
							break;
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			return val;
		}
	

}
