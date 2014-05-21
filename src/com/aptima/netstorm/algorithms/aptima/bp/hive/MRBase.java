package com.aptima.netstorm.algorithms.aptima.bp.hive;

import java.util.TreeMap;

import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelNode;
import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelRelation;

public class MRBase {

	protected static AttributedModelNode[] modelNodes;
	protected static AttributedModelRelation[] modelRelations;

	public static String DIR_IN = "IN";
	public static String DIR_OUT = "OUT";
	public static boolean constrainFlow = false;
	
	// 1.0 = exact match, 0.0 = completely inexact match
	protected static double sliderValue = 1.0;

	protected static double mismatchThreshold = 0.0;
	
	/**
	 * 
	 * @param args
	 */
	public MRBase() {
		
		calcSliderMismatch();
		
		System.err.println("Slider Value: " + sliderValue);
		System.err.println("Mismatch Threshold: " + mismatchThreshold);
	}

	// 0.0 -> 1.0 mismatch limit
	public static void calcSliderMismatch() {

		if (sliderValue == 1.0) {
			mismatchThreshold = 0.0f;
		} else if (sliderValue == 0.0) {
			mismatchThreshold = Float.MAX_VALUE;
		} else {
			mismatchThreshold = (float) (-1 * Math.log(sliderValue));
		}
	}

	protected static String mismatchVectorToString(TreeMap<Integer, Float> idToMismatchMap) {
		// build a string vector of model to data node mismatches
		String mismatchVectorAsString = "";
		for (Integer key : idToMismatchMap.keySet()) {
			if (mismatchVectorAsString.length() > 0) {
				mismatchVectorAsString = mismatchVectorAsString + ",";
			}
			mismatchVectorAsString = mismatchVectorAsString + key + "," + idToMismatchMap.get(key);
		}
		return mismatchVectorAsString;
	}

	protected static TreeMap<Integer, Float> stringToMismatchVector(String mismatchVectorAsString) {
		// rebuild a mismatch vector of model to data node mismatches from the string representation
		TreeMap<Integer, Float> idToMismatchMap = new TreeMap<Integer, Float>();
		String[] split = mismatchVectorAsString.split(",");
		for (int i = 0; i < split.length - 1; i++) {
			idToMismatchMap.put(Integer.parseInt(split[i]), Float.parseFloat(split[i + 1]));
			i++;
		}
		return idToMismatchMap;
	}
}