/*******************************************************************************
 * Copyright, Aptima, Inc. 2011
 * Unlimited Rights granted to Government per 252.227-7014
 *******************************************************************************/

package com.aptima.netstorm.algorithms.aptima.bp;

import java.util.ArrayList;
import java.util.Set;

import com.aptima.netstorm.algorithms.aptima.bp.RelationshipMismatchProvider.MismatchRelationshipType;

public class BeliefPropagationMath {

	public static boolean _is_1_to_1 = true;// ---GEORGIY added 9/20/2011
	public static double update_step = 1.0;
	
	/**Method performs Belief Propagation (BP) where nodes messages are passed between the nodes of the Model Graph, and link messages
	 * are passed between the links of the Model graph.  The messages hold information on the mismatch between the attributes of 
	 * the Model and Data graphs.
	 * 
	 * @param modelNodes			An array of the nodes in the Model Graph
	 * @param modelRelations		An array of the relations in the Model Graph (links between nodes)
	 * @param dataNodeCount			An integer that holds the number of nodes in the Data Graph
	 * @param mismatches			MismatchValues holds the mismatches between attributes of node/link in model and data graphs
	 * @param numIterations			An integer that holds the maximum number of iterations for BP to be performed
	 */
	public static void performBeliefPropagation(BPMathModelNode[] modelNodes, BPMathModelRelation[] modelRelations,
			int dataNodeCount, MismatchValues mismatches, int numIterations) {
		// dataModelNodeMismatch is double[dataNodeCount][modelNodes.size()]
		// predecessorRelationMismatch is indexed by "m,k,j" and stores a list of link mismatches for every link from i
		// to j where
		// i is a predecessor of j.
		// successorRelationMismatch is indexed by "m,k,j" and stores a list of link mismatches for every link from i to
		// j where
		// i is a predecessor of j.

		for (int i = 0; i < modelNodes.length; i++)
			modelNodes[i].initMu(dataNodeCount);

		for (int i = 0; i < modelRelations.length; i++)
			modelRelations[i].initLLR(dataNodeCount);

		for (int i = 0; i < numIterations; i++) {
//			if (context != null) {
//				context.setStatus("BP Iteration: " + i + " of " + (numIterations-1) + " (" + dataNodeCount + ")");
//			}
			// else
			// System.out.println("***Iteration: " + i);
			performIteration(modelNodes, modelRelations, mismatches);
		}

		// print out final state for testing
		// print(modelNodes, modelRelations, context, false, false, true);
	}

	
	/**Method prints the information of interest to the console.
	 * Information of interest is specified by setting the appropriate boolean variables to true
	 * 
	 * @param modelNodes			Array of the nodes in the Model Graph
	 * @param modelRelations		Array if the relations/links in the Model Graph
	 * @param printL				Boolean variable to print outgoing (L) and incoming (LR) link information
	 * @param printMu				Boolean variable to print the messages sent between nodes in Model Graph
	 * @param printP				Boolean variable to print the probabilities that are encoded within the messages
	 */
	public static void print(BPMathModelNode[] modelNodes, BPMathModelRelation[] modelRelations, boolean printL,
			boolean printMu, boolean printP) {
//		if (context != null)
//			System.out.println("***" + context.getStatus());
		if (printL) {
			System.err.println("---------L variables");
			for (int i = 0; i < modelRelations.length; i++) {

				// System.out.println("Model Relation : " + i + " Link:");
				printLorLR(modelRelations[i].getLink());

			}
			System.err.println("---------LR variables");
			for (int i = 0; i < modelRelations.length; i++) {

				// System.out.println("Model Relation : " + i + " Link Reverse:");
				printLorLR(modelRelations[i].getLinkReverse());
			}
		}
		//
		if (printMu) {
			System.err.println("---------Mu variables");
			for (int i = 0; i < modelNodes.length; i++) {
				// System.out.println("Model Node : " + i + " Mu:");
				printMu(modelNodes[i].getMu());
			}
		}
		//
		if (printP) {
			System.err.println("---------Probabilities");
			for (int i = 0; i < modelNodes.length; i++) {
				// System.out.println("Model Node : " + i + " Mu:");
				printProb(modelNodes[i].getMu());
			}
		}
	}

