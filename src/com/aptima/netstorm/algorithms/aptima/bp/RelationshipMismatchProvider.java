package com.aptima.netstorm.algorithms.aptima.bp;

import java.util.Set;

import com.aptima.netstorm.algorithms.aptima.bp.sampling.RelationshipForSample;

public interface RelationshipMismatchProvider {

	public enum MismatchRelationshipType { Predecessor, Successor };
	
	public boolean containsMKIKey(String mkiKey, MismatchRelationshipType mismatchRelationshipType);
	public boolean containsMKIJKey(String mkiKey, int j, MismatchRelationshipType mismatchRelationshipType);
	public Set<Integer> getDataNodesFor(String mkiKey, MismatchRelationshipType mismatchRelationshipType);
	public double getLowestMismatch(String mkiKey, int j, MismatchRelationshipType mismatchRelationshipType);
	public Set<String> getMKIKeys(MismatchRelationshipType mismatchRelationshipType);
	public void removeDataNode(String mkiKey, int j, MismatchRelationshipType mismatchRelationshipType);
	public void removeMKIKey(String mkiKey, MismatchRelationshipType mismatchRelationshipType);
	public int size(String mkiKey, int j, MismatchRelationshipType mismatchRelationshipType);
	public RelationshipForSample getLowestMismatchRelationshipID(String mkiKey, int j, MismatchRelationshipType mismatchRelationshipType);

}
