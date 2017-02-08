package com.lankr.tv_cloud.facade.impl;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lankr.orm.mybatis.mapper.ActivityApplicationMapper;
import com.lankr.orm.mybatis.mapper.ActivityMapper;
import com.lankr.orm.mybatis.mapper.CertificationMapper;
import com.lankr.tv_cloud.facade.ActivityOpusFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityApplication;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.ActivitySubject;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.PdfInfo;
import com.lankr.tv_cloud.model.QrCode;
import com.lankr.tv_cloud.model.QrScene;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.web.api.webchat.util.WebChatMenu;
import com.lankr.tv_cloud.web.api.webchat.vo.OpusVo;

public class ActivityOpusFacadeImp extends FacadeBaseImpl implements
		ActivityOpusFacade {

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return ActivityApplicationMapper.class.getName();
	}

	@Override
	public synchronized OpusVo addActivityapplication(
			ActivityApplication activityApplication) {
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		boolean mark = false;
		for (int i = 0; i <= 10; i++) {
			String code = maxOpusCode();
			activityApplication.setCode(code);
			mark = transaction.execute(new TransactionCallback<Boolean>() {

				@Override
				public Boolean doInTransaction(TransactionStatus arg0) {
					boolean flag = false;
					try {
						activityApplicationMapper
								.addActivityapplication(activityApplication);
						flag = true;
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("添加作品申请失败", e);
						flag = false;
					}
					return flag;
				}

			});
			if (mark) {
				break;
			}
		}
		OpusVo vo = new OpusVo();
		if (mark) {
			vo.setStatus(Status.SUCCESS);
			vo.buildData(activityApplication);
		} else {
			vo.setStatus(Status.FAILURE);
		}
		return vo;
	}

	// 查询表中最大的编号，再格式化数据，再加1
	public String maxOpusCode() {
		String val = "0001";
		String code = activityApplicationMapper.maxOpusCode();
		if (code == null) {
			return val;
		}
		int num = Integer.parseInt(code) + 1;
		DecimalFormat df = new DecimalFormat("0000");
		val = df.format(num);
		return val;
	}

	@Override
	public boolean oupsResCount(Resource resource) {
		// TODO Auto-generated method stub
		Integer count = activityApplicationMapper
				.oupsResCount(resource.getId());
		if (count == null) {
			return false;
		}
		if (count > 0) {
			return true;

		}
		return false;
	}

	@Override
	public ActivityApplication relatedResOups(Resource resource) {
		// TODO Auto-generated method stub
		return activityApplicationMapper.relatedResOups(resource.getId());
	}

	@Override
	public ActivityApplication getApplicateByCode(String code) {
		// TODO Auto-generated method stub
		return activityApplicationMapper.getApplicateByCode(code);
	}

	@Override
	public ActivityApplication getApplicateByUuid(String uuid) {
		// TODO Auto-generated method stub
		return activityApplicationMapper.getApplicateByUuid(uuid);
	}

	@Override
	public Status updateRelateOups(ActivityApplication activityApplication) {
		// TODO Auto-generated method stub
		try {
			// 设置匹配时间
			activityApplication.setMatchDate(Tools.getCurrentDate());
			//设置默认的状态
			Resource res = activityApplication.getResource();
			if(res.getStatus() == Resource.UNAPPROVED){
				activityApplication.setStatus(ActivityApplication.STATUS_ENCODE);
			}else if(res.getStatus() == Resource.APPROVED){
				activityApplication.setStatus(ActivityApplication.STATUS_SUCCESS);
			}else if(res.getStatus() == Resource.UNAPPROVED){
				activityApplication.setStatus(ActivityApplication.STATUS_PROFESS);
			}
			int effect = activityApplicationMapper
					.updateRelateOups(activityApplication);
			
			
			if (effect > 0) {
				// 记录code
				recordResouceCode(activityApplication.getResource(),
						activityApplication.getCode());
				// 添加讲者
				try {
					User user = activityApplication.getUser();
					Speaker speaker = speakerMapper.getSpeakerByUserId(user
							.getId());
					if (speaker != null) {
						recordResourceSpeaker(
								activityApplication.getResource(), speaker);
					}
				} catch (Exception e) {
					logger.error("add speaker to user error ", e);
					e.printStackTrace();
				}
			}
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("后台作品编号绑定资源出错", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Pagination<ActivityApplication> selectOupsList(String searchValue,
			int from, int pageItemTotal) {
		// TODO Auto-generated method stub
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from activity_resource_application where isActive=1 and (code like '%"
				+ searchValue + "%' or name like '%" + searchValue + "%')";
		Pagination<ActivityApplication> pagination = initPage(sql, from,
				pageItemTotal);
		SubParams params = new SubParams();
		params.setQuery(searchValue);
		params.setStart(from);
		params.setSize(pageItemTotal);
		List<ActivityApplication> list = activityApplicationMapper
				.selectOupsPage(params);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public Status removeBinding(ActivityApplication activityApplication) {
		// TODO Auto-generated method stub
		try {
			Resource res = activityApplication.getResource();
//			activityApplication.setStatus(BaseModel.UNAPPROVED);
			activityApplication.setResource(null);
			activityApplication.setMatchDate(null);
			activityApplication.setStatus(ActivityApplication.STATUS_PRODUCTED);
			int effect = activityApplicationMapper
					.updateRelateOups(activityApplication);
			if (effect > 0) {
				removeResouceCode(res);
			}
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("解除作品绑定失败", e);
		}
		return Status.FAILURE;
	}

	@Override
	public List<ActivityApplication> getOupsByActivityId(Activity activity) {
		// TODO Auto-generated method stub
		return activityApplicationMapper.getOupsByActivityId(activity.getId());
	}

	@Override
	public List<ActivityResource> oupsRankingByActivityId(Activity activity) {
		// TODO Auto-generated method stub
		return activityMapper.oupsRankingByActivityResId(activity.getId());
	}

	@Override
	public List<ActivityApplication> searchApplicateByUserId(int type,int userId,String startTime,int size) {
		// TODO Auto-generated method stub
		return activityApplicationMapper.searchApplicateByUserId(type,userId,startTime,size);
	}

	@Override
	public Status addCertification(Certification certification) {
		try {
			int effect = certificationMapper.addCertification(certification);
			if (effect == 1) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public Certification getCertificationByUserId(User user) {
		// TODO Auto-generated method stub
		return certificationMapper.getCertificationByUserId(user.getId());
	}
	
	@Override
	public List<ActivitySubject> searchActivitySubjectForWx(Activity activity) {
		// TODO Auto-generated method stub
		return activityMapper.searchActivitySubjectForWx(activity.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.ActivityOpusFacade#changeActivityApplicationStatus
	 * (com.lankr.tv_cloud.model.ActivityApplication, int)
	 */
	@Override
	public int changeActivityApplicationStatus(ActivityApplication applicaiton,
			int status) {
		applicaiton.setStatus(status);
		int effect = activityApplicationMapper.updateRelateOups(applicaiton);
		if (effect > 0) {
			return status;
		}
		return -1;
	}
}
