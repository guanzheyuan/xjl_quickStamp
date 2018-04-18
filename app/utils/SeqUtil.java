/**
 * Copyright 2010 ZTEsoft Inc. All Rights Reserved.
 *
 * This software is the proprietary information of ZTEsoft Inc.
 * Use is subject to license terms.
 * 
 * $Tracker List
 * 
 * $TaskId: $ $Date: 9:24:36 AM (May 9, 2008) $comments: create 
 * $TaskId: $ $Date: 3:56:36 PM (SEP 13, 2010) $comments: upgrade jvm to jvm1.5 
 *  
 *  
 */
package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import play.Logger;
import play.db.helper.JdbcHelper;

public class SeqUtil {

	/**
	 * 获取uuid类型的字符串
	 * 
	 * @return uuid字符串
	 */
	public static String getPrimaryKey() {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 
	 * @param tableName
	 * @param id
	 * @param colName
	 * @return
	 * @throws BaseAppException
	 */
	public Long maxSeq(String tableName, Long id, String colName) {
		return maxSeq(tableName, id, colName);
	}

	/**
	 * 
	 * @param tableName
	 * @param columnName
	 * @return
	 * @throws SQLException
	 * @throws BaseAppException
	 */
	public static synchronized Long maxValue(String tableName, String columnName) {
		return getMaxValue(tableName, columnName);
	}

	private static Long getMaxValue(String tableName, String columnName) {
		try {
			Long retVal = null;
			ResultSet rs = null;
			String sql = new StringBuffer().append("SELECT coalesce(MAX(")
					.append(columnName).append(")+1,1) FROM ")
					.append(tableName).toString();
			rs = JdbcHelper.execute(sql);
			if (rs.next()) {
				retVal = new Long(rs.getLong(1));
			}
			return retVal;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e, "");
		}
		return null;
	}
}
