package com.aptima.netstorm.algorithms.aptima.bp.hive;

import com.aptima.netstorm.algorithms.aptima.bp.mismatch.AttributeConstraintVocab;
import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelNode;
import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelRelation;
import com.aptima.netstorm.algorithms.aptima.bp.network.ModelAttributeConstraints;

public abstract class AkamaiMR extends MRBase {

	public AkamaiMR(String[] args) {

		super(args);
		// Test pattern. Ultimately, this should be read from a string argument passed into the main function.
		

		// TODO: implement this or replace with something that reads from a file.
		createTestPattern();
	}

	public static void createTestPattern() {
	
		// 1 3 1 pattern
		modelNodes = new AttributedModelNode[5];
		modelRelations = new AttributedModelRelation[6];
		
		// create nodes before all relations
		
		// out degree
		AttributedModelNode n1 = new AttributedModelNode();

		// middle nodes
		AttributedModelNode n2 = new AttributedModelNode();
		AttributedModelNode n3 = new AttributedModelNode();
		AttributedModelNode n4 = new AttributedModelNode();
		
		// in degree
		AttributedModelNode n5 = new AttributedModelNode();
		
		String latency = "250.0";
		
		ModelAttributeConstraints limitR1 = new ModelAttributeConstraints();
		limitR1.addAttribute(AkamaiMapper.COL_AKAMAI_LATENCY, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + latency);
		
		ModelAttributeConstraints limitR2 = new ModelAttributeConstraints();
		limitR2.addAttribute(AkamaiMapper.COL_AKAMAI_LATENCY, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + latency);
		
		ModelAttributeConstraints limitR3 = new ModelAttributeConstraints();
		limitR3.addAttribute(AkamaiMapper.COL_AKAMAI_LATENCY, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + latency);
		
		ModelAttributeConstraints limitR4 = new ModelAttributeConstraints();
		limitR4.addAttribute(AkamaiMapper.COL_AKAMAI_LATENCY, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + latency);
		
		ModelAttributeConstraints limitR5 = new ModelAttributeConstraints();
		limitR5.addAttribute(AkamaiMapper.COL_AKAMAI_LATENCY, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + latency);
		
		ModelAttributeConstraints limitR6 = new ModelAttributeConstraints();
		limitR6.addAttribute(AkamaiMapper.COL_AKAMAI_LATENCY, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + latency);
		

		// outflow
		AttributedModelRelation r1 = new AttributedModelRelation();
		r1.setFromNode(n1.getId());
		r1.setToNode(n2.getId());
		r1.setConstraintSet(limitR1);
		
		AttributedModelRelation r2 = new AttributedModelRelation();
		r2.setFromNode(n1.getId());
		r2.setToNode(n3.getId());
		r2.setConstraintSet(limitR2);
		
		AttributedModelRelation r3 = new AttributedModelRelation();
		r3.setFromNode(n1.getId());
		r3.setToNode(n4.getId());
		r3.setConstraintSet(limitR3);
		
		// inflow
		AttributedModelRelation r4 = new AttributedModelRelation();
		r4.setFromNode(n2.getId());
		r4.setToNode(n5.getId());
		r4.setConstraintSet(limitR4);
		
		AttributedModelRelation r5 = new AttributedModelRelation();
		r5.setFromNode(n3.getId());
		r5.setToNode(n5.getId());
		r5.setConstraintSet(limitR5);
		
		AttributedModelRelation r6 = new AttributedModelRelation();
		r6.setFromNode(n4.getId());
		r6.setToNode(n5.getId());
		r6.setConstraintSet(limitR6);
		
		// out degree Rel
		n1.addSuccessorRelation(r1.getId());
		n1.addSuccessorRelation(r2.getId());
		n1.addSuccessorRelation(r3.getId());
		
		// middle nodes Rel
		n2.addPredecessorRelation(r1.getId());
		n2.addSuccessorRelation(r4.getId());
		
		n3.addPredecessorRelation(r2.getId());
		n3.addSuccessorRelation(r5.getId());
		
		n4.addPredecessorRelation(r3.getId());
		n4.addSuccessorRelation(r6.getId());
		
		// in degree Rel
		n5.addPredecessorRelation(r4.getId());
		n5.addPredecessorRelation(r5.getId());
		n5.addPredecessorRelation(r6.getId());
	
		// node constraints
		ModelAttributeConstraints n1Constraints = new ModelAttributeConstraints();
		n1Constraints.addAttribute(AkamaiMapper.COL_AKAMAI_LATENCY, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + latency);
		
		ModelAttributeConstraints n2Constraints = new ModelAttributeConstraints();
		n2Constraints.addAttribute(AkamaiMapper.COL_AKAMAI_LATENCY, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + latency);
		
		ModelAttributeConstraints n3Constraints = new ModelAttributeConstraints();
		n3Constraints.addAttribute(AkamaiMapper.COL_AKAMAI_LATENCY, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + latency);
		
		ModelAttributeConstraints n4Constraints = new ModelAttributeConstraints();
		n4Constraints.addAttribute(AkamaiMapper.COL_AKAMAI_LATENCY, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + latency);
		
		ModelAttributeConstraints n5Constraints = new ModelAttributeConstraints();
		n5Constraints.addAttribute(AkamaiMapper.COL_AKAMAI_LATENCY, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + latency);
		
		n1.setConstraintSet(n1Constraints);
		n2.setConstraintSet(n2Constraints);
		n3.setConstraintSet(n3Constraints);
		n4.setConstraintSet(n4Constraints);
		n5.setConstraintSet(n5Constraints);
		
		modelNodes[0] = n1;
		modelNodes[1] = n2;
		modelNodes[2] = n3;
		modelNodes[3] = n4;;
		modelNodes[4] = n5;
		
		modelRelations[0] = r1;
		modelRelations[1] = r2;
		modelRelations[2] = r3;
		modelRelations[3] = r4;
		modelRelations[4] = r5;
		modelRelations[5] = r6;
	}
}
