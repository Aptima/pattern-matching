package com.aptima.netstorm.algorithms.aptima.bp.sampling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

public class Sample {
	private HashMap<Integer, Integer> modelNodeToDataNode;
	private HashMap<Integer, Integer> dataNodeToModelNode;;
	private HashMap<String, Integer> dataIDToDataNodeID;
	private double mismatch;
	private HashMap<String, RelationshipForSample> relationshipIDs;
	private HashMap<String, ArrayList<String>> additionalRelationshipIDs;

	private int mnmrReference;
	private boolean mnmrSet = false;
	private boolean hasAdditionalIDs = false;
	private int additionalIDCount = 0;
	private int ID = -1;

	public Sample() {
	}

	/**
	 * Create sample for a single-node model network
	 */
	public Sample(int modelNodeId, int dataNodeId, String dataId, double mismatch) {
		this.modelNodeToDataNode = new HashMap<Integer, Integer>(1);
		this.dataNodeToModelNode = new HashMap<Integer, Integer>(1);
		this.dataIDToDataNodeID = new HashMap<String, Integer>(1);

		this.modelNodeToDataNode.put(modelNodeId, dataNodeId);
		this.dataNodeToModelNode.put(dataNodeId, modelNodeId);
		this.dataIDToDataNodeID.put(dataId, dataNodeId);
		this.mismatch = mismatch;
		this.relationshipIDs = new HashMap<String, RelationshipForSample>();
		this.additionalRelationshipIDs = new HashMap<String, ArrayList<String>>();
	}

	/**
	 * 
	 * @param modelNodeToDataNode
	 * @param mismatch
	 * @throws IllegalArgumentException
	 *             if all of the data nodes in the sample are not unique.
	 */
	public Sample(HashMap<Integer, Integer> modelNodeToDataNode, HashMap<String, Integer> dataIDToDataNodeID,
			double mismatch, HashMap<String, RelationshipForSample> relationshipIDs) {
		this.dataIDToDataNodeID = new HashMap<String, Integer>(dataIDToDataNodeID.size());

		if (dataIDToDataNodeID != null)
			for (Entry<String, Integer> e : dataIDToDataNodeID.entrySet()) {
				this.dataIDToDataNodeID.put(new String(e.getKey()), new Integer(e.getValue()));
			}

		this.modelNodeToDataNode = new HashMap<Integer, Integer>(modelNodeToDataNode.size());
		this.dataNodeToModelNode = new HashMap<Integer, Integer>(modelNodeToDataNode.size());

		// Performing deep copy just in case
		for (Entry<Integer, Integer> e : modelNodeToDataNode.entrySet()) {
			this.modelNodeToDataNode.put(new Integer(e.getKey()), new Integer(e.getValue()));
			this.dataNodeToModelNode.put(new Integer(e.getValue()), new Integer(e.getKey()));
		}

		HashSet<Integer> dataNodes = new HashSet<Integer>(this.modelNodeToDataNode.values());
		if (dataNodes.size() != this.modelNodeToDataNode.size())
			throw new IllegalArgumentException("Sample contains multiple instances of the same data node.");

		this.relationshipIDs = new HashMap<String, RelationshipForSample>();
		for (Entry<String, RelationshipForSample> e : relationshipIDs.entrySet()) {
			this.relationshipIDs.put(e.getKey(), e.getValue());
		}
		this.additionalRelationshipIDs = new HashMap<String, ArrayList<String>>();

		this.mismatch = mismatch;
	}

