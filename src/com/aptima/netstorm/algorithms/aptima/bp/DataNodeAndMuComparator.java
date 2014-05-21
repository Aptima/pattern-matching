/*******************************************************************************
 * Copyright, Aptima, Inc. 2011
 * Unlimited Rights granted to Government per 252.227-7014
 *******************************************************************************/
package com.aptima.netstorm.algorithms.aptima.bp;

import java.util.Comparator;

public class DataNodeAndMuComparator implements Comparator<DataNodeAndMu> {
	
	/**
	 * Sorts from largest to smallest.
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
