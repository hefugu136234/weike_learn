package com.lankr.tv_cloud.web.api.webchat.vo;

import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class OupsDetail extends BaseAPIModel{
	
	private String uuid;
	
	private String speakerName;
	
	private String oupsTime;
	
	private String name;
	
	private String dateTime;
	
	private String desc;
	
	private int oupsStatus;
	
	private String oupsCode;
	
	private String fileId;
	
	private int resStatus;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSpeakerName() {
		return speakerName;
	}

	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
	}

	public String getOupsTime() {
		return oupsTime;
	}

	public void setOupsTime(String oupsTime) {
		this.oupsTime = oupsTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getOupsStatus() {
		return oupsStatus;
	}

	public void setOupsStatus(int oupsStatus) {
		this.oupsStatus = oupsStatus;
	}
	
	
	
	public String getOupsCode() {
		return oupsCode;
	}

	public void setOupsCode(String oupsCode) {
		this.oupsCode = oupsCode;
	}
	
	

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	

	public int getResStatus() {
		return resStatus;
	}

	public void setResStatus(int resStatus) {
		this.resStatus = resStatus;
	}

	public void buildData(ActivityApplication application){
		this.setUuid(application.getUuid());
		this.setOupsStatus(application.getStatus());
		String speaker=OptionalUtils.traceValue(application, "resource.speaker.name");
		if(speaker.isEmpty()){
			this.setSpeakerName(OptionalUtils.traceValue(application, "name"));
		}else{
			this.setSpeakerName(speaker);
		}
		int videoTime=OptionalUtils.traceInt(application, "resource.video.duration");
		this.setOupsTime(formatTime(videoTime));
		String name=OptionalUtils.traceValue(application, "resource.name");
		if(name.isEmpty()){
			this.setName(OptionalUtils.traceValue(application, "name"));
		}else{
			this.setName(name);
		}
		this.setDateTime(Tools.formatYMDHMSDate(application.getCreateDate()));
		String mark=OptionalUtils.traceValue(application, "resource.mark");
		if(mark.isEmpty()){
			this.setDesc(OptionalUtils.traceValue(application, "mark"));
		}else{
			this.setDesc(mark);
		}
		this.setOupsCode(application.getCode());
		this.setFileId(OptionalUtils.traceValue(application, "resource.video.fileId"));
		Resource resource=application.getResource();
		if(resource!=null){
			this.setResStatus(resource.getStatus());
		}else{
			this.setResStatus(0);
		}
		
	}
	
	public String formatTime(int time){
		if(time==0){
			return "00:00:00";
		}
		String value="";
		if(time<60){
			//秒
			value="00:00:"+repairZero(time);
		}else if(time<3600){
			//分
			int m=time/60;
			int s=time%60;
			value="00:"+repairZero(m)+":"+repairZero(s);
		}else{
			//小时
			int h=time/3600;
			int m=time%3600/60;
			int s=time%3600%60;
			value=repairZero(h)+":"+repairZero(m)+":"+repairZero(s);
		}
		return value;
		
	}
	
	public String repairZero(int num){
		if(num<10){
			return "0"+num;
		}
		return String.valueOf(num);
	}
	
	public static void main(String[] args) {
		OupsDetail detail=new OupsDetail();
		System.out.println(detail.formatTime(5400));
	}

}
