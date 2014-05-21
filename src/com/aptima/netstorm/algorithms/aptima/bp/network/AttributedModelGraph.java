package com.aptima.netstorm.algorithms.aptima.bp.network;

import java.util.List;

public class AttributedModelGraph {
	private List<AttributedModelNode> nodes;
	private List<AttributedModelRelation> relations;
	
	public AttributedModelGraph(List<AttributedModelNode> nodes, List<AttributedModelRelation> relations) {
		this.nodes = nodes;
		this.relations = relations;
	}

	public AttributedModelNode[] getNodes() {
		return nodes.toArray(new AttributedModelNode[0]);
	}
	
	public AttributedModelRelation[] getRelations() {
		return relations.toArray(new AttributedModelRelation[0]);
	}
}
