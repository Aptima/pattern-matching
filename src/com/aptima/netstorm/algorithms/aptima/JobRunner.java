package com.aptima.netstorm.algorithms.aptima;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.NullArgumentException;

import com.aptima.netstorm.algorithms.aptima.bp.ConvertFromGraphSON;
import com.aptima.netstorm.algorithms.aptima.bp.ModelGraph;
import com.aptima.netstorm.algorithms.aptima.bp.hive.driver.OutputRedirector;
import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelGraph;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReader;


/**
 * A class to run map/reduce jobs using Apache Hive. 
 * Jobs are run by dynamically building an HQL script file and calling into Hive.
 * A GraphSON file is used to represent the data model.
 * 
 * @author ckapopoulos
 *
 */

public class JobRunner {

	/**
	 * The name used for the dynamically created HQL file.
	 */
	private String HQL_FILENAME = "template.hql";
	
	/**
	 * The path location that the HQL file will be stored at.
	 */
	private String HQL_PATH = System.getProperty("user.dir") + "/" + HQL_FILENAME; 
	
	/**
	 * This method will take in several parameters needed to successfully run a job.
	 * The mapper class must implement both the org.apache.hadoop.hive.contrib.mr.Mapper and com.aptima.netstorm.algorithms.aptima.bp.ModelGraph interfaces.
	 * The reducer class must implement both the org.apache.hadoop.hive.contrib.mr.Reducer and com.aptima.netstorm.algorithms.aptima.bp.ModelGraph interfaces
	 * com.aptima.netstorm.algorithms.aptima.bp.ModelGraph is used to pass in the AttributedModelGraph data model which was converted from the GraphSON file.
	 * 
	 * @param mapper fully qualified class name of the Mapper.
	 * @param reducer fully qualified class name of the Reducer.
	 * @param inputTableName name of the input table to use.
	 * @param outputTableName name of the output table to use.
	 * @param patternFile GraphSON file containing model data.
	 */
	public void run(String mapper, String reducer, String inputTableName, String outputTableName, String patternFile) {		
		try {
			// Do classes exist?
			Class.forName(mapper);
			Class.forName(reducer);
			
			// Does input table exist?	
	        if (!tableExist(inputTableName))
	        	throw new IllegalArgumentException(String.format("Did not detect input table [%s] in database!", inputTableName));
	        
	        if (tableNameValid(outputTableName)) {
	        	// Create table if it doesn't exist.
	        	if (!createTable(outputTableName))
	        		throw new IllegalArgumentException(String.format("Output table [%s] could not be created!", outputTableName));
		    } else {
		    	throw new IllegalArgumentException(String.format("Output table name [%s] is invalid!", outputTableName));
		    }
	        
			// Does pattern file exist? Is it valid GraphSON?
	        if (patternFile == null)
	        	throw new IllegalArgumentException("Invalid patternFile!");
			File filePattern = new File(patternFile);
			if (!filePattern.exists())
				throw new IllegalArgumentException(String.format("[%s] does not exist!", patternFile));
			else {
				Graph graph = new TinkerGraph();
				FileInputStream fileInputStream = new FileInputStream(filePattern);
				GraphSONReader.inputGraph(graph, fileInputStream);	
			}
			
			// Ready to create HQL file.
			String hql = createBitcoinHQL(mapper, reducer, inputTableName, outputTableName, patternFile);
			FileWriter fileWriter = new FileWriter(HQL_PATH, false);
			fileWriter.write(hql);
			fileWriter.flush();
			fileWriter.close();
			
			// Call hive.
			System.out.println(String.format("Calling hive using %s.", HQL_PATH));
			ProcessBuilder hiveProcessBuilder = new ProcessBuilder("hive", "-f", HQL_PATH);
	        Process hiveProcess = hiveProcessBuilder.start();		
	        
	        OutputRedirector outRedirect = new OutputRedirector(hiveProcess.getInputStream(), "HIVE_OUTPUT");
	        OutputRedirector outToConsole = new OutputRedirector(hiveProcess.getErrorStream(), "HIVE_LOG");
	
	        outRedirect.start();	        
	        outToConsole.start();	       
	              
	        hiveProcess.waitFor();
	        System.out.println(String.format("Hive job call completed."));
	        hiveProcess.destroy();	        
	        
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A method to create an HQL script file. This file will be used with Apache Hive to run the job.
	 * The file will be dynamically created and then stored locally. If the file already exists it will be overwritten.
	 * 
	 * @param mapper fully qualified class name of the Mapper.
	 * @param reducer fully qualified class name of the Reducer.
	 * @param inputTableName name of the input table to use.
	 * @param outputTableName name of the output table to use.
	 * @param patternFile GraphSON file containing model data.
	 * @return
	 */
	private String createBitcoinHQL(String mapper, String reducer, String inputTableName, String outputTableName, String patternFile) {
		String hql =
		"add file target/PM-0.0.1-SNAPSHOT.jar;\n" +
		"add file target/lib/hive-contrib-0.10.0-cdh4.3.0.jar;\n" +
		"add file target/lib/commons-cli-1.3-SNAPSHOT.jar;\n" +
		"add file target/lib/blueprints-core-2.5.0.jar;\n" +
		"add file " + patternFile + ";\n" +

		"set mapred.reduce.tasks=24;\n" +
		"set hive.exec.script.allow.partial.consumption=true;\n" +

		"FROM (\n" +
		"	FROM " + inputTableName + "\n" +
		"	MAP id, source_edge_id, dest_edge_id, dtg, amount, in_degree, out_degree, node, incoming_amount, outgoing_amount\n" +
		"	USING 'java -cp PM-0.0.1-SNAPSHOT.jar:hive_contrib.jar:commons-cli-1.3-SNAPSHOT.jar:blueprints-core-2.5.0.jar com.aptima.netstorm.algorithms.aptima.bp.hive.HiveMapScript " + mapper + " " + patternFile + "'\n" +
		"	AS isLink, srcID, destID, mismatchVector, timeWindow, amount\n" +
		"	DISTRIBUTE BY timeWindow\n" +
		"	SORT BY timeWindow ASC, isLink ASC) temp\n" +
		"INSERT OVERWRITE TABLE " + outputTableName + "\n" +
		"	REDUCE temp.timeWindow, temp.isLink, temp.srcID, temp.destID, temp.mismatchVector, temp.amount\n" +
		"	USING 'java -cp PM-0.0.1-SNAPSHOT.jar:hive_contrib.jar:commons-cli-1.3-SNAPSHOT.jar:blueprints-core-2.5.0.jar com.aptima.netstorm.algorithms.aptima.bp.hive.HiveReduceScript " + reducer + " " + patternFile + "'\n" +
		"	AS result_num, modelID, dataID, mismatch, dir;\n";
		return hql;
	}
	
	/**
	 * A method to check the existence of a table using Apache Hive.
	 * 
	 * @param tableName name of the table to check.
	 * @return true if table exists. Otherwise false is returned.
	 */
	private boolean tableExist(String tableName) {		
		try {
			ProcessBuilder hiveProcessBuilder = new ProcessBuilder("hive", "-e", "show tables"); // "hive -f 'bitcoinDemo.hql'";
	        Process hiveProcess = hiveProcessBuilder.start();		
	        
	        OutputRedirector outRedirect = new OutputRedirector(hiveProcess.getInputStream(), "HIVE_OUTPUT", false, tableName);
	        OutputRedirector outToConsole = new OutputRedirector(hiveProcess.getErrorStream(), "HIVE_LOG", false);
	
	        outRedirect.start();	        
	        outToConsole.start();
	        
	        hiveProcess.waitFor();
	        hiveProcess.destroy();
	        
	        return outRedirect.isMatchDeteced();
        
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			return false;
			//e.printStackTrace();
		}	
	}

	/**
	 * A method to create a table using Apache Hive.
	 * 
	 * @param tableName name of the table to create.
	 * @return true if table was created successfully. Otherwise false is returned.
	 */
	private boolean createTable(String tableName) {
		try {
			String hql =
					"CREATE TABLE IF NOT EXISTS " + tableName + " " +
					"(result_num STRING, modelID STRING, dataID STRING, mismatch STRING, dir STRING)";
			ProcessBuilder hiveProcessBuilder = new ProcessBuilder("hive", "-e", hql);
	        Process hiveProcess = hiveProcessBuilder.start();		
	        
	        OutputRedirector outRedirect = new OutputRedirector(hiveProcess.getInputStream(), "HIVE_OUTPUT");
	        OutputRedirector outToConsole = new OutputRedirector(hiveProcess.getErrorStream(), "HIVE_LOG");
	
	        outRedirect.start();	        
	        outToConsole.start();
	        
	        hiveProcess.waitFor();
	        hiveProcess.destroy();
	        
	        return true;
        
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			return false;
			//e.printStackTrace();
		}
	}
	
	/**
	 * A method to validate the name used for a table.
	 * 
	 * @param tableName name of table to validate.
	 * @return true if table name is valid. Otherwise false is returned
	 */
	private boolean tableNameValid(String tableName) {
		/*
		 *  /^[a-zA-Z_][\w]{0,127}$/
		 *  ^ assert position at start of the string
		 *  [a-zA-Z_] match a single character present in the list below
		 *    a-z a single character in the range between a and z (case sensitive)
		 *    A-Z a single character in the range between A and Z (case sensitive)
		 *    _ the literal character _
		 *  [\w]{0,127} match a single character present in the list below
		 *    Quantifier: Between 0 and 127 times, as many times as possible, giving back as needed [greedy]
		 *    \w match any word character [a-zA-Z0-9_]
		 *  $ assert position at end of the string		
		 */		
	      String pattern = "^[a-zA-Z_][\\w]{0,127}$"; 

	      Pattern r = Pattern.compile(pattern);

	      Matcher m = r.matcher(tableName);
	      
	      if (m.find( )) {
	         return true;
	      } else {
	        return false;
	      }
	}

	public static void main(String[] args) {
		JobRunner jr = new JobRunner();
		jr.run("com.aptima.netstorm.algorithms.aptima.bp.hive.BitcoinMapper", "com.aptima.netstorm.algorithms.aptima.bp.hive.BitcoinReducer", "bitcoin_inout_edges", "aptima_pm_result", "testQuery.txt");
	}
}
