/** 
 *  author Kalean.Xiang
 *  createDate: 2016年3月22日
 * 	modifyDate: 2016年3月22日
 *  
 */
package com.lankr.tv_cloud.facade;

import java.util.List;

import com.lankr.tv_cloud.facade.impl.Pagination;
import com.lankr.tv_cloud.model.Shake;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.tmp.ShakeRule.Shaker;
import com.lankr.tv_cloud.tmp.YuanxiaoActive;
import com.lankr.tv_cloud.tmp.YuanxiaoRecord;

/**
 * @author Kalean.Xiang
 *
 */
public interface LuckyTmpFacade {

	// 元宵活动抽奖
	public YuanxiaoActive yuanxiaoPlay(User user);

	// 元宵活动次数用户初始
	public int yuanxiaoTimesInit(User user);

	// 元宵活动用户获奖记录
	public List<YuanxiaoRecord> yuanxiaoUserRecord(User user);

	public Shaker shakePlay(User user);
	
	//摇一摇的抽奖记录
	public List<YuanxiaoRecord> gameYaoRecord(User user);

	//后台查询摇一摇兑换记录（抽盒子的游戏）
	public Pagination<Shake> getExchangeRecord(String queryKey, int startPage,
			int pageSize, String isWiner, String isHandle);

	public Shake selectShakeByUuid(String uuid);

	public ActionMessage updateShakeStatus(Shake shake);

}
