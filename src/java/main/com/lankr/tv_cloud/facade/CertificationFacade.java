package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.model.User;

public interface CertificationFacade {

	public Pagination<Certification> searchCertificationsForDatatable(String query,
			int startPage, int pageSize, String state);

	public Certification getCertificationByUuid(String uuid);

	public Certification updateCertificationStatus(Certification certification);
	
	public Certification getCertifiActiveByUserId(User user);

	public Certification updateCertificationStatusWithOptional(
			Certification certification, UserExpand userExpand, Speaker speaker);

	public boolean isUserCertificated(User user);
	
	public String realNameByUser(User user);
}
