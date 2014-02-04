package com.aptima.netstorm.algorithms.aptima.bp.hive.driver.old;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;

public class HiveDriverAkamai {
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
		// Connection con = DriverManager.getConnection("jdbc:hive://localhost:10000/default", "", "");
		Connection con = DriverManager.getConnection("jdbc:hive2://xd-hive.xdata.data-tactics-corp.com:10000/default", "",
				"");
		Statement stmt = con.createStatement();

		// ResultSet res = stmt.executeQuery("create table " + tableName + " (key int, value string)");
		String sql = "show tables";
		ResultSet res = stmt.executeQuery(sql);
		while (res.next()) {
			System.out.println(res.getString(1));
		}
		// describe table
		sql = "describe " + "bitcoin";
		System.out.println("Running: " + sql);
		res = stmt.executeQuery(sql);
		while (res.next()) {
			System.out.println(res.getString(1) + "\t" + res.getString(2));
		}

		// select * query
		// sql = "select * from " + tableName;
		// System.out.println("Running: " + sql);
		// res = stmt.executeQuery(sql);
		// while (res.next()) {
		// System.out.println(String.valueOf(res.getInt(1)) + "\t" + res.getString(2));
		// }

	}
}