package com.aptima.netstorm.algorithms.aptima.bp;

public class MismatchValueAndRelationship implements Comparable<MismatchValueAndRelationship> {

	private String relationshipID;
	private double mismatch;
	
	public MismatchValueAndRelationship(double mismatch, String relationshipID) {
		this.mismatch = mismatch;
		this.relationshipID = relationshipID;
	}
	
	public String getRelationshipID() {
		return relationshipID;
	}
	
	public double getMismatch(){
		return mismatch;
	}

	public int compareTo(MismatchValueAndRelationship o) {
		if(this.mismatch == o.mismatch) {
			return this.relationshipID.compareTo(o.relationshipID);
		}
		else{
			return Double.compare(this.mismatch, o.mismatch);
		}
	}
}
