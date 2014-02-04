package com.aptima.netstorm.algorithms.aptima.bp;

import java.util.ArrayList;

import com.aptima.netstorm.algorithms.aptima.bp.network.AdjacencyListRelation;
import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelRelation;

public class BPMathModelRelation extends AttributedModelRelation {
	private static final long serialVersionUID = 4685194286822447736L;
	private ArrayList<Double> link;
	private ArrayList<Double> linkReverse;

	private AdjacencyListRelation baseRelation;

	private double initVal = 1.0;

	public BPMathModelRelation(AttributedModelRelation baseRelation) {
		this.baseRelation = baseRelation;
		this.link = new ArrayList<Double>();
		this.linkReverse = new ArrayList<Double>();
		
		this.setId(baseRelation.getId());
		this.setFromNode(baseRelation.getFromNode());
		this.setToNode(baseRelation.getToNode());
		this.setConstraintSet(baseRelation.getConstraintSet());
	}	

	public AdjacencyListRelation getBaseRelation() {
		return baseRelation;
	}

	public int getModelID() {
		return this.baseRelation.getId();
	}
	
	public void setLink(ArrayList<Double> link) {
		this.link = link;
	}

	public ArrayList<Double> getLink() {
		return link;
	}

	public void setLinkReverse(ArrayList<Double> linkRelation) {
		this.linkReverse = linkRelation;
	}

	public ArrayList<Double> getLinkReverse() {
		return linkReverse;
	}
	
	public void initLLR(int dataCount)
	{
		link = new ArrayList<Double>(dataCount);
		linkReverse = new ArrayList<Double>(dataCount);
		for (int i = 0; i < dataCount; i++) {
			link.add(initVal);
			linkReverse.add(initVal);
		}
	}
	
	public String LToString()
	{
		return arrToString(this.link);
	}
	
	public String LRToString()
	{
		return arrToString(this.linkReverse);
	}
	
	private static String arrToString(ArrayList<Double> l)
	{
		StringBuilder sb = new StringBuilder();
		for (Double d : l)
			sb.append(d + "\t");
		
		return sb.toString();
	}
}
