package com.aptima.netstorm.algorithms.aptima.bp.network;

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
