package com.lankr.tv_cloud.web.api.app.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;
import com.lankr.tv_cloud.web.api.webchat.vo.ItemVo;

public class CommonAppItemVo extends BaseAPIModel{
	private List<ItemVo> itemList;
	
	public void buildProvince(List<Province> list){
		itemList=new ArrayList<ItemVo>();
		for (Province province : list) {
			ItemVo app=new ItemVo();
			app.setUuid(province.getUuid());
			app.setName(province.getName());
			itemList.add(app);
		}
	}
	
	public void buildCity(List<City> list){
		itemList=new ArrayList<ItemVo>();
		for (City city : list) {
			ItemVo app=new ItemVo();
			app.setUuid(city.getUuid());
			app.setName(city.getName());
			itemList.add(app);
		}
	}
	
	public void buildHospital(List<Hospital> list){
		itemList=new ArrayList<ItemVo>();
		for (Hospital hospital : list) {
			ItemVo app=new ItemVo();
			app.setUuid(hospital.getUuid());
			app.setName(hospital.getName());
			itemList.add(app);
		}
	}
	
	public void buildDepartments(List<Departments> list){
		itemList=new ArrayList<ItemVo>();
		for (Departments departments : list) {
			ItemVo app=new ItemVo();
			app.setUuid(departments.getUuid());
			app.setName(departments.getName());
			itemList.add(app);
		}
	}
	
	public void buildCate(List<Category> list){
		itemList=new ArrayList<ItemVo>();
		for (Category category : list) {
			ItemVo app=new ItemVo();
			app.setUuid(category.getUuid());
			app.setName(category.getName());
			itemList.add(app);
		}
	}

	public void buildManufacturers(List<Manufacturer> list) {
		itemList=new ArrayList<ItemVo>();
		for (Manufacturer manufacturer : list) {
			ItemVo app=new ItemVo();
			app.setUuid(manufacturer.getUuid());
			app.setName(manufacturer.getName());
			itemList.add(app);
		}
		
	}
	

}
