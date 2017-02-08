package com.lankr.orm.mybatis.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceVoteOption;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.model.TagsResource;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.model.WxSubject;

public interface ResourceMapper {

	public void addResource(Resource resource);

	// public int updateResource(Resource resource);
	public List<Resource> searchAPIResources(Date updated_at, int categoryId,
			int batch_size);

	public Resource getResourceByUuid(String uuid);

	public Resource findResourceByVideoId(int videoId);

	public Resource findResourceByPdfId(int pdfId);

	public Resource findResourceByNewsId(int newsId);

	public Resource findResourceByThreeScreenId(int threeScreenId);

	public List<Resource> findUserFavorites(int userId, Date updated_at,
			int batch_size);

	public List<Resource> getResourcesByCateId(int id);

	public int updateResourceSpeaker(int resourceId, Integer speakerId);

	public List<Resource> findResouceRelated(int resId, int categoryId);

	public List<Resource> getResources(SubParams subParams);

	public List<Resource> searchResourceListByQ(String queryKey);

	public List<TagChild> getTagsByResourceUuid(String resourceUuid);

	public int resAddLabels(TagsResource tagsResource);

	public List<TagsResource> getTagsResourceByResourceId(int id);

	public int delResTagByResourceIdAndTagId(@Param("resId")Integer resId, @Param("tagId")Integer tagId);

	public List<Resource> searchAPIResourcesAllLatest(Date updated_at,
			int batch_size);

	public List<Resource> searchAPIResourcesLatest(Date updated_at,
			int categoryId, int batch_size);

	/**
	 * activity resource search
	 * */
	// begin
	public List<Resource> searchActivityRecommend(int activityId);

	public List<Resource> searchActivityResouces(int activityId,
			Date updated_at, int batch_size);

	public List<Resource> searchActivityReports(String activityName,
			Date updated_at, int batch_size);
	
	public List<Resource> searchActivityRankResources(int activityId);
	// end
	
	public List<Resource> getWebCastInitList();
	
	public List<Resource> getWebCastSearchList(String search);
	
	public List<Resource> searchResourceByQrSence(String search);
	
	public Resource getResourceById(int id);
	
	public Integer resourceCountByCategory(List<Integer> list);
	
	public List<Resource> resourceLatestByCategory(@Param("list")List<Integer> list,@Param("size")int size);
	
	public Integer resourceCountByCateId(int categoryId);
	
	public List<Resource> resourceFrontPage(int categoryId, int from,int size);
	
	public List<Resource> resourceActivityFrontPage(int activityId,int from, int size);
	
	public List<Resource> resourceBySpeakerWxPage(int speakerId,String startTime,int size);
	
	public List<Resource> resourceWxPage(int categoryId,String startTime,int size);

	
}
