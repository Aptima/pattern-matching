package com.aptima.netstorm.algorithms.aptima.bp;

import java.util.ArrayList;

import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelNode;

public class BPMathModelNode extends AttributedModelNode {
	private static final long serialVersionUID = -8157078466141209092L;
	private ArrayList<DataNodeAndMu> mu;
	private Boolean muSorted = false;
	private double initVal = 1.0;
	
	public BPMathModelNode(int dataCount, AttributedModelNode modelNode) {
		
		this.setId(modelNode.getId());
		this.setConstraintSet(modelNode.getConstraintSet());
		this.setSuccessorRelations(modelNode.getSuccessorRelations());
		this.setPredecessorRelations(modelNode.getPredecessorRelations());
	}
	
	public Boolean getMuSorted() {
		return muSorted;
	}

	public void setMuSorted(Boolean sorted) {
		muSorted = sorted;
	}

	public void setMu(ArrayList<DataNodeAndMu> mu) {
		this.mu = mu;
	}

	public ArrayList<DataNodeAndMu> getMu() {
		return mu;
	}

	public void initMu(int dataCount) {
		muSorted = false;
		mu = new ArrayList<DataNodeAndMu>(dataCount);
		for (int i = 0; i < dataCount; i++) {
			mu.add(new DataNodeAndMu(i, initVal));
		}
	}

	public String muToString() {
		StringBuilder sb = new StringBuilder();
		for (DataNodeAndMu dam : mu)
			sb.append(dam.Mu + "\t");

		return sb.toString();
	}
}
