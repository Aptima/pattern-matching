package com.aptima.netstorm.algorithms.aptima.bp.network;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import com.aptima.netstorm.algorithms.aptima.bp.mismatch.AttributeConstraintVocab;

/**Class assigns attributes to a model node or relation along with its value.  The attributes have a String key 
 * and String value
 * 
 * @author Aptima
 *
 */
public class ModelAttributeConstraints {
	/**
	 * Optional field that indicates node or relation type.  The type often constrains
	 * the list of attributes for a node or relation.
	 */
	private String type;
	private HashMap<String, String> attributes; // Map from name to constraints
	
	/**
	 * Constructor Initializes the attributes to a HashMap<String,String>
	 */
	public ModelAttributeConstraints()
	{
		this.attributes = new HashMap<String, String>();
	}
	
	/**Method adds an attribute to the HashMap of attributes 
	 * 
	 * @param name			String name of attribute
	 * @param value			String value of attribute
	 */
	public void addAttribute(String name, String value) {
		this.attributes.put(name, value);
	}
	
	/**Method sets all attributes to attributeToConstraintMap
	 * 
	 * @param attributeToConstraintMap			HashMap<String,String> that holds the keys and values of the attributes
	 */
	public void setAttributes(HashMap<String, String> attributeToConstraintMap) {
		this.attributes = attributeToConstraintMap;
	}

	/**Method gets the value of an attribute specified by "name"
	 * 
	 * @param name								String key of attribute
	 * @return									String value of attribute
	 */
	public String getAttributeConstraint(String name) {
		return this.attributes.get(name);
	}
	
	/**Method returns a Set<String> that holds all of the attribute keys
	 * 
	 * @return									Set<String> that holds all of the attribute keys
	 */
	public Set<String> getAttributeNames() {
		return this.attributes.keySet();
	}
	
	/**Method returns the number of attributes
	 * 
	 * @return									int that is the number of attributes			
	 */
	public int getNumConstraints() {
		return this.attributes.size();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumSpecifiedContraints() {
		int numConstraints = 0;
		for (String constraint : this.attributes.values())
			if (!constraint.equals(AttributeConstraintVocab.unspecified.toString()))
				numConstraints++;

		return numConstraints;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public boolean constraintsEqual(ModelAttributeConstraints other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if ( !(this.getAttributeNames().containsAll(other.getAttributeNames())) )
			return false;
		if ( !(other.getAttributeNames().containsAll(this.getAttributeNames())) )
			return false;
		if (!Arrays.equals(this.attributes.values().toArray(), other.attributes.values().toArray()))
			return false;
		return true;
	}
}