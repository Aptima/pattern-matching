package com.aptima.netstorm.algorithms.aptima.bp.mismatch;

public interface AttributeMismatchProcessor {
	
	/**
	 * Gets the mismatch for a single attribute.  The mismatch must be between 0.0 (for an 
	 * exact match) and 1.0 (for a complete mismatch).  Values between 0 and 1 indicate a
	 * partial match.
	 * 
	 * @param modelAttributeValue						The value of a specific attribute in the model graph
	 * @param dataAttributeValue						The value of a specific attribute in the data graph
	 * @return  										Double mismatch value between 0 and 1.
	 */
	public double getMismatch(String modelAttributeValue, String dataAttributeValue);
}