	/**Method prints the outgoing (L) and incoming (LR) relation information
	 * 
	 * @param list					A double list that holds information contained within outgoing (L) and incoming (LR) links within the Model Graph
	 */
	public static void printLorLR(ArrayList<Double> list) {
		String s = "";
		for (int i = 0; i < list.size(); i++) {
			// System.out.println(list.get(i));
			s += list.get(i) + " ";
		}
		System.err.println(s);
	}
	
	/**Method prints the messages between nodes in Model Graph
	 * 
	 * @param list					A list that contains message that contains information of the mismatch between a node in 
	 * 								the Model graph and the approriate node in the Data Graph
	 */
	public static void printMu(ArrayList<DataNodeAndMu> list) {
		String s = "";
		for (int i = 0; i < list.size(); i++) {
			// System.out.println(list.get(i));
			s += list.get(i).Mu + " ";
		}
		System.err.println(s);
	}
	
	/**Method prints the probabilities contained within the messages Mu
	 * 
	 * @param list					ArrayList<DataNodeAndMu> - A list that contains message that contains probability of a node in the Model Graph and a 
	 * 								node within the Data Graph
	 */
	public static void printProb(ArrayList<DataNodeAndMu> list) {
		String s = "";
		// get max val
		double vMax = 0.0;
		for (int i = 0; i < list.size(); i++) {
			// System.out.println(list.get(i).DataNode + ", " + Math.exp(list.get(i).Mu));
			if (vMax < Math.exp(list.get(i).Mu))
				vMax = Math.exp(list.get(i).Mu);
		}
		// update max
		vMax = vMax * 0.6;
		// only print the values near max
		for (int i = 0; i < list.size(); i++) {
			// System.out.println(list.get(i).DataNode + ", " + Math.exp(list.get(i).Mu));
			if (vMax < Math.exp(list.get(i).Mu))
				s += Math.exp(list.get(i).Mu) + "[" + i + "]" + " ";
		}
		System.err.println(s);

	}

	/**Method performs a single iteration of the BP algorithm
	 * 
	 * @param modelNodes			Array of the nodes in the Model Graph
	 * @param modelRelations		Array of the relations in the Model Graph	
	 * @param mismatches			Mismatch values of attributes between nodes/relations in Model and Data graphs
	 */
	
