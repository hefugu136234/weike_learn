/** 
 *  @author Kalean.Xiang
 *  @createDate 2016年5月19日
 * 	@modifyDate 2016年5月19日
 *  
 */
package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.model.ActivityExpert;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.MediaCentral;
import com.lankr.tv_cloud.model.NormalCollect;
import com.lankr.tv_cloud.model.Questionnaire;

/**
 * @author Kalean.Xiang
 *
 */
public interface MediaCentralFacade {

	// public ActionMessage addMediaCentral(MediaCentral media);

	/**
	 * @param sign
	 *            see MediaCentral.SIGN_* add and update supported
	 * */
	public ActionMessage saveCategoryMedia(Category category, int sign,
			String url, String cssText);

	public MediaCentral getCategoryMedia(Category category, int sign);

	public List<MediaCentral> getCategoryMedias(Category category);

	public ActionMessage saveActivityExpertMedia(ActivityExpert expert,
			int sign, String url);

	public MediaCentral getActivityExpertMedia(ActivityExpert expert, int sign);

	public ActionMessage saveNormalCollectMedia(NormalCollect collect,
			int sign, String url);
	
	public MediaCentral getNormalCollectMedia(NormalCollect collect, int sign);

	public MediaCentral getQuestionnaireMedia(Questionnaire questionnaire, int sign);

	public ActionMessage saveQuestionnaireMedia(Questionnaire questionnaire, int sign, String url);
}
