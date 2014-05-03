package com.aptima.netstorm.algorithms.aptima.bp.mismatch;

/**Enumerator holds all the operations that will be defined over the graph
 * Range, >=, <=, =, >, <, ""
 * @author Aptima
 *
 */
public enum AttributeConstraintVocab {
	RANGE("RANGE"),
	GREATER_THAN_OR_EQUAL_TO(">="), 
	LESS_THAN_OR_EQUAL_TO("<="),
	EQUAL_TO("="),
	GREATER_THAN(">"), 
	LESS_THAN("<"),
	NO_SPECIAL_CHARACTERS(""),
	
	// Used to indicate when a data value is either blank or
	// specified to be "unknown."
	unknown("unknownValue"),

	// Used to indicate when a model value is left unspecified.
	// This indicates that mismatches related to this value should
	// not matter.
	unspecified("unspecified");
;
	
	String matchString;
	
	private AttributeConstraintVocab(String matchString)
	{
		this.matchString = matchString;
	}
	
	/**Method returns an AttributeConstraintVocab that matches the constraint input 
	 * 
	 * @param constraint 	A constraint in the form  >=, <=, =, >, <, ""
	 * @return				AttributeConstraintVocab that matches the operator string that was input
	 */
	public static AttributeConstraintVocab matchString(String constraint)
	{
		// Making this a list of if statements instead of a loop
		// because the order matters.  ">=" will match ">=", ">", and
		// "=", so ">=" must be detected first.
		if (constraint.contains(RANGE.matchString))
			return RANGE;
		
		if (constraint.contains(GREATER_THAN_OR_EQUAL_TO.matchString))
			return GREATER_THAN_OR_EQUAL_TO;
		
		if (constraint.contains(LESS_THAN_OR_EQUAL_TO.matchString))
			return LESS_THAN_OR_EQUAL_TO;

		AttributeConstraintVocab[] arr =
			new AttributeConstraintVocab[] {EQUAL_TO, GREATER_THAN, LESS_THAN};
		for (AttributeConstraintVocab v : arr)
		if (constraint.contains(v.matchString))
			return v;
		
		return NO_SPECIAL_CHARACTERS;
	}
	
	/**Method overrides the toString method to be able to return a String instead of a symbol
	 * 
	 */
	@Override
	public String toString() {
		// Override required because symbols such as ">" cannot be used in an enum.
		return matchString;
	}
}