	private static void performIteration(BPMathModelNode[] modelNodes, BPMathModelRelation[] modelRelations, MismatchValues mismatches) {

		// /////////////////// L/LR updates
		ArrayList<ArrayList<Double>> nLinkSet_new = new ArrayList<ArrayList<Double>>();// ---GEORGIY - added these temp
																						// data struct 9/20/2011
		ArrayList<ArrayList<Double>> nLinkSetReverse_new = new ArrayList<ArrayList<Double>>();// ---GEORGIY - added
																								// these temp data
																								// struct 9/20/2011
		
		int dataNodeCount = 0;
		if(modelNodes != null && modelNodes.length >=1) {
			dataNodeCount = modelNodes[0].getMu().size();
		}
		
		for (int i = 0; i < modelRelations.length; i++) {
			int source = modelRelations[i].getBaseRelation().getFromNode();
			int dest = modelRelations[i].getBaseRelation().getToNode();
			// direct link update
//			if (context != null) {
//				context.setStatus("Update link " + i + " of " + (modelRelations.length-1) + " (" + dataNodeCount + ")");
//			}
			ArrayList<Double> link_new = BeliefPropagationMath.updateLink(modelRelations[i], modelNodes[source].getMu(),
					mismatches, MismatchRelationshipType.Predecessor);
			nLinkSet_new.add(link_new);
			// reverse link update
//			if (context != null) {
//				context.setStatus("Update link reverse " + i + " of " + (modelRelations.length-1) + " (" + dataNodeCount + ")");
//			}
			ArrayList<Double> link_rev_new = BeliefPropagationMath.updateLinkReverse(modelRelations[i], modelNodes[dest].getMu(),
					mismatches, MismatchRelationshipType.Successor);
			nLinkSetReverse_new.add(link_rev_new);

		}
		// finally update links
		for (int i = 0; i < modelRelations.length; i++) {
			modelRelations[i].setLink(nLinkSet_new.get(i));
			modelRelations[i].setLinkReverse(nLinkSetReverse_new.get(i));
		}
		// /////////////////// Mu updates
		for (int i = 0; i < modelNodes.length; i++) {
			// ---GEORGIY - this is not needed?
			ArrayList<ArrayList<Double>> nLink = new ArrayList<ArrayList<Double>>();
			ArrayList<ArrayList<Double>> nLinkReverse = new ArrayList<ArrayList<Double>>();

			for (int p = 0; p < modelNodes[i].getPredecessorRelations().size(); p++) {
				int predIdx = modelNodes[i].getPredecessorRelations().get(p);
				nLink.add(modelRelations[predIdx - modelNodes.length].getLink());
			}

			for (int s = 0; s < modelNodes[i].getSuccessorRelations().size(); s++) {
				int succIdx = modelNodes[i].getSuccessorRelations().get(s);
				nLinkReverse.add(modelRelations[succIdx - modelNodes.length].getLinkReverse());
			}

//			if (context != null) {
//				context.setStatus("Update mu " + i + " of " + (modelNodes.length-1) + " (" + dataNodeCount + ")");
//			}
			BeliefPropagationMath.updateMu(modelNodes[i], nLink, nLinkReverse, mismatches);
		}
		// end of the updates
		// print out final state for testing
		// print(modelNodes, modelRelations, context, false, false, true);
	}

	/**Method updates the messages that are sent between nodes and links based on the mismatch values
	 * 
	 * @param modelNode				Specific node within Model Graph
	 * @param nLink					A list of all the outgoing links from that node
	 * @param nLinkReverse			A list of all the incoming links from that node
	 * @param mismatches			The mismatches between attributes of nodes/links between Model and Data Graphs
	 */
	public static void updateMu(BPMathModelNode modelNode, ArrayList<ArrayList<Double>> nLink,
			ArrayList<ArrayList<Double>> nLinkReverse, MismatchValues mismatches) {
		ArrayList<DataNodeAndMu> nodeMuVector = modelNode.getMu();
		int dataNodesCount = nodeMuVector.size();

		double nodeMismatch = 0.0;
		double linkSum = 0.0;
		double linkReverseSum = 0.0;
		double nodeMu = 0.0;

		int modelNodeIndex = modelNode.getId();

		for (int i = 0; i < dataNodesCount; i++) {

			linkSum = 0.0;
			linkReverseSum = 0.0;

			nodeMismatch = mismatches.getNodeMismatch(modelNodeIndex, i);

			for (ArrayList<Double> nLocalLink : nLink) {
				// if (nLocalLink.size() == 0) {
				// System.out.println("LocalLink has no data!");
				// }
				linkSum += nLocalLink.get(i);
			}

			for (ArrayList<Double> nLocalLinkReverse : nLinkReverse) {
				// if (nLocalLinkReverse.size() == 0) {
				// System.out.println("LocalLinkReverse has no data!");
				// }
				linkReverseSum += nLocalLinkReverse.get(i);
			}

			double nodeMu_old = nodeMuVector.get(i).Mu;
			//new val
			nodeMu = (-1.0 * nodeMismatch) + linkSum + linkReverseSum;
			//incremental update
			nodeMu = nodeMu_old + update_step * (nodeMu - nodeMu_old);

			nodeMuVector.set(i, new DataNodeAndMu(i, nodeMu));

		}

		// mu normalize
		double muNormalize = 0.0;
		for (int j = 0; j < dataNodesCount; j++) {
			muNormalize += Math.exp(nodeMuVector.get(j).Mu);
		}
		if(muNormalize > 0)
			muNormalize = Math.log(muNormalize);

		for (int i = 0; i < dataNodesCount; i++) {

			nodeMu = nodeMuVector.get(i).Mu;
			nodeMu = nodeMu - muNormalize;
			if(nodeMu > 0)
			{
				String s = "ERROR in MU update";
				nodeMu = 0.0;
			}
			nodeMuVector.set(i, new DataNodeAndMu(i, nodeMu));
			
		}
	}

