/** 
 *  author Kalean.Xiang
 *  createDate: 2016年3月22日
 * 	modifyDate: 2016年3月22日
 *  
 */
package com.lankr.tv_cloud.facade.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lankr.orm.mybatis.mapper.ShakeMapper;
import com.lankr.tv_cloud.facade.ActionMessage;
import com.lankr.tv_cloud.facade.LuckyTmpFacade;
import com.lankr.tv_cloud.model.Shake;
import com.lankr.tv_cloud.model.User;
import com.lankr.tv_cloud.tmp.ShakeRule;
import com.lankr.tv_cloud.tmp.ShakeRule.Config;
import com.lankr.tv_cloud.tmp.ShakeRule.Shaker;
import com.lankr.tv_cloud.tmp.YuanxiaoActive;
import com.lankr.tv_cloud.tmp.YuanxiaoRecord;
import com.lankr.tv_cloud.tmp.YuanxiaoRule;
import com.lankr.tv_cloud.utils.Tools;

/**
 * @author Kalean.Xiang
 *
 */
public class LuckyTmpFacadeImpl extends FacadeBaseImpl implements
		LuckyTmpFacade {
	
	@Autowired
	private ShakeMapper shakeMapper;

	@Override
	public synchronized YuanxiaoActive yuanxiaoPlay(User user) {
		if (user == null || !user.hasPersisted())
			return null;
		YuanxiaoActive active = new YuanxiaoActive();
		active.setCode(YuanxiaoActive.CODE_OK);
		// 判断抽奖时间是否正确
		Date current = Tools.getCurrentDate();
		if (current.before(YuanxiaoRule.getStartDate())) {
			active.setCode(YuanxiaoActive.CODE_NOT_START);
		} else if (current.after(YuanxiaoRule.getEndDate())) {
			active.setCode(YuanxiaoActive.CODE_OVER);
		}
		if (!active.isOk()) {
			return active;
		}
		int times = userTimes(user.getId(), YuanxiaoRule.dateStart(new Date()),
				YuanxiaoRule.dateEnd(new Date()));
		if (times >= YuanxiaoRule.USER_MAX_TIMES_DAY) {
			active.setCode(YuanxiaoActive.CODE_DISABLE);
			return active;
		}
		// 获取随机数金额
		int seed = 100;
		// 判断用户今天是否有中奖记录,保证只有一次中奖机会
		if (winTimes(user.getId(), YuanxiaoRule.dateStart(new Date()),
				YuanxiaoRule.dateEnd(new Date())) > 0) {
			seed = YuanxiaoRule.WIN_START_NUMBER;
		}
		float money = YuanxiaoRule.randomMoney(seed);
		if (money != YuanxiaoRule.MONEY_0) {
			int max = YuanxiaoRule.moneyMaxTimesPerDay(money);
			if (moneyUseable(money, max, YuanxiaoRule.dateStart(new Date()),
					YuanxiaoRule.dateEnd(new Date()))) {
				active.setMoney(money);
			}
		}
		if (savePlay(user.getId(), active.getMoney(), null)) {
			// 剩余次数
			active.setTimes(YuanxiaoRule.USER_MAX_TIMES_DAY - times - 1);
		}
		return active;
	}

	@Override
	public List<YuanxiaoRecord> yuanxiaoUserRecord(User user) {
		if (user == null)
			return null;
		String sql = "select * from tmp_yuanxiao where isActive=1 and userId=? and createDate >= ? and createDate <= ? order by id desc";
		List<YuanxiaoRecord> records = searchSql(sql,
				new Object[] { user.getId(), YuanxiaoRule.START_DATE,
						YuanxiaoRule.END_DATE }, YuanxiaoRecord.class);
		return records;
	}
	
	@Override
	public List<YuanxiaoRecord> gameYaoRecord(User user) {
		// TODO Auto-generated method stub
		if (user == null)
			return null;
		ShakeRule.Config config=ShakeRule.configs[0];
		String sql = "select * from tmp_yuanxiao where isActive=1 and userId=? and createDate >= ? and createDate <= ? order by id desc";
		List<YuanxiaoRecord> records = searchSql(sql,
				new Object[] { user.getId(), config.dateStart,
				config.endDate }, YuanxiaoRecord.class);
		return records;
	}

	@Override
	public int yuanxiaoTimesInit(User user) {
		int times = userTimes(user.getId(), YuanxiaoRule.dateStart(new Date()),
				YuanxiaoRule.dateEnd(new Date()));
		return Math.max(0, YuanxiaoRule.USER_MAX_TIMES_DAY - times);
	}

	// 获取用户抽奖的次数
	private int userTimes(int userId, String start, String end) {
		String sql = "select count(id) from tmp_yuanxiao where isActive = 1 and userId=? and createDate > ? and createDate <= ?";
		return jdbcTemplate.queryForInt(sql,
				new Object[] { userId, start, end });
	}

	// 插入抽奖记录
	private boolean savePlay(int userId, float money, String mark) {
		try {
			String sql = "insert into tmp_yuanxiao (uuid,createDate,modifyDate,userId,money,status,isActive,mark) values (?,NOW(),NOW(),?,?,1,1,?)";
			int effect = jdbcTemplate.update(sql,
					new Object[] { Tools.getUUID(), userId, money, mark });
			return effect > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 判断此金额的奖励是否超过上限
	private boolean moneyUseable(float money, int max, String start, String end) {
		String sql = "select count(id) from tmp_yuanxiao where isActive = 1 and money=? and createDate > ? and createDate <= ?";
		int count = jdbcTemplate.queryForInt(sql, new Object[] { money, start,
				end });
		return count < max;
	}

	// 当天的中奖次数
	private int winTimes(int userId, String start, String end) {
		String sql = "select count(*) from tmp_yuanxiao where isActive = 1 and userId=? and money > 0 and createDate > ? and createDate < ?";
		int count = jdbcTemplate.queryForInt(sql, new Object[] { userId, start,
				end });
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lankr.tv_cloud.facade.impl.FacadeBaseImpl#namespace()
	 */
	@Override
	protected String namespace() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lankr.tv_cloud.facade.LuckyTmpFacade#shakePlay(com.lankr.tv_cloud
	 * .model.User)
	 */
	@Override
	public synchronized Shaker shakePlay(User user) {
		Shaker shaker = ShakeRule.shake(15);
		if (shaker.isOk()) {
			Config c = shaker.config;
			int times = userTimes(user.getId(), c.dateStart, c.endDate);
			if (times >= c.max_times) {
				shaker.code = YuanxiaoActive.CODE_DISABLE;
				return shaker;
			}
			if (shaker.money > 0) {
				if (c != null) {
					// 判断是否达到上限
					int max = c.getMaxTimes(shaker.money);
					// 判断奖池是否达到上限
					if (moneyUseable(shaker.money, max, c.dateStart, c.endDate)) {
						int wins = winTimes(user.getId(), c.dateStart,
								c.endDate);
						if (wins >= c.getMaxWins(shaker.money)) {
							shaker.money = 0;
						}
					} else {
						// 奖池达到上限
						shaker.money = 0;
					}
				}
			}
			savePlay(user.getId(), shaker.money, c.mark);
			shaker.times = c.max_times - times - 1;
		}
		return shaker;
	}

	//查询兑换记录（后台）
	@Override
	public Pagination<Shake> getExchangeRecord(String queryKey, int startPage,
			int pageSize, String isWiner, String isHandle) {
		queryKey = filterSQLSpecialChars(queryKey);
		String searSql = "select count(*) from tmp_yuanxiao where isActive=1 "
				+ " and (mark like '%" + queryKey + "%')";
		StringBuilder sBuilder = new StringBuilder(searSql);
		if(StringUtils.isNoneEmpty(isWiner)){
			if(isWiner.equals("yes")){
				sBuilder.append(" and money > 0 ");
			}else if(isWiner.equals("no")){
				sBuilder.append(" and money = 0 ");
			}
		}
		if(NumberUtils.isNumber(isHandle)){
            int status = Integer.valueOf(isHandle);
            if(status >= 1 && status <= 2){
                String tmpSql = " and status = " + status;
                sBuilder.append(tmpSql);
            }
        }
		Pagination<Shake> pagination = initPage(sBuilder.toString(),
				startPage, pageSize);
		List<Shake> result = shakeMapper.searchExchangeRecordForDatatable(queryKey,
						startPage, pageSize, isWiner, isHandle);
		pagination.setResults(result);
		return pagination;
	}

	@Override
	public Shake selectShakeByUuid(String uuid) {
		return shakeMapper.selectShakeByUuid(uuid);
	}

	@Override
	public ActionMessage updateShakeStatus(Shake shake) {
		try {
			int effect = shakeMapper.updateShakeStatus(shake);
			if (effect == 1) {
				return ActionMessage.successStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionMessage.failStatus("更新状态失败");
	}

}
