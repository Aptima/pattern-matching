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
	
	public DataNodeAndMu(int DataNode, double Mu)
	{
		this.DataNode = DataNode;
		this.Mu = Mu;
	}
	
	@Override
	public String toString() {
		return this.DataNode + ", " + this.Mu;
	}
	
	/////////////////////////////////////////////
	//overwriting methods that compare objects of this type
	//---equals
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
	@Override
	public int hashCode() {
		Integer dN = (Integer)this.DataNode;
		return dN.hashCode();
	}

}
