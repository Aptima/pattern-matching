package com.aptima.netstorm.algorithms.aptima.bp.mismatch;

import java.util.HashMap;

import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelNode;
import com.aptima.netstorm.algorithms.aptima.bp.network.AttributedModelRelation;
import com.aptima.netstorm.algorithms.aptima.bp.network.DataAttributeSet;
import com.aptima.netstorm.algorithms.aptima.bp.network.ModelAttributeConstraints;

/**
 * Assigns node and link mismatch values so that the mismatch for the network is calculated as follows:
 * 
 * C_network = C_attribute * num_mismatched_attr + sum(linkNotPresentMismatch)
 * 
 */
public class NormalizedMismatchCalculator {
	/*
	 * Mismatch of 50 = prob of 10^(-22) Mismatch of 10 = prob of 0.00005 Mismatch of 2.3 = prob of 0.1 Mismatch of 1.6
	 * = prob of 0.2 Mismatch of 1.3 = prob of 0.3 Mismatch of 0.9 = prob of 0.4 Mismatch of 0.7 = prob of 0.5 Mismatch
	 * of 0.5 = prob of 0.6 Mismatch of 0.36 = prob of 0.7 Mismatch of 0.22 = prob of 0.8 Mismatch of 0.11 = prob of 0.9
	 * Mismatch of 0.05 = prob of 0.95
	 */

	// Currently assumes all missing links receive the same mismatch. This should be
	// updated in the future. (See MismatchCalculation for Jen's notes.)
	private double linkNotPresentMismatch;

	/**
	 * Mismatch for an incorrect node type.
	 */
	private double incorrectNodeTypeMismatch = 200; // Incorrect node type has a probability of ~0

	/**
	 * Mismatch for each attribute. Each attribute receives the same mismatch so that nodes with more incorrect
	 * attributes are penalized more strongly.
	 */
	private double perAttributeMismatch;

	/**
	 * Map from link ID to the mismatch for an incorrect link type (this varies depending on the model link).
	 */
	private HashMap<Integer, Double> incorrectLinkTypeMismatch;

	private AttributeMismatchManager attributeMismatchManager;

	/**Constructor that initliazes the model nodes and links to compute the normalized mismatch
	 * number of node attributes, link attributes, total number of attributes, penalizations are all set in here
	 * 
	 * @param modelNodes					Array of model nodes					
	 * @param modelRelations				Array of model relations
	 */
	public NormalizedMismatchCalculator(AttributedModelNode[] modelNodes, 
			AttributedModelRelation[] modelRelations) {
		
		attributeMismatchManager = new AttributeMismatchManager();

		int numNodes = modelNodes.length;
		int numLinks = modelRelations.length;

		int numNodeAttributes = 0;
		int numLinkAttributes = 0;

		HashMap<Integer, Integer> numAttributesPerLink = new HashMap<Integer, Integer>(numLinks);

		// First count the number of attributes per node and link.
		for (AttributedModelNode n : modelNodes)
			numNodeAttributes += n.getConstraintSet().getNumSpecifiedContraints();

		for (AttributedModelRelation r : modelRelations) {
			// add 1 for the link type, and 1 for each attribute
			int numForLink = 1;
			numForLink += r.getConstraintSet().getNumSpecifiedContraints();

			numAttributesPerLink.put(r.getId(), numForLink);
			numLinkAttributes += numForLink;
		}

		// perAttributeMismatch is set so that a match with 100% correct structure but 100% incorrect
		// node attributes, link attributes, and link types has a probability of 0.1
		// (e.g. a mismatch of 2.3)
		int numAttributes = numNodeAttributes + numLinkAttributes;
		this.perAttributeMismatch = 2.3 / ((double) numAttributes);

		this.linkNotPresentMismatch = getMissingLinkPenalty(numLinks, numNodes, numNodeAttributes);

		this.incorrectLinkTypeMismatch = new HashMap<Integer, Double>(numLinks);
		for (AttributedModelRelation r : modelRelations)
			incorrectLinkTypeMismatch.put(r.getId(), perAttributeMismatch * ((double) numAttributesPerLink.get(r.getId())));
	}

	/**Method computes the mismatch between a model node and the data attribute set
	 * 
	 * @param modelNode						Model Node of interest
	 * @param dataAttributes				Set of data attributes
	 * @return								Float mismatch value that is the mismatch between a model node and the data attribute set
	 */
	public float ComputeMismatch(AttributedModelNode modelNode, DataAttributeSet dataAttributes) {
		
		if (modelNode.getConstraintSet().getType() != null &&
				!modelNode.getConstraintSet().getType().equals(dataAttributes.getType()))
			return (float) this.incorrectNodeTypeMismatch;

		return (float) getMismatch(modelNode.getConstraintSet(), dataAttributes);
	}
	
