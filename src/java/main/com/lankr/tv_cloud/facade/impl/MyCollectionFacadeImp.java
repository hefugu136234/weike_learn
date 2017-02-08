package com.lankr.tv_cloud.facade.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.orm.mybatis.mapper.MyCollectionMapper;
import com.lankr.tv_cloud.facade.MyCollectionFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.MyCollection;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.Tools;

public class MyCollectionFacadeImp extends FacadeBaseImpl implements
		MyCollectionFacade {

	@Override
	public Status addCollection(MyCollection myCollection) {
		// TODO Auto-generated method stub
		try {
			collectionMapper.addCollection(myCollection);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("myCollection save error", e);
		}
		return Status.SAVE_ERROR;
	}

	@Override
	public MyCollection selectCollectionById(int id) {
		// TODO Auto-generated method stub
		return collectionMapper.selectCollectionById(id);
	}

	@Override
	public MyCollection selectCollectionByUuid(String uuid) {
		// TODO Auto-generated method stub
		return collectionMapper.selectCollectionByUuid(uuid);
	}

	@Override
	public MyCollection selectCollectionByReIdAndUserId(SubParams params) {
		// TODO Auto-generated method stub
		return collectionMapper.selectCollectionByReIdAndUserId(params);
	}

	@Override
	public Status updateCollectionStatus(MyCollection myCollection) {
		// TODO Auto-generated method stub
		try {
			collectionMapper.updateCollectionStatus(myCollection);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("update collect status error", e);
		}
		return Status.FAILURE;
	}

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean favoriteAction(Resource res, User user) {
		//
		MyCollection coll = collectionMapper.searchCollectionByUserResource(
				user.getId(), res.getId());
		if (coll == null) {
			coll = new MyCollection();
			coll.setUser(user);
			coll.setResource(res);
			coll.setStatus(1);
			coll.setIsActive(MyCollection.ACTIVE);
			coll.setUuid(Tools.getUUID());
			collectionMapper.addCollection(coll);
			return true;
		}
		int status = 1 ^ coll.getStatus();
		coll.setStatus(status);
		int effect = collectionMapper.updateCollectionStatus(coll);
		return status == effect;
	}

	@Override
	public boolean isFavoritedResourceByUser(Resource res, User user) {
		MyCollection coll = collectionMapper.searchCollectionByUserResource(
				user.getId(), res.getId());
		if (coll == null)
			return false;
		return coll.getStatus() == 1;
	}

	@Override
	public List<Resource> getUserFavoriteResources(User user, Date updated_at,
			int batch_size) {
		return resourceMapper.findUserFavorites(user.getId(), updated_at,
				Math.min(50, batch_size));
	}
	
	@Override
	public int selectCountByReId(int resId) {
		// TODO Auto-generated method stub
		int num=collectionMapper.selectCountByReId(resId);
		return num;
	}
	
	@Override
	public int collectResCountByUser(User user) {
		// TODO Auto-generated method stub
		if(user==null){
			return 0;
		}
		Integer count=collectionMapper.collectResCountByUser(user.getId());
		if(count==null){
			return 0;
		}
		return count;
	}
}
