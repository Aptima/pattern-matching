package com.aptima.netstorm.algorithms.aptima.bp.network;

/**Extends the node properties from AdjacencyListNode to attach a set of constraints to make it an
 * Attributed model node
 * @author Aptima
 *
 */
public class AttributedModelNode extends AdjacencyListNode {
	
	private ModelAttributeConstraints constraintSet = new ModelAttributeConstraints();
	public static int nextId = 0;
	
	public AttributedModelNode()
	{
		super(nextId);
		nextId++;
	}
	
	public ModelAttributeConstraints getConstraintSet() {
		return constraintSet;
	}

	public void setConstraintSet(
			ModelAttributeConstraints constraintSet) {
		this.constraintSet = constraintSet;
	}
}