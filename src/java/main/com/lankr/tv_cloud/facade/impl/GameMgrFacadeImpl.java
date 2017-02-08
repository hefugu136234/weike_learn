package com.lankr.tv_cloud.facade.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestParam;

import com.lankr.orm.mybatis.mapper.ActivityMapper;
import com.lankr.orm.mybatis.mapper.GameMgrMapper;
import com.lankr.tv_cloud.cache.lru.LruCache;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.ActivityFacade;
import com.lankr.tv_cloud.facade.GameMgrFacade;
import com.lankr.tv_cloud.facade.Status;
import com.lankr.tv_cloud.facade.po.AwardResult;
import com.lankr.tv_cloud.model.Activity;
import com.lankr.tv_cloud.model.ActivityConfig;
import com.lankr.tv_cloud.model.ActivityResource;
import com.lankr.tv_cloud.model.ActivitySubject;
import com.lankr.tv_cloud.model.ActivityUser;
import com.lankr.tv_cloud.model.Award;
import com.lankr.tv_cloud.model.BaseModel;
import com.lankr.tv_cloud.model.Broadcast;
import com.lankr.tv_cloud.model.Lottery;
import com.lankr.tv_cloud.model.LotteryRecord;
import com.lankr.tv_cloud.model.LotteryReq;
import com.lankr.tv_cloud.model.Resource;
import com.lankr.tv_cloud.model.ResourceVoteOption;
import com.lankr.tv_cloud.model.ResourceVoteSubject;
import com.lankr.tv_cloud.model.Shake;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.tmp.YuanxiaoActive;
import com.lankr.tv_cloud.tmp.YuanxiaoRecord;
import com.lankr.tv_cloud.tmp.YuanxiaoRule;
import com.lankr.tv_cloud.utils.AwardUtils;
import com.lankr.tv_cloud.utils.Tools;
import com.sun.org.apache.bcel.internal.generic.INEG;

@SuppressWarnings("all")
public class GameMgrFacadeImpl extends FacadeBaseImpl implements GameMgrFacade {

	@Autowired
	private GameMgrMapper gameMgrMapper;

	@Override
	protected String namespace() {
		return GameMgrMapper.class.getName();
	}

	private LotteryCache _cache = new LotteryCache();

	@Override
	public ActionMessage addOrUpdateLottery(Lottery lottery) {
		ActionMessage action = this.updateLotteryAdvanced(lottery);
		if (!action.isSuccess()) {
			return this.addLottery(lottery);
		}
		return action;
	}

	// 更新游戏，同时更新游戏中的奖品信息
	private ActionMessage updateLotteryAdvanced(Lottery lottery) {

		if (null == lottery)

			return ActionMessage.failStatus("参数错误");
		// 获取奖品集合
		List<Award> awards = lottery.getAwards();
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		boolean resultTag;
		//删除缓存
		removeCache(lottery);
		try {
			resultTag = transaction.execute(new TransactionCallback<Boolean>() {
				@Override
				public Boolean doInTransaction(TransactionStatus arg0) {

					// 更新游戏基础数据
					int updateCount = gameMgrMapper.updateLottery(lottery);
					if (updateCount < 1)
						throw new RuntimeException();
					// 更新奖品数据
					for (int i = 0; i < awards.size(); i++) {
						Award award = awards.get(i);
						// 奖品概率设置
						// GameMgrFacadeImpl.this.awardOperation(awards, award);

						award.setLottery(lottery);
						// 根据uuid和lotteryid更新数据(uuid已从前台数据封装进Award)
						int award_eff = gameMgrMapper.updateAward(award);
						// 如果更新失败，说明该条记录为新增记录，往数据库中新增一条记录
						if (award_eff < 1) {
							award.setUuid(Tools.getUUID());
							award.setIsActive(BaseModel.ACTIVE);
							award.setStatus(BaseModel.APPROVED);
							gameMgrMapper.saveAward(award);
						}
					}
					_cache.remove(lottery.getUuid());
					return true;
				}
			});
		} catch (Exception e) {
			// e.printStackTrace();
			return ActionMessage.failStatus("更新游戏失败");
		}
		if (!resultTag) {
			return ActionMessage.failStatus("更新游戏失败");
		}
		return ActionMessage.successStatus();
	}

