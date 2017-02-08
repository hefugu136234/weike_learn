package com.lankr.tv_cloud.facade.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lankr.orm.mybatis.mapper.ActivityMapper;
import com.lankr.orm.mybatis.mapper.CertificationMapper;
import com.lankr.orm.mybatis.mapper.SpeakerMapper;
import com.lankr.orm.mybatis.mapper.UserExpandMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.ActivityFacade;
import com.lankr.tv_cloud.facade.CertificationFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityConfig;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.ActivityUser;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.UserExpand;
import com.lankr.tv_cloud.tmp.YuanxiaoActive;
import com.lankr.tv_cloud.tmp.YuanxiaoRecord;
import com.lankr.tv_cloud.tmp.YuanxiaoRule;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.sun.org.apache.bcel.internal.generic.INEG;

@SuppressWarnings("all")
public class CertificationFacadeImpl extends FacadeBaseImpl implements
		CertificationFacade {

	@Override
	protected String namespace() {
		return CertificationMapper.class.getName();
	}

	@Override
	public Pagination<Certification> searchCertificationsForDatatable(
			String query, int startPage, int pageSize, String state) {

		query = filterSQLSpecialChars(query);
		String total_sql = "select count(*) from certification where isActive=1 and (name like '%"
				+ query + "%' or pinyin like '%" + query + "%')";
		
		if(NumberUtils.isNumber(state)){
            int status = Integer.valueOf(state);
            if(status >= 0 && status <= 2){
                total_sql = total_sql + "and status = " + status;
            }
        }
		Pagination<Certification> pagination = initPage(total_sql, startPage, pageSize);
		List<Certification> as = certificationMapper.searchCertificationForTable(query, startPage, pageSize, state);
		pagination.setResults(as);
		return pagination;
	}

	@Override
	public Certification getCertificationByUuid(String uuid) {
		return certificationMapper.getCertificationByUuid(uuid);
	}

	@Override
	public Certification updateCertificationStatus(Certification certification) {
		int effect = 0;
		try {
			effect = certificationMapper
					.updateCertificationStatus(certification);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		if (effect > 0) {
			return certificationMapper.getCertificationById(certification
					.getId());
		}
		return null;
	}

	@Override
	public Certification updateCertificationStatusWithOptional(
			Certification certification, UserExpand userExpand, Speaker speaker) {
		Speaker existSpeaker = speakerMapper.getSpeakerByUserId(OptionalUtils
				.traceInt(certification, "user.id"));
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		boolean resultTag;
		try {
			resultTag = transaction.execute(new TransactionCallback<Boolean>() {
				@Override
				public Boolean doInTransaction(TransactionStatus arg0) {
					userExpandMapper.updateUserExpand(userExpand);
					certificationMapper
							.updateCertificationStatus(certification);
					// 如果是医生用户，并且该申请者未关联讲者，以申请者当前信息新建一个讲者
					if (null == existSpeaker
							&& UserExpand.USER_DOCTOR == userExpand.getType()) {
						speaker.setUser(userExpand.getUser());
						speakerMapper.addSpeaker(speaker);
					}
					return true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (!resultTag) {
			return null;
		}
		return certificationMapper.getCertificationById(certification.getId());
	}

	@Override
	public Certification getCertifiActiveByUserId(User user) {
		// TODO Auto-generated method stub
		return certificationMapper.getCertifiActiveByUserId(user.getId());
	}

	@Override
	public boolean isUserCertificated(User user) {
		if (!BaseModel.hasPersisted(user)) {
			return false;
		}
		Certification certification = getCertifiActiveByUserId(user);
		if (certification == null) {
			return false;
		}
		return certification.isCertificated();
	}
	
	@Override
	public String realNameByUser(User user) {
		// TODO Auto-generated method stub
		Certification certification=getCertifiActiveByUserId(user);
		if(certification==null){
			return Tools.nullValueFilter(user.getNickname());
		}else{
			return Tools.nullValueFilter(certification.getName());
		}
	}

}
