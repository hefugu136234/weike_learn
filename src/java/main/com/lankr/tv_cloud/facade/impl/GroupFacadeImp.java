package com.lankr.tv_cloud.facade.impl;

import java.util.List;

import com.lankr.tv_cloud.facade.GroupFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.Manufacturer;
import com.lankr.tv_cloud.model.ProductGroup;

public class GroupFacadeImp extends FacadeBaseImpl implements GroupFacade {

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "manufact";
	}

	@Override
	public Manufacturer selectManufacturerByUuid(String uuid) {
		// TODO Auto-generated method stub
		return manufacturerDao.getById(uuid,
				getSqlAlias("selectManufacturerByUuid"));
	}

	@Override
	public Pagination<Manufacturer> selectManufacturerList(String searchValue,
			int from, int pageItemTotal) {
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from manufacturer where isActive=1 and (name like '%"
				+ searchValue + "%' or pinyin like '%" + searchValue + "%')";
		Pagination<Manufacturer> pagination = initPage(sql, from, pageItemTotal);
		List<Manufacturer> list = manufacturerDao.searchAllPagination(
				getSqlAlias("selectManufacturerList"), searchValue, from,
				pageItemTotal);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public Status addManufacturer(Manufacturer manufacturer) {
		// TODO Auto-generated method stub
		try {
			manufacturerDao.add(manufacturer, getSqlAlias("addManufacturer"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("新增厂商出错", e);
		}
		return Status.SAVE_ERROR;
	}

	@Override
	public Status updateManufacturer(Manufacturer manufacturer) {
		try {
			manufacturerDao.update(manufacturer,
					getSqlAlias("updateManufacturer"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("修改厂商信息出错", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateManufacturerStatus(Manufacturer manufacturer) {
		// TODO Auto-generated method stub
		try {
			manufacturerDao.update(manufacturer,
					getSqlAlias("updateManufacturerStatus"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("修改厂商状态出错", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Pagination<ProductGroup> selectProgroupList(String searchValue,
			int from, int pageItemTotal, int manufacturerId) {
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from product_group where isActive=1 and manufacturerId="
				+ manufacturerId
				+ " and (name like '%"
				+ searchValue
				+ "%' or pinyin like '%" + searchValue + "%')";
		Pagination<ProductGroup> pagination = initPage(sql, from, pageItemTotal);
		SubParams params = new SubParams();
		params.id = manufacturerId;
		params.query = searchValue;
		List<ProductGroup> list = groupDao.searchAllPagination(
				getSqlAlias("selectProgroupList"), params, from, pageItemTotal);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public ProductGroup selectProgroupByUuid(String uuid) {
		// TODO Auto-generated method stub
		return groupDao.getById(uuid, getSqlAlias("selectProgroupByUuid"));
	}

	@Override
	public Status addProgroup(ProductGroup group) {
		try {
			groupDao.add(group, getSqlAlias("addProgroup"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("新增产品组出错", e);
		}
		return Status.SAVE_ERROR;
	}

	@Override
	public Status updateProgroupStatus(ProductGroup group) {
		// TODO Auto-generated method stub
		try {
			groupDao.update(group, getSqlAlias("updateProgroupStatus"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("产品组状态修改出错", e);
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateProgroup(ProductGroup group) {
		// TODO Auto-generated method stub
		try {
			groupDao.update(group, getSqlAlias("updateProgroup"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("产品组信息修改出错", e);
		}
		return Status.FAILURE;
	}

	@Override
	public List<Manufacturer> selectManufacturer() {
		// TODO Auto-generated method stub
		return manufacturerDao.searchAll(getSqlAlias("selectManufacturer"));
	}

	@Override
	public List<Manufacturer> selectManufacturerByQ(String q) {
		// TODO Auto-generated method stub
		return manufacturerDao.searchAll(getSqlAlias("selectManufacturerByQ"),
				q);
	}

	@Override
	public List<ProductGroup> getProductGroupListById(int manufacturerId) {
		// TODO Auto-generated method stub
		return groupDao.searchAll(getSqlAlias("getProductGroupListById"),
				manufacturerId);
	}

}
