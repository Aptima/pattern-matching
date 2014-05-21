package com.aptima.netstorm.algorithms.aptima.bp.sampling;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.aptima.netstorm.algorithms.aptima.bp.BPMathModelNode;
import com.aptima.netstorm.algorithms.aptima.bp.BPMathModelRelation;
import com.aptima.netstorm.algorithms.aptima.bp.DataNodeAndMu;
import com.aptima.netstorm.algorithms.aptima.bp.DataNodeAndMuComparator;
import com.aptima.netstorm.algorithms.aptima.bp.MismatchValues;
import com.aptima.netstorm.algorithms.aptima.bp.RelationshipMismatchProvider.MismatchRelationshipType;
//import org.apache.log4j.Logger;

public class DFSSampler implements Sampler {

	// private Logger mLogger; // general logger

	// Relation mismatches: key = m,k,i list of j (modelFrom, modelTo, dataFrom, dataTo)
	// Mismatch only exists if there is a relationship between i and j
	// private HashMap<String, HashMap<Integer, Double>> modelDataRelationMismatch;

	// Node mismatches: double[dataNode][modelNode]
	// private float[][] dataModelNodeMismatch;

	private MismatchValues mismatches;

	private boolean firstTopLevelIteration = true;
	private HashMap<Integer, String> recLevelLog = new HashMap<Integer, String>();

	// Contains sorted dataNodeAndMu information
	private BPMathModelNode[] modelNodes;

	// List of relationships that need to be checked for each model node
	// Assumes that the ModelNodes are analyzed in the order listed in
	// the <modelNodes> list.
	private Map<Integer, List<BPMathModelRelation>> relationshipConstraints;

	// the number of data nodes in the data network
	private int dataNodeCount;

	// limit samples and max recursionDepth
	private int maxSamplesToGenerate;
	// Replacing probDecrFactor with slider mismatch
	// private float probDecrFactor; // search from mu=ln(p) to minMu=ln(probDecrFactor*p) at each level of recursion
	private int maxBranchingFactor; // Follow at most maxBranchingFactor links at each level of recursion
	private int maxTopLevelIterations; // Consider at most maxTopLevelIterations nodes at the top level
	private int maxRecLevelIterations; // Consider at most maxRecLevelIterations nodes at each subsequent level
										// (only the ones with compatible link information turn into branches
	private double maxMismatch; // Maximum sample mismatch derived from the slider

	public ArrayList<Sample> samples;

	private boolean loggingOn = false;
	private boolean verbose = false;

	//private HashSet<String> seenGDIDs = new HashSet<String>();

	private HashMap<String, Integer> distinctAttributeConstraints = new HashMap<String, Integer>();

	// Set number maps to set of equivalent nodes
	public ArrayList<EquivalenceSet> equivalentNodeSets;

	private HashSet<Integer> getSuccessorNodes(BPMathModelNode n, BPMathModelRelation[] relations) {
		HashSet<Integer> succ = new HashSet<Integer>();
		for (int rId : n.getSuccessorRelations())
			succ.add(relations[rId - modelNodes.length].getBaseRelation().getToNode());
		return succ;
	}

	private HashSet<Integer> getPredecessorNodes(BPMathModelNode n, BPMathModelRelation[] relations) {
		HashSet<Integer> pred = new HashSet<Integer>();
		for (int rId : n.getPredecessorRelations())
			pred.add(relations[rId - modelNodes.length].getBaseRelation().getFromNode());
		return pred;
	}

	private boolean shareSamePredecessors(BPMathModelNode n1, BPMathModelNode n2, BPMathModelRelation[] relations) {
		HashSet<Integer> n1PredNodes = getPredecessorNodes(n1, relations);
		HashSet<Integer> n2PredNodes = getPredecessorNodes(n2, relations);

		return n1PredNodes.equals(n2PredNodes);
	}

	private boolean shareSameSuccessors(BPMathModelNode n1, BPMathModelNode n2, BPMathModelRelation[] relations) {
		HashSet<Integer> n1SuccNodes = getSuccessorNodes(n1, relations);
		HashSet<Integer> n2SuccNodes = getSuccessorNodes(n2, relations);

		return n1SuccNodes.equals(n2SuccNodes);
	}

	private HashMap<Integer, String> DataNodeToID;

