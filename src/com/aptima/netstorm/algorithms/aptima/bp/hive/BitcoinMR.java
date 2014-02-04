package com.aptima.netstorm.algorithms.aptima.bp.hive;

import com.aptima.netstorm.algorithms.aptima.bp.mismatch.AttributeConstraintVocab;
import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelNode;
import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelRelation;
import com.aptima.netstorm.algorithms.aptima.bp.network.ModelAttributeConstraints;

public abstract class BitcoinMR extends MRBase {

	public static boolean constrainFlow = false;

	public BitcoinMR(String[] args) {

		// Test pattern. Ultimately, this should be read from a string argument passed into the main function.
		super(args);

		// TODO: implement this or replace with something that reads from a file.
		// mixing();
		//thiefVictimFinder();
		lenderToDonor();
	}

	public static void lenderToDonor() {

		modelNodes = new AttributedModelNode[2];
		modelRelations = new AttributedModelRelation[1];

		AttributedModelNode lender = new AttributedModelNode();
		AttributedModelNode donor = new AttributedModelNode();
	
		AttributedModelRelation lenderDonor = new AttributedModelRelation();
		lenderDonor.setFromNode(lender.getId());
		lenderDonor.setToNode(donor.getId());

		ModelAttributeConstraints donorIndegree = new ModelAttributeConstraints();
		donorIndegree.addAttribute(BitcoinMapper.COL_BITCOIN_IN_DEGREE, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + "25000");
		donor.setConstraintSet(donorIndegree);
		
		ModelAttributeConstraints lenderOutAmount = new ModelAttributeConstraints();
		lenderOutAmount.addAttribute(BitcoinMapper.COL_BITCOIN_OUT_AMT, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + "100");
		lender.setConstraintSet(lenderOutAmount);
		
		modelNodes[0] = lender;
		modelNodes[1] = donor;

		modelRelations[0] = lenderDonor;
	}
	
	public static void thiefVictimFinder() {

		modelNodes = new AttributedModelNode[3];
		modelRelations = new AttributedModelRelation[2];

		// create nodes before all relations

		// out degree
		AttributedModelNode thief = new AttributedModelNode();
		AttributedModelNode victim = new AttributedModelNode();
		AttributedModelNode finder = new AttributedModelNode();
	
		ModelAttributeConstraints victimThiefConstraints = new ModelAttributeConstraints();
		victimThiefConstraints.addAttribute(BitcoinMapper.COL_BITCOIN_AMT, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + "20000");

		ModelAttributeConstraints thiefFinderConstraints = new ModelAttributeConstraints();
		thiefFinderConstraints.addAttribute(BitcoinMapper.COL_BITCOIN_AMT, AttributeConstraintVocab.LESS_THAN_OR_EQUAL_TO + "1.0");

		AttributedModelRelation victimThief = new AttributedModelRelation();
		victimThief.setFromNode(victim.getId());
		victimThief.setToNode(thief.getId());
		victimThief.setConstraintSet(victimThiefConstraints);

		AttributedModelRelation thiefFinder = new AttributedModelRelation();
		thiefFinder.setFromNode(thief.getId());
		thiefFinder.setToNode(finder.getId());
		thiefFinder.setConstraintSet(thiefFinderConstraints);

		// from flush
		ModelAttributeConstraints victimInDegree = new ModelAttributeConstraints();
		victimInDegree.addAttribute(BitcoinMapper.COL_BITCOIN_IN_DEGREE, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + "50");
		victim.setConstraintSet(victimInDegree);
		
		modelNodes[0] = victim;
		modelNodes[1] = thief;
		modelNodes[2] = finder;

		modelRelations[0] = victimThief;
		modelRelations[1] = thiefFinder;
	}

