package com.lankr.tv_cloud.facade;

import java.util.Date;
import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Message;
import com.lankr.tv_cloud.model.MyCollection;
import com.lankr.tv_cloud.model.Praise;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceVoteAnswer;
import com.lankr.tv_cloud.model.ResourceVoteOption;
import com.lankr.tv_cloud.model.ResourceVoteSubject;
import com.lankr.tv_cloud.model.Resourceable;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.TagChild;
import com.lankr.tv_cloud.model.User;

public interface ResourceFacade {

	public Resource findFromResourceable(Resourceable res);

	public Status addOrUpdateResourceVote(ResourceVoteSubject subject);

	public List<ResourceVoteSubject> getVotesByResourceId(Resource resource);

	public Status delVote(String uuid);

	public Status delVoteOption(String uuid);

	public Status votedByUser(User user, String subject_uuid,
			List<String> options);

	public Status votedByOpenId(String openId, String subject_uuid,
			List<String> options);

	public List<Integer> seachVotedSubjectOptions(Integer userId,
			Integer subjectId);

	public List<Integer> seachVotedSubjectOptions(String openId,
			Integer subjectId);

	public ResourceVoteSubject getVoteSubjectByUuid(String uuid);

	public List<Resource> getResourcesByCateId(int id);
	
	
	public Status recordResourceSpeaker(Resourceable resourceable, Speaker speaker);
	
	public Resource getResource(Resourceable res);
	
	public List<Resource> getResourceRelated(Resource resource);
	
	public Resource getResourceByUuid(String uuid);

	public List<Resource> searchAPIResources(Date updated_at,
			Category category, int batch_size);

	public Pagination<Resource> getResourcesList(String searchValue,
			Category category, int startPage, int pageSize, String type, String state);

	public List<Resource> searchResourceListByQ(String queryKey);

	public List<TagChild> getTagsByResourceUuid(String resourceUuid);

	public ActionMessage resAddLabels(int[] tagsIds, int[] existsTagIds, int resId);

	public int[] getTagsIdsByResourceId(int id);

	public ActionMessage delResTagByResourceIdAndTagId(Integer resId, Integer tagId);
	
	public List<Resource> searchAPIResourcesAllLatest(Date updated_at,int batch_size);
	
	public List<Resource> searchAPIResourcesLatest(Date updated_at,
			Category category, int batch_size);
	
	public int countResourceView(Resource res);
	
	//后台直播绑定资源初始获取10条最新的视频数据
	public List<Resource> getWebCastInitList();
	
	public List<Resource> getWebCastSearchList(String search);
	
	public List<Resource> searchResourceByQrSence(String search);
	
	public Resource getResourceById(int id);
	
	public int resourceCountByCategory(List<Integer> list);
	
	public List<Resource> resourceLatestByCategory(List<Integer> list,int size);
	
	public int resourceCountByCateId(int categoryId);
	
	public Pagination<Resource> resourceFrontPage(int categoryId,int from,int size);
	
	public Pagination<Resource> resourceActivityFrontPage(int activityId,int from,int size);
	
	//wx专家对应讲者的资源
	public List<Resource> resourceBySpeakerWxPage(int speakerId,String startTime,int size);
	
	public List<Resource> resourceWxPage(int categoryId,String startTime,int size);

	public Pagination<ResourceVoteAnswer> getResourceVoteAnswerListByOptionUuid(
			String uuid, String q, int from, int size);

	public ResourceVoteOption getResourceVoteOptionByUuid(String uuid);

	public Pagination<MyCollection> getCollectionUserRecord(
			Resource resourceByUuid, String q, int from, int size);

	public Pagination<Praise> getPraiseUserRecord(Resource resourceByUuid,
			String q, int from, int size);

	public Pagination<Message> getCommentUserRecord(Resource resourceByUuid,
			String q, int from, int size);

}
