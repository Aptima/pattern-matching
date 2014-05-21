package com.aptima.netstorm.algorithms.aptima.bp.network;

public class AttributedModelRelation extends AdjacencyListRelation {
	private ModelAttributeConstraints constraintSet = new ModelAttributeConstraints();
	
	// BP Math currently requires this ID to be incremented over the model node ID!!!
	public AttributedModelRelation()
	{
		super(AttributedModelNode.nextId);
		AttributedModelNode.nextId++;
	}

	public ModelAttributeConstraints getConstraintSet() {
		return constraintSet;
	}

	public void setConstraintSet(
			ModelAttributeConstraints constraintSet) {
		this.constraintSet = constraintSet;
	}
}
