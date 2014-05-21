package com.aptima.netstorm.algorithms.aptima.bp;

import java.util.ArrayList;

import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelNode;

public class BPMathModelNode extends AttributedModelNode {
	private static final long serialVersionUID = -8157078466141209092L;
	private ArrayList<DataNodeAndMu> mu;
	private Boolean muSorted = false;
	private double initVal = 1.0;
	
	/**Constructor of the ModelNode type that sets:
	 * 1) Unique ID
	 * 2) Optional field that indicates node or relation type.  The type often constrains
	 * the list of attributes for a node or relation.
	 * 3) TODO: What are "successor" relations?
	 * 4) TODO: What are "predecessor" relations?
	 * 
	 * @param dataCount		Number of nodes in the Data Graph
	 * @param modelNode		Model node in the graph that will be turned into a BPMathModel node to perform Belief propagation on it
	 */
	public BPMathModelNode(int dataCount, AttributedModelNode modelNode) {
		
		this.setId(modelNode.getId());
		this.setConstraintSet(modelNode.getConstraintSet());
		this.setSuccessorRelations(modelNode.getSuccessorRelations());
		this.setPredecessorRelations(modelNode.getPredecessorRelations());
	}
	
	/**Method gets a boolean variable that says if the messages should be sorted
	 * 
	 * @return				Boolean variable that sorts the messages if True
	 */
	public Boolean getMuSorted() {
		return muSorted;
	}

	/**Method sets a boolean parameter that if true will sort the messages
	 * 
	 * @param sorted		Boolean argument to have the messages sorted
	 */
	public void setMuSorted(Boolean sorted) {
		muSorted = sorted;
	}
	
	/**Sets the messages, which are mismatch comparisons, between model and all data nodes
	 * 
	 * @param mu			Array list of a message for each data node
	 */
	public void setMu(ArrayList<DataNodeAndMu> mu) {
		this.mu = mu;
	}
	
	/**Gets a list of messages, which are mismatch comparisons, between a model and each data node
	 * 
	 * @return				ArrayList of a message, which is the mismatch comparisons, between the model and all data nodes
	 */
	public ArrayList<DataNodeAndMu> getMu() {
		return mu;
	}

	/**Initilize the list of messages for each data node
	 * 
	 * @param dataCount		Integer number of data nodes within the Data Graph.  
	 * Note that initial messages will be set to 1.0 for each data node.
	 */
	public void initMu(int dataCount) {
		muSorted = false;
		mu = new ArrayList<DataNodeAndMu>(dataCount);
		for (int i = 0; i < dataCount; i++) {
			mu.add(new DataNodeAndMu(i, initVal));
		}
	}

	/**Print the list of messages, which is the mismatch between a model and data node, for each data node to a String
	 * 
	 * @return				String that contains a pair of (DataNodeIndex,Message)
	 */
	public String muToString() {
		StringBuilder sb = new StringBuilder();
		for (DataNodeAndMu dam : mu)
			sb.append(dam.Mu + "\t");

		return sb.toString();
	}
}