	public static void mixing() {

		constrainFlow = true;

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

		ModelAttributeConstraints limitR1 = new ModelAttributeConstraints();
		limitR1.addAttribute(BitcoinMapper.COL_BITCOIN_AMT, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + "0.5");

		ModelAttributeConstraints limitR2 = new ModelAttributeConstraints();
		limitR2.addAttribute(BitcoinMapper.COL_BITCOIN_AMT, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + "0.5");

		ModelAttributeConstraints limitR3 = new ModelAttributeConstraints();
		limitR3.addAttribute(BitcoinMapper.COL_BITCOIN_AMT, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + "0.5");

		ModelAttributeConstraints limitR4 = new ModelAttributeConstraints();
		limitR4.addAttribute(BitcoinMapper.COL_BITCOIN_AMT, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + "0.5");

		ModelAttributeConstraints limitR5 = new ModelAttributeConstraints();
		limitR5.addAttribute(BitcoinMapper.COL_BITCOIN_AMT, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + "0.5");

		ModelAttributeConstraints limitR6 = new ModelAttributeConstraints();
		limitR6.addAttribute(BitcoinMapper.COL_BITCOIN_AMT, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + "0.5");

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

		// constraints
		ModelAttributeConstraints outDegreeAndFlow = new ModelAttributeConstraints();
		outDegreeAndFlow.addAttribute(BitcoinMapper.COL_BITCOIN_OUT_DEGREE, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO
				+ "3");
		// outDegreeAndFlow.addAttribute(BitcoinMapper.COL_BITCOIN_OUT_AMT, AttributeConstraintVocab.EQUAL_TO + "50");
		// inDegreeAndFlow.addAttribute(OUTFLOW, AttributeConstraintVocab.EQUAL_TO + "5000");

		ModelAttributeConstraints inDegreeAndFlow = new ModelAttributeConstraints();
		inDegreeAndFlow
				.addAttribute(BitcoinMapper.COL_BITCOIN_IN_DEGREE, AttributeConstraintVocab.GREATER_THAN_OR_EQUAL_TO + "3");
		// inDegreeAndFlow.addAttribute(BitcoinMapper.COL_BITCOIN_IN_AMT, AttributeConstraintVocab.EQUAL_TO + "50");
		// outDegreeAndFlow.addAttribute(INFLOW, AttributeConstraintVocab.EQUAL_TO + "5000");

		n1.setConstraintSet(outDegreeAndFlow);
		n5.setConstraintSet(inDegreeAndFlow);

		/*
		 * ModelAttributeConstraints n2Flow = new ModelAttributeConstraints();
		 * n2Flow.addAttribute(BitcoinMapper.COL_BITCOIN_IN_DEGREE, AttributeConstraintVocab.EQUAL_TO + "1");
		 * n2Flow.addAttribute(BitcoinMapper.COL_BITCOIN_OUT_DEGREE, AttributeConstraintVocab.EQUAL_TO + "1");
		 * 
		 * ModelAttributeConstraints n3Flow = new ModelAttributeConstraints();
		 * n3Flow.addAttribute(BitcoinMapper.COL_BITCOIN_IN_DEGREE, AttributeConstraintVocab.EQUAL_TO + "1");
		 * n3Flow.addAttribute(BitcoinMapper.COL_BITCOIN_OUT_DEGREE, AttributeConstraintVocab.EQUAL_TO + "1");
		 * 
		 * ModelAttributeConstraints n4Flow = new ModelAttributeConstraints();
		 * n4Flow.addAttribute(BitcoinMapper.COL_BITCOIN_IN_DEGREE, AttributeConstraintVocab.EQUAL_TO + "1");
		 * n4Flow.addAttribute(BitcoinMapper.COL_BITCOIN_OUT_DEGREE, AttributeConstraintVocab.EQUAL_TO + "1");
		 */

		// n2.setConstraintSet(n2Flow);
		// n3.setConstraintSet(n3Flow);
		// n4.setConstraintSet(n4Flow);

		modelNodes[0] = n1;
		modelNodes[1] = n2;
		modelNodes[2] = n3;
		modelNodes[3] = n4;
		;
		modelNodes[4] = n5;

		modelRelations[0] = r1;
		modelRelations[1] = r2;
		modelRelations[2] = r3;
		modelRelations[3] = r4;
		modelRelations[4] = r5;
		modelRelations[5] = r6;
	}
}
