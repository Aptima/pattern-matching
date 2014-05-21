package com.aptima.netstorm.algorithms.aptima.bp.mismatch;

import java.util.HashMap;

import com.aptima.netstorm.algorithms.aptima.bp.network.ModelAttributeConstraints;

/**Class puts a specific constraint (range, greater than or equal to,...) to a processor
 * 
 * @author Aptima
 *
 */
public class AttributeMismatchManager {
	/**A map that holds as key a specific constraint for an attribute to a processor
	 * 
	 */
	HashMap<AttributeConstraintVocab, AttributeMismatchProcessor> constraintToProcessorMap;
	
	/**Constructor that adds the specific AttributeConstraintVocab to a Processor
	 * 
	 */
	public AttributeMismatchManager()
	{
//		TODO: Consider adding a nameToProcessorMap for when the processor depends on the name of the 
//		attribute.  (e.g., for the NameProcessor and potentially the BitSetProcessor)
		
		this.constraintToProcessorMap = new HashMap<AttributeConstraintVocab, AttributeMismatchProcessor>();
		
		this.constraintToProcessorMap.put(AttributeConstraintVocab.RANGE, new RangeProcessor());
		this.constraintToProcessorMap.put(AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO, new GreaterThanOrEqualIntegerProcessor());
		this.constraintToProcessorMap.put(AttributeConstraintVocab.LESS_THAN_OR_EQUAL_TO, new LessThanOrEqualIntegerProcessor());
		this.constraintToProcessorMap.put(AttributeConstraintVocab.EQUAL_TO, new EqualIntegerProcessor());
		this.constraintToProcessorMap.put(AttributeConstraintVocab.NO_SPECIAL_CHARACTERS, new DefaultAttributeMismatchProcessor());
	}
	
	/**Method gets the processor that a specific constraint is mapped to
	 * 
	 * @param m						The set of attributes for a model
	 * @param name					The name of the constraint/attribute
	 * @return						THe processor that is mapped to the constraint "name" in the list of attributes "m"
	 */
	public AttributeMismatchProcessor getMismatchProcessor(ModelAttributeConstraints m, String name)
	{
		String constraint = m.getAttributeConstraint(name);

		AttributeConstraintVocab constraintType = AttributeConstraintVocab.matchString(constraint);
		return this.constraintToProcessorMap.get(constraintType);
	}
}