package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.AssetPrice;
import com.lankr.tv_cloud.model.Category;
import com.lankr.tv_cloud.model.Project;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Speaker;
import com.lankr.tv_cloud.model.Video;
import com.lankr.tv_cloud.vo.SpeakerVo;

public interface AssetFacade {

	public Status addCategory(Category category);

	public Status updateCategory(Category category);

	public List<Category> fetchProjectRootCategory(Project project);

	public Category getCategoryByUuid(String uuid);
	
	public Category getCategoryById(int id);

	public Category getCategoryByName(String name);

	public Status addVideo(Video video);

	public Status updateVideo(Video video);

	public Video getVideoByUUID(String uuid);

	//modified by mayuan --> 添加 "categoryUuid" 字段
	public Pagination<Video> searchVideos(int projectId, String q, int from,
			int to, String sortValString, String sortvalueString, String categoryUuid);

	public Status deleteCategory(Category category);

	public Status addAssetPrice(AssetPrice price);

	public List<Category> findAllChildrenCategory(Category root_category);
	

	public Status addSpeaker(Speaker speaker);

	public Status updateSpeaker(Speaker speaker);

	public Speaker getSpeakerByUuid(String uuid);

	public Pagination<Speaker> searchAllSpeakersPaginatio(int from, int rows,
			String query);
	
	public List<SpeakerVo> fetchAllSimpleSpeakers();
	
	public Status updateSpeakerStatus(Speaker speaker);

	public Status speakerAssociationUser(Speaker speaker);

	public Status updateSpeakerCleanUser(Speaker speaker);

}