	protected void awardOperation(List<Award> awards, Award award) {
		AwardUtils.initAwardConditional(awards, award);
	}

	// 新增游戏,同时新增游戏奖品信息
	private ActionMessage addLottery(Lottery lottery) {

		if (null == lottery)
			return ActionMessage.failStatus("参数错误");
		lottery.setUuid(Tools.getUUID());
		lottery.setIsActive(BaseModel.ACTIVE);
		lottery.setStatus(BaseModel.UNDERLINE);
		List<Award> awards = lottery.getAwards();
		TransactionTemplate transaction = new TransactionTemplate(
				transactionManager);
		boolean resultTag;
		try {
			resultTag = transaction.execute(new TransactionCallback<Boolean>() {
				@Override
				public Boolean doInTransaction(TransactionStatus arg0) {

					// 保存游戏基础数据
					int saveCount = gameMgrMapper.saveLottery(lottery);
					Lottery result = gameMgrMapper.queryGamesByUuid(lottery
							.getUuid());
					if (null == result || saveCount < 1)
						throw new RuntimeException();
					// 保存奖品数据
					for (int i = 0; i < awards.size(); i++) {
						Award award = awards.get(i);
						// 奖品概率设置
						// GameMgrFacadeImpl.this.awardOperation(awards,
						// award);66
						award.setUuid(Tools.getUUID());
						award.setIsActive(BaseModel.ACTIVE);
						award.setStatus(BaseModel.APPROVED);
						if (null != result)
							award.setLottery(result);
						gameMgrMapper.saveAward(award);
					}
					return true;
				}
			});
		} catch (Exception e) {
			// e.printStackTrace();
			return ActionMessage.failStatus("新增游戏失败");
		}
		if (!resultTag) {
			return ActionMessage.failStatus("新增游戏失败");
		}
		return ActionMessage.successStatus();
	}

	@Override
	public Pagination<Lottery> searchGameListForTable(String query,
			int startPage, int pageSize) {
		query = filterSQLSpecialChars(query);
		String sql = "select count(id) from tmp_lottery where isActive=1 and (name like '%"
				+ query + "%' or mark like '%" + query + "%')";
		Pagination<Lottery> pagination = initPage(sql, startPage, pageSize);
		List<Lottery> lotteryList = gameMgrMapper.searchGameListForTable(query, startPage, pageSize);
		pagination.setResults(lotteryList);
		return pagination;
	}

