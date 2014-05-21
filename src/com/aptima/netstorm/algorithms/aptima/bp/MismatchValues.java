package com.aptima.netstorm.algorithms.aptima.bp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import com.aptima.netstorm.algorithms.aptima.bp.sampling.RelationshipForSample;

public class MismatchValues implements RelationshipMismatchProvider {

	/**
	 * Node mismatches indexed by model node m and data node i.
	 * 
	 * @return
	 */
	private HashMap<Integer, HashMap<Integer, Float>> modelDataNodeMismatch;

	/**
	 * Link mismatches between model links m,k and data links i,j indexed as follows: "m,k,i" -> {j->mismatch}, where
	 * the j's are all successors of data node i.
	 */
	private HashMap<String, HashMap<Integer, TreeSet<MismatchValueAndRelationship>>> successorRelationMismatch;

	/**
	 * Link mismatches between model links m,k and data links i,j indexed as follows: "m,k,j" -> {i->mismatch}, where
	 * the i's are all predecessors of data node j.
	 */
	private HashMap<String, HashMap<Integer, TreeSet<MismatchValueAndRelationship>>> predecessorRelationMismatch;

	/**
	 * Default mismatch for missing links
	 */
	public static float defaultLinkNotPresentMismatch = 200.0f;
	public static float defaultNodeNotPresentMismatch = 500.0f;

	private int dataNodeCount;

	public MismatchValues() {
		this.modelDataNodeMismatch = new HashMap<Integer, HashMap<Integer, Float>>();
		this.successorRelationMismatch = new HashMap<String, HashMap<Integer, TreeSet<MismatchValueAndRelationship>>>();
		this.predecessorRelationMismatch = new HashMap<String, HashMap<Integer, TreeSet<MismatchValueAndRelationship>>>();
	}

//	public MismatchValues(ModelNetwork mNet, DataNetwork dNet, MismatchCalculation mCalculator) {
//		this();
//		this.dataNodeCount = dNet.getNodeCount();
//
//		for (BPDataNode dNode : dNet.getNodes())
//			for (ModelNode mNode : mNet.getNodes())
//				addNodeMismatch(mNode.getId(), dNode.getId(), (float) mCalculator.ComputeMismatch(mNode, dNode));
//
//		for (ModelRelation mRel : mNet.getRelations()) {
//			String mk = mRel.getFromNode() + "," + mRel.getToNode();
//			for (BPDataRelation dRel : dNet.getRelations()) {
//				double mValue = mCalculator.ComputeMismatch(mRel, dRel);
//				this.addLinkMismatch(mk, dRel.getFromNode(), dRel.getToNode(), mValue);
//			}
//		}
//	}

	public RelationshipForSample getLowestMismatchRelationshipID(String mkiKey, int j,
			MismatchRelationshipType mismatchRelationshipType) {

		boolean hasMore = false;
		String relationshipID = null;

		switch (mismatchRelationshipType) {
		case Predecessor:
			hasMore = this.predecessorRelationMismatch.get(mkiKey).get(j).size() > 1;
			relationshipID = this.predecessorRelationMismatch.get(mkiKey).get(j).first().getRelationshipID();
			break;
		case Successor:
			hasMore = this.successorRelationMismatch.get(mkiKey).get(j).size() > 1;
			relationshipID = this.successorRelationMismatch.get(mkiKey).get(j).first().getRelationshipID();
		}
		return new RelationshipForSample(relationshipID, hasMore);
	}

	public boolean containsMKIKey(String mkiKey, MismatchRelationshipType mismatchRelationshipType) {
		switch (mismatchRelationshipType) {
		case Predecessor:
			return this.predecessorRelationMismatch.containsKey(mkiKey);
		case Successor:
			return this.successorRelationMismatch.containsKey(mkiKey);
		}
		return false;
	}

	public boolean containsMKIJKey(String mkiKey, int j, MismatchRelationshipType mismatchRelationshipType) {
		switch (mismatchRelationshipType) {
		case Predecessor:
			if (this.predecessorRelationMismatch.containsKey(mkiKey)) {
				return this.predecessorRelationMismatch.get(mkiKey).containsKey(j);
			} else {
				return false;
			}
		case Successor:
			if (this.successorRelationMismatch.containsKey(mkiKey)) {
				return this.successorRelationMismatch.get(mkiKey).containsKey(j);
			} else {
				return false;
			}
		}
		return false;
	}

	public Set<String> getMKIKeys(MismatchRelationshipType mismatchRelationshipType) {
		switch (mismatchRelationshipType) {
		case Predecessor:
			return this.predecessorRelationMismatch.keySet();
		case Successor:
			return this.successorRelationMismatch.keySet();
		}
		return null;
	}

	public Set<Integer> getDataNodesFor(String mkiKey, MismatchRelationshipType mismatchRelationshipType) {
		switch (mismatchRelationshipType) {
		case Predecessor:
			return this.predecessorRelationMismatch.get(mkiKey).keySet();
		case Successor:
			return this.successorRelationMismatch.get(mkiKey).keySet();
		}
		return null;
	}

