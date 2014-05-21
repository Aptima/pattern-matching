package com.aptima.netstorm.algorithms.aptima.bp.network;

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
