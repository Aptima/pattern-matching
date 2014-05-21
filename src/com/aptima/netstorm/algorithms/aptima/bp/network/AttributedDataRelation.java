package com.aptima.netstorm.algorithms.aptima.bp.network;

/**Extends the node properties from AdjacencyListRelation to attach a set of attributes to make it an
 * Attributed data relation
 * @author Aptima
 *
 */
public class AttributedDataRelation extends AdjacencyListRelation {
	private DataAttributeSet attributeSet;

	public AttributedDataRelation(int id) {
		super(id);
	}
	
	public DataAttributeSet getAttributeSet() {
		return attributeSet;
	}

	public void setAttributeSet(DataAttributeSet attributeSet) {
		this.attributeSet = attributeSet;
	}
}