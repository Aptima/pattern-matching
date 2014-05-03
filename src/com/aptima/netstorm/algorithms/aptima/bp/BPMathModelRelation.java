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

	/**Constructor takes in parameters of the relation within the graph to make a BPMathModelRelation
	 * so that it can be used in belief propagation (BP).  It sets the following:
	 * 1) Unique ID
	 * 2) From Node
	 * 3) To Node
	 * 4) Optional set of attributes that are associated with this relation
	 * 
	 * @param baseRelation		An AttributedModelRelation which is the actual relation as it is in the Model graph
	 */
	public BPMathModelRelation(AttributedModelRelation baseRelation) {
		this.baseRelation = baseRelation;
		this.link = new ArrayList<Double>();
		this.linkReverse = new ArrayList<Double>();
		
		this.setId(baseRelation.getId());
		this.setFromNode(baseRelation.getFromNode());
		this.setToNode(baseRelation.getToNode());
		this.setConstraintSet(baseRelation.getConstraintSet());
	}	

	/**Method gets the relation from the Model Graph
	 * 
	 * @return				An AttributedModelRelation that holds the information of the link as it is represented in the Model Graph
	 */
	public AdjacencyListRelation getBaseRelation() {
		return baseRelation;
	}
	
	/**Method gets the ID of the relation
	 * 
	 * @return				Integer that is the relation ID
	 */
	public int getModelID() {
		return this.baseRelation.getId();
	}
	
	/**Set the mismatches for the outgoing relations
	 * 
	 * @param link			ArrayList<Double> that sets the mismatches for the outgoing relation
	 */
	public void setLink(ArrayList<Double> link) {
		this.link = link;
	}
	
	/**Get the mismatches between this outgoing model relation and the outgoing Data Graph relations
	 * 
	 * @return				ArrayList<Double> that holds the mismatches computed between the outgoing model relation and Data relations
	 */
	public ArrayList<Double> getLink() {
		return link;
	}
	
	/**Set the mismatches for the incoming relations
	 * 
	 * @param linkRelation	ArrayList<Double> that holds the mismatches computed between the incoming model relation and data relations
	 */
	public void setLinkReverse(ArrayList<Double> linkRelation) {
		this.linkReverse = linkRelation;
	}

	/**Get the mismatches between this incoming model relation and the incoming Data Graph relations
	 * 
	 * @return				ArrayList<Double> that holds the mismatches computed between the model relation and Data relations
	 */
	public ArrayList<Double> getLinkReverse() {
		return linkReverse;
	}
	
	/**Method initializes the incoming and outgoing mismatches to 1.0 for the model relation and all data relations
	 * 
	 * @param dataCount		Integer that holds the  number of relations in the data graph
	 */
	public void initLLR(int dataCount)
	{
		link = new ArrayList<Double>(dataCount);
		linkReverse = new ArrayList<Double>(dataCount);
		for (int i = 0; i < dataCount; i++) {
			link.add(initVal);
			linkReverse.add(initVal);
		}
	}
	
	/**Method converts the outgoing relation information to a string for a specific relation
	 * 
	 * @return				String that holds the outgoing information for a relation
	 */
	public String LToString()
	{
		return arrToString(this.link);
	}
	
	/**Method converts the incoming relation information to a string for a specific relation
	 * 
	 * @return				String that holds the incoming information for a relation
	 */
	public String LRToString()
	{
		return arrToString(this.linkReverse);
	}
	
	/**Private  method that converts a double list to a string
	 * 
	 * @param l				ArrayList<Double> that holds mismatch information
	 * @return				String representation of ArrayList<Double> l
	 */
	private static String arrToString(ArrayList<Double> l)
	{
		StringBuilder sb = new StringBuilder();
		for (Double d : l)
			sb.append(d + "\t");
		
		return sb.toString();
	}
}
