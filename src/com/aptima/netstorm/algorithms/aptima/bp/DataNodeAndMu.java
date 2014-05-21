/*******************************************************************************
 * Copyright, Aptima, Inc. 2011
 * Unlimited Rights granted to Government per 252.227-7014
 *******************************************************************************/
package com.aptima.netstorm.algorithms.aptima.bp;

import java.io.Serializable;


public class DataNodeAndMu implements Serializable {
	private static final long serialVersionUID = -2158244018143760315L;
	public int DataNode;
	public double Mu;
	
	/**Constructor that sets the index of the datanode of interest and the message, which holds the mismatch information
	 *for this particular data node and a model node
	 * 
	 * @param DataNode			Integer index of the data node
	 * @param Mu				Double mismatch between the attributes of this data node and a model node
	 */
	public DataNodeAndMu(int DataNode, double Mu)
	{
		this.DataNode = DataNode;
		this.Mu = Mu;
	}
	
	/**Overrides the toString method be in the format "DataNode,Mu"
	 * 
	 */
	@Override
	public String toString() {
		return this.DataNode + ", " + this.Mu;
	}
	
	/////////////////////////////////////////////
	//overwriting methods that compare objects of this type
	//---equals
	
	/**Overrides the object equality comparison to returna  boolean if the data node indexes match only
	 * 
	 */
	@Override
	public boolean equals(Object o) 
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DataNodeAndMu equalTest = (DataNodeAndMu) o;
		if(equalTest != null && this.DataNode == equalTest.DataNode/* && this.Mu == equalTest.Mu*/)
			return true;
		return false;
	}
	
	/**Overrides the hashCode method to provide the hashCode of a data node with a particular index
	 * 
	 */
	@Override
	public int hashCode() {
		Integer dN = (Integer)this.DataNode;
		return dN.hashCode();
	}

}