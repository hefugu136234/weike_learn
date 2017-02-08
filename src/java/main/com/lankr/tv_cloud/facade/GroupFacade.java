package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.ProductGroup;

public interface GroupFacade {
	
	public Manufacturer selectManufacturerByUuid(String uuid);
	
	public List<Manufacturer> selectManufacturer();
	
	public List<Manufacturer> selectManufacturerByQ(String q);
	
	public Pagination<Manufacturer> selectManufacturerList(String searchValue,
			int from, int pageItemTotal);
	
	public Status addManufacturer(Manufacturer manufacturer);
	
	public Status updateManufacturerStatus(Manufacturer manufacturer);
	
	public Status updateManufacturer(Manufacturer manufacturer);
	
	public Pagination<ProductGroup> selectProgroupList(String searchValue,
			int from, int pageItemTotal,int manufacturerId);
	
	public ProductGroup selectProgroupByUuid(String uuid);
	
	public Status addProgroup(ProductGroup group);
	
	public Status updateProgroupStatus(ProductGroup group);
	
	public Status updateProgroup(ProductGroup group);
	
	public List<ProductGroup> getProductGroupListById(int manufacturerId);

}
