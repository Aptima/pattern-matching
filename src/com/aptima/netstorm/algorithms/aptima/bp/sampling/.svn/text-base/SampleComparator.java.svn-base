package com.aptima.netstorm.algorithms.aptima.bp.sampling;

import java.util.Comparator;

public class SampleComparator implements Comparator<Sample> {
	
	/**
	 * Sorts from smallest to largest.
	 */
	public int compare(Sample o1, Sample o2) {
		if (o1.getMismatch() < o2.getMismatch()) {
			return -1;
		} else if (o1.getMismatch() == o2.getMismatch()) {
			return 0;
		} else {
			return 1;
		}
	}
}