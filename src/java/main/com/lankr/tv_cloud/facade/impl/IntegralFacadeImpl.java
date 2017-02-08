package com.lankr.tv_cloud.facade.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lankr.orm.mybatis.mapper.IntegralMapper;
import com.lankr.tv_cloud.cache.lru.LruCache;
import com.lankr.tv_cloud.codes.Code;
import com.lankr.tv_cloud.codes.CodeProvider;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.IntegralFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Certification;
import com.lankr.tv_cloud.model.IntegralConfig;
import com.lankr.tv_cloud.model.IntegralConsume;
import com.lankr.tv_cloud.model.IntegralRecord;
import com.lankr.tv_cloud.model.IntegralWeekReport;
import com.lankr.tv_cloud.model.LogisticsAddress;
import com.lankr.tv_cloud.model.LogisticsInfo;
import com.lankr.tv_cloud.model.OfflineActivity;
import com.lankr.tv_cloud.model.ReceiptAddress;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.Resource.Type;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.utils.OptionalUtils;
import com.lankr.tv_cloud.utils.Tools;

/**
 * @author Kalean.Xiang
 *
 */
public class IntegralFacadeImpl extends FacadeBaseImpl implements
		IntegralFacade {

	private final InnerConfig configuration = new InnerConfig();

	// 用户积分新增权限缓存
	private final AddableCache addable_cache = new AddableCache();

	// 用户总积分缓存
	private final UserIntegralTotalCache user_integral_cache = new UserIntegralTotalCache();

	// 积分总是缓存时间常量 1个小时
	private static final long USER_INTEGRAL_CACHE_EXPIRED = TimeUnit.HOURS
			.toMillis(4);

	@Autowired
	public void setIntegralMapper(IntegralMapper integralMapper) {
		this.integralMapper = integralMapper;
	}

	@Override
	public int actionRegister(User user) {
		return internal(user, IntegralConfig.ACTION_REGISTER, null, null);
	}

	@Override
	public int actionCertification(User user) {
		// TODO Auto-generated method stub
		return internal(user, IntegralConfig.ACTION_CERTIFICATION, null, null);
	}

	@Override
	public int actionShareRegister(User user, User registerUser) {
		return internal(registerUser, IntegralConfig.ACTION_SHARE_REGISTER,
				null, registerUser);
	}

	@Override
	public int actionContributeResource(Resource resource) {
		if (resource == null) {
			return 0;
		}
		User user = OptionalUtils.traceValue(resource, "speaker.user",
				User.class, null);
		int action = 0;
		if (Type.VIDEO == resource.getType()) {
			action = IntegralConfig.ACTION_CONTRIBUTE_VIDEO;
		} else if (Type.THREESCREEN == resource.getType()) {
			action = IntegralConfig.ACTION_CONTRIBUTE_THREESCREEN;
		} else if (Type.PDF == resource.getType()) {
			action = IntegralConfig.ACTION_CONTRIBUTE_PDF;
		}
		return internal(user, action, resource, null);
	}

	@Override
	public int actionResourcePlayed(Resource resource, User fromUser) {
		User user = OptionalUtils.traceValue(resource, "speaker.user",
				User.class, null);
		// TODO Auto-generated method stub
		return internal(user, IntegralConfig.ACTION_RESOURCE_PLAYED, resource,
				fromUser);
	}

	@Override
	public int actionResourcePraised(Resource resource, User fromUser) {
		User user = OptionalUtils.traceValue(resource, "speaker.user",
				User.class, null);
		// TODO Auto-generated method stub
		return internal(user, IntegralConfig.ACTION_RESOURCE_PRAISED, resource,
				fromUser);
	}

	@Override
	public int actionResourceShared(Resource resource, User fromUser) {
		User user = OptionalUtils.traceValue(resource, "speaker.user",
				User.class, null);
		return internal(user, IntegralConfig.ACTION_RESOURCE_SHARED, resource,
				fromUser);
	}

	@Override
	public int actionUserPlayResource(User user, Resource resource) {
		// 需要给资源拥有者追加积分
		actionResourcePlayed(resource, user);
		return internal(user, IntegralConfig.ACTION_USER_PLAY_RESOURCE,
				resource, null);
	}

	@Override
	public int actionUserVoteResource(User user, Resource resource) {
		// TODO Auto-generated method stub
		return internal(user, IntegralConfig.ACTION_USER_VOTE_RESOURCE,
				resource, null);
	}

	@Override
	public int actionUserPraiseResource(User user, Resource resource) {
		// TODO Auto-generated method stub
		// 需要给资源拥有者追加积分
		actionResourcePraised(resource, user);

		return internal(user, IntegralConfig.ACTION_USER_PRAISE_RESOURCE,
				resource, null);
	}

	@Override
	public int actionUserSharingViewed(User sharingUser, Resource resource,
			User viewedUser) {
		// 需要给资源拥有者追加积分
		actionResourceShared(resource, sharingUser);
		return internal(sharingUser, IntegralConfig.ACTION_USER_SHARING_VIEWED,
				resource, viewedUser);
	}

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return IntegralMapper.class.getName();
	}

	/**
	 * @return 返回添加的积分
	 * */
	private int internal(User user, int action, Resource resource, User fromUser) {
		if (user == null || user.isSame(fromUser)) {
			return 0;
		}
		synchronized (user.getDefaultLock()) {
			if (addable(action, user, resource, fromUser)) {
				// 添加积分
				IntegralRecord record = new IntegralRecord();
				IntegralConfig config = configuration.config(action);
				if (config == null)
					return -1;
				record.setAction(config.getAction());
				record.setUser(user);
				record.setFromUser(fromUser);
				record.setUuid(Tools.getUUID());
				record.setResource(resource);
				record.setStatus(1);
				record.setValue(config.getValue());
				record.setMark(config.getMark());
				int effect = integralMapper.addIntegralRecord(record);
				if (effect > 0) {
					onIntegralChanged(user, record.getValue());
					return record.getValue();
				}
			}
		}
		return 0;
	}

	// 兑换积分
	@Override
	public ActionMessage userConsumeIntgral(User user,
			IntegralConsume consumeable, ReceiptAddress receiptAddress) {
		if (!BaseModel.hasPersisted(user)
				|| !BaseModel.hasPersisted(consumeable)) {
			return ActionMessage.failStatus("无效参数");
		}
		synchronized (user.getDefaultLock()) {
			int total = fetchUserIntegralTotal(user);
			if (total < consumeable.getIntegral()) {
				return codeProvider.code(-1001).getActionMessage();
			}
			// 如果不能兑换
			Code code = consumeable(user, consumeable);
			if (code.isBad()) {
				return code.getActionMessage();
			}
			int effect = exchangeLogicAdddress(user, consumeable,
					receiptAddress);
			if (effect > 0) {
				int value = -1 * Math.abs(consumeable.getIntegral());
				onIntegralChanged(user, value);
				return ActionMessage.successStatus();
			}
		}
		return ActionMessage.failStatus("兑换失败");
	}

	/**
	 * 2016-4-1,修改兑换商品物流地址
	 * 
	 * @param user
	 * @param consumeable
	 * @return
	 */
	public int exchangeLogicAdddress(User user, IntegralConsume consumeable,
			ReceiptAddress receiptAddress) {
		int effect = 0;
		// 兑换操作
		IntegralRecord record = new IntegralRecord();
		record.setUser(user);
		record.setValue(-1 * Math.abs(consumeable.getIntegral()));
		record.setMark(consumeable.getName());
		record.setUuid(Tools.getUUID());
		record.setAction(IntegralConfig.ACTION_USER_CONSUME_INTEGRAL);
		// 默认为未兑换
		record.setStatus(BaseModel.UNAPPROVED);
		record.setConsume(consumeable);
		if (consumeable.getType() == IntegralConsume.TYPE_ENTITY_GOODS
				&& receiptAddress != null) {
			// 创建物流信息
			LogisticsInfo logisticsInfo = new LogisticsInfo();
			logisticsInfo.setUuid(Tools.getUUID());
			logisticsInfo.setStatus(BaseModel.APPROVED);
			logisticsInfo.setIsActive(BaseModel.ACTIVE);
			logisticsInfo.setLogistics(LogisticsAddress
					.buildAddressJson(receiptAddress));
			TransactionTemplate transaction = new TransactionTemplate(
					transactionManager);
			try {
				transaction.execute(new TransactionCallback<Object>() {

					@Override
					public Object doInTransaction(TransactionStatus arg0) {
						// TODO Auto-generated method stub
						integralMapper.addIntegralRecord(record);
						logisticsInfo.setIntegralRecord(record);
						receiptAddressMapper.addLogisticsInfo(logisticsInfo);
						return null;
					}
				});
				effect = 1;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			effect = integralMapper.addIntegralRecord(record);
		}
		return effect;

	}

	// 用户能否兑换
	private Code consumeable(User user, IntegralConsume consumeable) {
		// 是否需要实名
		if (consumeable.needUserCertificated()) {
			Certification cert = certificationMapper
					.getCertifiActiveByUserId(user.getId());
			if (cert == null || !cert.isCertificated()) {
				return codeProvider.code(-1002);
			}
		}
		// 用户兑换是否达到上限
		if (consumeable.getUserLimited() > 0) {
			String sql = "select count(*) from integral_record where integralConsumeId="
					+ consumeable.getId() + " and userId=" + user.getId();
			int used = jdbcTemplate.queryForInt(sql);
			if (used > consumeable.getUserLimited()) {
				return codeProvider.code(-1004);
			}
		}
		if (consumeable.getNumber() > 0) {
			String sql = "select count(*) from integral_record where integralConsumeId="
					+ consumeable.getId();
			int count = jdbcTemplate.queryForInt(sql);
			if (count > consumeable.getNumber()) {
				return codeProvider.code(-1003);
			}
		}
		return codeProvider.codeOk();
	}

	/**
	 * <b>积分添加之后的调用函数</b>
	 * 
	 * @param user
	 *            积分加入的用户
	 * @param value
	 *            加入积分的值
	 * */
	protected void onIntegralChanged(User user, int value) {
		// 积分发生改变，更新
		Integer key = user.getId();
		Integer origin = user_integral_cache.get(key, false);
		if (origin != null) {
			user_integral_cache.put(key, Math.max(0, origin + value));
		}
	}

	// 判断此action 是否需要添加积分
	private boolean addable(int action, User user, Resource resource,
			User fromUser) {
		if (user == null)
			return false;
		IntegralConfig config = configuration.config(action);
		if (config == null)
			return false;
		// 判断的依据为是否已经存在加分的记录
		StringBuilder sb = new StringBuilder(
				"select count(id) from integral_record where isActive=1");
		sb.append(" and action=" + action)
				.append(" and userId=" + user.getId());
		if (resource != null) {
			sb.append(" and resourceId=" + resource.getId());
		}
		if (fromUser != null) {
			sb.append(" and fromUserId=" + fromUser.getId());
		}
		// 是否已经缓存
		Boolean addable = addable_cache.get(sb.toString(), false);
		String addable_sql = sb.toString();
		if (null == addable)
			addable = true;
		if (addable) {
			addable = 0 == jdbcTemplate.queryForInt(addable_sql);
		}
		// 不需要添加积分的情况缓存
		if (!addable) {
			addable_cache.put(addable_sql, addable);
		}
		return addable;
	}

	private class AddableCache extends LruCache<String, Boolean> {

		@Override
		protected int configMaxMemorySize() {
			return super.configMaxMemorySize();
		}

	}

	private class InnerConfig {

		private List<IntegralConfig> configs;
		// 设置config的过期时间为8个小时
		private long expired_time = TimeUnit.HOURS.toMillis(8);

		// 更新config的时间
		private long updated_time;

		private List<IntegralConfig> getConfigs() {
			if (needUpdated()) {
				synchronized (InnerConfig.this) {
					configs = integralMapper.getIntegralConfigs();
					if (configs != null && configs.isEmpty()) {
						updated_time = System.currentTimeMillis();
					}
				}
			}
			return configs;
		}

		public IntegralConfig config(int action) {
			configs = getConfigs();
			if (configs == null)
				return null;
			for (int i = 0; i < configs.size(); i++) {
				IntegralConfig config = configs.get(i);
				if (action == config.getAction()) {
					return config;
				}
			}
			return null;
		}

		// 是否需要更新配置缓存
		boolean needUpdated() {
			return System.currentTimeMillis() - updated_time > expired_time;
		}

	}

	@Override
	public int fetchUserIntegralTotal(User user) {
		if (user == null)
			return 0;
		Integer total = user_integral_cache.get(user.getId(),
				USER_INTEGRAL_CACHE_EXPIRED, true);
		return total;
	}

	private class UserIntegralTotalCache extends LruCache<Integer, Integer> {
		// 缓存10000个用户总积分
		@Override
		protected int configMaxMemorySize() {
			return 10000;
		}

		@Override
		protected Integer create(Integer key) {
			String sql = "select sum(value) from integral_record where isActive=1 and userId="
					+ key;
			return Math.max(0, jdbcTemplate.queryForInt(sql));
		}
	}

	@Override
	public Pagination<IntegralRecord> userIntegralRecords(User user,
			String query, int page, int batch_size) {
		if (user == null)
			return null;
		query = filterSQLSpecialChars(query);
		Pagination<IntegralRecord> records = initPage(
				"select count(id) from integral_record where isActive=1 and userId="
						+ user.getId() + " and mark like '%" + query + "%'",
				page, batch_size);
		records.setResults(integralMapper.fetchUserIntegralRecords(
				user.getId(), query, page, records.getPage_rows()));
		return records;
	}

	@Override
	public Pagination<IntegralRecord> userIntegralConsumeRecords(User user,
			int page, int batch_size) {
		if (user == null)
			return null;
		Pagination<IntegralRecord> records = initPage(
				"select count(id) from integral_record where isActive=1 and and action < 0 and userId="
						+ user.getId(), page, batch_size);
		records.setResults(integralMapper.fetchUserIntegralConsumeRecords(
				user.getId(), page, records.getPage_rows()));
		return records;
	}

	@Override
	public Pagination<IntegralRecord> userIntegralFetchRecords(User user,
			int page, int batch_size) {
		if (user == null)
			return null;
		Pagination<IntegralRecord> records = initPage(
				"select count(id) from integral_record  where isActive=1 and action > 0 and userId="
						+ user.getId(), page, batch_size);
		records.setResults(integralMapper.fetchUserIntegralFetchRecords(
				user.getId(), page, records.getPage_rows()));
		return records;
	}

	@Override
	public Pagination<IntegralRecord> allIntegralConsumeRecords(String query,
			int startPage, int pageSize) {
		query = filterSQLSpecialChars(query);
		Pagination<IntegralRecord> records;
		String sql = " select count(*) from integral_record ir "
				+ " left join user u on ir.userId = u.id "
				+ " left join integral_consume ic on ir.integralConsumeId = ic.id "
				+ " where ir.isActive=1 and ir.action < 0 "
				+ " and ((u.nickname like '%" + query
				+ "%') or (u.pinyin like '%" + query
				+ "%') or (ic.name like '%" + query
				+ "%') or (ic.pinyin like '%" + query + "%'))";
		records = initPage(sql, startPage, pageSize);
		records.setResults(integralMapper.fetchAllIntegralFetchRecords(query,
				startPage, pageSize));
		return records;
	}

	@Override
	public ActionMessage addIntegralConsume(IntegralConsume consume) {
		if (consume == null) {
			return ActionMessage.failStatus("没有商品");
		}
		consume.setStatus(BaseModel.UNAPPROVED);
		consume.setUuid(Tools.getUUID());
		consume.setPinyin(Tools.getPinYin(consume.getName()));
		consume.setIsActive(BaseModel.ACTIVE);
		try {
			integralConsumeMapper.addIntegralConsume(consume);
		} catch (Exception e) {
			return ActionMessage.failStatus("无法添加商品，请设置正确的值");
		}
		return ActionMessage.successStatus();
	}

	@Override
	public Pagination<IntegralConsume> searchIntegralConsumeList(
			String searchValue, int from, int size) {
		// TODO Auto-generated method stub
		searchValue = filterSQLSpecialChars(searchValue);
		String sql = "select count(id) from integral_consume where isActive=1 and (type=1 or type=3) and (name like '%"
				+ searchValue + "%' or pinyin like '%" + searchValue + "%')";
		Pagination<IntegralConsume> pagination = initPage(sql, from, size);
		List<IntegralConsume> list = integralConsumeMapper
				.searchIntegralConsumeList(searchValue, from, size);
		pagination.setResults(list);
		return pagination;
	}

	@Override
	public Status updateIntegralConsume(IntegralConsume integralConsume) {
		try {
			int effect = integralConsumeMapper
					.updateIntegralConsume(integralConsume);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public Status updateIntegralConsumeStatus(IntegralConsume integralConsume) {
		try {
			int effect = integralConsumeMapper
					.updateIntegralConsumeStatus(integralConsume);
			if (effect > 0) {
				return Status.SUCCESS;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Status.FAILURE;
	}

	@Override
	public IntegralConsume getIntegralConsumeByUuid(String uuid) {
		// TODO Auto-generated method stub
		return integralConsumeMapper.getIntegralConsumeByUuid(uuid);
	}

	@Override
	public IntegralRecord getIntegralRecordByUuid(String uuid) {
		return integralMapper.getIntegralRecordByUuid(uuid);
	}

	@Override
	public IntegralRecord updateIntegralRecordStatus(IntegralRecord recordByUuid) {
		int effect = 0;
		try {
			effect = integralMapper.updateCertificationStatus(recordByUuid);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		if (effect > 0) {
			return integralMapper.getIntegralRecordByUuid(recordByUuid
					.getUuid());
		}
		return null;
	}

	@Override
	public List<IntegralConsume> getLatestGoodsForWx() {
		// TODO Auto-generated method stub
		return integralConsumeMapper.getLatestGoodsForWx();
	}

	// @Override
	// public ActionMessage superAdminChangeIntegral(User user, int integral) {
	// try {
	// IntegralRecord record = new IntegralRecord();
	// record.setUser(user);
	// record.setUuid(Tools.getUUID());
	// record.setStatus(1);
	// record.setValue(integral);
	// record.setMark("超管修改积分");
	// int effect = integralMapper.addIntegralRecord(record);
	// if (effect > 0) {
	// onIntegralChanged(user, record.getValue());
	// }
	// return ActionMessage.successStatus();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return ActionMessage.failStatus("积分修改失败");
	// }

	@Override
	public int actionSystemGrantUserIntegral(User user, int integral,
			String mark) {
		if (!BaseModel.hasPersisted(user))
			return 0;
		synchronized (user.getDefaultLock()) {
			int total_integral = fetchUserIntegralTotal(user);
			if (integral == 0)
				return total_integral;
			int action = integral < 0 ? IntegralConfig.ACTION_SYSTEM_DEDUCTING
					: IntegralConfig.ACTION_SYSTEM_ADDITION;
			integral = Math.max(-1 * Math.abs(total_integral), integral);
			IntegralRecord record = new IntegralRecord();
			record.setUser(user);
			record.setUuid(Tools.getUUID());
			record.setStatus(BaseModel.APPROVED);
			record.setValue(integral);
			record.setMark(mark);
			record.setAction(action);
			record.setIsActive(BaseModel.ACTIVE);
			int effect = integralMapper.addIntegralRecord(record);
			if (effect > 0) {
				onIntegralChanged(user, record.getValue());
			}
			return fetchUserIntegralTotal(user);
		}
	}

	@Override
	public List<IntegralRecord> userExchangeIntegralWx(User user,
			String startTime, int batch_size) {
		return integralMapper.userExchangeIntegralWx(user.getId(), startTime,
				batch_size);
	}

	@Override
	public List<IntegralRecord> userIntegralDetailWx(User user,
			String startTime, int batch_size) {
		// TODO Auto-generated method stub
		return integralMapper.userIntegralDetailWx(user.getId(), startTime,
				batch_size);
	}

	@Override
	public List<IntegralWeekReport> userIntegralWeekReportWx(Date start,Date end) {
		// TODO Auto-generated method stub
		return integralMapper.userIntegralWeekReportWx(start,end);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.IntegralFacade#saveBroadcastIntegeralConsume
	 * (com.lankr.tv_cloud.model.Broadcast, int)
	 */
	@Override
	public ActionMessage saveBroadcastIntegeralConsume(Broadcast cast,
			int integral) {
		if (!BaseModel.hasPersisted(cast)) {
			return ActionMessage.failStatus("error broadcast");
		}
		try {
			IntegralConsume consume = getConsumeByType(
					IntegralConsume.TYPE_LIVE, cast.getId());
			if (consume == null) {
				consume = new IntegralConsume();
				consume.setUuid(Tools.getUUID());
				consume.setType(IntegralConsume.TYPE_LIVE);
				consume.setReferId(cast.getId());
			}
			consume.setIntegral(integral);
			consume.setName(cast.getName());
			consume.setStatus(BaseModel.APPROVED);
			consume.setUserLimited(1);
			if (consume.hasPersisted()) {
				integralConsumeMapper.updateIntegralConsume(consume);
			} else {
				integralConsumeMapper.addIntegralConsume(consume);
			}
		} catch (Exception e) {
			logger.error(e);
			return ActionMessage
					.failStatus("save broadcast integral consume error");
		}
		return ActionMessage.successStatus();
	}
	
	@Override
	public ActionMessage<?> offlineActivityIntegeralConsume(
			OfflineActivity offlineActivity, int integral) {
		// TODO Auto-generated method stub
		if (!BaseModel.hasPersisted(offlineActivity)) {
			return ActionMessage.failStatus("线下活动出错");
		}
		try {
			IntegralConsume consume = getConsumeByType(
					IntegralConsume.TYPE_OFFLINE_ACTIVITY, offlineActivity.getId());
			if (consume == null) {
				consume = new IntegralConsume();
				consume.setUuid(Tools.getUUID());
				consume.setType(IntegralConsume.TYPE_OFFLINE_ACTIVITY);
				consume.setReferId(offlineActivity.getId());
			}
			consume.setIntegral(integral);
			consume.setName(offlineActivity.getName());
			consume.setStatus(BaseModel.APPROVED);
			consume.setUserLimited(1);
			if (consume.hasPersisted()) {
				integralConsumeMapper.updateIntegralConsume(consume);
			} else {
				integralConsumeMapper.addIntegralConsume(consume);
			}
		} catch (Exception e) {
			logger.error(e);
			return ActionMessage
					.failStatus("线下活动关联积分商品失败");
		}
		return ActionMessage.successStatus();
	}

	/**
	 * @author Kalean.Xiang
	 * @createDate: 2016年3月23日
	 * @modifyDate: 2016年3月23日 获取非商品的积分消耗单元
	 */
	private IntegralConsume getConsumeByType(int type, int referId) {
		if (referId <= 0)
			return null;
		return integralConsumeMapper.getIntegralConsumeByType(type, referId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.IntegralFacade#searchBroadcastIntegralConsume
	 * (com.lankr.tv_cloud.model.Broadcast)
	 */
	@Override
	public IntegralConsume searchBroadcastIntegralConsume(Broadcast cast) {
		// TODO Auto-generated method stub
		if (!BaseModel.hasPersisted(cast)) {
			return null;
		}
		return getConsumeByType(IntegralConsume.TYPE_LIVE, cast.getId());
	}

	@Override
	public IntegralWeekReport getUserMaxAndHisRecordByUserId(User user) {
		if (null == user)
			return null;
		return integralMapper.getUserMaxAndHisRecordByUserId(user.getId());
	}
	
	@Override
	public int userIntegralWeekChange(int userId, Date lastWeek) {
		// TODO Auto-generated method stub
		Integer result=integralMapper.userIntegralWeekChange(userId, lastWeek);
		if(result==null){
			result=0;
		}
		return result;
	}
	
	@Override
	public int userResourceComment(User user, Resource resource) {
		// TODO Auto-generated method stub
		return addntegralByComment(user, resource, null);
	}
	
	public int addntegralByComment(User user, Resource resource,String mark) {
		// TODO Auto-generated method stub
		if (user == null) {
			return 0;
		}
		synchronized (user.getDefaultLock()) {
			Date date=Tools.getCurrentDate();
			String day=Tools.formatYMDDate(date);
			String start=day+" 00:00:00";
			String end=day+" 23:59:59";
			Integer actionSum=integralMapper.userIntegralOfDayByComment(user.getId(), start, end, IntegralConfig.ACTION_USER_RESOURCE_COMMENT);
			if(actionSum==null||actionSum<100){
				IntegralRecord record = new IntegralRecord();
				IntegralConfig config = configuration.config(IntegralConfig.ACTION_USER_RESOURCE_COMMENT);
				if (config == null)
					return -1;
				record.setAction(config.getAction());
				record.setUser(user);
				record.setUuid(Tools.getUUID());
				record.setResource(resource);
				record.setStatus(1);
				record.setValue(config.getValue());
				if(Tools.isBlank(mark)){
					record.setMark(config.getMark());
				}else{
					record.setMark(mark);
				}
				int effect = integralMapper.addIntegralRecord(record);
				if (effect > 0) {
					onIntegralChanged(user, record.getValue());
					return record.getValue();
				}
			}
		}
		return 0;
	}
	
	@Override
	public int userCommentAddIntegeral(User user) {
		// TODO Auto-generated method stub
		return addntegralByComment(user, null, "用户评论");
	}
}
