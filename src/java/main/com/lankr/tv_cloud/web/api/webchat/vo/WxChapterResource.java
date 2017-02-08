package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceGroup;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.web.api.webchat.util.WxUtil;
import com.lankr.tv_cloud.web.front.util.FrontWxOpenUtil;

public class WxChapterResource {
	
	private String uuid;
	
	private String name;
	
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String cover;
	
	private String showMessage;
	
	private int learnCount;
	
	private int praiseCount;
	
	private boolean pass;
	
	private int logined;//0=未登录 1=登录
	
	private int disableClick;//0=不能点击 1=可以点击

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

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getShowMessage() {
		return showMessage;
	}

	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}

	public int getLearnCount() {
		return learnCount;
	}

	public void setLearnCount(int learnCount) {
		this.learnCount = learnCount;
	}
	
	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public int getLogined() {
		return logined;
	}

	public void setLogined(int logined) {
		this.logined = logined;
	}

	public int getDisableClick() {
		return disableClick;
	}

	public void setDisableClick(int disableClick) {
		this.disableClick = disableClick;
	}

	public void buildBaseData(ResourceGroup resourceGroup){
		this.setUuid(resourceGroup.getUuid());
		this.setName(OptionalUtils.traceValue(resourceGroup, "name"));
	}
	
	public void buildResource(Resource resource,int viewTime){
		this.setType(resource.getType().name());
		String cover = OptionalUtils.traceValue(resource, "coverTaskId");
		cover = WxUtil.getResourceCover(cover);
		if(this.getName().isEmpty()){
			this.setName(OptionalUtils.traceValue(resource, "name"));
		}
		this.setCover(cover);
		if (resource.getType() == Type.PDF) {
			String num = OptionalUtils.traceValue(resource, "pdf.pdfnum");
			num+="页";
			this.setShowMessage(num);
			if(viewTime>0){
				this.setPass(true);
			}
		} else if (resource.getType() == Type.THREESCREEN) {
			int videoTime=OptionalUtils.traceInt(resource, "threeScreen.videoTime");
			String time=FrontWxOpenUtil.videoTime(videoTime);
			this.setShowMessage(time);
			if(viewTime>=videoTime){
				this.setPass(true);
			}
		} else if(resource.getType() ==Type.VIDEO){
			int tmp = OptionalUtils.traceInt(resource, "video.duration");
			String time=FrontWxOpenUtil.videoTime(tmp);
			if(viewTime>=tmp){
				this.setPass(true);
			}
			this.setShowMessage(time);
		}
	}
	
	public void buildResource(Resource resource){
		this.setType(resource.getType().name());
		String cover = OptionalUtils.traceValue(resource, "coverTaskId");
		cover = WxUtil.getResourceCover(cover);
		if(this.getName().isEmpty()){
			this.setName(OptionalUtils.traceValue(resource, "name"));
		}
		this.setCover(cover);
		if (resource.getType() == Type.PDF) {
			String num = OptionalUtils.traceValue(resource, "pdf.pdfnum");
			num+="页";
			this.setShowMessage(num);
		} else if (resource.getType() == Type.THREESCREEN) {
			int videoTime=OptionalUtils.traceInt(resource, "threeScreen.videoTime");
			String time=FrontWxOpenUtil.videoTime(videoTime);
			this.setShowMessage(time);
		} else if(resource.getType() ==Type.VIDEO){
			int tmp = OptionalUtils.traceInt(resource, "video.duration");
			String time=FrontWxOpenUtil.videoTime(tmp);
			this.setShowMessage(time);
		}
	}

}
