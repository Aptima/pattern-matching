package com.aptima.netstorm.algorithms.aptima.bp.hive;

import org.apache.hadoop.hive.contrib.mr.GenericMR;

/**
 * Reduce script used to run pattern matching on a network stored in a Hive table.
 * 
 * @author jroberts
 *
 */
public class AkamaiReduceScript {
	public static void main(String[] args)
	{
		 try {
			new GenericMR().reduce(System.in, System.out, new AkamaiReducer(args));
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	}
}
