package com.aptima.netstorm.algorithms.aptima.bp.network;

public class AttributedDataNode extends AdjacencyListNode {
	private DataAttributeSet attributeSet;
	
	public AttributedDataNode(int id) {
		super(id);
	}

	public DataAttributeSet getAttributeSet() {
		return attributeSet;
	}

	public void setAttributeSet(DataAttributeSet attributeSet) {
		this.attributeSet = attributeSet;
	}

}
