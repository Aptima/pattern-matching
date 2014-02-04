package com.aptima.netstorm.algorithms.aptima.bp.hive;

import org.apache.hadoop.hive.contrib.mr.GenericMR;

/**
 * Hive Streaming Map script used to run pattern matching on a network stored in a Hive table.
 * 
 * Currently targets an Akamai table
 * 
 * @author jroberts
 *
 */
public class AkamaiMapScript {
	public static void main(String[] args) 
	{
		 try {
			new GenericMR().map(System.in, System.out, new AkamaiMapper(args));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
