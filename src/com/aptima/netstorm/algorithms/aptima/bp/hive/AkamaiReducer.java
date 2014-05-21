package com.aptima.netstorm.algorithms.aptima.bp.hive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.hadoop.hive.contrib.mr.Output;
import org.apache.hadoop.hive.contrib.mr.Reducer;

import com.aptima.netstorm.algorithms.aptima.bp.BPMathModelNode;
import com.aptima.netstorm.algorithms.aptima.bp.BPMathModelRelation;
import com.aptima.netstorm.algorithms.aptima.bp.BeliefPropagationMath;
import com.aptima.netstorm.algorithms.aptima.bp.MismatchValues;
import com.aptima.netstorm.algorithms.aptima.bp.sampling.DFSSampler;
import com.aptima.netstorm.algorithms.aptima.bp.sampling.Sample;

public class AkamaiReducer extends AkamaiMR implements Reducer {

	private static MismatchValues mismatches;

	private int dataNodeCount = 0; // note, this won't be unique across all reducers (they run in parallel) unless we
									// have
									// just 1
	private int dataRelationCount = 0;

	private HashMap<String, Integer> IDToDataNode;
	private HashMap<Integer, String> DataNodeToID;

	private int rowCount = 0;
	private int maxRowCount = 0;

	public AkamaiReducer(String[] args) {

		super(args);

		mismatches = new MismatchValues();

		IDToDataNode = new HashMap<String, Integer>(); // maps String ID representations from bitcoin to local data node
														// IDs

		DataNodeToID = new HashMap<Integer, String>(); // maps local data node ID to bitcoin ID representations
	}

	public void reduce(String key, Iterator<String[]> values, Output output) throws Exception {

		// key is time window?
		String[] mapOutput;
		System.err.println("Key: " + key);

		// init
		mismatches = new MismatchValues();
		IDToDataNode = new HashMap<String, Integer>(); // maps String ID representations from bitcoin to local
														// data node
														// IDs
		DataNodeToID = new HashMap<Integer, String>(); // maps local data node ID to bitcoin ID representations
		dataNodeCount = 0;
		dataRelationCount = 0;
		rowCount = 0;

		while (values.hasNext()) {
			mapOutput = values.next();

			if (rowCount < maxRowCount) {
				for (int i = 0; i < mapOutput.length; i++) {
					System.err.println(mapOutput[i]);
				}
				rowCount++;

				System.err.println("");
			}

			// isLink, sourceID, destID, mismatchVector, timeWindow
			Boolean isLink = Boolean.parseBoolean(mapOutput[1]);
			String sourceID = mapOutput[2];
			TreeMap<Integer, Float> mismatchVector = stringToMismatchVector(mapOutput[4]);

			if (!isLink) {

				if (!IDToDataNode.containsKey(sourceID)) { // map i -> data node

					// System.err.println("Node: " + sourceID);

					for (int i = 0; i < modelNodes.length; i++) {
						// i, j, mismatch
						if (mismatchVector.containsKey(i)) {
							mismatches.addNodeMismatch(i, dataNodeCount, mismatchVector.get(i));
						}
					}

					IDToDataNode.put(sourceID, dataNodeCount);
					DataNodeToID.put(dataNodeCount, sourceID);
					dataNodeCount++;
				}
			} else {

				String destID = mapOutput[3];

				// edge contains nodes that weren't seen, skip
				if (!IDToDataNode.containsKey(sourceID) || !IDToDataNode.containsKey(destID)) {
					// System.err.println("reporter:counter:APTIMA,NODE_FILTERED,1");
					continue;
				}

				// System.err.println("Edge: " + sourceID + ", " + destID);

				String relID = sourceID + "," + destID + ", " + dataRelationCount;

				for (int i = 0; i < modelRelations.length; i++) {
					// m,k, i, j, mismatch
					if (mismatchVector.containsKey(i)) {
						String mk = modelRelations[i].getFromNode() + "," + modelRelations[i].getToNode();
						mismatches.addLinkMismatch(mk, IDToDataNode.get(sourceID), IDToDataNode.get(destID),
								mismatchVector.get(i), relID);
					}
				}

				dataRelationCount++;
			}
		}

		// do sampling on LAST BLOCK
		doSampling(key, output);
	}

