package com.lankr.tv_cloud.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.District;
import com.lankr.tv_cloud.model.Province;

public class SurfaceProvinceVo {
	public class RegionVo{
		String uuid;
		String name;
	}
	
	private List<RegionVo> regionVos;
	
	
	
	public List<RegionVo> getRegionVos() {
		return regionVos;
	}

	public void setRegionVos(List<RegionVo> regionVos) {
		this.regionVos = regionVos;
	}

	private String selectUuid;
	
	
	
	public String getSelectUuid() {
		return selectUuid;
	}

	public void setSelectUuid(String selectUuid) {
		this.selectUuid = selectUuid;
	}

	public void buildProvice(List<Province> list){
		if(list==null||list.size()==0)
			return ;
		this.regionVos=new ArrayList<SurfaceProvinceVo.RegionVo>();
		for (Province province : list) {
			RegionVo vo=new RegionVo();
			vo.uuid=province.getUuid();
			vo.name=province.getName();
			this.regionVos.add(vo);
		}
	}
	
	public void buildProvice(List<Province> list,int id){
		if(list==null||list.size()==0)
			return ;
		this.regionVos=new ArrayList<SurfaceProvinceVo.RegionVo>();
		for (Province province : list) {
			RegionVo vo=new RegionVo();
			vo.uuid=province.getUuid();
			vo.name=province.getName();
			if(province.getId()==id){
				this.selectUuid=province.getUuid();
			}
			this.regionVos.add(vo);
		}
	}
	
	public void buildCity(List<City> list){
		if(list==null||list.size()==0)
			return ;
		this.regionVos=new ArrayList<SurfaceProvinceVo.RegionVo>();
		for (City city : list) {
			RegionVo vo=new RegionVo();
			vo.uuid=city.getUuid();
			vo.name=city.getName();
			this.regionVos.add(vo);
		}
	}
	
	public void buildCity(List<City> list,int id){
		if(list==null||list.size()==0)
			return ;
		this.regionVos=new ArrayList<SurfaceProvinceVo.RegionVo>();
		for (City city : list) {
			RegionVo vo=new RegionVo();
			vo.uuid=city.getUuid();
			vo.name=city.getName();
			if(id==city.getId()){
				this.selectUuid=city.getUuid();
			}
				
			regionVos.add(vo);
		}
	}
	
	public void buildDistrict(List<District> list){
		if(list==null||list.size()==0)
			return ;
		this.regionVos=new ArrayList<SurfaceProvinceVo.RegionVo>();
		for (District district : list) {
			RegionVo vo=new RegionVo();
			vo.uuid=district.getUuid();
			vo.name=district.getName();
			this.regionVos.add(vo);
		}
	}
	
	public void buildDistrict(List<District> list,int id){
		if(list==null||list.size()==0)
			return ;
		this.regionVos=new ArrayList<SurfaceProvinceVo.RegionVo>();
		for (District district : list) {
			RegionVo vo=new RegionVo();
			vo.uuid=district.getUuid();
			vo.name=district.getName();
			if(id==district.getId()){
				this.selectUuid=district.getUuid();
			}
			this.regionVos.add(vo);
		}
	}

}
