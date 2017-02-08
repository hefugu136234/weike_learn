package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.model.Media;

public interface ClicinMediaMapper {
	
	public List<Media> getClicinInfoById(int clinicId);
	
	public void clinicUploadIamge(Media media);

	public Media getMediaByUuid(String uuid);
	
	public int updateMedia(Media media);
}
