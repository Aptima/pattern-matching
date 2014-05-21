package com.aptima.netstorm.algorithms.aptima.bp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelGraph;
import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelNode;
import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelRelation;
import com.aptima.netstorm.algorithms.aptima.bp.network.ModelAttributeConstraints;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class ConvertFromGraphSON {
	private Map<Integer, Integer> fileIdMap = new HashMap<Integer, Integer>();
	
	public AttributedModelGraph convert(Graph graph) {
		AttributedModelGraph attributedModelGraph = null;
		
		List<AttributedModelNode> nodes = new ArrayList<AttributedModelNode>();
		for(Vertex vertex: graph.getVertices()) {
			AttributedModelNode node = new AttributedModelNode();
			fileIdMap.put(Integer.parseInt((String) vertex.getId()), node.getId());
			node.setFileId(Integer.parseInt((String) vertex.getId()));
			Set<String> keys = vertex.getPropertyKeys();
			for (String key : keys) {
				ModelAttributeConstraints modelAttributeConstraints = new ModelAttributeConstraints();
				modelAttributeConstraints.addAttribute(key, (String) vertex.getProperty(key));
				node.setConstraintSet(modelAttributeConstraints);
			}
			nodes.add(node);
		}
		
		List<AttributedModelRelation> relations = new ArrayList<AttributedModelRelation>();
		for(Edge edge: graph.getEdges()) {
			AttributedModelRelation relation = new AttributedModelRelation();
			fileIdMap.put(Integer.parseInt((String) edge.getId()), relation.getId());
			relation.setFileId(Integer.parseInt((String) edge.getId()));
			int outId = Integer.parseInt((String) edge.getVertex(Direction.OUT).getId());
			int inId = Integer.parseInt((String) edge.getVertex(Direction.IN).getId());
			relation.setFromNode(fileIdMap.get(outId));
			relation.setToNode(fileIdMap.get(inId));
			Set<String> keys = edge.getPropertyKeys();
			for (String key : keys) {
				ModelAttributeConstraints modelAttributeConstraints = new ModelAttributeConstraints();
				modelAttributeConstraints.addAttribute(key, (String) edge.getProperty(key));
				relation.setConstraintSet(modelAttributeConstraints);
			}
			relations.add(relation);
		}
		
		attributedModelGraph = new AttributedModelGraph(nodes, relations);
		return attributedModelGraph;
	}
}
