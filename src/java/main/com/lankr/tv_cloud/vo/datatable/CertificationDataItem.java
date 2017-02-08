package com.lankr.tv_cloud.vo.datatable;

import org.springframework.web.util.HtmlUtils;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

@SuppressWarnings("all")
public class CertificationDataItem{

	private String imageUrl;
	private String realName;
	private String userName;
	private String nickName;
	private String claimantDate;
	private String checkDate;
	private String descrption;
	private int status;
	private String uuid;
	private int userId;
	private String userUuid;
	
	private String cityName;

	public static CertificationDataItem build(Certification certification) {
		if (certification == null)
			return null;
		
		CertificationDataItem item = new CertificationDataItem();
		item.imageUrl = OptionalUtils.traceValue(certification, "imageUrl");
		item.realName = OptionalUtils.traceValue(certification, "name");
		item.userName = OptionalUtils.traceValue(certification, "user.username");
		item.nickName = OptionalUtils.traceValue(certification, "user.nickname");
		item.claimantDate = Tools.formatYMDHMSDate(certification.getCreateDate());
		item.checkDate = Tools.formatYMDHMSDate(certification.getModifyDate());
		item.descrption = HtmlUtils.htmlEscape(certification.getMark());
		item.status = OptionalUtils.traceInt(certification, "status");
		item.uuid = OptionalUtils.traceValue(certification, "uuid");
		item.userId = OptionalUtils.traceInt(certification, "user.id");
		item.userUuid = OptionalUtils.traceValue(certification, "user.uuid");
		
		item.cityName = OptionalUtils.traceValue(certification, "user.userExpand.city.name");
		return item;
	}
}
