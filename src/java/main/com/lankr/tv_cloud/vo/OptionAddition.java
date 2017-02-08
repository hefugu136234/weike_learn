package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.utils.OptionalUtils;

public class OptionAddition {
	
	private String id;
	private String text;
	private String hospitalName;
	private String speakerName;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	
	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	
	

	public String getSpeakerName() {
		return speakerName;
	}

	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
	}

	public static List<OptionAddition> buildData(List<Manufacturer> list){
		List<OptionAddition> addList=new ArrayList<OptionAddition>();
		if(list==null||list.isEmpty()){
			return addList;
		}
		for(Manufacturer manufacturer:list){
			OptionAddition addition =new OptionAddition();
			addition.setId(manufacturer.getUuid());
			addition.setText(manufacturer.getName());
			addList.add(addition);
		}
		return addList;
	}
	
	public static List<OptionAddition> buildSpaker(List<Speaker> list){
		List<OptionAddition> addList=new ArrayList<OptionAddition>();
		if(list==null||list.isEmpty()){
			return addList;
		}
		for(Speaker speaker:list){
			OptionAddition addition =new OptionAddition();
			addition.setId(speaker.getUuid());
			addition.setText(speaker.getName());
			addition.setHospitalName(OptionalUtils.traceValue(speaker, "hospital.name"));
			addList.add(addition);
		}
		return addList;
	}
	
	public void buildBandResData(Resource resource){
		this.setId(resource.getUuid());
		String text=resource.getName();
		String speakerName=OptionalUtils.traceValue(resource, "speaker.name");
		if(speakerName.isEmpty()){
			speakerName="无";
		}
		text=text+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+speakerName;
		this.setText(text);
	}

}
