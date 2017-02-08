/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月19日
 * 	@modifyDate 2016年5月19日
 *  
 */
package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.orm.mybatis.mapper.MediaCentralMapper;
import com.lankr.tv_cloud.codes.Code;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.MediaCentralFacade;
import com.lankr.tv_cloud.model.ActivityExpert;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.Questionnaire;
import com.lankr.tv_cloud.utils.Tools;

public class MediaCentralFacadeImpl extends FacadeBaseImpl implements
		MediaCentralFacade {

	private MediaCentralMapper mediaCentralMapper;

	@Autowired
	public void setMediaCentralMapper(MediaCentralMapper mediaCentralMapper) {
		this.mediaCentralMapper = mediaCentralMapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.MediaCentralFacade#addCategoryMedia(com.lankr
	 * .tv_cloud.model.Category, int, java.lang.String)
	 */
	@Override
	public ActionMessage saveCategoryMedia(Category category, int sign,
			String url, String cssText) {
		MediaCentral mc = MediaCentral.initCategory(category, sign);
		mc.setUrl(url);
		mc.setText(cssText);
		return saveMediaCentral(mc).getActionMessage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.impl.FacadeBaseImpl#namespace()
	 */
	@Override
	protected String namespace() {
		return MediaCentralMapper.class.getName();
	}

	private Code saveMediaCentral(MediaCentral mc) {
		try {
			MediaCentral c = mediaCentralMapper.searchMediaCentral(
					mc.getReferType(), mc.getReferId(), mc.getSign());
			if (c == null) {
				addMediaCenter(mc);
			} else {
				c.setUrl(mc.getUrl());
				updateMediaCenter(c);
			}
			return codeProvider.codeOk();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codeProvider.code(-1100);
	}

	private int addMediaCenter(MediaCentral mc) throws Exception {
		mc.setUuid(Tools.getUUID());
		mc.setIsActive(MediaCentral.ACTIVE);
		mc.setStatus(MediaCentral.APPROVED);
		return mediaCentralMapper.add(mc);
	}

	private int updateMediaCenter(MediaCentral mc) throws Exception {
		return mediaCentralMapper.update(mc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.MediaCentralFacade#getCategoryMedia(com.lankr
	 * .tv_cloud.model.Category, int)
	 */
	@Override
	public MediaCentral getCategoryMedia(Category category, int sign) {
		if (!Category.hasPersisted(category))
			return null;
		return mediaCentralMapper.searchMediaCentral(
				MediaCentral.REFER_TYPE_CATEGORY, category.getId(), sign);
	}

	@Override
	public List<MediaCentral> getCategoryMedias(Category category) {
		if (!Category.hasPersisted(category))
			return null;
		return mediaCentralMapper.searchMediaCentrals(
				MediaCentral.REFER_TYPE_CATEGORY, category.getId());
	}

	@Override
	public ActionMessage saveActivityExpertMedia(ActivityExpert expert,
			int sign, String url) {
		MediaCentral mc = MediaCentral.initActivityExpert(expert, sign);
		mc.setUrl(url);
		return saveMediaCentral(mc).getActionMessage();
	}

	@Override
	public MediaCentral getActivityExpertMedia(ActivityExpert expert, int sign) {
		if (!ActivityExpert.hasPersisted(expert))
			return null;
		return mediaCentralMapper.searchMediaCentral(
				MediaCentral.REFER_TYPE_ACTIVITYEXPERT, expert.getId(), sign);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.MediaCentralFacade#saveNormalCollectMedia(com
	 * .lankr.tv_cloud.model.NormalCollect, int, java.lang.String)
	 */
	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年7月27日
	 * @modifyDate 2016年7月27日
	 * 
	 */
	@Override
	public ActionMessage saveNormalCollectMedia(NormalCollect collect,
			int sign, String url) {
		MediaCentral mc = MediaCentral.initNormalCollect(collect, sign);
		mc.setUrl(url);
		return saveMediaCentral(mc).getActionMessage();
	}

	@Override
	public MediaCentral getNormalCollectMedia(NormalCollect collect, int sign) {
		if (!NormalCollect.hasPersisted(collect))
			return null;
		return mediaCentralMapper.searchMediaCentral(
				MediaCentral.REFER_TYPE_NORMALCOLLECT, collect.getId(), sign);
	}

	@Override
	public MediaCentral getQuestionnaireMedia(Questionnaire questionnaire, int sign) {
		if (!Questionnaire.hasPersisted(questionnaire))
			return null;
		return mediaCentralMapper.searchMediaCentral(
				MediaCentral.REFER_TYPE_QUESTIONNAIRE, questionnaire.getId(), sign);
	}
	
	@Override
	public ActionMessage saveQuestionnaireMedia(Questionnaire questionnaire, 
			int sign, String url) {
			MediaCentral mc = MediaCentral.initQuestionnaire(questionnaire, sign);
			mc.setUrl(url);
			return saveMediaCentral(mc).getActionMessage();
	}
}