	/**Method updates the mismatch of a particular outgoing relation in the Model Graph with all other relevant relations in the Data Graph
	 * 
	 * @param r								Specific outgoing relation from a node
	 * @param rMuOfM						Message that is being passed from the relation						
	 * @param mismatchProvider				TODO: NOT SURE WHERE THIS INTEFACE IS TO BE OVERRIDDEN!!!	
	 * @param mismatchRelationshipType		TODO: Predecessor and Successor sound like they are previous and next iteration? Is that correct?
	 * @return
	 */
	public static ArrayList<Double> updateLink(BPMathModelRelation r, ArrayList<DataNodeAndMu> rMuOfM,
			RelationshipMismatchProvider mismatchProvider, MismatchRelationshipType mismatchRelationshipType) {
		String linkMismatchPrefix = r.getBaseRelation().getFromNode() + "," + r.getBaseRelation().getToNode() + ",";
		return updateLorLR(r.getLink(), rMuOfM, true, r.getLinkReverse(), linkMismatchPrefix, mismatchProvider, mismatchRelationshipType);
	}
	
	/**Method updates the mismatch of a particular incoming relation in the Model Graph with all other relevant relations in the Data Graph
	 * 
	 * @param r								Specific incoming relation from a node
	 * @param rMuOfK						Message that is being passed from the relation						
	 * @param mismatchProvider				TODO: NOT SURE WHERE THIS INTEFACE IS TO BE OVERRIDDEN!!!	
	 * @param mismatchRelationshipType		TODO: Predecessor and Successor sound like they are previous and next iteration? Is that correct?
	 * @return
	 */
	public static ArrayList<Double> updateLinkReverse(BPMathModelRelation r, ArrayList<DataNodeAndMu> rMuOfK,
			RelationshipMismatchProvider mismatchProvider, MismatchRelationshipType mismatchRelationshipType) {
		String linkMismatchPrefix = r.getBaseRelation().getFromNode() + "," + r.getBaseRelation().getToNode() + ",";
		return updateLorLR(r.getLinkReverse(), rMuOfK, false, r.getLink(), linkMismatchPrefix, mismatchProvider, mismatchRelationshipType);
	}

