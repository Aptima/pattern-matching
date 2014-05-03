package com.aptima.netstorm.algorithms.aptima.bp.network;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**Class that defines a set of attributes for the data nodes in the format HashMap<String, String> attributes
 * 
 * @author meslami
 *
 */
public class DataAttributeSet {
	/**
	 * Optional field that indicates node or relation type.  The type often constrains
	 * the list of attributes for a node or relation.
	 */
	private String type;
	private HashMap<String, String> attributes; // Map from name to values
	
	public DataAttributeSet()
	{
		this.attributes = new HashMap<String, String>();
	}
	
	public void addAttribute(String name, String value) {
		this.attributes.put(name, value);
	}
	
	public void setAttributes(HashMap<String, String> attributeToValueMap) {
		this.attributes = attributeToValueMap;
	}

	public String getAttributeValue(String name) {
		return this.attributes.get(name);
	}

	public Set<String> getAttributeNames() {
		return this.attributes.keySet();
	}
	
	public int getNumAttributes()
	{
		return this.attributes.size();
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public boolean attributesEqual(DataAttributeSet other) {
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
