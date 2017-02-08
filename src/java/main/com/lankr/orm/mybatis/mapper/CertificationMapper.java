package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.Certification;

public interface CertificationMapper {
	
	public int addCertification(Certification certification);
	
	public List<Certification> searchCertificationForTable(String query, int from, int size, @Param("state") String state);
	
	public int updateCertificationStatus(Certification certification);
	
	public Certification getCertificationByUuid(String uuid);
	
	public Certification getCertificationByUserId(int userId);

	public Certification getCertificationById(int id);
	
	//按userid查出实名通过的人认证信息
	public Certification getCertifiActiveByUserId(int userId);

}
