package com.lankr.tv_cloud.web.api.webchat.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.orm.mybatis.mapper.CategoryExpandMapper;
import com.lankr.tv_cloud.facade.MediaCentralFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ActivitySubject;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.CategoryExpand;
import com.lankr.tv_cloud.model.CategoryExpandStatus;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.District;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class CommonItemVo extends BaseAPIModel{
	private List<ItemVo> itemList;
	
	

	public List<ItemVo> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemVo> itemList) {
		this.itemList = itemList;
	}

	public void buildProvince(List<Province> list) {
		this.setStatus(Status.SUCCESS);
		itemList = new ArrayList<ItemVo>();
		for (Province province : list) {
			ItemVo app = new ItemVo();
			app.setUuid(province.getUuid());
			app.setName(province.getName());
			itemList.add(app);
		}
	}

	public void buildCity(List<City> list) {
		this.setStatus(Status.SUCCESS);
		itemList = new ArrayList<ItemVo>();
		for (City city : list) {
			ItemVo app = new ItemVo();
			app.setUuid(city.getUuid());
			app.setName(city.getName());
			itemList.add(app);
		}
	}
	
	public void buildDistrict(List<District> list) {
		this.setStatus(Status.SUCCESS);
		itemList = new ArrayList<ItemVo>();
		for (District model : list) {
			ItemVo app = new ItemVo();
			app.setUuid(model.getUuid());
			app.setName(model.getName());
			itemList.add(app);
		}
	}

	public void buildHospital(List<Hospital> list) {
		this.setStatus(Status.SUCCESS);
		itemList = new ArrayList<ItemVo>();
		for (Hospital hospital : list) {
			ItemVo app = new ItemVo();
			app.setUuid(hospital.getUuid());
			app.setName(hospital.getName());
			itemList.add(app);
		}
	}
	
	public void buildManufacturer(List<Manufacturer> list) {
		this.setStatus(Status.SUCCESS);
		itemList = new ArrayList<ItemVo>();
		for (Manufacturer manufacturer : list) {
			ItemVo app = new ItemVo();
			app.setUuid(manufacturer.getUuid());
			app.setName(manufacturer.getName());
			itemList.add(app);
		}
	}

	public void buildDepartments(List<Departments> list) {
		this.setStatus(Status.SUCCESS);
		itemList = new ArrayList<ItemVo>();
		for (Departments departments : list) {
			ItemVo app = new ItemVo();
			app.setUuid(departments.getUuid());
			app.setName(departments.getName());
			itemList.add(app);
		}
	}

	public void buildCate(List<Category> list,
			CategoryExpandMapper categoryExpandMapper, Integer status,
			String[] filter) {
		itemList = new ArrayList<ItemVo>();
		for (Category category : list) {
			// 设置过滤开关
			boolean flag = false;
			if (filter != null && filter.length > 0) {
				for (int i = 0; i < filter.length; i++) {
					if (filter[i].equals(category.getName())) {
						flag = true;
						break;
					}
				}
			}
			if (flag) {
				continue;
			}
			ItemVo app = new ItemVo();
			app.setUuid(category.getUuid());
			app.setName(category.getName());
			CategoryExpand categoryExpand = categoryExpandMapper
					.selectExpandByCateId(category.getId());
			if (categoryExpand != null && categoryExpand.getUuid() != null) {
				if (status == CategoryExpandStatus.WX_PLATFORM) {
					app.setTaskId(categoryExpand.getWxTaskId());
				} else if (status == CategoryExpandStatus.APP_PLATFORM) {
					app.setTaskId(categoryExpand.getAppTaskId());
				} else if (status == CategoryExpandStatus.TV_PLATFORM) {
					app.setTaskId(categoryExpand.getTvTaskId());
				} else if (status == CategoryExpandStatus.WEB_PLATFORM) {
					app.setTaskId(categoryExpand.getWebTaskId());
				}
			} else {
				app.setTaskId("");
			}
			itemList.add(app);
		}
	}
	
	public void buildCate(List<Category> list,
			CategoryExpandMapper categoryExpandMapper) {
		this.setStatus(Status.SUCCESS);
		if(list==null||list.isEmpty()){
			return ;
		}
		itemList = new ArrayList<ItemVo>();
		for (Category category : list) {
			ItemVo app = new ItemVo();
			app.setUuid(category.getUuid());
			app.setName(category.getName());
			CategoryExpand categoryExpand = categoryExpandMapper
					.selectExpandByCateId(category.getId());
			if (categoryExpand != null && categoryExpand.getUuid() != null) {
				app.setTaskId(categoryExpand.getWxTaskId());
			} else {
				app.setTaskId("");
			}
			itemList.add(app);
		}
	}
	
	public void buildCate(List<Category> list) {
		this.setStatus(Status.SUCCESS);
		if(list==null||list.isEmpty()){
			return ;
		}
		itemList = new ArrayList<ItemVo>();
		for (Category category : list) {
			ItemVo app = new ItemVo();
			app.setUuid(category.getUuid());
			app.setName(category.getName());
			itemList.add(app);
		}
	}
	
	public List<ItemVo> buildActivitySubject(List<ActivitySubject> list){
		this.itemList=new ArrayList<ItemVo>();
		if(list==null||list.isEmpty()){
			return this.itemList;
		}
		for (ActivitySubject activitySubject : list) {
			ItemVo app = new ItemVo();
			app.setUuid(OptionalUtils.traceValue(activitySubject, "category.uuid"));
			String name=activitySubject.getName();
			if(name!=null&&!name.isEmpty()){
				app.setName(name);
			}else{
				app.setName(OptionalUtils.traceValue(activitySubject, "category.name"));
			}
			itemList.add(app);
		}
		return itemList;
	}

	public void buildCate(List<Category> list,
			MediaCentralFacade mediaCentralFacade, CategoryExpandMapper categoryExpandMapper) {
		this.setStatus(Status.SUCCESS);
		if(list==null||list.isEmpty()){
			return ;
		}
		itemList = new ArrayList<ItemVo>();
		for (Category category : list) {
			ItemVo app = new ItemVo();
			app.setUuid(category.getUuid());
			app.setName(category.getName());
			MediaCentral mediaCentral = mediaCentralFacade.getCategoryMedia(category, MediaCentral.SIGN_WX_COVER);
			if (mediaCentral != null && mediaCentral.getUuid() != null) {
				app.setTaskId(mediaCentral.getUrl());
			} else {
				CategoryExpand categoryExpand = categoryExpandMapper
						.selectExpandByCateId(category.getId());
				if (categoryExpand != null && categoryExpand.getUuid() != null) {
					app.setTaskId(categoryExpand.getWxTaskId());
				} else {
					app.setTaskId("");
				}
			}
			itemList.add(app);
		}
	}

}
