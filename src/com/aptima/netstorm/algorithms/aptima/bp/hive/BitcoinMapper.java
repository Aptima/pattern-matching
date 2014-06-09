package com.aptima.netstorm.algorithms.aptima.bp.hive;

import java.util.GregorianCalendar;
import java.util.TreeMap;

import org.apache.hadoop.hive.contrib.mr.Mapper;
import org.apache.hadoop.hive.contrib.mr.Output;

import com.aptima.netstorm.algorithms.aptima.CalendarHelper;
import com.aptima.netstorm.algorithms.aptima.bp.ModelGraph;
import com.aptima.netstorm.algorithms.aptima.bp.mismatch.NormalizedMismatchCalculator;
import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelGraph;
import com.aptima.netstorm.algorithms.aptima.bp.network.DataAttributeSet;

public class BitcoinMapper extends MRBase implements Mapper, ModelGraph { // BitcoinMR 

	// BITCOIN columns
	public static String COL_BITCOIN_AMT = "amt";
	public static String COL_BITCOIN_IN_AMT = "inAmt";
	public static String COL_BITCOIN_OUT_AMT = "outAmt";
	public static String COL_BITCOIN_IN_DEGREE = "inDegree";
	public static String COL_BITCOIN_OUT_DEGREE = "outDegree";

	private static NormalizedMismatchCalculator mismatchCalculator;

	private static String[] outputMapBuffer;
	private static String[] nodeRecord;

	// private static int weekBinDivisor = 1; // 1 for week/ 52 for year, 12 for month
	private static boolean lastNodeSent = false;

	private static int rowCount = 0;
	private static int maxRowCount = 10;

	public BitcoinMapper() {
		System.err.println("Creating BitcoinMapper()");
		//mismatchCalculator = new NormalizedMismatchCalculator(modelNodes, modelRelations);
		outputMapBuffer = new String[6];
		nodeRecord = new String[4];

		// min/max dtg
		// 2009-01-03 12:15:05
		// 2013-04-10 14:22:50
		// Jan is month 0
		CalendarHelper.startDateForBinning = new GregorianCalendar(2009, 0, 3);
	}

	// Read and filter tab-delimited rows from Bitcoin table into dataNodes and dataLinks
	public void map(String[] record, Output output) {
		try {
		/*
		 * null as id, node as source_edge_ID, null as destination_edge_ID, null as dtg, null as amount, in_degree,
		 * out_degree, node, incoming_amount, outgoing_amount
		 */

		if (rowCount < maxRowCount) {
			System.err.println("Reading row");
			if (record == null) {
				return;
			} else {
				for (int i = 0; i < record.length; i++) {
					System.err.println(record[i]);
				}
			}
			rowCount++;
		}

		// Load attributes from a single node/relation into a DataAttributeSet object.
		DataAttributeSet dataAttributes = new DataAttributeSet();

		// check src/dest to determinte whether edge or node
		boolean isDataNode = !record[7].equals("\\N"); // Hive specific!

		// Calculate mismatch, filter nodes/relations with no mismatches under threshold
		// Print tab-delimited results for un-filtered values. AptimaNodeMM and RelationMM might help here.
		if (isDataNode) {

			lastNodeSent = false; // reset before filtering
			dataAttributes.addAttribute(COL_BITCOIN_IN_DEGREE, record[5]);
			dataAttributes.addAttribute(COL_BITCOIN_OUT_DEGREE, record[6]);
			dataAttributes.addAttribute(COL_BITCOIN_IN_AMT, record[8]);
			dataAttributes.addAttribute(COL_BITCOIN_OUT_AMT, record[9]);

			filterNodeBasedOnMismatch(record[7], dataAttributes);
		} else { // data relation

			dataAttributes.addAttribute(COL_BITCOIN_AMT, record[4]);

			filterRelationBasedOnMismatch(record[1], record[2], record[4], dataAttributes, output, record[3]);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void filterNodeBasedOnMismatch(String nodeID, DataAttributeSet dataAttributes) {
		double nodeMismatch;
		TreeMap<Integer, Float> idToMismatchMap = new TreeMap<Integer, Float>();

		for (int i = 0; i < modelNodes.length; i++) {
			nodeMismatch = mismatchCalculator.ComputeMismatch(modelNodes[i], dataAttributes);
			if (rowCount < maxRowCount) {
				System.err.println("Model Node: " + i + " Data Node: " + nodeID + " " + nodeMismatch);
			}
			if (nodeMismatch <= mismatchThreshold)
				idToMismatchMap.put(i, (float) nodeMismatch);
		}

		if (!idToMismatchMap.isEmpty()) {
			// TODO: Print information for node
			// Print isLink, nodeID, nodeID, mismatchVector, timeWindow

			// save node and mark as passed mismatch
			lastNodeSent = true;

			nodeRecord[0] = "false";
			nodeRecord[1] = nodeID;
			nodeRecord[2] = nodeID;
			nodeRecord[3] = mismatchVectorToString(idToMismatchMap);
		}
	}

	public static void filterRelationBasedOnMismatch(String sourceID, String destID, String amt, DataAttributeSet dataAttributes,
			Output output, String dateTime) {
		double linkMismatch;
		TreeMap<Integer, Float> idToMismatchMap = new TreeMap<Integer, Float>();

		for (int i = 0; i < modelRelations.length; i++) {
			linkMismatch = mismatchCalculator.ComputeMismatch(modelRelations[i], dataAttributes);
			if (rowCount < maxRowCount) {
				System.err.println("Model relation: " + i + " Data Relation: " + sourceID + "," + destID + " " + linkMismatch);
			}
			if (linkMismatch <= mismatchThreshold)
				idToMismatchMap.put(i, (float) linkMismatch);
		}

		if (!idToMismatchMap.isEmpty()) {
			// TODO: Print information for link
			// Print isLink, sourceID, destID, mismatchVector, timeWindow

			String weekBin = "" + CalendarHelper.weekFromBinStart(CalendarHelper.parseBitcoin(dateTime));

			if(weekBin.equals("-1")) {
				System.err.println("Error DT: " + dateTime);
				//System.out.println("reporter:counter:APTIMA,TIME_PARSE_ERROR,1");
			}
			
			if (lastNodeSent) {
				// emit node to time period
				outputMapBuffer[0] = nodeRecord[0];
				outputMapBuffer[1] = nodeRecord[1];
				outputMapBuffer[2] = nodeRecord[2];
				outputMapBuffer[3] = nodeRecord[3];
				outputMapBuffer[4] = weekBin;
				outputMapBuffer[5] = "";
				
				//System.err.println("reporter:counter:APTIMA,NODE_EMIT,1");

				try {
					output.collect(outputMapBuffer);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// emit link to time period
			outputMapBuffer[0] = "true";
			outputMapBuffer[1] = sourceID;
			outputMapBuffer[2] = destID;
			outputMapBuffer[3] = mismatchVectorToString(idToMismatchMap);
			outputMapBuffer[4] = weekBin;
			outputMapBuffer[5] = amt;
			
			//System.out.println("reporter:counter:APTIMA,REL_EMIT,1");
			
			try {
				output.collect(outputMapBuffer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	

	public void input(AttributedModelGraph graph) {
		modelNodes = graph.getNodes();
		modelRelations = graph.getRelations();
		
		// This used to be in constructor. Now we read model graph from file instead of hard coding it.
		mismatchCalculator = new NormalizedMismatchCalculator(modelNodes, modelRelations);
		
	}
}
