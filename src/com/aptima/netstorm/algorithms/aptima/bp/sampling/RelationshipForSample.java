package com.aptima.netstorm.algorithms.aptima.bp.sampling;

public class RelationshipForSample {

	public String relationshipID;
	public Boolean hasMoreSamples = false;

	public RelationshipForSample() {
		
	}
	
	public RelationshipForSample(String relationshipID, Boolean hasMoreSamples) {
		this.relationshipID = relationshipID;
		this.hasMoreSamples = hasMoreSamples;
	}
}
