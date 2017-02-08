package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.facade.po.AwardResult;
import com.lankr.tv_cloud.model.Award;
import com.lankr.tv_cloud.model.Lottery;
import com.lankr.tv_cloud.model.LotteryRecord;
import com.lankr.tv_cloud.model.User;

public interface GameMgrFacade {

	ActionMessage addOrUpdateLottery(Lottery lottery);

	Pagination<Lottery> searchGameListForTable(String query, int startPage,
			int pageSize);

	Lottery queryLotteryByUuid(String uuid);

	Award queryAwardByUuid(String uuid);

	ActionMessage removeAward(Award award);

	List<Award> queryAwardListByLotteryId(int id);

	/**
	 * @author Kalean.Xiang
	 * @createDate: 2016年4月14日
	 * @modifyDate: 2016年4月14日 抽奖
	 * @return 如果未中奖则返回null
	 * @param uuid
	 * @param user
	 *            抽奖的用户
	 */	

	public AwardResult play(String uuid, User user);
	
	
	/** 
	 *  @author Kalean.Xiang
	 *  @createDate: 2016年4月15日
	 * 	@modifyDate: 2016年4月15日
	 *  @param uuid 游戏
	 *  @param user 对应的用户
	 *  @return
	 */
	public int lotteryInit(String uuid, User user);

	List<Lottery> searchGameList();

	Pagination<LotteryRecord> getLotteryRecord(String queryKey, int startPage,
			int pageSize, String gameId, String isWiner, String isHandle, String exchangeCode);

	LotteryRecord queryLotteryRecordByUuid(String uuid);

	ActionMessage updateLotteryRecordStatus(LotteryRecord record);
	
	public List<Lottery> selectLotteryByQrsence(String search);
	
	public Lottery getLotteryById(int id);

	List<LotteryRecord> queryRecordByUser(User user, Lottery lottery);
}
