package com.aptima.netstorm.algorithms.aptima.bp.mismatch;

public class DefaultAttributeMismatchProcessor implements AttributeMismatchProcessor {
	private double scaleFactorForUnknowns = 0.2;

	protected double processSimpleCases(String modelValue, String dataValue)
	{
		if(modelValue == null) {
			return 0.0; // no model = any data will match
		}
		
		if(modelValue.equals(AttributeConstraintVocab.unspecified.toString())) {
			return 0.0; // unspecified - any matching relation will match
		}

		if(dataValue == null) {
			return 1.0; // no data = max mismatch
		}
		
		if(dataValue.equals("\\N")) { // Hive specific!
			return 1.0; // no data = max mismatch
		}
		
		if(dataValue.equals(AttributeConstraintVocab.unknown.toString())) {
			return scaleFactorForUnknowns; // unknown data - minor penalty
		}
		
		if (modelValue.equals(dataValue)) { // perfect match
			return 0.0;
		}
		
		// Not a simple case.
		return -1.0;
	}
	
	public double getMismatch(String modelValue, String dataValue)
	{
		double mm = processSimpleCases(modelValue, dataValue);
		
		if (mm >= 0.0)
			return mm;
		else
			return 1.0;  // Not an exact match.
	}
}
