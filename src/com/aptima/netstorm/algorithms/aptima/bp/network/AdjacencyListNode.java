package com.aptima.netstorm.algorithms.aptima.bp.network;

import java.util.ArrayList;

/**
 * Basic network node that stores a list of predecessors and successors with an id.  Other node types
 * should extend this class if they need to store additional algorithm specific information.
 * 
 * The node optionally stores attribute information via an AttributeSet.
 *  
 * @author jroberts
 * 
 */
public class AdjacencyListNode {
	
	private static final long serialVersionUID = 6628598656375113647L;
	private int id;
	private String sID;  // Printable version of the id
	
	private ArrayList<Integer> precedessorRelations = new ArrayList<Integer>();
	private ArrayList<Integer> successorRelations = new ArrayList<Integer>();
	
	public AdjacencyListNode(int id)
	{
		setId(id);
	}
	
	public int getId() {
		return id;
	}
	
	public String getPrintableId() {
		if (sID == null) {
			sID = "" + id;
		}
		return sID;
	}

	protected void setId(int id) {
		this.id = id;
		sID = null; // string version
		getPrintableId();
	}

	public void setSuccessorRelations(ArrayList<Integer> successorRelations) {
		this.successorRelations = successorRelations;
	}

	public ArrayList<Integer> getSuccessorRelations() {
		return successorRelations;
	}

	public void setPredecessorRelations(ArrayList<Integer> precedessorRelations) {
		this.precedessorRelations = precedessorRelations;
	}

	public ArrayList<Integer> getPredecessorRelations() {
		return precedessorRelations;
	}

	public void addPredecessorRelation(int relationId) {
		precedessorRelations.add(relationId);

	}

	public void addSuccessorRelation(int relationId) {
		successorRelations.add(relationId);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		Integer ID = (Integer) id;
		result = /* prime * result + */ID.hashCode();
		/*
		 * result = prime * result + Arrays.hashCode(attributeNames); result = prime * result +
		 * Arrays.hashCode(attributeValues); result = prime result + ((precedessorRelations == null) ? 0 :
		 * precedessorRelations .hashCode()); result = prime result + ((successorRelations == null) ? 0 :
		 * successorRelations .hashCode()); result = prime * result + ((type == null) ? 0 : type.hashCode());
		 */
		return result;
	}

	@Override
	public String toString() {
		String res = "" + id;
		return res;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AdjacencyListNode equalTest = (AdjacencyListNode) o;
		if (equalTest != null && equalTest.id == equalTest.id)
			return true;

		return false;
	}
}
