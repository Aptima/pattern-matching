package com.aptima.netstorm.algorithms.aptima.bp.network;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import com.aptima.netstorm.algorithms.aptima.bp.mismatch.AttributeConstraintVocab;

public class ModelAttributeConstraints {
	/**
	 * Optional field that indicates node or relation type.  The type often constrains
	 * the list of attributes for a node or relation.
	 */
	private String type;
	private HashMap<String, String> attributes; // Map from name to constraints
	
	public ModelAttributeConstraints()
	{
		this.attributes = new HashMap<String, String>();
	}
	
	public void addAttribute(String name, String value) {
		this.attributes.put(name, value);
	}
	
	public void setAttributes(HashMap<String, String> attributeToConstraintMap) {
		this.attributes = attributeToConstraintMap;
	}

	public String getAttributeConstraint(String name) {
		return this.attributes.get(name);
	}

	public Set<String> getAttributeNames() {
		return this.attributes.keySet();
	}
	
	public int getNumConstraints() {
		return this.attributes.size();
	}
	
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