	// after init and node/link processing, the BP and sampling step is done
	private final void doSampling(String key, Output output) {

		System.err.println("Sampling Key: " + key);
		System.err.println("Date Nodes: " + dataNodeCount);
		System.err.println("Date Relations: " + dataRelationCount);

		// did we not read any nodes?
		if (dataNodeCount == 0 || dataRelationCount == 0) {
			return;
		}

		System.err.println("BP Math Init");

		// init BP math constructs
		BPMathModelNode[] bpMathModelNodes = new BPMathModelNode[modelNodes.length];
		for (int i = 0; i < modelNodes.length; i++) {
			bpMathModelNodes[i] = new BPMathModelNode(dataNodeCount, modelNodes[i]);
		}

		BPMathModelRelation[] bpMathModelRelations = new BPMathModelRelation[modelRelations.length];
		for (int i = 0; i < modelRelations.length; i++) {
			bpMathModelRelations[i] = new BPMathModelRelation(modelRelations[i]);
		}

		// Simple Size Filter: Does our network contain at least the number of model nodes we want to match
		// to?
		// This is to enable a 1-1 mapping between model and data node
		// Relax this for missing links

		if (modelNodes.length <= dataNodeCount) {

			System.err.println("BP Math");

			int localExactMatches = 0, localInExactMatches = 0;
			int numberOfIterations = modelNodes.length;
			// Update the modelNode and modelRelation mu vectors.
			BeliefPropagationMath.performBeliefPropagation(bpMathModelNodes, bpMathModelRelations, dataNodeCount, mismatches,
					numberOfIterations);

			int maxSamplesToGenerate = 50;

			// Subnetwork search tuning. proDecrFactor uses % of maximum mu to limit how deep it samples (0.9), max
			// branching, top level, and recLevel control deeper branching behavior (10,000)
			// DFS parameter - searches from the maximum mu value to a mu
			// value with a node probability of probDecrFactor * maxValue
			double probDecrFactor = 0.9;
			// DFS parameter - max branching factor in the DFS. Only used if
			// there are no exact matches and the mu values are not
			// nformative. Controls how many branches are explored during
			// the DFS recursion.
			int maxBranchingFactor = 100;
			// DFS parameter - number of distinct places in the network that
			// that we search for matches. Controls the worst case behavior
			// if the mu values are not informative (i.e., no exact
			// structural matches)
			int maxTopLevelIterations = 100;
			// DFS parameter - controls how hard you search in one area of
			// the graph for a match. Controls the worst case behavior
			// if the mu values are not informative (i.e., no exact
			// structural matches)
			int maxRecLevelIterations = 100;

			System.err.println("Sampling: " + dataNodeCount);

			DFSSampler dfsSampler = new DFSSampler(maxSamplesToGenerate, dataNodeCount, mismatches, bpMathModelNodes,
					bpMathModelRelations, DataNodeToID, probDecrFactor, maxBranchingFactor, maxTopLevelIterations,
					maxRecLevelIterations, mismatchThreshold);
			dfsSampler.setVerbose(false);
			dfsSampler.setLogging(false);

			boolean maxSamplesReached = dfsSampler.generateSamples();

			System.err.println("Sampling Done");
			// todo: report count

			ArrayList<Sample> newSamples = dfsSampler.getSamples();

			System.err.println("Sampling Read: " + newSamples.size());

			if (newSamples.size() > 0) {
				for (int i = 0; i < newSamples.size(); i++) {
					if (newSamples.get(i).getMismatch() == 0.0) {
						localExactMatches++;
					} else {
						localExactMatches++;
					}

					// output samples as result_num, modelID, dataID, mismatch
					Sample sample = newSamples.get(i);

					String resultID = UUID.randomUUID().toString();

					HashMap<Integer, Integer> mtoD = sample.getModelNodeToDataNode();
					for (Integer modelNode : mtoD.keySet()) {
						String[] outputBuffer = new String[5];
						outputBuffer[0] = "" + resultID;
						outputBuffer[1] = "" + modelNode;
						outputBuffer[2] = "" + sample.getIDWithDataNodeID(mtoD.get(modelNode));
						outputBuffer[3] = "" + sample.getMismatch();
						outputBuffer[4] = "";

						try {
							output.collect(outputBuffer);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
				// System.err.println("reporter:counter:APTIMA,NODE_EXACT_MATCHES,1");
				// System.err.println("reporter:counter:APTIMA,NODE_INEXACT_MATCHES,1");
			}
		}
	}
}
