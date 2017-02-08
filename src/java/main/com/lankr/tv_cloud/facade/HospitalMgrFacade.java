package com.lankr.tv_cloud.facade;


import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.City;
import com.lankr.tv_cloud.model.Hospital;
import com.lankr.tv_cloud.model.Province;

public interface HospitalMgrFacade {

	public Pagination<Hospital> searchActivitiesForDatatable(String qurey,
			int from, int to, String provinceUuid, String cityUuid);

	public Hospital selectHospitalByUUid(String hospitalUuid);

	public Province selectProByUUid(String province);

	public City selectCtiByUUid(String city);

	public ActionMessage saveHospital(Hospital hospital);

	public ActionMessage updateHospital(Hospital hospital);

	public ActionMessage removeHospital(Hospital hospital);

	public List<Hospital> selectAllHospital();

}