	public DFSSampler(int maxSamplesToGenerate, int dataNodeCount, MismatchValues mismatches, BPMathModelNode[] modelNodes,
			BPMathModelRelation[] modelRelations, HashMap<Integer, String> DataNodeToID, double probDecrFactor,
			int maxBranchingFactor, int maxTopLevelIterations, int maxRecLevelIterations, double sliderValue) {
		// this.mLogger = Logger.getLogger(NetworkClusteringSensor.class);

		this.maxSamplesToGenerate = maxSamplesToGenerate;
		this.dataNodeCount = dataNodeCount;
		this.mismatches = mismatches;
		this.modelNodes = modelNodes;
		this.samples = new ArrayList<Sample>();
		this.DataNodeToID = DataNodeToID;
		// Replacing with sliderMismatch
		// this.probDecrFactor = probDecrFactor;
		this.maxBranchingFactor = (maxBranchingFactor < dataNodeCount ? maxBranchingFactor : dataNodeCount);
		this.maxTopLevelIterations = (maxTopLevelIterations < dataNodeCount ? maxTopLevelIterations : dataNodeCount);
		this.maxRecLevelIterations = (maxRecLevelIterations < dataNodeCount ? maxRecLevelIterations : dataNodeCount);
		this.maxMismatch = sliderValue;
		// this.seenGDIDs = seenGDIDs;

		for (BPMathModelNode mNode : modelNodes) {
			if (!mNode.getMuSorted()) {
				ArrayList<DataNodeAndMu> mMuVector = mNode.getMu();
				Collections.sort(mMuVector, new DataNodeAndMuComparator());
				mNode.setMu(mMuVector);
				mNode.setMuSorted(true);
			}
		}

		this.equivalentNodeSets = new ArrayList<EquivalenceSet>();
		HashSet<Integer> matches = new HashSet<Integer>();
		for (int i = 0; i < modelNodes.length; i++) {
			ArrayList<Integer> matchesForI = new ArrayList<Integer>();
			for (int j = i + 1; j < modelNodes.length; j++) {
				if (matches.contains(j))
					continue;
				if (modelNodes[i].getConstraintSet().constraintsEqual(modelNodes[j].getConstraintSet())
						&& shareSamePredecessors(modelNodes[i], modelNodes[j], modelRelations)
						&& shareSameSuccessors(modelNodes[i], modelNodes[j], modelRelations)) {
					if (matchesForI.isEmpty()) {
						matchesForI.add(modelNodes[i].getId());
						matches.add(modelNodes[i].getId());
					}
					matchesForI.add(modelNodes[j].getId());
					matches.add(modelNodes[j].getId());
				}
			}
			if (!matchesForI.isEmpty()) {
				this.equivalentNodeSets.add(new EquivalenceSet(matchesForI));
				// this.mLogger.info("Created equivalence set with " + matchesForI.size() + " nodes.");
			}
		}

		// Sort model nodes so that the most connected one is first and the first one is
		// not part of an equivalence set.
		this.modelNodes = getSortedModelNodes(modelNodes, modelRelations, this.equivalentNodeSets);

		// TODO: Delete this after it's been debugged
		// StringBuilder s = new StringBuilder();
		// s.append("Original node order: ");
		// for (ModelNode n : modelNodes)
		// {
		// int numNeighbors = n.getPredecessorRelations().size() + n.getSuccessorRelations().size();
		// s.append(numNeighbors + "\t");
		// }
		//
		// s.append("\nNumber of neighbors in the sorted nodes: ");
		// for (ModelNode n : this.modelNodes)
		// {
		// int numNeighbors = n.getPredecessorRelations().size() + n.getSuccessorRelations().size();
		// s.append(numNeighbors + "(" + n.getGuardDogID() + ")\t");
		// }
		// this.mLogger.info(s);

		// Initialize relationships constraints.
		this.relationshipConstraints = new HashMap<Integer, List<BPMathModelRelation>>(this.modelNodes.length);
		for (BPMathModelNode n : this.modelNodes)
			this.relationshipConstraints.put(n.getId(), new ArrayList<BPMathModelRelation>());

		for (BPMathModelRelation r : modelRelations) {
			int sourceId = r.getBaseRelation().getFromNode();
			int destId = r.getBaseRelation().getToNode();

			// Determine the relative order in which the node will be considered.
			int sourceIdx = getIndexOfNodeWithId(this.modelNodes, sourceId);
			int destIdx = getIndexOfNodeWithId(this.modelNodes, destId);

			if (sourceIdx < destIdx)
				// The link constrains sampling for the destination node
				this.relationshipConstraints.get(destId).add(r);
			else
				// The link constrains sampling for the source node
				this.relationshipConstraints.get(sourceId).add(r);
		}
	}

