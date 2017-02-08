package com.lankr.orm.mybatis.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lankr.tv_cloud.model.IntegralConfig;
import com.lankr.tv_cloud.model.IntegralRecord;
import com.lankr.tv_cloud.model.IntegralWeekReport;

public interface IntegralMapper {

	// 获取积分规则
	public List<IntegralConfig> getIntegralConfigs();

	public int addIntegralRecord(IntegralRecord record);

	public List<IntegralRecord> fetchUserIntegralRecords(int userId,
			String query, int start, int rows);

	public List<IntegralRecord> fetchUserIntegralConsumeRecords(int userId,
			int start, int rows);

	public List<IntegralRecord> fetchUserIntegralFetchRecords(int userId,
			int start, int rows);

	public List<IntegralRecord> fetchAllIntegralFetchRecords(String query,
			int startPage, int page_rows);

	public IntegralRecord getIntegralRecordByUuid(String uuid);

	public int updateCertificationStatus(IntegralRecord recordByUuid);

	public List<IntegralRecord> userExchangeIntegralWx(int userId,
			String startTime, int size);

	public List<IntegralRecord> userIntegralDetailWx(int userId,
			String startTime, int size);

	/**
	 * 
	 * @modifyDate 2016年5月17日 kalean
	 * @param start
	 *            统计开始时间
	 * @param end
	 *            统计结束时间
	 * 
	 */
	public List<IntegralWeekReport> userIntegralWeekReportWx(Date start,
			Date end);

	public IntegralWeekReport getUserMaxAndHisRecordByUserId(int id);

	public Integer userIntegralWeekChange(int userId, Date lastWeek);

	/**
	 * 统计用户一天中评论资源的所得的总积分
	 */
	public Integer userIntegralOfDayByComment(@Param("userId") int userId,
			@Param("start") String start, @Param("end") String end,
			@Param("action") int action);

}
