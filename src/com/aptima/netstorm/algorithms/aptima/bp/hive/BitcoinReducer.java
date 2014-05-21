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
import com.aptima.netstorm.algorithms.aptima.bp.ModelGraph;
import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelGraph;
import com.aptima.netstorm.algorithms.aptima.bp.sampling.DFSSampler;
import com.aptima.netstorm.algorithms.aptima.bp.sampling.RelationshipForSample;
import com.aptima.netstorm.algorithms.aptima.bp.sampling.RelationshipIDValue;
import com.aptima.netstorm.algorithms.aptima.bp.sampling.Sample;

public class BitcoinReducer extends MRBase implements Reducer, ModelGraph {

	private static MismatchValues mismatches;

	private int dataNodeCount = 0; // note, this won't be unique across all reducers (they run in parallel) unless we
									// have
									// just 1
	private int dataRelationCount = 0;

	// private int maxSamplesToReturnInResponse = 100;

	private HashMap<String, Integer> IDToDataNode;
	private HashMap<Integer, String> DataNodeToID;

	private int rowCount = 0;
	private int maxRowCount = 5;

	// private static String currentTimeReduce = "";

	public BitcoinReducer() {

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

			// String rowTime = mapOutput[0];
			// if (!rowTime.equals(currentTimeReduce)) {

			// System.err.println("Switching Time To: " + rowTime + " from " + currentTimeReduce);
			// currentTimeReduce = rowTime;
			// doSampling(key, output);

			// }

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

				String amount = mapOutput[5];

				// System.err.println("Edge: " + sourceID + ", " + destID);

				String relID = sourceID + "," + destID + ", " + dataRelationCount + "," + amount;

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

			int maxSamplesToGenerate = 100;

			// Subnetwork search tuning. proDecrFactor uses % of maximum mu to limit how deep it samples (0.9), max
			// branching, top level, and recLevel control deeper branching behavior (10,000)
			// DFS parameter - searches from the maximum mu value to a mu
			// value with a node probability of probDecrFactor * maxValue
			double probDecrFactor = 0.9;
			// DFS parameter - max branching factor in the DFS. Only used if
			// there are no exact matches and the mu values are not
			// nformative. Controls how many branches are explored during
			// the DFS recursion.
			int maxBranchingFactor = 1000;
			// DFS parameter - number of distinct places in the network that
			// that we search for matches. Controls the worst case behavior
			// if the mu values are not informative (i.e., no exact
			// structural matches)
			int maxTopLevelIterations = 1000;
			// DFS parameter - controls how hard you search in one area of
			// the graph for a match. Controls the worst case behavior
			// if the mu values are not informative (i.e., no exact
			// structural matches)
			int maxRecLevelIterations = 1000;

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

					HashMap<String, ArrayList<Double>> modelNodeIncomingAmts = new HashMap<String, ArrayList<Double>>();
					HashMap<String, ArrayList<RelationshipIDValue>> modelNodeOutgoingAmts = new HashMap<String, ArrayList<RelationshipIDValue>>();

					// custom logic - post select samples
					HashMap<String, RelationshipForSample> sampleRels = sample.getRelationshipIDs();
					for (String relKey : sampleRels.keySet()) {
						RelationshipForSample rfs = sampleRels.get(relKey);

						// unpack amount
						// todo make this more flexible!
						String relID = rfs.relationshipID;
						int lIndex = relID.lastIndexOf(",");
						String amt = relID.substring(lIndex + 1, relID.length());
						Double dAmt = Double.parseDouble(amt);

						int lineIndex = relKey.indexOf("|");
						String m = relKey.substring(0, lineIndex);
						String k = relKey.substring(lineIndex + 1, relKey.length());

						if (!modelNodeIncomingAmts.containsKey(m)) {
							modelNodeIncomingAmts.put(m, new ArrayList<Double>());
						}

						if (!modelNodeIncomingAmts.containsKey(k)) {
							modelNodeIncomingAmts.put(k, new ArrayList<Double>());
						}

						if (!modelNodeOutgoingAmts.containsKey(m)) {
							modelNodeOutgoingAmts.put(m, new ArrayList<RelationshipIDValue>());
						}

						if (!modelNodeOutgoingAmts.containsKey(k)) {
							modelNodeOutgoingAmts.put(k, new ArrayList<RelationshipIDValue>());
						}

						modelNodeIncomingAmts.get(k).add(dAmt);
						modelNodeOutgoingAmts.get(m).add(new RelationshipIDValue(k, dAmt));
					}

					double totalDelta = 0.0;
					// check the middle nodes for low flow
					if (constrainFlow) {
						ArrayList<String> mns = new ArrayList<String>();
						mns.add("1");
						mns.add("2");
						mns.add("3");

						boolean diffFail = false;

						for (String mn : mns) {
							double sumIn = 0;
							for (Double inAmt : modelNodeIncomingAmts.get(mn)) {
								sumIn += inAmt;
							}

							double sumOut = 0;
							for (RelationshipIDValue outAmt : modelNodeOutgoingAmts.get(mn)) {
								sumOut += outAmt.Value;
							}

							double magnitude = sumIn + sumOut;
							double diff = sumIn - sumOut;
							if (diff < 0) {
								diffFail = true;
							}

							double delta = diff / magnitude;
							totalDelta += delta;
						}

						if (diffFail) {
							continue;
						}
					}
					
					if (!constrainFlow || totalDelta < 0.2) { // arbitrary limit, refine

						// for (String modelNode : modelNodeIncomingAmts.keySet()) {
						// System.err.println("Incoming " + modelNode);
						// for (Double val : modelNodeIncomingAmts.get(modelNode)) {
						// System.err.println(val);
						// }
						// }
						//
						// for (String modelNode : modelNodeOutgoingAmts.keySet()) {
						// System.err.println("Outgoing " + modelNode);
						// for (Double val : modelNodeOutgoingAmts.get(modelNode)) {
						// System.err.println(val);
						// }
						// }

						String resultID = UUID.randomUUID().toString();

						HashMap<Integer, Integer> mtoD = sample.getModelNodeToDataNode();
						for (Integer modelNode : mtoD.keySet()) {
							String[] outputBuffer = new String[5];
							outputBuffer[0] = "" + resultID;
							outputBuffer[1] = "" + modelNode;
							outputBuffer[2] = "" + sample.getIDWithDataNodeID(mtoD.get(modelNode));
							outputBuffer[3] = "" + totalDelta;
							outputBuffer[4] = "";

							try {
								output.collect(outputBuffer);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						for (String modelNode : modelNodeIncomingAmts.keySet()) {

							for (Double val : modelNodeIncomingAmts.get(modelNode)) {

								String[] outputBuffer = new String[5];
								outputBuffer[0] = "" + resultID;
								outputBuffer[1] = "" + modelNode;
								outputBuffer[2] = "" + modelNode;
								outputBuffer[3] = "" + val;
								outputBuffer[4] = DIR_IN;

								try {
									output.collect(outputBuffer);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}

						for (String modelNode : modelNodeOutgoingAmts.keySet()) {

							for (RelationshipIDValue val : modelNodeOutgoingAmts.get(modelNode)) {

								String[] outputBuffer = new String[5];
								outputBuffer[0] = "" + resultID;
								outputBuffer[1] = "" + sample.getIDWithDataNodeID(mtoD.get(modelNode));
								outputBuffer[2] = "" + sample.getIDWithDataNodeID(mtoD.get(val.ID));
								outputBuffer[3] = "" + val.Value;
								outputBuffer[4] = DIR_OUT;

								try {
									output.collect(outputBuffer);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				// System.err.println("reporter:counter:APTIMA,NODE_EXACT_MATCHES,1");
				// System.err.println("reporter:counter:APTIMA,NODE_INEXACT_MATCHES,1");
			}
		}
	}

	@Override
	public void input(AttributedModelGraph graph) {
		modelNodes = graph.getNodes();
		modelRelations = graph.getRelations();		
	}
}
