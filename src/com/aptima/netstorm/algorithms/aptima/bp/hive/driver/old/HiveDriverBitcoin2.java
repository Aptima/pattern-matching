package com.aptima.netstorm.algorithms.aptima.bp.hive.driver.old;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveDriverBitcoin2 {
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		Connection con = DriverManager.getConnection("jdbc:hive2://xd-hive.xdata.data-tactics-corp.com:10000/default", "", "");
		Statement stmt = con.createStatement();
		
		String sql = "add file /home/bigdata/cloud/hive/AptimaHive.jar";
		stmt.execute(sql);
	
		sql = "add file /home/bigdata/cloud/hive/hive_contrib.jar";
		stmt.execute(sql);
	
		sql = "set mapred.reduce.tasks=100";
		stmt.executeQuery(sql);
		
		sql = "FROM (FROM a_s_test MAP src, dest, region, unixtime, latency USING 'java -cp AptimaHive.jar:hive_contrib.jar com.aptima.netstorm.algorithms.aptima.bp.hive.AkamaiMapScript' AS isLink, srcID, destID, mismatchVector, timeWindow DISTRIBUTE BY timeWindow SORT BY timeWindow ASC, isLink ASC) temp INSERT OVERWRITE TABLE aptima_pm_result REDUCE temp.timeWindow, temp.isLink, temp.srcID, temp.destID, temp.mismatchVector USING 'java -cp AptimaHive.jar:hive_contrib.jar com.aptima.netstorm.algorithms.aptima.bp.hive.AkamaiReduceScript' AS result_num, modelID, dataID, mismatch, dir";
		stmt.executeQuery(sql);
	}
}