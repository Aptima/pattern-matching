package com.aptima.netstorm.algorithms.aptima.bp.hive.driver.old;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;

public class HiveDriverOld {
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

		String sql = "source akamaiDemo.hql";
		ResultSet res = stmt.executeQuery(sql);
	}
}