	/**
	 * 
	 * @param toUpdate							List of Relation attributes that need to be updated
	 * @param relatedMu							Message that is being passed from the relation	
	 * @param mismatchSourceFirst				Boolean variable that if true checks mismatch between "From" node information first
	 * @param otherLorLR						List of Relation attributes of link in opposite direction
	 * @param linkMismatchPrefix				String that holds the From and To node names in "From_Node_Name,ToNodeName," format
	 * @param mismatchProvider					TODO: NOT SURE WHERE THIS INTEFACE IS TO BE OVERRIDDEN!!!	
	 * @param mismatchRelationshipType			TODO: Predecessor and Successor sound like they are previous and next iteration? Is that correct?
	 * @return	ArrayList<Double>				Updated relation information				
	 */
	private static ArrayList<Double> updateLorLR(ArrayList<Double> toUpdate, ArrayList<DataNodeAndMu> relatedMu,
			boolean mismatchSourceFirst, ArrayList<Double> otherLorLR, String linkMismatchPrefix,
			RelationshipMismatchProvider mismatchProvider, MismatchRelationshipType mismatchRelationshipType) {

		// init return result
		ArrayList<Double> res = new ArrayList<Double>();

		// compute comparison value (find 1-st and 2-nd max, so that could deal with 1-to-1 constraint)
		// if L update, find value = - C_mk_max + max_i {Mu(m,i)-LR(m,k,i)}
		// if LR update, find value = - C_mk_max + max_i {Mu(k,i)-L(m,k,i)}

		double comparison_val_1 = (-1.0) * Double.MAX_VALUE;
		double comparison_val_2 = (-1.0) * Double.MAX_VALUE;
		int best_index = -1;
		for (int j = 0; j < toUpdate.size(); j++) {
			double currVal = relatedMu.get(j).Mu - otherLorLR.get(j);
			if (currVal >= comparison_val_1) {
				comparison_val_2 = comparison_val_1;
				comparison_val_1 = currVal;
				best_index = j;
			} else if (currVal > comparison_val_2)
				comparison_val_2 = currVal;
			//
			res.add(j, 0.0);
		}
		// add link not present value
		comparison_val_1 -= MismatchValues.defaultLinkNotPresentMismatch;// NOTE: need to make this penalty DIFFERENT
																			// for different (m,k)
		comparison_val_2 -= MismatchValues.defaultLinkNotPresentMismatch;// NOTE: need to make this penalty DIFFERENT
																			// for different (m,k)

		// update Link
		for (int j = 0; j < toUpdate.size(); j++) {
			double max = (-1.0) * Double.MAX_VALUE;
			double linkMismatch = 0.0;
			double mu = 0.0;
			double linkValue = 0.0;
			double value = 0.0;

			String linkMismatchKey = linkMismatchPrefix + j;

			// determine the actual compare value (see if the best val = the data with i=j or not)
			max = (_is_1_to_1 && best_index == j) ? comparison_val_2 : comparison_val_1;// --- GEORGIY ADDED on
																						// 9/20/2011
			// key is now m,k,i
			if (mismatchProvider.containsMKIKey(linkMismatchKey, mismatchRelationshipType)) {
				// this set is all the j's for a given i (where j is either a pred or succ, depending on the
				// calculation)
				Set<Integer> dataNodeSet = mismatchProvider.getDataNodesFor(linkMismatchKey, mismatchRelationshipType); 
				for(Integer dataNode : dataNodeSet) {
					if (dataNode != j) {
						linkMismatch = -1.0 * mismatchProvider.getLowestMismatch(linkMismatchKey, dataNode, mismatchRelationshipType);
						mu = relatedMu.get(dataNode).Mu;
						linkValue = -1.0 * otherLorLR.get(dataNode);

						value = linkMismatch + mu + linkValue;
						if (value > max) {
							max = value;
						}
					}
				}
				/*
				 * Set<Integer> dataNodes = modelDataRelationMismatch.get(linkMismatchKey).keySet(); for (int dataNode :
				 * dataNodes) { if (dataNode != j) { linkMismatch = -1.0 *
				 * modelDataRelationMismatch.get(linkMismatchKey).get(dataNode); mu = relatedMu.get(dataNode).Mu;
				 * linkValue = -1.0 * otherLorLR.get(dataNode);
				 * 
				 * value = linkMismatch + mu + linkValue; if (value > max) { max = value; } } }
				 */
				res.set(j, max);
			} else {
				// TODO: check this.
				res.set(j, max);// -1 * Mismatch.LinkNotPresent); --- GEORGIY CHANGED on 9/20/2011
			}
		}
		//incremental update
		for (int j = 0; j < toUpdate.size(); j++) {
			double valNew = res.get(j);
			double valOld = toUpdate.get(j);
			double val = valOld + update_step * (valNew - valOld);
			res.set(j, val);
		}
		
		// normalize
		double normalize = 0.0;
		for (int j = 0; j < res.size(); j++) {
			normalize += Math.exp(res.get(j));
		}
		if(normalize > 0)
			normalize = Math.log(normalize);

		double LorLR;
		for (int i = 0; i < res.size(); i++) {

			LorLR = res.get(i);
			LorLR = LorLR - normalize;
			if(LorLR > 0)
			{
				String s = "ERROR in L/LR update";
				LorLR = 0.0;
			}
			res.set(i, LorLR);
		}

		// return
		return res;
	}
}
