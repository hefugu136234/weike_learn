package com.lankr.tv_cloud.facade.impl;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.lankr.tv_cloud.facade.ActivationCodeFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.ActivationCode;
import com.lankr.tv_cloud.model.ProductGroup;
import com.lankr.tv_cloud.model.ViewSharing;
import com.lankr.tv_cloud.utils.Tools;

public class ActivationCodeFacadeImp extends FacadeBaseImpl implements
		ActivationCodeFacade {

	@Override
	public Pagination<ActivationCode> selectActivationList(String searchValue,
			int from, int pageItemTotal) {
		// TODO Auto-generated method stub
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from activation_code where isActive=1 and (activeCode like '%"
				+ searchValue + "%' or cardNum like '%" + searchValue + "%')";
		Pagination<ActivationCode> pagination = initPage(sql, from,
				pageItemTotal);
		List<ActivationCode> list = activationDao.searchAllPagination(
				getSqlAlias("selectActivationList"), searchValue, from,
				pageItemTotal);
		pagination.setResults(list);
		return pagination;
	}
	
	//modified by mayuan start --> show sharedetail
	@Override
	public Pagination<ViewSharing> selectShareList(String searchValue, int start, int pageSize) {
		// TODO Auto-generated method stub
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(vs.id) from view_sharing vs left join resource re on vs.resourceId = re.id where vs.isActive=1 and (re.name like '%"
				+ searchValue + "%')";
		Pagination<ViewSharing> pagination = initPage(sql, start, pageSize);
		
		SubParams params = new SubParams();
		params.setQuery(searchValue);
		params.setSize(pageSize);
		params.setStart(start);
		
		List<ViewSharing> list = shareGiftMpper.searchAllPagination(params);
		pagination.setResults(list);
		return pagination;
	}
	//modified by mayuan end

	@Override
	public Status addActivation(ActivationCode activationCode) {
		// TODO Auto-generated method stub
		try {
			activationDao.add(activationCode, getSqlAlias("addActivation"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("新增激活码出错", e);
		}
		return Status.SAVE_ERROR;
	}

	private int selectActivationCountBygroup(int groupId) {
		String sql = "select count(id) from activation_code where groupId=?";
		int total = jdbcTemplate.queryForInt(sql, new Object[] { groupId });
		return total;
	}

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return "activation_code";
	}

	private static DecimalFormat df = new DecimalFormat("0000");

	@Override
	public Status addBuildActivite(int numint, ProductGroup productGroup,
			int timeint) {
		// TODO Auto-generated method stub
		synchronized (productGroup.getDefaultLock()) {
			int current = selectActivationCountBygroup(productGroup.getId());
			for (int i = 1; i <= numint;) {
				try {
					String uuid = Tools.getUUID();
					ActivationCode activationCode = new ActivationCode();
					activationCode.setUuid(uuid);
					String activeCode = Tools.generateShortUuid(6);
					activationCode.setActiveCode(activeCode);
					long deadline = TimeUnit.DAYS.toSeconds(timeint);
					activationCode.setDeadline(deadline);
					activationCode.setProductGroup(productGroup);
					activationCode.setStatus(ActivationCode.STATUS_USEABLE);
					activationCode.setIsActive(1);
					// 卡号
					String pre = productGroup.getManufacturer().getSerialNum();
					String mid = productGroup.getSerialNum();
					// 数据库的个数
					String last = df.format(current + i);
					String cardNum = pre + mid + last;
					activationCode.setCardNum(cardNum);
					addActivation(activationCode);
					i++;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("add code with an error ", e);
					if (e instanceof SQLIntegrityConstraintViolationException) {
						//如果code健重复，继续添加
						continue;
					} else {
						i++;
					}
				}
			}
		}
		return Status.SUCCESS;
	}
	
	@Override
	public ActivationCode getActivationByCode(String code) {
		return activationDao.getById(code, getSqlAlias("selectActivationByCode"));
	}
	
	@Override
	public ActivationCode getActivationByUuid(String uuid) {
		// TODO Auto-generated method stub
		return activationDao.getById(uuid, getSqlAlias("getActivationByUuid"));
	}
	
	@Override
	public Status updateDisCode(ActivationCode activationCode) {
		// TODO Auto-generated method stub
		try {
			activationDao.update(activationCode, getSqlAlias("updateDisCode"));
			return Status.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	//modified by mayuan -->根据用户id查询流量卡
	@Override
	public List<ActivationCode> getActivationByUserId(int userId) {
		// TODO Auto-generated method stub
		return activationCodeMapper.getByUserId(userId);
	}
}
