/*******************************************************************************
 * Copyright, Aptima, Inc. 2011
 * Unlimited Rights granted to Government per 252.227-7014
 *******************************************************************************/
package com.aptima.netstorm.algorithms.aptima.bp;

import java.util.Comparator;

/**Class sorts the indices of the Data nodes based on the mismatch from largest to smallest.
 * 
 */
public class DataNodeAndMuComparator implements Comparator<DataNodeAndMu> {
	

	/**Method returns -1 if dataNode 1 has a larger mismatch than dataNode 2
	 * 		  returns  0 if mismatch between dataNode 1 and dataNode 2 are equal 
	 * 		  returns  1 if dataNode 1 has a smaller mismatch than dataNode 2
	 */
	public int compare(DataNodeAndMu o1, DataNodeAndMu o2) {
		if (o1.Mu > o2.Mu) {
			return -1;
		} else if (o1.Mu == o2.Mu) {
			return 0;
		} else {
			return 1;
		}
	}
}
