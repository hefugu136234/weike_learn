package com.lankr.tv_cloud.web.api.app.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class CityVo extends BaseAPIModel{
	private List<RegionVoApp> cityList;
	
	public void buildCity(List<City> list){
		if(list==null||list.size()==0)
			return ;
		cityList=new ArrayList<RegionVoApp>();
		for (City city : list) {
			RegionVoApp app=new RegionVoApp();
			app.setUuid(city.getUuid());
			app.setName(city.getName());
			cityList.add(app);
		}
	}

}
