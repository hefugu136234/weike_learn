package com.lankr.tv_cloud.facade;

import java.util.Date;
import java.util.List;

import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.TvAuthentication;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.vo.ActivityTotalApiData;

public interface APIFacade {

	public List<Video> searchAllOnlineVideosByCategoryId(Category category,
			Long updated_at);

	public Category searchCategoryByPorjectId(int projectId, int categoryId);

	public List<Video> getVideoByCategoryId(int projectid, int categoryId);

	public TvAuthentication createTvAuth(User user, String device,
			String remoteIp, String oldToken);

	public TvAuthentication getTvAuthByToken(String token);

	public ActionMessage logoutTvAuth(TvAuthentication auth);

	// 获取活动推荐的资源
	public List<Resource> fetchActivityRecommendResources(Activity activity);

	// 获取活动资源
	public List<Resource> fetchActivityResources(Activity activity,
			Date updated_at, int batch_size);

	// 获取活动报道
	public List<Resource> fetchActivityReports(Activity activity,
			Date updated_at, int batch_size);

	// 获取活动排名
	public List<Resource> fetchActivityRanking(Activity activity);

	// total
	public ActivityTotalApiData activityCompletedJson(Activity activity);

	public List<Resource> search(String q);
}