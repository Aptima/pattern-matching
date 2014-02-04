package com.aptima.netstorm.algorithms.aptima.bp.hive.driver;

import influent.idl.FL_Entity;
import influent.idl.FL_EntityMatchResult;
import influent.idl.FL_EntityTag;
import influent.idl.FL_Link;
import influent.idl.FL_LinkMatchResult;
import influent.idl.FL_LinkTag;
import influent.idl.FL_PatternSearchResult;
import influent.idl.FL_PatternSearchResults;
import influent.idl.FL_Property;
import influent.idl.FL_PropertyTag;
import influent.idl.FL_PropertyType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import com.aptima.netstorm.algorithms.aptima.bp.hive.MRBase;

public class BitcoinDriver {

	public static void main(String[] args) {

		runBitcoin();
	}

	public static FL_PatternSearchResults runBitcoin() {

		String[] hiveArgs = new String[1];

		hiveArgs[0] = "hive -f 'bitcoinDemo.hql'";

		// run Hive job
		HiveDriverProcess.main(hiveArgs);

		// Read results from table
		String driverName = "org.apache.hive.jdbc.HiveDriver";
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:hive2://xd-hive.xdata.data-tactics-corp.com:10000/default", "", "");

			Statement stmt = con.createStatement();

			String sql = "select * from aptima_pm_result";
			System.out.println("Running: " + sql);
			ResultSet res = stmt.executeQuery(sql);
			ArrayList<String[]> results = new ArrayList<String[]>();
			HashSet<String> uniqueIDs = new HashSet<String>();
			while (res.next()) {
				// five columns
				// result_num string
				// modelid int
				// dataid string
				// mismatch double
				// dir string

				String resultNum = res.getString(1);
				String modelId = "" + res.getInt(2);
				String dataId = res.getString(3);
				String mismatch = "" + res.getDouble(4);
				String dir = res.getString(5);

				String[] result = new String[] { resultNum, modelId, dataId, mismatch, dir };
				results.add(result);

				uniqueIDs.add(resultNum);
			}

			System.out.println("Read: " + results.size() + " results");

			// convert to influent
			FL_PatternSearchResults psrs = new FL_PatternSearchResults();
			psrs.setTotal((long) uniqueIDs.size());
			String previousID = "";

			//todo: aggregate links
			ArrayList<String[]> matchResults = new ArrayList<String[]>();
			for (String[] result : results) {

				String result_num = result[0];

				if (!result_num.equals(previousID) && matchResults.size() > 0) {
					// switch to new result when ID changes
					previousID = result_num;

					// build match
					ArrayList<FL_EntityMatchResult> entityMatchResults = new ArrayList<FL_EntityMatchResult>();
					ArrayList<FL_LinkMatchResult> linkMatchResults = new ArrayList<FL_LinkMatchResult>();
					FL_PatternSearchResult psr = new FL_PatternSearchResult();
					
					boolean first = true;
					for (String[] matchResult : matchResults) {

						String modelId = matchResult[1];
						String dataId = matchResult[2];
						String mismatch = matchResult[3];
						String dir = matchResult[4];

						if (first) {
							first = false;
							psr.setScore(1.0 - Double.parseDouble(mismatch));
						}

						if (dir == null || dir.equals("")) {
							FL_EntityMatchResult emr = new FL_EntityMatchResult();

							emr.setScore(psr.getScore());
							emr.setUid(modelId); // model id

							// data id
							FL_Entity entity = new FL_Entity();
							entity.setUid(dataId);
							//need to initialize the tags array otherwise, we get a null pointer exeption with we try to serialize
							entity.setTags(new ArrayList<FL_EntityTag>());
							entity.setProperties(new ArrayList<FL_Property>());
							emr.setEntity(entity);

							
							entityMatchResults.add(emr);
							
						} else if (dir.equals(MRBase.DIR_OUT)) {
							
							FL_LinkMatchResult lmr = new FL_LinkMatchResult();
							lmr.setScore(psr.getScore());
							lmr.setUid(modelId + "|" + dataId); // model id
							
							// data from/to
							FL_Link link = new FL_Link();
							link.setDirected(true);
							link.setSource(modelId); // we override this
							link.setTarget(dataId);
							link.setTags(new ArrayList<FL_LinkTag>());
			
							FL_Property property = new FL_Property();
							property.setFriendlyText("Value");
							property.setKey("Value");
							property.setValue(mismatch); // we override this
							property.setType(FL_PropertyType.DOUBLE);
							property.setTags(new ArrayList<FL_PropertyTag>());
							
							//need to check if the Properties list is initialized
							if (link.getProperties() == null) {
								//System.out.println("link properties is null");
								link.setProperties(new ArrayList<FL_Property>());
							}
							
							link.getProperties().add(property);
							
							lmr.setLink(link);
							linkMatchResults.add(lmr);
						}
					} // end for
					
					// rest local match results
					matchResults.clear();
					
					//System.out.println("Size of entityMatchResults: " + entityMatchResults.size());
					//System.out.println("Size of linkMatchResults: " + linkMatchResults.size());
					
					psr.setEntities(entityMatchResults);
					psr.setLinks(linkMatchResults);
					
					if (psrs.getResults() == null) {
						System.out.println("psrs getResults is NULL!");
						psrs.setResults(new ArrayList<FL_PatternSearchResult>());	//it looks like the results set is not initialized in the psrs
					}

					psrs.getResults().add(psr);
					
				} else {
					matchResults.add(result); // continue to locally collect until ID changes
				}
			}
			
			return psrs; // return results here!
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			return null;
		}
	}
}
