package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityConfigMedia;
import com.lankr.tv_cloud.model.ActivityConfigMedia.Snapshot;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.WxSubject;
import com.lankr.tv_cloud.model.WxSubject.WxSubjectProperty;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class WxSubjectVo extends BaseAPIModel{

	public String uuid;

	private String name;

	private String createDate;

	private String categroyName;
	
	private String categroyUuid;

	private int isStatus;

	private String cover;
	
	private int rootType;
	
	private int propertype;
	
	private String activityUuid;
	
	private String activityName;
	
	private boolean parent;
	
	
	public boolean isParent() {
		return parent;
	}

	public void setParent(boolean parent) {
		this.parent = parent;
	}

	public int getRootType() {
		return rootType;
	}

	public void setRootType(int rootType) {
		this.rootType = rootType;
	}

	public int getPropertype() {
		return propertype;
	}

	public void setPropertype(int propertype) {
		this.propertype = propertype;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCategroyName() {
		return categroyName;
	}

	public void setCategroyName(String categroyName) {
		this.categroyName = categroyName;
	}

	public int getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(int isStatus) {
		this.isStatus = isStatus;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}
	
	

	public String getCategroyUuid() {
		return categroyUuid;
	}

	public void setCategroyUuid(String categroyUuid) {
		this.categroyUuid = categroyUuid;
	}
	
	

	public String getActivityUuid() {
		return activityUuid;
	}

	public void setActivityUuid(String activityUuid) {
		this.activityUuid = activityUuid;
	}
	
	

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	
	public void buildParentList(WxSubject wxSubject,String parentUuid){
		String uuid=wxSubject.getUuid();
		this.setUuid(uuid);
		this.setName(OptionalUtils.traceValue(wxSubject, "name"));
		if(uuid.equals(parentUuid)){
			this.setParent(true);
		}
		
	}

	public void buildFirstList(WxSubject wxSubject,Category category) {
		this.setUuid(wxSubject.getUuid());
		this.setCreateDate(Tools.formatYMDHMSDate(wxSubject.getCreateDate()));
		this.setName(OptionalUtils.traceValue(wxSubject, "name"));
		this.setIsStatus(wxSubject.getStatus());
		this.setCategroyName(OptionalUtils.traceValue(category, "name"));
		WxSubjectProperty wxSubjectProperty=WxSubject.typeJsonForObject(wxSubject.getTypeProperty());
		this.setCover(OptionalUtils.traceValue(wxSubjectProperty, "cover"));
		this.setPropertype(OptionalUtils.traceInt(wxSubjectProperty,"type"));
	}
	
	public void buildFirstUpdate(WxSubject wxSubject,Category category){
		this.setUuid(wxSubject.getUuid());
		this.setName(OptionalUtils.traceValue(wxSubject, "name"));
		this.setCategroyName(OptionalUtils.traceValue(category, "name"));
		this.setCategroyUuid(OptionalUtils.traceValue(category, "uuid"));
		WxSubjectProperty wxSubjectProperty=WxSubject.typeJsonForObject(wxSubject.getTypeProperty());
		this.setCover(OptionalUtils.traceValue(wxSubjectProperty, "cover"));
		this.setPropertype(OptionalUtils.traceInt(wxSubjectProperty,"type"));
	}
	
	public void buildActivityList(WxSubject wxSubject) {
		this.setUuid(wxSubject.getUuid());
		this.setName(OptionalUtils.traceValue(wxSubject, "name"));
		this.setCreateDate(Tools.formatYMDHMSDate(wxSubject.getCreateDate()));
		this.setIsStatus(wxSubject.getStatus());
		WxSubjectProperty wxSubjectProperty=WxSubject.typeJsonForObject(wxSubject.getTypeProperty());
		this.setCover(OptionalUtils.traceValue(wxSubjectProperty, "cover"));
		this.setPropertype(OptionalUtils.traceInt(wxSubjectProperty,"type"));
	}
	
	public void buildActivityUpdate(WxSubject wxSubject,Activity activity){
		this.setUuid(wxSubject.getUuid());
		this.setName(OptionalUtils.traceValue(wxSubject, "name"));
		this.setIsStatus(wxSubject.getStatus());
		this.setActivityName(OptionalUtils.traceValue(activity, "name"));
		WxSubjectProperty wxSubjectProperty=WxSubject.typeJsonForObject(wxSubject.getTypeProperty());
		this.setCover(OptionalUtils.traceValue(wxSubjectProperty, "cover"));
		this.setPropertype(OptionalUtils.traceInt(wxSubjectProperty,"type"));
	}
	
	public void buildActivityCover(Activity activity){
		String json = OptionalUtils.traceValue(activity, "config.medias");
		if (json.isEmpty()) {
			this.setCover(json);
		} else {
			try {
				List<ActivityConfigMedia> list = Snapshot.convertMedias(json);
				if (list == null || list.isEmpty()) {
					this.setCover("");
				} else {
					for (ActivityConfigMedia activityConfigMedia : list) {
						if(activityConfigMedia.getType()==ActivityConfigMedia.MEDIA_WX_KV){
							this.setCover(activityConfigMedia.getUrl());
							break;
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				this.setCover("");
			}
		}
		this.setActivityName(OptionalUtils.traceValue(activity, "name"));
		this.setStatus(Status.SUCCESS);
	}
	

}
