package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.Departments;
import com.lankr.tv_cloud.model.Hospital;

public interface HospitalMapper {
	
	public Hospital selectHospitalById(int id);
	
	public Hospital selectHospitalByUUid(String uuid);
	
	public List<Hospital> selectHosListByCityId(int cityId);
	
	public List<Departments> selectDePatList();
	
	public Departments selectDePatByUuid(String uuid);
	
	public void batchAddHospital(List<Hospital> list);
	
	public void batchAddDepartments(List<Departments> list);
	
	//@Param映射数据库字段,后者行参自定
	List<Hospital> searchDatatableHospitals(String qurey, int from, int to, @Param("provinceId")int provinceId, @Param("cityId")int cityId);

	public int saveHospital(Hospital hospital);

	public int updateHospital(Hospital hospital);

	public int removeHospital(Hospital hospital);

	public List<Hospital> selectAllHospital();
}
