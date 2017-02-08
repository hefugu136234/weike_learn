package com.lankr.tv_cloud.facade;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.ThreeScreen;

public interface ThreeScreenFacade {

	public Status addThreeScreen(ThreeScreen threeScreen);

	public ThreeScreen selectThreeScreenByUuid(String uuid);

	public Status updateThreeScreen(ThreeScreen threeScreen);

	public Status updateThreeScreenStatus(ThreeScreen threeScreen);

	public Pagination<ThreeScreen> selectThreeScreenList(String searchValue,
			int from, int pageItemTotal);

	public ActionMessage addPlaysInfo(ThreeScreen threeScreen,
			String plays_metainfo);
	
	public Status updateThreeScreenRelation(ThreeScreen threeScreen);
}
