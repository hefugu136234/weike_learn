package com.lankr.tv_cloud.facade.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.lankr.tv_cloud.facade.AccessLogFacade;
import com.lankr.tv_cloud.model.AccessLog;
import com.lankr.tv_cloud.utils.Tools;

public class AccessLogFacadeImp extends FacadeBaseImpl implements
		AccessLogFacade {

	private static final String TABLE_PREFIX = "access_log";

	private ExecutorService pool = Executors.newFixedThreadPool(20);

	public synchronized boolean insretPreAction() {
		String tableName = TABLE_PREFIX + "_" + getNowTime();
		// 检查要插入的表存不存在,不存在新建
		boolean flag = true;
		if (checkTable(tableName))
			return flag;
		flag = createTable(tableName);
		return flag;
	}

	/**
	 * 创建新表 每个月的创建新表
	 * 
	 * @return
	 */
	public boolean createTable(String tableName) {
		boolean flag = false;
		StringBuffer sqlTableBuffer = new StringBuffer();
		sqlTableBuffer.append("CREATE TABLE `");
		sqlTableBuffer.append(tableName + "` (");
		sqlTableBuffer.append("`id` INT(11) NOT NULL AUTO_INCREMENT,");
		sqlTableBuffer.append("`accessDate` DATETIME NULL DEFAULT NULL,");
		sqlTableBuffer.append("`userId` INT(11) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`ip` VARCHAR(100) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`access_uri` VARCHAR(150) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`plateform` VARCHAR(50) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`user_agent` VARCHAR(200) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`plam_version` VARCHAR(20) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`isActive` INT(11) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`code` INT(11) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`mark` VARCHAR(250) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`resource` VARCHAR(100) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`table_name` VARCHAR(100) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`table_id` INT(11) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`operate` VARCHAR(100) NULL DEFAULT NULL,");
		sqlTableBuffer
				.append("`status` VARCHAR(50) NULL DEFAULT NULL COMMENT '操作的状态，成功，还是失败',");
		sqlTableBuffer
				.append("`title` VARCHAR(200) NULL DEFAULT NULL COMMENT '资源名称',");
		sqlTableBuffer
				.append("`responseTime` INT(11) NULL DEFAULT NULL COMMENT '此次的响应时间',");
		sqlTableBuffer
				.append("`field_a` VARCHAR(200) NULL DEFAULT NULL COMMENT '预留字段',");
		sqlTableBuffer.append("`field_b` VARCHAR(100) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`field_c` VARCHAR(100) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`field_d` INT(11) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`field_e` INT(11) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`field_f` INT(11) NULL DEFAULT NULL,");
		sqlTableBuffer.append("`openId` VARCHAR(60) NULL DEFAULT NULL,");
		sqlTableBuffer.append("PRIMARY KEY (`id`)");
		sqlTableBuffer.append(" )");
		sqlTableBuffer.append(" COMMENT='日志记录表' ");
		sqlTableBuffer.append(" COLLATE='utf8_general_ci' ");
		sqlTableBuffer.append(" ENGINE=InnoDB; ");
		try {
			accessLogJdbc.update(sqlTableBuffer.toString());
			flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 检查某一个张表存不存在
	 * 
	 * @return
	 */
	private static Map<String, Boolean> flag_cache = new HashMap<String, Boolean>();

	private boolean checkTable(String tableName) {
		Boolean flag = flag_cache.get(tableName);
		if (flag != null && flag) {
			return true;
		}
		flag = false;
		Connection conn = null;
		try {
			conn = accessLogJdbc.getDataSource().getConnection();
			String[] types = { "TABLE" };
			DatabaseMetaData databaseMetaData = conn.getMetaData();
			ResultSet tabs = databaseMetaData.getTables(null, null, tableName,
					types);
			if (tabs.next()) {
				flag = true;
				flag_cache.put(tableName, flag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceUtils.releaseConnection(conn,
					accessLogJdbc.getDataSource());
		}
		return flag;
	}

	// 存储日志数据
	public boolean saveLog(AccessLog accessLog) {
		boolean flag = false;
		String tableName = TABLE_PREFIX + "_" + getNowTime();
		StringBuffer insertSql = new StringBuffer();
		insertSql.append("INSERT INTO `" + tableName);
		insertSql.append("`(");
		insertSql
				.append("accessDate,userId,ip,access_uri,plateform,user_agent,plam_version,"
						+ "isActive,code,mark,resource,table_name,table_id,operate,status,title,"
						+ "responseTime,field_a,field_b,field_c,field_d,field_e,field_f");
		insertSql.append(")");
		insertSql.append(" VALUES ");
		insertSql.append("(NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		int num = 0;
		try {
			num = accessLogJdbc.update(insertSql.toString(),
					new PreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement arg0)
								throws SQLException {
							// TODO Auto-generated method stub
							arg0.setInt(1, accessLog.getUserId());
							String ip = Tools.nullValueFilter(accessLog.getIp());
							ip = ip.length() > 100 ? ip.substring(0, 100) : ip;
							arg0.setString(2, ip);
							String access_url = Tools.nullValueFilter(accessLog
									.getAccess_uri());
							access_url = access_url.length() > 150 ? access_url
									.substring(0, 150) : access_url;
							arg0.setString(3, access_url);
							arg0.setString(4, accessLog.getPlateform());
							arg0.setString(
									5,
									accessLog.getUser_agent().length() > 180 ? accessLog
											.getUser_agent().substring(0, 180)
											: accessLog.getUser_agent());
							arg0.setString(6, accessLog.getPlam_version());
							arg0.setInt(7, accessLog.getIsActive());
							arg0.setInt(8, accessLog.getCode());
							arg0.setString(9, accessLog.getMark());
							arg0.setString(10, accessLog.getResource());
							arg0.setString(11, accessLog.getTable_name());
							arg0.setInt(12, accessLog.getTable_id());
							arg0.setString(13, accessLog.getOperate());
							arg0.setString(14, accessLog.getStatus());
							arg0.setString(15, accessLog.getTitle());
							arg0.setInt(16, accessLog.getResponseTime());
							arg0.setString(17, accessLog.getFieldA());
							arg0.setString(18, accessLog.getFieldB());
							arg0.setString(19, accessLog.getFieldC());
							arg0.setInt(20, accessLog.getFieldD());
							arg0.setInt(21, accessLog.getFieldE());
							arg0.setInt(22, accessLog.getFieldF());
						}

					});
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (num == 1)
			flag = true;
		return flag;
	}

	/**
	 * 2016-08-01 号执行
	 * @param accessLog
	 * @return
	 */
	public boolean saveNewLog(AccessLog accessLog) {
		boolean flag = false;
		String tableName = TABLE_PREFIX + "_" + getNowTime();
		StringBuffer insertSql = new StringBuffer();
		insertSql.append("INSERT INTO `" + tableName);
		insertSql.append("`(");
		insertSql
				.append("accessDate,userId,ip,access_uri,plateform,user_agent,plam_version,"
						+ "isActive,code,mark,resource,table_name,table_id,operate,status,title,"
						+ "responseTime,field_a,field_b,field_c,field_d,field_e,field_f,openId");
		insertSql.append(")");
		insertSql.append(" VALUES ");
		insertSql.append("(NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		int num = 0;
		try {
			num = accessLogJdbc.update(insertSql.toString(),
					new PreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement arg0)
								throws SQLException {
							// TODO Auto-generated method stub
							arg0.setInt(1, accessLog.getUserId());
							String ip = Tools.nullValueFilter(accessLog.getIp());
							ip = ip.length() > 100 ? ip.substring(0, 100) : ip;
							arg0.setString(2, ip);
							String access_url = Tools.nullValueFilter(accessLog
									.getAccess_uri());
							access_url = access_url.length() > 150 ? access_url
									.substring(0, 150) : access_url;
							arg0.setString(3, access_url);
							arg0.setString(4, accessLog.getPlateform());
							arg0.setString(
									5,
									accessLog.getUser_agent().length() > 180 ? accessLog
											.getUser_agent().substring(0, 180)
											: accessLog.getUser_agent());
							arg0.setString(6, accessLog.getPlam_version());
							arg0.setInt(7, accessLog.getIsActive());
							arg0.setInt(8, accessLog.getCode());
							arg0.setString(9, accessLog.getMark());
							arg0.setString(10, accessLog.getResource());
							arg0.setString(11, accessLog.getTable_name());
							arg0.setInt(12, accessLog.getTable_id());
							arg0.setString(13, accessLog.getOperate());
							arg0.setString(14, accessLog.getStatus());
							arg0.setString(15, accessLog.getTitle());
							arg0.setInt(16, accessLog.getResponseTime());
							arg0.setString(17, accessLog.getFieldA());
							arg0.setString(18, accessLog.getFieldB());
							arg0.setString(19, accessLog.getFieldC());
							arg0.setInt(20, accessLog.getFieldD());
							arg0.setInt(21, accessLog.getFieldE());
							arg0.setInt(22, accessLog.getFieldF());
							arg0.setString(23, accessLog.getOpenId());
						}

					});
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (num == 1)
			flag = true;
		return flag;
	}

	private static SimpleDateFormat format = new SimpleDateFormat("yy_MM");

	/**
	 * 得到当前的年月 格式：15-07
	 * 
	 * @return
	 */
	public String getNowTime() {
		// SimpleDateFormat format = new SimpleDateFormat("yy_MM");
		Date date = new Date();
		String val = format.format(date);
		return val;
	}

	@Override
	public void addAccessLog(final AccessLog accessLog) {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (insretPreAction()) {
					try {
						if(getNowTime().compareTo("16_08")<0){
							saveLog(accessLog);
						}else{
							saveNewLog(accessLog);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	public static void main(String[] args) {
		AccessLogFacadeImp imp = new AccessLogFacadeImp();
		// System.out.println(imp.getNowTime());
		System.out.println("16_07".compareTo(imp.getNowTime()));
	}

	@Override
	protected String namespace() {
		// TODO Auto-generated method stub
		return null;
	}

}
