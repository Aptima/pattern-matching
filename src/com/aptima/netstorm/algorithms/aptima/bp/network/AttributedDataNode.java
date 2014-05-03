package com.aptima.netstorm.algorithms.aptima.bp.network;

/**Extends the node properties from AdjacencyListNode to attach a set of attributes to make it an
 * Attributed data node
 * @author Aptima
 *
 */
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
