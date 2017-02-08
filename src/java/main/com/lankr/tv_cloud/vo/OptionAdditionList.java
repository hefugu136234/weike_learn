package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Lottery;
import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.ProductGroup;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class OptionAdditionList extends BaseAPIModel{
	
	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	private List<OptionAddition> items;
	private String q;

	public List<OptionAddition> getItems() {
		return items;
	}

	public void setItems(List<OptionAddition> items) {
		this.items = items;
	}
	
	public void buildData(List<Manufacturer> list){
		items=new ArrayList<OptionAddition>();
		if(list==null||list.isEmpty()){
			return ;
		}
		for(Manufacturer manufacturer:list){
			OptionAddition addition =new OptionAddition();
			addition.setId(manufacturer.getUuid());
			addition.setText(manufacturer.getName());
			items.add(addition);
		}
	}
	
	public void buildGroup(List<ProductGroup> list){
		items=new ArrayList<OptionAddition>();
		if(list==null||list.isEmpty()){
			return ;
		}
		for(ProductGroup group:list){
			OptionAddition addition =new OptionAddition();
			addition.setId(group.getUuid());
			addition.setText(group.getName());
			items.add(addition);
		}
	}
	
	public void buildSpeak(List<Speaker> list){
		items=new ArrayList<OptionAddition>();
		if(list==null||list.isEmpty()){
			return ;
		}
		for(Speaker speaker:list){
			OptionAddition addition =new OptionAddition();
			addition.setId(speaker.getUuid());
			addition.setText(speaker.getName());
			addition.setHospitalName(OptionalUtils.traceValue(speaker, "hospital.name"));
			items.add(addition);
		}
	}
	
	public void buildResource(List<Resource> list){
		items=new ArrayList<OptionAddition>();
		if(list==null||list.isEmpty()){
			return ;
		}
		for(Resource resource:list){
			OptionAddition addition =new OptionAddition();
			addition.setId(resource.getUuid());
			addition.setText(resource.getName());
			items.add(addition);
		}
	}

	public void buildUser(List<User> list) {
		items=new ArrayList<OptionAddition>();
		if(list==null||list.isEmpty()){
			return ;
		}
		for(User user:list){
			OptionAddition addition =new OptionAddition();
			addition.setId(user.getUuid());
			String text=OptionalUtils.traceValue(user, "nickname");
			String phone=OptionalUtils.traceValue(user, "phone");
			if(!Tools.isBlank(phone)){
				text=text+"-"+phone;
			}
			addition.setText(text);
			items.add(addition);
		}
		
	}
	
	public void buildCastRecordRes(List<Resource> list){
		items=new ArrayList<OptionAddition>();
		if(list==null||list.isEmpty())
			return ;
		for(Resource resource:list){
			OptionAddition addition =new OptionAddition();
			addition.buildBandResData(resource);
			items.add(addition);
		}
	}
	
	public void buildActivityList(List<Activity> list){
		items=new ArrayList<OptionAddition>();
		if(list==null||list.isEmpty())
			return ;
		for(Activity activity:list){
			OptionAddition addition =new OptionAddition();
			addition.setId(activity.getUuid());
			addition.setText(OptionalUtils.traceValue(activity, "name"));
			items.add(addition);
		}
	}
	
	public void buildBroadCastList(List<Broadcast> list){
		items=new ArrayList<OptionAddition>();
		if(list==null||list.isEmpty())
			return ;
		for(Broadcast broadcast:list){
			OptionAddition addition =new OptionAddition();
			addition.setId(broadcast.getUuid());
			addition.setText(OptionalUtils.traceValue(broadcast, "name"));
			items.add(addition);
		}
	}
	
	public void buildGameList(List<Lottery> list){
		items=new ArrayList<OptionAddition>();
		if(list==null||list.isEmpty())
			return ;
		for(Lottery lottery:list){
			OptionAddition addition =new OptionAddition();
			addition.setId(lottery.getUuid());
			addition.setText(OptionalUtils.traceValue(lottery, "name"));
			items.add(addition);
		}
	}

}
