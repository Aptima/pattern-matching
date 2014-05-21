package com.aptima.netstorm.algorithms.aptima.bp.sampling;

import java.util.ArrayList;

public interface Sampler {
	/**
	 * Returns true if you generate the desired number of samples, false otherwise.
	 */
	public boolean generateSamples();
	
	/**
	 * Returns the generated samples
	 */
	public ArrayList<Sample> getSamples();

}