	public double getLowestMismatch(String mkiKey, int j, MismatchRelationshipType mismatchRelationshipType) {
		switch (mismatchRelationshipType) {
		case Predecessor:
			return this.predecessorRelationMismatch.get(mkiKey).get(j).first().getMismatch();
		case Successor:
			return this.successorRelationMismatch.get(mkiKey).get(j).first().getMismatch();
		}
		return 0.0;
	}

	public void removeDataNode(String mkiKey, int j, MismatchRelationshipType mismatchRelationshipType) {
		switch (mismatchRelationshipType) {
		case Predecessor:
			this.predecessorRelationMismatch.get(mkiKey).remove(j);
			break;
		case Successor:
			this.successorRelationMismatch.get(mkiKey).remove(j);
		}
	}

	public void removeMKIKey(String mkiKey, MismatchRelationshipType mismatchRelationshipType) {
		switch (mismatchRelationshipType) {
		case Predecessor:
			this.predecessorRelationMismatch.remove(mkiKey);
			break;
		case Successor:
			this.successorRelationMismatch.remove(mkiKey);
		}
	}

	public int size(String mkiKey, int j, MismatchRelationshipType mismatchRelationshipType) {
		switch (mismatchRelationshipType) {
		case Predecessor:
			return this.predecessorRelationMismatch.get(mkiKey).get(j).size();
		case Successor:
			return this.successorRelationMismatch.get(mkiKey).get(j).size();
		}
		return 0;
	}

	public HashMap<Integer, Float> getDataNodeToMismatchForModelNode(int i) {
		return this.modelDataNodeMismatch.get(i);
	}

	public float getNodeMismatch(int i, int j) {
		if (!this.modelDataNodeMismatch.containsKey(i))
			return defaultNodeNotPresentMismatch;

		if (!this.modelDataNodeMismatch.get(i).containsKey(j))
			return defaultNodeNotPresentMismatch;

		return this.modelDataNodeMismatch.get(i).get(j);
	}

	/**
	 * 
	 * @param i
	 *            - model node id
	 * @param j
	 *            - data node id
	 * @param mismatch
	 */
	public void addNodeMismatch(int i, int j, float mismatch) {
		if (!this.modelDataNodeMismatch.containsKey(i))
			this.modelDataNodeMismatch.put(i, new HashMap<Integer, Float>());

		this.modelDataNodeMismatch.get(i).put(j, mismatch);
	}

	/**
	 * 
	 * @param i
	 *            model node id
	 * @return true if any of the mismatches for model node i are less than the defaultNodeNotPresentMismatch.
	 */
	public boolean containsNodeMismatchForModelNode(int i) {
		return this.modelDataNodeMismatch.containsKey(i);
	}

	/**
	 * 
	 * @param mk
	 *            - String of the form "m,k" that contains the model node indices
	 * @param i
	 *            - Data node index 1 (corresponding to model node m)
	 * @param j
	 *            - Data node index 2 (corresponding to model node k)
	 * @param mismatch
	 *            - mismatch between link mk and link ij
	 */
	@Deprecated
	public void addLinkMismatch(String mk, int i, int j, double mismatch) {
		addLinkMismatch(mk, i, j, mismatch, "");
	}

	public void addLinkMismatch(String mk, int i, int j, double mismatch, String relationshipID) {
		String predKey_j = mk + "," + j;
		String succKey_i = mk + "," + i;

		addLinkToMismatchMap(this.predecessorRelationMismatch, predKey_j, i, mismatch, relationshipID);
		addLinkToMismatchMap(this.successorRelationMismatch, succKey_i, j, mismatch, relationshipID);
	}

	private void addLinkToMismatchMap(HashMap<String, HashMap<Integer, TreeSet<MismatchValueAndRelationship>>> map, String key1,
			int key2, double mismatch, String relationshipID) {
		if (map.containsKey(key1)) {
			// If there are multiple links from i to j, store the smallest mismatch value (naturally sorted by treeset)
			if (map.get(key1).containsKey(key2)) {

				if (map.get(key1).get(key2).size() >= 2 && mismatch >= map.get(key1).get(key2).first().getMismatch()) {
					return; // already have top 2, no need to update
				}

				// need to populate first two or mismatch is actually lower
				map.get(key1).get(key2).add(new MismatchValueAndRelationship(mismatch, relationshipID));

				LinkedList<MismatchValueAndRelationship> removeList = new LinkedList<MismatchValueAndRelationship>();
				int count = 0;
				for (MismatchValueAndRelationship mvr : map.get(key1).get(key2)) {
					count++;
					if (count > 2) {
						removeList.add(mvr); // take top 2
					}
				}
				for (MismatchValueAndRelationship remove : removeList) {
					map.get(key1).get(key2).remove(remove);
				}
			} else {
				map.get(key1).put(key2, new TreeSet<MismatchValueAndRelationship>()); // init
				map.get(key1).get(key2).add(new MismatchValueAndRelationship(mismatch, relationshipID));
			}
		} else { // list init
			map.put(key1, new HashMap<Integer, TreeSet<MismatchValueAndRelationship>>());
			map.get(key1).put(key2, new TreeSet<MismatchValueAndRelationship>());
			map.get(key1).get(key2).add(new MismatchValueAndRelationship(mismatch, relationshipID));
		}
	}
}
