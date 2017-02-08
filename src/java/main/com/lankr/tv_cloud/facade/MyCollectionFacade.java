package com.lankr.tv_cloud.facade;

import java.util.Date;
import java.util.List;

import com.lankr.tv_cloud.facade.impl.SubParams;
import com.lankr.tv_cloud.model.MyCollection;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;

public interface MyCollectionFacade {

	public Status addCollection(MyCollection myCollection);

	public MyCollection selectCollectionById(int id);

	public MyCollection selectCollectionByUuid(String uuid);

	public MyCollection selectCollectionByReIdAndUserId(SubParams params);

	public Status updateCollectionStatus(MyCollection myCollection);

	/**
	 * 如果没有收藏 该资源返回 false，否则返回true
	 * */
	public boolean favoriteAction(Resource res, User user);

	public boolean isFavoritedResourceByUser(Resource res, User user);

	public List<Resource> getUserFavoriteResources(User user, Date updated_at,
			int batch_size);
	
	public int selectCountByReId(int resId);
	
	public int collectResCountByUser(User user);
}
