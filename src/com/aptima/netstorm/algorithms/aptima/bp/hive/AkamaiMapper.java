package com.aptima.netstorm.algorithms.aptima.bp.hive;

import java.util.HashSet;
import java.util.TreeMap;

import org.apache.hadoop.hive.contrib.mr.Mapper;
import org.apache.hadoop.hive.contrib.mr.Output;

import com.aptima.netstorm.algorithms.aptima.bp.mismatch.NormalizedMismatchCalculator;
import com.aptima.netstorm.algorithms.aptima.bp.network.DataAttributeSet;

/**Class forms the mapper for the Akamai example
 * 
 * @author Aptima
 *
 */
public class AkamaiMapper extends AkamaiMR implements Mapper {

	private static NormalizedMismatchCalculator mismatchCalculator;

	private static String[] outputMapBuffer;
	private static String[] nodeRecord;

	private static int rowCount = 0;
	private static int maxRowCount = 0;

	private static HashSet<String> seenNodeKeys = new HashSet<String>();
	private static HashSet<String> seenEdgeKeys = new HashSet<String>();

	private static int count = 0;
	private static int countLimit = 100000; // limit each mapper to 100K to start

	public AkamaiMapper(String[] args) {

		super(args);
		mismatchCalculator = new NormalizedMismatchCalculator(modelNodes, modelRelations);
		outputMapBuffer = new String[5];
		nodeRecord = new String[5];
	}

	private static String src, dest, region, ut, timeKey, nodeKey, edgeKey, latency;

	public static String COL_AKAMAI_LATENCY = "lat";
	private static DataAttributeSet dataAttributes;
	private static double nodeMismatch, linkMismatch;
	private static TreeMap<Integer, Float> idToMismatchMap;

	// Read and filter tab-delimited rows from Akamai table into dataNodes and dataLinks
	public void map(String[] record, Output output) throws Exception {

		// Load attributes from a single node/relation into a DataAttributeSet object.
		src = record[0];
		region = record[2];
		ut = record[3]; // unix time
		timeKey = region + "_" + ut;
		latency = record[4];

		nodeKey = timeKey + "_" + src;
		if (count < countLimit && !seenNodeKeys.contains(nodeKey)) {
			dataAttributes = new DataAttributeSet();
			dataAttributes.addAttribute(COL_AKAMAI_LATENCY, latency);
			filterNodeBasedOnMismatch(timeKey, nodeKey, src, dataAttributes, output);
		}

		dest = record[1];
		edgeKey = timeKey + "_" + src + "_" + dest;
		if (count < countLimit && !seenEdgeKeys.contains(edgeKey)) {
			dataAttributes = new DataAttributeSet();
			dataAttributes.addAttribute(COL_AKAMAI_LATENCY, latency);
			filterRelationBasedOnMismatch(timeKey, edgeKey, src, dest, dataAttributes, output);
		}
	}

	public static void filterNodeBasedOnMismatch(String timeKey, String nodeKey, String src, DataAttributeSet dataAttributes,
			Output output) {

		idToMismatchMap = new TreeMap<Integer, Float>();

		for (int i = 0; i < modelNodes.length; i++) {
			nodeMismatch = mismatchCalculator.ComputeMismatch(modelNodes[i], dataAttributes);
			if (nodeMismatch <= mismatchThreshold)
				idToMismatchMap.put(i, (float) nodeMismatch);
		}

		if (!idToMismatchMap.isEmpty()) {

			seenNodeKeys.add(nodeKey);
			count++;

			nodeRecord[0] = "false";
			nodeRecord[1] = src;
			nodeRecord[2] = src;
			nodeRecord[3] = mismatchVectorToString(idToMismatchMap);
			nodeRecord[4] = timeKey;

			try {
				output.collect(nodeRecord);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void filterRelationBasedOnMismatch(String timeKey, String edgeKey, String sourceID, String destID,
			DataAttributeSet dataAttributes, Output output) {

		idToMismatchMap = new TreeMap<Integer, Float>();

		for (int i = 0; i < modelRelations.length; i++) {
			linkMismatch = mismatchCalculator.ComputeMismatch(modelRelations[i], dataAttributes);
			if (rowCount < maxRowCount) {
				System.err.println("Model relation: " + i + " Data Relation: " + sourceID + "," + destID + " " + linkMismatch);
			}
			if (linkMismatch <= mismatchThreshold)
				idToMismatchMap.put(i, (float) linkMismatch);
		}

		if (!idToMismatchMap.isEmpty()) {

			seenEdgeKeys.add(edgeKey);
			count++;

			// emit link to time period
			outputMapBuffer[0] = "true";
			outputMapBuffer[1] = sourceID;
			outputMapBuffer[2] = destID;
			outputMapBuffer[3] = mismatchVectorToString(idToMismatchMap);
			outputMapBuffer[4] = timeKey;

			try {
				output.collect(outputMapBuffer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
