package com.lankr.tv_cloud.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lankr.tv_cloud.model.Hospital;

/**
 * @Description:  JDBC 操作数据库(PreparedStatement)
 */
public class JDBCOperationByPreparedStatement {
	// 定义数据库链接参数
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://127.0.0.1:3306/test?characterEncoding=UTF-8";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "111111";

	
	/**
	 * @Description: 查询数据
	 */
	@SuppressWarnings("unused")
	private static void selectRecordsFromTable() throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String selectSQL = "SELECT USER_ID, USERNAME FROM DBUSER WHERE USER_ID = ?";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, 1001);
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String userid = rs.getString("USER_ID");
				String username = rs.getString("USERNAME");
				System.out.println("userid : " + userid);
				System.out.println("username : " + username);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	
	/**
	 * @Description: 插入数据
	 */
	public static void insertRecordIntoTable(Hospital hospital)
			throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String tableName = "base_hospital";
		StringBuffer insertSql = new StringBuffer();
		insertSql.append("INSERT INTO `" + tableName + "`");
		insertSql
				.append("(createDate, modifyDate, uuid, name, grade, mobile, address, provinceId, cityId, isActive)");
		insertSql.append(" VALUES ");
		insertSql.append("(NOW(),NOW(),?,?,?,?,?,?,?,?)");

		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(insertSql
					.toString());
			preparedStatement.setString(1, hospital.getUuid());
			preparedStatement.setString(2, hospital.getName());
			preparedStatement.setString(3, hospital.getGrade());
			preparedStatement.setString(4, hospital.getMobile());
			preparedStatement.setString(5, hospital.getAddress());
			preparedStatement.setInt(6, hospital.getProvince().getId());
			preparedStatement.setInt(7, hospital.getCity().getId());
			preparedStatement.setInt(8, hospital.getIsActive());

			preparedStatement.executeUpdate();
			System.out.println("Record is inserted into DBUSER table!");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}
	
	
	/**
	 * @Description: 更新数据
	 */
	@SuppressWarnings("unused")
	private static void updateRecordToTable() throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String updateTableSQL = "UPDATE DBUSER SET USERNAME = ? "
				                  + " WHERE USER_ID = ?";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(updateTableSQL);
			preparedStatement.setString(1, "mkyong_new_value");
			preparedStatement.setInt(2, 1001);
			
			preparedStatement.executeUpdate();
			System.out.println("Record is updated to DBUSER table!");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}
	
	
	/**
	 * @Description: 建表
	 */
	private static void createTable() throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String createTableSQL = "CREATE TABLE DBUSER1("
				+ "USER_ID NUMBER(5) NOT NULL, "
				+ "USERNAME VARCHAR(20) NOT NULL, "
				+ "CREATED_BY VARCHAR(20) NOT NULL, "
				+ "CREATED_DATE DATE NOT NULL, " + "PRIMARY KEY (USER_ID) "
				+ ")";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(createTableSQL);
			System.out.println(createTableSQL);
			
			preparedStatement.executeUpdate();
			System.out.println("Table \"dbuser\" is created!");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}
	
	
	/**
	 * @Description: 删除数据
	 */
	@SuppressWarnings("unused")
	private static void deleteRecordFromTable() throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String deleteSQL = "DELETE DBUSER WHERE USER_ID = ?";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, 1001);

			preparedStatement.executeUpdate();
			System.out.println("Record is deleted!");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	
	/**
	 * @Description: 获取数据库链接
	 */
	private static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
					DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}

	
	private static java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}
}