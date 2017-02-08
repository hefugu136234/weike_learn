package com.lankr.tv_cloud.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.lankr.orm.mybatis.mapper.TvAuthenticationMapper;
import com.lankr.tv_cloud.cache.CacheBucket;
import com.lankr.tv_cloud.cache.lru.LruCache;
import com.lankr.tv_cloud.facade.APIFacade;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.TagFacade;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.model.TvAuthentication;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.utils.Tools;
import com.lankr.tv_cloud.vo.ActivityTotalApiData;

public class APIFacadeImpl extends FacadeBaseImpl implements APIFacade {

	private static Date seed_date = new Date(0);

	private TvAuthenticationMapper tvAuthMapper;

	@Autowired
	private TagFacade tagFacade;

	@Autowired
	public void setTvAuthMapper(TvAuthenticationMapper tvAuthMapper) {
		this.tvAuthMapper = tvAuthMapper;
	}

	@Override
	public List<Video> searchAllOnlineVideosByCategoryId(Category category,
			Long updated_at) {
		Date d = seed_date;
		if (updated_at != null) {
			d = new Date(updated_at);
		}
		if (category == null)
			return null;
		SubParams sp = new SubParams();
		sp.id = category.getId();
		sp.updated_at = d;
		List<Video> videos = videoDao.searchAll(
				getSqlAlias("asset", "searchApiVideos"), sp);
		return videos;
	}

	@Override
	protected String namespace() {
		return "";
	}

	@Override
	public Category searchCategoryByPorjectId(int projectId, int categoryId) {
		Category cat = new Category();
		cat.setId(categoryId);
		Project project = new Project();
		project.setId(projectId);
		cat.setProject(project);
		return categoryDao.search(cat,
				getSqlAlias("asset", "fetchRootCategoryById"));
	}

	@Override
	public List<Video> getVideoByCategoryId(int projectid, int categoryId) {
		SubParams sub = new SubParams();
		sub.id = categoryId;
		sub.projectId = projectid;
		return videoDao
				.searchAll(getSqlAlias("asset", "getVideoBycateId"), sub);
	}

	@Override
	public TvAuthentication createTvAuth(User user, String device,
			String remoteIp, String oldToken) {
		try {
			TvAuthentication ta = TvAuthentication.makePrototype(
					TvAuthentication.class, remoteIp);
			ta.setUser(user);
			ta.setDevice(device);
			tvAuthMapper.addTvAuth(ta);
			// 禁用之前的auth
			tvAuthMapper.disableUserAuths(ta);
			try {
				if (!Tools.isBlank(oldToken)) {
					evictCache(CacheBucket.TVAUTHCACHE, oldToken);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return ta;
		} catch (Exception e) {
			logger.error("create auth with an exception e " + e);
			return null;
		}
	}

	@Cacheable(value = CacheBucket.TVAUTHCACHE, key = "#token")
	@Override
	public TvAuthentication getTvAuthByToken(String token) {
		try {
			TvAuthentication auth = tvAuthMapper.getTvAuthByToken(token);
			logger.info(token + " search for " + auth);
			return auth;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Resource> fetchActivityRecommendResources(Activity activity) {
		if (BaseModel.hasPersisted(activity)) {
			return resourceMapper.searchActivityRecommend(activity.getId());
		}
		return null;
	}

	@Override
	public List<Resource> fetchActivityResources(Activity activity,
			Date updated_at, int batch_size) {
		if (BaseModel.hasPersisted(activity)) {
			List<Resource> resources = resourceMapper.searchActivityResouces(
					activity.getId(), updated_at, Math.min(batch_size, 30));
			return resources;
		}
		return null;
	}

	@Override
	public List<Resource> fetchActivityReports(Activity activity,
			Date updated_at, int batch_size) {
		if (BaseModel.hasPersisted(activity)) {
			return resourceMapper.searchActivityReports(activity.getName(),
					updated_at, Math.min(batch_size, 30));
		}
		return null;
	}

	@Override
	public List<Resource> fetchActivityRanking(Activity activity) {
		if (BaseModel.hasPersisted(activity)) {
			return resourceMapper.searchActivityRankResources(activity.getId());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.APIFacade#activityCompletedJson(com.lankr.tv_cloud
	 * .model.Activity)
	 */
	@Override
	public ActivityTotalApiData activityCompletedJson(Activity activity) {
		ActivityTotalApiData data = new ActivityTotalApiData().buildFirst(this,
				activity);
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.APIFacade#search(java.lang.String)
	 */
	@Override
	public List<Resource> search(String q) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.APIFacade#logoutTvAuth(com.lankr.tv_cloud.model
	 * .TvAuthentication)
	 */
	/**
	 * @author Kalean.Xiang
	 * @createDate 2016年5月5日
	 * @modifyDate 2016年5月5日
	 * 
	 */
	@Override
	public ActionMessage logoutTvAuth(TvAuthentication auth) {
		if (auth == null)
			return ActionMessage.failStatus("empty user tv authentication");
		try {
			String token = auth.getToken();
			// 清除缓存
			evictCache(CacheBucket.TVAUTHCACHE, token);
			tvAuthMapper.disableUserAuths(auth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.successStatus();
	}

}
