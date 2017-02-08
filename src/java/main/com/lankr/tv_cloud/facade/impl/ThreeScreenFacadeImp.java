package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.orm.mybatis.mapper.ThreeScreenMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.ThreeScreenFacade;
import com.lankr.tv_cloud.model.ThreeScreen;

public class ThreeScreenFacadeImp extends FacadeBaseImpl implements
		ThreeScreenFacade {


	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "com.lankr.orm.mybatis.mapper.ThreeScreenMapper";
	}

	@Override
	public Status addThreeScreen(ThreeScreen threeScreen) {
		// TODO Auto-generated method stub
		try {
			threeScreenDao.add(threeScreen, getSqlAlias("addThreeScreen"));
			recodeResource(threeScreen);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("新增三分屏数据出错", e);
		}
		return Status.SAVE_ERROR;
	}

	@Override
	public ThreeScreen selectThreeScreenByUuid(String uuid) {
		// TODO Auto-generated method stub
		return threeScreenDao.getById(uuid,
				getSqlAlias("selectThreeScreenByUuid"));
	}

	@Override
	public Status updateThreeScreen(ThreeScreen threeScreen) {
		// TODO Auto-generated method stub
		try {
			threeScreenDao
					.update(threeScreen, getSqlAlias("updateThreeScreen"));
			recodeResource(threeScreen);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("修改三分屏数据出错", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateThreeScreenStatus(ThreeScreen threeScreen) {
		// TODO Auto-generated method stub
		try {
			threeScreenDao.update(threeScreen,
					getSqlAlias("updateThreeScreenStatus"));
			recodeResource(threeScreen);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("修改三分屏数据状态出错", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Pagination<ThreeScreen> selectThreeScreenList(String searchValue,
			int from, int pageItemTotal) {
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from three_screen where isActive=1 and (name like '%"
				+ searchValue + "%' or pinyin like '%" + searchValue + "%')";
		Pagination<ThreeScreen> pagination = initPage(sql, from, pageItemTotal);
		List<ThreeScreen> list = threeScreenDao.searchAllPagination(
				getSqlAlias("selectThreeScreenList"), searchValue, from,
				pageItemTotal);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public ActionMessage addPlaysInfo(ThreeScreen threeScreen,
			String plays_metainfo) {
		if (threeScreen == null)
			return ActionMessage.failStatus("资源为空");
		try {
			threeScreenMapper.addPlaysInfo(threeScreen.getId(), plays_metainfo);
		} catch (Exception e) {
			e.printStackTrace();
			return ActionMessage.failStatus("添加播放元素失败");
		}
		return ActionMessage.successStatus();
	}
	
	@Override
	public Status updateThreeScreenRelation(ThreeScreen threeScreen) {
		// TODO Auto-generated method stub
		try {
			threeScreenDao.update(threeScreen,
					getSqlAlias("updateThreeScreenRelation"));
			recodeResource(threeScreen);
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("修改三分屏数据对应关系出错", e);
		}
		return Status.FAILURE;
	}

}
