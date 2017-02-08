package com.lankr.tv_cloud.facade.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lankr.orm.mybatis.mapper.ActivityMapper;
import com.lankr.orm.mybatis.mapper.HospitalMapper;
import com.lankr.orm.mybatis.mapper.ProvinceMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.ActivityFacade;
import com.lankr.tv_cloud.facade.HospitalMgrFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityConfig;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.ActivityUser;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.tmp.YuanxiaoActive;
import com.lankr.tv_cloud.tmp.YuanxiaoRecord;
import com.lankr.tv_cloud.tmp.YuanxiaoRule;
import com.lankr.tv_cloud.utils.Tools;
import com.sun.org.apache.bcel.internal.generic.INEG;

@SuppressWarnings("all")
public class HospitalMgrFacadeImpl extends FacadeBaseImpl implements HospitalMgrFacade {

	@Autowired
	private HospitalMapper hostpitalMapper;
	@Autowired
	private ProvinceMapper provinceMapper;
	
	@Override
	protected String namespace() {
		return HospitalMgrFacadeImpl.class.getName();
	}
	
	@Override
	public Pagination<Hospital> searchActivitiesForDatatable(String qurey,
			int from, int to, String provinceUuid, String cityUuid) {
		qurey = filterSQLSpecialChars(qurey);
		String total_sql = "select count(*) from base_hospital where isActive=1 and (name like '%"
				+ qurey + "%' )";
		
		int provinceId = 0;
		int cityId = 0;
		if(StringUtils.isNotEmpty(provinceUuid) && StringUtils.isEmpty(cityUuid)){
			Province province = provinceMapper.selectProByUUid(provinceUuid);
			if(null != province){
				provinceId = province.getId();
				total_sql += " and provinceId = " + provinceId;
			}
		}
		
		if(StringUtils.isNotEmpty(provinceUuid) && StringUtils.isNotEmpty(cityUuid)){
			Province province = provinceMapper.selectProByUUid(provinceUuid);
			City city = provinceMapper.selectCtiByUUid(cityUuid);
			if(null != province && null != city){
				provinceId = province.getId();
				cityId = city.getId();
				total_sql += " and provinceId = " + provinceId + " and cityId = " + cityId ;
			}
		}
		
		Pagination<Hospital> pagination = initPage(total_sql, from, to);
		List<Hospital> as = hostpitalMapper.searchDatatableHospitals(qurey,
				from, to, provinceId, cityId );
		pagination.setResults(as);
		return pagination;
	}

	@Override
	public Hospital selectHospitalByUUid(String hospitalUuid) {
		if(StringUtils.isEmpty(hospitalUuid))
			return null;
		Hospital hospital = null;
		try {
			hospital = hostpitalMapper.selectHospitalByUUid(hospitalUuid);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据uuid查询医院出错", e);
		}
		return hospital;
	}

	@Override
	public Province selectProByUUid(String provinceUuid) {
		if(StringUtils.isEmpty(provinceUuid))
			return null;
		Province province = null;
		try {
			province = provinceMapper.selectProByUUid(provinceUuid);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据uuid查询省份出错", e);
		}
		return province;
	}

	@Override
	public City selectCtiByUUid(String cityUuid) {
		if(StringUtils.isEmpty(cityUuid))
			return null;
		City city = null;
		try {
			city = provinceMapper.selectCtiByUUid(cityUuid);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据uuid查询城市出错", e);
		}
		return city;
	}

	@Override
	public ActionMessage saveHospital(Hospital hospital) {
		if (null == hospital)
			return ActionMessage.failStatus("保存失败");
		int effect = 0;
		try {
			effect = hostpitalMapper.saveHospital(hospital);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		if (effect > 0) {
			return ActionMessage.successStatus();
		}
		return ActionMessage.failStatus("保存失败");
	}

	@Override
	public ActionMessage updateHospital(Hospital hospital) {
		if (null == hospital)
			return ActionMessage.failStatus("更新失败");
		int effect = 0;
		try {
			effect = hostpitalMapper.updateHospital(hospital);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		if (effect > 0) {
			return ActionMessage.successStatus();
		}
		return ActionMessage.failStatus("更新失败");
	}

	@Override
	public ActionMessage removeHospital(Hospital hospital) {
		if (null == hospital)
			return ActionMessage.failStatus("删除失败");
		int effect = 0;
		try {
			effect = hostpitalMapper.removeHospital(hospital);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		if (effect > 0) {
			return ActionMessage.successStatus();
		}
		return ActionMessage.failStatus("删除失败");
	}

	@Override
	public List<Hospital> selectAllHospital() {
		List<Hospital> hospitals = null ;
		try {
			hospitals = hostpitalMapper.selectAllHospital() ;
		} catch (Exception e) {
			e.printStackTrace() ;
			logger.error("rebuild出错",e) ;
		}
		return hospitals;
	}
}
