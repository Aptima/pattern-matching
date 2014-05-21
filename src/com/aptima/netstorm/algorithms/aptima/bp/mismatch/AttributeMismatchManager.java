package com.aptima.netstorm.algorithms.aptima.bp.mismatch;

import java.util.HashMap;

import com.aptima.netstorm.algorithms.aptima.bp.network.ModelAttributeConstraints;

public class AttributeMismatchManager {
	HashMap<AttributeConstraintVocab, AttributeMismatchProcessor> constraintToProcessorMap;
	
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
	
	public AttributeMismatchProcessor getMismatchProcessor(ModelAttributeConstraints m, String name)
	{
		String constraint = m.getAttributeConstraint(name);

		AttributeConstraintVocab constraintType = AttributeConstraintVocab.matchString(constraint);
		return this.constraintToProcessorMap.get(constraintType);
	}
}