	/**Method computes the mismatch between a model link and the data attribute set
	 * 
	 * @param modelRelation					Model link of interest
	 * @param dataAttributes				Set of data attributes
	 * @return								Float mismatch value that is the mismatch between a model node and the data attribute set
	 */
	public float ComputeMismatch(AttributedModelRelation modelRelation, DataAttributeSet dataAttributes) {
		
		if (modelRelation.getConstraintSet().getType() != null &&
				modelRelation.getConstraintSet().getType().equals(AttributeTypeVocab.ANY.toString()))
			return 0; // ANY model relationship type can match with ANY data relation; mismatch = 0;

		if (modelRelation.getConstraintSet().getType() != null &&
				!modelRelation.getConstraintSet().getType().equals(dataAttributes.getType()))
			return this.incorrectLinkTypeMismatch.get(modelRelation.getId()).floatValue();

		return (float) getMismatch(modelRelation.getConstraintSet(), dataAttributes);
	}

	/**
	 * Counts the total number of mismatched attributes. Partial matches receive a value between 0 and 1. Uses
	 * perAttributeMismatch to normalize the mismatches across the model network.
	 * 
	 * @param modelElement					Model attributes				
	 * @param dataElement					Data attributes
	 * @return								Double mismatch value between the two attribute sets
	 */
	private double getMismatch(ModelAttributeConstraints constraintSet, DataAttributeSet attributeSet) {
		double mm = 0;
		for (String name : constraintSet.getAttributeNames()) {

			if (!attributeSet.getAttributeNames().contains(name)) // should never happen.
				throw new RuntimeException(String.format("Expected nodes/links of the same type that share the same names (%s).", name));

			AttributeMismatchProcessor amp = attributeMismatchManager.getMismatchProcessor(constraintSet, name);
			
			//System.out.println("Attribute name: " + name);
			//System.out.println("Attribute value: " + attributeSet.getAttributeValue(name));
			
			mm += amp.getMismatch(constraintSet.getAttributeConstraint(name), attributeSet.getAttributeValue(name));
		}

		return mm * perAttributeMismatch;
	}

	/**Method gets the mismatch value if a link/relation is not present
	 * 
	 * @return						Preset double constant that represents the link not being present
	 */
	public double getLinkNotPresentMismatch() {
		return this.linkNotPresentMismatch;
	}

	/**
	 * This gives the mismatch for a data network that has all of the correct node attributes but is missing all of its
	 * links.
	 * 
	 * Because match probabilities are constrained to be e^(-MM) and the total mismatch for a network is defined to be
	 * the sum of the node and link mismatches, missing links are constrained to affect the probability as e^(-kx). This
	 * means that we have two ways of adjusting how missing links affect the match probability: 1. Adjust k 2. Adjust
	 * the points at which e^(-kx) are sampled.
	 * 
	 * This function is used to calculate a value for k. We select k by assuming that the missing link penalty will be
	 * evenly distributed across the links and selecting the probability, p_ml, that we want to assign to a pattern in
	 * which all nodes are perfect matches and all links are missing. Then, p_ml = e^(-k * numLinks), so k = -ln(p_ml) /
	 * numLinks
	 * 
	 * and the TotalMissingLinkPenalty (returned by this function) is k * numLinks = -ln(p_ml).
	 * 
	 * 
	 */
	private double getTotalMissingLinkPenalty(int numNodes, int numNodeAttributes) {
		double numAttributesPerNode = ((double) numNodeAttributes) / ((double) numNodes);

		// total missing link probability when no node attribute information is available.
		double min_P_ml = 0.1;
		double max_P_ml = 0.6;

		// maximum number of attributes per node that will affect p_ml. If
		// numAttributesPerNode is greater than or equal to this value, p_ml = max_P_ml.
		double maxNumAttributesPerNode = 10.0;

		numAttributesPerNode = Math.min(maxNumAttributesPerNode, numAttributesPerNode);
		double p_ml = (max_P_ml - min_P_ml) * numAttributesPerNode / maxNumAttributesPerNode + min_P_ml;
		return p_ml;
	}

	/**
	 * Returns the mismatch penalty for each missing link, assuming that all missing links are penalized by the same
	 * amount. Future implementations could vary the penalty based on the link's attributes and connectivity.
	 * 
	 * See getTotalMissingLinkPenalty for more information.
	 * 
	 * @param numLinks								Integer that is the number of links
	 * @param numNodes								Integer that is the number of nodes
	 * @param numNodeAttributes						Integer that is the number of attributes
	 * @return										Double that is the mismatch penalty
	 */
	private double getMissingLinkPenalty(int numLinks, int numNodes, int numNodeAttributes) {
		return getTotalMissingLinkPenalty(numNodes, numNodeAttributes) / (double) numLinks;
	}
}