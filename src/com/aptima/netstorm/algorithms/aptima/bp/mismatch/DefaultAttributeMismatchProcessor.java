package com.aptima.netstorm.algorithms.aptima.bp.mismatch;

public class DefaultAttributeMismatchProcessor implements AttributeMismatchProcessor {
	private double scaleFactorForUnknowns = 0.2;

	/**Method computes the mismatch between the attribute value of a model and data element
	 * in the most simplest of cases (namely, if neither have info in them, OR, the constraint to be used
	 * is unknown
	 * 
	 * @param modelValue			String that holds the attribute of the model element
	 * @param dataValue				String that holds the attribute of the data element
	 * @return						double = 0 IF model has no info,  model constraint is unspecified, or it equals data element
	 * 								double = 1 IF data has no info, data == "\\N" a hive specific character, 
	 *								double = preset scale factor IF data constraint is unspecified
	 *								double = -1 if it is not one of these simple cases
	 */
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
	
	/**Method gets the mismatch between model and data element if it falls under the category of a "simple case"
	 * 
	 * @param modelValue 					String that holds the value of the model element
	 * @param dataValue						String that holds the value of the data element
	 * 
	 * @return 								Double that is 0, 1.0, or a preset scale factor
	 * 
	 */
	public double getMismatch(String modelValue, String dataValue)
	{
		double mm = processSimpleCases(modelValue, dataValue);
		
		if (mm >= 0.0)
			return mm;
		else
			return 1.0;  // Not an exact match.
	}
}
