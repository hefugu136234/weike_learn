package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.ActivitySubject;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.web.api.webchat.vo.OpusVo;

public interface ActivityOpusFacade {

	// 添加活动作品申请
	public OpusVo addActivityapplication(ActivityApplication activityApplication);

	@Deprecated
	public boolean oupsResCount(Resource resource);

	public ActivityApplication relatedResOups(Resource resource);

	public ActivityApplication getApplicateByCode(String code);

	public ActivityApplication getApplicateByUuid(String uuid);

	public Status updateRelateOups(ActivityApplication activityApplication);

	public Pagination<ActivityApplication> selectOupsList(String searchValue,
			int from, int pageItemTotal);

	public Status removeBinding(ActivityApplication activityApplication);

	public List<ActivityApplication> getOupsByActivityId(Activity activity);

	public List<ActivityResource> oupsRankingByActivityId(Activity activity);

	public List<ActivityApplication> searchApplicateByUserId(int type,int userId,String startTime,int size);

	public Certification getCertificationByUserId(User user);

	public Status addCertification(Certification certification);
	
	public int changeActivityApplicationStatus(ActivityApplication application,
			int status);
	
	public List<ActivitySubject> searchActivitySubjectForWx(Activity activity);
	
}
