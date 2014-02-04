package com.aptima.netstorm.algorithms.aptima.bp.hive;

import org.apache.hadoop.hive.contrib.mr.GenericMR;

/**
 * Hive Streaming Map script used to run pattern matching on a network stored in a Hive table.
 * 
 * Current targets a Bitcoin table
 * 
 * @author jroberts
 *
 */
public class HiveMapScript {
	public static void main(String[] args) 
	{
		 try {
			new GenericMR().map(System.in, System.out, new BitcoinMapper(args));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
