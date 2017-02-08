package com.lankr.tv_cloud.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lankr.orm.mybatis.mapper.ProvinceMapper;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.District;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.utils.Tools;

import net.minidev.json.JSONArray;

public class EphemeralData {

	public ProvinceMapper provinceMapper;

	private static Object _lock = new Object();

	private static EphemeralData object = null;

	public List<Province> provinces;
	
	private String location ;

	private EphemeralData(ProvinceMapper isprovinceMapper) {
		if (provinceMapper == null) {
			provinceMapper = isprovinceMapper;
		}
	}

	// key=provinceuuid
	public Map<String, List<City>> cityMap;
	// key city uuid
	public Map<String, List<District>> districtMap;

	public List<Province> getProvinces() {
		synchronized (_lock) {
			if (provinces == null || provinces.isEmpty()) {
				provinces = provinceMapper.selectProList();
				buildPinYin();
//				Collections.sort(provinces, new Comparator<Province>() {
//
//					@Override
//					public int compare(Province args1, Province args2) {
//						// TODO Auto-generated method stub
//						String pinyin1 = args1.getPinyin();
//						String pinyin2 = args2.getPinyin();
//						if (pinyin1.compareTo(pinyin2) < 0) {
//							return -1;
//						} else if (pinyin1.compareTo(pinyin2) == 0) {
//							return 0;
//						} else {
//							return 1;
//						}
//					}
//				});
			}
		}
		return provinces;
	}

	public void buildPinYin() {
		String name;
		for (Province province : provinces) {
			name = province.getName();
			province.setPinyin(Tools.getPinYin(name));
		}
	}

	public List<City> getCitys(Province province) {
		if (province == null) {
			return null;
		}
		if (cityMap == null) {
			cityMap = new HashMap<String, List<City>>();
		}
		List<City> list = cityMap.get(province.getUuid());
		if (list == null) {
			list = provinceMapper.selectCtiListById(province.getId());
			cityMap.put(province.getUuid(), list);
		}		
		return list;
	}

	public List<District> getDistricts(City city) {
		if (city == null) {
			return null;
		}
		if (districtMap == null) {
			districtMap = new HashMap<String, List<District>>();
		}
		List<District> list = districtMap.get(city.getUuid());
		if (list == null) {
			list = provinceMapper.selectDisListById(city.getId());
		}
		districtMap.put(city.getUuid(), list);
		return list;
	}

	public static EphemeralData getIntansce(ProvinceMapper isprovinceMapper) {
		if (object == null) {
			object = new EphemeralData(isprovinceMapper);
		}
		object.provinceMapper = isprovinceMapper;
		return object;
	}
	
	public static EphemeralData getIntansce() throws Exception{
		if(object == null || object.provinceMapper == null){
			throw new Exception("can not be able to access the database");
		}
		return object;
	}
	
	public String getLocation() {
		if (object.location == null) {
			object.location = getLocationJSONAPI() ;
		} 
		return object.location ;
	}
	
	@SuppressWarnings("all")
	private String getLocationJSONAPI() {
		List locationData = new ArrayList() ;
		Map provinces = null;
		List<Province> list = this.getProvinces();
		for(Province province : list){
			provinces = new HashMap() ;
			provinces.put("uuid", province.getUuid()) ;
			provinces.put("name", province.getName()) ;
			
			List citys = new ArrayList() ;
			List<City> cityList = this.getCitys(province) ;
			for(City city : cityList){
				Map cityTemp = new HashMap() ;
				cityTemp.put("uuid", city.getUuid()) ;
				cityTemp.put("name", city.getName()) ;
				List districts = new ArrayList() ; 
				List<District> districtList = this.getDistricts(city) ;
				for(District district : districtList){
					Map districtTemp = new HashMap() ;
					districtTemp.put("uuid", district.getUuid()) ;
					districtTemp.put("name", district.getName()) ;
					districts.add(districtTemp) ;
				}
				cityTemp.put("districts", districts) ;
				citys.add(cityTemp) ;
			}
			provinces.put("citys", citys) ;
			locationData.add(provinces) ;
		}
		return JSONArray.toJSONString(locationData) ;
	}
}