	@Override
	public Lottery queryLotteryByUuid(String uuid) {
		Lottery lottery = null;
		try {
			lottery = gameMgrMapper.queryGamesByUuid(uuid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return lottery;
	}

	@Override
	public Award queryAwardByUuid(String uuid) {
		Award award = null;
		try {
			award = gameMgrMapper.queryAwardByUuid(uuid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return award;
	}

	@Override
	public ActionMessage removeAward(Award award) {
		try {
			int effect = gameMgrMapper.removeAward(award);
			if (effect == 1) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("删除奖品失败，请刷新页面重新尝试");
	}

	@Override
	public List<Award> queryAwardListByLotteryId(int id) {
		List<Award> awards = null;
		try {
			awards = gameMgrMapper.queryAwardListByLotteryId(id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return awards;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.GameMgrFacade#play(java.lang.String)
	 */
	@Override
	public AwardResult play(String uuid, User user) {
		if (Tools.isBlank(uuid) || !User.hasPersisted(user))
			return null;
		Lottery lottery = _cache.get(uuid, TimeUnit.MINUTES.toMillis(10), true);
		AwardResult result = generalCheck(lottery, user);
		if (!result.isOk()) {
			return result;
		}
		// 用户是否有机会
		int userPlayTimes = userPlayTimes(lottery.getId(), user.getId());
		// 用户参与次数超过游戏规定上限
		if (userPlayTimes >= lottery.getJoinTimes()) {
			result.setTimes(Lottery.TIMES_USELESS);
			//20160425 xm
			return result;
		}
		// 用户开始抽奖
		Award award = lottery.play();
		award = awardProvided(award) && userAwardFetchable(award, user) ? award
				: null;
		// 添加抽奖记录
		int effect = addLetteryRecord(lottery, award, user);
		result.setTimes(lottery.getJoinTimes() - userPlayTimes - effect);
		if (effect > 0) {
			result.setAward(award);
		}
		return result;
	}

	private AwardResult generalCheck(Lottery lottery, User user) {
		AwardResult result = new AwardResult();
		//游戏未上线
		if (lottery == null || BaseModel.UNDERLINE == lottery.getStatus()){
			result.setTimes(Lottery.TIMES_GAME_UNDERLINE);
		}
		//游戏不存在，游戏无效
		else if (BaseModel.DISABLE == lottery.getIsActive()) {
			result.setTimes(Lottery.TIMES_GAME_DISABLE);
			return result;
		}
		// 游戏未开始
		else if (lottery.isNotBegin()) {
			result.setTimes(Lottery.TIMES_NOT_START);
		}
		// 游戏已结束
		else if (lottery.isGameOver()) {
			result.setTimes(Lottery.TIMES_GAME_OVER);
		}
		return result;
	}

	private int addLetteryRecord(Lottery lottery, Award award, User user) {
		try {
			LotteryRecord record = new LotteryRecord();
			record.setUser(user);
			record.setLottery(lottery);
			record.setAward(award);
			record.setUuid(Tools.getUUID());
			record.setTicket(Tools.generateShortUuid(8));
			record.setIsActive(LotteryRecord.ACTIVE);
			record.setStatus(BaseModel.FALSE);
			return gameMgrMapper.addLotteryRecord(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 用户抽奖的次数
	private int userPlayTimes(int lotteryId, int uesrId) {
		String sql = "select count(*) from tmp_lottery_record where isActive=1 and lotteryId=? and userId=?";
		return jdbcTemplate
				.queryForInt(sql, new Object[] { lotteryId, uesrId });
	}

	// 用户是否还能抽中该奖品
	private boolean userAwardFetchable(Award award, User user) {
		if (!Award.hasPersisted(award)) {
			return true;
		}
		synchronized (award.getLock("" + user.getId())) {
			String sql = "select count(id) from tmp_lottery_record where isActive=1 and awardId=? and userId=?";
			int wins = jdbcTemplate.queryForInt(sql,
					new Object[] { award.getId(), user.getId() });
			return wins < award.getMaxWinTimes();
		}
	}

	// 奖品是否还有提供
	private boolean awardProvided(Award award) {
		if (!Award.hasPersisted(award)) {
			return true;
		}
		synchronized (award.getLock("all")) {
			String sql = "select count(id) from tmp_lottery_record where isActive=1 and awardId="
					+ award.getId();
			int used = jdbcTemplate.queryForInt(sql);
			return used < award.getNumber();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.GameMgrFacade#lotteryInit(java.lang.String,
	 * com.lankr.tv_cloud.model.User)
	 */
	@Override
	public int lotteryInit(String uuid, User user) {
		if (Tools.isBlank(uuid) || !User.hasPersisted(user))
			return Lottery.TIMES_GAME_ERROR;
		Lottery lottery = _cache.get(uuid, TimeUnit.MINUTES.toMillis(10), true);
		AwardResult result = generalCheck(lottery, user);
		if (!result.isOk())
			return result.getTimes();

		return lottery.getJoinTimes()
				- userPlayTimes(lottery.getId(), user.getId());
	}

	private void removeCache(Lottery lottery) {
		if (lottery != null) {
			_cache.remove(lottery.getUuid());
		}
	}

	// 增抽奖记录

	private class LotteryCache extends LruCache<String, Lottery> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.lankr.tv_cloud.cache.lru.LruCache#configMaxMemorySize()
		 */
		@Override
		protected int configMaxMemorySize() {
			return 100;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.lankr.tv_cloud.cache.lru.LruCache#create(java.lang.Object)
		 */
		@Override
		protected Lottery create(String key) {
			Lottery lottery = queryLotteryByUuid(key);
			if (lottery != null && lottery.apiUseable()) {
				lottery.setAwards(queryAwardListByLotteryId(lottery.getId()));
				lottery.lotteryPrepare();
				return lottery;
			}
			return super.create(key);
		}

	}

	@Override
	public List<Lottery> searchGameList() {
		List<Lottery> lotterys = null;
		try {
			lotterys = gameMgrMapper.searchGameList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return lotterys;
	}

	@Override
	public Pagination<LotteryRecord> getLotteryRecord(String queryKey,
			int startPage, int pageSize, String gameId, String isWiner,
			String isHandle, String exchangeCode) {
		queryKey = filterSQLSpecialChars(queryKey);
		String searSql = "select count(*) from tmp_lottery_record re left join tmp_lottery lo on re.lotteryId = lo.id where re.isActive=1 "
				+ " and (lo.name like '%" + queryKey + "%' or lo.pinyin like '%" + queryKey + "%'" +
				" or lo.mark like '%" + queryKey + "%'" + 
				" or lo.rules like '%" + queryKey + "%'" +
				" or re.ticket like '%" + queryKey + "%')";
		StringBuilder sBuilder = new StringBuilder(searSql);
		if(StringUtils.isNotBlank(gameId) && NumberUtils.isNumber(gameId)){
            int tmp = Integer.valueOf(gameId);
            if(tmp > 0){
                String tmpSql = " and re.lotteryId = " + tmp;
                sBuilder.append(tmpSql);
            }
        }
		if(StringUtils.isNotBlank(isWiner)){
			if("YES".equals(isWiner)){
				sBuilder.append(" and re.awardId > 0 ");
			}else if("NO".equals(isWiner)){
				sBuilder.append(" and re.awardId is NULL ");
			}
		}
		if(StringUtils.isNotBlank(isHandle) && NumberUtils.isNumber(isHandle)){
            int tmp = Integer.valueOf(isHandle);
            if(tmp >= 1 && tmp <= 2){
                String tmpSql = " and re.status = " + tmp;
                sBuilder.append(tmpSql);
            }
        }
		if(StringUtils.isNotBlank(exchangeCode)){
			//String tmpSql = " and re.ticket = " + exchangeCode;
			String tmpSql = " and re.ticket = '" + exchangeCode + "' ";
			sBuilder.append(tmpSql);
		}
		Pagination<LotteryRecord> pagination = initPage(sBuilder.toString(),
				startPage, pageSize);
		List<LotteryRecord> result = gameMgrMapper.getLotteryRecord(queryKey,
						startPage, pageSize, gameId, isWiner, isHandle, exchangeCode);
		pagination.setResults(result);
		return pagination;
	}

	@Override
	public LotteryRecord queryLotteryRecordByUuid(String uuid) {
		LotteryRecord record = null;
		try {
			record = gameMgrMapper.queryLotteryRecordByUuid(uuid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return record;
	}

	@Override
	public ActionMessage updateLotteryRecordStatus(LotteryRecord record) {
		try {
			int effect = gameMgrMapper.updateLotteryRecordStatus(record);
			if (effect == 1) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("更新状态失败");
	}
	
	@Override
	public List<Lottery> selectLotteryByQrsence(String search) {
		return gameMgrMapper.selectLotteryByQrsence(search);
	}
	
	@Override
	public Lottery getLotteryById(int id) {
		return gameMgrMapper.selectLotteryById(id);
	}

	@Override
	public List<LotteryRecord> queryRecordByUser(User user, Lottery lottery) {
		if(null == user || null == lottery)
			return null;
		List<LotteryRecord> records = null;
		try {
			records = gameMgrMapper.queryLotteryRecordByUserId(user.getId(), lottery.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return records;
	}

}
