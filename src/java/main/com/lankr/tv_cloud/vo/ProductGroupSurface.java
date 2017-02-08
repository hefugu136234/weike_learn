package com.lankr.tv_cloud.vo;

import java.util.List;

import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.ProductGroup;
import com.lankr.tv_cloud.vo.datatable.DataTableModel;

public class ProductGroupSurface extends DataTableModel<ProductGroupVo>{
	
	public void buildMuman(List<Manufacturer> list){
		if(list==null||list.isEmpty()){
			return ;
		}
		for (Manufacturer manufacturer : list) {
			build(manufacturer);
		}
	}
	
	public void buildGroup(List<ProductGroup> list){
		if(list==null||list.isEmpty()){
			return ;
		}
		for (ProductGroup productGroup : list) {
			build(productGroup);
		}
	}
	
	public void build(Manufacturer muManufacturer){
		if(muManufacturer==null)
			return ;
		ProductGroupVo vo=new ProductGroupVo();
		vo.buildData(muManufacturer);
		aaData.add(vo);
	}
	
	public void build(ProductGroup group){
		if(group==null)
			return ;
		ProductGroupVo vo=new ProductGroupVo();
		vo.buildData(group);
		aaData.add(vo);
	}

}