	public Sample copy(boolean copyAdditionalIDs) {
		Sample newSample = new Sample();

		newSample.modelNodeToDataNode = new HashMap<Integer, Integer>();
		newSample.dataNodeToModelNode = new HashMap<Integer, Integer>();
		newSample.dataIDToDataNodeID = new HashMap<String, Integer>();
		newSample.relationshipIDs = new HashMap<String, RelationshipForSample>();

		newSample.mismatch = this.mismatch;
		newSample.mnmrReference = this.mnmrReference;
		newSample.mnmrSet = this.mnmrSet;
		newSample.additionalRelationshipIDs = new HashMap<String, ArrayList<String>>();
		newSample.ID = this.ID;

		if (copyAdditionalIDs) {
			newSample.hasAdditionalIDs = this.hasAdditionalIDs;
			newSample.additionalIDCount = this.additionalIDCount;
			copyHashMap(this.additionalRelationshipIDs, newSample.additionalRelationshipIDs);
		}

		copyHashMap(this.dataIDToDataNodeID, newSample.dataIDToDataNodeID);
		copyHashMap(this.dataNodeToModelNode, newSample.dataNodeToModelNode);
		copyHashMap(this.relationshipIDs, newSample.relationshipIDs);
		copyHashMap(this.modelNodeToDataNode, newSample.modelNodeToDataNode);

		return newSample;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public <T1, T2> void copyHashMap(HashMap<T1, T2> source, HashMap<T1, T2> target) {
		for (T1 t1 : source.keySet()) {
			target.put(t1, source.get(t1));
		}
	}

	public int getMNMRReference() {
		return mnmrReference;
	}

	public void setMNMRReference(int mnmrReference) {
		if (mnmrReference == -1) { // special case to mark sample invalid
			this.mnmrReference = mnmrReference;
		} else {
			if (!mnmrSet) {
				this.mnmrReference = mnmrReference;
				mnmrSet = true;
			}
		}
	}

	public HashMap<String, RelationshipForSample> getRelationshipIDs() {
		return relationshipIDs;
	}

	public boolean hasAdditionalIDs() {
		return this.hasAdditionalIDs;
	}

	public HashMap<String, ArrayList<String>> getAdditionalRelationshipIDs() {
		return additionalRelationshipIDs;
	}

	public void addAdditionalRelationshipID(String modelFromBarTo, String arID) {
		if (!additionalRelationshipIDs.containsKey(modelFromBarTo)) {
			additionalRelationshipIDs.put(modelFromBarTo, new ArrayList<String>());
			hasAdditionalIDs = true;
		}
		additionalRelationshipIDs.get(modelFromBarTo).add(arID);
		additionalIDCount++;
	}

	public int relationshipsSize() {
		return this.relationshipIDs.keySet().size() + additionalIDCount;
	}

	public void setRelationshipIDs(HashMap<String, RelationshipForSample> newRelationshipIDs) {
		this.relationshipIDs.clear();
		for (Entry<String, RelationshipForSample> e : newRelationshipIDs.entrySet()) {
			relationshipIDs.put(e.getKey(), e.getValue());
		}
	}

	public HashMap<Integer, Integer> getModelNodeToDataNode() {
		return modelNodeToDataNode;
	}
	
	public HashMap<Integer, Integer> getDataNodeToModelNode() {
		return dataNodeToModelNode;
	}

	public void addModelNodeToDataNode(int modelNode, int dataNode) {
		this.modelNodeToDataNode.put(modelNode, dataNode);
		this.dataNodeToModelNode.put(dataNode, modelNode);
	}

	public void setModelNodeToDataNode(HashMap<Integer, Integer> newModelNodeToDataNode) {
		this.modelNodeToDataNode.clear();
		this.dataNodeToModelNode.clear();
		for (Entry<Integer, Integer> e : newModelNodeToDataNode.entrySet()) {
			this.modelNodeToDataNode.put(new Integer(e.getKey()), new Integer(e.getValue()));
			this.dataNodeToModelNode.put(new Integer(e.getValue()), new Integer(e.getKey()));
		}
	}

	public int getNextHighestDataNode() {
		int highest = 0;
		for (int dataNode : dataNodeToModelNode.keySet()) {
			if (dataNode >= highest) {
				highest = dataNode;
			}
		}
		return ++highest;
	}

	public HashMap<String, Integer> getDataIDToDataNodeID() {
		return dataIDToDataNodeID;
	}

	public String getIDWithDataNodeID(int dataNode) {
		for (String idKey : dataIDToDataNodeID.keySet()) {
			if (dataIDToDataNodeID.get(idKey) == dataNode) {
				return idKey;
			}
		}
		return null;
	}

	public TreeSet<String> getIDs() {
		return new TreeSet<String>(this.dataIDToDataNodeID.keySet());
	}

	public TreeSet<String> getRelationships() {
		TreeSet<String> relationships = new TreeSet<String>();
		for (RelationshipForSample rfs : this.relationshipIDs.values()) {
			relationships.add(rfs.relationshipID);
		}
		return relationships;
	}
	
	public HashSet<String> getRelationshipsHash() {
		HashSet<String> relationships = new HashSet<String>();
		for (RelationshipForSample rfs : this.relationshipIDs.values()) {
			relationships.add(rfs.relationshipID);
		}
		return relationships;
	}

	public int getMatchingModelNode(int dataNode) {
		if (dataNodeToModelNode.containsKey(dataNode)) {
			return this.dataNodeToModelNode.get(dataNode);
		}
		return -1;
	}

	public double getMismatch() {
		return mismatch;
	}

	public void setMismatch(double mismatch) {
		this.mismatch = mismatch;
	}

	// ///////////////////////////////////////////
	// overwriting methods that compare objects of this type
	// ---equals
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sample otherSample = (Sample) obj;
		if (modelNodeToDataNode == null && otherSample.modelNodeToDataNode == null)
			return true;

		TreeSet<String> dn1 = this.getIDs();
		double mismatch1 = this.getMismatch();
		TreeSet<String> rel1 = this.getRelationships();

		TreeSet<String> dn2 = otherSample.getIDs();
		double mismatch2 = otherSample.getMismatch();
		TreeSet<String> rel2 = otherSample.getRelationships();

		// System.out.println(mismatch1);
		// System.out.println(mismatch1);
		// printSet(dn1);
		// printSet(dn2);
		// printSet(rel1);
		// printSet(rel2);

		if (mismatch1 == mismatch2 && dn1.equals(dn2) && rel1.equals(rel2)) {
			// System.out.println("equal");
			return true;
		}
		// System.out.println("not equal");
		return false;
	}

	// private <T> void printSet(TreeSet<T> set) {
	// for (T t : set) {
	// System.out.println(t);
	// }
	// }

	// ---to string
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		if (modelNodeToDataNode == null)
			return "";

		List<Integer> keyList = new ArrayList<Integer>(modelNodeToDataNode.keySet());
		Collections.sort(keyList);
		for (Integer key : keyList) {
			result.append(key + "_" + modelNodeToDataNode.get(key) + "|");
		}

		return result.toString();
	}

	// ---hashcode
	@Override
	public int hashCode() {
		if (ID != -1) {
			return ID;
		} else {
			String str = toString();
			return (str.isEmpty() ? 0 : str.hashCode());
		}
	}
}
