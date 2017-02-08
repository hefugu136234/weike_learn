package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.Speaker;

public interface SpeakerMapper {

	public int addSpeaker(Speaker speaker);

	public int updateSpeaker(Speaker speaker);
	
	public int updateSpeakerStatus(Speaker speaker);

	public Speaker getSpeakerByUuid(String uuid);

	public List<Speaker> searchSpeakerPagination(int from, int rows,
			String qurey);
	
	public List<Speaker> searchSpeakerListByQ(String q);
	
	public List<Speaker> searchSpeakerList();

	public int speakerAssociationUser(Speaker speaker);

	public Speaker getSpeakerByUserId(int userId);

	public int updateSpeakerCleanUser(Speaker speaker);
	
	public Speaker getSpeakerById(int id);

}