	private static int getIndexOfNodeWithId(BPMathModelNode[] nodes, int id) {
		for (int i = 0; i < nodes.length; i++)
			if (nodes[i].getId() == id)
				return i;

		return -1;
	}

	public void setLogging(boolean loggingOn) {
		this.loggingOn = loggingOn;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	private static BPMathModelNode[] getSortedModelNodes(BPMathModelNode[] modelNodes, BPMathModelRelation[] modelRelations,
			ArrayList<EquivalenceSet> eSets) {
		ArrayList<BPMathModelNode> sortedNodes = new ArrayList<BPMathModelNode>(Arrays.asList(modelNodes));

		// Sort nodes so that the first one has the highest degree and the last one has the lowest.
		Collections.sort(sortedNodes, new Comparator<BPMathModelNode>() {
			public int compare(BPMathModelNode n1, BPMathModelNode n2) {
				// This isn't technically correct because it's counting the number of
				// links instead of the number of neighboring nodes, but it's probably
				// good enough.
				int numN1Neighbors = n1.getPredecessorRelations().size() + n1.getSuccessorRelations().size();
				int numN2Neighbors = n2.getPredecessorRelations().size() + n2.getSuccessorRelations().size();

				return numN2Neighbors - numN1Neighbors;
			}
		});

		int idForFirst = sortedNodes.get(0).getId();
		boolean firstIsUnique = true;
		for (EquivalenceSet s : eSets)
			if (s.getModelNodeIds().contains(idForFirst)) {
				firstIsUnique = false;
				break;
			}

		// if (!firstIsUnique)
		// mLogger.info("Model node with the highest degree is part of an equivalence set.  "
		// + "Results will probably contain multiple instances of the same match.  "
		// + "Need to implement a work-around to deal with this special case.");

		return sortedNodes.toArray(new BPMathModelNode[sortedNodes.size()]);
	}

	public ArrayList<Sample> getSamples() {
		return this.samples;
	}

	private String generateRecLevelLog(int level, BPMathModelNode m, double firstMu, double endMu, int musConsidered) {
		StringBuilder s = new StringBuilder();
		s.append("At recursive depth " + level + ":");

		List<DataNodeAndMu> muVector = m.getMu();
		if (!(firstMu - muVector.get(0).Mu < 0.000001))
			s.append("Error in identifying firstMu value.  ");

		double lastMu = muVector.get(dataNodeCount - 1).Mu;

		DecimalFormat df = new DecimalFormat("#.##");
		// String endMuString = (this.probDecrFactor < 0) ? "Unconstrained" : df.format(endMu);
		//
		// s.append("Start mu = " + df.format(firstMu) + "; End mu = " + endMuString + "; Last mu = " +
		// df.format(lastMu)
		// + "; Num mus searched = " + musConsidered);

		return s.toString();
	}

	private String generateMuMatrix() {
		StringBuilder s = new StringBuilder();

		DecimalFormat df = new DecimalFormat("#.##");

		for (int i = 0; i < this.modelNodes.length; i++) {
			BPMathModelNode m = this.modelNodes[i];
			if (m.getMu().get(0).Mu < m.getMu().get(dataNodeCount - 1).Mu)
				s.append("\n>>>>> Error in sorting Mu vector.  ");

			s.append("\n" + m.getPrintableId() + " mus:\n");
			for (DataNodeAndMu nam : m.getMu())
				s.append(df.format(nam.Mu) + "  ");
			s.append("\n");

			s.append("Mismatches:\n");
			for (DataNodeAndMu nam : m.getMu())
				s.append(df.format(mismatches.getNodeMismatch(m.getId(), nam.DataNode)) + "  ");
			s.append("\n");

			int topN = 3;
			if (m.getMu().size() < 3) {
				topN = m.getMu().size();
			}
			s.append("Top " + topN + " data nodes: ");

			for (int j = 0; j < topN; j++) {
				int dNodeId = m.getMu().get(j).DataNode;
				s.append(this.DataNodeToID.get(dNodeId));
				if (j < 2)
					s.append(", ");
			}
			s.append("\n");
		}

		return s.toString();
	}

	public boolean generateSamples() {

		BPMathModelNode mNode1 = this.modelNodes[0];
		double maxMu = mNode1.getMu().get(0).Mu;

		// Replacing probDecrFactor with sliderMismatch
		// // Mu is a ln(prob), so mu is < 0.
		// // Stop this type of search when mu < ln(probDecrFactor * prob). This means that
		// // the probability that the node is part of a match has decreased by probDecrFactor.
		// double endMu;
		// if (probDecrFactor <= 0)
		// endMu = -Float.MAX_VALUE;
		// else
		// endMu = maxMu + Math.log(probDecrFactor);

		// double mu = maxMu;
		int dataNode1Idx = 0;
		int dNode1Id = mNode1.getMu().get(dataNode1Idx).DataNode;
		int maxSearchStepsPerRec = 0;
		double mismatch = this.mismatches.getNodeMismatch(mNode1.getId(), dNode1Id);
		// mLogger.info("Max Top Level Iterations: " + maxTopLevelIterations);

		// Replacing probDecrFactor version with the slider version.
		// while (dataNode1Idx < maxTopLevelIterations && this.samples.size() < maxSamplesToGenerate && mu > endMu) {

		while (dataNode1Idx < maxTopLevelIterations && this.samples.size() < maxSamplesToGenerate && mismatch <= this.maxMismatch) {

			// mLogger.info("Iteration: " + dataNode1Idx);

			// mLogger.info("Mapping model node 0 to data node " + dNode1Id);
			HashMap<Integer, Integer> modelNodeToDataNode = new HashMap<Integer, Integer>();
			modelNodeToDataNode.put(mNode1.getId(), dNode1Id);

			// mLogger.info("ModelNodeId = " + mNode1.getId() + ", DataNodeId = " + dNode1Id);

			HashMap<String, RelationshipForSample> relationshipsForSample = new HashMap<String, RelationshipForSample>();

			int numSearchSteps = getConnectedSampleRec(modelNodeToDataNode, 1,
					(double) this.mismatches.getNodeMismatch(mNode1.getId(), dNode1Id), 1, relationshipsForSample);
			if (numSearchSteps > maxSearchStepsPerRec)
				maxSearchStepsPerRec = numSearchSteps;

			dataNode1Idx++;
			if (dataNode1Idx < maxTopLevelIterations) {
				dNode1Id = mNode1.getMu().get(dataNode1Idx).DataNode;
				// mu = mNode1.getMu().get(dataNode1Idx).Mu;
				mismatch = this.mismatches.getNodeMismatch(mNode1.getId(), dNode1Id);
			}
			firstTopLevelIteration = false;
		}

		if (loggingOn) {
			// String topLevelMsg = generateRecLevelLog(0, mNode1, maxMu, endMu, dataNode1Idx);
			// this.recLevelLog.put(0, topLevelMsg);

			StringBuilder s = new StringBuilder("\n");
			for (Integer recLevel : this.recLevelLog.keySet())
				s.append("\n" + this.recLevelLog.get(recLevel) + "\n");
			// mLogger.info(s);
			// if (verbose)
			// mLogger.info(generateMuMatrix());
			// mLogger.info("MaxSteps per top level iteration = " + maxSearchStepsPerRec);
			// mLogger.info("Samples generated: " + samples.size());
		}

		// did we reach the max sample size? return true, otherwise, false
		if (this.samples.size() >= maxSamplesToGenerate) {
			return true;
		} else {
			return false;
		}
	}

	private int getConnectedSampleRec(HashMap<Integer, Integer> modelNodeToDataNode, int modelIdx, double totalMismatch,
			int numSearchSteps, HashMap<String, RelationshipForSample> relationshipsForSample) {
		if (totalMismatch > maxMismatch || this.samples.size() >= this.maxSamplesToGenerate)
			return numSearchSteps;

		numSearchSteps++;
		BPMathModelNode currModelNode = this.modelNodes[modelIdx];
		int modelId = currModelNode.getId();
		List<DataNodeAndMu> dataNodeAndMu = currModelNode.getMu();

		// Replaced by sliderMismatch
		// // Mu is a ln(prob), so mu is < 0.
		// // Stop this type of search when mu < ln(probDecrFactor * prob). This means that
		// // the probability that the node is part of a match has decreased by probDecrFactor.
		// double endMu;
		// if (probDecrFactor <= 0)
		// endMu = -Float.MAX_VALUE;
		// else
		// endMu = dataNodeAndMu.get(0).Mu + Math.log(probDecrFactor);

		int numBranchesSearched = 0;

		// mLogger.info("Starting data node iteration");
		int i;
		for (i = 0; i < this.dataNodeCount; i++) {

			// Replaced by SliderMM
			// if (this.samples.size() >= maxSamplesToGenerate || dataNodeAndMu.get(i).Mu < endMu
			// || numBranchesSearched > maxBranchingFactor || i >= maxRecLevelIterations) {
			if (this.samples.size() >= maxSamplesToGenerate || numBranchesSearched > maxBranchingFactor
					|| i >= maxRecLevelIterations) {
				break;
			}
			// if (numSearched > 8) // MW ?
			// mLogger.info("Encountered node with more than 8 neighbors.");

			int dataId = dataNodeAndMu.get(i).DataNode;
			if (modelNodeToDataNode.containsValue(dataId)) {
				// mLogger.info("Skipping data node with ID: " + dataId);
				continue;
			}

			float newNodeMM = this.mismatches.getNodeMismatch(modelId, dataId);
			if (newNodeMM + totalMismatch > maxMismatch)
				continue;

			// Make sure dataNodeId is not in an equivalence set.
			boolean equivSetOverlap = false;
			for (EquivalenceSet eSet : this.equivalentNodeSets)
				if (eSet.getModelNodeIds().contains(currModelNode.getId()) && eSet.containsDataNodeIds()
						&& eSet.getDataNodeIds().contains(dataId)) {
					equivSetOverlap = true;
					break;
				}
			if (equivSetOverlap)
				continue;

			// if (modelIdx <= 2) // MW why 2?
			// mLogger.info("   Mapping model node " + modelIdx + " to data node " + dataId);

			boolean relationsExist = true;
			double relationMismatch = 0.0;

			for (BPMathModelRelation r : this.relationshipConstraints.get(modelId)) {
				int mSourceId = r.getBaseRelation().getFromNode();
				int mDestId = r.getBaseRelation().getToNode();

				int dSourceId, dDestId;
				if (modelId == mSourceId) {
					dSourceId = dataId;
					dDestId = modelNodeToDataNode.get(mDestId);
				} else {
					dSourceId = modelNodeToDataNode.get(mSourceId);
					dDestId = dataId;
				}

				// m, k, i
				String mkiKey = mSourceId + "," + mDestId + "," + dSourceId; // dDest is below
				if (!mismatches.containsMKIKey(mkiKey, MismatchRelationshipType.Successor)) {
					// mLogger.info("Failed on relationship: " + r.getBaseRelation().getFromNode() + " " + r.getType() +
					// " " +
					// r.getBaseRelation().getToNode());
					relationsExist = false;
					break;
				}

				// m, k, i, j
				if (!mismatches.containsMKIJKey(mkiKey, dDestId, MismatchRelationshipType.Successor)) {
					relationsExist = false;
					break;
				}

				// MW force perfect (0) relation matches for now; this shows much better results in the Oculus UI
				// MW remove this now 10/25/11
				// if (successorRelationMismatch.get(mkiKey).get(dDestId) > 0) {
				// relationsExist = false;
				// break;
				// }

				relationMismatch += mismatches.getLowestMismatch(mkiKey, dDestId, MismatchRelationshipType.Successor);
				if (relationMismatch + newNodeMM + totalMismatch > maxMismatch) {
					relationsExist = false;
					break;
				}

				relationshipsForSample.put("" + r.getBaseRelation().getFromNode() + "|" + r.getBaseRelation().getToNode(),
						mismatches.getLowestMismatchRelationshipID(mkiKey, dDestId, MismatchRelationshipType.Successor));
			}

			if (!relationsExist) {
				// mLogger.info(">> Invalid relationships.");
				continue;
			}

			// mLogger.info("   Mapping model node " + modelIdx + " to data node " + dataId);

			numBranchesSearched++;
			double newMismatch = newNodeMM + relationMismatch;
			modelNodeToDataNode.put(modelId, dataId);

			// If you have a complete sample, calculate its score and save it.
			if (modelIdx == this.modelNodes.length - 1) {
				HashMap<String, Integer> dataIDToDataNodeIDForSample = new HashMap<String, Integer>();
				for (int dataNode : modelNodeToDataNode.values()) {
					dataIDToDataNodeIDForSample.put(DataNodeToID.get(dataNode), dataNode);
				}
				Sample sample = new Sample(modelNodeToDataNode, dataIDToDataNodeIDForSample, totalMismatch + newMismatch,
						relationshipsForSample);

				// Added these two lines to replace samplePassesConstraints logic
				this.samples.add(sample);
				logValuesForEquivalentSets(modelNodeToDataNode);

				// boolean samplePassesConstraints = AttributeConstraints.checkAttributeConstraints(sample,
				// this.distinctAttributeConstraints, this.mismatches);
				//
				// if (samplePassesConstraints) {
				// if (seenGDIDs.size() > 0) {
				// boolean atLeastOneGDIDMatches = false;
				// for (String GDID : sample.getGDIDs()) {
				// if (seenGDIDs.contains(GDID)) {
				// atLeastOneGDIDMatches = true;
				// break;
				// }
				// }
				// if (atLeastOneGDIDMatches) {
				// this.samples.add(sample);
				// logValuesForEquivalentSets(modelNodeToDataNode);
				// }
				// } else {
				// this.samples.add(sample);
				// logValuesForEquivalentSets(modelNodeToDataNode);
				// }
				// }

				// StringBuilder s = new StringBuilder();
				// for (ModelNode n : this.modelNodes)
				// s.append(n.getGuardDogID() + " -> " + sample.getModelNodeToDataNode().get(n.getId()) + "\n");
				// mLogger.info(s);
				// mLogger.info("Equiv Set data:" + this.equivalentNodeSets.get(0).getDataNodeIds().toString());

				// mLogger.info("Created sample");
			} else {
				if (this.samples.size() >= maxSamplesToGenerate) {
					return numSearchSteps;
				}
				// Otherwise, continue recursion to add more nodes to the sample
				numSearchSteps = getConnectedSampleRec(modelNodeToDataNode, modelIdx + 1, totalMismatch + newMismatch,
						numSearchSteps, relationshipsForSample);
			}

			// Remove the modelId when you're done so that it won't interfere with
			// detecting multiple instances of the same node when creating more samples.
			modelNodeToDataNode.remove(modelId);

			String sModelBar = modelId + "|";
			// remove corresponding relationshipIDs
			ArrayList<String> remove = new ArrayList<String>();
			for (String modelNodeFromToRelationship : relationshipsForSample.keySet()) {
				if (modelNodeFromToRelationship.startsWith(sModelBar)) {
					remove.add(modelNodeFromToRelationship);
				}
			}
			for (String removeString : remove) {
				relationshipsForSample.remove(removeString);
			}
		}

		// if (this.firstTopLevelIteration && !this.recLevelLog.containsKey(modelIdx))
		// this.recLevelLog.put(modelIdx, generateRecLevelLog(modelIdx, currModelNode, dataNodeAndMu.get(0).Mu, endMu,
		// i));

		// If all of the modelNodes in an equivalent set have already been processed, remove the
		// corresponding dataNode information
		for (EquivalenceSet eSet : this.equivalentNodeSets)
			if (Collections.disjoint(modelNodeToDataNode.keySet(), eSet.getModelNodeIds())) {
				eSet.clearDataNodeIds();
				// this.mLogger.info("Clearing equivalence set info...\n");
			}

		// mLogger.info("Data node iteration done");
		return numSearchSteps;
	}

	private void logValuesForEquivalentSets(HashMap<Integer, Integer> modelNodeToDataNode) {
		// If there are sets of equivalent model nodes, log the data nodes.
		for (EquivalenceSet e : this.equivalentNodeSets)
			for (Integer eModelId : e.getModelNodeIds())
				e.addDataNodeId(modelNodeToDataNode.get(eModelId));
	}

	/**
	 * Used to store equivalent sets of model nodes to ensure that sampling does not return multiple instances of the
	 * same pattern.
	 * 
	 * @author jroberts
	 */
	private class EquivalenceSet {
		private ArrayList<Integer> modelNodeIds;
		private HashSet<Integer> dataNodeIds;

		public EquivalenceSet(ArrayList<Integer> modelNodeIds) {
			this.modelNodeIds = modelNodeIds;
			this.dataNodeIds = new HashSet<Integer>();
		}

		public void addDataNodeId(int dataNodeId) {
			this.dataNodeIds.add(dataNodeId);
		}

		public void clearDataNodeIds() {
			this.dataNodeIds.clear();
		}

		public HashSet<Integer> getDataNodeIds() {
			return this.dataNodeIds;
		}

		public ArrayList<Integer> getModelNodeIds() {
			return this.modelNodeIds;
		}

		public boolean containsDataNodeIds() {
			return !this.dataNodeIds.isEmpty();
		}
	}
}