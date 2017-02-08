package com.lankr.tv_cloud.web.api.app.vo;

import java.util.ArrayList;
import java.util.List;

import com.lankr.tv_cloud.model.Province;
import com.lankr.tv_cloud.vo.api.BaseAPIModel;

public class ProvinceVo extends BaseAPIModel{
	
	private List<RegionVoApp> provinceList;
	
	public void buildProvice(List<Province> list){
		provinceList=new ArrayList<RegionVoApp>();
		for (Province province : list) {
			RegionVoApp app=new RegionVoApp();
			app.setUuid(province.getUuid());
			app.setName(province.getName());
			provinceList.add(app);
		}
	}

}
