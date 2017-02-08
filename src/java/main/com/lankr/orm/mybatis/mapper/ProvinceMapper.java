package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.District;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Province;

public interface ProvinceMapper {
	/**
	 * 批量添加省
	 * @param list
	 */
	public void batchAddProvince(List<Province> list);
	/**
	 * 用名字查省
	 */
	public Province selectProByName(String name);
	
	/**
	 * 用name和省的id查市
	 */
	public City selectCtiByNameAndId(SubParams subParams);
	/**
	 * 用名字查找市
	 * @param subParams
	 * @return
	 */
	public City selectCtiByName(String name);
	
	/**
	 * 批量添加市
	 * @param list
	 */
	public void batchAddCity(List<City> list);
	/**
	 * 批量添加区
	 * @param list
	 */
	public void batchAddDistrict(List<District> list);
	
	public Province selectProByUUid(String uuid);
	
	public City selectCtiByUUid(String uuid);
	
	public District selectDisByUUid(String uuid);
	
	public List<Province> selectProList();
	
	public List<City> selectCtiListById(int id);
	
	public List<District> selectDisListById(int id);

}
