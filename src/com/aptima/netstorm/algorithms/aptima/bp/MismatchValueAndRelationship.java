package com.aptima.netstorm.algorithms.aptima.bp;


public class MismatchValueAndRelationship implements Comparable<MismatchValueAndRelationship> {

	private String relationshipID;
	private double mismatch;
	
	/**Constructor that initalizes a String RelationshipID with a Double mismatch
	 * 
	 * @param mismatch				Double - Mismatch computed between attributes of graph elements
	 * @param relationshipID		String - ID in form of a string for the associated mismatch
	 */
	public MismatchValueAndRelationship(double mismatch, String relationshipID) {
		this.mismatch = mismatch;
		this.relationshipID = relationshipID;
	}
	
	/**Get the ID for the relationship of interest
	 * 
	 * @return						String that is the relationshipID of interest
	 */
	public String getRelationshipID() {
		return relationshipID;
	}
	
	/**Get the mismatch computed between the elements of the graph
	 * 
	 * @return						Double that is the computed mismatch between attributes of the graph elements
	 */
	public double getMismatch(){
		return mismatch;
	}

	/**Compares the String ID if the mismatches are equal and the mismatches otherwise
	 * 
	 * @param						MismatchValueAndRelationship o that should be compared against
	 */
	public int compareTo(MismatchValueAndRelationship o) {
		if(this.mismatch == o.mismatch) {
			return this.relationshipID.compareTo(o.relationshipID);
		}
		else{
			return Double.compare(this.mismatch, o.mismatch);
		}
	}
}