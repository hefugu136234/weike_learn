package com.lankr.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.Award;
import com.lankr.tv_cloud.model.Lottery;
import com.lankr.tv_cloud.model.LotteryRecord;
import com.lankr.tv_cloud.model.User;

public interface GameMgrMapper {

	int saveLottery(Lottery lottery);

	void saveAward(Award award);

	Lottery queryGamesByUuid(String uuid);

	Lottery selectLotteryById(int id);

	List<Lottery> searchGameListForTable(String query, int startPage,
			int pageSize);

	int updateLotteryStatus(Lottery lottery);

	int removeLottery(Lottery lottery);

	int updateLottery(Lottery lottery);

	int updateAward(Award award);

	Award queryAwardByUuid(String uuid);

	int removeAward(Award award);

	List<Award> queryAwardListByLotteryId(int id);

	public int addLotteryRecord(LotteryRecord record);

	List<Lottery> searchGameList();

	List<LotteryRecord> getLotteryRecord(String queryKey, int startPage,
			int pageSize, @Param("gameId") String gameId, @Param("isWiner") String isWiner, 
						  @Param("isHandle") String isHandle, @Param("exchangeCode") String exchangeCode);

	LotteryRecord queryLotteryRecordByUuid(String uuid);

	int updateLotteryRecordStatus(LotteryRecord record);
	
	public List<Lottery> selectLotteryByQrsence(String search);

	List<LotteryRecord> queryLotteryRecordByUserId(int userId, int lotteryId);
	
}
