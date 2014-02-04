package com.aptima.netstorm.algorithms.aptima.bp.hive;

import org.apache.hadoop.hive.contrib.mr.GenericMR;

/**
 * Reduce script used to run pattern matching on a network stored in a Hive table.
 * 
 * @author jroberts
 *
 */
public class HiveReduceScript {
	public static void main(String[] args)
	{
		// Read in nodes first, then edges within each time window
		// Reducer may receive data for multiple time windows
		// Tab-delimited data is of the form "isNode, sourceID, destID, mismatchVector, timeWindow"
		
		// Read in model nodes/relations from pattern input file (or grab from MapScript method).
		
		// Read in data node information, then filter data edges based on whether both nodes exist
		// Read data information into a mismatchValue object.  Store unique ids in place of GD ids.
		
		// Run BeliefPropagation
		
		// Fix up DFSSampler to no longer require GD ids
		
		// Print tab-delimited results.
		 try {
			new GenericMR().reduce(System.in, System.out, new BitcoinReducer(args));
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	}
}
