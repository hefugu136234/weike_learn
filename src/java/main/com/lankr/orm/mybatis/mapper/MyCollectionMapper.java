package com.lankr.orm.mybatis.mapper;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.MyCollection;
import com.lankr.tv_cloud.model.User;

public interface MyCollectionMapper {

	public void addCollection(MyCollection myCollection);

	public MyCollection selectCollectionById(int id);

	public MyCollection selectCollectionByUuid(String uuid);

	public MyCollection selectCollectionByReIdAndUserId(SubParams params);

	public int updateCollectionStatus(MyCollection myCollection);

	public MyCollection searchCollectionByUserResource(int userId, int resId);
	
	public int selectCountByReId(int resId);
	
	public Integer collectResCountByUser(int userId);

	public List<MyCollection> getUserCollectionRecord(int id, String query,
			int startPage, int pageSize);

	public List<MyCollection> getCollectionUserRecord(int id, String query,
			int startPage, int pageSize);
}
